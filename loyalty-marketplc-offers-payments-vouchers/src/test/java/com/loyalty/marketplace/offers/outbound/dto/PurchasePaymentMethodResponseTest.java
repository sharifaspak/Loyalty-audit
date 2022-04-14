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

@SpringBootTest(classes = PurchasePaymentMethodResponse.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class PurchasePaymentMethodResponseTest {
	private PurchasePaymentMethodResponse purchasePaymentMethodResponse;
    private String externalTransactionId;
	
    @Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		purchasePaymentMethodResponse = new PurchasePaymentMethodResponse(externalTransactionId);
		purchasePaymentMethodResponse.setPurchasePaymentMethods(new ArrayList<PurchasePaymentMethodsDto>());
	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {

		assertNotNull(purchasePaymentMethodResponse.getPurchasePaymentMethods());

	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {

		assertNotNull(purchasePaymentMethodResponse.toString());

	}
}