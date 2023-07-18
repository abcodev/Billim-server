package com.web.billim.member.controller;

import com.web.billim.jwt.service.JwtService;
import com.web.billim.jwt.dto.ReIssueTokenRequest;
import com.web.billim.member.dto.response.ReIssueTokenResponse;
import com.web.billim.member.service.AuthService;
import com.web.billim.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 인증", description = "AuthController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final AuthService authService;

    @Operation(summary = "토큰 재발급", description = "refresh token 유효성 검사후 access token 재발급")
    @PostMapping("/reIssue/token")
    public ResponseEntity<?> reIssueToken(@RequestBody ReIssueTokenRequest req){
        ReIssueTokenResponse response = jwtService.reIssueToken(req);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "* 로그아웃")
    @PostMapping
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal long memberId
    ){
        authService.logout(memberId);
        return ResponseEntity.ok().build();
    }


}
