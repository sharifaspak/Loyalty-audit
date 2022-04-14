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

@SpringBootTest(classes = OfferTitle.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OfferTitleTest {
	
	@InjectMocks
	OfferTitle offerTitleMock;
	
	private OfferTitle offerTitle;
		
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		offerTitle = new OfferTitle();
		offerTitle.setOfferTitleEn("");
		offerTitle.setOfferTitleAr("");
				
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
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(offerTitle.toString());
			
	}
		
}
