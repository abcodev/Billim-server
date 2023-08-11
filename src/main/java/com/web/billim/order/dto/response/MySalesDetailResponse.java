package com.web.billim.order.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.web.billim.order.domain.ProductOrder;
import com.web.billim.product.domain.Product;

@Getter
@Builder
public class MySalesDetailResponse {

    private long memberId;
    private long productId;
    private String productName;
    private String imageUrls;

    // 현재 빌리고 있는 사람에 대한 정보
    private MySalesOrderResponse currentOrder;

    // 대기중인 사람에 대한 정보 (정렬)
    private List<MySalesOrderResponse> standbyOrders;

    // 이미 완료된 사람에 대한 정보 (정렬)
    private List<MySalesOrderResponse> pastOrders;

    // 취소된 예약 정보 (정렬)
    private List<MySalesOrderResponse> canceledOrders;

    public static MySalesDetailResponse of(Product product, List<ProductOrder> orderHistories) {
        List<MySalesOrderResponse> standbyOrders = new ArrayList<>();
        List<MySalesOrderResponse> pastOrders = new ArrayList<>();
        List<MySalesOrderResponse> currentOrder = new ArrayList<>();
        List<MySalesOrderResponse> canceledOrders = orderHistories.stream()
                .filter(ProductOrder::isCanceled)
                .map(MySalesOrderResponse::from)
                .collect(Collectors.toList());

        orderHistories.stream()
                .filter(order -> !order.isCanceled())
                .map(MySalesOrderResponse::from)
                .forEach(order -> {
                    if (order.getStartAt().isAfter(LocalDate.now())) {
                        standbyOrders.add(order);
                    } else if (order.getEndAt().isBefore(LocalDate.now())) {
                        pastOrders.add(order);
                    } else {
                        currentOrder.add(order);
                    }
                });
        
        return new MySalesDetailResponse(
                product.getMember().getMemberId(),
                product.getProductId(),
                product.getProductName(),
                product.mainImage(),
                currentOrder.stream().findFirst().orElse(null),
                standbyOrders,
                pastOrders,
                canceledOrders
        );
    }

}
