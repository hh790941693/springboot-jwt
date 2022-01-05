package com.pjb.springbootjwt.common.oss.qiniu;

import com.google.gson.Gson;
import com.pjb.springbootjwt.common.vo.Result;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/api/qiniu")
public class QiniuController {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(QiniuController.class);

    @Autowired
    private QiniuConfig qiniuConfig;

    @RequestMapping("/page")
    public String uploadPage() {
        return "ws/upload";
    }

    @PostMapping("/upload")
    @ResponseBody
    public Result qiNiuYunUpload(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        String filename = file.getOriginalFilename();
        FileInputStream inputStream = (FileInputStream) file.getInputStream();
        //为文件重命名：uuid+filename
        filename = UUID.randomUUID() + filename;
        String link = uploadImgToQiNiu(inputStream, filename);
        model.addAttribute("link",link);
        System.out.println(link);
        return Result.ok(link);
    }

    private String uploadImgToQiNiu(FileInputStream file, String filename)  {
        // 构造一个带指定Zone对象的配置类，注意后面的zone各个地区不一样的
        Configuration cfg = new Configuration();
        cfg.useHttpsDomains = false;
        UploadManager uploadManager = new UploadManager(cfg);
        // 生成密钥
        Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());
        try {
            String upToken = auth.uploadToken(qiniuConfig.getBucket());
            try {
                Response response = uploadManager.put(file, filename, upToken, null, null);
                // 解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                // 这里要改成自己的外链域名
                String returnPath= qiniuConfig.getDomainPath() + File.separator + putRet.key;
                return returnPath;
            } catch (QiniuException ex) {
                Response r = ex.response;
                logger.error(r.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
