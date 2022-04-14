package com.loyalty.marketplace.voucher.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest(classes = PurchaseDetailsDto.class)
@ActiveProfiles("unittest")
public class PurchaseDetailsDtoTest {

	private PurchaseDetailsDto purchaseDetailsDto;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		purchaseDetailsDto = new PurchaseDetailsDto();
		purchaseDetailsDto.setAuthorizationCode("");
		purchaseDetailsDto.setCardExpiryDate("");
		purchaseDetailsDto.setCardNumber("");
		purchaseDetailsDto.setCardSubType("");
		purchaseDetailsDto.setCardToken("");
		purchaseDetailsDto.setCardType("");
		purchaseDetailsDto.setSpentAmount(0.0);
	}

	@Test
	public void testGetters() {
		assertNotNull(purchaseDetailsDto.getAuthorizationCode());
		assertNotNull(purchaseDetailsDto.getCardExpiryDate());
		assertNotNull(purchaseDetailsDto.getCardNumber());
		assertNotNull(purchaseDetailsDto.getCardSubType());
		assertNotNull(purchaseDetailsDto.getCardToken());
		assertNotNull(purchaseDetailsDto.getCardType());
		assertNotNull(purchaseDetailsDto.getSpentAmount());
	}
	
	@Test
	public void testToString() {
		assertNotNull(purchaseDetailsDto.toString());
	}

}