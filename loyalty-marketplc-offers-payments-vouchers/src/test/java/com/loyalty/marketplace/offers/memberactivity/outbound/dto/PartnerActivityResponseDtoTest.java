package com.loyalty.marketplace.offers.memberactivity.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = PartnerActivityResponseDto.class)
@ActiveProfiles("unittest")
public class PartnerActivityResponseDtoTest {

	private PartnerActivityResponseDto responseDto;
	
	@Before
	public void setUp(){
		responseDto = new PartnerActivityResponseDto();
		responseDto.setDescription("");
		responseDto.setActivityCode("");
		responseDto.setActivityId("");
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(responseDto.getDescription());
		assertNotNull(responseDto.getActivityCode());
		assertNotNull(responseDto.getActivityId());
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(responseDto.toString());
	}
	
}