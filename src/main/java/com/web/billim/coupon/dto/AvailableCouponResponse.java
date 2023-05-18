package com.web.billim.coupon.dto;

import com.web.billim.coupon.domain.CouponIssue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableCouponResponse {

	private long couponIssueId;
	private String name;
	private long rate;
	private LocalDateTime expiredAt;

	public static AvailableCouponResponse from(CouponIssue coupon) {
		return new AvailableCouponResponse(
			coupon.getId(),
			coupon.getCoupon().getName(),
			coupon.getCoupon().getRate(),
			coupon.getExpiredAt()
		);
	}

}
