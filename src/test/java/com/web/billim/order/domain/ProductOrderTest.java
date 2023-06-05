package com.web.billim.order.domain;

import com.web.billim.product.domain.Product;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductOrderTest {

	@Test
	public void getPrice_금액계산_확인() {
		// given
		ProductOrder order = ProductOrder.builder()
			.product(Product.builder().price(1000).build())
			.startAt(LocalDate.of(2023, 4, 10))
			.endAt(LocalDate.of(2023, 4, 15))
			.build();

		// when
		long price = order.getPrice();

		// then
		assertEquals(price, 6000);
	}

}