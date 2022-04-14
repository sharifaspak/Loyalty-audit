package com.loyalty.marketplace.offers.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = BirthdayInfo.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class BirthdayInfoTest {
	
	private BirthdayInfo birthdayInfo;
	
	@Before
	public void setUp(){
		
		birthdayInfo = new BirthdayInfo();
		birthdayInfo.setId("");
		birthdayInfo.setProgramCode("programCode");
		birthdayInfo.setTitle(new BirthdayTitle());
		birthdayInfo.setSubTitle(new BirthdaySubTitle());
		birthdayInfo.setDescription(new BirthdayDescription());
		birthdayInfo.setIconText(new BirthdayIconText());
		birthdayInfo.setWeekIcon(new BirthdayWeekIcon());
		birthdayInfo.setPurchaseLimit(0);
		birthdayInfo.setDisplayLimit(0);
		birthdayInfo.setThresholdMinusValue(0);
		birthdayInfo.setThresholdPlusValue(0);
		birthdayInfo.setCreatedDate(new Date());
		birthdayInfo.setUpdatedDate(new Date());
		birthdayInfo.setCreatedUser("");
		birthdayInfo.setUpdatedUser("");
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(birthdayInfo.getId());
		assertNotNull(birthdayInfo.getProgramCode());
		assertNotNull(birthdayInfo.getTitle());
		assertNotNull(birthdayInfo.getSubTitle());
		assertNotNull(birthdayInfo.getDescription());
		assertNotNull(birthdayInfo.getIconText());
		assertNotNull(birthdayInfo.getWeekIcon());
		assertNotNull(birthdayInfo.getPurchaseLimit());
		assertNotNull(birthdayInfo.getDisplayLimit());
		assertNotNull(birthdayInfo.getThresholdMinusValue());
		assertNotNull(birthdayInfo.getThresholdPlusValue());
		assertNotNull(birthdayInfo.getCreatedDate());
		assertNotNull(birthdayInfo.getUpdatedDate());
		assertNotNull(birthdayInfo.getCreatedUser());
		assertNotNull(birthdayInfo.getUpdatedUser());
		
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(birthdayInfo.toString());
	    
	}
		
}
