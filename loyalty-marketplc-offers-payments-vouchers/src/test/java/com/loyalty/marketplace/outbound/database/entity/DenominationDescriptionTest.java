package com.loyalty.marketplace.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=DenominationDescription.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class DenominationDescriptionTest {
	
	
	@InjectMocks
	DenominationDescription denominationDescriptionMock;
	
	private DenominationDescription denominationDescription;
		
	@Before
	public void setUp(){
		
		denominationDescription = new DenominationDescription();
		denominationDescription.setDenominationDescriptionEn("");
		denominationDescription.setDenominationDescriptionAr("");
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {

		assertNotNull(denominationDescription.getDenominationDescriptionEn());
	    assertNotNull(denominationDescription.getDenominationDescriptionAr());
	    
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
	   
		assertNotNull(denominationDescription.toString());
	}	
}
