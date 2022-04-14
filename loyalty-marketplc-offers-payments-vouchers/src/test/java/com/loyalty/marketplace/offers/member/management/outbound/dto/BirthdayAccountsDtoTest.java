package com.loyalty.marketplace.offers.member.management.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = BirthdayAccountsDto.class)
@ActiveProfiles("unittest")
public class BirthdayAccountsDtoTest {

	private BirthdayAccountsDto birthdayAccountsDto;
	
	@Before
	public void setUp(){
		birthdayAccountsDto = new BirthdayAccountsDto();
		birthdayAccountsDto.setAccountNumber("100L");
		birthdayAccountsDto.setEmail("");
		birthdayAccountsDto.setLanguage("");
		birthdayAccountsDto.setUiLanguage("");
		birthdayAccountsDto.setFirstName("");
		birthdayAccountsDto.setLastName("");
		birthdayAccountsDto.setDob(new Date());
		birthdayAccountsDto.setStatus("");
		birthdayAccountsDto.setMembershipCode("");
		
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(birthdayAccountsDto.getAccountNumber());
		assertNotNull(birthdayAccountsDto.getEmail());
		assertNotNull(birthdayAccountsDto.getLanguage());
		assertNotNull(birthdayAccountsDto.getUiLanguage());
		assertNotNull(birthdayAccountsDto.getFirstName());
		assertNotNull(birthdayAccountsDto.getLastName());
		assertNotNull(birthdayAccountsDto.getDob());
	
	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(birthdayAccountsDto.toString());
	}
	
}
