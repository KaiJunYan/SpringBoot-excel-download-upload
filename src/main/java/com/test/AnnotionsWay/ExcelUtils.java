package com.test.AnnotionsWay;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;


/**
 * 采用反射加注解的方式获得excel表格中的数据
 * https://blog.csdn.net/bk_yzw/article/details/78549497
 * 注意：只做了2003版本的excel导入。如使用2007请使用XSSFWorkbook对象。
 * */

public class ExcelUtils {

    /** HSSFWorkbook对象 **/
    private HSSFWorkbook    workbook    = null;

    /**
     * 方法描述: [获取excel数据接口,返回excel表格数据中的集合.]</br>
     * 初始作者: bk_yzw<br/>
     * 创建日期: 2017年11月16日-上午11:25:17<br/>
     * 开始版本: 2.0.0<br/>
     * =================================================<br/>
     * 修改记录：<br/>
     * 修改作者 日期 修改内容<br/>
     * ================================================<br/>
     *
     * @param in
     *            excel输入流
     * @param clazz
     *            excel 数据对应的实体被映射为 Class 对象的一个类
     * @return
     * @throws IOException
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws InstantiationException
     * @throws IllegalAccessException
     *             List<Object>
     */
    public List<Object> importExcelData(InputStream in, Class<?> clazz) throws IOException, SecurityException,
            NoSuchFieldException, InstantiationException, IllegalAccessException {

        // 获取解析Excel文件POIFSFileSystem类
        POIFSFileSystem fs = new POIFSFileSystem(in);

        return importExcelData(fs, clazz);
    }

    /**
     * 方法描述: [根据excel的输入流创建一个Workbook.]</br>
     * 初始作者: bk_yzw<br/>
     * 创建日期: 2017年11月16日-下午2:21:25<br/>
     * 开始版本: 2.0.0<br/>
     * =================================================<br/>
     * 修改记录：<br/>
     * 修改作者 日期 修改内容<br/>
     * ================================================<br/>
     *
     * @param in
     * @return
     * @throws IOException
     *             HSSFWorkbook
     */
    private HSSFWorkbook getHSSFWorkbook(POIFSFileSystem in) throws IOException {

        // 把一张xls的数据表读到wb里
        return new HSSFWorkbook(in);
    }

