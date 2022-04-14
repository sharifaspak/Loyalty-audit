package com.loyalty.marketplace.offers.inbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = DenominationLimitDto.class)
@ActiveProfiles("unittest")
public class DenominationLimitDtoTest {

	private DenominationLimitDto denominationLimitDto;

	@Before
	public void setUp() {
		denominationLimitDto = new DenominationLimitDto();
		denominationLimitDto.setDenomination(1);
		denominationLimitDto.setDailyLimit(1);
		denominationLimitDto.setWeeklyLimit(2);
		denominationLimitDto.setMonthlyLimit(3);
		denominationLimitDto.setAnnualLimit(4);
		denominationLimitDto.setTotalLimit(5);

	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(denominationLimitDto.getDenomination());
		assertNotNull(denominationLimitDto.getDailyLimit());
		assertNotNull(denominationLimitDto.getWeeklyLimit());
		assertNotNull(denominationLimitDto.getMonthlyLimit());
		assertNotNull(denominationLimitDto.getAnnualLimit());
		assertNotNull(denominationLimitDto.getTotalLimit());
	}

	/**
	 * 
	 * Test ToString
	 * 
	 */

	@Test
	public void testToString() {
		assertNotNull(denominationLimitDto.toString());
	}

}
