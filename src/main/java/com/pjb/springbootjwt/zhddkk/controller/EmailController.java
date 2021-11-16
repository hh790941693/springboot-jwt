package com.pjb.springbootjwt.zhddkk.controller;

import com.pjb.springbootjwt.zhddkk.base.Result;
import com.pjb.springbootjwt.zhddkk.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @RequestMapping("sendSimpleEmail.do")
    @ResponseBody
    public Result<String> sendSimpleEmail(String to, String subject, String content) {
        return emailService.sendSimpleMail(to, subject, content);
    }

    @RequestMapping("sendHtmlMail.do")
    @ResponseBody
    public Result<String> sendHtmlMail(String to, String subject, String content) {
        return emailService.sendHtmlMail(to, subject, content);
    }

    @RequestMapping("sendAttachmentsMail.do")
    @ResponseBody
    public Result<String> sendAttachmentsMail(String to, String subject, String content, String filePath) {
        return emailService.sendAttachmentsMail(to, subject, content, filePath);
    }
}
