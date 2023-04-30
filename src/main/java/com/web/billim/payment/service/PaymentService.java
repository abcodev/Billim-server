package com.web.billim.payment.service;

import com.web.billim.client.iamport.IamPortService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.billim.coupon.domain.CouponIssue;
import com.web.billim.coupon.repository.CouponIssueRepository;
import com.web.billim.order.dto.response.PaymentInfoResponse;
import com.web.billim.payment.domain.Payment;
import com.web.billim.payment.dto.PaymentCommand;
import com.web.billim.payment.dto.PaymentInfoDto;
import com.web.billim.payment.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final IamPortService iamPortService;
    private final PaymentAmountCalculateService paymentAmountCalculateService;
    private final CouponIssueRepository couponIssueRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentInfoResponse payment(PaymentCommand command) {
        CouponIssue coupon = couponIssueRepository.findById(command.getCouponIssueId()).orElse(null);

        // 하나의 사용자는 한순간에 하나의 결제만 진행할 수 있다.
        // TODO: 진짜 아직도 여전히 쓸수있는 쿠폰일까?
        // TODO: 진짜 아직도 그만큼의 포인트가 남아있을까?
        PaymentInfoDto info = paymentAmountCalculateService.calculateAmount(command.getOrder(), coupon, command.getUsedPoint());
        String merchantUid = iamPortService.generateMerchantUid();

        Payment payment = paymentRepository.save(Payment.of(merchantUid, info));
        return PaymentInfoResponse.from(payment);
    }

}
// FE 에서 사용자 입력 받고
// 서버 API 호출(/order) 해서 결제금액 받고(merchant_uid, amount..)
// IMP request_pay 호출
// http://localhost:8080/order/validation




//package com.web.billim.payment.service;
//
//import com.web.billim.client.iamport.IamPortClientService;
//import com.web.billim.client.iamport.response.IamPortPaymentData;
//import com.web.billim.coupon.domain.CouponIssue;
//import com.web.billim.coupon.repository.CouponIssueRepository;
//import com.web.billim.coupon.service.CouponService;
//import com.web.billim.order.dto.response.PaymentInfoResponse;
//import com.web.billim.payment.domain.Payment;
//import com.web.billim.payment.dto.PaymentCommand;
//import com.web.billim.payment.dto.PaymentInfoDto;
//import com.web.billim.payment.repository.PaymentRepository;
//import com.web.billim.point.dto.AddPointCommand;
//import com.web.billim.point.service.PointService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//public class PaymentService {
//
//    private final IamPortClientService iamPortClientService;
//    private final PaymentAmountCalculateService paymentAmountCalculateService;
//    private final CouponService couponService;
//    private final PointService pointService;
//
//    private final CouponIssueRepository couponIssueRepository;
//    private final PaymentRepository paymentRepository;
//
//    @Transactional
//    public PaymentInfoResponse payment(PaymentCommand command) {
//        CouponIssue coupon = couponIssueRepository.findById(command.getCouponIssueId()).orElse(null);
//
//        // 하나의 사용자는 하나의 결제만 진행할 수 있다.
//        // TODO: 진짜 아직도 여전히 쓸수있는 쿠폰일까?
//        // TODO: 진짜 아직도 그만큼의 포인트가 남아있을까?
//        PaymentInfoDto info = paymentAmountCalculateService.calculateAmount(command.getOrder(), coupon, command.getUsedPoint());
//        String merchantUid = iamPortClientService.generateMerchantUid();
//
//        Payment payment = paymentRepository.save(Payment.of(merchantUid, info));
//        return PaymentInfoResponse.from(payment);
//    }
//
//    @Transactional
//    public void complete(String impUid, String merchantUid) {
//        // IamPort 결제내역 조회
//        IamPortPaymentData paymentData = iamPortClientService.retrievePayment(impUid);
//        // Payment Entity 조회
//        Payment payment = paymentRepository.findByMerchantUid(merchantUid).orElseThrow();
//        payment.setImpUid(impUid);
//        // 데이터 검증
//        if (payment.getTotalAmount() != paymentData.getAmount() || !paymentData.getStatus().equals("paid")) {
//            // TODO : IamPort 쪽에 결제 취소 API 호출
//            // Order, Payment Entity 취소
//            this.rollback(merchantUid);
//            throw new RuntimeException("결제내역이 일치하지 않습니다. 결제가 취소되었습니다.");
//        } else {
//            // 쿠폰 사용처리
//            couponService.useCoupon(payment.getMember(), payment.getCouponIssue().getId());
//            // 적립금 사용처리
//            pointService.usePoint(payment);
//            // Order, Payment Entity 완료처리
//            paymentRepository.save(payment.complete());
//            // 포인트 적립 -> 사용자의 회원 등급, 결제 금액
//            pointService.addPoint(AddPointCommand.from(payment));
//        }
//    }
//
//    @Transactional
//    public void rollback(String merchantUid) {
//        Payment payment = paymentRepository.findByMerchantUid(merchantUid).orElseThrow();
//        paymentRepository.save(payment.cancel());
//    }
//
//}
//
//// FE 에서 사용자 입력 받고
//// 서버 API 호출(/order) 해서 결제금액 받고(merchant_uid, amount..)
//// IMP request_pay 호출
//// http://localhost:8080/order/validation
//
//

