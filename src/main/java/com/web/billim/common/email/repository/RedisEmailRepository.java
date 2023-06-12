package com.web.billim.common.email.repository;

import com.web.billim.common.email.domain.RedisEmail;
import org.springframework.data.repository.CrudRepository;

public interface RedisEmailRepository extends CrudRepository<RedisEmail,String> {
}
