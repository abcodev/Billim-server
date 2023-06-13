package com.web.billim.product.repository;

import com.web.billim.product.domain.Product;
import com.web.billim.product.dto.response.MostProductList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByMember_memberId(long memberId);

    Page<Product> findAllByOrderByCreatedAtDesc(Pageable paging);

    // Fetch Join (JPA 개념)
    @Query("SELECT p FROM Product p "
        + "WHERE p.productName like %:keyword% OR p.detail like %:keyword% ORDER BY p.createdAt DESC")
    Page<Product> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

    Optional<List<Product>> findAllByProductIdIn(List<Long> mostProductLists);

//    @Query(value = "SELECT * FROM product ORDER BY product_id DESC", nativeQuery = true)
//    Page<Product> findAllOrderByCreatedAtDesc(Pageable paging);

//     JPQL
//     @Query("SELECT p FROM Product p WHERE p.member.memberId = :memberId")
//     List<Product> findByMemberId(@Param("memberId") int memberId);

//    Page<Product> findByProductNameAndDetailContaining(String keyword, Pageable pageable);

}



