package com.web.billim.jwt.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
@Repository
@RequiredArgsConstructor
public class JwtBlackListRepository {
    private final static String BLACKLIST_REDIS_KEY = "JWT_BLACK_LIST";

    private final RedisTemplate<String, String> redisTemplate;

    public void saveBlackList(long memberId, String accessToken, Date expiration) {
        redisTemplate.opsForHash().put(BLACKLIST_REDIS_KEY, memberId, accessToken);

        Instant instantExpiration = expiration.toInstant();
        Instant now = Instant.now();
        long remainingTimeInSeconds = now.until(instantExpiration, java.time.temporal.ChronoUnit.SECONDS);
        redisTemplate.expire(BLACKLIST_REDIS_KEY, remainingTimeInSeconds, TimeUnit.SECONDS);
    }
    public List<Object> checkBlackList() {
        List<Object> accessTokens = redisTemplate.opsForHash().values(BLACKLIST_REDIS_KEY);
        return accessTokens;
    }
}
