package com.web.billim.product.dto.response;

import com.web.billim.product.domain.ImageProduct;
import com.web.billim.product.domain.Product;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ProductListResponse {

    private long productId;
    private String productName;
    private String categoryName;
    private long price;
    private List<String> imageUrls;
    private double starRating;

    public static ProductListResponse of(Product product, double starRating) {
        return ProductListResponse.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .categoryName(product.getProductCategory().getCategoryName())
                .price(product.getPrice())
                .imageUrls(
                        product.getImages().stream()
                                .map(ImageProduct::getUrl)
                                .collect(Collectors.toList())
                )
                .starRating(starRating)
                .build();
    }

}
