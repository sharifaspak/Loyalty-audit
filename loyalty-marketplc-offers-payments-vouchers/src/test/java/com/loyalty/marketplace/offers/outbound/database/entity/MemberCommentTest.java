package com.loyalty.marketplace.offers.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = MemberComment.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class MemberCommentTest {
	
	private MemberComment memberComment;
	
	@Before
	public void setUp(){
		
		memberComment = new MemberComment();
		memberComment.setComment("");
		memberComment.setRating(0);
		memberComment.setReviewDate(new Date());
	}	
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(memberComment.getComment());
		assertNotNull(memberComment.getRating());
		assertNotNull(memberComment.getReviewDate());		
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(memberComment.toString());
	    
	}
		
}
