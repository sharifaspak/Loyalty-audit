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

@SpringBootTest(classes = SubOfferTitle.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class SubOfferTitleTest {
	
	@InjectMocks
	SubOfferTitle subOfferTitleMock;
	
	private SubOfferTitle subOfferTitle;
		
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		subOfferTitle = new SubOfferTitle();
		subOfferTitle.setSubOfferTitleEn("");
		subOfferTitle.setSubOfferTitleAr("");
				
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
