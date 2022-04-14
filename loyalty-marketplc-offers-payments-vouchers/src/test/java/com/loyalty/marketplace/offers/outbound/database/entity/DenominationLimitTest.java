package com.loyalty.marketplace.offers.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = DenominationLimit.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class DenominationLimitTest {

	@InjectMocks
	DenominationLimit denominationLimits;
	private DenominationLimit denominationLimit;

	private Integer denomination;

	private Integer dailyLimit;

	private Integer weeklyLimit;

	private Integer monthlyLimit;

	private Integer annualLimit;

	private Integer totalLimit;

	@Before
	public void setUp() {
		denomination = 1;
		dailyLimit = 2;
		weeklyLimit = 3;
		monthlyLimit = 4;
		annualLimit = 5;
		totalLimit = 6;

		MockitoAnnotations.initMocks(this);
		denominationLimit = new DenominationLimit();
		denominationLimit.setDenomination(denomination);
		denominationLimit.setDailyLimit(dailyLimit);
		denominationLimit.setWeeklyLimit(weeklyLimit);
		denominationLimit.setMonthlyLimit(monthlyLimit);
		denominationLimit.setAnnualLimit(annualLimit);
		denominationLimit.setTotalLimit(totalLimit);

	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {

		assertNotNull(denominationLimit.getDenomination());
		assertNotNull(denominationLimit.getDailyLimit());
		assertNotNull(denominationLimit.getWeeklyLimit());
		assertNotNull(denominationLimit.getMonthlyLimit());
		assertNotNull(denominationLimit.getAnnualLimit());
		assertNotNull(denominationLimit.getTotalLimit());

	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {

		assertNotNull(denominationLimit.toString());

	}

}
