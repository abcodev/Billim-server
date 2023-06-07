package com.web.billim.product.dto.response;

import com.web.billim.member.domain.Member;
import com.web.billim.product.domain.ImageProduct;
import com.web.billim.product.domain.Product;
import com.web.billim.product.type.TradeMethod;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ProductDetailResponse {

    @ApiModelProperty("상품 고유번호")
    private long productId;
    @ApiModelProperty("상품명")
    private String productName;
    @ApiModelProperty("상품 상세설명")
    private String detail;
    @ApiModelProperty("대여료(/일)")
    private long price;
    private Member member;
    @ApiModelProperty("상품 이미지 url")
    private List<String> imageUrls;
    @ApiModelProperty("거래 방법")
    private List<TradeMethod> tradeMethods;
    @ApiModelProperty("이미 예약된 날짜")
    private List<LocalDate> alreadyDates;

    public static ProductDetailResponse of(Product product, List<LocalDate> alreadyDates){
        return ProductDetailResponse.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .member(product.getMember())
                .detail(product.getDetail())
                .price(product.getPrice())
                .imageUrls(
                        product.getImages().stream()
                                .map(ImageProduct::getUrl)
                                .collect(Collectors.toList())
                )
                .tradeMethods(product.getTradeMethods())
                .alreadyDates(alreadyDates)
                .build();
    }
}
