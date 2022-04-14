package com.loyalty.marketplace.offers.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = PaymentMethodResponse.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class PaymentMethodResponseTest {

	private PaymentMethodResponse paymentMethodResponse;

	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);
		paymentMethodResponse = new PaymentMethodResponse("");
		paymentMethodResponse.setPaymentMethods(new ArrayList<String>());

	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {

		assertNotNull(paymentMethodResponse.getPaymentMethods());

	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {

		assertNotNull(paymentMethodResponse.toString());

	}

}
