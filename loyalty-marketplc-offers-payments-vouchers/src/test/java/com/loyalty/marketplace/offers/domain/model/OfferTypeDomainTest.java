package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.loyalty.marketplace.domain.model.PaymentMethodDomain;

@SpringBootTest(classes=OfferTypeDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OfferTypeDomainTest {
	
	private OfferTypeDomain offerType;
	private String id;
	private String offerTypeId;
	private OfferTypeDescriptionDomain offerDescription;
	private List<PaymentMethodDomain> paymentMethods;
	private String usrCreated;
	private String usrUpdated;
	private Date dtCreated;
	private Date dtUpdated;
		
	@Before
	public void setUp(){
		
		id = "id";
		offerTypeId = "offerTypeId";
		offerDescription =  new OfferTypeDescriptionDomain();
		paymentMethods = new ArrayList<>();
		usrCreated = "usrCreated";
		usrUpdated = "usrUpdated";
		dtCreated = new Date();
		dtUpdated = new Date();
		offerType = new OfferTypeDomain();
		offerType = new OfferTypeDomain(offerTypeId, offerDescription, paymentMethods, usrCreated, usrUpdated, dtCreated, dtUpdated);
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(offerType.getOfferTypeId());
	    assertNotNull(offerType.getOfferDescription());
	    assertNotNull(offerType.getPaymentMethods());
	    assertNotNull(offerType.getDtCreated());
	    assertNotNull(offerType.getDtUpdated());
	    assertNotNull(offerType.getUsrCreated());
	    assertNotNull(offerType.getUsrUpdated());
			
	}
	
	/**
	 * 
	 * Test Builder
	 * 
	 */
	@Test
	public void testBuilder() {
		
		offerType= new OfferTypeDomain.OfferTypeBuilder(id).offerTypeId(offerTypeId)
				     .offerDescription(offerDescription).paymentMethods(paymentMethods)
				     .dtUpdated(dtUpdated).usrUpdated(usrUpdated).dtCreated(dtCreated)
				     .usrCreated(usrCreated).build();
		assertNotNull(offerType);
		
	}
	
		
}
