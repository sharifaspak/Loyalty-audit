package com.loyalty.marketplace.voucher.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = VoucherUploadList.class)
@ActiveProfiles("unittest")
public class VoucherUploadListTest {

	private VoucherUploadList voucherUploadList = new VoucherUploadList();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		voucherUploadList = new VoucherUploadList("", "", new Date(), "");
		voucherUploadList.setFileName("");
		voucherUploadList.setHandbackFile("");
		voucherUploadList.setUploadedDate(new Date());
	}

	@Test
	public void testGetters() {
		assertNotNull(voucherUploadList.getFileName());
		assertNotNull(voucherUploadList.getHandbackFile());
		assertNotNull(voucherUploadList.getUploadedDate());
	}

	@Test
	public void testToString() {
		assertNotNull(voucherUploadList.toString());
	}

}