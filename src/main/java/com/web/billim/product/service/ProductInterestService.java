package com.web.billim.product.service;

import com.web.billim.product.domain.ProductInterest;
import com.web.billim.product.dto.request.InterestRequest;
import com.web.billim.product.repository.ProductInterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductInterestService {
    private final ProductInterestRepository productInterestRepository;

//    public void myInterestProduct(long memberId, InterestRequest interestRequest) {
//        ProductInterest productInterest = ProductInterest.builder()
//                .product(interestRequest.getProductId())
//                .member(memberId)
//                .build();
//        if(interestRequest.getInterest()){
//            productInterestRepository.save();
//
//        }else{
//
//
//        }
//
//    }
}
