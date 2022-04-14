package com.loyalty.marketplace.voucher.carrefour.reconcilation.inbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = TransactionReconciliationRequest.class)
@ActiveProfiles("unittest")
public class TransactionReconciliationRequestTest {

	private TransactionReconciliationRequest transactionReconciliationRequest;

	@Before
	public void setUp() throws Exception {
		transactionReconciliationRequest = new TransactionReconciliationRequest();
		transactionReconciliationRequest.setApplicationHeader(new ApplicationHeader());
		transactionReconciliationRequest.setDataHeader(new DataHeader());
	}

	@Test
	public void test() {
		assertNotNull(transactionReconciliationRequest.getApplicationHeader());
		assertNotNull(transactionReconciliationRequest.getDataHeader());
	}

	@Test
	public void testToString() {
		assertNotNull(transactionReconciliationRequest.toString());
	}
}
