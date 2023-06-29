package com.web.billim.product.dto.response;

import com.web.billim.product.domain.ImageProduct;
import com.web.billim.product.domain.Product;
import com.web.billim.product.domain.ProductCategory;
import io.swagger.annotations.ApiModelProperty;
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
    private String detail;
    private long price;
    @ApiModelProperty(value = "상품 이미지 url")
    private List<String> imageUrls;
    @ApiModelProperty(value = "리뷰 평균 별점")
    private double starRating;

    public static ProductListResponse of(Product product, double starRating) {
        return ProductListResponse.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
//                .productCategory(product.getProductCategory())
                .categoryName(product.getProductCategory().getCategoryName())
                .detail(product.getDetail())
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
