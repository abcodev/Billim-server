package com.web.billim.review.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.billim.order.domain.ProductOrder;
import com.web.billim.review.domain.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class WrittenReviewList {

    private long orderId;
    private String sellerNickname;
    private String productName;
    private long price;
    private String productImageUrl;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startAt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endAt;

    private long reviewId;
    private String content;
    private long starRating;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSS")
    private LocalDateTime createAt;

    private String isWritable;

    public static WrittenReviewList of(Review review) {
        var productOrder = review.getProductOrder();
        var product = review.getProductOrder().getProduct();
        return WrittenReviewList.builder()
                .orderId(productOrder.getOrderId())
                .sellerNickname(product.getMember().getNickname())
                .productName(product.getProductName())
                .price(productOrder.getPrice())
                .productImageUrl(product.mainImage())
                .startAt(productOrder.getStartAt())
                .endAt(productOrder.getEndAt())
                .reviewId(review.getReviewId())
                .content(review.getContent())
                .starRating(review.getStarRating())
                .createAt(review.getCreatedAt())
                .isWritable("false")
                .build();
    }

}
