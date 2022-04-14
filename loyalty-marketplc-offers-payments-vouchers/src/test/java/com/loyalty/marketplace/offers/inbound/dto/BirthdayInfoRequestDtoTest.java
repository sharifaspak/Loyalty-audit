package com.loyalty.marketplace.offers.inbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = BirthdayInfoRequestDto.class)
@ActiveProfiles("unittest")
public class BirthdayInfoRequestDtoTest {

	private BirthdayInfoRequestDto birthdayInfoRequestDto;
	
	@Before
	public void setUp(){
		
		birthdayInfoRequestDto = new BirthdayInfoRequestDto();
		birthdayInfoRequestDto.setTitleEn("");
		birthdayInfoRequestDto.setTitleAr("");
		birthdayInfoRequestDto.setSubTitleEn("");
		birthdayInfoRequestDto.setSubTitleAr("");
		birthdayInfoRequestDto.setDescriptionEn("");
		birthdayInfoRequestDto.setDescriptionAr("");
		birthdayInfoRequestDto.setIconTextEn("");
		birthdayInfoRequestDto.setIconTextAr("");
		birthdayInfoRequestDto.setWeekIconEn("");
		birthdayInfoRequestDto.setWeekIconAr("");
		birthdayInfoRequestDto.setPurchaseLimit(0);
		birthdayInfoRequestDto.setThresholdPlusValue(0);
		birthdayInfoRequestDto.setThresholdMinusValue(0);
		birthdayInfoRequestDto.setDisplayLimit(0);
	}
	
	@Test
	public void testGetters() {
		assertNotNull(birthdayInfoRequestDto.getTitleEn());
		assertNotNull(birthdayInfoRequestDto.getTitleAr());
		assertNotNull(birthdayInfoRequestDto.getSubTitleEn());
		assertNotNull(birthdayInfoRequestDto.getSubTitleAr());
		assertNotNull(birthdayInfoRequestDto.getDescriptionEn());
		assertNotNull(birthdayInfoRequestDto.getDescriptionAr());
		assertNotNull(birthdayInfoRequestDto.getIconTextEn());
		assertNotNull(birthdayInfoRequestDto.getIconTextAr());
		assertNotNull(birthdayInfoRequestDto.getWeekIconEn());
		assertNotNull(birthdayInfoRequestDto.getWeekIconAr());
		assertNotNull(birthdayInfoRequestDto.getPurchaseLimit());
		assertNotNull(birthdayInfoRequestDto.getThresholdPlusValue());
		assertNotNull(birthdayInfoRequestDto.getThresholdMinusValue());
		assertNotNull(birthdayInfoRequestDto.getDisplayLimit());
	}
	
	@Test
	public void testToString() {
		assertNotNull(birthdayInfoRequestDto.toString());
	}
	
}
