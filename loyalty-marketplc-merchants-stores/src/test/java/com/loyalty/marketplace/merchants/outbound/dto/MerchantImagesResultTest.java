package com.loyalty.marketplace.merchants.outbound.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = MerchantImagesResultTest.class)
@ActiveProfiles("unittest")
public class MerchantImagesResultTest {

	private MerchantImagesResult merchantImagesResult;
	private String testString;
	
	@Before
	public void setUp(){
		merchantImagesResult = new MerchantImagesResult();
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(merchantImagesResult.toString());
	}
	
	@Test
	public void testGetProgramCode()
	{
		testString = "Test String";
		merchantImagesResult.setProgramCode(testString);;
		assertEquals(merchantImagesResult.getProgramCode(), testString);
	}
	
	@Test
	public void testGetImageName()
	{
		merchantImagesResult.setImageName(testString);;
		assertEquals(merchantImagesResult.getImageName(), testString);
	}
	
	@Test
	public void testGetTitleEn()
	{
		merchantImagesResult.setTitleEn(testString);;
		assertEquals(merchantImagesResult.getTitleEn(), testString);
	}
	
	@Test
	public void testGetTitleAr()
	{
		merchantImagesResult.setTiteAr(testString);;
		assertEquals(merchantImagesResult.getTiteAr(), testString);
	}
	
	@Test
	public void testGetDescriptionEn()
	{
		merchantImagesResult.setDescriptionEn(testString);;
		assertEquals(merchantImagesResult.getDescriptionEn(), testString);
	}
	
	@Test
	public void testGetDescriptionAr()
	{
		merchantImagesResult.setDescriptionAr(testString);;
		assertEquals(merchantImagesResult.getDescriptionAr(), testString);
	}
	
	@Test
	public void testGetImageUrl()
	{
		merchantImagesResult.setImageUrl(testString);;
		assertEquals(merchantImagesResult.getImageUrl(), testString);
	}
	
	@Test
	public void testGetImageOrder()
	{
		merchantImagesResult.setImageOrder(testString);;
		assertEquals(merchantImagesResult.getImageOrder(), testString);
	}
	
	@Test
	public void testGetImageType()
	{
		merchantImagesResult.setImageType(testString);;
		assertEquals(merchantImagesResult.getImageType(), testString);
	}
	
	@Test
	public void testGetDomainId()
	{
		merchantImagesResult.setDomainId(testString);;
		assertEquals(merchantImagesResult.getDomainId(), testString);
	}
	
	@Test
	public void testGetDomain()
	{
		merchantImagesResult.setDomain(testString);;
		assertEquals(merchantImagesResult.getDomain(), testString);
	}
	
}
