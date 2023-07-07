package com.web.billim.order.controller;

import com.web.billim.order.dto.OrderCommand;
import com.web.billim.order.dto.response.MyOrderHistoryListResponse;
import com.web.billim.order.dto.response.PaymentInfoResponse;
import com.web.billim.order.service.OrderService;
import com.web.billim.point.service.PointService;
import com.web.billim.product.service.ProductService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;
    private final PointService pointService;

    @ApiOperation(value = "상품 주문", notes = "상품 상세보기에서 주문시 호출")
    @PostMapping
    public ResponseEntity<PaymentInfoResponse> order(
            @RequestBody OrderCommand command,
            @AuthenticationPrincipal long memberId
    ) {
        PaymentInfoResponse resp = orderService.order(memberId, command);
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "마이페이지 상품 구매 내역", notes = "마이페이지 구매목록 조회")
    @GetMapping("/my/purchase/list")
    public ResponseEntity<MyOrderHistoryListResponse> myOrder(@AuthenticationPrincipal long memberId) {
        return ResponseEntity.ok(orderService.findMyOrder(memberId));
    }

}
