package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=AddTAndCDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class AddTAndCDomainTest {
	
	
	private AddTAndCDomain addTAndC;
		
	@Before
	public void setUp(){
		
		addTAndC = new AddTAndCDomain();
		addTAndC = new AddTAndCDomain("En","Ar");
				
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
		
}
