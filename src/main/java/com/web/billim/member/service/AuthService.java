package com.web.billim.member.service;

import com.web.billim.jwt.JwtTokenRedisRepository;
import com.web.billim.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtils jwtUtils;
    private final JwtTokenRedisRepository jwtTokenRedisRepository;


    public void logout(long memberId) {
        String token = " ";
        Authentication authentication = jwtUtils.getAuthentication(token);

        if(jwtTokenRedisRepository.existsById(String.valueOf(memberId))){
            jwtTokenRedisRepository.deleteById(String.valueOf(memberId));
        }
    }
}
