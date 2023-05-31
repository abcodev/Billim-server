package com.web.billim.jwt;

import com.web.billim.jwt.dto.RedisJwt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtTokenRedisService {

    private final JwtTokenRedisRepository jwtTokenRedisRepository;

    public void saveToken(RedisJwt redisJwt){
        jwtTokenRedisRepository.save(redisJwt.getMemberId(), redisJwt.getRefreshToken());
    }
}
