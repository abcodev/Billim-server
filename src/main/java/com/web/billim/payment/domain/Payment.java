package com.web.billim.payment.domain;

import com.web.billim.common.domain.JpaEntity;
import com.web.billim.coupon.domain.CouponIssue;
import com.web.billim.member.domain.Member;
import com.web.billim.order.domain.ProductOrder;
import com.web.billim.payment.dto.PaymentInfoDto;
import com.web.billim.payment.type.PaymentStatus;
import com.web.billim.product.type.TradeMethod;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "payment")
@Builder
@Getter
public class Payment extends JpaEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id")
	private Long id;

	@JoinColumn(name = "order_id", referencedColumnName = "product_order_id")
	@OneToOne
	private ProductOrder productOrder;

	@JoinColumn(name = "coupon_issue_id", referencedColumnName = "coupon_issue_id")
	@OneToOne
	private CouponIssue couponIssue;

	@Column(name = "point")
	private long usedPoint;

	private String impUid;
	private String merchantUid;
	private long totalAmount;

	@Enumerated(EnumType.STRING)
	private PaymentStatus status;

	@Enumerated(EnumType.STRING)
	private TradeMethod tradeMethod;

	public Member getMember() {
		return this.productOrder.getMember();
	}

	public static Payment of(String merchantUid, PaymentInfoDto dto) {
		return Payment.builder()
				.productOrder(dto.getOrder())
				.couponIssue(dto.getCoupon())
				.usedPoint(dto.getUsedPoint())
				.merchantUid(merchantUid)
				.totalAmount(dto.getAmount())
				.status(PaymentStatus.IN_PROGRESS)
				.tradeMethod(dto.getOrder().getTradeMethod())
				.build();
	}

	public Payment cancel() {
		this.status = PaymentStatus.CANCELED;
		this.getProductOrder().cancel();
		return this;
	}

	public Payment complete() {
		this.status = PaymentStatus.DONE;
		this.getProductOrder().complete();
		return this;
	}

	public void setImpUid(String impUid) {
		this.impUid = impUid;
	}
}

