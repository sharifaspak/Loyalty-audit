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

@SpringBootTest(classes=OfferDetailWeb.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OfferDetailWebTest {
	
	
	@InjectMocks
	OfferDetailWeb offerDetailWebMock;
	
	private OfferDetailWeb offerDetailWeb;
		
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		offerDetailWeb = new OfferDetailWeb();
		offerDetailWeb.setOfferDetailWebEn("");
		offerDetailWeb.setOfferDetailWebAr("");
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(offerDetailWeb.getOfferDetailWebEn());
	    assertNotNull(offerDetailWeb.getOfferDetailWebAr());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(offerDetailWeb.toString());
			
	}
		
}
