package com.web.billim.security.jwt.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@AllArgsConstructor
@Getter
@RedisHash(value = "jwtToken", timeToLive = 60*60*24*3)
public class RefreshToken {
    // redis 생명주기를 timeToLive 3일로 지정
    @Id
    private long memberId;

    private String refreshToken;

//    /**
//     * @Indexed -- 해당 필드값으로 데이터를 불러올수 있다.
//     */
//    @Indexed
//    private String accessToken;
}
