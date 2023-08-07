package com.web.billim.review.service;

import com.web.billim.exception.NotFoundException;
import com.web.billim.exception.handler.ErrorCode;
import com.web.billim.member.domain.Member;
import com.web.billim.order.domain.ProductOrder;
import com.web.billim.order.repository.OrderRepository;
import com.web.billim.order.service.OrderService;
import com.web.billim.point.domain.service.PointDomainService;
import com.web.billim.point.dto.AddPointCommand;
import com.web.billim.point.service.PointService;
import com.web.billim.review.domain.Review;
import com.web.billim.review.dto.WrittenReviewList;
import com.web.billim.review.dto.request.ReviewWriteRequest;
import com.web.billim.review.dto.response.MyReviewListResponse;
import com.web.billim.review.dto.response.ProductReviewListResponse;
import com.web.billim.review.dto.WritableReviewList;
import com.web.billim.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final PointService pointService;
    private final PointDomainService pointDomainService;

    public double calculateStarRating(long productId) {
        return reviewRepository.findAllByProductId(productId).stream()
                .collect(Collectors.summarizingLong(Review::getStarRating))
                .getAverage();
    }

    @Transactional
    public void productReviewWrite(ReviewWriteRequest reviewWriteRequest) {
        ProductOrder productOrder = orderService.findByOrder(reviewWriteRequest.getOrderId());
        Review review = reviewRepository.save(ReviewWriteRequest.toEntity(reviewWriteRequest, productOrder));

        long amount = pointDomainService.calculate(review);
        pointService.addPoint(new AddPointCommand(productOrder.getMember(), amount, Duration.ofDays(365)));
    }

    public List<ProductReviewListResponse> reviewList(long productId) {
        return reviewRepository.findAllByProductId(productId)
                .stream()
                .map(ProductReviewListResponse::of)
                .collect(Collectors.toList());
    }

    // 작성 가능한 리뷰 개수
    public long writableReviewCount(long memberId) {
        return orderRepository.countByMemberAndStatus(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    // 작성 가능한 리뷰 리스트
    @Transactional
    public List<WritableReviewList> findMyWritableReview(long memberId) {
        return orderRepository.findProductOrdersWritableReview(memberId)
                .stream().map(WritableReviewList::of).collect(Collectors.toList());
    }

    @Transactional
    public MyReviewListResponse myReviewList(long memberId) {

        List<WritableReviewList> writableReviewList = orderRepository.findProductOrdersWritableReview(memberId)
                .stream().map(WritableReviewList::of).collect(Collectors.toList());
        List<WrittenReviewList> writtenReviewList = reviewRepository.findByProductOrder_Member_MemberId(memberId)
                .stream().map(WrittenReviewList::of).collect(Collectors.toList());

        return new MyReviewListResponse(writableReviewList, writtenReviewList);
    }


}
