package com.pjb.springbootjwt.common.oss.alibaba;

import com.pjb.springbootjwt.common.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 阿里云oss文件上传存储
 */
@Controller
@RequestMapping("/api/ali")
public class AliOssController {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(AliOssController.class);

    /**
     * 文件上传
     * @param file
     */
    @RequestMapping(value = "upload",method = RequestMethod.POST)
    @ResponseBody
    public Result uploadBlog(@RequestParam(required = true)MultipartFile file,
                             @RequestParam(required = false)Integer userId,
                             @RequestParam(required = false)String folder){
        logger.info("进入文件上传控制层, file:{} userId:{} folder:{}", file, userId, folder);
        String uploadUrl = "";
        try {
            if(null != file){
                String filename = file.getOriginalFilename();
                logger.info("filename:{}", filename);
                if(StringUtils.isNotBlank(filename)){
                    File newFile = new File(filename);
                    FileOutputStream os = new FileOutputStream(newFile);
                    os.write(file.getBytes());
                    os.close();
                    file.transferTo(newFile);
                    //上传到OSS
                    uploadUrl = AliOssUtil.upload(newFile);
                    if (StringUtils.isNotBlank(uploadUrl)){
                        logger.info("oss文件访问地址:{}", uploadUrl);
                        return Result.ok(uploadUrl);
                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
            logger.info("上传文件异常:{}", ex.getMessage());
        }

        return Result.fail();
    }
}
