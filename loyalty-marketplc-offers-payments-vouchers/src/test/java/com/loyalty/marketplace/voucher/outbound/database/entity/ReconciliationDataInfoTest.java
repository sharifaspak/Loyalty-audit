package com.loyalty.marketplace.voucher.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ReconciliationDataInfo.class)
@ActiveProfiles("unittest")
public class ReconciliationDataInfoTest {

	private ReconciliationDataInfo reconciliationDataInfo;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		reconciliationDataInfo = new ReconciliationDataInfo("", "", "", "", "", "", 0.0, new Date());
		reconciliationDataInfo.setLoyaltyTransactionId("");
		reconciliationDataInfo.setOrderReferenceNumber("");
		reconciliationDataInfo.setPartnerCode("");
		reconciliationDataInfo.setPartnerReferenceNumber("");
		reconciliationDataInfo.setPartnerTransactionId("");
		reconciliationDataInfo.setTransactionDate(new Date());
		reconciliationDataInfo.setVoucherAmount(0.0);
		reconciliationDataInfo.setVoucherCode("");
	}

	@Test
	public void testGetters() {
		assertNotNull(reconciliationDataInfo.getLoyaltyTransactionId());
		assertNotNull(reconciliationDataInfo.getOrderReferenceNumber());
		assertNotNull(reconciliationDataInfo.getPartnerCode());
		assertNotNull(reconciliationDataInfo.getPartnerReferenceNumber());
		assertNotNull(reconciliationDataInfo.getPartnerTransactionId());
		assertNotNull(reconciliationDataInfo.getVoucherAmount());
		assertNotNull(reconciliationDataInfo.getTransactionDate());
		assertNotNull(reconciliationDataInfo.getVoucherCode());
	}

	@Test
	public void testToString() {
		assertNotNull(reconciliationDataInfo.toString());
	}

}