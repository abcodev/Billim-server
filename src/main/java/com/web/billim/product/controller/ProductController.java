package com.web.billim.product.controller;

import com.web.billim.product.domain.Product;
import com.web.billim.product.dto.command.ProductRegisterCommand;
import com.web.billim.product.dto.command.ProductUpdateCommand;
import com.web.billim.product.dto.request.InterestRequest;
import com.web.billim.product.dto.request.ProductRegisterRequest;
import com.web.billim.product.dto.request.ProductUpdateRequest;
import com.web.billim.product.dto.response.*;
import com.web.billim.product.service.ProductInterestService;
import com.web.billim.product.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Tag(name = "상품", description = "ProductController")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final ProductInterestService productInterestService;

    @Operation(summary = "상품 등록", description = "이미지 1장부터 최대 5장까지 첨부 가능")
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> registerProduct(
            @AuthenticationPrincipal long memberId,
            @ModelAttribute @Valid ProductRegisterRequest req
    ) {
        req.setRegisterMember(memberId);
        ProductRegisterCommand command = new ProductRegisterCommand(req);
        return ResponseEntity.ok(productService.register(command));
    }

    @Operation(summary = "전체 상품목록 조회, 검색, 페이징", description = "전체 상품목록 조회, 카테고리별 검색, 키워드 검색, 페이징 처리")
    @Transactional
    @GetMapping("/list/search")
    public ResponseEntity<Page<ProductListResponse>> productList(
            @RequestParam(required = false, defaultValue = "") String category,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "1", value = "page") int page
    ) {
        PageRequest paging = PageRequest.of(page - 1, 20);
        return ResponseEntity.ok(productService.search(category, keyword, paging));
    }

    @Operation(summary = "상품 상세정보", description = "productId에 따른 상품 상세정보 & 이미 예약되어 이용할 수 없는 날짜")
    @GetMapping("/detail/{productId}")
    public ResponseEntity<ProductDetailResponse> productDetail(
            @PathVariable("productId") long productId
    ) {
        ProductDetailResponse resp = productService.retrieveDetail(productId);
        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "상품 수정시 기존 내용 조회", description = "상품 수정시 기존 정보 조회를 조회한다")
    @GetMapping("/update/{productId}")
    public ResponseEntity<ProductUpdateResponse> updateProductResponse(
            @PathVariable("productId") long productId
    ) {
        ProductUpdateResponse resp = productService.retrieveUpdateProduct(productId);
        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "상품 수정", description = "삭제할 이미지 deleteImages, 새로운 이미지 MultipartFile 형태로 전송")
    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateProduct(
            @AuthenticationPrincipal long memberId,
            @ModelAttribute @Valid ProductUpdateRequest req
    ) {
        req.setRegisterMember(memberId);
        ProductUpdateCommand command = new ProductUpdateCommand(req);
        productService.update(command);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "상품 삭제", description = "해당 회원이 작성한 상품 및 상품 이미지 삭제")
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @AuthenticationPrincipal long memberId,
            @PathVariable("productId") long productId
    ) {
        productService.delete(memberId, productId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "관심상품 등록, 삭제", description = "true 관심상품 등록, false 관심상품 삭제")
    @PostMapping("/interest")
    public ResponseEntity<Void> saveOrDeleteInterest(
            @AuthenticationPrincipal long memberId,
            @RequestBody InterestRequest interestRequest
    ) {
        productInterestService.saveOrDeleteInterest(memberId, interestRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "관심목록 조회", description = "상품 페이지에서 관심상품 목록 조회")
    @GetMapping("/interest")
    public ResponseEntity<MyInterestProductList> myInterestProductList(
            @AuthenticationPrincipal long memberId
    ) {
        return ResponseEntity.ok(productInterestService.myInterestProductList(memberId));
    }

    @Operation(summary = "마이페이지 관심 상품 목록 조회", description = "마이페이지에서 관심상품 목록 조회 - 페이징")
    @GetMapping("/my/interest")
    public ResponseEntity<Page<MyInterestProduct>> myInterestProduct(
            @AuthenticationPrincipal long memberId,
            @RequestParam(required = false, defaultValue = "1", value = "page") int page
    ) {
        PageRequest paging = PageRequest.of(page - 1, 9);
        Page<MyInterestProduct> interestProductList = productInterestService.myInterestProduct(memberId, paging);
        return ResponseEntity.ok(interestProductList);
    }

    @Operation(summary = "인기 상품 조회", description = "많이 본 상품 상품 리스트")
    @GetMapping("/list/most/popular")
    public ResponseEntity<List<MostProductList>> mostProductList() {
        return ResponseEntity.ok(productService.findMostPopularProduct());
    }

    @Operation(summary = "최근 본 상품 조회", description = "로그인 시 최근 본 상품 리스트")
    @GetMapping("/list/recent")
    public ResponseEntity<List<RecentProductResponse>> recentProductList(
            @AuthenticationPrincipal long memberId
    ) {
        return ResponseEntity.ok(productService.recentProductList(memberId));
    }

}

