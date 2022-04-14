package com.loyalty.marketplace.offers.helper.dto;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = BirthdayDurationInfoDto.class)
@ActiveProfiles("unittest")
public class BirthdayDurationInfoDtoTest {

	private BirthdayDurationInfoDto birthdayDurationInfoDto;
	
	@Before
	public void setUp(){
		birthdayDurationInfoDto = new BirthdayDurationInfoDto();
		birthdayDurationInfoDto.setStartDate(new Date());
		birthdayDurationInfoDto.setEndDate(new Date());
	}
	
	@Test
	public void testGetters() {
		assertNotNull(birthdayDurationInfoDto.getStartDate());
		assertNotNull(birthdayDurationInfoDto.getEndDate());
	}
	
	@Test
	public void testToString() {
		assertNotNull(birthdayDurationInfoDto.toString());
	}
	
}
