package com.web.billim.member.service;

import com.web.billim.common.email.domain.RedisEmail;
import com.web.billim.common.email.repository.RedisEmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberRedisService {

    private final RedisEmailRepository redisEmailRepository;

    public void saveEmailToken(String email, String authToken) {
        RedisEmail redisEmail = new RedisEmail(email,authToken);
        redisEmailRepository.save(redisEmail);
    }

    public String findByEmail(String email) {
        RedisEmail redisEmail = redisEmailRepository.findById(email)
                .orElseThrow();
        return redisEmail.getVerifyCode();
    }
}
