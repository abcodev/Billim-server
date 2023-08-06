package com.web.billim.review.dto.response;

import com.web.billim.review.dto.WrittenReviewList;
import com.web.billim.review.dto.WritableReviewList;
import lombok.Getter;

import java.util.List;

@Getter
public class MyReviewListResponse {
    List<WritableReviewList> writableReviewList;
    List<WrittenReviewList> writtenReviewList;
}
