package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = OfferTitleDescriptionDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OfferTitleDescriptionDomainTest {
	
	private OfferTitleDescriptionDomain offerTitleDescription;
		
	@Before
	public void setUp(){
		
		offerTitleDescription = new OfferTitleDescriptionDomain();
		offerTitleDescription = new OfferTitleDescriptionDomain("En","Ar");
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(offerTitleDescription.getOfferTitleDescriptionEn());
	    assertNotNull(offerTitleDescription.getOfferTitleDescriptionAr());
			
	}
		
}
