package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = MemberRatingDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class MemberRatingDomainTest {
	
	private MemberRatingDomain memberRating;
	
	@Before
	public void setUp(){
		
		memberRating = new MemberRatingDomain();
		
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		memberRating = new MemberRatingDomain
				.MemberRatingBuilder("", "", "", "", new ArrayList<>())
				.build();
		assertNotNull(memberRating.getMembershipCode());
		assertNotNull(memberRating.getAccountNumber());
		assertNotNull(memberRating.getComments());
		assertNotNull(memberRating.getFirstName());	
		assertNotNull(memberRating.getLastName());
		
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(memberRating.toString());
	    
	}
		
}
