package com.web.billim.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@Getter
@RedisHash(value = "jwtToken", timeToLive = 12000)
public class RedisJwt {
    // redis 생명주기를 timeToLive 3일로 지정
    @Id
    private long memberId;
    private String refreshToken;
}
