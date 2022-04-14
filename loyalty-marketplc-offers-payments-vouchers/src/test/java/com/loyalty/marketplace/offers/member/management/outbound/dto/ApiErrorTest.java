package com.loyalty.marketplace.offers.member.management.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ApiError.class)
@ActiveProfiles("unittest")
public class ApiErrorTest {
	
	private ApiError apiError;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		apiError = new ApiError();
		apiError.setCode(0);
		apiError.setMessage("");
		ApiError api = new ApiError(0,"");
		api.setCode(0);
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(apiError.getCode());
		assertNotNull(apiError.getMessage());
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(apiError.toString());
	}
	
}
