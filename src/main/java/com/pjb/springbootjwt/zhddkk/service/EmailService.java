package com.pjb.springbootjwt.zhddkk.service;

import com.pjb.springbootjwt.zhddkk.base.Result;
import org.springframework.web.multipart.MultipartFile;

public interface EmailService {

    /**
     * 发送文本邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     */
    Result<String> sendSimpleMail(String to, String subject, String content);

    /**
     * 发送HTML邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     */
    Result<String> sendHtmlMail(String to, String subject, String content);

    /**
     * 发送带附件的邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @param filePath 附件
     */
    Result<String> sendAttachmentsMail(String to, String subject, String content, String filePath);

    /**
     * 发送带附件的邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @param filePath 附件
     */
    Result<String> sendAttachmentsMail(String to, String subject, String content, MultipartFile attachment);
}
