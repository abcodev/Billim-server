package com.web.billim.review.repository;

import com.web.billim.review.domain.Review;
import com.web.billim.review.dto.WrittenReviewList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query("SELECT count(r) FROM Review r WHERE r.productOrder.member.memberId = :memberId ")
    Optional<Long> countByMemberId(long memberId);
//    Long countByMemberId(@Param("memberId") long memberId);

	@Query("SELECT r FROM Review r WHERE r.productOrder.product.productId = :productId")
	List<Review> findAllByProductId(@Param("productId") long productId);


    List<Review> findByProductOrder_Member_MemberId(Long memberId);



}
