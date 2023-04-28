package com.web.billim.coupon.controller;

import com.web.billim.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

//    @GetMapping("coupon/list")
//    public String myCouponList() {
//        return "pages/myPage/myCouponList";
//    }

}
