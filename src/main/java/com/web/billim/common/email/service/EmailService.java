package com.web.billim.common.email.service;

import com.web.billim.common.exception.BadRequestException;
import com.web.billim.common.exception.handler.ErrorCode;
import com.web.billim.member.dto.TemporaryPasswordDto;
import com.web.billim.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;
import java.util.UUID;

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

            helper.setTo(email);
            helper.setSubject("BILLIM 이메일 인증링크 입니다");
            helper.setText(emailContent, true);
            helper.setFrom(new InternetAddress("duatjgkr123@gmail.com", "BILLIM"));

            javaMailSender.send(message);
        }catch (Exception e){
            System.out.println(e);
            throw new BadRequestException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }

    @Transactional
    public void sendTempPassword(TemporaryPasswordDto temporaryPasswordDto) {

        String tempPassword = getTempPassword();

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String emailContent = "<div style='margin: 20px;'>" +
                    "<h1>안녕하세요, BILLIM입니다.</h1>" +
                    "<br>" +
                    "<p>임시 비밀번호</p>" +
                    "<br>" +
                    "<p>"+ tempPassword +"</p>" +
                    "</div>" +
                    "</div>";

            helper.setTo(temporaryPasswordDto.getEmail());
            helper.setSubject("BILLIM 임시비밀번호 입니다");
            helper.setText(emailContent, true);
            helper.setFrom(new InternetAddress("duatjgkr123@gmail.com", "BILLIM"));
            javaMailSender.send(message);

        }catch (Exception e){
            System.out.println(e);
            throw new BadRequestException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }

    public String getTempPassword(){
//        return UUID.randomUUID().toString().replace("-", "").substring(0,10);

//        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
//                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
//
//        StringBuilder tempPassword = new StringBuilder();
//
//        int idx = 0;
//        for (int i = 0; i < 10; i++) {
//            idx = (int) (charSet.length * Math.random());
//            tempPassword.append(charSet[idx]);
//        }

        Random random = new Random();
        StringBuilder tempPassword = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int rcd = random.nextInt(4);
            if (rcd == 0) {
                int randomNum = random.nextInt(10);
                tempPassword.append(randomNum);
            } else if (rcd == 1) {
                char randomChar = (char) (random.nextInt(26) + 65);
                tempPassword.append(randomChar);
            } else if (rcd == 2){
                char randomChar = (char) (random.nextInt(26) + 97);
                tempPassword.append(randomChar);
            } else {
                char randomChar = (char) (random.nextInt(41));
                tempPassword.append(randomChar);
            }
        }
        return tempPassword.toString();

    }

}
