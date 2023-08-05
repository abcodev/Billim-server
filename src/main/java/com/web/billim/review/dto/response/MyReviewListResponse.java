package com.web.billim.review.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MyReviewListResponse {

    private long orderId;
    private String productName;
    private String productImageUrl;
    private long price;
    private String sellerNickname;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSS")
    private LocalDateTime createDate;
    private String reviewContent;

}
