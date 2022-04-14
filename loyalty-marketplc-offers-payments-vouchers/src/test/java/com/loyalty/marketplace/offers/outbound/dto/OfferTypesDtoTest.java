package com.loyalty.marketplace.offers.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.loyalty.marketplace.offers.inbound.dto.PaymentMethodDto;

@SpringBootTest(classes=OfferTypesDto.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OfferTypesDtoTest {
	
	
	@InjectMocks
	OfferTypesDto offerTypesDto;
	
	private OfferTypesDto offerTypes;
		
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		offerTypes = new OfferTypesDto();
		
		offerTypes.setOfferTypeId("");;
		offerTypes.setDescriptionEn("");
		offerTypes.setDescriptionAr("");
		offerTypes.setPaymentMethods(new ArrayList<PaymentMethodDto>());
		
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(offerTypes.getOfferTypeId());
	    assertNotNull(offerTypes.getDescriptionEn());
	    assertNotNull(offerTypes.getDescriptionAr());
	    assertNotNull(offerTypes.getPaymentMethods());
	    
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(offerTypes.toString());
	    
			
	}
		
}
