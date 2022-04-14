package com.loyalty.marketplace.offers.outbound.database.entity;

import static org.junit.Assert.assertNotNull;


import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=OfferDetails.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OfferDetailsTest {
	
	
	@InjectMocks
	OfferDetails offerDetailsMock;
	
	private OfferDetails offerDetails;
		
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		offerDetails = new OfferDetails();
		offerDetails.setOfferLabel(new OfferLabel());
		offerDetails.setOfferTitle(new OfferTitle());
	    offerDetails.setOfferTitleDescription(new OfferTitleDescription());
	    offerDetails.setOfferMobile(new OfferDetailMobile());
	    offerDetails.setOfferWeb(new OfferDetailWeb());		
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(offerDetails.getOfferLabel());
		assertNotNull(offerDetails.getOfferTitle());
	    assertNotNull(offerDetails.getOfferTitleDescription());
	    assertNotNull(offerDetails.getOfferMobile());
	    assertNotNull(offerDetails.getOfferWeb());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(offerDetails.toString());
			
	}
		
}
