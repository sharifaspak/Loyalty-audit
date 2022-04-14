package com.loyalty.marketplace.offers.merchants.outbound.database.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = MerchantBillingRate.class)
@ActiveProfiles("unittest")
public class MerchantBillingRateTest {

	private MerchantBillingRate billingRate;
	private String testString;
	private Double testRate;
	private Date testDate;
	
	@Before
	public void setUp(){
		billingRate = new MerchantBillingRate();
		testString = "testValue";
		testRate = 5.5;
		testDate = new Date();
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(billingRate.toString());
	}
	
	@Test
	public void testGetId()
	{
		billingRate.setId(testString);
		assertEquals(billingRate.getId(), testString);
	}
	
	@Test
	public void testGetRate()
	{
		billingRate.setRate(testRate);
		assertEquals(billingRate.getRate(), testRate);
	}
	
	@Test
	public void testGetStartDate()
	{
		billingRate.setStartDate(testDate);
		assertEquals(billingRate.getStartDate(), testDate);
	}
	
	@Test
	public void testGetEndDate()
	{
		billingRate.setEndDate(testDate);
		assertEquals(billingRate.getEndDate(), testDate);
	}
	
	@Test
	public void testGetRateType()
	{
		billingRate.setRateType(testString);
		assertEquals(billingRate.getRateType(), testString);
	}
	
	@Test
	public void testGetCurrency()
	{
		billingRate.setCurrency(testString);
		assertEquals(billingRate.getCurrency(), testString);
	}
	
}
