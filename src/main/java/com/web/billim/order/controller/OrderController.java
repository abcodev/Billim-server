package com.web.billim.order.controller;

import com.web.billim.order.dto.OrderCommand;
import com.web.billim.order.dto.response.PaymentInfoResponse;
import com.web.billim.order.service.OrderService;
import com.web.billim.point.service.PointService;
import com.web.billim.product.domain.Product;
import com.web.billim.product.dto.response.ProductDetailResponse;
import com.web.billim.product.service.ProductService;
import com.web.billim.security.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final ProductService productService;
    private final PointService pointService;

    @GetMapping ("/order/confirm")
    public String orderConfirm(String startDate, String endDate, int productId, Model model) {

        model.addAttribute("startDate",startDate);
        model.addAttribute("endDate", endDate);

        Product product = productService.retrieve(productId);
        ProductDetailResponse productDetail = ProductDetailResponse.of(product);
        List<LocalDate> alreadyDates = orderService.reservationDate(product);

        model.addAttribute("product", productDetail);
        model.addAttribute("alreadyDates",alreadyDates);

        return "pages/order/orderInfo";
    }


    @PostMapping("/order")
    public ResponseEntity<PaymentInfoResponse> order(@RequestBody OrderCommand command,
                                                     @AuthenticationPrincipal User user
    ) {
        PaymentInfoResponse resp = orderService.order(user.getMemberId(), command);
        return ResponseEntity.ok(resp);
    }


}
