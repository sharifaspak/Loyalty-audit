package com.loyalty.marketplace.offers.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = OfferCatalogResultResponse.class)
@ActiveProfiles("unittest")
public class OfferCatalogResultResponseTest {

	private OfferCatalogResultResponse resultResponse;
	
	@Before
	public void setUp(){
		resultResponse = new OfferCatalogResultResponse("");
		resultResponse.setOfferCatalogs(new ArrayList<OfferCatalogResultResponseDto>());
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(resultResponse.toString());
	}
	
	@Test
	public void testGetResult()
	{ 
		assertNotNull(resultResponse.getOfferCatalogs());
	}
	
}
