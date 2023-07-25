package com.web.billim.payment.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.web.billim.coupon.repository.CouponIssueRepository;
import com.web.billim.coupon.service.CouponService;
import com.web.billim.payment.domain.Payment;
import com.web.billim.payment.repository.PaymentRepository;
import com.web.billim.point.dto.AddPointCommand;
import com.web.billim.point.service.PointService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentDomainService {

	private final CouponService couponService;
	private final PointService pointService;
	private final PaymentRepository paymentRepository;

	@Transactional
	public void payment(Payment payment) {
		// 쿠폰 사용처리
		if (payment.getCouponIssue() != null) {
			couponService.useCoupon(payment.getMember(), payment.getCouponIssue().getId());
		}
		// 적립금 사용처리
		pointService.usePoint(payment);
		// Order, Payment Entity 완료처리
		paymentRepository.save(payment.complete());
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void rollback(String merchantUid) {
		Payment payment = paymentRepository.findByMerchantUid(merchantUid).orElseThrow();
		paymentRepository.save(payment.cancel());
	}

	@Transactional
	public void refund(Payment payment) {
		pointService.refund(payment);
		couponService.refund(payment.getCouponIssue());
		payment.cancel();
	}

}
