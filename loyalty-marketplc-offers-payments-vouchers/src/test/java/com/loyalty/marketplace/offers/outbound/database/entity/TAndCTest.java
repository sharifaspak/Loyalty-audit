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

@SpringBootTest(classes=TAndC.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class TAndCTest {
	
	
	@InjectMocks
	TAndC tAndCMock;
	
	private TAndC tAndC;
		
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		tAndC = new TAndC();
		tAndC.setTermsAndConditionsEn("");
		tAndC.setTermsAndConditionsAr("");
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(tAndC.getTermsAndConditionsEn());
	    assertNotNull(tAndC.getTermsAndConditionsAr());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(tAndC.toString());
	    
			
	}
		
}
