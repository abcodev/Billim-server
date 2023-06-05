package com.web.billim.point.service;

import com.web.billim.member.domain.Member;
import com.web.billim.payment.domain.Payment;
import com.web.billim.payment.domain.PointUsedHistory;
import com.web.billim.payment.repository.PointUsedHistoryRepository;
import com.web.billim.point.domain.SavedPoint;
import com.web.billim.point.dto.AddPointCommand;
import com.web.billim.point.dto.PointResponse;
import com.web.billim.point.repository.SavedPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointService {

	private final PointDomainService pointDomainService;
	private final SavedPointRepository savedPointRepository;
	private final PointUsedHistoryRepository pointUsedHistoryRepository;
	private final PointHistoryDomainService pointHistoryDomainService;

	// 1. 특정 사용자에게 적립금 부여 (C)
	@Transactional
	public void addPoint(AddPointCommand command) {
		savedPointRepository.save(SavedPoint.of(command));
	}

	// 2. 특정 사용자의 사용가능 적립금 조회 (R)
	@Transactional
	public long retrieveAvailablePoint(long memberId) {
		return savedPointRepository.findAllNotExpired(memberId).stream()
				// A : SavedPoint -> B : Integer
				.mapToLong(SavedPoint::getAvailableAmount)
				.sum();
	}

	// 3. 포인트 적립 및 사용 내역 조회
	public List<PointResponse> retrieveAllPointHistory(Member member) {
		// 1. 사용 내역 조회
		List<PointResponse> usedPoints = pointUsedHistoryRepository.findAllByMember(member).stream()
				.collect(Collectors.groupingBy(PointUsedHistory::getPayment))
				.entrySet().stream()
				.map(entry -> PointResponse.usedOf(entry.getKey(), entry.getValue()))
				.collect(Collectors.toList());

		// 2. 적립 내역 조회
		List<PointResponse> savedPoints = savedPointRepository.findAllByMember(member).stream()
				.map(PointResponse::savedOf)
				.collect(Collectors.toList());

		// 3. Merge
		List<PointResponse> pointHistories = new ArrayList<>();
		pointHistories.addAll(usedPoints);
		pointHistories.addAll(savedPoints);

		// 4. 정렬해서 반환
		// Comparable, Comparator -> JAVA 문법
		pointHistories.sort(Comparator.comparing(PointResponse::getAt).reversed());
		return pointHistories;
	}

	// 4. 결제시 특정 사용자의 적립금 사용 (U)
	@Transactional
	public List<PointUsedHistory> usePoint(Payment payment) {
		// 1. 포인트 사용
		Map<SavedPoint, Long> usedPointMap = pointDomainService.use(payment.getMember(), payment.getUsedPoint());

		// 2. 포인트 사용내역 추가
		return pointHistoryDomainService.generateHistory(payment, usedPointMap);
	}


}
