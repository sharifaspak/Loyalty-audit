package com.loyalty.marketplace.offers.outbound.database.entity;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = AdditionalDetails.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class AdditionalDetailsTest {
	
	private AdditionalDetails additionalDetails;
	
	@Before
	public void setUp(){
		
		additionalDetails = new AdditionalDetails();
		additionalDetails.setBirthdayOffer(true);
		additionalDetails.setDod(true);
		additionalDetails.setFeatured(true);
		additionalDetails.setFree(true);
		additionalDetails.setGift(true);
		additionalDetails.setPromotionApplied(true);
		additionalDetails.setSubscribed(true);
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		additionalDetails = new AdditionalDetails(true, true, true, true, true, true, true);
		assertTrue(additionalDetails.isBirthdayOffer());
		assertTrue(additionalDetails.isDod());
		assertTrue(additionalDetails.isFeatured());
		assertTrue(additionalDetails.isFree());
		assertTrue(additionalDetails.isGift());
		assertTrue(additionalDetails.isPromotionApplied());
		assertTrue(additionalDetails.isSubscribed());
	
		
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(additionalDetails.toString());
	    
	}
		
}
