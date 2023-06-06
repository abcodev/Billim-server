package com.web.billim.jwt;

import com.web.billim.jwt.dto.RedisJwt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenRedisService {

    private final JwtRepositryTest jwtRepositryTest;

    public  RedisJwt compareToken(long memberId) {
        RedisJwt redisJwt = jwtRepositryTest.findById(String.valueOf(memberId))
                .orElseThrow();
        log.info("리프레시 트큰 값: "+redisJwt.getRefreshToken());
        return redisJwt;
    }

    public void saveToken(RedisJwt redisJwt){
        jwtRepositryTest.save(redisJwt);
    }

}
