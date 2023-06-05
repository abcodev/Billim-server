package com.web.billim.jwt;

import com.web.billim.jwt.dto.RedisJwt;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtRepositryTest extends CrudRepository<RedisJwt,String> {
//
//    @Override
//    Optional<RedisJwt> findById(String memberId);

}
