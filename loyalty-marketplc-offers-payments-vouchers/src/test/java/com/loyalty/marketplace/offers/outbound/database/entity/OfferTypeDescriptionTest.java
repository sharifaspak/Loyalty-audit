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

@SpringBootTest(classes = OfferTypeDescription.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OfferTypeDescriptionTest {
	
	@InjectMocks
	OfferTypeDescription offerTypeDescriptionMock;
	
	private OfferTypeDescription offerTypeDescription;
		
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		offerTypeDescription = new OfferTypeDescription();
		offerTypeDescription.setTypeDescriptionEn("");
		offerTypeDescription.setTypeDescriptionAr("");
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(offerTypeDescription.getTypeDescriptionEn());
	    assertNotNull(offerTypeDescription.getTypeDescriptionAr());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(offerTypeDescription.toString());
			
	}
		
}
