package com.web.billim.point.dto;

import com.web.billim.payment.domain.Payment;
import com.web.billim.payment.domain.PointUsedHistory;
import com.web.billim.point.domain.SavedPoint;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PointResponse {

	private PointType type;
	private int amount;
	private LocalDateTime at; // 시점
	private LocalDateTime expiredAt;

	public static PointResponse usedOf(Payment payment, List<PointUsedHistory> histories) {
		return PointResponse.builder()
			.type(PointType.USED)
			.amount(
				histories.stream()
					.mapToInt(PointUsedHistory::getAmount)
					.sum()
			)
			.at(payment.getCreatedAt())
			.build();
	}

	public static PointResponse savedOf(SavedPoint savedPoint) {
		return PointResponse.builder()
			.type(PointType.SAVED)
			.amount(savedPoint.getAmount())
			.at(savedPoint.getCreatedAt())
			.expiredAt(savedPoint.getExpiredAt())
			.build();
	}

}

enum PointType {
	SAVED, USED
}
