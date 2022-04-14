package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = AdditionalDetailsDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class AdditionalDetailsDomainTest {
	
	private AdditionalDetailsDomain additionalDetails;
	
	@Before
	public void setUp(){
		
		additionalDetails = new AdditionalDetailsDomain();
		additionalDetails = new AdditionalDetailsDomain(true,
				true, true, true, true, true, true);
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
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
