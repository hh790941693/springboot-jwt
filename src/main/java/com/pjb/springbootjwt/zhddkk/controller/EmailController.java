package com.pjb.springbootjwt.zhddkk.controller;

import com.pjb.springbootjwt.zhddkk.base.Result;
import com.pjb.springbootjwt.zhddkk.dto.EmailDTO;
import com.pjb.springbootjwt.zhddkk.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @RequestMapping("")
    public String emailPage() {
        return "ws/email";
    }

    @RequestMapping("sendSimpleEmail.do")
    @ResponseBody
    public Result<String> sendSimpleEmail(@Validated EmailDTO emailDTO) {
        return emailService.sendSimpleMail(emailDTO.getTo(), emailDTO.getSubject(), emailDTO.getContent());
    }

    @RequestMapping("sendHtmlMail.do")
    @ResponseBody
    public Result<String> sendHtmlMail(@Validated EmailDTO emailDTO) {
        return emailService.sendHtmlMail(emailDTO.getTo(), emailDTO.getSubject(), emailDTO.getContent());
    }

    @RequestMapping("sendAttachmentsMail.do")
    @ResponseBody
    public Result<String> sendAttachmentsMail(@Validated EmailDTO emailDTO) {
        return emailService.sendAttachmentsMail(emailDTO.getTo(), emailDTO.getSubject(), emailDTO.getContent(), emailDTO.getFilePath());
    }
}
