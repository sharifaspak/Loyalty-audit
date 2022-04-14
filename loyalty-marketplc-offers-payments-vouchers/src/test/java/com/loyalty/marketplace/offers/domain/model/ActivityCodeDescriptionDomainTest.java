package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=ActivityCodeDescriptionDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class ActivityCodeDescriptionDomainTest {
	
	private ActivityCodeDescriptionDomain activityCodeDescriptionDomain;
		
	@Before
	public void setUp(){
		
		activityCodeDescriptionDomain = new ActivityCodeDescriptionDomain();
		activityCodeDescriptionDomain = new ActivityCodeDescriptionDomain("En", "Ar");
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(activityCodeDescriptionDomain.getActivityCodeDescriptionDescriptionEn());
	    assertNotNull(activityCodeDescriptionDomain.getActivityCodeDescriptionDescriptionAr());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(activityCodeDescriptionDomain.toString());
			
	}
		
}
