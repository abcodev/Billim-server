package com.web.billim.product.dto.response;

import com.web.billim.product.domain.ImageProduct;
import com.web.billim.product.domain.Product;
import com.web.billim.product.type.TradeMethod;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class UpdateProductResponse {

    private String category;
    private long productId;
    private String rentalProduct;
    private String description;
    private long rentalFee;
    private List<TradeMethod> tradeMethods;
    private String place;
    private List<String> imageUrls;

    public static UpdateProductResponse of(Product product){
        return UpdateProductResponse.builder()
                .category(product.getProductCategory().getCategoryName())
                .productId(product.getProductId())
                .rentalProduct(product.getProductName())
                .description(product.getDetail())
                .rentalFee(product.getPrice())
                .tradeMethods(product.getTradeMethods())
                .place(product.getTradeArea())
                .imageUrls(
                        product.getImages().stream()
                                .map(ImageProduct::getUrl)
                                .collect(Collectors.toList())
                )
                .build();
    }


}
