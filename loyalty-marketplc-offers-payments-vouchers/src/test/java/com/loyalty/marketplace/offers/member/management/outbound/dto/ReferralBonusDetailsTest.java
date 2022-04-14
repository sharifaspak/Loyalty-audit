package com.loyalty.marketplace.offers.member.management.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ReferralBonusDetails.class)
@ActiveProfiles("unittest")
public class ReferralBonusDetailsTest {

	private ReferralBonusDetails referralBonusDetails;

	@Before
	public void setUp() {
		referralBonusDetails = new ReferralBonusDetails();
		referralBonusDetails.setAccountNumber("");
		referralBonusDetails.setReferralCode("");
	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(referralBonusDetails.getAccountNumber());
		assertNotNull(referralBonusDetails.getReferralCode());
	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(referralBonusDetails.toString());
	}
}
