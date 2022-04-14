package com.loyalty.marketplace.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=Denomination.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class DenominationTest {
	
	
	@InjectMocks
	Denomination denominationMock;
	
	private Denomination denomination;
		
	@Before
	public void setUp(){
		
		denomination = new Denomination();
		denomination.setProgramCode("");
		denomination.setDenominationId("");
		denomination.setDenominationDescription(new DenominationDescription());
		denomination.setDenominationValue(new DenominationValue());
		denomination.setUsrUpdated("");
		denomination.setUsrCreated("");
		denomination.setDtCreated(new Date());
		denomination.setDtUpdated(new Date());		
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(denomination.getProgramCode());
		assertNotNull(denomination.getDenominationId());
		assertNotNull(denomination.getDenominationDescription());
		assertNotNull(denomination.getDenominationValue());
		assertNotNull(denomination.getUsrCreated());
	    assertNotNull(denomination.getUsrUpdated());
	    assertNotNull(denomination.getDtCreated());
	    assertNotNull(denomination.getDtUpdated());
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
