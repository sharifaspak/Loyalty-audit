package com.loyalty.marketplace.offers.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = PaymentMethodDto.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class PaymentMethodDtoTest {

	private PaymentMethodDto paymentMethodDto;

	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);
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
