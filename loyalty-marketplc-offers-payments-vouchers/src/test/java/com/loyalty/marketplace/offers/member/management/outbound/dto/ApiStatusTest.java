package com.loyalty.marketplace.offers.member.management.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ApiStatus.class)
@ActiveProfiles("unittest")
public class ApiStatusTest {

	private ApiStatus apiStatus;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		apiStatus = new ApiStatus();
		apiStatus.setExternalTransationId("");
		apiStatus.setMessage("");
		apiStatus.setOverallStatus("");
		apiStatus.setStatusCode(0);
		List<ApiError> api = new ArrayList<>();
		apiStatus.setErrors(api);
		
		ApiStatus apiConstructor = new ApiStatus("", "", "", 0, new ArrayList<ApiError>());
		
		apiConstructor.setExternalTransationId("");
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(apiStatus.getExternalTransationId());
		assertNotNull(apiStatus.getMessage());
		assertNotNull(apiStatus.getOverallStatus());
		assertNotNull(apiStatus.getStatusCode());
		assertNotNull(apiStatus.getErrors());
	
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(apiStatus.toString());
	}
	
}
