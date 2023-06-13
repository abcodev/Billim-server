package com.web.billim.product.dto.response;

import com.web.billim.product.domain.ImageProduct;
import com.web.billim.product.domain.Product;
import com.web.billim.product.domain.ProductInterest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class MyInterestProduct {
    private long productId;
    private String productName;
    private String imageUrl;
    private Double starRating;
    private long price;


    public static MyInterestProduct of(ProductInterest productInterest){
        return MyInterestProduct.builder()
                .productId(productInterest.getProduct().getProductId())
                .productName(productInterest.getProduct().getProductName())
                .imageUrl(productInterest.getProduct().mainImage())
                .price(productInterest.getProduct().getPrice())
                .build();
    }

}
