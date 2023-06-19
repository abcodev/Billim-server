package com.web.billim.common.email.service;

import com.web.billim.common.exception.BadRequestException;
import com.web.billim.common.exception.handler.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Async
    public void sendMail(String email,String authToken){
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String emailContent = "<div style='margin: 20px;'>" +
                    "<h1>안녕하세요, BILLIM입니다.</h1>" +
                    "<br>" +
                    "<p>해당 링크를 통해 회원가입을 진행해주세요.</p>" +
                    "<br>" +
                    "<p>감사합니다.</p>" +
                    "<br>" +
                    "<div align='center' font-family: verdana;'>" +
                    "<h3 style='color: blue;'>" +
                    "<a href='http://3.36.154.178:8080/member/email/confirm?email=" + email + "&authToken=" + authToken + "'>" +
                    "회원가입 완료하기" +
                    "</a>" +
                    "</h3>" +
                    "</div>" +
                    "</div>";
            helper.setTo("sukil625@naver.com");
            helper.setSubject("BILLIM 이메일 인증링크 입니다");
            helper.setText(emailContent, true);
            helper.setFrom(new InternetAddress("duatjgkr123@gmail.com", "BILLIM"));

            javaMailSender.send(message);
        }catch (Exception e){
            throw new BadRequestException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }
}
