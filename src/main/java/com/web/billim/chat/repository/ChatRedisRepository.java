package com.web.billim.chat.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRedisRepository {

    private final RedisTemplate<String, Long> longRedisTemplate;

    public void push(long memberId, long chatRoomId) {
        longRedisTemplate.opsForList().leftPush("CONNECTED_MEMBER:" + memberId, chatRoomId);
    }

    public void remove(long memberId, long chatRoomId) {
        longRedisTemplate.opsForList().remove("CONNECTED_MEMBER:" + memberId, 0, chatRoomId);
    }
}
