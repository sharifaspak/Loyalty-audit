package com.loyalty.marketplace.offers.inbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = OfferRatingDto.class)
@ActiveProfiles("unittest")
public class OfferRatingDtoTest {

	private OfferRatingDto offerRatingDto;
	
	@Before
	public void setUp(){
		offerRatingDto = new OfferRatingDto();
		offerRatingDto.setOfferId("");
		offerRatingDto.setAccountNumber("");
		offerRatingDto.setRating(0);
		offerRatingDto.setComment("");
	}
	
	@Test
	public void testGetters() {
		assertNotNull(offerRatingDto.getOfferId());
		assertNotNull(offerRatingDto.getAccountNumber());
		assertNotNull(offerRatingDto.getRating());
		assertNotNull(offerRatingDto.getComment());
	}
	
	@Test
	public void testToString() {
		assertNotNull(offerRatingDto.toString());
	}
	
}
