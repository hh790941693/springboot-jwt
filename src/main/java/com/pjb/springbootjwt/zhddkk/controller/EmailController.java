package com.pjb.springbootjwt.zhddkk.controller;

import com.pjb.springbootjwt.zhddkk.base.Result;
import com.pjb.springbootjwt.zhddkk.dto.EmailDTO;
import com.pjb.springbootjwt.zhddkk.service.EmailService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("sendEmail.do")
    @ResponseBody
    public Result<String> sendEmail(@Validated EmailDTO emailDTO) {
        if (null != emailDTO.getAttachment() && StringUtils.isNotBlank(emailDTO.getAttachment().getOriginalFilename())) {
            // 附件邮件
            return emailService.sendAttachmentsMail(emailDTO.getTo(), emailDTO.getSubject(), emailDTO.getContent(), emailDTO.getAttachment());
        } else {
            if (StringUtils.isNotBlank(emailDTO.getContent()) && emailDTO.getContent().contains("<") && emailDTO.getContent().contains(">")) {
                // H5邮件
                return emailService.sendHtmlMail(emailDTO.getTo(), emailDTO.getSubject(), emailDTO.getContent());
            } else {
                // 普通邮件
                return emailService.sendSimpleMail(emailDTO.getTo(), emailDTO.getSubject(), emailDTO.getContent());
            }
        }
    }

    /**
     * 发送普通邮件
     * @param emailDTO
     * @return
     */
    @PostMapping("sendSimpleEmail.do")
    @ResponseBody
    public Result<String> sendSimpleEmail(@Validated EmailDTO emailDTO) {
        return emailService.sendSimpleMail(emailDTO.getTo(), emailDTO.getSubject(), emailDTO.getContent());
    }

    /**
     * 发送H5邮件
     * @param emailDTO
     * @return
     */
    @PostMapping("sendHtmlMail.do")
    @ResponseBody
    public Result<String> sendHtmlMail(@Validated EmailDTO emailDTO) {
        return emailService.sendHtmlMail(emailDTO.getTo(), emailDTO.getSubject(), emailDTO.getContent());
    }

    /**
     * 发送包含附件的邮件
     * @param emailDTO
     * @return
     */
    @PostMapping("sendAttachmentsMail.do")
    @ResponseBody
    public Result<String> sendAttachmentsMail(@Validated EmailDTO emailDTO) {
        return emailService.sendAttachmentsMail(emailDTO.getTo(), emailDTO.getSubject(), emailDTO.getContent(), emailDTO.getAttachment());
    }
}
