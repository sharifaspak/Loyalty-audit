package com.loyalty.marketplace.voucher.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = VoucherUploadFileContentResponse.class)
@ActiveProfiles("unittest")
public class VoucherUploadFileContentResponseTest {

	private VoucherUploadFileContentResponse voucherUploadFileContentResponse;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		voucherUploadFileContentResponse = new VoucherUploadFileContentResponse("externalTransactionId");
		voucherUploadFileContentResponse.setVoucherUploadResult(
				new VoucherUploadFileContentResult(null, null, null, null, null, null, null, null, null, null, null,null));
	}

	@Test
	public void testGetters() {
		assertNotNull(voucherUploadFileContentResponse.getVoucherUploadResult());
	}

	@Test
	public void testToString() {
		assertNotNull(voucherUploadFileContentResponse.toString());
	}

}