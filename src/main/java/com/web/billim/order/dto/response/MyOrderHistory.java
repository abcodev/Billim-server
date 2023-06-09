package com.web.billim.order.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.billim.order.domain.ProductOrder;
import com.web.billim.order.type.ProductOrderStatus;
import com.web.billim.product.domain.ImageProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class MyOrderHistory {

    private long orderId;
    private long productId;
    private long sellId;
    private String productName;
    private long price;
    private String imageUrl;
    private ProductOrderStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate orderTime;


    public static MyOrderHistory from(ProductOrder productOrder){
        List<ImageProduct> images = productOrder.getProduct().getImages();
        String imageUrl = (images !=null && !images.isEmpty()) ? images.get(0).getUrl() : null;

        return MyOrderHistory.builder()
                .orderId(productOrder.getOrderId())
                .productId(productOrder.getProduct().getProductId())
                .sellId(productOrder.getProduct().getMember().getMemberId())
                .productName(productOrder.getProduct().getProductName())
                .price(productOrder.getPrice())
                .imageUrl(imageUrl)
                .status(productOrder.getStatus())
                .orderTime(productOrder.getStartAt())
                .build();
    }

}
