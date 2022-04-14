package com.loyalty.marketplace.offers.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = PurchasePaymentMethodRequestDto.class)
@ActiveProfiles("unittest")
public class PurchasePaymentMethodRequestDtoTest {

	private PurchasePaymentMethodRequestDto requestDto;
	
	@Before
	public void setUp(){
		requestDto = new PurchasePaymentMethodRequestDto();
		requestDto.setPurchasePaymentMethods(new ArrayList<PurchasePaymentMethodDto>());
		
	}
	
	@Test
	public void testGetters() {
		assertNotNull(requestDto.getPurchasePaymentMethods());	
		
	}
	
	@Test
	public void testToString() {
		assertNotNull(requestDto.toString());
	}
	
}
