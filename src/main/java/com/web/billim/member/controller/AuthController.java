package com.web.billim.member.controller;

import com.web.billim.jwt.JwtService;
import com.web.billim.jwt.dto.ReIssueTokenRequest;
import com.web.billim.member.dto.response.ReIssueTokenResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;

    @ApiOperation(value = "accessToken 재발급",notes = "accessToken 만료 응답 받았을시, refreshToken 을 통해 accessToken 을 재발급 요청")
    @PostMapping("/reIssue/token")
    public ResponseEntity<?> reIssueToken(@RequestBody ReIssueTokenRequest req){
        String accessToken = req.getAccessToken();
        String refreshToken = req.getRefreshToken();
        ReIssueTokenResponse response = jwtService.reIssueToken(accessToken,refreshToken);
        return ResponseEntity.ok(response);
    }
}
