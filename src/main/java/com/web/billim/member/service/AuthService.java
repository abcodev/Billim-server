package com.web.billim.member.service;

import com.web.billim.jwt.service.JwtTokenRedisService;
import com.web.billim.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final JwtTokenRedisService jwtTokenRedisService;


    public void logout(long memberId) {
        String token = " ";
        Authentication authentication = jwtProvider.getAuthentication(token);

        if(jwtTokenRedisService.existsById(memberId)){
            jwtTokenRedisService.deleteRefreshToken(memberId);
        }
    }
}
