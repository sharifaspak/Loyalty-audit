package com.loyalty.marketplace.voucher.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.offers.merchants.outbound.database.entity.Barcode;

@SpringBootTest(classes = VoucherRequestDto.class)
@ActiveProfiles("unittest")
public class VoucherRequestDtoTest {
	private VoucherRequestDto voucherRequestDto;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		voucherRequestDto = new VoucherRequestDto();
		voucherRequestDto.setAccountNumber("");
		voucherRequestDto.setBarcodeType(new Barcode());
		voucherRequestDto.setCost(0.0);
		voucherRequestDto.setDenominationId("");
		voucherRequestDto.setMerchantCode("");
		voucherRequestDto.setMembershipCode("");
		voucherRequestDto.setMerchantEmail(new ArrayList<String>());
		voucherRequestDto.setMerchantName("");
		voucherRequestDto.setNumberOfVoucher(0);
		voucherRequestDto.setOfferId("");
		voucherRequestDto.setOfferType("");
		voucherRequestDto.setPartnerCode("");
		voucherRequestDto.setPointsValue(0);
		voucherRequestDto.setSubOfferId("");
		voucherRequestDto.setUuid("");
		voucherRequestDto.setVoucherAction("");
		voucherRequestDto.setVoucherAmount(0.0);
		voucherRequestDto.setVoucherExpiryDate(new Date());
		voucherRequestDto.setVoucherExpiryPeriod(0);
		
	}

	@Test
	public void testGetters() {
		assertNotNull(voucherRequestDto.getAccountNumber());
		assertNotNull(voucherRequestDto.getBarcodeType());
		assertNotNull(voucherRequestDto.getCost());
		assertNotNull(voucherRequestDto.getDenominationId());
		assertNotNull(voucherRequestDto.getMembershipCode());
		assertNotNull(voucherRequestDto.getMerchantCode());
		assertNotNull(voucherRequestDto.getMerchantEmail());
		assertNotNull(voucherRequestDto.getMerchantName());
		assertNotNull(voucherRequestDto.getNumberOfVoucher());
		assertNotNull(voucherRequestDto.getOfferId());
		assertNotNull(voucherRequestDto.getOfferType());
		assertNotNull(voucherRequestDto.getPartnerCode());
		assertNotNull(voucherRequestDto.getPointsValue());
		assertNotNull(voucherRequestDto.getSubOfferId());
		assertNotNull(voucherRequestDto.getUuid());
		assertNotNull(voucherRequestDto.getVoucherAction());
		assertNotNull(voucherRequestDto.getVoucherAmount());
		assertNotNull(voucherRequestDto.getVoucherExpiryDate());
		assertNotNull(voucherRequestDto.getVoucherExpiryPeriod());
		
	}
	
	@Test
	public void testToString() {
		assertNotNull(voucherRequestDto.toString());
	}

}