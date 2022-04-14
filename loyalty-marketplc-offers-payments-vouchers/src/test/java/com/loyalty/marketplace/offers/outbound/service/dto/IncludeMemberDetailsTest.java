package com.loyalty.marketplace.offers.outbound.service.dto;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = IncludeMemberDetails.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class IncludeMemberDetailsTest {
	
	private IncludeMemberDetails includeMemberDetails;
	
	@Before
	public void setUp(){
		
		includeMemberDetails = new IncludeMemberDetails();
		includeMemberDetails.setIncludeCustomerInterestInfo(true);
		includeMemberDetails.setIncludeMemberActivityInfo(true);
		includeMemberDetails.setIncludeEligibilityMatrix(true);
		includeMemberDetails.setIncludePaymentMethods(true);
		includeMemberDetails.setIncludePointsBankInfo(true);
		includeMemberDetails.setIncludeSubscriptionInfo(true);
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertTrue(includeMemberDetails.isIncludeCustomerInterestInfo());
		assertTrue(includeMemberDetails.isIncludeMemberActivityInfo());
		assertTrue(includeMemberDetails.isIncludeEligibilityMatrix());
		assertTrue(includeMemberDetails.isIncludePaymentMethods());
		assertTrue(includeMemberDetails.isIncludePointsBankInfo());
		assertTrue(includeMemberDetails.isIncludeSubscriptionInfo());
	
		
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(includeMemberDetails.toString());
	    
	}
		
}
