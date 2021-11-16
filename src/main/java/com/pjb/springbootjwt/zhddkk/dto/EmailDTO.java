package com.pjb.springbootjwt.zhddkk.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class EmailDTO {

    @Email
    private String to;

    @NotEmpty
    private String subject;

    private String content;

    private String filePath;

    private MultipartFile attachment;
}
