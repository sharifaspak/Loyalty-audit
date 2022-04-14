package com.loyalty.marketplace.offers.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = PaymentMethodRequestDto.class)
@ActiveProfiles("unittest")
public class PaymentMethodRequestDtoTest {

	private PaymentMethodRequestDto paymentMethodRequestDto;
	
	@Before
	public void setUp(){
		paymentMethodRequestDto = new PaymentMethodRequestDto();
		paymentMethodRequestDto.setPaymentMethods(new ArrayList<PaymentMethodDto>());
	}
	
	@Test
	public void testGetters() {
		assertNotNull(paymentMethodRequestDto.getPaymentMethods());
	}
	
	@Test
	public void testToString() {
		assertNotNull(paymentMethodRequestDto.toString());
	}
	
}
