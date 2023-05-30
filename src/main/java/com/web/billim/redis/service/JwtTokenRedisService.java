package com.web.billim.redis.service;

import com.web.billim.redis.JwtTokenRedisRepository;
import com.web.billim.security.jwt.domain.RedisJwt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtTokenRedisService {

    private final JwtTokenRedisRepository jwtTokenRedisRepository;

    public void saveToken(RedisJwt redisJwt){
        jwtTokenRedisRepository.save(redisJwt.getMemberId(), redisJwt.getRefreshToken());
    }
}
