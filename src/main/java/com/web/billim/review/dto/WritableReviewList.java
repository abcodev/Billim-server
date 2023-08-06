package com.web.billim.review.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.billim.order.domain.ProductOrder;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class WritableReviewList {

    private long orderId;
    private String sellerNickname;
    private String productName;
    private long price;
    private String productImageUrl;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startAt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSS")
    private LocalDateTime createAt;
    private String isWritable;

    public static WritableReviewList of(ProductOrder productOrder) {
        return WritableReviewList.builder()
                .orderId(productOrder.getOrderId())
                .sellerNickname(productOrder.getProduct().getMember().getNickname())
                .productName(productOrder.getProduct().getProductName())
                .price(productOrder.getPrice())
                .productImageUrl(productOrder.getProduct().mainImage())
                .startAt(productOrder.getStartAt())
                .endAt(productOrder.getEndAt())
                .createAt(productOrder.getCreatedAt())
                .isWritable("true")
                .build();
    }

}
