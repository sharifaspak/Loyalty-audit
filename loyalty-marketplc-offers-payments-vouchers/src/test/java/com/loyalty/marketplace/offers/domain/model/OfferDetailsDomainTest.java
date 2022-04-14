package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=OfferDetailsDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OfferDetailsDomainTest {
	
	private OfferDetailsDomain offerDetails;
		
	@Before
	public void setUp(){
		
		offerDetails = new OfferDetailsDomain();
		offerDetails = new OfferDetailsDomain(new OfferLabelDomain("",""), new OfferTitleDomain("",""), new OfferTitleDescriptionDomain("",""),
				                 new OfferDetailMobileDomain("",""), new OfferDetailWebDomain("",""));
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(offerDetails.getOfferLabel());
		assertNotNull(offerDetails.getOfferTitle());
	    assertNotNull(offerDetails.getOfferTitleDescription());
	    assertNotNull(offerDetails.getOfferMobile());
	    assertNotNull(offerDetails.getOfferWeb());
			
	}
		
}
