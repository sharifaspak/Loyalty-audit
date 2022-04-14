package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = OfferLimitDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OfferLimitDomainTest {
	
	private OfferLimitDomain offerLimit;
		
	@Before
	public void setUp(){
		
		offerLimit = new OfferLimitDomain();
		offerLimit = new OfferLimitDomain(null, 0,0,0,0,0,0,null,0,0,0,0,0,null,0,0,0,0,0,null);
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(offerLimit.getCouponQuantity());
	    assertNotNull(offerLimit.getDownloadLimit());
	    assertNotNull(offerLimit.getDailyLimit());
	    assertNotNull(offerLimit.getWeeklyLimit());
	    assertNotNull(offerLimit.getMonthlyLimit());
	    assertNotNull(offerLimit.getAccountDailyLimit());
	    assertNotNull(offerLimit.getAccountMonthlyLimit());
	    assertNotNull(offerLimit.getAccountWeeklyLimit());
			
	}
		
}
