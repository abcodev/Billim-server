package com.web.billim.product.domain;

import com.web.billim.common.domain.JpaEntity;
import com.web.billim.product.dto.command.ProductRegisterCommand;
import com.web.billim.product.dto.command.ProductUpdateCommand;
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
    private Long productId;

    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductCategory productCategory;

    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String productName;
    private String detail;
    private long price;
    private String tradeMethod;
    private String tradeArea;

    @JoinColumn(name = "product_id")
    @OneToMany(fetch = FetchType.LAZY)
    private List<ImageProduct> images;

    public List<TradeMethod> getTradeMethods() {
        return Arrays.stream(tradeMethod.split(",")).map(TradeMethod::valueOf).collect(Collectors.toList());
    }

    public String mainImage(){
        return this.images.get(0).getUrl();
    }

    public static Product generateNewProduct(ProductRegisterCommand command, ProductCategory category, Member member, List<ImageProduct> images) {
        return Product.builder()
                .productCategory(category)
                .member(member)
                .productName(command.getRentalProduct())
                .detail(command.getDescription())
                .price(command.getRentalFee())
                .tradeMethod(command.getTradeMethods().stream().map(Objects::toString).collect(Collectors.joining(",")))
                .tradeArea(command.getPlace())
                .images(images)
                .build();
    }

	public void update(ProductUpdateCommand command, List<ImageProduct> appendImages, ProductCategory category) {
        this.productCategory = category;
        this.productName = command.getProductName();
        this.detail = command.getProductDetail();
        this.price = command.getPrice();
        this.tradeMethod = command.getTradeMethods().stream().map(Objects::toString).collect(Collectors.joining(","));
        this.tradeArea = command.getTradeArea();
        this.images.addAll(appendImages);
	}

}

