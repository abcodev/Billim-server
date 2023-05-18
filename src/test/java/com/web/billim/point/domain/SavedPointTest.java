package com.web.billim.point.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SavedPointTest {

	// BDD (Behavior Driven Development)
	@Test
	public void use_사용가능금액_충분할_때_정상동작_확인() {
		// given
		SavedPoint savedPoint = SavedPoint.builder()
				.availableAmount(2000)
				.build();
		long amount = 1000;

		// when
		long usedAmount = savedPoint.use(amount);

		// then
		assertEquals(usedAmount, 1000);
		assertEquals(savedPoint.getAvailableAmount(), 1000);
	}

	@Test
	public void use_사용가능금액_충분하지_않을_때_정상동작_확인() {
		// given
		SavedPoint savedPoint = SavedPoint.builder()
				.availableAmount(500)
				.build();
		long amount = 1000;

		// when
		long usedAmount = savedPoint.use(amount);

		// then
		assertEquals(usedAmount, 500);
		assertEquals(savedPoint.getAvailableAmount(), 0);
	}

}