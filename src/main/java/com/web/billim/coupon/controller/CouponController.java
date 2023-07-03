package com.web.billim.coupon.controller;

import com.web.billim.coupon.dto.AvailableCouponResponse;
import com.web.billim.coupon.service.CouponService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon")
public class CouponController {

    private final CouponService couponService;

    @ApiOperation(value = "회원 쿠폰 목록 조회", notes = "마이페이지 쿠폰 클릭시 현재 사용가능한 쿠폰 목록 조회")
    @GetMapping("/list")
    public ResponseEntity<List<AvailableCouponResponse>> myCouponList(@AuthenticationPrincipal long memberId) {
        return ResponseEntity.ok(couponService.retrieveAvailableCouponList(memberId));
    }

}
