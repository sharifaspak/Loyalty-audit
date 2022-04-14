package com.loyalty.marketplace.voucher.carrefour.reconcilation.inbound.dto;

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
		applicationHeader.setRequestedSystem("");
		applicationHeader.setTransactionID("");
	}

	@Test
	public void test() {
		assertNotNull(applicationHeader.getRequestedSystem());
		assertNotNull(applicationHeader.getTransactionID());
	}

	@Test
	public void testToString() {
		assertNotNull(applicationHeader.toString());
	}

}
