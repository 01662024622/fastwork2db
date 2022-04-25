package com.fastwok.crawler.services.impl;

import com.fastwok.crawler.services.isservice.MailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailSenderServiceImpl implements MailSenderService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(String content) throws MessagingException {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper  helper = new MimeMessageHelper(msg, true);
        helper.setSubject("Create Customer");
        helper.setTo("bi@htauto.com.vn");
        helper.setText(content,true);
        javaMailSender.send(msg);
    }
}
