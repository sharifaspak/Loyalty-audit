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

@SpringBootTest(classes=DynamicDenominationValue.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class DynamicDenominationValueTest {
	
	
	@InjectMocks
	DynamicDenominationValue dynamicDenominationValueMock;
	
	private DynamicDenominationValue dynamicDenominationValue;
		
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		dynamicDenominationValue = new DynamicDenominationValue();
		dynamicDenominationValue.setMinDenomination(0);
		dynamicDenominationValue.setMaxDenomination(0);
		
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(dynamicDenominationValue.getMinDenomination());
	    assertNotNull(dynamicDenominationValue.getMaxDenomination());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(dynamicDenominationValue.toString());
	    
			
	}
		
}
