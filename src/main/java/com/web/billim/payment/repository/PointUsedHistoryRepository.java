package com.web.billim.payment.repository;

import com.web.billim.member.domain.Member;
import com.web.billim.payment.domain.Payment;
import com.web.billim.payment.domain.PointUsedHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointUsedHistoryRepository extends JpaRepository<PointUsedHistory, Long> {

	@Query("SELECT puh FROM PointUsedHistory puh WHERE puh.payment.productOrder.member = :member")
	List<PointUsedHistory> findAllByMember(@Param("member") Member member);

	List<PointUsedHistory> findAllByPayment(Payment payment);
}
