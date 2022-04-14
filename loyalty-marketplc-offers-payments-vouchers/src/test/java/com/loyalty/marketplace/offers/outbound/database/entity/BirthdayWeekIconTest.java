package com.loyalty.marketplace.offers.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=BirthdayWeekIcon.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class BirthdayWeekIconTest {
	
	private BirthdayWeekIcon birthdayWeekIcon;
		
	@Before
	public void setUp(){
		
		birthdayWeekIcon = new BirthdayWeekIcon();
		birthdayWeekIcon.setWeekIconEn("");
		birthdayWeekIcon.setWeekIconAr("");
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		
		assertNotNull(birthdayWeekIcon.getWeekIconEn());
	    assertNotNull(birthdayWeekIcon.getWeekIconAr());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(birthdayWeekIcon.toString());
	    
			
	}
		
}
