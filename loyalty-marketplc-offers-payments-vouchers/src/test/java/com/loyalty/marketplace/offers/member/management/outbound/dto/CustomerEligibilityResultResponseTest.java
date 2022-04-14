package com.loyalty.marketplace.offers.member.management.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CustomerEligibilityResultResponse.class)
@ActiveProfiles("unittest")
public class CustomerEligibilityResultResponseTest {

	private CustomerEligibilityResultResponse customerEligibilityResultResponse;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		customerEligibilityResultResponse = new CustomerEligibilityResultResponse();
		CustomerEligibilityResult customerEligibility = new CustomerEligibilityResult();
		customerEligibilityResultResponse.setCustomerEligibilityResult(customerEligibility);
		CustomerEligibilityResultResponse customer = new CustomerEligibilityResultResponse(new CustomerEligibilityResult());
		customer.setCustomerEligibilityResult(new CustomerEligibilityResult());
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(customerEligibilityResultResponse.getCustomerEligibilityResult());
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(customerEligibilityResultResponse.toString());
	}
	
}
