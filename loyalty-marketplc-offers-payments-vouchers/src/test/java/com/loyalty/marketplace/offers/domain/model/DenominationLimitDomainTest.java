package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = DenominationLimitDomain.class)
@ActiveProfiles("unittest")
public class DenominationLimitDomainTest {

	private DenominationLimitDomain denominationLimitDomain;
	
	@Before
	public void setUp(){
		denominationLimitDomain = new DenominationLimitDomain();
		denominationLimitDomain = new DenominationLimitDomain(1,1,2,3,4,5);
		
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(denominationLimitDomain.getDenomination());
		assertNotNull(denominationLimitDomain.getDailyLimit());
		assertNotNull(denominationLimitDomain.getWeeklyLimit());
		assertNotNull(denominationLimitDomain.getMonthlyLimit());
		assertNotNull(denominationLimitDomain.getAnnualLimit());
		assertNotNull(denominationLimitDomain.getTotalLimit());
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(denominationLimitDomain.toString());
	}
	
}


