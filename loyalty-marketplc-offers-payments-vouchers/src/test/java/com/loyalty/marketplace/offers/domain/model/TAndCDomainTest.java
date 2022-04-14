package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=TAndCDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class TAndCDomainTest {
	
	private TAndCDomain tAndC;
		
	@Before
	public void setUp(){
		
		tAndC = new TAndCDomain();
		tAndC = new TAndCDomain("En","Ar");
				
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
		
}
