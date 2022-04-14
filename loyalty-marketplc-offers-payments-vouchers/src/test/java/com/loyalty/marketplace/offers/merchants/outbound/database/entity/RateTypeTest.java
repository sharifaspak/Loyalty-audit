package com.loyalty.marketplace.offers.merchants.outbound.database.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = RateType.class)
@ActiveProfiles("unittest")
public class RateTypeTest {

	private RateType rateType;
	private String testString;
	
	@Before
	public void setUp(){
		rateType = new RateType();
		testString = "testValue";
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(rateType.toString());
	}
	
	@Test
	public void testGetTypeId()
	{
		rateType.setTypeId(testString);
		assertEquals(rateType.getTypeId(), testString);
	}
	
	@Test
	public void testGetTypeRate()
	{
		rateType.setTypeRate(testString);
		assertEquals(rateType.getTypeRate(), testString);
	}
	
	@Test
	public void testGetTypeRateDesc()
	{
		rateType.setTypeRateDesc(testString);
		assertEquals(rateType.getTypeRateDesc(), testString);
	}
	
	@Test
	public void testGetType()
	{
		rateType.setType(testString);
		assertEquals(rateType.getType(), testString);
	}
	
}
