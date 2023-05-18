package com.web.billim.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponRegisterCommand {

	private String name;
	private long rate;
	private long limitDate;

}
