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
	// FE 에서 만약에 amount 가 0원 이상이면 기존처럼 IamPort 호출하고,
	// 아니라면(0원이면) IamPort 말고 서버 API 호출

	public static PaymentInfoResponse from(Payment savedPayment) {
		return new PaymentInfoResponse(
			savedPayment.getProductOrder().getProduct().getProductName(),
			savedPayment.getMerchantUid(),
			savedPayment.getTotalAmount()
		);
	}
}
