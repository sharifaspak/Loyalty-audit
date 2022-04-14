package com.loyalty.marketplace.merchants.inbound.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.merchants.outbound.database.entity.RateType;

@SpringBootTest(classes = RateTypeRequestDto.class)
@ActiveProfiles("unittest")
public class RateTypeRequestDtoTest {

	private RateTypeRequestDto rateTypeRequestDto;
	
	@Before
	public void setUp(){
		rateTypeRequestDto = new RateTypeRequestDto();
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(rateTypeRequestDto.toString());
	}
	
	@Test
	public void testHashCode()
	{
		assertNotNull(rateTypeRequestDto.hashCode());
	}
	
	@Test
	public void testEquals()
	{
		assertTrue(rateTypeRequestDto.equals(new RateTypeRequestDto()));
	}
	
	@Test
	public void testEqualsObject()
	{
		RateTypeRequestDto rateTypeRequest = rateTypeRequestDto;
		assertTrue(rateTypeRequestDto.equals(rateTypeRequest));
	}
	
	@Test
	public void testNotEqualsObject()
	{
		RateTypeRequestDto rateTypeRequest = null;
		assertFalse(rateTypeRequestDto.equals(rateTypeRequest));
	}
	
	@Test
	@SuppressWarnings("unlikely-arg-type")
	public void testNotEqualsObjectClass()
	{
		assertFalse(rateTypeRequestDto.equals(new RateType()));
	}
	
	@Test
	public void testNotEqualsNullRateTypes()
	{
		RateTypeRequestDto rateType = new RateTypeRequestDto();
		rateTypeRequestDto.setRateTypes(null);
		rateType.setRateTypes(new ArrayList<RateTypeDto>());
		assertFalse(rateTypeRequestDto.equals(rateType));
	}
	
	@Test
	public void testNotEqualsRateTypes()
	{
		RateTypeRequestDto merchant = new RateTypeRequestDto();
		rateTypeRequestDto.setRateTypes(new ArrayList<RateTypeDto>());
		merchant.setRateTypes(null);
		assertFalse(rateTypeRequestDto.equals(merchant));
	}
	
	@Test
	public void testGetRateTypes()
	{
		List<RateTypeDto> rateTypes = new ArrayList<RateTypeDto>();
		rateTypeRequestDto.setRateTypes(rateTypes);;
		assertEquals(rateTypeRequestDto.getRateTypes(), rateTypes);
	}
	
}
