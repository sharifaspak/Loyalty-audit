package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = WhatYouGetDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class WhatYouGetDomainTest {
	
	private WhatYouGetDomain whatYouGet;
		
	@Before
	public void setUp(){
        
		whatYouGet = new WhatYouGetDomain();
		whatYouGet = new WhatYouGetDomain("", "");
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(whatYouGet.getWhatYouGetEn());
	    assertNotNull(whatYouGet.getWhatYouGetAr());
			
	}
		
}
