package com.web.billim.product.service;

import com.web.billim.exception.BadRequestException;
import com.web.billim.exception.NotFoundException;
import com.web.billim.exception.handler.ErrorCode;
import com.web.billim.common.infra.ImageUploadService;
import com.web.billim.member.domain.Member;
import com.web.billim.member.repository.MemberRepository;
import com.web.billim.order.dto.response.MySalesListResponse;
import com.web.billim.order.repository.OrderRepository;
import com.web.billim.order.service.OrderService;
import com.web.billim.product.domain.ImageProduct;
import com.web.billim.product.domain.Product;
import com.web.billim.product.domain.ProductCategory;
import com.web.billim.product.dto.command.ProductRegisterCommand;
import com.web.billim.product.dto.command.ProductUpdateCommand;
import com.web.billim.product.dto.response.*;
import com.web.billim.product.repository.ImageProductRepository;
import com.web.billim.product.repository.ProductCategoryRepository;
import com.web.billim.product.repository.ProductRepository;
import com.web.billim.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ImageProductRepository imageProductRepository;
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final ImageUploadService imageUploadService;
    private final ProductRedisService productRedisService;
    private final RecentProductRedisService recentProductRedisService;
    private final ReviewService reviewService;

    // 상품 등록
    @Transactional
    public Product register(ProductRegisterCommand command) {
        Member registerMember = memberRepository.findById(command.getMemberId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        ProductCategory productCategory = productCategoryRepository.findByCategoryName(command.getCategory())
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

        // 1. 이미지 저장
        List<ImageProduct> images = command.getImages().stream().map(image -> {
            String url = imageUploadService.upload(image, "product");
            return imageProductRepository.save(ImageProduct.of(url));
        }).collect(Collectors.toList());

        // 2. Product 정보 데이터베이스에 저장 & 반환
        Product product = Product.generateNewProduct(command, productCategory, registerMember, images);
        return productRepository.save(product);
    }

    // 상품 목록 조회, 페이징, 카테고리 및 키워드 검색
    public Page<ProductListResponse> search(String category, String keyword, PageRequest paging) {
        return productRepository.findAllByKeyword(category, keyword, paging)
                .map(product -> {
                    double starRating = reviewService.calculateStarRating(product.getProductId());
                    return ProductListResponse.of(product, starRating);
                });
    }

    // 상품 상세 정보 조회
    @Transactional
    public ProductDetailResponse retrieveDetail(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
        List<LocalDate> alreadyDates = orderService.reservationDate(productId);
        double starRating = reviewService.calculateStarRating(product.getProductId());
        productRedisService.saveProduct(productId);
        return ProductDetailResponse.of(product, alreadyDates, starRating);
    }

    // 상품 수정시 정보 조회
    @Transactional
    public ProductUpdateResponse retrieveUpdateProduct(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
        return ProductUpdateResponse.of(product);
    }

    // 상품 수정
    @Transactional
    public void update(ProductUpdateCommand command) {
        // 0. 이미지 개수 검증
        var imageCount = imageProductRepository.countByProductId(command.getProductId());
        assert imageCount + command.getAppendImageCount() <= 5;

        // 1. 삭제된 이미지 삭제
        command.getDeleteImageUrls().forEach(url -> {
            imageUploadService.delete(url);
            imageProductRepository.deleteByUrl(url);
        });

        // 2. 추가된 이미지 추가
        List<ImageProduct> appendImages = command.getAppendImages().stream().map(image -> {
            String url = imageUploadService.upload(image, "product");
            return imageProductRepository.save(ImageProduct.of(url));
        }).collect(Collectors.toList());

        // 3. 수정된 데이터로 덮어쓰기
        productRepository.findById(command.getProductId())
                .map(product -> {
                    var category = productCategoryRepository.findByCategoryName(command.getCategory())
                            .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
                    product.update(command, appendImages, category);
                    return productRepository.save(product);
                }).orElseThrow();
    }

    // 상품 삭제
    @Transactional
    public void delete(long memberId, long productId) {
        productRepository.findById(productId)
                .filter(product -> product.getMember().getMemberId() == memberId)
                .ifPresent(product -> {
                    if (!orderRepository.findAllByProductAndEndAtAfter(product, LocalDate.now()).isEmpty()) {
                        throw new BadRequestException(ErrorCode.PRODUCT_HAS_RESERVATION);
                    }
                    imageProductRepository.deleteAllInBatch(product.getImages());
                    product.delete();
                });
    }

    // 인기 상품 목록
    @Transactional
    public List<MostProductList> findMostPopularProduct() {
        return productRepository.findAllByProductIdInAndIsDeleted(productRedisService.rankPopularProduct(), false)
                .stream()
                .map(MostProductList::of)
                .collect(Collectors.toList());
    }

    // 최근 본 상품 목록
    public List<RecentProductResponse> recentProductList(long memberId) {
        return productRepository.findAllByProductIdInAndIsDeleted(recentProductRedisService.findTopN(memberId, 5), false)
                .stream()
                .map(RecentProductResponse::of)
                .collect(Collectors.toList());
    }

    // 마이페이지 상품 판매 목록 조회
    @Transactional
    public Page<MySalesListResponse> findMySalesList(long memberId, PageRequest paging) {
        Page<Product> productPage = productRepository.findByMemberId(memberId, paging);
        return productPage.map(MySalesListResponse::of);
    }

}

