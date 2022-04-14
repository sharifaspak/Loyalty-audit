package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=MemberOfferCountDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class MemberOfferCountDomainTest {
	
	private MemberOfferCountDomain memberOfferCountDomain;

	@Before
	public void setUp() throws ParseException {
		
		memberOfferCountDomain = new MemberOfferCountDomain();
		memberOfferCountDomain = new MemberOfferCountDomain("", 
				0, 0, 0, 0, 0, new Date(), new ArrayList<>());

	}
	
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(memberOfferCountDomain.getMembershipCode());
		assertNotNull(memberOfferCountDomain.getDailyCount());
		assertNotNull(memberOfferCountDomain.getWeeklyCount());
		assertNotNull(memberOfferCountDomain.getMonthlyCount());
		assertNotNull(memberOfferCountDomain.getAnnualCount());
		assertNotNull(memberOfferCountDomain.getTotalCount());
		assertNotNull(memberOfferCountDomain.getLastPurchased());
		assertNotNull(memberOfferCountDomain.getDenominationCount());

	}
	
	/**
	 * 
	 * Test toString method
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(memberOfferCountDomain.toString());
	}	
	

	
}
