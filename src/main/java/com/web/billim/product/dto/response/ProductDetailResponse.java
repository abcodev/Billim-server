package com.web.billim.product.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.billim.product.domain.ImageProduct;
import com.web.billim.product.domain.Product;
import com.web.billim.product.type.TradeMethod;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
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

    private long sellerMemberId;
    private String sellerNickname;
    private String sellerGrade;
    private String sellerProfileImage;

    @ApiModelProperty("상품 이미지 url")
    private List<String> imageUrls;
    @ApiModelProperty("거래 방법")
    private List<TradeMethod> tradeMethods;
    @ApiModelProperty("이미 예약된 날짜")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private List<LocalDate> alreadyDates;

    @ApiModelProperty("상품 후기")
    private List<ProductReviewList> productReviewLists;

    public static ProductDetailResponse of(Product product, List<LocalDate> alreadyDates){
        var member = product.getMember();
        return ProductDetailResponse.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .sellerMemberId(member.getMemberId())
                .sellerNickname(member.getNickname())
                .sellerGrade(member.getGrade().getAuthority())
                .sellerProfileImage(member.getProfileImageUrl())
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
