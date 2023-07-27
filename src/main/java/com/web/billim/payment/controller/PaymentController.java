package com.web.billim.payment.controller;

import com.web.billim.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "결제", description = "PaymentController")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "0원 결제", description = "최종 카드 결제금액이 0원일 경우 호출하는 api")
    @PostMapping("/complete/zero-amount")
    public ResponseEntity<Void> paymentCompleteZeroAmount(
            @RequestParam("merchant_uid") String merchantUid
    ) {
        paymentService.completeZeroAmount(merchantUid);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "결제 성공")
    @GetMapping("/complete")
    public ResponseEntity<Void> paymentComplete(
            @RequestParam("imp_uid") String impUid,
            @RequestParam("merchant_uid") String merchantUid
    ) {
        paymentService.complete(impUid, merchantUid);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "결제 실패")
    @GetMapping("/failure")
    public ResponseEntity<Void> paymentFailure(@RequestParam("merchant_uid") String merchantUid) {
        paymentService.rollback(merchantUid);
        return ResponseEntity.ok().build();
    }

}
