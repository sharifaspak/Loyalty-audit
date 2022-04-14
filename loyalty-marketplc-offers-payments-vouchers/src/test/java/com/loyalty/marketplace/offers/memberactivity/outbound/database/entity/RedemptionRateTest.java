package com.loyalty.marketplace.offers.memberactivity.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = RedemptionRate.class)
@ActiveProfiles("unittest")
public class RedemptionRateTest {

	private RedemptionRate redemptionRate;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		redemptionRate = new RedemptionRate();
		redemptionRate.setDenominationRange(new DenominationRange());
		redemptionRate.setEquivalentPoint(2.1);
		redemptionRate.setRate(3.2);
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(redemptionRate.getDenominationRange());
		assertNotNull(redemptionRate.getEquivalentPoint());
		assertNotNull(redemptionRate.getRate());
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(redemptionRate.toString());
	}
	
}

