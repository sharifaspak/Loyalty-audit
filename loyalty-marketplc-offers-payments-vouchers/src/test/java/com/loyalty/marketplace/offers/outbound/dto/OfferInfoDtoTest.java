package com.loyalty.marketplace.offers.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=OfferInfoDto.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OfferInfoDtoTest {
	
	
	@InjectMocks
	OfferInfoDto offerInfoDto;
	
	private OfferInfoDto offerInfo;
		
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		offerInfo = new OfferInfoDto();
		
		offerInfo.setOfferId("");
		offerInfo.setOfferTypeId("");
		offerInfo.setOfferTypeDescriptionEn("");
		offerInfo.setOfferTypeDescriptionAr("");
		offerInfo.setOfferTitleEn("");
		offerInfo.setOfferTitleAr("");
		offerInfo.setPointsValue(0);
		offerInfo.setDirhamValue(0);
		offerInfo.setSubOffer(new ArrayList<SubOfferDto>());
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(offerInfo.getOfferId());
	    assertNotNull(offerInfo.getOfferTypeId());
	    assertNotNull(offerInfo.getOfferTypeDescriptionEn());
	    assertNotNull(offerInfo.getOfferTypeDescriptionAr());
	    assertNotNull(offerInfo.getOfferTitleEn() );
	    assertNotNull(offerInfo.getOfferTitleAr());
	    assertNotNull(offerInfo.getPointsValue());
	    assertNotNull(offerInfo.getDirhamValue());
	    assertNotNull(offerInfo.getSubOffer());
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(offerInfo.toString());
	    
			
	}
		
}
