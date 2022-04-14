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

@SpringBootTest(classes = PurchaseResponseDto.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class PurchaseResponseDtoTest {

	private PurchaseResponseDto purchaseResponseDto;

	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);
		purchaseResponseDto = new PurchaseResponseDto();
		purchaseResponseDto.setPaymentStatus("");
		purchaseResponseDto.setTransactionNo("");
		purchaseResponseDto.setActivityCode("");
		purchaseResponseDto.setEarnPointsRate(1.0);
		purchaseResponseDto.setPartnerCode("");
		purchaseResponseDto.setEpgTransactionId("");
		purchaseResponseDto.setVoucherCodes(new ArrayList<String>());
		purchaseResponseDto.setAdditionalParams("");

	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {

		assertNotNull(purchaseResponseDto.getPaymentStatus());
		assertNotNull(purchaseResponseDto.getTransactionNo());
		assertNotNull(purchaseResponseDto.getActivityCode());
		assertNotNull(purchaseResponseDto.getEarnPointsRate());
		assertNotNull(purchaseResponseDto.getPartnerCode());
		assertNotNull(purchaseResponseDto.getEpgTransactionId());
		assertNotNull(purchaseResponseDto.getVoucherCodes());
		assertNotNull(purchaseResponseDto.getAdditionalParams());

	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {

		assertNotNull(purchaseResponseDto.toString());

	}
}
