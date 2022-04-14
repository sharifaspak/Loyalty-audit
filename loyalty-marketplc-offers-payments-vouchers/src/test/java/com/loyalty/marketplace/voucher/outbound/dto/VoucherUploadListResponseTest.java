package com.loyalty.marketplace.voucher.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = VoucherUploadListResponse.class)
@ActiveProfiles("unittest")
public class VoucherUploadListResponseTest {

	private VoucherUploadListResponse voucherUploadListResponse;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		voucherUploadListResponse = new VoucherUploadListResponse("externalTransactionId");
		voucherUploadListResponse.setVoucherUploadListResult(new ArrayList<VoucherUploadList>());
	}

	@Test
	public void testGetters() {
		assertNotNull(voucherUploadListResponse.getVoucherUploadListResult());
	}

	@Test
	public void testToString() {
		assertNotNull(voucherUploadListResponse.toString());
	}

}