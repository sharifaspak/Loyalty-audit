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

@SpringBootTest(classes = OfferCounter.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OfferCounterTest {
	
	private OfferCounter offerCounter;
		
	@Before
	public void setUp(){
		
		offerCounter = new OfferCounter();
		offerCounter.setId("");
		offerCounter.setOfferId("");
		offerCounter.setDailyCount(0);
		offerCounter.setWeeklyCount(0);	
		offerCounter.setMonthlyCount(0);
		offerCounter.setAnnualCount(0);
		offerCounter.setTotalCount(0);
		offerCounter.setLastPurchased(new Date());
		offerCounter.setDenominationCount(new ArrayList<>());
		offerCounter.setMemberOfferCount(new ArrayList<>());
		offerCounter.setAccountOfferCount(new ArrayList<>());

	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(offerCounter.getId());
		assertNotNull(offerCounter.getOfferId());
		assertNotNull(offerCounter.getDailyCount());
		assertNotNull(offerCounter.getWeeklyCount());
		assertNotNull(offerCounter.getMonthlyCount());
		assertNotNull(offerCounter.getAnnualCount());
		assertNotNull(offerCounter.getTotalCount());
		assertNotNull(offerCounter.getLastPurchased());
		assertNotNull(offerCounter.getDenominationCount());
		assertNotNull(offerCounter.getMemberOfferCount());
		assertNotNull(offerCounter.getAccountOfferCount());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(offerCounter.toString());
	    
			
	}

}
