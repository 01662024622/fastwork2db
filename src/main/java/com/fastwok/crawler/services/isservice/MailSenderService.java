package com.fastwok.crawler.services.isservice;

import javax.mail.MessagingException;

public interface MailSenderService {
    public void sendMail(String content) throws MessagingException;
}
