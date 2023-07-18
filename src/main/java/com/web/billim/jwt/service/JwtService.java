package com.web.billim.jwt.service;

import com.web.billim.common.exception.JwtException;
import com.web.billim.jwt.dto.RedisJwt;
import com.web.billim.jwt.JwtTokenRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.web.billim.common.exception.handler.ErrorCode.INVALID_REFRESH_TOKEN;
import static com.web.billim.common.exception.handler.ErrorCode.MISMATCH_REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final JwtTokenRedisRepository jwtTokenRedisRepository;

    public  RedisJwt compareToken(String refreshToken ,long memberId) {
        RedisJwt redisJwt = jwtTokenRedisRepository.findById(String.valueOf(memberId))
                .orElseThrow(()-> new JwtException(MISMATCH_REFRESH_TOKEN));

        log.info("리프레시 토큰 값: "+redisJwt.getRefreshToken());

        if(!refreshToken.equals(redisJwt.getRefreshToken())){
            throw new JwtException(INVALID_REFRESH_TOKEN);
        }
        return redisJwt;
    }

    public void saveToken(RedisJwt redisJwt){
        jwtTokenRedisRepository.save(redisJwt);
    }

    public void deleteRefreshToken(long memberId) {
        jwtTokenRedisRepository.deleteById(String.valueOf(memberId));
        log.info("토큰 삭제 완료");
    }

    public boolean existsById(long memberId) {
        return jwtTokenRedisRepository.existsById(String.valueOf(memberId));
    }
}
