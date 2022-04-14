package com.loyalty.marketplace.offers.outbound.database.entity;

import static org.junit.Assert.assertNotNull;


import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=ActivityCode.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class ActivityCodeTest {
	
	
	@InjectMocks
	ActivityCode activityCodeMock;
	
	private ActivityCode activityCode;
		
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		activityCode = new ActivityCode();
		activityCode.setAccrualActivityCode(new MarketplaceActivity());
		activityCode.setRedemptionActivityCode(new MarketplaceActivity());
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
