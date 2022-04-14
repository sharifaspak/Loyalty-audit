package com.loyalty.marketplace.voucher.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = VoucherUploadFile.class)
@ActiveProfiles("unittest")
public class VoucherUploadFileTest {

	private VoucherUploadFile voucherUploadFile;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		voucherUploadFile = new VoucherUploadFile();
		voucherUploadFile.setFileContent("");
		voucherUploadFile.setFileName("");
		voucherUploadFile.setFileProcessingStatus("");
		voucherUploadFile.setFileType("");
		voucherUploadFile.setHandbackFile("");
		voucherUploadFile.setId("");
		voucherUploadFile.setMerchantCode("");
		voucherUploadFile.setProgramCode("");
		voucherUploadFile.setUpdatedDate(new Date());
		voucherUploadFile.setUpdatedUser("");
		voucherUploadFile.setUploadedDate(new Date());
	}

	@Test
	public void testGetters() {
		assertNotNull(voucherUploadFile.getFileContent());
		assertNotNull(voucherUploadFile.getFileName());
		assertNotNull(voucherUploadFile.getFileProcessingStatus());
		assertNotNull(voucherUploadFile.getFileType());
		assertNotNull(voucherUploadFile.getHandbackFile());
		assertNotNull(voucherUploadFile.getId());
		assertNotNull(voucherUploadFile.getMerchantCode());
		assertNotNull(voucherUploadFile.getProgramCode());
		assertNotNull(voucherUploadFile.getUpdatedDate());
		assertNotNull(voucherUploadFile.getUpdatedUser());
		assertNotNull(voucherUploadFile.getUploadedDate());
	}

	@Test
	public void testToString() {
		assertNotNull(voucherUploadFile.toString());
	}

}