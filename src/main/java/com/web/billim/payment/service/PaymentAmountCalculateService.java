package com.web.billim.payment.service;

import com.web.billim.coupon.domain.CouponIssue;
import com.web.billim.order.domain.ProductOrder;
import com.web.billim.payment.dto.PaymentInfoDto;
import com.web.billim.product.type.TradeMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentAmountCalculateService {

	@Transactional
	public PaymentInfoDto calculateAmount(ProductOrder order, CouponIssue coupon, int usedPoint) {
		int basePrice = order.getPrice();

		int salePrice = 0;
		if (coupon != null) {
			salePrice = (int) (basePrice * (coupon.getCoupon().getRate() / 100.0));
		}
		int deliveryPrice = order.getTradeMethod().equals(TradeMethod.DELIVERY) ? 3000 : 0;

		int amount = basePrice - salePrice - usedPoint + deliveryPrice;
		return new PaymentInfoDto(order, coupon, usedPoint, amount);
	}

	// 결제 버튼을 누르고
	// 1. 서버에 결제정보 요청 (merchant_uid, amount)
	// 2. iamport API 호출

}
