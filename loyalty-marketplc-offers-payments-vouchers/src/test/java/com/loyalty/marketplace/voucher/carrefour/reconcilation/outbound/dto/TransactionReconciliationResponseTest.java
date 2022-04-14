package com.loyalty.marketplace.voucher.carrefour.reconcilation.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.voucher.carrefour.inbound.dto.AckMessage;
import com.loyalty.marketplace.voucher.carrefour.reconcilation.inbound.dto.ResponseData;

@SpringBootTest(classes = TransactionReconciliationResponse.class)
@ActiveProfiles("unittest")
public class TransactionReconciliationResponseTest {
	private TransactionReconciliationResponse transactionReconciliationResponse;

	@Before
	public void setUp() throws Exception {
		transactionReconciliationResponse = new TransactionReconciliationResponse();
		transactionReconciliationResponse.setAckMessage(new AckMessage());
		transactionReconciliationResponse.setResponseData(new ResponseData());
	}

	@Test
	public void testGetters() {
		assertNotNull(transactionReconciliationResponse.getAckMessage());
		assertNotNull(transactionReconciliationResponse.getResponseData());
	}

	@Test
	public void testToString() {
		assertNotNull(transactionReconciliationResponse.toString());
	}

}
