package com.loyalty.marketplace.offers.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = OfferTypeRequestDto.class)
@ActiveProfiles("unittest")
public class OfferTypeRequestDtoTest {

	private OfferTypeRequestDto offerTypeRequestDto;
	
	@Before
	public void setUp(){
		offerTypeRequestDto = new OfferTypeRequestDto();
		offerTypeRequestDto.setOfferTypes(new ArrayList<OfferTypeDto>());
	}
	
	@Test
	public void testGetters() {
		assertNotNull(offerTypeRequestDto.getOfferTypes());
	}
	
	@Test
	public void testToString() {
		assertNotNull(offerTypeRequestDto.toString());
	}
	
}
