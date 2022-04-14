package com.loyalty.marketplace.voucher.carrefour.reconcilation.inbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ResponseData.class)
@ActiveProfiles("unittest")
public class ResponseDataTest {

	private ResponseData responseData;

	@Before
	public void setUp() throws Exception {
		responseData = new ResponseData();
		responseData.setIsSuccessful("");
		responseData.setTransactionID("");
	}

	@Test
	public void test() {
		assertNotNull(responseData.getIsSuccessful());
		assertNotNull(responseData.getTransactionID());
	}

	@Test
	public void testToString() {
		assertNotNull(responseData.toString());
	}

}
