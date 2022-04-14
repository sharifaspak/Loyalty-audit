package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = OfferLabelDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OfferLabelDomainTest {
	
	private OfferLabelDomain offerLabel;
		
	@Before
	public void setUp(){
		
		offerLabel = new OfferLabelDomain();
		offerLabel = new OfferLabelDomain("En","Ar");
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(offerLabel.getOfferLabelEn());
	    assertNotNull(offerLabel.getOfferLabelAr());
			
	}
		
}
