package com.loyalty.marketplace.offers.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=MarketplaceActivity.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class MarketplaceActivityTest {
	
	private MarketplaceActivity marketplaceActivity;
	
	@Before
	public void setUp(){
		marketplaceActivity = new MarketplaceActivity();
		marketplaceActivity.setActivityId("");
		marketplaceActivity.setActivityCode("");
		marketplaceActivity.setActivityCodeDescription(new ActivityCodeDescription());
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(marketplaceActivity.getActivityId());
		assertNotNull(marketplaceActivity.getActivityCode());
		assertNotNull(marketplaceActivity.getActivityCodeDescription());
	    
	}
	
	@Test
	public void testToString() {
		
		assertNotNull(marketplaceActivity.toString());
		
	}
	
		
}
