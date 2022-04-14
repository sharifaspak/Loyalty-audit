package com.loyalty.marketplace.voucher.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = VoucherUploadDto.class)
@ActiveProfiles("unittest")
public class VoucherUploadDtoTest {

	private VoucherUploadDto voucherUploadDto;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		voucherUploadDto = new VoucherUploadDto();
		voucherUploadDto.setDenomination(0.0);
		voucherUploadDto.setEndDate(new Date());
		voucherUploadDto.setError("");
		voucherUploadDto.setExpiryDate(new Date());
		voucherUploadDto.setMerchantCode("");
		voucherUploadDto.setOfferId("");
		voucherUploadDto.setStartDate(new Date());
		voucherUploadDto.setStatus("");
		voucherUploadDto.setSubOfferId("");
		voucherUploadDto.setUploadDate(new Date());
		voucherUploadDto.setVoucherCode("");
		voucherUploadDto.setMerchantName("");
		voucherUploadDto.setPartnerCode("");
		voucherUploadDto.setOfferType("");
	}

	@Test
	public void testGetters() {
		assertNotNull(voucherUploadDto.getDenomination());
		assertNotNull(voucherUploadDto.getEndDate());
		assertNotNull(voucherUploadDto.getError());
		assertNotNull(voucherUploadDto.getExpiryDate());
		assertNotNull(voucherUploadDto.getMerchantCode());
		assertNotNull(voucherUploadDto.getOfferId());
		assertNotNull(voucherUploadDto.getStartDate());
		assertNotNull(voucherUploadDto.getStatus());
		assertNotNull(voucherUploadDto.getSubOfferId());
		assertNotNull(voucherUploadDto.getUploadDate());
		assertNotNull(voucherUploadDto.getVoucherCode());
		assertNotNull(voucherUploadDto.getMerchantName());
		assertNotNull(voucherUploadDto.getPartnerCode());
		assertNotNull(voucherUploadDto.getOfferType());
	}

	@Test
	public void testToString() {
		assertNotNull(voucherUploadDto.toString());
	}

}