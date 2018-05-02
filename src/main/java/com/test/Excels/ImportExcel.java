package com.test.Excels;

import com.test.Model.CloudServer;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ImportExcel {

    public static void importData(String fileName, MultipartFile mfile, Map map) {
        File uploadDir = new File("src/main/java/com/test/files/");
        //创建一个目录 （它的路径名由当前 File 对象指定，包括任一必须的父路径。）
        if (!uploadDir.exists()) uploadDir.mkdirs();
        //新建一个文件
        File tempFile = new File("src/main/java/com/test/files/" + new Date().getTime() + ".xlsx");
        if (!tempFile.exists()) {
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //初始化输入流
        InputStream is = null;
        try {
            //将上传的文件写入新建的文件中
            // 在spring boot 内嵌的tomcat中不work
            //mfile.transferTo(tempFile);

            FileUtils.copyInputStreamToFile(mfile.getInputStream(), tempFile);

            //根据新建的文件实例化输入流
            is = new FileInputStream(tempFile);

            //根据版本选择创建Workbook的方式
            Workbook wb = null;
            //根据文件名判断文件是2003版本还是2007版本
            if (ImportExcelUtils.isExcel2007(fileName)) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = new HSSFWorkbook(is);
            }
            //根据excel里面的内容读取知识库信息
            readExcelValue(wb, tempFile, map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void readExcelValue(Workbook wb, File tempFile, Map map) {
        //错误信息接收器
        String errorMsg = "";
        //得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        //得到Excel的行数
        int totalRows = sheet.getPhysicalNumberOfRows();
        //总列数
        int totalCells = 0;
        //得到Excel的列数(前提是有行数)，从第二行算起
        if (totalRows >= 2 && sheet.getRow(1) != null) {
            totalCells = sheet.getRow(1).getPhysicalNumberOfCells();
        }
        List<CloudServer> cloudServerList = new ArrayList<CloudServer>();
        CloudServer cloudServer;

        String br = "\n";
        Row rowHeader = sheet.getRow(0);

        //循环Excel行数,从第二行开始。标题不入库
        for (int r = 1; r < totalRows; r++) {
            String rowMessage = "";
            Row row = sheet.getRow(r);
            if (row == null) {
                errorMsg += br + "第" + (r + 1) + "行数据有问题，请仔细检查！";
                continue;
            }
            cloudServer = new CloudServer();
            //循环Excel的列
            //采用反射的方式判断每列数据并读取数据
            Class<? extends CloudServer> aClass = cloudServer.getClass();
            Field[] declaredFields = aClass.getDeclaredFields();
            for (int c = 0; c < totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    Object cellValue = ExcelUtils.getCellValue(cell);
                    if (cellValue == null || "".equals(cellValue.toString())) {
                        rowMessage += "第" + (c + 1) + "列数据有问题，请仔细检查；";
                    } else {
                        Cell cellHeader = rowHeader.getCell(c);
                        System.out.println((r + 1) + "行" + (c + 1) + "列 ==cellHeader==" + cellHeader + "==cellValue==" + cellValue);
                        for (Field f : declaredFields) {
                            f.setAccessible(true);
                            if (f.getName().equals(String.valueOf(ExcelUtils.getCellValue(cellHeader)))) {
                                if ("ip".equals(f.getName()) && !(StringUtils.isboolIp(cellValue.toString()))) {
                                    rowMessage += "第" + (c + 1) + "列数据不是有效的IP，请仔细检查；";
                                    break;
                                }
                                try {
                                    switch (f.getGenericType().toString()) {
                                        case "class java.lang.String":
                                            f.set(cloudServer, String.valueOf(cellValue));
                                            break;
                                        case "int":
                                            f.set(cloudServer, Integer.valueOf(cellValue.toString()));
                                            break;
                                        default:
                                            break;
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }
                    }
                } else {
                    rowMessage += "第" + (c + 1) + "列数据有问题，请仔细检查；";
                }
            }
            //拼接每行的错误提示
            if (!StringUtils.isEmpty(rowMessage)) {
                errorMsg += br + "第" + (r + 1) + "行，" + rowMessage;
                break;
            } else {
                cloudServerList.add(cloudServer);
            }
        }

        //删除上传的临时文件
        if (tempFile.exists()) {
            tempFile.delete();
        }

        //全部验证通过才导入到数据库
        if (StringUtils.isEmpty(errorMsg)) {
            for (CloudServer server : cloudServerList) {
                //TODO 插入到数据库
                System.out.println(server);
            }
            errorMsg = "导入成功，共" + cloudServerList.size() + "条数据！";
        }
        map.put("cloudServerList", cloudServerList);
        map.put("errorMsg", errorMsg);
    }

}
