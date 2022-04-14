package com.loyalty.marketplace.offers.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = PurchaseHistory.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class PurchaseHistoryTest {

	private PurchaseHistory offerPurchaseHistory;

	@Before
	public void setUp() {

		offerPurchaseHistory = new PurchaseHistory();
		offerPurchaseHistory.setId("");
		offerPurchaseHistory.setProgramCode("");
		offerPurchaseHistory.setPartnerCode("");
		offerPurchaseHistory.setMerchantCode("");
		offerPurchaseHistory.setMerchantName("");
		offerPurchaseHistory.setMembershipCode("");
		offerPurchaseHistory.setAccountNumber("");
		offerPurchaseHistory.setOfferId("");
		offerPurchaseHistory.setSubOfferId("");
		offerPurchaseHistory.setPromoCode("");
		offerPurchaseHistory.setExtRefNo("");
		offerPurchaseHistory.setEpgTransactionId("");
		offerPurchaseHistory.setCouponQuantity(1);
		offerPurchaseHistory.setVoucherCode(new ArrayList<String>());
		offerPurchaseHistory.setSubscriptionId("");
		offerPurchaseHistory.setPaymentMethod("");
		offerPurchaseHistory.setSpentPoints(23);
		offerPurchaseHistory.setPartnerActivity(new MarketplaceActivity());
		offerPurchaseHistory.setPurchaseAmount(0.0);
		offerPurchaseHistory.setStatus("");
		offerPurchaseHistory.setStatusReason("");
		offerPurchaseHistory.setCreatedDate(new Date());
		offerPurchaseHistory.setCreatedUser("");
		offerPurchaseHistory.setUpdatedDate(new Date());
		offerPurchaseHistory.setUpdatedUser("");
		offerPurchaseHistory.setChannelId("");
		offerPurchaseHistory.setLanguage("");
		offerPurchaseHistory.setOfferType("");
		offerPurchaseHistory.setPurchaseAmount(1.0);
		offerPurchaseHistory.setSpentAmount(1.0);
		offerPurchaseHistory.setTransactionNo("");
		offerPurchaseHistory.setTransactionType("");
		offerPurchaseHistory.setPurchaseItem("");
		offerPurchaseHistory.setAdditionalDetails(new AdditionalDetails());
		offerPurchaseHistory.setCost(0.0);
		offerPurchaseHistory.setPointsTransactionId("");
		offerPurchaseHistory.setReferralAccountNumber("");
		offerPurchaseHistory.setReferralBonusCode("");
		offerPurchaseHistory.setReferralBonus(0.0);
		offerPurchaseHistory.setVatAmount(0.0);

	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {

		assertNotNull(offerPurchaseHistory.getProgramCode());
		assertNotNull(offerPurchaseHistory.getPartnerCode());
		assertNotNull(offerPurchaseHistory.getMerchantCode());
		assertNotNull(offerPurchaseHistory.getMerchantName());
		assertNotNull(offerPurchaseHistory.getMembershipCode());
		assertNotNull(offerPurchaseHistory.getAccountNumber());
		assertNotNull(offerPurchaseHistory.getOfferId());
		assertNotNull(offerPurchaseHistory.getSubOfferId());
		assertNotNull(offerPurchaseHistory.getPromoCode());
		assertNotNull(offerPurchaseHistory.getExtRefNo());
		assertNotNull(offerPurchaseHistory.getEpgTransactionId());
		assertNotNull(offerPurchaseHistory.getCouponQuantity());
		assertNotNull(offerPurchaseHistory.getVoucherCode());
		assertNotNull(offerPurchaseHistory.getSubscriptionId());
		assertNotNull(offerPurchaseHistory.getPaymentMethod());
		assertNotNull(offerPurchaseHistory.getSpentPoints());
		assertNotNull(offerPurchaseHistory.getPartnerActivity());
		assertNotNull(offerPurchaseHistory.getPurchaseAmount());
		assertNotNull(offerPurchaseHistory.getStatus());
		assertNotNull(offerPurchaseHistory.getStatusReason());
		assertNotNull(offerPurchaseHistory.getCreatedDate());
		assertNotNull(offerPurchaseHistory.getCreatedUser());
		assertNotNull(offerPurchaseHistory.getUpdatedDate());
		assertNotNull(offerPurchaseHistory.getUpdatedUser());
		assertNotNull(offerPurchaseHistory.getChannelId());
		assertNotNull(offerPurchaseHistory.getLanguage());
		assertNotNull(offerPurchaseHistory.getOfferType());
		assertNotNull(offerPurchaseHistory.getPurchaseAmount());
		assertNotNull(offerPurchaseHistory.getSpentAmount());
		assertNotNull(offerPurchaseHistory.getTransactionNo());
		assertNotNull(offerPurchaseHistory.getTransactionType());
		assertNotNull(offerPurchaseHistory.getPurchaseItem());
		assertNotNull(offerPurchaseHistory.getAdditionalDetails());
		assertNotNull(offerPurchaseHistory.getCost());
		assertNotNull(offerPurchaseHistory.getPointsTransactionId());
		assertNotNull(offerPurchaseHistory.getReferralAccountNumber());
		assertNotNull(offerPurchaseHistory.getReferralBonusCode());
		assertNotNull(offerPurchaseHistory.getReferralBonus());
		assertNotNull(offerPurchaseHistory.getVatAmount());

	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {

		assertNotNull(offerPurchaseHistory.toString());

	}

}
