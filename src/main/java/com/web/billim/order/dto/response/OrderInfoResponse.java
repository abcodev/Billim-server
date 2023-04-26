package com.web.billim.order.dto.response;

import com.web.billim.product.type.TradeMethod;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrderInfoResponse {

    private int productId;
    private String productName;
    private String detail;
    private int price;
    private List<TradeMethod> tradeMethods;

}
