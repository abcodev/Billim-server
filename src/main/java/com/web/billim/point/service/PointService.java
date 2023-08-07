package com.web.billim.point.service;

import com.web.billim.member.domain.Member;
import com.web.billim.order.repository.OrderRepository;
import com.web.billim.payment.domain.Payment;
import com.web.billim.payment.domain.PointUsedHistory;
import com.web.billim.payment.repository.PaymentRepository;
import com.web.billim.payment.repository.PointUsedHistoryRepository;
import com.web.billim.point.domain.SavedPoint;
import com.web.billim.point.domain.service.PointDomainService;
import com.web.billim.point.domain.service.PointHistoryDomainService;
import com.web.billim.point.dto.AddPointCommand;
import com.web.billim.point.dto.PointResponse;
import com.web.billim.point.repository.SavedPointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointService {

	private final PointDomainService pointDomainService;
	private final SavedPointRepository savedPointRepository;
	private final PointUsedHistoryRepository pointUsedHistoryRepository;
	private final PointHistoryDomainService pointHistoryDomainService;
	private final OrderRepository orderRepository;
	private final PaymentRepository paymentRepository;

	// 1. 특정 사용자에게 적립금 부여 (C)
	@Transactional
	public void addPoint(AddPointCommand command) {
		savedPointRepository.save(SavedPoint.of(command));
	}

	// 2. 특정 사용자의 사용가능 적립금 조회 (R)
	@Transactional
	public long retrieveAvailablePoint(long memberId) {
		return savedPointRepository.findAllNotExpired(memberId).stream()
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

	@Transactional
	public void refund(Payment payment) {
		pointUsedHistoryRepository.findAllByPayment(payment).forEach(history -> {
			// 1. SavedPoint 의 availablePoint 늘려주기
			history.refund();
			// 2. 해당 history 삭제
			pointUsedHistoryRepository.delete(history);
		});
	}

	public void savingPointOrderHistory() {
		// 1. 어제 기간이 만료된 Order 전부 조회
		// 		-> where end_at = '어제' -> 인덱스가 없으면 탐색속도가 느려질 수 있다.
		// 		-> 데이터가 많다 -> 조회기능이 추가된다? -> 그 조회 쿼리가 인덱스를 잘 타나?
		//		-> 기존에 인덱스들이 있는데 그거 잘타면 오케이 -> 만약 못탄다? 그럼 망했다.
		//		-> 새로운 인덱스를 추가해줘야 한다.
		//  InnoDB 내부설계에 의해서 결정이 되는데..
		//  인덱스가 잘 타는지 확인할 수 있으면 됨. - 실행계획
		var yesterday = LocalDate.now().minusDays(1);
		orderRepository.findAllByEndAt(yesterday).forEach(order -> {
			// 2. 전부 포인트 적립
			// amount 계산 필요
			long amount = paymentRepository.findByProductOrder(order)
				.map(pointDomainService::calculate)
				.orElseThrow();
			SavedPoint savedPoint = new SavedPoint(order.getMember(), amount, Duration.ofDays(365));
			log.info(String.format("[PointService] savingPointOrderHistory : %s(%s) 에게 %s 포인트가 적립되었습니다.",
				order.getMember().getMemberId(), order.getMember().getName(), amount));
			savedPointRepository.save(savedPoint);
		});
	}

}
