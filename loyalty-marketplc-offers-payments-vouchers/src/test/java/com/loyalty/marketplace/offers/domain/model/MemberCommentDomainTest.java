package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = MemberCommentDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class MemberCommentDomainTest {
	
	private MemberCommentDomain memberComment;
	
	@Before
	public void setUp(){
		
		memberComment = new MemberCommentDomain();
		memberComment = new MemberCommentDomain(0, "", new Date());
	}	
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		memberComment = new MemberCommentDomain.MemberCommentBuilder(new Date())
				.rating(0)
				.comment("")
				.build();
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
