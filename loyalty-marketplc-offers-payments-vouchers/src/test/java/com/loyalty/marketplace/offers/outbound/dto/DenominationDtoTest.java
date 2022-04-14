package com.loyalty.marketplace.offers.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=DenominationDto.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class DenominationDtoTest {
	
	
	private DenominationDto denomination;
		
	@Before
	public void setUp(){
		
		denomination = new DenominationDto();
		denomination.setDenominationId("");
		denomination.setDenominationDescriptionEn("");
		denomination.setDenominationDescriptionAr("");
		denomination.setPointValue(0);
		denomination.setDirhamValue(0);
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(denomination.getDenominationId());
	    assertNotNull(denomination.getDenominationDescriptionEn());
	    assertNotNull(denomination.getDenominationDescriptionAr());
	    assertNotNull(denomination.getPointValue());
	    assertNotNull(denomination.getDirhamValue());
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(denomination.toString());
	    
			
	}
		
}
