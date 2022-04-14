package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=OfferDetailMobileDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OfferDetailMobileDomainTest {
	
	private OfferDetailMobileDomain offerDetailMobile;
		
	@Before
	public void setUp(){
		
		offerDetailMobile = new OfferDetailMobileDomain();
		offerDetailMobile = new OfferDetailMobileDomain("En", "Ar");
				
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
		
}
