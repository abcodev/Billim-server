package com.web.billim.point.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.billim.point.service.PointService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/point")
public class PointController {

	private final PointService pointService;

	@ApiOperation(value = "사용 가능 적립금 금액 조회", notes = "나의 사용 가능한 적림금 총 금액을 조회")
	@GetMapping("/available")
	public ResponseEntity<Long> retrieveAvailablePoint(@RequestParam long memberId) {
		long availablePoint = pointService.retrieveAvailablePoint(memberId);
		return ResponseEntity.ok(availablePoint);
	}

	// 적립금 내역 조회

}
