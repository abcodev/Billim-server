package com.web.billim.product.service;

import com.web.billim.product.repository.ProductRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentProductRedisService {

   private final ProductRedisRepository productRedisRepository;

   public void push(long memberId, long productId) {
       productRedisRepository.remove(memberId, productId);
       productRedisRepository.push(memberId, productId);
   }

   public List<Long> findTopN(long memberId, int n) {
       return productRedisRepository.findTopN(memberId, n);
   }

}
