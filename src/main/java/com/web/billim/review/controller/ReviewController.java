package com.web.billim.review.controller;

import com.web.billim.review.dto.request.ReviewWriteRequest;
import com.web.billim.review.dto.response.MyReviewListResponse;
import com.web.billim.review.dto.response.ProductReviewListResponse;
import com.web.billim.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "리뷰", description = "ReviewController")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 작성하기" , description = "리뷰를 작성한다. productId를 넘겨주세요.")
    @PostMapping("/write")
    public ResponseEntity<Void> reviewWrite(@RequestBody ReviewWriteRequest reviewWriteRequest) {
        reviewService.productReviewWrite(reviewWriteRequest);
        return ResponseEntity.ok().build();
    }

//    @Operation(summary = "리뷰 작성 가능한 상품 목록 불러오기", description = "마이페이지 헤더에서 후기작성 개수 클릭시 회원이 이용한 상품 중 리뷰 작성 가능한 상품 목록 불러온다.")
//    @GetMapping("/writable-list")
//    public ResponseEntity<List<WritableReviewList>> myWritableReview(@AuthenticationPrincipal long memberId) {
//        return ResponseEntity.ok(reviewService.findMyWritableReview(memberId));
//    }

    @Operation(summary = "상품 디테일 리뷰 목록", description = "상품에 따른 리뷰 전체 불러오기")
    @GetMapping("/list/{productId}")
    public ResponseEntity<Page<ProductReviewListResponse>> productReviewList(
            @PathVariable long productId,
            @PageableDefault(size = 4) Pageable pageable   // TODO : HandlerMethodArgumentResolver
    ) {
        Page<ProductReviewListResponse> reviewList = reviewService.productReviewList(productId, pageable);
        return ResponseEntity.ok(reviewList);
    }

    @Operation(summary = "마이페이지 리뷰 목록", description = "작성 가능한 리뷰와 작성한 리뷰 목록 전체 불러오기")
    @GetMapping("/my/list")
    public ResponseEntity<MyReviewListResponse> myReviewList(
            @AuthenticationPrincipal long memberId
    ) {
        return ResponseEntity.ok(reviewService.myReviewList(memberId));
    }

}
