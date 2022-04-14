package com.loyalty.marketplace.offers.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=OfferDate.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OfferDateTest {
	
	
	@InjectMocks
	OfferDate offerDateMock;
	
	private OfferDate offerDate;
		
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		offerDate = new OfferDate();
		offerDate.setOfferStartDate(new Date());
		offerDate.setOfferEndDate(new Date());	
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(offerDate.getOfferStartDate());
	    assertNotNull(offerDate.getOfferEndDate());
			
	}
	
	/**
	 * 
	 * Test toString method
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(offerDate.toString());
	}
		
}
