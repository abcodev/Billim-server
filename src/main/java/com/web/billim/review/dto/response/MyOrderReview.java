package com.web.billim.review.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class MyOrderReview {

    private long orderId;
    private String productName;
    private String productImageUrl;
    private long price;
    private String buyerNickname;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSS")
    private LocalDateTime createDate;
    private String reviewContent;

}
