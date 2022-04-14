package com.loyalty.marketplace.offers.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = DenominationCount.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class DenominationCountTest {
	
	private DenominationCount denominationCount;
	
	@Before
	public void setUp() {
		
		denominationCount = new DenominationCount();
		denominationCount.setDenomination(0);
		denominationCount.setDailyCount(0);
		denominationCount.setWeeklyCount(0);
		denominationCount.setMonthlyCount(0);
		denominationCount.setAnnualCount(0);
		denominationCount.setTotalCount(0);
		denominationCount.setLastPurchased(new Date());
	
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(denominationCount.getDenomination());
		assertNotNull(denominationCount.getDailyCount());
		assertNotNull(denominationCount.getWeeklyCount());	
		assertNotNull(denominationCount.getMonthlyCount());
		assertNotNull(denominationCount.getAnnualCount());
		assertNotNull(denominationCount.getTotalCount());
		assertNotNull(denominationCount.getLastPurchased());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(denominationCount.toString());
	    
			
	}

}
