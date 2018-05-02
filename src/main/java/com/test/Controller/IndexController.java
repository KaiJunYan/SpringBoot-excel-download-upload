package com.test.Controller;

import com.test.Excels.*;
import com.test.Model.CloudServer;
import com.test.Model.ExcelData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    @RequestMapping(value = {"/index", "/"}, method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = {"/error"}, method = RequestMethod.GET)
    public String error() {
        return "error";
    }

    @RequestMapping(value = {"/upload"}, method = RequestMethod.POST)
    public String upload(@RequestParam(value = "file1") MultipartFile file, Model model) throws IOException {
        Map<String, Object> map = new HashMap<>();
        //判断文件是否为空
        if (file == null) {
            map.put("msg", "文件不能为空！");
        }

        //获取文件名
        String fileName = file.getOriginalFilename();

        //验证文件名是否合格
        if (!ImportExcelUtils.validateExcel(fileName)) {
            map.put("msg", "文件必须是excel格式！");
        }

        //进一步判断文件内容是否为空（即判断其大小是否为0或其名称是否为null）
        long size = file.getSize();
        if (StringUtils.isEmpty(fileName) || size == 0) {
            map.put("msg", "文件不能为空！");
        }

        //导入
        ImportExcel.importData(fileName, file, map);
        model.addAttribute("data", map);
        return "index";
    }

    @RequestMapping(value = {"/download"}, method = RequestMethod.GET)
    public void download(HttpServletResponse response) throws Exception {
        CloudServer cloudServer = new CloudServer();
        ExcelData data = new ExcelData();
        List<List<Object>> rows = new ArrayList();
        List<Object> row = new ArrayList();
        Class<? extends CloudServer> aClass = new CloudServer().getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        List<String> titles = new ArrayList<>();
        for (Field f : declaredFields) {
            titles.add(f.getName());
            f.setAccessible(true);
            row.add(f.get(cloudServer));
        }
        data.setTitles(titles);
        rows.add(row);
        data.setRows(rows);
        data.setName("cloudServer");
        ExportExcelUtils.exportExcel(response, "cloudServer.xlsx", data);
    }

}
