package com.web.billim.email.controller;

import com.web.billim.email.dto.EmailAuthRequest;
import com.web.billim.email.dto.EmailRequest;
import com.web.billim.email.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "이메일", description = "EmailController")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    @Operation(summary ="이메일인증 링크 발송", description = "해당 이메일에 인증 링크 발송")
    @PostMapping("/send")
    public ResponseEntity<Void> sendEmail(@RequestBody EmailRequest request) {
        emailService.certifyEmail(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "이메일인증 코드 확인", description = "클라이언트가 링크를 클릭시 해당 APi로 연결")
    @PostMapping("/confirm")
    public ResponseEntity<Void> confirmEmail(@RequestBody EmailAuthRequest emailAuthRequest) {
        emailService.confirmEmail(emailAuthRequest);
        return ResponseEntity.ok().build();
    }
}
