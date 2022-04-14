package com.loyalty.marketplace.offers.member.management.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = EmailVerificationDto.class)
@ActiveProfiles("unittest")
public class EmailVerificationDtoTest {

	private EmailVerificationDto emailVerificationDto;

	@Before
	public void setUp() {

		emailVerificationDto = new EmailVerificationDto();
		emailVerificationDto.setVerificationStatus("");
		emailVerificationDto.setVerificationDate(new Date());
	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(emailVerificationDto.getVerificationStatus());
		assertNotNull(emailVerificationDto.getVerificationDate());

	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(emailVerificationDto.toString());
	}
}
