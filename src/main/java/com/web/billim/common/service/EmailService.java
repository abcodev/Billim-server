package com.web.billim.common.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor

public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendMail(String email,String authToken){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("BILLIM");
        message.setText("http://localhost:8080/member/confirm/email="+email+"&authToken="+authToken);
        javaMailSender.send(message);
    }
}
