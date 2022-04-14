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

@SpringBootTest(classes = OfferTitleDescription.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OfferTitleDescriptionTest {
	
	@InjectMocks
	OfferTitleDescription offerTitleDescriptionMock;
	
	private OfferTitleDescription offerTitleDescription;
		
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		offerTitleDescription = new OfferTitleDescription();
		offerTitleDescription.setOfferTitleDescriptionEn("");
		offerTitleDescription.setOfferTitleDescriptionAr("");
				
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
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(offerTitleDescription.toString());
			
	}
		
}
