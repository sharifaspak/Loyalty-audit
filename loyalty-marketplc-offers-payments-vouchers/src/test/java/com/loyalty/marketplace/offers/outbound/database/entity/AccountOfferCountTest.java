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

@SpringBootTest(classes = AccountOfferCount.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class AccountOfferCountTest {
	
	private AccountOfferCount accountOfferCount;
	
	@Before
	public void setUp(){
		
		accountOfferCount = new AccountOfferCount();
		accountOfferCount.setMembershipCode("");
		accountOfferCount.setAccountNumber("");
		accountOfferCount.setDailyCount(0);
		accountOfferCount.setWeeklyCount(0);	
		accountOfferCount.setMonthlyCount(0);
		accountOfferCount.setAnnualCount(0);
		accountOfferCount.setTotalCount(0);
		accountOfferCount.setLastPurchased(new Date());
		accountOfferCount.setDenominationCount(new ArrayList<>());
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(accountOfferCount.getMembershipCode());
		assertNotNull(accountOfferCount.getAccountNumber());
		assertNotNull(accountOfferCount.getDailyCount());
		assertNotNull(accountOfferCount.getWeeklyCount());	
		assertNotNull(accountOfferCount.getMonthlyCount());
		assertNotNull(accountOfferCount.getAnnualCount());
		assertNotNull(accountOfferCount.getTotalCount());
		assertNotNull(accountOfferCount.getLastPurchased());
		assertNotNull(accountOfferCount.getDenominationCount());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(accountOfferCount.toString());
	    
	}
		
}
