package com.loyalty.marketplace.domain.model;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=DenominationDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class DenominationDomainTest {
	
	private DenominationDomain denomination;
		
	@Before
	public void setUp(){
		
		denomination = new DenominationDomain();
		denomination = new DenominationDomain.DenominationBuilder("")
                       .denominationId("")
                       .programCode("")
                       .denominationValue(new DenominationValueDomain(0, 0))
                       .denominationDescription(new DenominationDescriptionDomain("", ""))
                       .dtCreated(new Date())
	       			   .usrCreated("")
	       			   .dtUpdated(new Date())
	       			   .usrUpdated("")
                       .build(); 						
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
