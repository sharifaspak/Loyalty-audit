package com.loyalty.marketplace.offers.decision.manager.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CommonApiStatus.class)
@ActiveProfiles("unittest")
public class CommonApiStatusTest {

	private CommonApiStatus commonApiStatus;

	@Before
	public void setUp() {
		commonApiStatus = new CommonApiStatus();
		commonApiStatus.setApiStatus(new ApiStatus());
		commonApiStatus.setResult(new Object());
		commonApiStatus = new CommonApiStatus(new ApiStatus(), new Object());
	}

	/**
	 * Test Getters
	 */
	@Test
	public void testGetters() {
		assertNotNull(commonApiStatus.getApiStatus());
		assertNotNull(commonApiStatus.getResult());
	}

	/**
	 * Test toString
	 */
	@Test
	public void testToString() {
		assertNotNull(commonApiStatus.toString());
	}
}
