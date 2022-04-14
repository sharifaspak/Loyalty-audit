package com.loyalty.marketplace.offers.decision.manager.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ApiStatus.class)
@ActiveProfiles("unittest")
public class ApiStatusTest {

	private ApiStatus apiStatus;

	@Before
	public void setUp() {
		apiStatus = new ApiStatus();
		apiStatus.setErrors(new ArrayList<ApiError>());
		apiStatus.setOverallStatus("");
		apiStatus.setStatusCode(0);
		apiStatus.setType("");
		apiStatus = new ApiStatus("", "", 0, new ArrayList<ApiError>());
	}

	/**
	 * Test Getters
	 */
	@Test
	public void testGetters() {
		assertNotNull(apiStatus.getErrors());
		assertNotNull(apiStatus.getOverallStatus());
		assertNotNull(apiStatus.getStatusCode());
		assertNotNull(apiStatus.getType());
	}

	/**
	 * Test toString
	 */
	@Test
	public void testToString() {
		assertNotNull(apiStatus.toString());
	}
}
