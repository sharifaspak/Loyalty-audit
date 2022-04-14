package com.loyalty.marketplace.offers.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = BirthdayInfoResultResponse.class)
@ActiveProfiles("unittest")
public class BirthdayInfoResultResponseTest {

	private BirthdayInfoResultResponse resultResponse;
	
	@Before
	public void setUp(){
		resultResponse = new BirthdayInfoResultResponse("");
		resultResponse.setBirthdayInfoResponseDto(new BirthdayInfoResponseDto());
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(resultResponse.toString());
	}
	
	@Test
	public void testGetResult()
	{ 
		assertNotNull(resultResponse.getBirthdayInfoResponseDto());
	}
	
}
