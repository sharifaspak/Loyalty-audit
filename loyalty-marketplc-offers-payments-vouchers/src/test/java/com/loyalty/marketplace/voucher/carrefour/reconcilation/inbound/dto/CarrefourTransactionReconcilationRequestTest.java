package com.loyalty.marketplace.voucher.carrefour.reconcilation.inbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CarrefourTransactionReconcilationRequest.class)
@ActiveProfiles("unittest")
public class CarrefourTransactionReconcilationRequestTest {

	CarrefourTransactionReconcilationRequest carrefourTransactionReconcilationRequest;

	@Before
	public void setUp() throws Exception {
		carrefourTransactionReconcilationRequest = new CarrefourTransactionReconcilationRequest();
		carrefourTransactionReconcilationRequest
				.setTransactionReconciliationRequest(new TransactionReconciliationRequest());
	}

	@Test
	public void test() {
		assertNotNull(carrefourTransactionReconcilationRequest.getTransactionReconciliationRequest());
	}

	@Test
	public void testToString() {
		assertNotNull(carrefourTransactionReconcilationRequest.toString());
	}

}
