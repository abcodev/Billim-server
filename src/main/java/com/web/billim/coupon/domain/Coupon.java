package com.web.billim.coupon.domain;

import com.web.billim.common.domain.JpaEntity;
import com.web.billim.coupon.dto.CouponRegisterCommand;
import com.web.billim.member.domain.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "coupon")
@Builder
@Getter
public class Coupon extends JpaEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coupon_id")
	private Long id;

	private String name;
	private long rate;
	private long limitDate;

	public CouponIssue issue(Member member) {
		return CouponIssue.builder()
				.member(member)
				.coupon(this)
				.status(CouponStatus.AVAILABLE)
				.build();
	}

	public static Coupon from(CouponRegisterCommand command) {
		return Coupon.builder()
				.name(command.getName())
				.rate(command.getRate())
				.limitDate(command.getLimitDate())
				.build();
	}

}


