package com.web.billim.chat.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatConnectRepository {
    private final RedisTemplate<Long, Long> chatRedisTemplate;
}
