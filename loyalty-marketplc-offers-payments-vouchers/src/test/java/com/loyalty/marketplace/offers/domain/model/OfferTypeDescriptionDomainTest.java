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
public class OfferTypeDescriptionDomainTest {
	
	private OfferTypeDescriptionDomain offerTypeDescription;
		
	@Before
	public void setUp(){
		
		offerTypeDescription = new OfferTypeDescriptionDomain();
		offerTypeDescription = new OfferTypeDescriptionDomain("En","Ar");
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(offerTypeDescription.getTypeDescriptionEn());
	    assertNotNull(offerTypeDescription.getTypeDescriptionAr());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(offerTypeDescription.toString());
	    
			
	}
		
}
