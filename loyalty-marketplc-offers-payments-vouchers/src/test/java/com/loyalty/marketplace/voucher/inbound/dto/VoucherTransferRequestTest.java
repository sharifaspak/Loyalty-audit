package com.loyalty.marketplace.voucher.inbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = VoucherTransferRequest.class)
@ActiveProfiles("unittest")
public class VoucherTransferRequestTest {

	private VoucherTransferRequest voucherTransferRequest;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		voucherTransferRequest = new VoucherTransferRequest();
		voucherTransferRequest.setAgentName("");
		voucherTransferRequest.setCustomerType("");
		voucherTransferRequest.setEmail("");
		voucherTransferRequest.setGift(false);
		voucherTransferRequest.setIsExternalUser("");
		voucherTransferRequest.setTargetAccountNumber("");
		voucherTransferRequest.setVoucherCode("");
	}

	@Test
	public void testGetters() {
		assertNotNull(voucherTransferRequest.getAgentName());
		assertNotNull(voucherTransferRequest.getCustomerType());
		assertNotNull(voucherTransferRequest.getEmail());
		assertNotNull(voucherTransferRequest.getIsExternalUser());
		assertNotNull(voucherTransferRequest.getTargetAccountNumber());
		assertNotNull(voucherTransferRequest.getVoucherCode());
		assertNotNull(voucherTransferRequest.isGift());
	}

	@Test
	public void testToString() {
		assertNotNull(voucherTransferRequest.toString());
	}

}