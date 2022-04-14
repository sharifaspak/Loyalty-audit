package com.loyalty.marketplace.offers.decision.manager.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CustomerSegmentResult.class)
@ActiveProfiles("unittest")
public class CustomerSegmentResultTest {
	private CustomerSegmentResult decisionManagerResult;

	@Before
	public void setUp() {
		decisionManagerResult = new CustomerSegmentResult();
		decisionManagerResult.setRulesResult(new ArrayList<>());

	}

	/**
	 * Test Getters
	 */
	@Test
	public void testGetters() {
		assertNotNull(decisionManagerResult.getRulesResult());
	}

	/**
	 * Test toString
	 */
	@Test
	public void testToString() {
		assertNotNull(decisionManagerResult.toString());
	}
}