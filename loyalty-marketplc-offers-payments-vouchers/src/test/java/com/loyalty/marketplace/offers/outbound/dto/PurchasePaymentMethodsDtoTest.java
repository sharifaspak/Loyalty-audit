package com.loyalty.marketplace.offers.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.loyalty.marketplace.offers.inbound.dto.PaymentMethodDto;

@SpringBootTest(classes = PurchasePaymentMethodsDto.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class PurchasePaymentMethodsDtoTest {

	private PurchasePaymentMethodsDto purchasePaymentMethodsDto;

	@Before
	public void setUp() {
		purchasePaymentMethodsDto = new PurchasePaymentMethodsDto();
		purchasePaymentMethodsDto.setPurchaseItem("");
		purchasePaymentMethodsDto.setPaymentMethods(new ArrayList<PaymentMethodDto>());
	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {

		assertNotNull(purchasePaymentMethodsDto.getPurchaseItem());
		assertNotNull(purchasePaymentMethodsDto.getPaymentMethods());
	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {

		assertNotNull(purchasePaymentMethodsDto.toString());

	}
}
