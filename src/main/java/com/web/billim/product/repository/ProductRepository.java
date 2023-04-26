package com.web.billim.product.repository;

import com.web.billim.product.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByMember_memberId(int memberId);

    Page<Product> findAllByOrderByCreatedAtDesc(Pageable paging);

//    @Query(value = "SELECT * FROM product ORDER BY product_id DESC", nativeQuery = true)
//    Page<Product> findAllOrderByCreatedAtDesc(Pageable paging);

//     JPQL
//     @Query("SELECT p FROM Product p WHERE p.member.memberId = :memberId")
//     List<Product> findByMemberId(@Param("memberId") int memberId);

//    Page<Product> findByProductNameAndDetailContaining(String keyword, Pageable pageable);

}



