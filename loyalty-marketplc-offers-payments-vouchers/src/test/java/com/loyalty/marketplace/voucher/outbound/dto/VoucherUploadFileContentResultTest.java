package com.loyalty.marketplace.voucher.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = VoucherUploadFileContentResult.class)
@ActiveProfiles("unittest")
public class VoucherUploadFileContentResultTest {

	private VoucherUploadFileContentResult voucherUploadFileContentResult;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		voucherUploadFileContentResult = new VoucherUploadFileContentResult("", "", "", "", "", new Date(), "", "", "", "",
				new Date(), "");
		voucherUploadFileContentResult.setFileContent("");
		voucherUploadFileContentResult.setFileName("");
		voucherUploadFileContentResult.setFileProcessingStatus("");
		voucherUploadFileContentResult.setFileType("");
		voucherUploadFileContentResult.setHandbackFile("");
		voucherUploadFileContentResult.setId("");
		voucherUploadFileContentResult.setMerchantCode("");
		voucherUploadFileContentResult.setProgramCode("");
		voucherUploadFileContentResult.setUpdatedDate(new Date());
		voucherUploadFileContentResult.setUpdatedUser("");
		voucherUploadFileContentResult.setUploadedDate(new Date());
	}

	@Test
	public void testGetters() {
		assertNotNull(voucherUploadFileContentResult.getFileContent());
		assertNotNull(voucherUploadFileContentResult.getFileName());
		assertNotNull(voucherUploadFileContentResult.getFileProcessingStatus());
		assertNotNull(voucherUploadFileContentResult.getFileType());
		assertNotNull(voucherUploadFileContentResult.getHandbackFile());
		assertNotNull(voucherUploadFileContentResult.getId());
		assertNotNull(voucherUploadFileContentResult.getMerchantCode());
		assertNotNull(voucherUploadFileContentResult.getProgramCode());
		assertNotNull(voucherUploadFileContentResult.getUpdatedDate());
		assertNotNull(voucherUploadFileContentResult.getUpdatedUser());
		assertNotNull(voucherUploadFileContentResult.getUploadedDate());
	}

	@Test
	public void testToString() {
		assertNotNull(voucherUploadFileContentResult.toString());
	}

}