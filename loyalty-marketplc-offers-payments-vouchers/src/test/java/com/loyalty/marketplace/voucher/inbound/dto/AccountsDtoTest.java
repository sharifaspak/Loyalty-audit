package com.loyalty.marketplace.voucher.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest(classes = AccountsDto.class)
@ActiveProfiles("unittest")
public class AccountsDtoTest {

	private AccountsDto accountsDto;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		accountsDto = new AccountsDto();
		accountsDto.setAccountNumber(new ArrayList<String>());
	}

	@Test
	public void testGetters() {
		assertNotNull(accountsDto.getAccountNumber());		
	
	}
	
	@Test
	public void testToString() {
		assertNotNull(accountsDto.toString());
	}

}