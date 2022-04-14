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

import com.loyalty.marketplace.offers.constants.OfferConstants;

@SpringBootTest(classes=PaymentMethod.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class PaymentMethodTest {
	
	
	@InjectMocks
	PaymentMethod paymentMethodMock;
	
	private PaymentMethod paymentMethod;
		
	@Before
	public void setUp(){
		
		paymentMethod = new PaymentMethod();
		paymentMethod.setProgramCode("");
		paymentMethod.setDescription(OfferConstants.SPACE_CHARACTER.get());
		paymentMethod.setUsrCreated(OfferConstants.SPACE_CHARACTER.get());
		paymentMethod.setUsrUpdated(OfferConstants.SPACE_CHARACTER.get());
		paymentMethod.setPaymentMethodId(OfferConstants.SPACE_CHARACTER.get());
		paymentMethod.setDtCreated(new Date());
		paymentMethod.setDtUpdated(new Date());
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(paymentMethod.getProgramCode());
		assertNotNull(paymentMethod.getPaymentMethodId());
	    assertNotNull(paymentMethod.getDescription());
	    assertNotNull(paymentMethod.getUsrCreated());
	    assertNotNull(paymentMethod.getUsrUpdated());
	    assertNotNull(paymentMethod.getDtCreated());
	    assertNotNull(paymentMethod.getDtUpdated());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
	   
		assertNotNull(paymentMethod.toString());
	}	
}
