package com.web.billim.jwt;

import com.web.billim.common.domain.RedisEmail;
import com.web.billim.common.handler.TokenExpiredException;
import com.web.billim.common.repository.RedisEmailRepository;
import com.web.billim.jwt.dto.RedisJwt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.web.billim.common.handler.ErrorCode.MISMATCH_REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenRedisService {

    private final JwtTokenRedisRepository jwtTokenRedisRepository;
    private final RedisEmailRepository redisEmailRepository;

    public  RedisJwt compareToken(long memberId) {
        RedisJwt redisJwt = jwtTokenRedisRepository.findById(String.valueOf(memberId))
                .orElseThrow(()-> new TokenExpiredException(MISMATCH_REFRESH_TOKEN));
        log.info("리프레시 트큰 값: "+redisJwt.getRefreshToken());
        return redisJwt;
    }
    public void saveToken(RedisJwt redisJwt){
        jwtTokenRedisRepository.save(redisJwt);
    }

    public void deleteRefreshToken(long memberId) {
        jwtTokenRedisRepository.deleteById(String.valueOf(memberId));
        log.info(memberId+"토큰 삭제 완료");
    }

    public void saveEmailToken(String email, String authToken) {
        RedisEmail redisEmail = new RedisEmail(email,authToken);
        redisEmailRepository.save(redisEmail);
    }

    public String findByEmail(String email) {
        RedisEmail redisEmail = redisEmailRepository.findById(email)
                .orElseThrow();
        return redisEmail.getVerifyCode();
    }
}
