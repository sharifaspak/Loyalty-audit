package com.loyalty.marketplace.offers.decision.manager.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = RuleResult.class)
@ActiveProfiles("unittest")
public class RuleResultTest {
	private RuleResult ruleResult;

	@Before
	public void setUp() {
		ruleResult = new RuleResult();
		ruleResult.setEligibility(true);
		ruleResult.setAccountNumber("");
		ruleResult.setReason("");
		ruleResult.setCustomerSegments(new ArrayList<>());

	}

	/**
	 * Test Getters
	 */
	@Test
	public void testGetters() {
		assertNotNull(ruleResult.getAccountNumber());
		assertNotNull(ruleResult.isEligibility());
		assertNotNull(ruleResult.getReason());
		assertNotNull(ruleResult.getCustomerSegments());
	}

	/**
	 * Test toString
	 */
	@Test
	public void testToString() {
		assertNotNull(ruleResult.toString());
	}
}
