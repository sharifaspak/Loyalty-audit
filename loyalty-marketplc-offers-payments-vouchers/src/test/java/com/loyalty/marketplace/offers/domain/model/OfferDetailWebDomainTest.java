package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=OfferDetailWebDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OfferDetailWebDomainTest {
	
	private OfferDetailWebDomain offerDetailWeb;
		
	@Before
	public void setUp(){
		
		offerDetailWeb = new OfferDetailWebDomain();
		offerDetailWeb = new OfferDetailWebDomain("En", "Ar");
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(offerDetailWeb.getOfferDetailWebEn());
	    assertNotNull(offerDetailWeb.getOfferDetailWebAr());
			
	}
		
}
