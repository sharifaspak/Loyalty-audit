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

@SpringBootTest(classes = SubOfferValue.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class SubOfferValueTest {
	
	@InjectMocks
	SubOfferValue subOfferValueMock;
	
	private SubOfferValue subOfferValue;
		
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		subOfferValue = new SubOfferValue();
		subOfferValue.setNewCost(0.0);
		subOfferValue.setOldCost(0.0);
		subOfferValue.setNewPointValue(0);
		subOfferValue.setOldPointValue(0);
				
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
