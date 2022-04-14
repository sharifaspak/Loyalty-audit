package com.loyalty.marketplace.domain.model;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=PaymentMethodDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class PaymentMethodDomainTest {
	
	private PaymentMethodDomain paymentMethod;
		
	@Before
	public void setUp(){
		
		paymentMethod = new PaymentMethodDomain("",
				"", "", "", "", new Date(), new Date());
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
