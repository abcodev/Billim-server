package com.web.billim.order.service;

import com.web.billim.common.exception.NotFoundException;
import com.web.billim.common.exception.OrderFailedException;
import com.web.billim.common.exception.handler.ErrorCode;
import com.web.billim.member.domain.Member;
import com.web.billim.member.dto.response.MemberInfoResponse;
import com.web.billim.member.service.MemberService;
import com.web.billim.order.domain.ProductOrder;
import com.web.billim.order.dto.OrderCommand;
import com.web.billim.order.dto.response.MyOrderHistory;
import com.web.billim.order.dto.response.MyOrderHistoryListResponse;
import com.web.billim.order.dto.response.PaymentInfoResponse;
import com.web.billim.order.repository.OrderRepository;
import com.web.billim.order.type.ProductOrderStatus;
import com.web.billim.order.util.LocalDateHelper;
import com.web.billim.payment.dto.PaymentCommand;
import com.web.billim.payment.service.PaymentService;
import com.web.billim.product.domain.Product;
import com.web.billim.product.domain.service.ProductDomainService;
import com.web.billim.product.dto.response.MostProductList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final MemberService memberService;
    private final PaymentService paymentService;
    private final OrderRepository orderRepository;
    private final ProductDomainService productDomainService;

    public List<LocalDate> reservationDate(long productId) {
        Product product = productDomainService.find(productId);
        List<ProductOrder> orderList = orderRepository.findAllByProductAndEndAtAfter(product,LocalDate.now());

        return orderList.stream()
                .flatMap(order -> LocalDateHelper.changeDate(order.getStartAt(),order.getEndAt()).stream())
                .filter(date -> !date.isBefore(LocalDate.now()))
                .collect(Collectors.toList());
    }

    @Transactional
    public PaymentInfoResponse order(long memberId, OrderCommand orderCommand) {
        Member member = memberService.retrieve(memberId);

        // 1. 해당 사용자가 주문중인게 있는지 확인
        orderRepository.findByMemberAndStatus(member, ProductOrderStatus.IN_PROGRESS)
            .ifPresent(order -> {
                throw new OrderFailedException(ErrorCode.ORDER_DUPLICATED_REQUEST);
            });

        // 2. 다른 사용자가 해당 Product 의 해당 기간을 결제중인게 있는지 확인
        Product product = productDomainService.find(orderCommand.getProductId());
        orderRepository.findByProductAndStatus(product, ProductOrderStatus.IN_PROGRESS)
            .ifPresent(order -> {
                if (LocalDateHelper.checkDuplicatedPeriod(order.getPeriod(), orderCommand.getPeriod())) {
                    throw new OrderFailedException(ErrorCode.ORDER_DUPLICATED_PERIOD);
                }
            });

        // 3. 주문정보 생성
        ProductOrder order = orderRepository.save(ProductOrder.generateNewOrder(member, product, orderCommand));

        // 4. 결제정보 생성
        PaymentCommand paymentCommand = new PaymentCommand(member, order, orderCommand.getCouponIssueId(), orderCommand.getUsedPoint());
        return paymentService.payment(paymentCommand);
    }

    @Transactional
    public MyOrderHistoryListResponse findMyOrder(long memberId) {
       List<ProductOrder> productOrders= orderRepository.findAllByMember_memberId(memberId)
               .orElseThrow(()-> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
       List<MyOrderHistory> myOrderHistories = productOrders.stream()
                .map(MyOrderHistory::from)
                .collect(Collectors.toList());
       return new MyOrderHistoryListResponse(myOrderHistories);
    }



    public ProductOrder findByOrder(long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow();
    }

    public long numberOfOrders(long memberId) {
        return orderRepository.countByMember_memberId(memberId)
                .orElseThrow(()-> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
    }

}
