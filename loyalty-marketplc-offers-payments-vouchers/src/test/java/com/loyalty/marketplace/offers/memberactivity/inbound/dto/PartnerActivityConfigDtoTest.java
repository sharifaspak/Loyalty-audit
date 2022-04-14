package com.loyalty.marketplace.offers.memberactivity.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = PartnerActivityConfigDto.class)
@ActiveProfiles("unittest")
public class PartnerActivityConfigDtoTest {

	private PartnerActivityConfigDto requestDto;

	@Before
	public void setUp() {
		requestDto = new PartnerActivityConfigDto();
		requestDto.setBaseRate(0.0);
		requestDto.setChainedActivity(new ArrayList<>());
		requestDto.setCustomerType(new ArrayList<>());
		requestDto.setThresholdCap(new ArrayList<>());
		requestDto.setTierBonus(new ArrayList<>());
	}

	/**
	 * 
	 * Test Getters
	 * 
	 */

	@Test
	public void testGetters() {
		assertNotNull(requestDto.getBaseRate());
		assertNotNull(requestDto.getChainedActivity());
		assertNotNull(requestDto.getCustomerType());
		assertNotNull(requestDto.getThresholdCap());
		assertNotNull(requestDto.getTierBonus());
		
	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(requestDto.toString());
	}

}
