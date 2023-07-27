package com.web.billim.order.dto.response;

import java.time.LocalDate;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.web.billim.order.domain.ProductBuyer;
import com.web.billim.order.domain.ProductOrder;
import com.web.billim.order.type.ProductOrderStatus;
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

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate startAt;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate endAt;

	private ProductOrderStatus status;

	public static MySalesOrderResponse from(ProductOrder order) {
		return new MySalesOrderResponse(
			order.getOrderId(),
			order.getMember().getMemberId(),
			order.getMember().getNickname(),
			order.getTradeMethod(),
			Optional.ofNullable(order.getBuyer()).map(ProductBuyer::getName).orElse(null),
			Optional.ofNullable(order.getBuyer()).map(ProductBuyer::getAddress).orElse(null),
			Optional.ofNullable(order.getBuyer()).map(ProductBuyer::getPhone).orElse(null),
			order.getStartAt(),
			order.getEndAt(),
			order.getStatus()
		);
	}

}
