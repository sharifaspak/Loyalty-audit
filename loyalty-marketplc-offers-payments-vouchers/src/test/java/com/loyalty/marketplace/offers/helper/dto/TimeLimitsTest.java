package com.loyalty.marketplace.offers.helper.dto;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = TimeLimits.class)
@ActiveProfiles("unittest")
public class TimeLimitsTest {

	private TimeLimits timeLimits;
	
	@Before
	public void setUp(){
		timeLimits = new TimeLimits();
		timeLimits.setAnnualCount(0);
		timeLimits.setDailyCount(0);
		timeLimits.setWeeklyCount(0);
		timeLimits.setMonthlyCount(0);
		timeLimits.setLastPurchased(new Date());
	}
	
	@Test
	public void testGetters() {
		assertNotNull(timeLimits.getDailyCount());
		assertNotNull(timeLimits.getWeeklyCount());
		assertNotNull(timeLimits.getMonthlyCount());
		assertNotNull(timeLimits.getAnnualCount());
		assertNotNull(timeLimits.getLastPurchased());
	}
	
	@Test
	public void testToString() {
		assertNotNull(timeLimits.toString());
	}
	
}
