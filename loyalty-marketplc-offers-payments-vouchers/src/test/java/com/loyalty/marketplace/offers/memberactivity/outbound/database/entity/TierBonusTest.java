package com.loyalty.marketplace.offers.memberactivity.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = TierBonus.class)
@ActiveProfiles("unittest")
public class TierBonusTest {

	private TierBonus tierBonus;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		tierBonus = new TierBonus();
		tierBonus.setCustomerTier("");
		tierBonus.setTierBonusRate(100.25);
		tierBonus.setExpiryInMonth(0);
		tierBonus.setRateType("");
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(tierBonus.getCustomerTier());
		assertNotNull(tierBonus.getTierBonusRate());
		assertNotNull(tierBonus.getExpiryInMonth());
		assertNotNull(tierBonus.getRateType());
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(tierBonus.toString());
	}
	
}
