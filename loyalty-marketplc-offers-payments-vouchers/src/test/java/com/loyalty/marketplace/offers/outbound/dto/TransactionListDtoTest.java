package com.loyalty.marketplace.offers.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.loyalty.marketplace.offers.outbound.database.entity.AdditionalDetails;
import com.loyalty.marketplace.offers.outbound.database.entity.MarketplaceActivity;

@SpringBootTest(classes = TransactionListDto.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class TransactionListDtoTest {

	private TransactionListDto transactionListDto;

	@Before
	public void setUp() {
		transactionListDto = new TransactionListDto();
		transactionListDto.setAccountNumber("");
		transactionListDto.setAdditionalDetails(new AdditionalDetails());
		transactionListDto.setChannelId("");
		transactionListDto.setCost(0.0);
		transactionListDto.setCouponQuantity(0);
		transactionListDto.setCreatedDate(new Date());
		transactionListDto.setCreatedUser("");
		transactionListDto.setEpgTransactionId("");
		transactionListDto.setExtRefNo("");
		transactionListDto.setId("");
		transactionListDto.setLanguage("");
		transactionListDto.setMembershipCode("");
		transactionListDto.setMerchantCode("");
		transactionListDto.setMerchantName("");
		transactionListDto.setOfferId("");
		transactionListDto.setOfferType("");
		transactionListDto.setPartnerActivity(new MarketplaceActivity());
		transactionListDto.setPartnerCode("");
		transactionListDto.setPaymentMethod("");
		transactionListDto.setPaymentMethodId("");
		transactionListDto.setProgramActivity("");
		transactionListDto.setPromoCode("");
		transactionListDto.setPurchaseItem("");
		transactionListDto.setProgramCode("");
		transactionListDto.setPurchaseAmount(0.0);
		transactionListDto.setSpentAmount(0.0);
		transactionListDto.setSpentPoints(0);
		transactionListDto.setStatus("");
		transactionListDto.setStatusReason("");
		transactionListDto.setSubOfferId("");
		transactionListDto.setSubscriptionId("");
		transactionListDto.setTransactionNo("");
		transactionListDto.setTransactionType("");
		transactionListDto.setUpdatedDate(new Date());
		transactionListDto.setUpdatedUser("");
		transactionListDto.setVatAmount(0.0);
		transactionListDto.setVoucherCode(new ArrayList<>());
		transactionListDto.setVoucherDetails(new ArrayList<>());
	    
	}

	@Test
	public void testGetters() {

		assertNotNull(transactionListDto.getAccountNumber());
		assertNotNull(transactionListDto.getAdditionalDetails());
		assertNotNull(transactionListDto.getChannelId());
		assertNotNull(transactionListDto.getCost());
		assertNotNull(transactionListDto.getCouponQuantity());
		assertNotNull(transactionListDto.getCreatedDate());
		assertNotNull(transactionListDto.getCreatedUser());
		assertNotNull(transactionListDto.getEpgTransactionId());
		assertNotNull(transactionListDto.getExtRefNo());
		assertNotNull(transactionListDto.getId());
		assertNotNull(transactionListDto.getLanguage());
		assertNotNull(transactionListDto.getMembershipCode());
		assertNotNull(transactionListDto.getMerchantCode());
		assertNotNull(transactionListDto.getMerchantName());
		assertNotNull(transactionListDto.getOfferId());
		assertNotNull(transactionListDto.getOfferType());
		assertNotNull(transactionListDto.getPartnerActivity());
		assertNotNull(transactionListDto.getPartnerCode());
		assertNotNull(transactionListDto.getPaymentMethod());
		assertNotNull(transactionListDto.getPaymentMethodId());
		assertNotNull(transactionListDto.getProgramActivity());
		assertNotNull(transactionListDto.getProgramCode());
		assertNotNull(transactionListDto.getPurchaseAmount());
		assertNotNull(transactionListDto.getPromoCode());
		assertNotNull(transactionListDto.getPurchaseItem());
		assertNotNull(transactionListDto.getSpentAmount());
		assertNotNull(transactionListDto.getSpentPoints());
		assertNotNull(transactionListDto.getStatus());
		assertNotNull(transactionListDto.getStatusReason());
		assertNotNull(transactionListDto.getSubOfferId());
		assertNotNull(transactionListDto.getSubscriptionId());
		assertNotNull(transactionListDto.getTransactionNo());
		assertNotNull(transactionListDto.getTransactionType());
		assertNotNull(transactionListDto.getUpdatedDate());
		assertNotNull(transactionListDto.getUpdatedUser());
		assertNotNull(transactionListDto.getVatAmount());
		assertNotNull(transactionListDto.getVoucherCode());
		assertNotNull(transactionListDto.getVoucherDetails());
	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {

		assertNotNull(transactionListDto.toString());

	}
}
