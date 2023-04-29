package com.web.billim.product.controller;

import com.web.billim.order.service.OrderService;
import com.web.billim.product.domain.Product;
import com.web.billim.product.domain.ProductCategory;
import com.web.billim.product.dto.request.ProductRegisterRequest;
import com.web.billim.product.dto.response.MyProductSalesResponse;
import com.web.billim.product.dto.response.ProductDetailResponse;
import com.web.billim.product.dto.response.ProductListResponse;
import com.web.billim.product.service.ProductService;
import com.web.billim.security.domain.User;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final OrderService orderService;

    @ApiOperation(value = "전체 상품목록 조회", notes = "전체 상품목록조회, 페이징 처리")
    @GetMapping("/product/list")
    public ResponseEntity<Page<ProductListResponse>> productList(
            @RequestParam(required = false, defaultValue = "0", value = "page") int page
    ) {
        Page<ProductListResponse> productList = productService.findAllProduct(page);
        return ResponseEntity.ok(productList);
    }

    //
    @GetMapping("/product/detail/{productId}")
    public ResponseEntity<ProductDetailResponse> productDetail(
            @PathVariable("productId") int productId, Model model
    ) {
        Product product = productService.retrieve(productId);
        List<LocalDate> alreadyDates = orderService.reservationDate(product);
        return ResponseEntity.ok(ProductDetailResponse.of(product, alreadyDates));
    }



    @GetMapping("/product/date")
    @ResponseBody
    public ResponseEntity<List<LocalDate>> test() {
        Product product = productService.retrieve(1);
        List<LocalDate> dates = orderService.reservationDate(product);
        return ResponseEntity.ok(dates);
    }


//    @GetMapping("/myPage/purchase")
//    public String myPage() {
//        return "pages/myPage/myPurchaseList";
//    }


    @GetMapping("/myPage/sales")
    public String myPageSalesManagement(Model model, User user) {
        List<MyProductSalesResponse> products = productService.myProduceSales(user);
        model.addAttribute("myProducts", products);
        return "pages/myPage/mySalesList";
    }


    //
    // FE 는 상품 등록 페이지로 진입
    // 필요한 정보들 -> API 호출을 통해서 카테고리 목록을 가져옴
    @ApiOperation(value = "상품 카테고리", notes = "상품 카테고리 불러오기")
    @GetMapping("/product/category")
    @ResponseBody
    public ResponseEntity<List<ProductCategory>> productEnroll() {
        List<ProductCategory> categoryList = productService.categoryList();
        return ResponseEntity.ok(categoryList);
    }


    @PostMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<Product> registerProduct(
            @ModelAttribute @Valid ProductRegisterRequest req,
            User user
    ) {
        req.setRegisterMember(user.getMemberId());
        return ResponseEntity.ok(productService.register(req));
    }


}

