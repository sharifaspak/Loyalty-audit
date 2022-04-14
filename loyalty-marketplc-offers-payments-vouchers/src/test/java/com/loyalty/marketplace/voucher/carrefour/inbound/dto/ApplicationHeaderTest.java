package com.loyalty.marketplace.voucher.carrefour.inbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ApplicationHeader.class)
@ActiveProfiles("unittest")
public class ApplicationHeaderTest {

	private ApplicationHeader applicationHeader;

	@Before
	public void setUp() throws Exception {
		applicationHeader = new ApplicationHeader();
		applicationHeader.setRequestedDate("");
		applicationHeader.setRequestedSystem("");
		applicationHeader.setRetryLimit("");
		applicationHeader.setTransactionID("");
	}

	@Test
	public void testGetters() {
		assertNotNull(applicationHeader.getRequestedDate());
		assertNotNull(applicationHeader.getRequestedSystem());
		assertNotNull(applicationHeader.getRetryLimit());
		assertNotNull(applicationHeader.getTransactionID());
	}

	@Test
	public void testToString() {
		assertNotNull(applicationHeader.toString());
	}

}
