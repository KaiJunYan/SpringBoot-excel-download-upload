package com.test.Excels;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils {
    //数字保留一位小数
    private static DecimalFormat Double_One_Formater = new DecimalFormat("0");
    //数字保留两位小数 
    private static DecimalFormat Double_Two_Formater = new DecimalFormat("0.00");
    //转成整数
    private static DecimalFormat intformater = new DecimalFormat("");
    // 默认单元格格式化日期字符串   
    private static SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");

    public static Object getCellType(Cell cell) {
        return cell.getCellType();
    }

    @SuppressWarnings("deprecation")
    public static Object getCellValue(Cell cell) {
        String value = null;
        Object returnValue = null;
        switch (cell.getCellType()) {
            case XSSFCell.CELL_TYPE_STRING:
                returnValue = cell.getStringCellValue().replaceAll(",", "");
                break;
            case XSSFCell.CELL_TYPE_NUMERIC:
                double x = cell.getNumericCellValue();
                if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                    value = Double_One_Formater.format(x).replaceAll(",", "");
                    if (StringUtils.isNotBlank(value)) {
                        returnValue = Double.valueOf(value);
                    }
                } else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                    if (Math.abs(x - Math.round(x)) > 0.0) {
                        value = Double_Two_Formater.format(x).replaceAll(",", "");
                        if (StringUtils.isNotBlank(value)) {
                            returnValue = Double.valueOf(value);
                        }
                    } else {
                        value = intformater.format(x).replaceAll(",", "");
                        if (StringUtils.isNotBlank(value)) {
                            returnValue = Integer.valueOf(value);
                        }
                    }

                } else {
                    returnValue = YYYY_MM_DD.format(HSSFDateUtil.getJavaDate(x));
                }
                break;
            case XSSFCell.CELL_TYPE_BOOLEAN:
                returnValue = Boolean.valueOf(cell.getBooleanCellValue());
                break;
            case XSSFCell.CELL_TYPE_BLANK:
                returnValue = cell.getStringCellValue();
                break;
            default:
                returnValue = cell.getStringCellValue();
        }
        if (returnValue instanceof String && (null == returnValue || "".equals(returnValue) || "null".equals(returnValue))) {
            return null;
        } else {
            return returnValue;
        }

    }

    public static <K, V> Map<K, V> listToMap(List<V> list, String keyMethodName, String valueMethodName, Class<V> c) {
        Map<K, V> map = new HashMap<K, V>();
        if (list != null) {
            try {
                Method methodGetKey = c.getMethod(keyMethodName);
                Method methodGetValue = c.getMethod(valueMethodName);
                for (int i = 0; i < list.size(); i++) {
                    V value = (V) methodGetValue.invoke(list.get(i));
                    K key = (K) methodGetKey.invoke(list.get(i));
                    map.put(key, value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return map;
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if ((obj instanceof List)) {
            return ((List) obj).size() == 0;
        }
        if ((obj instanceof String)) {
            return ((String) obj).trim().equals("");
        }
        return false;
    }

}
