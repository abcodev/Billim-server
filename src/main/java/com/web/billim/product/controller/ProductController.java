package com.web.billim.product.controller;

import com.web.billim.order.service.OrderService;
import com.web.billim.product.domain.Product;
import com.web.billim.product.domain.ProductCategory;
import com.web.billim.product.dto.request.InterestRequest;
import com.web.billim.product.dto.request.ProductRegisterRequest;
import com.web.billim.product.dto.request.ReviewWriteRequest;
import com.web.billim.product.dto.response.*;
import com.web.billim.product.repository.ProductRepository;
import com.web.billim.product.service.ProductInterestService;
import com.web.billim.product.service.ProductService;
import com.web.billim.product.service.ReviewService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final OrderService orderService;
    private final ProductRepository productRepository;
    private final ProductInterestService productInterestService;
    private final ReviewService reviewService;

    @ApiOperation(value = "상품 등록")
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> registerProduct(
            @AuthenticationPrincipal long memberId,
            @ModelAttribute @Valid ProductRegisterRequest request
    ) {
        request.setRegisterMember(memberId);
        return ResponseEntity.ok(productService.register(request));
    }

    @ApiOperation(value = "*전체 상품목록 조회, 검색, 페이징", notes = "전체 상품목록 조회, 카테고리별 검색, 키워드 검색, 페이징 처리")
    @Transactional
    @GetMapping("/list/search")
    public ResponseEntity<Page<ProductListResponse>> productList(
            @RequestParam(required = false, defaultValue = "") String category,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "1", value = "page") int page
    ) {
        PageRequest paging = PageRequest.of(page - 1, 20);
        Page<ProductListResponse> resp = productRepository.findAllByKeyword(category, keyword, paging)
            .map(product -> ProductListResponse.of(product, reviewService.calculateStarRating(product.getProductId())));
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/list/category")
    public ResponseEntity<List<ProductCategory>> productEnroll() {
        List<ProductCategory> categoryList = productService.categoryList();
        return ResponseEntity.ok(categoryList);
    }

    @ApiOperation(value = "상품 상세정보", notes = "productId에 따른 상품 상세정보 & 이미 예약되어 이용할 수 없는 날짜")
    @GetMapping("/detail/{productId}")
    public ResponseEntity<ProductDetailResponse> productDetail(@PathVariable("productId") long productId) {
        ProductDetailResponse resp = productService.retrieveDetail(productId);
        resp.setProductReviewLists(reviewService.reviewList(productId));
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/detail/date/{productId}")
    public ResponseEntity<List<LocalDate>> alreadyReservedDate(@PathVariable("productId") long productId) {
        List<LocalDate> dates = orderService.reservationDate(productId);
        return ResponseEntity.ok(dates);
    }


    // 상품 수정
    @PutMapping("/update")
    public ResponseEntity<Void> updateProduct(@RequestParam("productId") long productId) {
        productService.update(productId);
        return ResponseEntity.ok().build();
    }


    // 상품삭제
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteProduct(
            @RequestParam("productId") long productId) {
        productService.delete(productId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "*인기 상품 조회",notes = "사람들이 많이 본 상품 사진 리스트")
    @GetMapping("/list/most/popular")
    public ResponseEntity<List<MostProductList>> mostProductList() {
        return ResponseEntity.ok(productService.findMostPopularProduct());
    }

    @ApiOperation(value = "관심상품 등록, 삭제", notes = "true 관심상품등록, false 관심등록삭제")
    @PostMapping("/interest")
    public ResponseEntity<Void> saveOrDeleteInterest(
            @AuthenticationPrincipal long memberId,
            @RequestBody InterestRequest interestRequest
    ) {
        productInterestService.saveOrDeleteInterest(memberId,interestRequest);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "마이페이지 관심목록 조회")
    @GetMapping("/my/interestList")
    public ResponseEntity<MyInterestProductList> myInterestProductList(@AuthenticationPrincipal long memberId) {
        return ResponseEntity.ok(productInterestService.myInterestProductList(memberId));
    }


    // 판매중인 상품 목록 조회
    @GetMapping("/my/sell/list")
    public ResponseEntity<?> mySellList() {
        return ResponseEntity.ok().build();
    }

    // 판매중인 상품 클릭시 판매 주문 내역 조회
    @GetMapping("/my/sell/detail")
    public ResponseEntity<?> mySellDetail() {
        return ResponseEntity.ok().build();
    }


    @ApiOperation(value = "회원이 이용한 상품 리뷰 불러오기", notes = "마이페이지 헤더에서 리뷰 클릭시 작성한 후기, 작성 해야하는 리뷰를 불러옴")
    @GetMapping("/my/product/review/")
    public ResponseEntity<Void> myProductReview(@AuthenticationPrincipal long memberId) {
        reviewService.findMyProductReview(memberId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "리뷰 작성하기" , notes = "리뷰 작성하기, productId를 넘겨주세요.")
    @PostMapping("/review/write")
    public ResponseEntity<Void> productWrite(@RequestBody ReviewWriteRequest reviewWriteRequest) {
        reviewService.productReviewWrite(reviewWriteRequest);
        return ResponseEntity.ok().build();
    }

//    @ApiOperation(value = "상품 리뷰 리스트", notes = "상품 디테일에서 리뷰리스트 가져오기")
//    @GetMapping("/review/list")
//    public ResponseEntity<List<ProductReviewList>> reviewList(@RequestParam long productId) {
//        return ResponseEntity.ok(reviewService.reviewList(productId));
//    }

}

