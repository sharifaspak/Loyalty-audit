package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=MarketplaceActivityDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class MarketplaceActivityDomainTest {
	
	private MarketplaceActivityDomain marketplaceActivity;
	
	@Before
	public void setUp(){
	
		marketplaceActivity = new MarketplaceActivityDomain();
		marketplaceActivity = new MarketplaceActivityDomain("",
				"", new ActivityCodeDescriptionDomain());
		
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(marketplaceActivity.getActivityCode());
	    assertNotNull(marketplaceActivity.getActivityId());
	    assertNotNull(marketplaceActivity.getActivityCodeDescription());
			
	}

	/**
	 * Test toString
	 */
	@Test
	public void testToString() {
		
		assertNotNull(marketplaceActivity.toString());
		
	}
	
		
}
