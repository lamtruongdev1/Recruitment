package com.poly.Recruitment.service.serviceImplement;

import com.poly.Recruitment.dto.MailModel;
import com.poly.Recruitment.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MailerServiceImpl implements MailService {

    private final JavaMailSender sender;
    private final List<MailModel> list = new ArrayList<>();

    @Override
    public void send(MailModel mail) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        helper.setFrom(mail.getFrom());
        helper.setTo(mail.getTo());
        helper.setSubject(mail.getSubject());
        helper.setText(mail.getContent(), true);
        helper.setReplyTo(mail.getFrom());
        sender.send(message);
    }

    @Override
    public void send(String to, String subject, String body) throws MessagingException {
        this.send(MailModel.builder().to(to).subject(subject).content(body).from("travelbee@gmail.com").build());
    }
    @Override
    public void queue(MailModel mail) {
        list.add(mail);
    }


    @Scheduled(fixedDelay = 1000, initialDelay = 1000)
    public void run() throws MessagingException {
        while (!list.isEmpty()) {
            MailModel mail = list.remove(0);
            this.send(mail);

        }
    }
}