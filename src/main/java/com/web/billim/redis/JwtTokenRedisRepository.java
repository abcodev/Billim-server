package com.web.billim.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JwtTokenRedisRepository {
    private final RedisTemplate<String, String> redisTemplate;


    private static final String KEY_PREFIX = "REFRESH_TOKEN:";

    public void save(long memId, String refreshToken){
        redisTemplate.opsForValue().set(KEY_PREFIX+memId, refreshToken);
    }
    public Optional<String> find(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(KEY_PREFIX + key));
    }
}

