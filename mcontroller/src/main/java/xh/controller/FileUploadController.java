package xh.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author xiehuang
 * @date 2022/05/06 3:23
 */
@RestController
@RequestMapping("upload")
public class FileUploadController {
    @Value("${web.upload-path}")
    private String uploadPath;

    @RequestMapping(value = "uploadFile", method = RequestMethod.POST)
    public String uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        File folder = new File(uploadPath);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        String newName = file.getOriginalFilename();
        File[] files = folder.listFiles();
        for (File item : files) {
            // 已经存在相同文件名的文件，则对其重命名
            if (item.getName().equals(file.getOriginalFilename())) {
                String oldName = file.getOriginalFilename();
                newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."), oldName.length());
                break;
            }
        }
        try {
            file.transferTo(new File(folder, newName));
            String filePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/" + newName;
            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传失败！";
    }

    @RequestMapping(value = "getData", method = RequestMethod.GET)
    public List<Map<String, String>> getData(HttpServletRequest request) {
        File file = new File(uploadPath);
        List<Map<String, String>> result = new ArrayList<>();
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            String filePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/" + files[i].getName();
            Map<String, String> item = new HashMap<>();
            item.put("name", files[i].getName());
            item.put("path", filePath);
            result.add(item);
        }
        return result;
    }
}
