package com.loyalty.marketplace.voucher.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = VoucherResult.class)
@ActiveProfiles("unittest")
public class VoucherResultTest {

	private VoucherResult voucherResult;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		voucherResult = new VoucherResult();
		voucherResult.setStatus("");
		voucherResult.setVoucherCode("");
		voucherResult.setVoucherId("");
	}

	@Test
	public void testGetters() {
		assertNotNull(voucherResult.getStatus());
		assertNotNull(voucherResult.getVoucherCode());
		assertNotNull(voucherResult.getVoucherId());
	}

	@Test
	public void testToString() {
		assertNotNull(voucherResult.toString());
	}

}