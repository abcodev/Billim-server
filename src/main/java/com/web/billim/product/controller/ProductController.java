package com.web.billim.product.controller;

import com.web.billim.order.service.OrderService;
import com.web.billim.product.domain.Product;
import com.web.billim.product.domain.ProductCategory;
import com.web.billim.product.dto.request.InterestRequest;
import com.web.billim.product.dto.request.ProductRegisterRequest;
import com.web.billim.product.dto.response.MostProductList;
import com.web.billim.product.dto.response.ProductDetailResponse;
import com.web.billim.product.dto.response.ProductListResponse;
import com.web.billim.product.repository.ProductRepository;
import com.web.billim.product.service.ProductInterestService;
import com.web.billim.product.service.ProductRedisService;
import com.web.billim.product.service.ProductService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
import java.nio.file.LinkOption;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final OrderService orderService;
    private final ProductRepository productRepository;
    private final ProductRedisService productRedisService;
    private final ProductInterestService productInterestService;

    @ApiOperation(value = "상품 등록")
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> registerProduct(
            @ModelAttribute @Valid ProductRegisterRequest request,
            @AuthenticationPrincipal long memberId
    ) {
        request.setRegisterMember(memberId);
        return ResponseEntity.ok().build();
    }

    @Transactional
    @GetMapping("/search/{keyword}")
    public ResponseEntity<Page<ProductListResponse>> test(
        @PathVariable String keyword,
        @RequestParam(required = false, defaultValue = "0", value = "page") int page
    ) {
        PageRequest paging = PageRequest.of(page, 20);
        Page<ProductListResponse> resp = productRepository.findAllByKeyword(keyword, paging)
            .map(product -> ProductListResponse.of(product, 5.0));
        return ResponseEntity.ok(resp);
    }


    @ApiOperation(value = "전체 상품목록 조회", notes = "전체 상품목록조회, 페이징")
    @GetMapping("/list")
    public ResponseEntity<Page<ProductListResponse>> productList(
            @RequestParam(required = false, defaultValue = "0", value = "page") int page
    ) {
        Page<ProductListResponse> productList = productService.findAllProduct(page);
        return ResponseEntity.ok(productList);
    }

    /*
        JPA 의 Persistence Context - 1차 캐시, 지연 로딩, 변경 감지,,,
         1. Persistence Context 는 임시 공간
         2. 만들어지는 시점 : Transaction 이 시작될 때
         3. 사라지는 시점 : Transaction 이 끝날 때
     */
    @ApiOperation(value = "상품 상세정보", notes = "productId에 따른 상품 상세정보 & 이미 예약되어 이용할 수 없는 날짜")
    @GetMapping("/detail/{productId}")
    public ResponseEntity<ProductDetailResponse> productDetail(@PathVariable("productId") long productId) {
        ProductDetailResponse resp = productService.retrieveDetail(productId);
        productRedisService.saveProduct(productId);
        // 비영속
        // Product product = productService.retrieve(productId);
        // List<LocalDate> alreadyDates = orderService.reservationDate(product);
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "인기 상품 조회",notes = "사람들이 많이 본 상품 사진 리스트")
    @GetMapping("/most/popular")
    public ResponseEntity<List<MostProductList>> mostProductList(){
        return ResponseEntity.ok(productRedisService.rankPopularProduct());
    }


    @GetMapping("/product/test")
    public ResponseEntity<?> productTest(
    ){
        long productId = 1;
        productRedisService.saveProduct(productId);
        return ResponseEntity.ok(200);
    }


    @ApiOperation(value = "상품 예약된 날짜 조회", notes = "예약중이어서 이용할 수 없는 날짜 조회")
    @GetMapping("/date/{productId}")
    public ResponseEntity<List<LocalDate>> alreadyReservedDate(@PathVariable("productId") long productId) {
        List<LocalDate> dates = orderService.reservationDate(productId);
        return ResponseEntity.ok(dates);
    }

    @ApiOperation(value = "*상품 카테고리 목록", notes = "상품 카테고리 목록 조회")
    @GetMapping("/category")
    public ResponseEntity<List<ProductCategory>> productEnroll() {
        List<ProductCategory> categoryList = productService.categoryList();
        return ResponseEntity.ok(categoryList);
    }



    // 상품 수정


    // 상품 삭제



//    @PostMapping("/my/interest")
//    public ResponseEntity<?> myInterestProduct(@AuthenticationPrincipal long memberId,
//                                               @ModelAttribute InterestRequest interestRequest
//                                               ){
//        productInterestService.myInterestProduct(memberId,interestRequest);
//
//        return
//    }
}

