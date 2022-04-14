package com.loyalty.marketplace.offers.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=BirthdayInfoResponseDto.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class BirthdayInfoResponseDtoTest {
	
	private BirthdayInfoResponseDto birthdayInfo;
		
	@Before
	public void setUp(){
		
		birthdayInfo = new BirthdayInfoResponseDto();
		birthdayInfo.setDob(new Date());
		birthdayInfo.setDescriptionEn("");
		birthdayInfo.setDescriptionAr("");
		birthdayInfo.setTitleEn("");
		birthdayInfo.setTitleAr("");
		birthdayInfo.setSubTitleEn("");
		birthdayInfo.setSubTitleAr("");
		birthdayInfo.setIconTextEn("");
		birthdayInfo.setIconTextAr("");
		birthdayInfo.setWeekIconEn("");
		birthdayInfo.setWeekIconAr("");
		birthdayInfo.setPurchaseLimit(0);
		birthdayInfo.setDisplayLimit(0);
		birthdayInfo.setThresholdMinusValue(0);
		birthdayInfo.setThresholdPlusValue(0);
		birthdayInfo.setFirstName("");
		birthdayInfo.setLastName("");
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(birthdayInfo.getDob());
		assertNotNull(birthdayInfo.getDescriptionEn());
		assertNotNull(birthdayInfo.getDescriptionAr());
		assertNotNull(birthdayInfo.getTitleEn());
		assertNotNull(birthdayInfo.getTitleAr());
		assertNotNull(birthdayInfo.getSubTitleEn());
		assertNotNull(birthdayInfo.getSubTitleAr());
		assertNotNull(birthdayInfo.getIconTextEn());
		assertNotNull(birthdayInfo.getIconTextAr());
		assertNotNull(birthdayInfo.getWeekIconEn());
		assertNotNull(birthdayInfo.getWeekIconAr());
		assertNotNull(birthdayInfo.getPurchaseLimit());
		assertNotNull(birthdayInfo.getDisplayLimit());
		assertNotNull(birthdayInfo.getThresholdMinusValue());
		assertNotNull(birthdayInfo.getThresholdPlusValue());
		assertNotNull(birthdayInfo.getFirstName());
		assertNotNull(birthdayInfo.getLastName());
		
	
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(birthdayInfo.toString());
	    
			
	}
		
}
