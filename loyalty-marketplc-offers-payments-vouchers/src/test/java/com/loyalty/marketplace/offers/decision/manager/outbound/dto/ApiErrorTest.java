package com.loyalty.marketplace.offers.decision.manager.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ApiError.class)
@ActiveProfiles("unittest")
public class ApiErrorTest {

	private ApiError apiError;

	@Before
	public void setUp() {
		apiError = new ApiError();
		apiError.setCode(0);
		apiError.setMessage("");
		apiError = new ApiError(0, "");
	}

	/**
	 * Test Getters
	 */
	@Test
	public void testGetters() {
		assertNotNull(apiError.getCode());
		assertNotNull(apiError.getMessage());
	}

	/**
	 * Test toString
	 */
	@Test
	public void testToString() {
		assertNotNull(apiError.toString());
	}
}
