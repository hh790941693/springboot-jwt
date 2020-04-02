package com.pjb.springbootjwt.common.uploader.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    /**
     * 上传文件
     *
     * @param file       文件对象
     * @param folder     子目录名
     * @param userId     用户id
     * @return
     * @throws Exception
     */
    String uploadFile(MultipartFile file, String folder, String userId) throws Exception;
}
