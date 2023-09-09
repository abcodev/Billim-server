package com.web.billim.product.repository;

import com.web.billim.member.domain.Member;
import com.web.billim.product.domain.Product;
import com.web.billim.product.domain.ProductInterest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductInterestRepository extends JpaRepository<ProductInterest, Long> {

//    Optional<List<ProductInterest>> findAllByMember_memberId(long memberId);

    Page<ProductInterest> findAllByMember_memberId(long memberId, Pageable pageable);

    @Modifying
    @Transactional
    void deleteByMemberAndProduct(Member member, Product product);
}
