package com.web.billim.product.controller;

import com.web.billim.order.service.OrderService;
import com.web.billim.product.domain.Product;
import com.web.billim.product.domain.ProductCategory;
import com.web.billim.product.dto.request.ProductRegisterRequest;
import com.web.billim.product.dto.response.ProductDetailResponse;
import com.web.billim.product.dto.response.ProductListResponse;
import com.web.billim.product.repository.ProductRepository;
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

    @ApiOperation(value = "상품 상세정보", notes = "productId에 따른 상품 상세정보 & 이미 예약되어 이용할 수 없는 날짜")
    @GetMapping("/detail/{productId}")
    public ResponseEntity<ProductDetailResponse> productDetail(@PathVariable("productId") long productId) {
        Product product = productService.retrieve(productId);
        List<LocalDate> alreadyDates = orderService.reservationDate(product);
        return ResponseEntity.ok(ProductDetailResponse.of(product, alreadyDates));
    }

    @ApiOperation(value = "상품 예약된 날짜 조회", notes = "예약중이어서 이용할 수 없는 날짜 조회")
    @GetMapping("/date/{productId}")
    public ResponseEntity<List<LocalDate>> alreadyReservedDate(@PathVariable("productId") long productId) {
        Product product = productService.retrieve(productId);
        List<LocalDate> dates = orderService.reservationDate(product);
        return ResponseEntity.ok(dates);
    }

    @ApiOperation(value = "*상품 카테고리 목록", notes = "상품 전체 카테고리 목록 조회")
    @GetMapping("/category")
    public ResponseEntity<List<ProductCategory>> productEnroll() {
        List<ProductCategory> categoryList = productService.categoryList();
        return ResponseEntity.ok(categoryList);
    }


    @ApiOperation(value = "상품 등록")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "name", value = "상품명")
    )
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> registerProduct(@ModelAttribute @Valid ProductRegisterRequest request,
                                                   @AuthenticationPrincipal long memberId
    ) {
        request.setRegisterMember(memberId);
        return ResponseEntity.ok(productService.register(request));
    }

}

