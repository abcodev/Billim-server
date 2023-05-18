package com.web.billim.coupon.domain;

import com.web.billim.common.domain.JpaEntity;
import com.web.billim.member.domain.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "coupon_issue")
@Builder
@Getter
public class CouponIssue extends JpaEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coupon_issue_id")
	private Long id;

	@JoinColumn(name = "member_id", referencedColumnName = "member_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	@JoinColumn(name = "coupon_id", referencedColumnName = "coupon_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Coupon coupon;

	@Enumerated(EnumType.STRING)
	private CouponStatus status;

	public LocalDateTime getExpiredAt() {
		return this.getCreatedAt().plusDays(this.coupon.getLimitDate());
	}

	public void use() {
		this.status = CouponStatus.USED;
	}

}

enum CouponStatus {
	AVAILABLE, DELETED, USED
}