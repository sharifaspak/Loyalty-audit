package com.loyalty.marketplace.voucher.carrefour.inbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = AckMessage.class)
@ActiveProfiles("unittest")
public class AckMessageTest {

	private AckMessage ackMessage;

	@Before
	public void setUp() throws Exception {
		ackMessage = new AckMessage();
		ackMessage.setErrorCode("");
		ackMessage.setErrorDescription("");
		ackMessage.setErrorType("");
		ackMessage.setStatus("");
	}

	@Test
	public void testGetters() {
		assertNotNull(ackMessage.getErrorCode());
		assertNotNull(ackMessage.getErrorDescription());
		assertNotNull(ackMessage.getErrorType());
		assertNotNull(ackMessage.getStatus());
	}

	@Test
	public void testToString() {
		assertNotNull(ackMessage.toString());
	}

}
