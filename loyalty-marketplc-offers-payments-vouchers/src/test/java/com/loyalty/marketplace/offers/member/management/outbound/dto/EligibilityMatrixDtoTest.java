package com.loyalty.marketplace.offers.member.management.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = EligibilityMatrixDto.class)
@ActiveProfiles("unittest")
public class EligibilityMatrixDtoTest {

	private EligibilityMatrixDto eligibilityMatrixDto;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		eligibilityMatrixDto = new EligibilityMatrixDto();
		eligibilityMatrixDto.setEligibleFeature(new ArrayList<EligibilitySectionDto>());
		eligibilityMatrixDto.setPaymentMethod(new ArrayList<PaymentMethodDto>());
		
		EligibilityMatrixDto eligibility= new EligibilityMatrixDto(new ArrayList<EligibilitySectionDto>(), new ArrayList<PaymentMethodDto>());
		eligibility.setEligibleFeature(new ArrayList<EligibilitySectionDto>());
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(eligibilityMatrixDto.getEligibleFeature());
		assertNotNull(eligibilityMatrixDto.getPaymentMethod());
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(eligibilityMatrixDto.toString());
	}
	
}
