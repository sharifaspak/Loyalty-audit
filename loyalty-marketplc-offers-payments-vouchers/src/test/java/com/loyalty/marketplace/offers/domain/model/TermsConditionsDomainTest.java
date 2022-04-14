package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = TermsConditionsDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class TermsConditionsDomainTest {
	
	private TermsConditionsDomain termsConditions;
		
	@Before
	public void setUp(){
		
		termsConditions = new TermsConditionsDomain();
		termsConditions = new TermsConditionsDomain(new TAndCDomain("",""), new AddTAndCDomain("",""));
				
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
		
}
