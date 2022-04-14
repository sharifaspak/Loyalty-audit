package com.loyalty.marketplace.voucher.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = VoucherReconciliation.class)
@ActiveProfiles("unittest")
public class VoucherReconciliationTest {

	private VoucherReconciliation voucherReconciliation;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		voucherReconciliation = new VoucherReconciliation();
		voucherReconciliation.setCreatedDate(new Date());
		voucherReconciliation.setCreatedUser("");
		voucherReconciliation.setEndDateTime(new Date());
		voucherReconciliation.setId("");
		voucherReconciliation.setLoyalty(new ReconciliationInfo());
		voucherReconciliation.setPartner(new ReconciliationInfo());
		voucherReconciliation.setPartnerCode("");
		voucherReconciliation.setProgramCode("");
		voucherReconciliation.setStartDateTime(new Date());
		voucherReconciliation.setUpdatedDate(new Date());
		voucherReconciliation.setUpdatedUser("");

	}

	@Test
	public void testGetters() {
		assertNotNull(voucherReconciliation.getCreatedDate());
		assertNotNull(voucherReconciliation.getCreatedUser());
		assertNotNull(voucherReconciliation.getEndDateTime());
		assertNotNull(voucherReconciliation.getId());
		assertNotNull(voucherReconciliation.getLoyalty());
		assertNotNull(voucherReconciliation.getPartner());
		assertNotNull(voucherReconciliation.getPartnerCode());
		assertNotNull(voucherReconciliation.getStartDateTime());
		assertNotNull(voucherReconciliation.getUpdatedDate());
		assertNotNull(voucherReconciliation.getUpdatedUser());
	}

	@Test
	public void testToString() {
		assertNotNull(voucherReconciliation.toString());
	}

}