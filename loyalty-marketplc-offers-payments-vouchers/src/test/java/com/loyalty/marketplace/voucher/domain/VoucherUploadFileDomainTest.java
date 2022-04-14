package com.loyalty.marketplace.voucher.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.voucher.domain.VoucherUploadFileDomain.VoucherUploadFileBuilder;

@SpringBootTest(classes = VoucherUploadFileDomain.class)
@ActiveProfiles("unittest")
public class VoucherUploadFileDomainTest {

	private VoucherUploadFileDomain voucherUploadFileDomain;

	@Before
	public void setUp() throws Exception {

		String programCode = "";
		String fileName = "";
		String merchantCode = "";
		String offerId = "";
		Date uploadedDate = new Date();
		String fileType = "";
		String fileContent = "";
		String fileProcessingStatus = "";
		String handbackFile = "";
		Date updatedDate = new Date();
		String updatedUser = "";
		VoucherUploadFileBuilder voucherBuilder = new VoucherUploadFileDomain.VoucherUploadFileBuilder(programCode,
				fileName, merchantCode, offerId, uploadedDate, fileType, fileContent, fileProcessingStatus, handbackFile,
				updatedDate, updatedUser);
		voucherBuilder.fileContent(fileContent).fileName(fileName).fileProcessingStatus(fileProcessingStatus)
				.fileType(fileType).handbackFile(handbackFile).merchantCode(merchantCode).programCode(programCode)
				.updatedDate(updatedDate).updatedUser(updatedUser).uploadedDate(uploadedDate).build();
		voucherUploadFileDomain = new VoucherUploadFileDomain(voucherBuilder);
	}

	@Test
	public void testGetters() {
		assertNotNull(voucherUploadFileDomain.getFileContent());
		assertNotNull(voucherUploadFileDomain.getFileName());
		assertNotNull(voucherUploadFileDomain.getFileProcessingStatus());
		assertNotNull(voucherUploadFileDomain.getFileType());
		assertNotNull(voucherUploadFileDomain.getHandbackFile());
		assertNull(voucherUploadFileDomain.getId());
		assertNotNull(voucherUploadFileDomain.getMerchantCode());
		assertNotNull(voucherUploadFileDomain.getOfferId());
		assertNotNull(voucherUploadFileDomain.getProgramCode());
		assertNotNull(voucherUploadFileDomain.getUpdatedDate());
		assertNotNull(voucherUploadFileDomain.getUpdatedUser());
		assertNotNull(voucherUploadFileDomain.getUploadedDate());

	}

	@Test
	public void testBuilder() {
		voucherUploadFileDomain = new VoucherUploadFileDomain();
		assertNotNull(voucherUploadFileDomain);
	}

	@Test
	public void testToString() {
		assertNotNull(voucherUploadFileDomain.toString());
	}

}
