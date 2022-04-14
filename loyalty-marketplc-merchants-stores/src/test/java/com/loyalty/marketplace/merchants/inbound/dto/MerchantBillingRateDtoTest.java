package com.loyalty.marketplace.merchants.inbound.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.merchants.outbound.database.entity.MerchantBillingRate;

@SpringBootTest(classes = MerchantBillingRateDto.class)
@ActiveProfiles("unittest")
public class MerchantBillingRateDtoTest {

	private MerchantBillingRateDto billingRateDto;
	private String testString;
	private String originalString;
	private Double testRate;
	private Double originalRate;
	
	@Before
	public void setUp(){
		billingRateDto = new MerchantBillingRateDto();
		testString = "Test String";
		originalString = "Original String";
		testRate = 0.05;
		originalRate = 0.08;
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(billingRateDto.toString());
	}
	
	@Test
	public void testHashCode()
	{
		assertNotNull(billingRateDto.hashCode());
	}
	
	@Test
	public void testEquals()
	{
		assertTrue(billingRateDto.equals(new MerchantBillingRateDto()));
	}
	
	@Test
	public void testEqualsObject()
	{
		MerchantBillingRateDto merchant = billingRateDto;
		assertTrue(billingRateDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsObject()
	{
		MerchantBillingRateDto merchant = null;
		assertFalse(billingRateDto.equals(merchant));
	}
	
	@Test
	@SuppressWarnings("unlikely-arg-type")
	public void testNotEqualsObjectClass()
	{
		assertFalse(billingRateDto.equals(new MerchantBillingRate()));
	}
	
	@Test
	public void testNotEqualsNullRate()
	{
		MerchantBillingRateDto billingRate = new MerchantBillingRateDto();
		billingRateDto.setRate(null);
		billingRate.setRate(testRate);
		assertFalse(billingRateDto.equals(billingRate));
	}
	
	@Test
	public void testNotEqualsRate()
	{
		MerchantBillingRateDto billingRate = new MerchantBillingRateDto();
		billingRateDto.setRate(originalRate);
		billingRate.setRate(testRate);
		assertFalse(billingRateDto.equals(billingRate));
	}
	
	@Test
	public void testNotEqualsNullStartDate()
	{
		MerchantBillingRateDto billingRate = new MerchantBillingRateDto();
		billingRateDto.setStartDate(null);
		billingRate.setStartDate(testString);
		assertFalse(billingRateDto.equals(billingRate));
	}
	
	@Test
	public void testNotEqualsStartDate()
	{
		MerchantBillingRateDto billingRate = new MerchantBillingRateDto();
		billingRateDto.setStartDate(originalString);
		billingRate.setStartDate(testString);
		assertFalse(billingRateDto.equals(billingRate));
	}
	
	@Test
	public void testNotEqualsNullEndDate()
	{
		MerchantBillingRateDto billingRate = new MerchantBillingRateDto();
		billingRateDto.setEndDate(null);
		billingRate.setEndDate(testString);
		assertFalse(billingRateDto.equals(billingRate));
	}
	
	@Test
	public void testNotEqualsEndDate()
	{
		MerchantBillingRateDto billingRate = new MerchantBillingRateDto();
		billingRateDto.setEndDate(originalString);
		billingRate.setEndDate(testString);
		assertFalse(billingRateDto.equals(billingRate));
	}
	
	@Test
	public void testNotEqualsNullRateType()
	{
		MerchantBillingRateDto billingRate = new MerchantBillingRateDto();
		billingRateDto.setRateType(null);
		billingRate.setRateType(testString);
		assertFalse(billingRateDto.equals(billingRate));
	}
	
	@Test
	public void testNotEqualsRateType()
	{
		MerchantBillingRateDto billingRate = new MerchantBillingRateDto();
		billingRateDto.setRateType(originalString);
		billingRate.setRateType(testString);
		assertFalse(billingRateDto.equals(billingRate));
	}
	
	@Test
	public void testNotEqualsNullCurrency()
	{
		MerchantBillingRateDto billingRate = new MerchantBillingRateDto();
		billingRateDto.setCurrency(null);
		billingRate.setCurrency(testString);
		assertFalse(billingRateDto.equals(billingRate));
	}
	
	@Test
	public void testNotEqualsCurrency()
	{
		MerchantBillingRateDto billingRate = new MerchantBillingRateDto();
		billingRateDto.setCurrency(originalString);
		billingRate.setCurrency(testString);
		assertFalse(billingRateDto.equals(billingRate));
	}
	
	@Test
	public void testGetRate()
	{
		billingRateDto.setRate(testRate);;
		assertEquals(billingRateDto.getRate(), testRate);
	}
	
	@Test
	public void testGetStartDate()
	{
		billingRateDto.setStartDate(testString);;
		assertEquals(billingRateDto.getStartDate(), testString);
	}
	
	@Test
	public void testGetEndDate()
	{
		billingRateDto.setEndDate(testString);;
		assertEquals(billingRateDto.getEndDate(), testString);
	}
	
	@Test
	public void testGetRateType()
	{
		billingRateDto.setRateType(testString);;
		assertEquals(billingRateDto.getRateType(), testString);
	}
	
	@Test
	public void testGetCurrency()
	{
		billingRateDto.setCurrency(testString);;
		assertEquals(billingRateDto.getCurrency(), testString);
	}
	
}
