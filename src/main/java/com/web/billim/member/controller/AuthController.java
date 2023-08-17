package com.web.billim.member.controller;

import com.web.billim.jwt.dto.ReIssueTokenRequest;
import com.web.billim.member.dto.response.ReIssueTokenResponse;
import com.web.billim.member.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 인증", description = "AuthController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";
    private final AuthService AuthService;

    @Operation(summary = "토큰 재발급", description = "refresh token 유효성 검사후 access token 재발급")
    @PostMapping("/reIssue/token")
    public ResponseEntity<ReIssueTokenResponse> reIssueToken(@RequestBody ReIssueTokenRequest req){
        ReIssueTokenResponse response = AuthService.reIssueToken(req);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "로그아웃")
    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(name = "Authorization") String authorization) {
        String accessToken = authorization.substring(AUTHORIZATION_HEADER_PREFIX.length());
        AuthService.logout(accessToken);
        return ResponseEntity.ok().build();
    }
}