    /**
     * 方法描述: [获取表格中的数据.]</br>
     * 初始作者: bk_yzw<br/>
     * 创建日期: 2017年11月16日-上午11:31:24<br/>
     * 开始版本: 2.0.0<br/>
     * =================================================<br/>
     * 修改记录：<br/>
     * 修改作者 日期 修改内容<br/>
     * ================================================<br/>
     *
     * @param in
     *            获取解析Excel文件POIFSFileSystem类
     * @param clazz
     *            excel 数据对应的实体被映射为 Class 对象的一个类
     * @return
     * @throws IOException
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws InstantiationException
     * @throws IllegalAccessException
     *             List<Object>
     */
    private List<Object> importExcelData(POIFSFileSystem in, Class<?> clazz) throws IOException, SecurityException,
            NoSuchFieldException, InstantiationException, IllegalAccessException {

        // 把一张xls的数据表读到wb里
        workbook = getHSSFWorkbook(in);

        // 标记变量，判断整条记录是否为空，并消除全部的空行记录
        StringBuilder sb = new StringBuilder();

        // 获取第一个sheet即sheet1
        HSSFSheet sheet = workbook.getSheetAt(0);
        // 获取第二行
        HSSFRow headerCellRow = sheet.getRow(1);
        // 获取第二行最大列
        Integer cellHeaderNum = Integer.valueOf(headerCellRow.getLastCellNum());
        // 定义列
        HSSFCell dataCell = null;
        // 定义行
        HSSFRow dataRow = null;
        // 定义返回数据
        List<Object> rowList = new ArrayList<Object>();
        // 定义excel字段和实体对应字段map
        Map<String, String> columnMap = new HashMap<String, String>();
        // 获取第一行
        dataRow = sheet.getRow(1);
        // 循环最大列,将每一列属性与所要产生实体的每个字段匹配，组成map
        for (int m = 0; m < cellHeaderNum; m++) {
            String columnNameE = "";
            // 获取第一行每一列描述属性
            switch (dataRow.getCell(m).getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    columnNameE = String.valueOf(dataRow.getCell(m).getNumericCellValue()).trim();
                    break;
                case Cell.CELL_TYPE_STRING:
                    columnNameE =     String.valueOf(dataRow.getCell(m).getRichStringCellValue().toString()).trim();
                    break;
            }

            // === 循环遍历字节码注解 获取属性名称
            // 创建 class的属性数组
            Field[] fields = clazz.getDeclaredFields();
            // 循环该class的属性
            for (Field field : fields) {
                // 判断该属性上的ExcelColumn注解的columnName值是否存在
                if (field.isAnnotationPresent(ExcelColumn.class)) {
                    // 获取该属性上的ExcelColumn的columnName值
                    ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
                    // 回去该属性名称
                    String fieldName = field.getName();
                    // 判断第一行每一列的属性是否与该class上的注解名称匹配
                    if (excelColumn.columnName().trim().equals(columnNameE)) {
                        // 匹配成功则把该字段名称作为value和该列属性作为key放入map中
                        columnMap.put(columnNameE, fieldName);
                    }
                }
            }
        }

        // === 循环遍历数据获取有多道行数据
        Integer rowNum = sheet.getLastRowNum();
        // 这里从第三行开始属于表格中的数据 (根据excel文件定)
        for (int i = 2; i <= rowNum; i++) {
            // 每次循环将标记标量清空
            sb.delete(0, sb.length());
            // 存放行标记
            sb.append(String.valueOf(i));
            // 获取当前行
            dataRow = sheet.getRow(i);
            if (dataRow != null) {
                // 通过反射获取该实体
                Object obj = clazz.newInstance();
                // 循环每一行的列长度
                for (int j = 0; j < cellHeaderNum; j++) {
                    // 获取当前列
                    dataCell = dataRow.getCell(j);
                    // =================================== 读取Excel文件中的数据文本，数值或日期类型的条件判断 开始 =============================
                    if (dataCell != null) {
                        // 初始化每个属性值
                        Object value = "";
                        // 转换数据类型
                        switch (dataCell.getCellType()) {
                            case HSSFCell.CELL_TYPE_NUMERIC:
                                if (HSSFDateUtil.isCellDateFormatted(dataCell)) {
                                    // === 如果是date类型则 ，获取该cell的date值

                                    HSSFDateUtil.getJavaDate(dataCell.getNumericCellValue()).toString();
                                    Date date = dataCell.getDateCellValue();

                                    value = date;
                                } else { // === 纯数字
                                    dataCell.setCellType(Cell.CELL_TYPE_STRING);
                                    value = String.valueOf(dataCell.getRichStringCellValue().toString());
                                }
                                break;

                            case HSSFCell.CELL_TYPE_STRING:
                                value = dataCell.getRichStringCellValue().toString();
                                break;

                            case HSSFCell.CELL_TYPE_FORMULA:
                                // === 读公式计算值
                                value = String.valueOf(dataCell.getNumericCellValue());
                                // === 如果获取的数据值为非法值,则转换为获取字符串
                                if (value.equals("NaN")) {
                                    value = dataCell.getRichStringCellValue().toString();
                                }
                                break;

                            case HSSFCell.CELL_TYPE_BOOLEAN:
                                value = dataCell.getBooleanCellValue();
                                break;

                            case HSSFCell.CELL_TYPE_BLANK:
                                // 这里根据需求自行判断，
                                // 当该列是空的时候，跳出这列当前循环，避免后面塞值，出现类型转换异常
                                continue;

                            case HSSFCell.CELL_TYPE_ERROR:
                                value = "";
                                break;

                            default:
                                value = dataCell.getRichStringCellValue().toString();
                                break;
                        }
                        // 当获得值的时候追加标记变量
                        sb.append(value);

                        // 或其该列所描述的属性
                        String columnNameE = String.valueOf(
                                sheet.getRow(1).getCell(j).getRichStringCellValue().toString()).trim();
                        // 通过map获得对应class中的字段
                        String fieldName = columnMap.get(columnNameE);
                        if (fieldName != "") {
                            // 根据该属性名称获取该类该属性类
                            Field f = obj.getClass().getDeclaredField(fieldName);
                            // 取消java的权限控制检查。
                            f.setAccessible(true);
                            // 对该对象该属性赋值
                            f.set(obj, value);
                        }

                    }
                    // =================================== 读取Excel文件中的数据文本，数值或日期类型的条件判断 结束 =============================
                }
                // 判断这一行是否有数据
                if (sb.toString().equals(String.valueOf(i))) {
                    // 不做操作
                    Collections.emptyList();
                } else {
                    // 添加该条数据
                    rowList.add(obj);
                }
            }

        }

        return rowList;
    }
}