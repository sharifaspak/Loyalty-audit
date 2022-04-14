package com.loyalty.marketplace.voucher.carrefour.reconcilation.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CarrefourTransactionReconcilationResponse.class)
@ActiveProfiles("unittest")
public class CarrefourTransactionReconcilationResponseTest {
	private CarrefourTransactionReconcilationResponse carrefourTransactionReconcilationResponse;

	@Before
	public void setUp() throws Exception {
		carrefourTransactionReconcilationResponse = new CarrefourTransactionReconcilationResponse();
		carrefourTransactionReconcilationResponse
				.setTransactionReconciliationResponse(new TransactionReconciliationResponse());
	}

	@Test
	public void test() {
		assertNotNull(carrefourTransactionReconcilationResponse.getTransactionReconciliationResponse());
	}

	@Test
	public void testToString() {
		assertNotNull(carrefourTransactionReconcilationResponse.toString());
	}

}
