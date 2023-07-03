package com.web.billim.product.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.billim.product.domain.ImageProduct;
import com.web.billim.product.domain.Product;
import com.web.billim.product.type.TradeMethod;
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

    private long sellerMemberId;
    private String sellerNickname;
    private String sellerGrade;
    private String sellerProfileImage;

    private long productId;
    private String productName;
    private String detail;
    private long price;
    private List<TradeMethod> tradeMethods;
    private String place;
    private List<String> imageUrls;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private List<LocalDate> alreadyDates;

    private List<ProductReviewList> productReviewLists;
    private double starRating;

    public static ProductDetailResponse of(Product product, List<LocalDate> alreadyDates, double starRating){
        var member = product.getMember();
        return ProductDetailResponse.builder()
                .sellerMemberId(member.getMemberId())
                .sellerNickname(member.getNickname())
                .sellerGrade(member.getGrade().getAuthority())
                .sellerProfileImage(member.getProfileImageUrl())
                .productId(product.getProductId())
                .productName(product.getProductName())
                .detail(product.getDetail())
                .price(product.getPrice())
                .tradeMethods(product.getTradeMethods())
                .place(product.getTradeArea())
                .imageUrls(
                        product.getImages().stream()
                                .map(ImageProduct::getUrl)
                                .collect(Collectors.toList())
                )
                .alreadyDates(alreadyDates)
                .starRating(starRating)
                .build();
    }

}
