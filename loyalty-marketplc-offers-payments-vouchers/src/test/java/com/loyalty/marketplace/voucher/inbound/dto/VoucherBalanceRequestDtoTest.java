package com.loyalty.marketplace.voucher.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest(classes = VoucherBalanceRequestDto.class)
@ActiveProfiles("unittest")
public class VoucherBalanceRequestDtoTest {

	private VoucherBalanceRequestDto voucherBalanceRequestDto;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		voucherBalanceRequestDto = new VoucherBalanceRequestDto();
		voucherBalanceRequestDto.setAccountNumber("");
		voucherBalanceRequestDto.setAmount(0);
		voucherBalanceRequestDto.setCardNumber("");
		voucherBalanceRequestDto.setCrfrTransId("");
		voucherBalanceRequestDto.setUuid("");
	}

	@Test
	public void testGetters() {
		assertNotNull(voucherBalanceRequestDto.getAccountNumber());
		assertNotNull(voucherBalanceRequestDto.getAmount());
		assertNotNull(voucherBalanceRequestDto.getCardNumber());
		assertNotNull(voucherBalanceRequestDto.getCrfrTransId());
		assertNotNull(voucherBalanceRequestDto.getUuid());
	}
	
	@Test
	public void testToString() {
		assertNotNull(voucherBalanceRequestDto.toString());
	}
	
}