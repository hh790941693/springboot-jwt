package com.pjb.springbootjwt.common.uploader.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    /**
     * 上传文件
     *
     * @param file       文件对象
     * @param category     子目录名
     * @return
     * @throws Exception
     */
    String uploadFile(MultipartFile file, String category) throws Exception;

    /**
     * 检查用户上传容量是否超出
     * @param
     * @return
     * @throws Exception
     */
    boolean checkUploadCapacity() throws Exception;
}
