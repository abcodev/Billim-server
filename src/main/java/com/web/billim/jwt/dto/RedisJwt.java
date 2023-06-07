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
@RedisHash(value = "REFRESHTOKEN", timeToLive = 3600000)
public class RedisJwt {
    // redis 생명주기를 timeToLive 3일로 지정
    @Id
    private long memberId;
    private String refreshToken;

//    @Builder
//    public RedisJwt(long memberId, String refreshToken) {
//        this.memberId = memberId;
//        this.refreshToken = refreshToken;
//    }
}
