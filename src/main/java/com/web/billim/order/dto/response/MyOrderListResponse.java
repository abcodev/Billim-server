package com.web.billim.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyOrderListResponse {
    private List<MyOrderHistory> productOrders;
}
