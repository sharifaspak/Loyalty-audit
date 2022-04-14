package com.loyalty.marketplace.offers.inbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = DenominationDto.class)
@ActiveProfiles("unittest")
public class DenominationDtoTest {

	private DenominationDto denominationDto;
	
	@Before
	public void setUp(){
		denominationDto = new DenominationDto();
		denominationDto.setDenominationId("");
		denominationDto.setDescriptionEn("");
		denominationDto.setDescriptionAr("");
		denominationDto.setPointValue(0);
		denominationDto.setDirhamValue(0);
	}
	
	@Test
	public void testGetters() {
		assertNotNull(denominationDto.getDenominationId());
		assertNotNull(denominationDto.getDescriptionEn());
		assertNotNull(denominationDto.getDescriptionAr());
		assertNotNull(denominationDto.getPointValue());
		assertNotNull(denominationDto.getDirhamValue());
	}
	
	@Test
	public void testToString() {
		assertNotNull(denominationDto.toString());
	}
	
}
