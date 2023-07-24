package com.web.billim.product.dto.response;

import com.web.billim.product.domain.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecentProductResponse {

    private long memberId;
    private long productId;
    private String productName;
    private String productImageUrl;

    public static RecentProductResponse of(Product product) {
        return RecentProductResponse.builder()
                .memberId(product.getMember().getMemberId())
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productImageUrl(product.mainImage())
                .build();
    }

}
