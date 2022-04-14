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

@SpringBootTest(classes = SubOffer.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class SubOfferTest {
	
	@InjectMocks
	SubOffer subOfferMock;
	
	private SubOffer subOffer;
		
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		subOffer = new SubOffer();
		subOffer.setSubOfferId("");
		subOffer.setSubOfferTitle(new SubOfferTitle());
		subOffer.setSubOfferDesc(new SubOfferDesc());
		subOffer.setSubOfferValue(new SubOfferValue());		
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		
		assertNotNull(subOffer.getSubOfferId());
	    assertNotNull(subOffer.getSubOfferDesc());
	    assertNotNull(subOffer.getSubOfferTitle());
	    assertNotNull(subOffer.getSubOfferValue());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(subOffer.toString());
			
	}
		
}
