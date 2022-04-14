package com.loyalty.marketplace.offers.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=BirthdaySubTitle.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class BirthdaySubTitleTest {
	
	private BirthdaySubTitle birthdaySubTitle;
		
	@Before
	public void setUp(){
		
		birthdaySubTitle = new BirthdaySubTitle();
		birthdaySubTitle.setSubTitleEn("");
		birthdaySubTitle.setSubTitleAr("");
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		
		assertNotNull(birthdaySubTitle.getSubTitleEn());
	    assertNotNull(birthdaySubTitle.getSubTitleAr());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(birthdaySubTitle.toString());
	    
			
	}
		
}
