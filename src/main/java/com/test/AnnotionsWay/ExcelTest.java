package com.test.AnnotionsWay;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class ExcelTest {

    //test
    public static void testExcelUtils() {
        String xlsPath = "src/main/java/com/test/files/test.xls";
        // 根据文件路径创建文件输入流
        InputStream in;
        try {
            in = new FileInputStream(xlsPath);
            List<Object> resultList = new ExcelUtils().importExcelData(in, EmployeeParam.class);


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
