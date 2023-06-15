package com.web.billim.product.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.billim.product.dto.response.MostProductList;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class ProductRedisService {

    private final RedisTemplate<String,String > redisTemplate;

    private final ProductService productService;

    public ProductRedisService(RedisTemplate<String,String> redisTemplate, ProductService productService) {
        this.redisTemplate = redisTemplate;
        this.productService = productService;
    }


    public void saveProduct(long productId) {
        redisTemplate.opsForZSet().incrementScore("MOST_POPULAR_PRODUCT",String.valueOf(productId),1);
    }

    @Transactional
    public List<MostProductList> rankPopularProduct(){
        ObjectMapper objectMapper = new ObjectMapper();
        List<Long> mostProductLists = objectMapper.convertValue(
                Objects.requireNonNull(
                        redisTemplate.opsForZSet().
                                reverseRange("product", 0, 4)), new TypeReference<List<Long>>() {});
        return productService.findMostPopularProduct(mostProductLists);
    }
}



