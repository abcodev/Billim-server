package com.web.billim.product.repository;

import com.web.billim.product.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query("SELECT count(r) FROM Review r WHERE r.productOrder.member.memberId = :memberId ")
    Optional<Long> countByMemberId(long memberId);

    @Query("SELECT r FROM Review r WHERE r.productOrder.product.productId = :productId")
    List<Review> findAllByProductId(long productId);

//	@Query("SELECT r FROM Review r WHERE r.product.id = :productId")
//	List<Review> findAllByProductId(@Param("productId") long productId);


}
