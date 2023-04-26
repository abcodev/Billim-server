package com.web.billim.payment.dto;

import com.web.billim.coupon.domain.CouponIssue;
import com.web.billim.order.domain.ProductOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfoDto {

	private ProductOrder order;
	private CouponIssue coupon;
	private int usedPoint;
	private int amount;

}
