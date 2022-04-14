package com.loyalty.marketplace.offers.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = RuleFailure.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class RuleFailureTest {

	private RuleFailure ruleEligibility;

	@Before
	public void setUp() {

		
		ruleEligibility = new RuleFailure();
		ruleEligibility.setRule("");
		ruleEligibility.setFailureReason("");

	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {

		ruleEligibility = new RuleFailure("rule","reason");
		assertNotNull(ruleEligibility.getRule());
		assertNotNull(ruleEligibility.getFailureReason());

	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {

		assertNotNull(ruleEligibility.toString());

	}

}
