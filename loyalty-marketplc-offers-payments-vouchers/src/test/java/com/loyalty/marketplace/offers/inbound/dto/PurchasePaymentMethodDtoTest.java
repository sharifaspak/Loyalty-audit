package com.loyalty.marketplace.offers.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = PurchasePaymentMethodDto.class)
@ActiveProfiles("unittest")
public class PurchasePaymentMethodDtoTest {

	private PurchasePaymentMethodDto requestDto;
	
	@Before
	public void setUp(){
		requestDto = new PurchasePaymentMethodDto();
		requestDto.setPurchaseItem("");
		requestDto.setPaymentMethods(new ArrayList<>());
		
	}
	
	@Test
	public void testGetters() {
		assertNotNull(requestDto.getPurchaseItem());
		assertNotNull(requestDto.getPaymentMethods());
		
	}
	
	@Test
	public void testToString() {
		assertNotNull(requestDto.toString());
	}
	
}
