package com.loyalty.marketplace.offers.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=SubOfferDto.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class SubOfferDtoTest {
	
	
	@InjectMocks
	SubOfferDto subOfferDto;
	
	private SubOfferDto subOffer;
		
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		subOffer = new SubOfferDto();
		
		subOffer.setSubOfferId("");
		subOffer.setSubOfferTitleEn("");
		subOffer.setSubOfferTitleAr("");
		subOffer.setSubOfferDescEn("");
		subOffer.setSubOfferDescAr("");
		subOffer.setOldCost(0.0);
		subOffer.setOldPointValue(0);
		subOffer.setNewCost(0.0);
		subOffer.setNewPointValue(0);
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(subOffer.getSubOfferId());
	    assertNotNull(subOffer.getSubOfferTitleEn());
	    assertNotNull(subOffer.getSubOfferTitleAr());
	    assertNotNull(subOffer.getSubOfferDescEn());
	    assertNotNull(subOffer.getSubOfferDescAr());
	    assertNotNull(subOffer.getOldCost());
	    assertNotNull(subOffer.getOldPointValue());
	    assertNotNull(subOffer.getNewCost());
	    assertNotNull(subOffer.getNewPointValue());
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(subOffer.toString());
	    
			
	}
		
}
