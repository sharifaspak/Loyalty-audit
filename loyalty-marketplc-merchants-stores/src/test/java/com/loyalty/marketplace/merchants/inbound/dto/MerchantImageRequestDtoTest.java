package com.loyalty.marketplace.merchants.inbound.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.merchants.outbound.database.entity.MerchantImage;

@SpringBootTest(classes = MerchantImageRequestDto.class)
@ActiveProfiles("unittest")
public class MerchantImageRequestDtoTest {

	private MerchantImageRequestDto merchantImageRequestDto;
	private String testString;
	
	@Before
	public void setUp(){
		merchantImageRequestDto = new MerchantImageRequestDto();
		testString = "Test String";
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(merchantImageRequestDto.toString());
	}
	
	@Test
	public void testHashCode()
	{
		assertNotNull(merchantImageRequestDto.hashCode());
	}
	
	@Test
	public void testEqualsObject()
	{
		MerchantImageRequestDto merchantImageRequest = merchantImageRequestDto;
		assertTrue(merchantImageRequestDto.equals(merchantImageRequest));
	}
	
	@Test
	public void testNotEqualsObject()
	{
		MerchantImageRequestDto merchantImageRequest = null;
		assertFalse(merchantImageRequestDto.equals(merchantImageRequest));
	}
	
	@Test
	@SuppressWarnings("unlikely-arg-type")
	public void testNotEqualsObjectClass()
	{
		assertFalse(merchantImageRequestDto.equals(new MerchantImage()));
	}
	
	@Test
	public void testGetProgramCode()
	{
		merchantImageRequestDto.setProgramCode(testString);;
		assertEquals(merchantImageRequestDto.getProgramCode(), testString);
	}
	
	@Test
	public void testGetImageName()
	{
		merchantImageRequestDto.setImageName(testString);;
		assertEquals(merchantImageRequestDto.getImageName(), testString);
	}
	
	@Test
	public void testGetTitleEn()
	{
		merchantImageRequestDto.setTitleEn(testString);;
		assertEquals(merchantImageRequestDto.getTitleEn(), testString);
	}
	
	@Test
	public void testGetTitleAr()
	{
		merchantImageRequestDto.setTiteAr(testString);;
		assertEquals(merchantImageRequestDto.getTiteAr(), testString);
	}
	
	@Test
	public void testGetDescriptionEn()
	{
		merchantImageRequestDto.setDescriptionEn(testString);;
		assertEquals(merchantImageRequestDto.getDescriptionEn(), testString);
	}
	
	@Test
	public void testGetDescriptionAr()
	{
		merchantImageRequestDto.setDescriptionAr(testString);;
		assertEquals(merchantImageRequestDto.getDescriptionAr(), testString);
	}
	
	@Test
	public void testGetImagePath()
	{
		merchantImageRequestDto.setImagePath(testString);;
		assertEquals(merchantImageRequestDto.getImagePath(), testString);
	}
	
	@Test
	public void testGetImageOrder()
	{
		merchantImageRequestDto.setImageOrder(testString);;
		assertEquals(merchantImageRequestDto.getImageOrder(), testString);
	}
	
	@Test
	public void testGetImageType()
	{
		merchantImageRequestDto.setImageType(testString);;
		assertEquals(merchantImageRequestDto.getImageType(), testString);
	}
	
	@Test
	public void testGetDomainId()
	{
		merchantImageRequestDto.setDomainId(testString);;
		assertEquals(merchantImageRequestDto.getDomainId(), testString);
	}
	
	@Test
	public void testGetDomain()
	{
		merchantImageRequestDto.setDomain(testString);;
		assertEquals(merchantImageRequestDto.getDomain(), testString);
	}
	
}
