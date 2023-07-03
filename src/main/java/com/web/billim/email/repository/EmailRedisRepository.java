package com.web.billim.email.repository;

import com.web.billim.email.domain.EmailRedis;
import org.springframework.data.repository.CrudRepository;

public interface EmailRedisRepository extends CrudRepository<EmailRedis,String> {

}
