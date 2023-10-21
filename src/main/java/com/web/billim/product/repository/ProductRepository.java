package com.web.billim.product.repository;

import com.web.billim.product.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductQueryDslRepository {

    List<Product> findAllByProductIdInAndIsDeleted(List<Long> mostProductLists, boolean deleted);

    @Query("SELECT p FROM Product p WHERE p.member.memberId = :memberId AND p.isDeleted = false")
    Page<Product> findByMemberId(@Param("memberId") long memberId, Pageable pageable);

    List<Product> findAllByMember_memberId(long memberId);

}



