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

@SpringBootTest(classes=AddTAndC.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class AddTAndCTest {
	
	
	@InjectMocks
	AddTAndC addTandCMock;
	
	private AddTAndC addTAndC;
		
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		addTAndC = new AddTAndC();
		addTAndC.setAdditionalTermsAndConditionsEn("");
		addTAndC.setAdditionalTermsAndConditionsAr("");
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(addTAndC.getAdditionalTermsAndConditionsEn());
	    assertNotNull(addTAndC.getAdditionalTermsAndConditionsAr());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(addTAndC.toString());
	    
			
	}
		
}
