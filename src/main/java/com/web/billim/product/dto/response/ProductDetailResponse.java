package com.web.billim.product.dto.response;

import com.web.billim.member.domain.Member;
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
public class ProductDetailResponse {

    private int productId;
    private String productName;
    private String detail;
    private int price;
    private Member member;
    private List<String> imageUrls;
    private List<TradeMethod> tradeMethods;
    private List<LocalDate> alreadyDates;

    /*
     *  응답데이터는 JSON
     *   객체 -> JSON 이 되어야함 : 이거를 데이터 파싱
     *   Jackson(ObjectMapper)
     *
     *  {
     *      "productId": 1,
     *      "productName": "테스트",
     *      "detail": "상품 설명",
     *      "price": 50000,
     *      "member": {
     *          "memberId": 10,
     *          ...
     *      },
     *      "imageUrls": [
     *          "https://s3/1",
     *          "https://s3/2",
     *          "https://s3/3"
     *      ],
     *      "tradeMethods": ["DIRECT"],
     *      "alreadyDates": ["2023-04-17", "2023-04-18"]
     *  }
     */

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
