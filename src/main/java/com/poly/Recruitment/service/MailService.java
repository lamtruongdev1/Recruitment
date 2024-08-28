package com.poly.Recruitment.service;

import com.poly.Recruitment.dto.MailModel;
import jakarta.mail.MessagingException;

public interface MailService {
    void send(MailModel mailModel) throws MessagingException;
    void send(String to, String subject, String body) throws MessagingException;
    void queue(MailModel mailModel);
}