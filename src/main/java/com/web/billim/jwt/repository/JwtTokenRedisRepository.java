package com.web.billim.jwt.repository;

import com.web.billim.jwt.dto.RedisJwt;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtTokenRedisRepository extends CrudRepository<RedisJwt,String> {
}
