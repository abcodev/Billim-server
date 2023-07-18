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
//@ApiModel(value = "사용가능한 쿠폰")
public class AvailableCouponResponse {

//	@ApiModelProperty(value = "쿠폰 발급번호")
	private long couponIssueId;

//	@ApiModelProperty(value = "쿠폰 이름")
	private String name;

//	@ApiModelProperty(value = "쿠폰 할인율")
	private long rate;

//	@ApiModelProperty(value = "쿠폰 소멸일자")
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
