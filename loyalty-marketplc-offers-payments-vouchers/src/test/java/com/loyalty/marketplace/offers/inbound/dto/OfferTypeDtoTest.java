package com.loyalty.marketplace.offers.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = OfferTypeDto.class)
@ActiveProfiles("unittest")
public class OfferTypeDtoTest {

	private OfferTypeDto offerTypeDto;
	
	@Before
	public void setUp(){
		offerTypeDto = new OfferTypeDto();
		offerTypeDto.setOfferTypeId("");
		offerTypeDto.setDescriptionEn("");
		offerTypeDto.setDescriptionAr("");
		offerTypeDto.setPaymentMethods(new ArrayList<String>());
	}
	
	@Test
	public void testGetters() {
		assertNotNull(offerTypeDto.getOfferTypeId());
		assertNotNull(offerTypeDto.getDescriptionEn());
		assertNotNull(offerTypeDto.getDescriptionAr());
		assertNotNull(offerTypeDto.getPaymentMethods());
	}
	
	@Test
	public void testToString() {
		assertNotNull(offerTypeDto.toString());
	}
	
}
