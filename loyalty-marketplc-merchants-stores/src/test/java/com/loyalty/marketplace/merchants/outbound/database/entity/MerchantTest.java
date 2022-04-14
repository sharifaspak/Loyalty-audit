package com.loyalty.marketplace.merchants.outbound.database.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.outbound.database.entity.Category;

@SpringBootTest(classes = Merchant.class)
@ActiveProfiles("unittest")
public class MerchantTest {

	private Merchant merchant;
	private String testString;
	private Date testDate;
	
	@Before
	public void setUp(){
		merchant = new Merchant();
		testString = "testValue";
		testDate = new Date();
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(merchant.toString());
	}
	
	@Test
	public void testGetImageUrl()
	{
		merchant.setImageUrl(testString);
		assertEquals(merchant.getImageUrl(), testString);
	}
	
	@Test
	public void testGetId()
	{
		merchant.setId(testString);
		assertEquals(merchant.getId(), testString);
	}
	
	@Test
	public void testGetProgramCode()
	{
		merchant.setProgramCode(testString);
		assertEquals(merchant.getProgramCode(), testString);
	}
	
	@Test
	public void testGetMerchantCode()
	{
		merchant.setMerchantCode(testString);
		assertEquals(merchant.getMerchantCode(), testString);
	}
	
	@Test
	public void testGetMerchantName()
	{
		MerchantName merchantName = new MerchantName();
		merchantName.setMerchantNameAr(testString);
		merchantName.setMerchantNameEn(testString);
		merchant.setMerchantName(merchantName);
		assertEquals(merchant.getMerchantName(), merchantName);
	}
	
	@Test
	public void testGetPartnerCode()
	{
		merchant.setPartnerCode(testString);
		assertEquals(merchant.getPartnerCode(), testString);
	}
	
	@Test
	public void testGetCategory()
	{
		Category category = new Category();
		category.setCategoryId(testString);
		merchant.setCategory(category);
		assertEquals(merchant.getCategory(), category);
	}
	
	@Test
	public void testGetBarcodeType()
	{
		Barcode barcode = new Barcode();
		barcode.setId(testString);
		barcode.setDescription(testString);
		merchant.setBarcodeType(barcode);
		assertEquals(merchant.getBarcodeType(), barcode);
	}

	@Test
	public void testGetStatus()
	{
		merchant.setStatus(testString);
		assertEquals(merchant.getStatus(), testString);
	}
	
	@Test
	public void testGetWhatYouGet()
	{
		WhatYouGet whatYouGet = new WhatYouGet();
		whatYouGet.setWhatYouGetAr(testString);
		whatYouGet.setWhatYouGetEn(testString);
		merchant.setWhatYouGet(whatYouGet);
		assertEquals(merchant.getWhatYouGet(), whatYouGet);
	}
	
	@Test
	public void testGetTnC()
	{
		TAndC tAndC = new TAndC();
		tAndC.setTnCEn(testString);
		tAndC.setTnCAr(testString);
		merchant.setTnC(tAndC);
		assertEquals(merchant.getTnC(), tAndC);
	}
	
	@Test
	public void testGetMerchantDescription()
	{
		MerchantDescription merchantDescription = new MerchantDescription();
		merchantDescription.setMerchantDescAr(testString);
		merchantDescription.setMerchantDescEn(testString);
		merchant.setMerchantDescription(merchantDescription);
		assertEquals(merchant.getMerchantDescription(), merchantDescription);
	}
	
	@Test
	public void testGetExternalName()
	{
		merchant.setExternalName(testString);
		assertEquals(merchant.getExternalName(), testString);
	}
	
	@Test
	public void testGetUsrCreated()
	{
		merchant.setUsrCreated(testString);
		assertEquals(merchant.getUsrCreated(), testString);
	}
	
	@Test
	public void testGetUsrUpdated()
	{
		merchant.setUsrUpdated(testString);
		assertEquals(merchant.getUsrUpdated(), testString);
	}
	
	@Test
	public void testGetDtCreated()
	{ 
		merchant.setDtCreated(testDate);
		assertEquals(merchant.getDtCreated(), testDate);
	}
	
	@Test
	public void testGetDtUpdated()
	{ 
		merchant.setDtUpdated(testDate);
		assertEquals(merchant.getDtUpdated(), testDate);
	}
	
	@Test
	public void testGetContactPersons()
	{
		List<ContactPerson> contactPersons = new ArrayList<ContactPerson>();
		merchant.setContactPersons(contactPersons);
		assertEquals(merchant.getContactPersons(), contactPersons);
	}
	
	@Test
	public void testGetBillingRates()
	{
		List<MerchantBillingRate> billingRate = new ArrayList<MerchantBillingRate>();
		merchant.setBillingRates(billingRate);
		assertEquals(merchant.getBillingRates(), billingRate);
	}
	
}
