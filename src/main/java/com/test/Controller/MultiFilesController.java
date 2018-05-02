package com.test.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class MultiFilesController {

    @PostMapping("/file/uploads")
    public void upload(@RequestParam("upload_files") MultipartFile[] uploadingFiles) throws IOException {
        for (MultipartFile uploadedFile : uploadingFiles) {
            File file = new File("src/main/java/com/test/files/" + uploadedFile.getOriginalFilename());
            uploadedFile.transferTo(file);
        }
    }
}
