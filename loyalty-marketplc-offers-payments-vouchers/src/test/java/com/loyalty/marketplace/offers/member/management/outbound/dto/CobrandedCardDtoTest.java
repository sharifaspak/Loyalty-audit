package com.loyalty.marketplace.offers.member.management.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CobrandedCardDto.class)
@ActiveProfiles("unittest")
public class CobrandedCardDtoTest {
	
	private CobrandedCardDto cobrandedCardDto;
	
	@Before
	public void setUp(){
		cobrandedCardDto = new CobrandedCardDto();
		cobrandedCardDto.setPartnerCode("");
		cobrandedCardDto.setBankId("");
		cobrandedCardDto.setStatus("");
		cobrandedCardDto.setCardType("");
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(cobrandedCardDto.getPartnerCode());
		assertNotNull(cobrandedCardDto.getBankId());
		assertNotNull(cobrandedCardDto.getStatus());
		assertNotNull(cobrandedCardDto.getCardType());
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(cobrandedCardDto.toString());
	}
	
}
