package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=BirthdayWeekIconDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class BirthdayWeekIconDomainTest {
	
	private BirthdayWeekIconDomain birthdayWeekIcon;
		
	@Before
	public void setUp(){
		
		birthdayWeekIcon = new BirthdayWeekIconDomain();
		birthdayWeekIcon = new BirthdayWeekIconDomain("", "");
				
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
