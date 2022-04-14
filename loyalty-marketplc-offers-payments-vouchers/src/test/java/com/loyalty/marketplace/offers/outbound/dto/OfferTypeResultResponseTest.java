package com.loyalty.marketplace.offers.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = OfferTypeResultResponse.class)
@ActiveProfiles("unittest")
public class OfferTypeResultResponseTest {

	private OfferTypeResultResponse resultResponse;
	
	@Before
	public void setUp(){
		resultResponse = new OfferTypeResultResponse("");
		resultResponse.setOfferTypes(new ArrayList<OfferTypesDto>());
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(resultResponse.toString());
	}
	
	@Test
	public void testGetResult()
	{ 
		assertNotNull(resultResponse.getOfferTypes());
	}
	
}
