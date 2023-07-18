package com.web.billim.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@RedisHash(value = "REFREHTOKEN", timeToLive = 300)
public class RedisJwt {
    @Id
    private long memberId;
    private String refreshToken;
}
