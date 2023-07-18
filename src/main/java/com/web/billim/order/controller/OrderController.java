package com.web.billim.order.controller;

import com.web.billim.order.dto.OrderCommand;
import com.web.billim.order.dto.response.MyOrderHistoryListResponse;
import com.web.billim.order.dto.response.PaymentInfoResponse;
import com.web.billim.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "주문", description = "OrderController")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "상품 주문", description = "상품 결제버튼 클릭시 호출한다.")
    @PostMapping
    public ResponseEntity<PaymentInfoResponse> order(
            @RequestBody OrderCommand command,
            @AuthenticationPrincipal long memberId
    ) {
        PaymentInfoResponse resp = orderService.order(memberId, command);
        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "마이페이지 상품 구매 내역", description = "마이페이지에서 구매 목록을 조회한다.")
    @GetMapping("/my/purchase/list")
    public ResponseEntity<MyOrderHistoryListResponse> myOrder(@AuthenticationPrincipal long memberId) {
        return ResponseEntity.ok(orderService.findMyOrder(memberId));
    }


}
