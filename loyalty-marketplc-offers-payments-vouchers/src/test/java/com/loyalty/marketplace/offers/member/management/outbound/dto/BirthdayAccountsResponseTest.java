package com.loyalty.marketplace.offers.member.management.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = BirthdayAccountsResponse.class)
@ActiveProfiles("unittest")
public class BirthdayAccountsResponseTest {

	private BirthdayAccountsResponse birthdayAccountsResponse;

	@Before
	public void setUp() {

		birthdayAccountsResponse = new BirthdayAccountsResponse();
		birthdayAccountsResponse.setAccountList(new ArrayList<>());
		
	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(birthdayAccountsResponse.getAccountList());

	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(birthdayAccountsResponse.toString());
	}
}
