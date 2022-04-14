package com.loyalty.marketplace.offers.helper.dto;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = BirthDayOfferValidationDto.class)
@ActiveProfiles("unittest")
public class BirthdayOfferValidationDtoTest {

	private BirthDayOfferValidationDto birthDayOfferValidationDto;
	
	@Before
	public void setUp(){
		birthDayOfferValidationDto = new BirthDayOfferValidationDto();
		birthDayOfferValidationDto.setAccountNumber("");
		birthDayOfferValidationDto.setMembershipCode("");
		birthDayOfferValidationDto.setLastGiftReceivedDate("");
		birthDayOfferValidationDto.setUpdatedAt(new Date());
	}
	
	@Test
	public void testGetters() {
		birthDayOfferValidationDto = new BirthDayOfferValidationDto("", "", "", new Date());
		assertNotNull(birthDayOfferValidationDto.getAccountNumber());
		assertNotNull(birthDayOfferValidationDto.getMembershipCode());
		assertNotNull(birthDayOfferValidationDto.getLastGiftReceivedDate());
		assertNotNull(birthDayOfferValidationDto.getUpdatedAt());
	}
	
	@Test
	public void testToString() {
		assertNotNull(birthDayOfferValidationDto.toString());
	}
	
}
