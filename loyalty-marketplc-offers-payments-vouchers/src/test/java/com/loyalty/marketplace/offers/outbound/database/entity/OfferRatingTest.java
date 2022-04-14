package com.loyalty.marketplace.offers.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = OfferRating.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OfferRatingTest {
	
	private OfferRating offerRating;
	
	@Before
	public void setUp(){
		
		offerRating = new OfferRating();
		offerRating.setAverageRating(0.0);
		offerRating.setCommentCount(0);
		offerRating.setId("");
		offerRating.setCreatedUser("");	
		offerRating.setUpdatedUser("");
		offerRating.setProgramCode("");
		offerRating.setRatingCount(0);
		offerRating.setCreatedDate(new Date());
		offerRating.setUpdatedDate(new Date());
		offerRating.setMemberRatings(new ArrayList<>());
		offerRating.setOfferId("");
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(offerRating.getAverageRating());
		assertNotNull(offerRating.getOfferId());
		assertNotNull(offerRating.getCommentCount());
		assertNotNull(offerRating.getCreatedDate());
		assertNotNull(offerRating.getCreatedUser());
		assertNotNull(offerRating.getUpdatedDate());
		assertNotNull(offerRating.getUpdatedUser());
		assertNotNull(offerRating.getId());
		assertNotNull(offerRating.getRatingCount());
		assertNotNull(offerRating.getCommentCount());
		assertNotNull(offerRating.getMemberRatings());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(offerRating.toString());
	    
	}
		
}
