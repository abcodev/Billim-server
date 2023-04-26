package com.web.billim.coupon.controller;

import com.web.billim.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    /*
     *  1. 사용 가능한 쿠폰 목록 보기
     */

    @GetMapping("coupon/list")
    public String myCouponList() {
        return "pages/myPage/myCouponList";
    }

}
