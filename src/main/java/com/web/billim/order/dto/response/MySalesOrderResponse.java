package com.web.billim.order.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.web.billim.order.domain.ProductOrder;
import com.web.billim.product.type.TradeMethod;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MySalesOrderResponse {

	private long orderId;
	private long buyerId;
	private String buyerNickname;
	private TradeMethod tradeMethods;
	private String name;  // nullable
	private String address;  // nullable
	private String phoneNumber;  // nullable
	private LocalDate startAt;
	private LocalDate endAt;

	public static MySalesOrderResponse from(ProductOrder order) {
		return new MySalesOrderResponse(
			order.getOrderId(),
			order.getMember().getMemberId(),
			order.getMember().getNickname(),
			order.getTradeMethod(),
			order.getBuyer().getName(),
			order.getBuyer().getAddress(),
			order.getBuyer().getPhone(),
			order.getStartAt(),
			order.getEndAt()
		);
	}
}
