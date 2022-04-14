package com.loyalty.marketplace.offers.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=OfferType.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OfferTypeTest {
	
	private OfferType offerType;
	private String offerTypeId;
	private String programCode;
	private OfferTypeDescription offerDescription;
	private String usrCreated;
	private String usrUpdated;
	private Date dtCreated;
	private Date dtUpdated;
		
	@Before
	public void setUp(){
		
		offerTypeId = "offerTypeId";
		programCode="programCode";
		offerDescription =  new OfferTypeDescription();
		usrCreated = "usrCreated";
		usrUpdated = "usrUpdated";
		dtCreated = new Date();
		dtUpdated = new Date();
		
		offerType = new OfferType();
		offerType.setOfferTypeId(offerTypeId);
		offerType.setProgramCode(programCode);
		offerType.setOfferDescription(offerDescription);
		offerType.setDtCreated(dtCreated);
		offerType.setDtUpdated(dtUpdated);
		offerType.setUsrCreated(usrCreated);
		offerType.setUsrUpdated(usrUpdated);

	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(offerType.getOfferTypeId());
		assertNotNull(offerType.getProgramCode());
	    assertNotNull(offerType.getOfferDescription());
	    assertNotNull(offerType.getDtCreated());
	    assertNotNull(offerType.getDtUpdated());
	    assertNotNull(offerType.getUsrCreated());
	    assertNotNull(offerType.getUsrUpdated());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(offerType.toString());
			
	}
	
		
}
