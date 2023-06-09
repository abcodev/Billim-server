package com.web.billim.order.dto.response;

import com.web.billim.order.domain.ProductOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyOrderHistoryListResponse {
    private List<MyOrderHistory> productOrders;
}
