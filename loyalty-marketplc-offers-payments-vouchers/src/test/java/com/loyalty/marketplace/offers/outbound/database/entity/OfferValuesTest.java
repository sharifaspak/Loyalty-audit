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

@SpringBootTest(classes = OfferValues.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OfferValuesTest {
	
	@InjectMocks
	OfferValues offerValuesMock;
	
	private OfferValues offerValues;
		
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		offerValues = new OfferValues();
		offerValues.setPointsValue(0);
		offerValues.setCost(0.0);
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(offerValues.getPointsValue());
	    assertNotNull(offerValues.getCost());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(offerValues.toString());
			
	}
		
}
