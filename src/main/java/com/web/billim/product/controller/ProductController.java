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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final OrderService orderService;

    @GetMapping("/product/list")
    public String productList(@RequestParam(required = false, defaultValue = "0", value = "page") int page,
                              Model model
    ) {
        Page<ProductListResponse> productList = productService.findAllProduct(page);
        model.addAttribute("productList", productList);
        model.addAttribute("totalPage", productList.getTotalPages());
        return "pages/product/productList";
    }


    @GetMapping("/product/detail/{productId}")
    public String productDetail(@PathVariable("productId") int productId, Model model) {
        Product product = productService.retrieve(productId);
        ProductDetailResponse productDetail = ProductDetailResponse.of(product);
        List<LocalDate> alreadyDates = orderService.reservationDate(product);
        model.addAttribute("product", productDetail);
        model.addAttribute("alreadyDates",alreadyDates);
        return "pages/product/productDetail";
    }


    @GetMapping("/product/date")
    @ResponseBody
    public ResponseEntity<List<LocalDate>> test() {
        Product product = productService.retrieve(1);
        List<LocalDate> dates = orderService.reservationDate(product);
        return ResponseEntity.ok(dates);
    }


    @GetMapping("/myPage/purchase")
    public String myPage() {
        return "pages/myPage/myPurchaseList";
    }


    @GetMapping("/myPage/sales")
    public String myPageSalesManagement(Model model, User user) {
        List<MyProductSalesResponse> products = productService.myProduceSales(user);
        model.addAttribute("myProducts", products);
        return "pages/myPage/mySalesList";
    }


    @GetMapping("/product/enroll")
    public String productEnroll(Model model) {
        List<ProductCategory> categoryList = productService.categoryList();
        model.addAttribute("categoryList", categoryList);
        return "pages/product/productEnroll";
    }


    @PostMapping(value = "/product/enroll", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<Product> registerProduct(@ModelAttribute @Valid ProductRegisterRequest req,
                                                   User user
    ) {
        req.setRegisterMember(user.getMemberId());
        return ResponseEntity.ok(productService.register(req));
    }


}

