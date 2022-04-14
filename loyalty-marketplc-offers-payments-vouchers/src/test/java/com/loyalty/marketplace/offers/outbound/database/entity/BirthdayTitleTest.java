package com.loyalty.marketplace.offers.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=BirthdayTitle.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class BirthdayTitleTest {
	
	private BirthdayTitle birthdayTitle;
		
	@Before
	public void setUp(){
		
		birthdayTitle = new BirthdayTitle();
		birthdayTitle.setTitleEn("");
		birthdayTitle.setTitleAr("");
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		
		assertNotNull(birthdayTitle.getTitleEn());
	    assertNotNull(birthdayTitle.getTitleAr());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(birthdayTitle.toString());
	    
			
	}
		
}
