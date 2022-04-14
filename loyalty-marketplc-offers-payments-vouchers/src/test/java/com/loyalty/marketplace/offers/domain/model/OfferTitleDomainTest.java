package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = OfferTitleDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OfferTitleDomainTest {
	
	private OfferTitleDomain offerTitle;
		
	@Before
	public void setUp(){
		
		offerTitle = new OfferTitleDomain();
		offerTitle = new OfferTitleDomain("En","Ar");
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(offerTitle.getOfferTitleEn());
	    assertNotNull(offerTitle.getOfferTitleAr());
			
	}
		
}
