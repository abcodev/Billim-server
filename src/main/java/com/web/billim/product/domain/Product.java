package com.web.billim.product.domain;

import com.web.billim.common.domain.JpaEntity;
import com.web.billim.product.dto.request.ProductRegisterRequest;
import com.web.billim.member.domain.Member;
import com.web.billim.product.type.TradeMethod;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "product")
@Builder
@Getter
public class Product extends JpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @ApiModelProperty(value = "상품 카테고리")
    private ProductCategory productCategory;

    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ApiModelProperty(value = "상품명")
    private String productName;

    @ApiModelProperty(value = "상품설명")
    private String detail;

    @ApiModelProperty(value = "일일 대여료")
    private long price;

    @ApiModelProperty(value = "거래 방법")
    private String tradeMethod;

    @ApiModelProperty(value = "직거래 지역")
    private String tradeArea;

    @JoinColumn(name = "product_id")
    @OneToMany(fetch = FetchType.LAZY) // EAGER(즉시 로딩)
    @ApiModelProperty("상품 이미지 리스트 주소")
    private List<ImageProduct> images;

    public List<TradeMethod> getTradeMethods() {
        return Arrays.stream(tradeMethod.split(",")).map(TradeMethod::valueOf).collect(Collectors.toList());
    }

    public String mainImage(){
        return this.images.get(0).getUrl();
    }

    public static Product generateNewProduct(ProductRegisterRequest request, ProductCategory category, Member member, List<ImageProduct> images) {
        return Product.builder()
                .productCategory(category)
                .member(member)
                .productName(request.getName())
                .detail(request.getDetail())
                .price(request.getPrice())
                .tradeMethod(request.getTradeMethods().stream().map(Objects::toString).collect(Collectors.joining(",")))
                .tradeArea(request.getTradeArea())
                .images(images)
                .build();
    }


//    public static Product generateNewProduct(ProductRegisterRequest request, ProductCategory category, Member member, List<ImageProduct> images) {
//        return Product.builder()
//                .productCategory(category)
//                .member(member)
//                .productName(request.getName())
//                .detail(request.getDetail())
//                .price(request.getPrice())
//                .tradeMethod(request.getTradeMethods().stream().map(Objects::toString).collect(Collectors.joining(",")))
//                .place(request.getPlace())
//                .images(images)
//                .build();
//    }




}

