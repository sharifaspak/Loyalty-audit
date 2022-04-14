package com.loyalty.marketplace.merchants.outbound.database.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = MerchantDescription.class)
@ActiveProfiles("unittest")
public class MerchantDescriptionTest {

	private MerchantDescription merchantDescription;
	private String testString;
	
	@Before
	public void setUp(){
		merchantDescription = new MerchantDescription();
		testString = "testValue";
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(merchantDescription.toString());
	}
	
	@Test
	public void testGetMerchantDescEn()
	{
		merchantDescription.setMerchantDescEn(testString);
		assertEquals(merchantDescription.getMerchantDescEn(), testString);
	}
	
	@Test
	public void testGetMerchantDescAr()
	{
		merchantDescription.setMerchantDescAr(testString);
		assertEquals(merchantDescription.getMerchantDescAr(), testString);
	}
	
}
