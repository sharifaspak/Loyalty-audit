package com.loyalty.marketplace.voucher.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = VoucherTransfer.class)
@ActiveProfiles("unittest")
public class VoucherTransferTest {

	private VoucherTransfer voucherTransfer;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		voucherTransfer = new VoucherTransfer(false, "", "");
		voucherTransfer.setAgentName("");
		voucherTransfer.setOldAccountNumber("");
		voucherTransfer.setTransfer(false);
	}

	@Test
	public void testGetters() {
		assertNotNull(voucherTransfer.getAgentName());
		assertNotNull(voucherTransfer.getOldAccountNumber());
		assertNotNull(voucherTransfer.isTransfer());
	}

	@Test
	public void testToString() {
		assertNotNull(voucherTransfer.toString());
	}

}