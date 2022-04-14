package com.loyalty.marketplace.offers.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.loyalty.marketplace.offers.inbound.dto.DenominationDto;

@SpringBootTest(classes = DenominationResultResponse.class)
@ActiveProfiles("unittest")
public class DenominationResultResponseTest {

	private DenominationResultResponse resultResponse;
	
	@Before
	public void setUp(){
		resultResponse = new DenominationResultResponse("");
		resultResponse.setDenominations(new ArrayList<DenominationDto>());
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(resultResponse.toString());
	}
	
	@Test
	public void testGetResult()
	{ 
		assertNotNull(resultResponse.getDenominations());
	}
	
}
