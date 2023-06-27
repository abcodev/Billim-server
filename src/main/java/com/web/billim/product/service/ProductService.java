package com.web.billim.product.service;

import com.web.billim.common.exception.NotFoundException;
import com.web.billim.common.exception.handler.ErrorCode;
import com.web.billim.infra.ImageUploadService;
import com.web.billim.member.domain.Member;
import com.web.billim.member.repository.MemberRepository;
import com.web.billim.order.service.OrderService;
import com.web.billim.product.domain.ImageProduct;
import com.web.billim.product.domain.Product;
import com.web.billim.product.domain.ProductCategory;
import com.web.billim.product.dto.request.ProductRegisterRequest;
import com.web.billim.product.dto.response.ProductDetailResponse;
import com.web.billim.product.dto.response.MostProductList;
import com.web.billim.product.dto.response.ProductListResponse;
import com.web.billim.product.repository.ImageProductRepository;
import com.web.billim.product.repository.ProductCategoryRepository;
import com.web.billim.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ImageProductRepository imageProductRepository;
    private final OrderService orderService;
    private final ImageUploadService imageUploadService;
    private final ProductRedisService productRedisService;
    private final ReviewService reviewService;

    @Transactional
    public Product register(ProductRegisterRequest request) {
        Member registerMember = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        ProductCategory productCategory = productCategoryRepository.findByCategoryName(request.getCategory())
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

        // 1. 이미지 저장
        List<ImageProduct> images = request.getImages().stream().map(image -> {
            String url = imageUploadService.upload(image, "product");
            return imageProductRepository.save(ImageProduct.of(url));
        }).collect(Collectors.toList());

        // 2. Product 정보 데이터베이스에 저장 & 반환
        Product product = Product.generateNewProduct(request, productCategory, registerMember, images);
        return productRepository.save(product);
    }

    public List<ProductCategory> categoryList() {
        return productCategoryRepository.findAll();
    }

    @Transactional
    public Page<ProductListResponse> findAllProduct(int page) {
        PageRequest paging = PageRequest.of(page, 20);
        return productRepository.findAllByOrderByCreatedAtDesc(paging).map(product -> {
            double starRating = reviewService.calculateStarRating(product.getProductId());
            return ProductListResponse.of(product, starRating);
        });
    }

    @Transactional
    public ProductDetailResponse retrieveDetail(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
        List<LocalDate> alreadyDates = orderService.reservationDate(productId);
        productRedisService.saveProduct(productId);
        return ProductDetailResponse.of(product, alreadyDates);
    }

    public List<MostProductList> findMostPopularProduct() {
        return productRepository.findAllByProductIdIn(productRedisService.rankPopularProduct())
                .stream().map(MostProductList::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(long productId) {
        productRepository.deleteById(productId);
    }

    @Transactional
    public void update(long productId) {


    }


//    public ReservationDateResponse reservationDate(int productId) {
//        Optional<ProductOrder> productOrder = Optional.ofNullable(orderRepository.findByProductId(productId)
//                .orElseThrow(() ->
//                        new RuntimeException("해당 ProductId(" + productId + ") 에 대한 예약날짜가 없습니다.")));
//        return (ReservationDateResponse) productOrder.stream().map(ReservationDateResponse::of)
//                .collect(Collectors.toList());
//    }



}

