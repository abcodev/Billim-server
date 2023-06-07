package com.web.billim.common.repository;

import com.web.billim.common.domain.RedisEmail;
import org.springframework.data.repository.CrudRepository;

public interface RedisEmailRepository extends CrudRepository<RedisEmail,String> {
}
