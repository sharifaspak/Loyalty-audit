package com.loyalty.marketplace.offers.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = DenominationRequestDto.class)
@ActiveProfiles("unittest")
public class DenominationRequestDtoTest {

	private DenominationRequestDto denominationRequestDto;
	
	@Before
	public void setUp(){
		denominationRequestDto = new DenominationRequestDto();
		denominationRequestDto.setDenominations(new ArrayList<DenominationDto>());
	}
	
	@Test
	public void testGetters() {
		assertNotNull(denominationRequestDto.getDenominations());
	}
	
	@Test
	public void testToString() {
		assertNotNull(denominationRequestDto.toString());
	}
	
}
