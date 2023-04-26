package com.web.billim.order.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ProductBuyer {

	private String name;
	private String address;
	private String phone;

}
