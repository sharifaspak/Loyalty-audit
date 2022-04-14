package com.loyalty.marketplace.voucher.carrefour.reconcilation.inbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = DataHeader.class)
@ActiveProfiles("unittest")
public class DataHeaderTest {

	private DataHeader dataHeader;

	@Before
	public void setUp() throws Exception {
		dataHeader = new DataHeader();
		dataHeader.setMerchantID("");
		dataHeader.setPassphrase("");
		dataHeader.setBusinessDate("");
		dataHeader.setReconciliationRecords(new ReconciliationRecords());
	}

	@Test
	public void test() {
		assertNotNull(dataHeader.getBusinessDate());
		assertNotNull(dataHeader.getMerchantID());
		assertNotNull(dataHeader.getPassphrase());
		assertNotNull(dataHeader.getReconciliationRecords());
	}

	@Test
	public void testToString() {
		assertNotNull(dataHeader.toString());
	}

}
