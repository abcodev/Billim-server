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
    @ApiOperation(value = "주문")
    @PostMapping("/order")
    public ResponseEntity<PaymentInfoResponse> order(@RequestBody OrderCommand command,
                                                     @AuthenticationPrincipal long memberId
    ) {
        PaymentInfoResponse resp = orderService.order(memberId, command);
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "주분내역상품")
    @GetMapping("/order/my")
    public ResponseEntity<MyOrderHistoryListResponse> myOrder(
            @AuthenticationPrincipal long memberId
    ){
        return ResponseEntity.ok(orderService.findMyOrder(memberId));
    }
}
