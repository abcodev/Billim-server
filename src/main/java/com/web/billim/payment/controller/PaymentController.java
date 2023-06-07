package com.web.billim.payment.controller;

import com.web.billim.payment.service.PaymentService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @ApiOperation(value = "결제 성공")
    @ApiImplicitParam(name = "imp_uid", value = "")
    @GetMapping("/complete")
    public ResponseEntity<Void> paymentComplete(@RequestParam("imp_uid") String impUid,
                                                @RequestParam("merchant_uid") String merchantUid
    ) {
        paymentService.complete(impUid, merchantUid);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/failure")
    public ResponseEntity<Void> paymentFailure(@RequestParam("merchant_uid") String merchantUid) {
        paymentService.rollback(merchantUid);
        return ResponseEntity.ok().build();
    }

}
