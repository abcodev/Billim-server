package com.web.billim.product.domain;

import com.web.billim.common.domain.JpaEntity;
import com.web.billim.product.dto.request.ProductRegisterRequest;
import com.web.billim.member.domain.Member;
import com.web.billim.product.type.TradeMethod;

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
    private Integer productId;

    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    @ManyToOne
    private ProductCategory productCategory;

    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    @ManyToOne
    private Member member;

    private String productName;

    private String detail;

    private int price;

    private String tradeMethod;

    @JoinColumn(name = "product_id")
    @OneToMany(fetch = FetchType.LAZY) // EAGER(즉시 로딩)
    private List<ImageProduct> images;

    public List<TradeMethod> getTradeMethods() {
        return Arrays.stream(tradeMethod.split(",")).map(TradeMethod::valueOf).collect(Collectors.toList());
    }

    public static Product generateNewProduct(ProductRegisterRequest request, ProductCategory category, Member member, List<ImageProduct> images) {
        return Product.builder()
                .productCategory(category)
                .member(member)
                .productName(request.getName())
                .detail(request.getDetail())
                .price(request.getPrice())
                .tradeMethod(request.getTradeMethods().stream().map(Objects::toString).collect(Collectors.joining(",")))
                .images(images)
                .build();
    }

}

