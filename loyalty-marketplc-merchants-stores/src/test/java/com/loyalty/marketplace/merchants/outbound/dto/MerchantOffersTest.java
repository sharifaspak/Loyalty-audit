package com.loyalty.marketplace.merchants.outbound.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = MerchantOffersTest.class)
@ActiveProfiles("unittest")
public class MerchantOffersTest {

	private MerchantOffers merchantOffersDto;
	private String testString;
	
	@Before
	public void setUp(){
		merchantOffersDto = new MerchantOffers();
		testString = "Test String";
	}
	
	@Test
	public void testGetCategory()
	{
		merchantOffersDto.setCategory(testString);;
		assertEquals(merchantOffersDto.getCategory(), testString);
	}
	
	@Test
	public void testGetOfferTitle()
	{
		merchantOffersDto.setOfferTitle(testString);;
		assertEquals(merchantOffersDto.getOfferTitle(), testString);
	}
	
	@Test
	public void testGetOfferType()
	{
		merchantOffersDto.setOfferType(testString);;
		assertEquals(merchantOffersDto.getOfferType(), testString);
	}
	
	@Test
	public void testGetPointValue()
	{
		merchantOffersDto.setPointValue(123);
		assertEquals(123, merchantOffersDto.getPointValue());
	}
}
