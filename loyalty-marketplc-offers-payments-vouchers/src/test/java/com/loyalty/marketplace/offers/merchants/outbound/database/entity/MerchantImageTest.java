package com.loyalty.marketplace.offers.merchants.outbound.database.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = MerchantImage.class)
@ActiveProfiles("unittest")
public class MerchantImageTest {

	private MerchantImage merchantImage;
	private String testString;
	
	@Before
	public void setUp(){
		merchantImage = new MerchantImage();
		testString = "testValue";
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(merchantImage.toString());
	}
	
	@Test
	public void testGetProgramCode()
	{
		merchantImage.setProgramCode(testString);
		assertEquals(merchantImage.getProgramCode(), testString);
	}
	
	@Test
	public void testGetImageName()
	{
		merchantImage.setImageName(testString);
		assertEquals(merchantImage.getImageName(), testString);
	}
	
	@Test
	public void testGetTitleEn()
	{
		merchantImage.setTitleEn(testString);
		assertEquals(merchantImage.getTitleEn(), testString);
	}
	
	@Test
	public void testGetTitleAr()
	{
		merchantImage.setTiteAr(testString);
		assertEquals(merchantImage.getTiteAr(), testString);
	}
	
	@Test
	public void testGetDescriptionEn()
	{
		merchantImage.setDescriptionEn(testString);
		assertEquals(merchantImage.getDescriptionEn(), testString);
	}
	
	@Test
	public void testGetDescriptionAr()
	{
		merchantImage.setDescriptionAr(testString);
		assertEquals(merchantImage.getDescriptionAr(), testString);
	}
	
	@Test
	public void testGetImageUrl()
	{
		merchantImage.setImageUrl(testString);
		assertEquals(merchantImage.getImageUrl(), testString);
	}
	
	@Test
	public void testGetImageOrder()
	{
		merchantImage.setImageOrder(testString);
		assertEquals(merchantImage.getImageOrder(), testString);
	}
	
	@Test
	public void testGetImageType()
	{
		merchantImage.setImageType(testString);
		assertEquals(merchantImage.getImageType(), testString);
	}
	
	@Test
	public void testGetDomainId()
	{
		merchantImage.setDomainId(testString);
		assertEquals(merchantImage.getDomainId(), testString);
	}
	
	@Test
	public void testGetDomain()
	{
		merchantImage.setDomain(testString);
		assertEquals(merchantImage.getDomain(), testString);
	}
	
}
