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

@SpringBootTest(classes = SubOfferDesc.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class SubOfferDescTest {
	
	@InjectMocks
	SubOfferDesc subOfferDescMock;
	
	private SubOfferDesc subOfferDesc;
		
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		subOfferDesc = new SubOfferDesc();
		subOfferDesc.setSubOfferDescEn("");
		subOfferDesc.setSubOfferDescAr("");
				
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
