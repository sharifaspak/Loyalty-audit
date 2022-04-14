package com.loyalty.marketplace.offers.member.management.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = InterestDetails.class)
@ActiveProfiles("unittest")
public class InterestDetailsDtoTest {

	private InterestDetails interestDetails;
	
	@Before
	public void setUp(){
		interestDetails = new InterestDetails();
		interestDetails.setInterestList(new ArrayList<>());
		
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(interestDetails.getInterestList());
	
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(interestDetails.toString());
	}
	
}
