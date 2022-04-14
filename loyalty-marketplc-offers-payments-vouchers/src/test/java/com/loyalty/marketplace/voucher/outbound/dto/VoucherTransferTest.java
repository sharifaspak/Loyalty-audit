package com.loyalty.marketplace.voucher.outbound.dto;

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
		voucherTransfer = new VoucherTransfer("transactionRefId", "agentName", "memberShipCode");
		voucherTransfer.setAgentName("");
		voucherTransfer.setMemberShipCode("");
		voucherTransfer.setTransactionRefId("");
	}

	@Test
	public void testGetters() {
		assertNotNull(voucherTransfer.getAgentName());
		assertNotNull(voucherTransfer.getMemberShipCode());
		assertNotNull(voucherTransfer.getTransactionRefId());
	}

	@Test
	public void testToString() {
		assertNotNull(voucherTransfer.toString());
	}

}