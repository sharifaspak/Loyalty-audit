package com.loyalty.marketplace.offers.member.management.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = EligibilityDto.class)
@ActiveProfiles("unittest")
public class EligibilityDtoTest {

	private EligibilityDto eligibilityDto;

	@Before
	public void setUp() {

		eligibilityDto = new EligibilityDto();
		eligibilityDto = new EligibilityDto("",new EligibilityMatrixDto());
		eligibilityDto.setCustomerType("");
		eligibilityDto.setEligibility(new EligibilityMatrixDto());
	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(eligibilityDto.getCustomerType());
		assertNotNull(eligibilityDto.getEligibility());

	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(eligibilityDto.toString());
	}
}
