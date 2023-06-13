package com.web.billim.product.repository;

import com.web.billim.member.domain.Member;
import com.web.billim.product.domain.Product;
import com.web.billim.product.domain.ProductInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductInterestRepository extends JpaRepository<ProductInterest,Long> {


    Optional<ProductInterest> findByMemberAndProduct(Member member, Product product);

    Optional<List<ProductInterest>> findAllByMember_memberId(long memberId);
}
