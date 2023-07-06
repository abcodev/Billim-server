package com.web.billim.product.dto.response;

import com.web.billim.order.domain.ProductOrder;
import com.web.billim.product.domain.ImageProduct;
import com.web.billim.product.domain.Product;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class MySellDetailResponse {

    private long memberId;

    private long productId;
    private String productName;
    private String imageUrls;

    private long productOrderId;
    private long buyerId;
    private String tradeMethod;
    private String buyerAddress;
    private String buyerPhone;
    private String status;

    private LocalDate startAt;
    private LocalDate endAt;


}
