package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = SubOfferTitleDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class SubOfferTitleDomainTest {
	
	private SubOfferTitleDomain subOfferTitle;
		
	@Before
	public void setUp(){
		
		subOfferTitle = new SubOfferTitleDomain();
		subOfferTitle = new SubOfferTitleDomain("","");
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(subOfferTitle.getSubOfferTitleEn());
	    assertNotNull(subOfferTitle.getSubOfferTitleAr());
	    	
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(subOfferTitle.toString());
			
	}
		
}
