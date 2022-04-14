package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=BirthdaySubTitleDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class BirthdaySubTitleDomainTest {
	
	private BirthdaySubTitleDomain birthdaySubTitle;
		
	@Before
	public void setUp(){
		
		birthdaySubTitle = new BirthdaySubTitleDomain();
		birthdaySubTitle = new BirthdaySubTitleDomain("", "");
				
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
