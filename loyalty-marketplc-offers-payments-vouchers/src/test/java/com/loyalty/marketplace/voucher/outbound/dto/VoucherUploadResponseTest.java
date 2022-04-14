package com.loyalty.marketplace.voucher.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = VoucherUploadResponse.class)
@ActiveProfiles("unittest")
public class VoucherUploadResponseTest {

	private VoucherUploadResponse voucherUploadResponse;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		voucherUploadResponse = new VoucherUploadResponse("externalTransactionId");
	}

	@Test
	public void testToString() {
		assertNotNull(voucherUploadResponse.toString());
	}

}