package com.web.billim.coupon.repository;

import com.web.billim.coupon.domain.CouponIssue;
import com.web.billim.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponIssueRepository extends JpaRepository<CouponIssue, Long> {
	@Query("SELECT ci FROM CouponIssue ci WHERE ci.member = :member AND ci.status = 'AVAILABLE'")
	List<CouponIssue> findAllByMember(@Param("member") Member member);
}
