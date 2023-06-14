package com.web.billim.product.service;

import com.web.billim.member.domain.Member;
import com.web.billim.member.repository.MemberRepository;
import com.web.billim.product.domain.Product;
import com.web.billim.product.domain.ProductInterest;
import com.web.billim.product.dto.request.InterestRequest;
import com.web.billim.product.dto.response.MyInterestProduct;
import com.web.billim.product.dto.response.MyInterestProductList;
import com.web.billim.product.repository.ProductInterestRepository;
import com.web.billim.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductInterestService {
    private final ProductInterestRepository productInterestRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void saveOrDeleteInterest(long memberId, InterestRequest interestRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        Product product = productRepository.findById(interestRequest.getProductId()).orElseThrow();
        if(interestRequest.getInterest()){
            ProductInterest productInterest = ProductInterest.builder()
                    .product(product)
                    .member(member)
                    .build();
            productInterestRepository.save(productInterest);
        }else{
            productInterestRepository.deleteByMemberAndProduct(member, product);
//            ProductInterest productInterest = productInterestRepository.findByMemberAndProduct(member, product)
//                    .orElseThrow();
//            productInterestRepository.delete(productInterest);
        }
    }

    @Transactional
    public MyInterestProductList myInterestProductList(long memberId) {
        List<MyInterestProduct> myInterestProduct= productInterestRepository.findAllByMember_memberId(memberId)
                .orElseThrow()
                .stream()
                .map(MyInterestProduct::of)
                .collect(Collectors.toList());
        return new MyInterestProductList(myInterestProduct);
    }
}
