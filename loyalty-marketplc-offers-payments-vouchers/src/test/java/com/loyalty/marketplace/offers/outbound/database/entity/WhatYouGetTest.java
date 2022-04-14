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

@SpringBootTest(classes = WhatYouGet.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class WhatYouGetTest {
	
	@InjectMocks
	WhatYouGet whatYouGetMock;
	
	private WhatYouGet whatYouGet;
		
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		whatYouGet = new WhatYouGet();
		whatYouGet.setWhatYouGetEn("");
		whatYouGet.setWhatYouGetAr("");
				
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
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(whatYouGet.toString());
	    
			
	}
		
}
