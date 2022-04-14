package com.loyalty.marketplace.offers.inbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = TransactionRequestDto.class)
@ActiveProfiles("unittest")
public class TransactionRequestDtoTest {

	private TransactionRequestDto transactionRequestDto;
	
	@Before
	public void setUp(){
		transactionRequestDto = new TransactionRequestDto();
		transactionRequestDto.setFromDate("");
		transactionRequestDto.setToDate("");
		transactionRequestDto.setAccountNumber("");
		transactionRequestDto.setMembershipCode("");
		transactionRequestDto.setTransactionType("");
		transactionRequestDto.setIncludeLinkedAccounts(true);
		
	}
	
	@Test
	public void testGetters() {
		assertNotNull(transactionRequestDto.getFromDate());
		assertNotNull(transactionRequestDto.getToDate());
		assertNotNull(transactionRequestDto.getAccountNumber());
		assertNotNull(transactionRequestDto.getMembershipCode());
		assertNotNull(transactionRequestDto.getTransactionType());
		assertNotNull(transactionRequestDto.isIncludeLinkedAccounts());
		
	}
	
	@Test
	public void testToString() {
		assertNotNull(transactionRequestDto.toString());
	}
	
}
