package com.web.billim.order.dto.response;

import com.web.billim.payment.domain.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfoResponse {
	private String name;
	private String merchantUid;
	private long amount;

	public static PaymentInfoResponse from(Payment savedPayment) {
		return new PaymentInfoResponse(
			savedPayment.getProductOrder().getProduct().getProductName(),
			savedPayment.getMerchantUid(),
			savedPayment.getTotalAmount()
		);
	}
}
