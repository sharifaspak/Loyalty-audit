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

@SpringBootTest(classes = OfferLabel.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OfferLabelTest {
	
	@InjectMocks
	OfferLabel offerLabelMock;
	
	private OfferLabel offerLabel;
		
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		offerLabel = new OfferLabel();
		offerLabel.setOfferLabelEn("");
		offerLabel.setOfferLabelAr("");
				
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
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(offerLabel.toString());
			
	}
		
}
