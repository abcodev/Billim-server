package com.web.billim.payment.service;

import com.web.billim.client.iamport.IamPortClientService;
import com.web.billim.coupon.domain.CouponIssue;
import com.web.billim.coupon.repository.CouponIssueRepository;
import com.web.billim.exception.OrderFailedException;
import com.web.billim.exception.handler.ErrorCode;
import com.web.billim.order.domain.ProductOrder;
import com.web.billim.order.dto.response.PaymentInfoResponse;
import com.web.billim.payment.domain.Payment;
import com.web.billim.payment.dto.PaymentCommand;
import com.web.billim.payment.dto.PaymentInfoDto;
import com.web.billim.payment.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final IamPortClientService iamPortClientService;
    private final PaymentAmountCalculateService paymentAmountCalculateService;
    private final PaymentDomainService paymentDomainService;
    private final CouponIssueRepository couponIssueRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentInfoResponse payment(PaymentCommand command) {
        CouponIssue coupon = couponIssueRepository.findById(command.getCouponIssueId()).orElse(null);

        // TODO: 진짜 아직도 여전히 쓸수있는 쿠폰일까?
        // TODO: 진짜 아직도 그만큼의 포인트가 남아있을까?
        PaymentInfoDto info = paymentAmountCalculateService.calculateAmount(command.getOrder(), coupon, command.getUsedPoint());
        String merchantUid = iamPortClientService.generateMerchantUid();

        Payment payment = paymentRepository.save(Payment.of(merchantUid, info));
        return PaymentInfoResponse.from(payment);
    }

    @Transactional
    public void completeZeroAmount(String merchantUid) {
        try {
            Payment payment = paymentRepository.findByMerchantUid(merchantUid).orElseThrow();
            paymentDomainService.payment(payment);
        } catch (RuntimeException ex) {
            paymentDomainService.rollback(merchantUid);
        }
    }

    @Transactional
    public void complete(String impUid, String merchantUid) {
        try {
            // Payment Entity 조회
            Payment payment = paymentRepository.findByMerchantUid(merchantUid).orElseThrow();
            payment.setImpUid(impUid);

            if (iamPortClientService.validate(impUid, payment.getTotalAmount())) {
                paymentDomainService.payment(payment);
            } else {
                paymentDomainService.rollback(merchantUid);
                throw new OrderFailedException(ErrorCode.MISMATCH_PAYMENT_INFO);
            }
        } catch (Exception ex) {
            log.error("결제를 완료 처리하는 과정에서 에러가 발생했습니다.", ex);
            paymentDomainService.rollback(merchantUid);
            throw new OrderFailedException(ErrorCode.PAYMENT_FAILED, ex);
        }
    }

    public void rollback(String merchantUid) {
        paymentDomainService.rollback(merchantUid);
    }

    @Transactional
    public void cancel(ProductOrder order) {
        Payment payment = paymentRepository.findByProductOrder(order).orElseThrow();
        paymentDomainService.refund(payment);
        iamPortClientService.cancel(payment.getImpUid());
        //
        // TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
        //     @Override
        //     public void afterCommit() {
        //         iamPortClientService.cancel(payment.getImpUid());
        //     }
        // });
    }

    // 1. Transaction 은 이미 예외가 발생한 상황에서 재사용이 안된다.
    // 2. Transaction 은 내부호출에 대해서는 적용이 안된다. (@Transactional 이 AOP 로 구현되어있어서 -> 내부호출문제)

}
