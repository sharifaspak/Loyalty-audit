package com.loyalty.marketplace.offers.outbound.dto;

import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = PurchaseResultResponse.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class PurchaseResultResponseTest {

	private PurchaseResultResponse purchaseResultResponse;

	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);
		purchaseResultResponse = new PurchaseResultResponse("");
		purchaseResultResponse.setPurchaseResponseDto(new PurchaseResponseDto());

	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {

		assertNotNull(purchaseResultResponse.getPurchaseResponseDto());

	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {

		assertNotNull(purchaseResultResponse.toString());

	}

}
