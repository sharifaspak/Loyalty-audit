package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=ActivityCodeDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class ActivityCodeDomainTest {
	
	private ActivityCodeDomain activityCode;
		
	@Before
	public void setUp(){
		
		activityCode = new ActivityCodeDomain();
		activityCode = new ActivityCodeDomain(new MarketplaceActivityDomain(), new MarketplaceActivityDomain());
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(activityCode.getAccrualActivityCode());
	    assertNotNull(activityCode.getRedemptionActivityCode());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(activityCode.toString());
			
	}
		
}
