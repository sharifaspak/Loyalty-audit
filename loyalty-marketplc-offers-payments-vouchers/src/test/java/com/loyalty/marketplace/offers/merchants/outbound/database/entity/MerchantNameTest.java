package com.loyalty.marketplace.offers.merchants.outbound.database.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = MerchantName.class)
@ActiveProfiles("unittest")
public class MerchantNameTest {

	private MerchantName merchantName;
	private String testString;
	
	@Before
	public void setUp(){
		merchantName = new MerchantName();
		testString = "testValue";
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(merchantName.toString());
	}
	
	@Test
	public void testGetMerchantNameEn()
	{
		merchantName.setMerchantNameEn(testString);
		assertEquals(merchantName.getMerchantNameEn(), testString);
	}
	
	@Test
	public void testGetMerchantNameAr()
	{
		merchantName.setMerchantNameAr(testString);
		assertEquals(merchantName.getMerchantNameAr(), testString);
	}
	
}
