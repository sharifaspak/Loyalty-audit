package com.loyalty.marketplace.offers.decision.manager.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CustomerSegmentResponse.class)
@ActiveProfiles("unittest")
public class CustomerSegmentResponseTest {

	private CustomerSegmentResponse dmResponse;

	@Before
	public void setUp() {
		dmResponse = new CustomerSegmentResponse();
		dmResponse.setMemberDetails(new ArrayList<Object>());

	}

	/**
	 * Test Getters
	 */
	@Test
	public void testGetters() {
		assertNotNull(dmResponse.getMemberDetails());
	}

	/**
	 * Test toString
	 */
	@Test
	public void testToString() {
		assertNotNull(dmResponse.toString());
	}

}
