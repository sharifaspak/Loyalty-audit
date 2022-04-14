package com.loyalty.marketplace.offers.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=ActivityCodeDescription.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class ActivityCodeDescriptionTest {
	
	private ActivityCodeDescription activityCodeDescription;
		
	@Before
	public void setUp(){
		
		activityCodeDescription = new ActivityCodeDescription();
		activityCodeDescription.setActivityCodeDescriptionEn("");
		activityCodeDescription.setActivityCodeDescriptionAr("");
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(activityCodeDescription.getActivityCodeDescriptionEn());
	    assertNotNull(activityCodeDescription.getActivityCodeDescriptionAr());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(activityCodeDescription.toString());
	    
			
	}
		
}
