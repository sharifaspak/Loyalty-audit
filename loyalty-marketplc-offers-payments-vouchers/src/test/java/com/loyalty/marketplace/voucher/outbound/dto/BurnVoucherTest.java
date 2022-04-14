package com.loyalty.marketplace.voucher.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = BurnVoucher.class)
@ActiveProfiles("unittest")
public class BurnVoucherTest {

	private BurnVoucher burnVoucher;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		burnVoucher = new BurnVoucher();
		burnVoucher.setTransactionRefId("");
		burnVoucher.setVoucherCode("");
	}

	@Test
	public void testGetters() {
		assertNotNull(burnVoucher.getTransactionRefId());
		assertNotNull(burnVoucher.getVoucherCode());
	}

	@Test
	public void testToString() {
		assertNotNull(burnVoucher.toString());
	}

}