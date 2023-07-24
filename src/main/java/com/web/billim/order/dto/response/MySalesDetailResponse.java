package com.web.billim.order.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MySalesDetailResponse {

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
