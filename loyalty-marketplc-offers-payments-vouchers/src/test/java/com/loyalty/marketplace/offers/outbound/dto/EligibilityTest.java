package com.loyalty.marketplace.offers.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = Eligibility.class)
@ActiveProfiles("unittest")
public class EligibilityTest {

	private Eligibility eligibility;

	@Before
	public void setUp() {
		eligibility = new Eligibility();
		eligibility = new Eligibility(true, new ArrayList<RuleFailure>());
		eligibility.setStatus(true);
		eligibility.setFailureStatus(new ArrayList<RuleFailure>());
	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {

		assertNotNull(eligibility.isStatus());
		assertNotNull(eligibility.getFailureStatus());
	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {

		assertNotNull(eligibility.toString());

	}
}
