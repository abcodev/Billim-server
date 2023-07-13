package com.web.billim.order.dto;

import com.web.billim.order.vo.Period;
import com.web.billim.product.type.TradeMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCommand {
	private long productId;
	private LocalDate startAt;
	private LocalDate endAt;
	private int couponIssueId;
	private int usedPoint;
	private TradeMethod tradeMethod;
	private String name;
	private String address;
	private String phone;

	public Period getPeriod() {
		return new Period(startAt, endAt);
	}

}
