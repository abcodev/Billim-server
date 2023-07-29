package com.web.billim.order.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.billim.order.domain.ProductOrder;
import com.web.billim.order.type.ProductOrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class MyOrderHistory {

    private long orderId;
    private long productId;
    private long sellerId;
    private String sellerNickname;
    private String productName;
    private long price;
    private String imageUrl;
    private ProductOrderStatus status;
    private boolean isDeleted;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime orderDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate orderStartAt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate orderEndAt;

    public static MyOrderHistory from(ProductOrder productOrder) {
        return MyOrderHistory.builder()
                .orderId(productOrder.getOrderId())
                .productId(productOrder.getProduct().getProductId())
                .sellerId(productOrder.getProduct().getMember().getMemberId())
                .sellerNickname(productOrder.getProduct().getMember().getNickname())
                .productName(productOrder.getProduct().getProductName())
                .price(productOrder.getPrice())
                .imageUrl(productOrder.getProduct().mainImage())
                .status(productOrder.getStatus())
                .isDeleted(productOrder.getProduct().isDeleted())
                .orderDate(productOrder.getCreatedAt())
                .orderStartAt(productOrder.getStartAt())
                .orderEndAt(productOrder.getEndAt())
                .build();
    }

}
