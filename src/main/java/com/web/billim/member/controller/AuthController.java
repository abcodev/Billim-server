package com.web.billim.member.controller;

import com.web.billim.jwt.JwtService;
import com.web.billim.jwt.dto.ReIssueTokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;



    @GetMapping("/test")
    public ResponseEntity<String> test1(){
        String test = "check";
        return ResponseEntity.ok(test);
    }

    @PostMapping("/reIssue/token")
    public ResponseEntity<?> reIssueToken(@RequestBody ReIssueTokenRequest req){

        jwtService.reIssueToken(req);
        return ResponseEntity.ok("a");


        // 1. reIssueRequest (memberId,refreshToken) 으로 받아오기
        // 2. refreshtoken 유효성 검사
        // 3. redis 에 저장된 refreshToken 으로 검증
        // 4. 일치하다면 accessToken , refreshToken 재발급
        // 5. 일치하지 않다면 예외처리



    }

}
