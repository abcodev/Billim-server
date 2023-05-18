package com.web.billim.payment.dto;

import com.web.billim.member.domain.Member;
import com.web.billim.order.domain.ProductOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentCommand {

	private Member member;
	private ProductOrder order;
	private long couponIssueId;
	private long usedPoint;

	public PaymentCommand(Member member, ProductOrder order, long couponIssueId, long usedPoint) {
		this.member = member;
		this.order = order;
		this.couponIssueId = couponIssueId;
		this.usedPoint = usedPoint;
	}

}
