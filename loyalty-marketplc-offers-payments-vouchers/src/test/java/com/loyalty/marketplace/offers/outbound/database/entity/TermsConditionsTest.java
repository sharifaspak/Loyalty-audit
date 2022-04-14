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

@SpringBootTest(classes = TermsConditions.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class TermsConditionsTest {
	
	@InjectMocks
	TermsConditions termsConditionsMock;
	
	private TermsConditions termsConditions;
		
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		termsConditions = new TermsConditions();
		termsConditions.setTermsAndConditions(new TAndC());
		termsConditions.setAddTermsAndConditions(new AddTAndC());
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(termsConditions.getTermsAndConditions());
	    assertNotNull(termsConditions.getAddTermsAndConditions());
	    
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(termsConditions.toString());
	    
			
	}
		
}
