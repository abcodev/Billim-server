package com.web.billim.jwt.service;

import com.web.billim.exception.JwtException;
import com.web.billim.exception.handler.ErrorCode;
import com.web.billim.jwt.repository.JwtBlackListRepository;
import com.web.billim.jwt.dto.RedisJwt;
import com.web.billim.jwt.repository.JwtTokenRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.web.billim.exception.handler.ErrorCode.INVALID_REFRESH_TOKEN;
import static com.web.billim.exception.handler.ErrorCode.MISMATCH_REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final JwtTokenRedisRepository jwtTokenRedisRepository;

    private final JwtBlackListRepository jwtBlackListRepository;

    public RedisJwt compareToken(String refreshToken, long memberId) {
        RedisJwt redisJwt = jwtTokenRedisRepository.findById(String.valueOf(memberId))
                .orElseThrow(() -> new JwtException(MISMATCH_REFRESH_TOKEN));
        log.info("리프레시 토큰 값: " + redisJwt.getRefreshToken());
        if (!refreshToken.equals(redisJwt.getRefreshToken())) {
            throw new JwtException(INVALID_REFRESH_TOKEN);
        }
        return redisJwt;
    }

    public void saveToken(RedisJwt redisJwt) {
        jwtTokenRedisRepository.save(redisJwt);
    }

    public void deleteRefreshToken(String memberId) {
        jwtTokenRedisRepository.deleteById(memberId);
        log.info("토큰 삭제 완료");
    }

    public boolean existsById(String memberId) {
        return jwtTokenRedisRepository.existsById(memberId);
    }

    public void blackList(String memberId, String accessToken, Date expiration) {
        jwtBlackListRepository.saveBlackList(Long.parseLong(memberId), accessToken, expiration);
    }

    public boolean checkBlackList(String jwt) {
        return jwtBlackListRepository.checkBlackList().contains(jwt);
    }

    public String getRefreshToken(String memberId) {
        RedisJwt redisJwt = jwtTokenRedisRepository.findById(memberId)
                .orElseThrow(() -> new JwtException(ErrorCode.UNKNOWN_ERROR));
        return redisJwt.getRefreshToken();
    }

}
