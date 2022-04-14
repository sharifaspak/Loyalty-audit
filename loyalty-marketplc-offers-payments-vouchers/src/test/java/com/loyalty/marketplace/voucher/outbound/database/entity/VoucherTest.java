package com.loyalty.marketplace.voucher.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.offers.merchants.outbound.database.entity.Barcode;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchaseHistory;

@SpringBootTest(classes = Voucher.class)
@ActiveProfiles("unittest")
public class VoucherTest {

	private Voucher voucher;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		voucher = new Voucher();
		voucher.setAccountNumber("");
		voucher.setBarcodeType(new Barcode());
		voucher.setBlackListed(false);
		voucher.setBlacklistedDate(new Date());
		voucher.setBlackListedUser("");
		voucher.setBulkId("");
		voucher.setBurntInfo(new BurntInfo());
		voucher.setCreatedDate(new Date());
		voucher.setCreatedUser("");
		voucher.setDownloadedDate(new Date());
		voucher.setEndDate(new Date());
		voucher.setExpiryDate(new Date());
		voucher.setId("");
		
		voucher.setMembershipCode("");
		voucher.setMerchantCode("");
		voucher.setMerchantInvoiceId("");
		voucher.setMerchantName("");
		voucher.setOfferInfo(new OfferInfo());
		voucher.setPartnerCode("");
		voucher.setPartnerReferNumber("");
		voucher.setProgramCode("");
		voucher.setStartDate(new Date());
		voucher.setStatus("");
		voucher.setTransfer(new VoucherTransfer(false, "", ""));
		voucher.setType("");
		voucher.setUpdatedDate(new Date());
		voucher.setUpdatedUser("");
		voucher.setUuid(new PurchaseHistory());
		voucher.setVoucherAmount(0.0);
		voucher.setVoucherCode("");
		voucher.setVoucherType("");
		voucher.setVoucherValues(new VoucherValues());

	}

	@Test
	public void testGetters() {
		assertNotNull(voucher.getAccountNumber());
		assertNotNull(voucher.getBarcodeType());
		assertNotNull(voucher.isBlackListed());
		assertNotNull(voucher.getBlacklistedDate());
		assertNotNull(voucher.getBlackListedUser());
		assertNotNull(voucher.getBulkId());
		assertNotNull(voucher.getBurntInfo());
		assertNotNull(voucher.getCreatedDate());
		assertNotNull(voucher.getCreatedUser());
		assertNotNull(voucher.getDownloadedDate());
		assertNotNull(voucher.getEndDate());
		assertNotNull(voucher.getExpiryDate());
		assertNotNull(voucher.getId());
		
		assertNotNull(voucher.getMembershipCode());
		assertNotNull(voucher.getMerchantCode());
		assertNotNull(voucher.getMerchantInvoiceId());
		assertNotNull(voucher.getMerchantName());
		assertNotNull(voucher.getOfferInfo());
		assertNotNull(voucher.getPartnerCode());
		assertNotNull(voucher.getPartnerReferNumber());
		assertNotNull(voucher.getProgramCode());
		assertNotNull(voucher.getStartDate());
		assertNotNull(voucher.getStatus());
		assertNotNull(voucher.getTransfer());
		assertNotNull(voucher.getType());
		assertNotNull(voucher.getUpdatedDate());
		assertNotNull(voucher.getUpdatedUser());
		assertNotNull(voucher.getUuid());
		assertNotNull(voucher.getVoucherAmount());
		assertNotNull(voucher.getVoucherCode());
		assertNotNull(voucher.getVoucherType());
		assertNotNull(voucher.getVoucherValues());
	}

	@Test
	public void testToString() {
		assertNotNull(voucher.toString());
	}

}