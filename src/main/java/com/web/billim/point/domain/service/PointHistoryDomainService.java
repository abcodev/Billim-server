package com.web.billim.point.domain.service;

import com.web.billim.payment.domain.Payment;
import com.web.billim.payment.domain.PointUsedHistory;
import com.web.billim.payment.repository.PointUsedHistoryRepository;
import com.web.billim.point.domain.SavedPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointHistoryDomainService {

	private final PointUsedHistoryRepository pointUsedHistoryRepository;

	public List<PointUsedHistory> generateHistory(Payment payment, Map<SavedPoint, Long> usedPointMap) {
		List<PointUsedHistory> histories = usedPointMap.entrySet().stream().map(entry -> {
			SavedPoint savedPoint = entry.getKey();
			long usedAmount = entry.getValue();

			return PointUsedHistory.builder()
				.payment(payment)
				.savedPoint(savedPoint)
				.amount(usedAmount)
				.build();
		}).collect(Collectors.toList());
		return pointUsedHistoryRepository.saveAll(histories);
	}

}
