package com.web.billim.member.controller;

import com.web.billim.jwt.JwtService;
import com.web.billim.jwt.dto.ReIssueTokenRequest;
import com.web.billim.member.dto.response.ReIssueTokenResponse;
import com.web.billim.member.service.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 인증", description = "AuthController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final MemberService memberService;

//    @ApiOperation(value = "accessToken 재발급",notes = "accessToken 만료 응답 받았을시, refreshToken 을 통해 accessToken 을 재발급 요청")
    @PostMapping("/reIssue/token")
    public ResponseEntity<?> reIssueToken(@RequestBody ReIssueTokenRequest req){
        String accessToken = req.getAccessToken();
        String refreshToken = req.getRefreshToken();
        ReIssueTokenResponse response = jwtService.reIssueToken(accessToken,refreshToken);
        return ResponseEntity.ok(response);
    }

//    @ApiOperation(value = "로그아웃")
    @PostMapping
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal long memberId
    ){
        memberService.logout(memberId);
        return ResponseEntity.ok().build();
    }

    // 로그아웃

}
