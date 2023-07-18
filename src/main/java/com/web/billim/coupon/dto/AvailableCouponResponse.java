package com.web.billim.coupon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.billim.coupon.domain.CouponIssue;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSS")
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
