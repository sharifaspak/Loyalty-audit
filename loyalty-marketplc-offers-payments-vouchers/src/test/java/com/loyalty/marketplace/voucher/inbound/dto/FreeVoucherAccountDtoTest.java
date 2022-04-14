package com.loyalty.marketplace.voucher.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest(classes = FreeVoucherAccountDto.class)
@ActiveProfiles("unittest")
public class FreeVoucherAccountDtoTest {

	private FreeVoucherAccountDto freeVoucherAccountDto;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		freeVoucherAccountDto = new FreeVoucherAccountDto();
		freeVoucherAccountDto.setAccountNumber("");
		freeVoucherAccountDto.setLoyaltyId("");
		freeVoucherAccountDto.setMembershipCode("");
	}

	@Test
	public void testGetters() {
		assertNotNull(freeVoucherAccountDto.getAccountNumber());		
		assertNotNull(freeVoucherAccountDto.getLoyaltyId());		
		assertNotNull(freeVoucherAccountDto.getMembershipCode());
	}
	
	@Test
	public void testToString() {
		assertNotNull(freeVoucherAccountDto.toString());
	}

}