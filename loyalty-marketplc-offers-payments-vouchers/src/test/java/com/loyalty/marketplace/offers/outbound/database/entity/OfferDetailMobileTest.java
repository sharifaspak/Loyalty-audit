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

@SpringBootTest(classes=OfferDetailMobile.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OfferDetailMobileTest {
	
	
	@InjectMocks
	OfferDetailMobile offerDetailMobileMock;
	
	private OfferDetailMobile offerDetailMobile;
		
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		offerDetailMobile = new OfferDetailMobile();
		offerDetailMobile.setOfferDetailMobileEn("");
		offerDetailMobile.setOfferDetailMobileAr("");
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(offerDetailMobile.getOfferDetailMobileEn());
	    assertNotNull(offerDetailMobile.getOfferDetailMobileAr());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(offerDetailMobile.toString());
			
	}
		
}
