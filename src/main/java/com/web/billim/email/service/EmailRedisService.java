package com.web.billim.email.service;

import com.web.billim.email.domain.EmailRedis;
import com.web.billim.email.repository.EmailRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailRedisService {

    private final EmailRedisRepository redisEmailRepository;

    public void saveEmailToken(String email, String authToken) {
        EmailRedis redisEmail = new EmailRedis(email,authToken);
        redisEmailRepository.save(redisEmail);
    }

    public String findByEmail(String email) {
        EmailRedis redisEmail = redisEmailRepository.findById(email)
                .orElseThrow();
        return redisEmail.getVerifyCode();
    }

}
