package com.loyalty.marketplace.voucher.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = VoucherContent.class)
@ActiveProfiles("unittest")
public class VoucherContentTest {

	private VoucherContent voucherContent;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		voucherContent = new VoucherContent();
		voucherContent.setContent("");
		voucherContent.setFileName("");
		voucherContent.setFormatType("");
	}

	@Test
	public void testGetters() {
		assertNotNull(voucherContent.getContent());
		assertNotNull(voucherContent.getFileName());
		assertNotNull(voucherContent.getFormatType());
	}

	@Test
	public void testToString() {
		assertNotNull(voucherContent.toString());
	}

}