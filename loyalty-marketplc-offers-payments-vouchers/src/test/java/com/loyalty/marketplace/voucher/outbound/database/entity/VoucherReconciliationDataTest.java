package com.loyalty.marketplace.voucher.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = VoucherReconciliationData.class)
@ActiveProfiles("unittest")
public class VoucherReconciliationDataTest {

	private VoucherReconciliationData voucherReconciliationData;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		voucherReconciliationData = new VoucherReconciliationData();
		voucherReconciliationData.setCreatedDate(new Date());
		voucherReconciliationData.setCreatedUser("");
		voucherReconciliationData.setId("");
		voucherReconciliationData.setLoyaltyReconciliationData(new ArrayList<ReconciliationDataInfo>());
		voucherReconciliationData.setPartnerContent(new ArrayList<>());
		voucherReconciliationData.setPartnerReconciliationData(new ArrayList<ReconciliationDataInfo>());
		voucherReconciliationData.setProgramCode("");
		voucherReconciliationData.setReconciliationLevel("");
		voucherReconciliationData.setUpdatedDate(new Date());
		voucherReconciliationData.setUpdatedUser("");

	}

	@Test
	public void testGetters() {
		assertNotNull(voucherReconciliationData.getCreatedDate());
		assertNotNull(voucherReconciliationData.getCreatedUser());
		assertNotNull(voucherReconciliationData.getId());
		assertNotNull(voucherReconciliationData.getLoyaltyReconciliationData());
		assertNotNull(voucherReconciliationData.getPartnerContent());
		assertNotNull(voucherReconciliationData.getPartnerReconciliationData());
		assertNotNull(voucherReconciliationData.getProgramCode());
		assertNotNull(voucherReconciliationData.getReconciliationLevel());
		assertNotNull(voucherReconciliationData.getUpdatedDate());
		assertNotNull(voucherReconciliationData.getUpdatedUser());

	}

	@Test
	public void testToString() {
		assertNotNull(voucherReconciliationData.toString());
	}

}