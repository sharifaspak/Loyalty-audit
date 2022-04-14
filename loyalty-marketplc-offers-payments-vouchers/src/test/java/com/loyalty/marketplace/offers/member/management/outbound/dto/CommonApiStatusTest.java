package com.loyalty.marketplace.offers.member.management.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.outbound.dto.Result;

@SpringBootTest(classes = CommonApiStatus.class)
@ActiveProfiles("unittest")
public class CommonApiStatusTest {

	private CommonApiStatus commonApiStatus;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		commonApiStatus = new CommonApiStatus();
		
		commonApiStatus.setApiStatus(new ApiStatus());
		commonApiStatus.setResult(new Result());
		
		CommonApiStatus commonApi = new CommonApiStatus(new ApiStatus(), new Object());
		commonApi.setApiStatus(new ApiStatus());
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(commonApiStatus.getApiStatus());
		assertNotNull(commonApiStatus.getResult());
	
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(commonApiStatus.toString());
	}
	
}
