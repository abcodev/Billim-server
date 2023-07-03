package com.web.billim.product.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class ProductRedisService {

    private final RedisTemplate<String,String > redisTemplate;


    public ProductRedisService(RedisTemplate<String,String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveProduct(long productId) {
        redisTemplate.opsForZSet().incrementScore("MOST_POPULAR_PRODUCT", String.valueOf(productId), 1);
    }

    @Transactional
    public List<Long> rankPopularProduct(){
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(
                Objects.requireNonNull(
                        redisTemplate.opsForZSet().
                                reverseRange("MOST_POPULAR_PRODUCT", 0, 4)), new TypeReference<List<Long>>() {});
    }
}



