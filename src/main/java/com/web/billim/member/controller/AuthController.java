package com.web.billim.member.controller;

import com.web.billim.jwt.JwtService;
import com.web.billim.jwt.dto.ReIssueTokenRequest;
import com.web.billim.member.dto.response.ReIssueTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;

    @PostMapping("/reIssue/token")
    public ResponseEntity<?> reIssueToken(@RequestBody ReIssueTokenRequest req){
        String accessToken = req.getAccessToken();
        String refreshToken = req.getRefreshToken();
        ReIssueTokenResponse response = jwtService.reIssueToken(accessToken,refreshToken);
        return ResponseEntity.ok(response);
    }
}
