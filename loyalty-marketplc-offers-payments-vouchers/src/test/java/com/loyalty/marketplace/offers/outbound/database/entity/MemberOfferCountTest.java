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

@SpringBootTest(classes = MemberOfferCount.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class MemberOfferCountTest {
	
	private MemberOfferCount memberOfferCount;
		
	@Before
	public void setUp(){
		
		memberOfferCount = new MemberOfferCount();
		memberOfferCount.setMembershipCode("");
		memberOfferCount.setDailyCount(0);
		memberOfferCount.setWeeklyCount(0);	
		memberOfferCount.setMonthlyCount(0);
		memberOfferCount.setAnnualCount(0);
		memberOfferCount.setTotalCount(0);
		memberOfferCount.setLastPurchased(new Date());
		memberOfferCount.setDenominationCount(new ArrayList<>());

	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(memberOfferCount.getMembershipCode());
		assertNotNull(memberOfferCount.getDailyCount());
		assertNotNull(memberOfferCount.getWeeklyCount());
		assertNotNull(memberOfferCount.getMonthlyCount());
		assertNotNull(memberOfferCount.getAnnualCount());
		assertNotNull(memberOfferCount.getTotalCount());
		assertNotNull(memberOfferCount.getLastPurchased());
		assertNotNull(memberOfferCount.getDenominationCount());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(memberOfferCount.toString());
	    
			
	}

}
