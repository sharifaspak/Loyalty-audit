package com.loyalty.marketplace.offers.inbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = SubOfferDto.class)
@ActiveProfiles("unittest")
public class SubOfferDtoTest {

	private SubOfferDto subOfferDto;
	
	@Before
	public void setUp(){
		subOfferDto = new SubOfferDto();
		subOfferDto.setSubOfferId("");
		subOfferDto.setSubOfferTitleEn("");
		subOfferDto.setSubOfferTitleAr("");
		subOfferDto.setSubOfferDescEn("");
		subOfferDto.setSubOfferDescAr("");
		subOfferDto.setOldCost(1.0);
		subOfferDto.setOldPointValue(1);
		subOfferDto.setNewCost(2.0);
		subOfferDto.setNewPointValue(2);
	}
	
	@Test
	public void testGetters() {
		assertNotNull(subOfferDto.getSubOfferId());
		assertNotNull(subOfferDto.getSubOfferTitleEn());
		assertNotNull(subOfferDto.getSubOfferTitleAr());
		assertNotNull(subOfferDto.getSubOfferDescEn());
		assertNotNull(subOfferDto.getSubOfferDescAr());
		assertNotNull(subOfferDto.getOldCost());
		assertNotNull(subOfferDto.getOldPointValue());
		assertNotNull(subOfferDto.getNewCost());
		assertNotNull(subOfferDto.getNewPointValue());
	}
	
	@Test
	public void testToString() {
		assertNotNull(subOfferDto.toString());
	}
	
}
