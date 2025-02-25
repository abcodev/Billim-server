package com.web.billim.point.domain;

import com.web.billim.common.domain.JpaEntity;
import com.web.billim.member.domain.Member;
import com.web.billim.point.dto.AddPointCommand;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "saved_point")
@Builder
@Getter
public class SavedPoint extends JpaEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "point_id")
	private Long id;

	@JoinColumn(name = "member_id", referencedColumnName = "member_id")
	@ManyToOne
	private Member member;

	private long amount;
	private long availableAmount;
	private LocalDateTime expiredAt;

	public SavedPoint(Member member, long amount, Duration expiresIn) {
		this.member = member;
		this.amount = amount;
		this.availableAmount = amount;
		this.expiredAt = LocalDateTime.now().plus(expiresIn);
	}

	public static SavedPoint of(AddPointCommand command) {
		return SavedPoint.builder()
				.member(command.getMember())
				.amount(command.getAmount())
				.availableAmount(command.getAmount())
				.expiredAt(command.getExpiredAt())
				.build();
	}

	public long use(long amount) {
		long usedAmount;
		if (this.availableAmount >= amount) {
			usedAmount = amount;
			this.availableAmount -= amount;
		} else {
			usedAmount = this.availableAmount;
			this.availableAmount = 0;
		}
		return usedAmount;
	}

	public void addAvailablePoint(long amount) {
		this.availableAmount += amount;
	}

}
