package com.loyalty.marketplace.offers.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = BirthdayGiftTracker.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class BirthdayGiftTrackerTest {
	
	private BirthdayGiftTracker birthdayGiftTracker;
	
	@Before
	public void setUp(){
		
		birthdayGiftTracker = new BirthdayGiftTracker();
		birthdayGiftTracker.setAccountNumber("");
		birthdayGiftTracker.setMembershipCode("");
		birthdayGiftTracker.setProgramCode("programCode");
		birthdayGiftTracker.setLastViewedDate(new Date());
		birthdayGiftTracker.setBirthDate(new Date());
		birthdayGiftTracker.setId("");
		birthdayGiftTracker.setCreatedAt(new Date());
		birthdayGiftTracker.setUpdatedAt(new Date());
		birthdayGiftTracker.setCreatedBy("");
		birthdayGiftTracker.setUpdatedBy("");
		
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(birthdayGiftTracker.getAccountNumber());
		assertNotNull(birthdayGiftTracker.getMembershipCode());
		assertNotNull(birthdayGiftTracker.getProgramCode());
		assertNotNull(birthdayGiftTracker.getLastViewedDate());
		assertNotNull(birthdayGiftTracker.getBirthDate());
		assertNotNull(birthdayGiftTracker.getId());
		assertNotNull(birthdayGiftTracker.getCreatedAt());
		assertNotNull(birthdayGiftTracker.getUpdatedAt());
		assertNotNull(birthdayGiftTracker.getCreatedBy());
		assertNotNull(birthdayGiftTracker.getUpdatedBy());
		
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(birthdayGiftTracker.toString());
	    
	}
		
}
