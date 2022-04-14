package com.loyalty.marketplace.offers.inbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = PaymentMethodDto.class)
@ActiveProfiles("unittest")
public class PaymentMethodDtoTest {

	private PaymentMethodDto paymentMethodDto;

	@Before
	public void setUp() {
		paymentMethodDto = new PaymentMethodDto();
		paymentMethodDto.setPaymentMethodId("");
		paymentMethodDto.setDescription("");
	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(paymentMethodDto.getPaymentMethodId());
		assertNotNull(paymentMethodDto.getDescription());
	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(paymentMethodDto.toString());
	}

}
