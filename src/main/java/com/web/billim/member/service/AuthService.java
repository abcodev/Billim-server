package com.web.billim.member.service;

import com.web.billim.jwt.JwtTokenRedisRepository;
import com.web.billim.jwt.JwtTokenRedisService;
import com.web.billim.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtils jwtUtils;
    private final JwtTokenRedisService jwtTokenRedisService;


    public void logout(long memberId) {
        String token = " ";
        Authentication authentication = jwtUtils.getAuthentication(token);

        if(jwtTokenRedisService.existsById(memberId)){
            jwtTokenRedisService.deleteRefreshToken(memberId);
        }
    }
}
