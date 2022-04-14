package com.loyalty.marketplace.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=DenominationValue.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class DenominationValueTest {
	
	
	@InjectMocks
	DenominationValue denominationValueMock;
	
	private DenominationValue denominationValue;
		
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		denominationValue = new DenominationValue();
		
		denominationValue.setPointValue(0);
		denominationValue.setDirhamValue(0);
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {

		assertNotNull(denominationValue.getPointValue());
	    assertNotNull(denominationValue.getDirhamValue());
	    
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
	   
		assertNotNull(denominationValue.toString());
	}	
}
