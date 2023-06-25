package com.web.billim.common.email.service;

import com.web.billim.common.exception.BadRequestException;
import com.web.billim.common.exception.handler.ErrorCode;
import com.web.billim.member.dto.TemporaryPasswordDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String billimEmail;

    @Async
    public void sendMail(String email,String authToken){
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String emailContent =
                    "<html>" +
                            "<head></head>" +
                            "<body>" +
                            "<div style=\"max-width: 600px; margin: 20px auto; font-family: 'NanumSquareNeoBold'; text-align: center;\">" +
                            "      <img src='https://billim.s3.ap-northeast-2.amazonaws.com/common/billim_logo_1.png' alt=\"로고이미지\" style=\"width: 120px; height: 50px;\">" +
                            "" +
                            "      <div style=\"max-width: 600px; margin: 0 auto; padding: 10px 0 20px; background-color: #ffffff;\">" +
                            "        <h2 style=\"font-family: 'NanumSquareNeoExtraBold'; font-size: 24px; margin: 16px 0 32px;\">이메일 인증 안내</h2>" +
                            "        <p style=\"margin-bottom: 20px; font-size: 15px; line-height: 1.5;\">" +
                            "          안녕하세요. Billim 입니다.<br />" +
                            "          회원가입을 위해서는 이메일 인증이 필요합니다.<br />" +
                            "          아래 버튼을 눌러 인증을 완료해주세요!" +
                            "        </p>" +
                            "      </div>" +
                            "" +
                            "      <a href='http:localhost:3000/emailverify/confirm?email="+email+"&authToken="+authToken+"'"+"style=\"display: inline-block; background-color: #fcd34d; font-weight: bolder; color: #ffffff; font-size: 13px; padding: 10px 30px; text-decoration: none; border-radius: 20px;\">이메일 인증</a>" +
                            "" +
                            "      <hr style=\"background: #dee2e6; height: 1px; border: 0; margin: 32px auto 0;\">" +
                            "      <p style=\"font-size: 11px; text-align: initial; padding-left: 13px; color: #868e96;\">만약 버튼이 정상적으로 클릭 되지 않으면, 아래 주소를 복사하여 접속해 주세요.</p>" +
                            "      <p style=\"font-size: 11px; text-align: initial; padding-left: 13px; margin: 0; color: #868e96;\">http://localhost:3000/emailverify/confirm?email=" + email + "&authToken=" + authToken + "</p>" +
                            "    </div>" +
                            "</body>" +
                            "</html>";

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

            String emailContent =

                    "<html>" +
                            "<head></head>" +
                            "<body>" +
                            "   <div style=\"max-width: 600px; margin: 20px auto; font-family: 'NanumSquareNeoBold'; text-align: center;\">" +
                            "      <img src='https://billim.s3.ap-northeast-2.amazonaws.com/common/billim_logo_1.png' alt=\"로고이미지\" style=\"width: 120px; height: 50px;\">" +
                            "      <div style=\"max-width: 600px; margin: 0 auto; padding: 10px 0 20px; background-color: #ffffff;\">" +
                            "        <h2 style=\"font-family: 'NanumSquareNeoExtraBold'; font-size: 24px; margin: 16px 0 32px;\">임시 비밀번호 안내</h2>" +
                            "        <p style=\"margin-bottom: 20px; font-size: 15px; line-height: 1.5;\">" +
                            "          안녕하세요. Billim 입니다.<br />" +
                            "          임시 비밀번호<br />" + tempPassword +
                            "        </p>" +
                            "      </div>" +
                            "    </div>" +
                            "</body>" +
                            "</html>";

            helper.setTo(temporaryPasswordDto.getEmail());
            helper.setSubject("BILLIM 임시 비밀번호 안내");
            helper.setText(emailContent, true);
            helper.setFrom(new InternetAddress("duatjgkr123@gmail.com", "BILLIM"));
            helper.setFrom(new InternetAddress(billimEmail, "BILLIM"));
            javaMailSender.send(message);

        }catch (Exception e){
            System.out.println(e);
            throw new BadRequestException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }

    public String getTempPassword(){
        return UUID.randomUUID().toString().replace("-", "").substring(0,12);

//        SecureRandom random = new SecureRandom();
//        StringBuilder tempPassword = new StringBuilder();
//        for (int i = 0; i < 10; i++) {
//            int rcd = random.nextInt(4);
//            if (rcd == 0) {
//                int randomNum = random.nextInt(10);
//                tempPassword.append(randomNum);
//            } else if (rcd == 1) {
//                char randomChar = (char) (random.nextInt(26) + 65);
//                tempPassword.append(randomChar);
//            } else if (rcd == 2){
//                char randomChar = (char) (random.nextInt(26) + 97);
//                tempPassword.append(randomChar);
//            } else {
//                char randomChar = (char) (random.nextInt(41));
//                tempPassword.append(randomChar);
//            }
//        }
//        return tempPassword.toString();

    }

}
