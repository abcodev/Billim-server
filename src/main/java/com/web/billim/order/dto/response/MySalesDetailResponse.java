package com.web.billim.order.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.web.billim.order.domain.ProductOrder;
import com.web.billim.product.domain.Product;

@Getter
@Builder
public class MySalesDetailResponse {

    private long memberId;
    private long productId;
    private String productName;
    private String imageUrls;

    // 현재 빌리고 있는 사람에 대한 정보
    private MySalesOrderResponse currentOrder;

    // 대기중인 사람에 대한 정보 (정렬)
    private List<MySalesOrderResponse> standbyOrders;

    // 이미 완료된 사람에 대한 정보 (정렬)
    private List<MySalesOrderResponse> pastOrders;

    // 취소된 예약 정보 (정렬)
    private List<MySalesOrderResponse> canceledOrders;

    public static MySalesDetailResponse of(Product product, List<ProductOrder> orderHistories) {
        List<MySalesOrderResponse> standbyOrders = new ArrayList<>();
        List<MySalesOrderResponse> pastOrders = new ArrayList<>();
        List<MySalesOrderResponse> currentOrder = new ArrayList<>();
        List<MySalesOrderResponse> canceledOrders = orderHistories.stream()
                .filter(ProductOrder::isCanceled)
                .map(MySalesOrderResponse::from)
                .collect(Collectors.toList());

        orderHistories.stream()
                .filter(order -> !order.isCanceled())
                .map(MySalesOrderResponse::from)
                .forEach(order -> {
                    if (order.getStartAt().isAfter(LocalDate.now())) {
                        standbyOrders.add(order);
                    } else if (order.getEndAt().isBefore(LocalDate.now())) {
                        pastOrders.add(order);
                    } else {
                        currentOrder.add(order);
                    }
                });

        // Optional : null 을 피하기 위함이다. null 은 NPE(NullPointException) 유발
        //  => NPE 는 Runtime 시점에 발견되는 에러고, 디버깅도 까다로운 면이 있다.
        //  => optional.get()  : 너가 값을 가지고 있으면 그 값을 꺼내줘. 근데 만약 없으면 null 을 반환해줘.
        return new MySalesDetailResponse(
                product.getMember().getMemberId(),
                product.getProductId(),
                product.getProductName(),
                product.mainImage(),
                currentOrder.stream().findFirst().orElse(null),
                standbyOrders,
                pastOrders,
                canceledOrders
        );
    }

}

/**
 *  만약에 5분에 한번씩 도는 스케줄러가 있어
 *  근데 그 스케줄러는 5분에 한번씩 IN_PROGRESS 를 CANCELED 로 바꿔.
 *  진짜 결제를 진행하고 있는 사람이 있는데 그거까지 손대버릴까봐...
 *  "IN_PROGRESS -> CANCELED -> DONE"
 */