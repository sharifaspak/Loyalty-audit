package com.loyalty.marketplace.offers.inbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = PurchaseRequestDto.class)
@ActiveProfiles("unittest")
public class PurchaseRequestDtoTest {

	private PurchaseRequestDto purchaseRequestDto;

	@Before
	public void setUp() {
		purchaseRequestDto = new PurchaseRequestDto();
		purchaseRequestDto.setSelectedPaymentItem("");
		purchaseRequestDto.setSelectedOption("");
		purchaseRequestDto.setSpentPoints(1);
		purchaseRequestDto.setSpentAmount(1.0);
		purchaseRequestDto.setCardNumber("");
		purchaseRequestDto.setCardType("");
		purchaseRequestDto.setCardSubType("");
		purchaseRequestDto.setCardToken("");
		purchaseRequestDto.setCardExpiryDate("");
		purchaseRequestDto.setMsisdn("");
		purchaseRequestDto.setAuthorizationCode("");
		purchaseRequestDto.setPaymentType("");
		purchaseRequestDto.setOfferId("");
		purchaseRequestDto.setSubOfferId("");
		purchaseRequestDto.setCouponQuantity(12);
		purchaseRequestDto.setVoucherDenomination(13);
		purchaseRequestDto.setAccountNumber("");
		purchaseRequestDto.setMembershipCode("");
		purchaseRequestDto.setAtgUserName("");
		purchaseRequestDto.setLevel("");
		purchaseRequestDto.setPreferredNumber("");
		purchaseRequestDto.setExtTransactionId("");
		purchaseRequestDto.setEpgTransactionId("");
		purchaseRequestDto.setPartialTransactionId("");
		purchaseRequestDto.setPromoCode("");
		purchaseRequestDto.setUiLanguage("");
		purchaseRequestDto.setOfferTitle("");
		purchaseRequestDto.setOrderId("");
		purchaseRequestDto.setSubscriptionCatalogId("");
		purchaseRequestDto.setSubscriptionMethod("");
		purchaseRequestDto.setFreeSubscriptionDays(0);
		purchaseRequestDto.setStartDate("");
		purchaseRequestDto.setAdditionalParams("");

	}

	@Test
	public void testGetters() {
		assertNotNull(purchaseRequestDto.getSelectedPaymentItem());
		assertNotNull(purchaseRequestDto.getSelectedOption());
		assertNotNull(purchaseRequestDto.getSpentPoints());
		assertNotNull(purchaseRequestDto.getCardNumber());
		assertNotNull(purchaseRequestDto.getCardType());
		assertNotNull(purchaseRequestDto.getCardSubType());
		assertNotNull(purchaseRequestDto.getCardToken());
		assertNotNull(purchaseRequestDto.getCardExpiryDate());
		assertNotNull(purchaseRequestDto.getMsisdn());
		assertNotNull(purchaseRequestDto.getAuthorizationCode());
		assertNotNull(purchaseRequestDto.getPaymentType());
		assertNotNull(purchaseRequestDto.getOfferId());
		assertNotNull(purchaseRequestDto.getSubOfferId());
		assertNotNull(purchaseRequestDto.getCouponQuantity());
		assertNotNull(purchaseRequestDto.getVoucherDenomination());
		assertNotNull(purchaseRequestDto.getAccountNumber());
		assertNotNull(purchaseRequestDto.getMembershipCode());
		assertNotNull(purchaseRequestDto.getAtgUserName());
		assertNotNull(purchaseRequestDto.getLevel());
		assertNotNull(purchaseRequestDto.getPreferredNumber());
		assertNotNull(purchaseRequestDto.getExtTransactionId());
		assertNotNull(purchaseRequestDto.getEpgTransactionId());
		assertNotNull(purchaseRequestDto.getPartialTransactionId());
		assertNotNull(purchaseRequestDto.getPromoCode());
		assertNotNull(purchaseRequestDto.getUiLanguage());
		assertNotNull(purchaseRequestDto.getOfferTitle());
		assertNotNull(purchaseRequestDto.getOrderId());
		assertNotNull(purchaseRequestDto.getSubscriptionCatalogId());
		assertNotNull(purchaseRequestDto.getSubscriptionMethod());
		assertNotNull(purchaseRequestDto.getFreeSubscriptionDays());
		assertNotNull(purchaseRequestDto.getStartDate());
		assertNotNull(purchaseRequestDto.getAdditionalParams());
	}

	@Test
	public void testToString() {
		assertNotNull(purchaseRequestDto.toString());
	}

}
