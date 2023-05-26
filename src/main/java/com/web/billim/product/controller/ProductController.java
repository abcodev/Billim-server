package com.web.billim.product.controller;

import com.web.billim.order.service.OrderService;
import com.web.billim.product.domain.Product;
import com.web.billim.product.domain.ProductCategory;
import com.web.billim.product.dto.request.ProductRegisterRequest;
import com.web.billim.product.dto.response.ProductDetailResponse;
import com.web.billim.product.dto.response.ProductListResponse;
import com.web.billim.product.service.ProductService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @ApiOperation(value = "전체 상품목록 조회", notes = "전체 상품목록조회, 페이징")
    @GetMapping("/list")
    public ResponseEntity<Page<ProductListResponse>> productList(
            @RequestParam(required = false, defaultValue = "0", value = "page") int page
    ) {
        Page<ProductListResponse> productList = productService.findAllProduct(page);
        return ResponseEntity.ok(productList);
    }


    @GetMapping("/detail/{productId}")
    public ResponseEntity<ProductDetailResponse> productDetail(
            @PathVariable("productId") long productId
    ) {
        Product product = productService.retrieve(productId);
        List<LocalDate> alreadyDates = orderService.reservationDate(product);
        return ResponseEntity.ok(ProductDetailResponse.of(product, alreadyDates));
    }

    @GetMapping("/date")
    @ResponseBody
    public ResponseEntity<List<LocalDate>> test() {
        Product product = productService.retrieve(1);
        List<LocalDate> dates = orderService.reservationDate(product);
        return ResponseEntity.ok(dates);
    }

    // ok
    @ApiOperation(value = "상품 카테고리 목록", notes = "상품 전체 카테고리 목록 조회")
    @GetMapping("/category")
    @ResponseBody
    public ResponseEntity<List<ProductCategory>> productEnroll() {
        List<ProductCategory> categoryList = productService.categoryList();
        return ResponseEntity.ok(categoryList);
    }

    //
    @ApiOperation(value = "상품 등록")
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<Product> registerProduct(
            @ModelAttribute @Valid ProductRegisterRequest req,
            @AuthenticationPrincipal long memberId
    ) {
        req.setRegisterMember(memberId);
        return ResponseEntity.ok(productService.register(req));
    }

}

