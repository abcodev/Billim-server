package com.web.billim.order.util;

import com.web.billim.order.vo.Period;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalDateHelperTest {

	@Test
	public void isValidationPass_확인() {
		// given
		LocalDate standardStartAt = LocalDate.of(2023, 4, 10);
		LocalDate standardEndAt = LocalDate.of(2023, 4, 15);

		LocalDate startAt_1 = LocalDate.of(2023, 4, 5);
		LocalDate endAt_1 = LocalDate.of(2023, 4, 9);

		LocalDate startAt_2 = LocalDate.of(2023, 4, 5);
		LocalDate endAt_2 = LocalDate.of(2023, 4, 10);

		LocalDate startAt_3 = LocalDate.of(2023, 4, 10);
		LocalDate endAt_3 = LocalDate.of(2023, 4, 14);

		LocalDate startAt_4 = LocalDate.of(2023, 4, 10);
		LocalDate endAt_4 = LocalDate.of(2023, 4, 15);

		LocalDate startAt_5 = LocalDate.of(2023, 4, 11);
		LocalDate endAt_5 = LocalDate.of(2023, 4, 15);

		LocalDate startAt_6 = LocalDate.of(2023, 4, 15);
		LocalDate endAt_6 = LocalDate.of(2023, 4, 20);

		LocalDate startAt_7 = LocalDate.of(2023, 4, 16);
		LocalDate endAt_7 = LocalDate.of(2023, 4, 20);

		// when
		boolean result_1 = LocalDateHelper.checkDuplicatedPeriod(new Period(standardStartAt, standardEndAt), new Period(startAt_1, endAt_1));
		boolean result_2 = LocalDateHelper.checkDuplicatedPeriod(new Period(standardStartAt, standardEndAt), new Period(startAt_2, endAt_2));
		boolean result_3 = LocalDateHelper.checkDuplicatedPeriod(new Period(standardStartAt, standardEndAt), new Period(startAt_3, endAt_3));
		boolean result_4 = LocalDateHelper.checkDuplicatedPeriod(new Period(standardStartAt, standardEndAt), new Period(startAt_4, endAt_4));
		boolean result_5 = LocalDateHelper.checkDuplicatedPeriod(new Period(standardStartAt, standardEndAt), new Period(startAt_5, endAt_5));
		boolean result_6 = LocalDateHelper.checkDuplicatedPeriod(new Period(standardStartAt, standardEndAt), new Period(startAt_6, endAt_6));
		boolean result_7 = LocalDateHelper.checkDuplicatedPeriod(new Period(standardStartAt, standardEndAt), new Period(startAt_7, endAt_7));

		// then
		assertFalse(result_1);
		assertTrue(result_2);
		assertTrue(result_3);
		assertTrue(result_4);
		assertTrue(result_5);
		assertTrue(result_6);
		assertFalse(result_7);
	}

}