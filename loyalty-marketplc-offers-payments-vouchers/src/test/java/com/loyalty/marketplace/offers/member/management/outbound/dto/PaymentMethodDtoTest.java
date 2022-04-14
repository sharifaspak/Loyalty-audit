package com.loyalty.marketplace.offers.member.management.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = PaymentMethodDto.class)
@ActiveProfiles("unittest")
public class PaymentMethodDtoTest {
	
	private PaymentMethodDto paymentMethodDto;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		paymentMethodDto = new PaymentMethodDto();
		
		paymentMethodDto.setMethodId("");
		paymentMethodDto.setMethodDescription("");
		
		PaymentMethodDto payment = new PaymentMethodDto("","");
		payment.setMethodId("");
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(paymentMethodDto.getMethodId());
		assertNotNull(paymentMethodDto.getMethodDescription());
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
