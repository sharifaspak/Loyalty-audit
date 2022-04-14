package com.loyalty.marketplace.offers.merchants.outbound.database.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = WhatYouGet.class)
@ActiveProfiles("unittest")
public class WhatYouGetTest {

	private WhatYouGet whatYouGet;
	private String testString;
	
	@Before
	public void setUp(){
		whatYouGet = new WhatYouGet();
		testString = "testValue";
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(whatYouGet.toString());
	}
	
	@Test
	public void testGetWhatYouGetEn()
	{
		whatYouGet.setWhatYouGetEn(testString);
		assertEquals(whatYouGet.getWhatYouGetEn(), testString);
	}
	
	@Test
	public void testGetWhatYouGetAr()
	{
		whatYouGet.setWhatYouGetAr(testString);
		assertEquals(whatYouGet.getWhatYouGetAr(), testString);
	}
	
}
