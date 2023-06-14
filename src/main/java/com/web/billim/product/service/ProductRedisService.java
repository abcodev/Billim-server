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

    private final RedisTemplate<String, String> redisTemplate;
    private final ProductService productService;

    public ProductRedisService(RedisTemplate<String, String> redisTemplate, ProductService productService) {
        this.redisTemplate = redisTemplate;
        this.productService = productService;
    }

    public void saveProduct(long productId) {
        redisTemplate.opsForZSet().incrementScore("MOST_POPULAR_PRODUCT", String.valueOf(productId), 1);
    }

    @Transactional
    public List<MostProductList> rankPopularProduct(){
        ObjectMapper objectMapper = new ObjectMapper();
        List<Long> mostProductLists = objectMapper.convertValue(
                Objects.requireNonNull(
                        redisTemplate.opsForZSet().
                                reverseRange("product", 0, 4)), new TypeReference<List<Long>>() {}); // 요기 키는 왜 다르지
        return productService.findMostPopularProduct(mostProductLists);
    }


//    @Transactional
//    public List<Long> rankPopularProduct(){
//        ObjectMapper objectMapper = new ObjectMapper();
//        return objectMapper.convertValue(
//                Objects.requireNonNull(
//                        redisTemplate.opsForZSet().
//                                reverseRange("MOST_POPULAR_PRODUCT", 0, 4)), new TypeReference<List<Long>>() {});
//    }

}

/*
    rankPopularProduct 의 로직
        - Redis 에서 상위 5개의 MOST_POPULAR_PRODUCT 의 productId 를 조회
        - ProductId 를 가지고 MostProductList Dto 를 만들어서 반환
    Redis 는 외부 니까 바뀔 수 있는 부분
        - 상위5개 로직이 분리되면 좋을 것 같음 (productService 에서 ProductRedisService 를 보는게 좋을것같음)
 */



