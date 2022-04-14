package com.loyalty.marketplace.offers.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = DenominationLimitDto.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class DenominationLimitDtoTest {

	private DenominationLimitDto denominationLimitDto;

	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);
		denominationLimitDto = new DenominationLimitDto();
		denominationLimitDto.setDenominationDescription("");
		denominationLimitDto.setDailyLimit(11);
		denominationLimitDto.setWeeklyLimit(12);
		denominationLimitDto.setMonthlyLimit(13);
		denominationLimitDto.setAnnualLimit(14);
		denominationLimitDto.setTotalLimit(15);

	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {

		assertNotNull(denominationLimitDto.getDenominationDescription());
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
