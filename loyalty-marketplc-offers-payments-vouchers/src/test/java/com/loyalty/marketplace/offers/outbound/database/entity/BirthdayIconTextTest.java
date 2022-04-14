package com.loyalty.marketplace.offers.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=BirthdayIconText.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class BirthdayIconTextTest {
	
	private BirthdayIconText birthdayIconText;
		
	@Before
	public void setUp(){
		
		birthdayIconText = new BirthdayIconText();
		birthdayIconText.setIconTextEn("");
		birthdayIconText.setIconTextAr("");
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		
		assertNotNull(birthdayIconText.getIconTextEn());
	    assertNotNull(birthdayIconText.getIconTextAr());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(birthdayIconText.toString());
	    
			
	}
		
}
