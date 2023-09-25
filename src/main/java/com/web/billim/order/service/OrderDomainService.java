package com.web.billim.order.service;

import com.web.billim.exception.OrderFailedException;
import com.web.billim.exception.handler.ErrorCode;
import com.web.billim.member.domain.Member;
import com.web.billim.order.dto.OrderCommand;
import com.web.billim.order.repository.OrderRepository;
import com.web.billim.order.type.ProductOrderStatus;
import com.web.billim.order.util.LocalDateHelper;
import com.web.billim.order.vo.Period;
import com.web.billim.product.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderDomainService {

    private final OrderRepository orderRepository;

    public boolean checkAlreadyInProgressOrder(Member member) {
        return orderRepository.findByMemberAndStatus(member, ProductOrderStatus.IN_PROGRESS).isPresent();
    }

    public boolean checkAlreadyInProgressOrderOfOtherUser(Product product, Period period) {
        return orderRepository.findByProductAndStatus(product, ProductOrderStatus.IN_PROGRESS)
                .map(order -> LocalDateHelper.checkDuplicatedPeriod(order.getPeriod(), period))
                .orElse(false);
    }
}
