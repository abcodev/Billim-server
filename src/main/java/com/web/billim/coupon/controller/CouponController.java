package com.web.billim.coupon.controller;

import com.web.billim.chat.dto.ChatRoomAndPreviewResponse;
import com.web.billim.coupon.dto.AvailableCouponResponse;
import com.web.billim.coupon.service.CouponService;
import com.web.billim.member.domain.Member;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon")
public class CouponController {

    private final CouponService couponService;

    @ApiOperation(value = "내 쿠폰 목록 조회")
    @ApiImplicitParam(name = "memberId", value = "회원고유번호")
    @GetMapping("/list/{memberId}")
    public ResponseEntity<List<AvailableCouponResponse>> myCouponList(@PathVariable("memberId") Member member) {
        return ResponseEntity.ok(couponService.retrieveAvailableCouponList(member));
    }
}
