package com.web.billim.review.controller;

import com.web.billim.review.dto.request.ReviewWriteRequest;
import com.web.billim.review.service.ReviewService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @ApiOperation(value = "리뷰 작성하기" , notes = "리뷰 작성하기, productId를 넘겨주세요.")
    @PostMapping("/write")
    public ResponseEntity<Void> productWrite(@RequestBody ReviewWriteRequest reviewWriteRequest) {
        reviewService.productReviewWrite(reviewWriteRequest);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "회원이 이용한 상품 리뷰 불러오기", notes = "마이페이지 헤더에서 리뷰 클릭시 작성한 후기, 작성 해야하는 리뷰를 불러옴")
    @GetMapping("/my/list")
    public ResponseEntity<Void> myProductReview(@AuthenticationPrincipal long memberId) {
        reviewService.findMyProductReview(memberId);
        return ResponseEntity.ok().build();
    }

}
