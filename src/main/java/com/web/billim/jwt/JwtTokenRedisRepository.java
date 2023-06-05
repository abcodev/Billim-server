//package com.web.billim.jwt;
//
//import com.web.billim.jwt.dto.RedisJwt;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.repository.CrudRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//@RequiredArgsConstructor
//public class JwtTokenRedisRepository extends CrudRepository<RedisJwt,String> {
//    private final RedisTemplate<String, String> redisTemplate;
//
//
//    private static final String KEY_PREFIX = "REFRESH_TOKEN:";
//
//    public void save(long memId, String refreshToken){
//        redisTemplate.opsForValue().set(KEY_PREFIX+memId, refreshToken);
//    }
//    public Optional<String> find(String key) {
//        return Optional.ofNullable(redisTemplate.opsForValue().get(KEY_PREFIX + key));
//    }
//
//    @Override
//    public <S extends RedisJwt> S save(RedisJwt redisJwt) {
//        return redisTemplate.opsForValue().set(redisJwt);
//    }
//}
//
