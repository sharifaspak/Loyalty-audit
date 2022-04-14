package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = SubOfferValueDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class SubOfferValueDomainTest {
	
	private SubOfferValueDomain subOfferValue;
		
	@Before
	public void setUp(){
		
		subOfferValue = new SubOfferValueDomain();
		subOfferValue = new SubOfferValueDomain(0.0,0,0.0,0);
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(subOfferValue.getOldCost());
	    assertNotNull(subOfferValue.getOldPointValue());
	    assertNotNull(subOfferValue.getNewCost());
	    assertNotNull(subOfferValue.getNewPointValue());
	
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(subOfferValue.toString());
			
	}
		
}
