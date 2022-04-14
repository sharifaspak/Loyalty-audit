package com.loyalty.marketplace.offers.member.management.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CRMResponse.class)
@ActiveProfiles("unittest")
public class CRMResponseTest {

	private CRMResponse cRMResponse;
	
	@Before
	public void setUp(){
		cRMResponse = new CRMResponse();
		cRMResponse.setAccountNumber("");
		cRMResponse.setFirstName("");
		cRMResponse.setLastName("");
		cRMResponse.setCustomerTier("");
		cRMResponse.setCustomerType("");
		cRMResponse.setNationality("");
		
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(cRMResponse.getAccountNumber());
		assertNotNull(cRMResponse.getFirstName());
		assertNotNull(cRMResponse.getLastName());
		assertNotNull(cRMResponse.getCustomerTier());
		assertNotNull(cRMResponse.getCustomerType());
	
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(cRMResponse.toString());
	}
	
}
