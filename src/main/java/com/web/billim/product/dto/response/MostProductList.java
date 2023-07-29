package com.web.billim.product.dto.response;

import com.web.billim.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MostProductList {

    private long productId;
    private String imageUrl;
    private String productName;

    public static MostProductList of(Product product){
        return MostProductList.builder()
                .productId(product.getProductId())
                .imageUrl(product.mainImage())
                .productName(product.getProductName())
                .build();
    }

}
