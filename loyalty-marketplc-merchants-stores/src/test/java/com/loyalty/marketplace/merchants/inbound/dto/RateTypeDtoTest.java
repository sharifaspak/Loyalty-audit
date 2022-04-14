package com.loyalty.marketplace.merchants.inbound.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.merchants.outbound.database.entity.RateType;

@SpringBootTest(classes = RateTypeDto.class)
@ActiveProfiles("unittest")
public class RateTypeDtoTest {

	private RateTypeDto rateTypeDto;
	private String testString;
	private String originalString;
	
	@Before
	public void setUp(){
		rateTypeDto = new RateTypeDto();
		testString = "Test String";
		originalString = "Original String";
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(rateTypeDto.toString());
	}
	
	@Test
	public void testHashCode()
	{
		assertNotNull(rateTypeDto.hashCode());
	}
	
	@Test
	public void testEquals()
	{
		assertTrue(rateTypeDto.equals(new RateTypeDto()));
	}
	
	@Test
	public void testEqualsObject()
	{
		RateTypeDto rateType = rateTypeDto;
		assertTrue(rateTypeDto.equals(rateType));
	}
	
	@Test
	public void testNotEqualsObject()
	{
		RateTypeDto rateType = null;
		assertFalse(rateTypeDto.equals(rateType));
	}
	
	@Test
	@SuppressWarnings("unlikely-arg-type")
	public void testNotEqualsObjectClass()
	{
		assertFalse(rateTypeDto.equals(new RateType()));
	}
	
	@Test
	public void testNotEqualsNullTypeId()
	{
		RateTypeDto rateType = new RateTypeDto();
		rateTypeDto.setTypeId(null);
		rateType.setTypeId(testString);
		assertFalse(rateTypeDto.equals(rateType));
	}
	
	@Test
	public void testNotEqualsTypeId()
	{
		RateTypeDto rateType = new RateTypeDto();
		rateTypeDto.setTypeId(originalString);
		rateType.setTypeId(testString);
		assertFalse(rateTypeDto.equals(rateType));
	}
	
	@Test
	public void testNotEqualsNullTypeRate()
	{
		RateTypeDto rateType = new RateTypeDto();
		rateTypeDto.setTypeRate(null);
		rateType.setTypeRate(testString);
		assertFalse(rateTypeDto.equals(rateType));
	}
	
	@Test
	public void testNotEqualsTypeRate()
	{
		RateTypeDto rateType = new RateTypeDto();
		rateTypeDto.setTypeRate(originalString);
		rateType.setTypeRate(testString);
		assertFalse(rateTypeDto.equals(rateType));
	}
	
	@Test
	public void testNotEqualsNullTypeRateDesc()
	{
		RateTypeDto rateType = new RateTypeDto();
		rateTypeDto.setTypeRateDesc(null);
		rateType.setTypeRateDesc(testString);
		assertFalse(rateTypeDto.equals(rateType));
	}
	
	@Test
	public void testNotEqualsTypeRateDesc()
	{
		RateTypeDto rateType = new RateTypeDto();
		rateTypeDto.setTypeRateDesc(originalString);
		rateType.setTypeRateDesc(testString);
		assertFalse(rateTypeDto.equals(rateType));
	}
	
	@Test
	public void testNotEqualsNullType()
	{
		RateTypeDto rateType = new RateTypeDto();
		rateTypeDto.setType(null);
		rateType.setType(testString);
		assertFalse(rateTypeDto.equals(rateType));
	}
	
	@Test
	public void testNotEqualsType()
	{
		RateTypeDto rateType = new RateTypeDto();
		rateTypeDto.setType(originalString);
		rateType.setType(testString);
		assertFalse(rateTypeDto.equals(rateType));
	}
	
	@Test
	public void testGetTypeId()
	{
		rateTypeDto.setTypeId(testString);;
		assertEquals(rateTypeDto.getTypeId(), testString);
	}
	
	@Test
	public void testGetTypeRate()
	{
		rateTypeDto.setTypeRate(testString);;
		assertEquals(rateTypeDto.getTypeRate(), testString);
	}
	
	@Test
	public void testGetTypeRateDesc()
	{
		rateTypeDto.setTypeRateDesc(testString);;
		assertEquals(rateTypeDto.getTypeRateDesc(), testString);
	}
	
	@Test
	public void testGetType()
	{
		rateTypeDto.setType(testString);;
		assertEquals(rateTypeDto.getType(), testString);
	}
	
}
