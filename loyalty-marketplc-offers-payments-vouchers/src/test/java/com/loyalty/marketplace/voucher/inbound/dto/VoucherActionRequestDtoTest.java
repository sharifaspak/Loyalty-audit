package com.loyalty.marketplace.voucher.inbound.dto;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = VoucherActionRequestDto.class)
@ActiveProfiles("unittest")
public class VoucherActionRequestDtoTest {

	private VoucherActionRequestDto voucherActionRequestDto;
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		voucherActionRequestDto = new VoucherActionRequestDto();
		voucherActionRequestDto.setVoucherActionDto(new ArrayList<VoucherActionDto>());
	}

	@Test
	public void testGetters() {
		assertNotNull(voucherActionRequestDto.getVoucherActionDto());
	}
	
	@Test
	public void testToString() {
		assertNotNull(voucherActionRequestDto.toString());
	}

}