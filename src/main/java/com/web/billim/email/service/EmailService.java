package com.web.billim.email.service;

import com.web.billim.common.exception.DuplicatedException;
import com.web.billim.common.exception.handler.ErrorCode;
import com.web.billim.email.dto.EmailAuthRequest;
import com.web.billim.email.dto.EmailRequest;
import com.web.billim.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class EmailService {

    private final EmailRedisService emailRedisService;
    private final EmailSendService emailSendService;
    private final MemberRepository memberRepository;


    public void certifyEmail(EmailRequest request) {
        validateDuplicated(request.getEmail());
        String authToken = UUID.randomUUID().toString();
        emailRedisService.saveEmailToken(request.getEmail(), authToken);
        emailSendService.sendMail(request.getEmail(), authToken);
    }

    public void validateDuplicated(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicatedException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    public void confirmEmail(EmailAuthRequest emailAuthRequest) {
        if (!emailRedisService.findByEmail(emailAuthRequest.getEmail())
                .equals(emailAuthRequest.getAuthToken())) {
            throw new RuntimeException("인증번호가 일치하지 않습니다.");
        }
    }

}
