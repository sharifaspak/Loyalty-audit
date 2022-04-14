package com.loyalty.marketplace.offers.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=MemberRatingDto.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class MemberRatingDtoTest {
	
	private MemberRatingDto memberRating;
		
	@Before
	public void setUp(){
		
		memberRating = new MemberRatingDto();
		memberRating.setAccountNumber("");
		memberRating.setComments(new ArrayList<>());
		memberRating.setFirstName("");
		memberRating.setLastName("");
		memberRating.setMembershipCode("");
		
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(memberRating.getAccountNumber());
		assertNotNull(memberRating.getComments());
		assertNotNull(memberRating.getFirstName());
		assertNotNull(memberRating.getLastName());
		assertNotNull(memberRating.getMembershipCode());
		
		
	
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
