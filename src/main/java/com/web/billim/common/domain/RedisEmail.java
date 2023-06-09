package com.web.billim.common.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@RedisHash(value = "Email", timeToLive = 12000)
public class RedisEmail {
    @Id
    private String email;
    private String verifyCode;
}
