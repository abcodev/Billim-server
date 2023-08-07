package com.web.billim.review.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.billim.review.domain.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProductReviewListResponse {

    private long reviewId;
    private String nickname;
    private String profileImageUrl;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSS")
    private LocalDateTime createdDate;

    public static ProductReviewListResponse of(Review review){
        return ProductReviewListResponse.builder()
                .reviewId(review.getReviewId())
                .nickname(review.getProductOrder().getMember().getNickname())
                .profileImageUrl(review.getProductOrder().getMember().getProfileImageUrl())
                .content(review.getContent())
                .createdDate(review.getCreatedAt())
                .build();
    }

}
