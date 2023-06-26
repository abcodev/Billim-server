package com.web.billim.product.dto.request;

import com.web.billim.order.domain.ProductOrder;
import com.web.billim.product.domain.Product;
import com.web.billim.product.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewWriteRequest {
    private long orderId;
    private String content;
    private long starRating;

    @Builder
    public static Review of(ReviewWriteRequest reviewWriteRequest, ProductOrder productOrder){
        return Review.builder()
                .productOrder(productOrder)
                .content(reviewWriteRequest.getContent())
                .starRating(reviewWriteRequest.getStarRating())
                .build();
    }
}
