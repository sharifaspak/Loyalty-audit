package com.loyalty.marketplace.voucher.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = VoucherListResult.class)
@ActiveProfiles("unittest")
public class VoucherListResultTest {

	private VoucherListResult voucherListResult;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		voucherListResult = new VoucherListResult();
		voucherListResult.setAccountNumber("");
		voucherListResult.setAgentName("");
		voucherListResult.setBarcodeType("");
		voucherListResult.setBlackListed(false);
		voucherListResult.setBlacklistedDate(new Date());
		voucherListResult.setBlackListedUser("");
		voucherListResult.setBulkId("");
		voucherListResult.setBurnNotes("");
		voucherListResult.setCost(0.0);
		voucherListResult.setCreatedDate(new Date());
		voucherListResult.setCreatedUser("");
		voucherListResult.setDenomination("");
		voucherListResult.setDownloadedDate(new Date());
		voucherListResult.setEndDate(new Date());
		voucherListResult.setExpiryDate(new Date());
		voucherListResult.setId("");
		voucherListResult.setMembershipCode("");
		voucherListResult.setMerchantCode("");
		voucherListResult.setMerchantInvoiceId("");
		voucherListResult.setMerchantName("");
		voucherListResult.setOffer("");
		voucherListResult.setOldAccountNumber("");
		voucherListResult.setPartnerCode("");
		voucherListResult.setPartnerReferNumber("");
		voucherListResult.setPointsValue(0l);
		voucherListResult.setStartDate(new Date());
		voucherListResult.setStatus("");
		voucherListResult.setStoreCode("");
		voucherListResult.setSubOffer("");
		voucherListResult.setTransfer(false);
		voucherListResult.setUpdatedDate(new Date());
		voucherListResult.setUpdatedUser("");
		voucherListResult.setUuid("");
		voucherListResult.setVoucherAmount(0.0);
		voucherListResult.setVoucherBurnt(false);
		voucherListResult.setVoucherBurntDate(new Date());
		voucherListResult.setVoucherBurntId("");
		voucherListResult.setVoucherBurntUser("");
		voucherListResult.setVoucherCode("");
		voucherListResult.setVoucherDescriptionAr("");
		voucherListResult.setVoucherDescriptionEn("");
		voucherListResult.setVoucherNameAr("");
		voucherListResult.setVoucherNameEn("");
		voucherListResult.setVoucherType("");
	}

	@Test
	public void testGetters() {
		assertNotNull(voucherListResult.getAccountNumber());
		assertNotNull(voucherListResult.getAgentName());
		assertNotNull(voucherListResult.getBarcodeType());
		assertNotNull(voucherListResult.isBlackListed());
		assertNotNull(voucherListResult.getBlacklistedDate());
		assertNotNull(voucherListResult.getBlackListedUser());
		assertNotNull(voucherListResult.getBulkId());
		assertNotNull(voucherListResult.getBurnNotes());
		assertNotNull(voucherListResult.getCost());
		assertNotNull(voucherListResult.getCreatedDate());
		assertNotNull(voucherListResult.getCreatedUser());
		assertNotNull(voucherListResult.getDenomination());
		assertNotNull(voucherListResult.getDownloadedDate());
		assertNotNull(voucherListResult.getEndDate());
		assertNotNull(voucherListResult.getExpiryDate());
		assertNotNull(voucherListResult.getId());
		assertNotNull(voucherListResult.getMembershipCode());
		assertNotNull(voucherListResult.getMerchantCode());
		assertNotNull(voucherListResult.getMerchantInvoiceId());
		assertNotNull(voucherListResult.getMerchantName());
		assertNotNull(voucherListResult.getOffer());
		assertNotNull(voucherListResult.getOldAccountNumber());
		assertNotNull(voucherListResult.getPartnerCode());
		assertNotNull(voucherListResult.getPartnerReferNumber());
		assertNotNull(voucherListResult.getPointsValue());
		assertNotNull(voucherListResult.getStartDate());
		assertNotNull(voucherListResult.getStatus());
		assertNotNull(voucherListResult.getStoreCode());
		assertNotNull(voucherListResult.getSubOffer());
		assertNotNull(voucherListResult.isTransfer());
		assertNotNull(voucherListResult.getUpdatedDate());
		assertNotNull(voucherListResult.getUpdatedUser());
		assertNotNull(voucherListResult.getUuid());
		assertNotNull(voucherListResult.getVoucherAmount());
		assertNotNull(voucherListResult.isVoucherBurnt());
		assertNotNull(voucherListResult.getVoucherBurntDate());
		assertNotNull(voucherListResult.getVoucherBurntId());
		assertNotNull(voucherListResult.getVoucherBurntUser());
		assertNotNull(voucherListResult.getVoucherCode());
		assertNotNull(voucherListResult.getVoucherDescriptionAr());
		assertNotNull(voucherListResult.getVoucherDescriptionEn());
		assertNotNull(voucherListResult.getVoucherNameAr());
		assertNotNull(voucherListResult.getVoucherNameEn());
		assertNotNull(voucherListResult.getVoucherType());
	}

	@Test
	public void testToString() {
		assertNotNull(voucherListResult.toString());
	}

}