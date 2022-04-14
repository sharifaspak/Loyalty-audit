package com.loyalty.marketplace.offers.member.management.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CustomerEligibilityResult.class)
@ActiveProfiles("unittest")
public class CustomerEligibilityResultTest {

	private CustomerEligibilityResult customerEligibilityResult;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		customerEligibilityResult = new CustomerEligibilityResult();
		EligibilityMatrixDto eligibilityMatrix = new EligibilityMatrixDto();
		customerEligibilityResult.setEligibilityMatrix(eligibilityMatrix);
		CustomerEligibilityResult customerEligibility = new CustomerEligibilityResult(new EligibilityMatrixDto());
		customerEligibility.setEligibilityMatrix(new EligibilityMatrixDto());
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(customerEligibilityResult.getEligibilityMatrix());
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(customerEligibilityResult.toString());
	}
	
}
