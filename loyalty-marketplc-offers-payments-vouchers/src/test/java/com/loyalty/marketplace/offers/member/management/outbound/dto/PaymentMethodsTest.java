package com.loyalty.marketplace.offers.member.management.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = PaymentMethods.class)
@ActiveProfiles("unittest")
public class PaymentMethodsTest {

	private PaymentMethods paymentMethods;
	
	@Before
	public void setUp(){
		paymentMethods = new PaymentMethods();
		paymentMethods.setPaymentMethodId("");
		paymentMethods.setDescription("");
		
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(paymentMethods.getPaymentMethodId());
		assertNotNull(paymentMethods.getDescription());
	
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(paymentMethods.toString());
	}
	
}
