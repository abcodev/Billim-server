//package com.web.billim.point.service;
//
//import com.web.billim.member.domain.Member;
//import com.web.billim.point.domain.SavedPoint;
//import com.web.billim.point.repository.SavedPointRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class PointDomainService {
//
//	private final SavedPointRepository savedPointRepository;
//
//	public Map<SavedPoint, Integer> use(Member member, int amount) {
//		List<SavedPoint> savedPoints = savedPointRepository.findAllNotExpired(member.getEmail());
//		if (savedPoints.stream().mapToInt(SavedPoint::getAvailableAmount).sum() < amount) {
//			throw new RuntimeException("사용가능 적립금이 부족합니다.");
//		}
//
//		Map<SavedPoint, Integer> usedPointMap = new HashMap<>();
//		int totalUsedAmount = 0;
//		for (SavedPoint savedPoint: savedPoints) {
//			int usedPoint = savedPoint.use(amount - totalUsedAmount);
//
//			usedPointMap.put(savedPoint, usedPoint);
//			totalUsedAmount += usedPoint;
//			if (totalUsedAmount >= amount) break;
//		}
//		return usedPointMap;
//	}
//
//}
