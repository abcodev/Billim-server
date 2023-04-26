package com.web.billim.payment.controller;

import com.web.billim.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/complete")
    public ResponseEntity<Void> paymentComplete(
            @RequestParam("imp_uid") String impUid,
            @RequestParam("merchant_uid") String merchantUid
    ) {
        paymentService.complete(impUid, merchantUid);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/failure")
    public ResponseEntity<Void> paymentFailure(
            @RequestParam("merchant_uid") String merchantUid
    ) {
        paymentService.rollback(merchantUid);
        return ResponseEntity.ok().build();
    }

    // TODO : 동시에 두명이 클릭했을 때 어떻게 해야?
    /*
     *   1. 결제 화면 진입
     *   	1-1. 파라미터 : ProductId, 결제 기간(start_at, end_at)
     *   	1-2. ProductId 로 Product, Member(판매자 정보) 조회
     *   	1-3. SecurityContext 에 들어있는 Member(구매자 정보) 아래 내용 조회
     *   		1-3-1. 사용 가능 적립금
     *   		1-3-2. 사용가능 쿠폰 목록
     *   	1-4. PaymentPageResponse
     *   		1-4-1. start_at, end_at
     *   		1-4-2. product 정보 (ProductDetailResponse)
     *   				price 포함된 정보
     *   		1-4-3. available_amount
     *   		1-4-4. coupons (List) -> Id, 이름, 할인률
     *
     *   2. 결제 버튼 눌렀을 때
     *   	2-1. 파라미터 : 결제 기간, 거래방법, 사용 적립금, 선택한 쿠폰, 이름, 주소, 전화번호
     *   	2-2. 적립금 검증 (진짜 사용 가능한지)
     *   	2-3. 쿠폰 검증 (본인거가 맞고 진짜 사용 가능한지)
     *   	2-4. Payment 레코드 생성 후 결제 금액 반환 (얼마 결제가 되어야 한다)
     *
     * 	 3. 그리고 아임포트쪽에서 다시한번 검증하는 로직..
     * 	    실제 결제 완료처리
     * 	    3-1. 적립금 사용처리 (saved_point 의 available_amount 감소 되어야 하고,, point_history 에 새로운거 추가)
     * 	    3-2. 쿠폰 사용처리 (상태 바꾸면 되고)
     * 	    3-3. ProductOrder 레코드 생성
     *
     *   3. 결제 내역 조회
     *   	3-1. 파라미터 :
     */

}
