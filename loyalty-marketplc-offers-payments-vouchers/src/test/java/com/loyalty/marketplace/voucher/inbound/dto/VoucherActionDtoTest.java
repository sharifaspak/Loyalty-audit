package com.loyalty.marketplace.voucher.inbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = VoucherActionDto.class)
@ActiveProfiles("unittest")
public class VoucherActionDtoTest {
	
	private VoucherActionDto voucherActionDto;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		voucherActionDto = new VoucherActionDto();
		voucherActionDto.setAction("");
		voucherActionDto.setId("");
		voucherActionDto.setLabel("");
		voucherActionDto.setRedemptionMethod("");
	}

	@Test
	public void testGetters() {
		assertNotNull(voucherActionDto.getAction());
		assertNotNull(voucherActionDto.getId());
		assertNotNull(voucherActionDto.getLabel());
		assertNotNull(voucherActionDto.getRedemptionMethod());
	}
	
	@Test
	public void testToString() {
		assertNotNull(voucherActionDto.toString());
	}

}