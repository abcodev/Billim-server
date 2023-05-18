package com.web.billim.product.domain;

import com.web.billim.common.domain.JpaEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "image_product")
@Getter
@Builder
public class ImageProduct extends JpaEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_product_id")
    private Long id;

    @ApiModelProperty(value = "상품 이미지 주소")
    private String url;

    public static ImageProduct of(String url) {
        return ImageProduct.builder()
                .url(url)
                .build();
    }
}
