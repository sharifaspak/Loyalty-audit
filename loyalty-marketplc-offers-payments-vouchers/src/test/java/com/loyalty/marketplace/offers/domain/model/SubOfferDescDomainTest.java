package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = SubOfferDescDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class SubOfferDescDomainTest {
	
	private SubOfferDescDomain subOfferDesc;
		
	@Before
	public void setUp(){
		
		subOfferDesc = new SubOfferDescDomain();
		subOfferDesc = new SubOfferDescDomain("", "");
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(subOfferDesc.getSubOfferDescEn());
	    assertNotNull(subOfferDesc.getSubOfferDescAr());
	    
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(subOfferDesc.toString());
			
	}
		
}
