package com.web.billim.product.repository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRedisRepository {

	private final RedisTemplate<String, Long> longRedisTemplate;

	public void push(long memberId, long productId) {
		longRedisTemplate.opsForList().leftPush("RECENT_VIEW_PRODUCT:" + memberId, productId);
	}

	public void remove(long memberId, long productId) {
		longRedisTemplate.opsForList().remove("RECENT_VIEW_PRODUCT:" + memberId, 0, productId);
	}

	public List<Long> findTopN(long memberId, int n) {
		return longRedisTemplate.opsForList().range("RECENT_VIEW_PRODUCT:" + memberId, 0, n - 1);
	}
}
