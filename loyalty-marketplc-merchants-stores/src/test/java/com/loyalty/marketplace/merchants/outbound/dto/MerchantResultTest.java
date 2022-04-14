package com.loyalty.marketplace.merchants.outbound.dto;

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

import com.loyalty.marketplace.inbound.dto.ContactPersonDto;
import com.loyalty.marketplace.merchants.inbound.dto.MerchantBillingRateDto;
import com.loyalty.marketplace.merchants.outbound.database.entity.Merchant;

@SpringBootTest(classes = MerchantResultTest.class)
@ActiveProfiles("unittest")
public class MerchantResultTest {

	private MerchantResult merchantResultDto;
	private String testString;
	private String originalString;
	
	@Before
	public void setUp(){
		merchantResultDto = new MerchantResult();
		testString = "Test String";
		originalString = "Original String";
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(merchantResultDto.toString());
	}
	
	@Test
	public void testHashCode()
	{
		assertNotNull(merchantResultDto.hashCode());
	}
	
	@Test
	public void testEquals()
	{
		assertTrue(merchantResultDto.equals(new MerchantResult()));
	}
	
	@Test
	public void testEqualsObject()
	{
		MerchantResult merchantResult = merchantResultDto;
		assertTrue(merchantResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsObject()
	{
		MerchantResult merchantResult = null;
		assertFalse(merchantResultDto.equals(merchantResult));
	}
	
	@Test
	@SuppressWarnings("unlikely-arg-type")
	public void testNotEqualsObjectClass()
	{
		assertFalse(merchantResultDto.equals(new Merchant()));
	}
	
	@Test
	public void testNotEqualsNullMerchantCode()
	{
		MerchantResult merchantResult = new MerchantResult();
		merchantResultDto.setMerchantCode(null);
		merchantResult.setMerchantCode(testString);
		assertFalse(merchantResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsMerchantCode()
	{
		MerchantResult merchant = new MerchantResult();
		merchantResultDto.setMerchantCode(originalString);
		merchant.setMerchantCode(testString);
		assertFalse(merchantResultDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsNullMerchantNameEn()
	{
		MerchantResult merchantResult = new MerchantResult();
		merchantResultDto.setMerchantNameEn(null);
		merchantResult.setMerchantNameEn(testString);
		assertFalse(merchantResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsMerchantNameEn()
	{
		MerchantResult merchant = new MerchantResult();
		merchantResultDto.setMerchantNameEn(originalString);
		merchant.setMerchantNameEn(testString);
		assertFalse(merchantResultDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsNullMerchantNameAr()
	{
		MerchantResult merchantResult = new MerchantResult();
		merchantResultDto.setMerchantNameAr(null);
		merchantResult.setMerchantNameAr(testString);
		assertFalse(merchantResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsMerchantNameAr()
	{
		MerchantResult merchant = new MerchantResult();
		merchantResultDto.setMerchantNameAr(originalString);
		merchant.setMerchantNameAr(testString);
		assertFalse(merchantResultDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsNullExternalName()
	{
		MerchantResult merchantResult = new MerchantResult();
		merchantResultDto.setExternalName(null);
		merchantResult.setExternalName(testString);
		assertFalse(merchantResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsExternalName()
	{
		MerchantResult merchant = new MerchantResult();
		merchantResultDto.setExternalName(originalString);
		merchant.setExternalName(testString);
		assertFalse(merchantResultDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsNullWhatYouGetEn()
	{
		MerchantResult merchantResult = new MerchantResult();
		merchantResultDto.setWhatYouGetEn(null);
		merchantResult.setWhatYouGetEn(testString);
		assertFalse(merchantResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsWhatYouGetEn()
	{
		MerchantResult merchant = new MerchantResult();
		merchantResultDto.setWhatYouGetEn(originalString);
		merchant.setWhatYouGetEn(testString);
		assertFalse(merchantResultDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsNullWhatYouGetAr()
	{
		MerchantResult merchantResult = new MerchantResult();
		merchantResultDto.setWhatYouGetAr(null);
		merchantResult.setWhatYouGetAr(testString);
		assertFalse(merchantResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsWhatYouGetAr()
	{
		MerchantResult merchant = new MerchantResult();
		merchantResultDto.setWhatYouGetAr(originalString);
		merchant.setWhatYouGetAr(testString);
		assertFalse(merchantResultDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsNullTnCEn()
	{
		MerchantResult merchantResult = new MerchantResult();
		merchantResultDto.setTnCEn(null);
		merchantResult.setTnCEn(testString);
		assertFalse(merchantResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsTnCEn()
	{
		MerchantResult merchant = new MerchantResult();
		merchantResultDto.setTnCEn(originalString);
		merchant.setTnCEn(testString);
		assertFalse(merchantResultDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsNullTnCAr()
	{
		MerchantResult merchantResult = new MerchantResult();
		merchantResultDto.setTnCAr(null);
		merchantResult.setTnCAr(testString);
		assertFalse(merchantResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsTnCAr()
	{
		MerchantResult merchant = new MerchantResult();
		merchantResultDto.setTnCAr(originalString);
		merchant.setTnCAr(testString);
		assertFalse(merchantResultDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsNullMerchantDescEn()
	{
		MerchantResult merchantResult = new MerchantResult();
		merchantResultDto.setMerchantDescEn(null);
		merchantResult.setMerchantDescEn(testString);
		assertFalse(merchantResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsMerchantDescEn()
	{
		MerchantResult merchant = new MerchantResult();
		merchantResultDto.setMerchantDescEn(originalString);
		merchant.setMerchantDescEn(testString);
		assertFalse(merchantResultDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsNullMerchantDescAr()
	{
		MerchantResult merchantResult = new MerchantResult();
		merchantResultDto.setMerchantDescAr(null);
		merchantResult.setMerchantDescAr(testString);
		assertFalse(merchantResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsMerchantDescAr()
	{
		MerchantResult merchant = new MerchantResult();
		merchantResultDto.setMerchantDescAr(originalString);
		merchant.setMerchantDescAr(testString);
		assertFalse(merchantResultDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsNullPartnerCode()
	{
		MerchantResult merchantResult = new MerchantResult();
		merchantResultDto.setPartnerCode(null);
		merchantResult.setPartnerCode(testString);
		assertFalse(merchantResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsPartnerCode()
	{
		MerchantResult merchant = new MerchantResult();
		merchantResultDto.setPartnerCode(originalString);
		merchant.setPartnerCode(testString);
		assertFalse(merchantResultDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsNullCategory()
	{
		MerchantResult merchantResult = new MerchantResult();
		merchantResultDto.setCategory(null);
		merchantResult.setCategory(testString);
		assertFalse(merchantResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsCategory()
	{
		MerchantResult merchant = new MerchantResult();
		merchantResultDto.setCategory(originalString);
		merchant.setCategory(testString);
		assertFalse(merchantResultDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsNullBarcodeType()
	{
		MerchantResult merchantResult = new MerchantResult();
		merchantResultDto.setBarcodeType(null);
		merchantResult.setBarcodeType(testString);
		assertFalse(merchantResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsBarcodeType()
	{
		MerchantResult merchant = new MerchantResult();
		merchantResultDto.setBarcodeType(originalString);
		merchant.setBarcodeType(testString);
		assertFalse(merchantResultDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsNullStatus()
	{
		MerchantResult merchantResult = new MerchantResult();
		merchantResultDto.setStatus(null);
		merchantResult.setStatus(testString);
		assertFalse(merchantResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsStatus()
	{
		MerchantResult merchant = new MerchantResult();
		merchantResultDto.setStatus(originalString);
		merchant.setStatus(testString);
		assertFalse(merchantResultDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsNullContactPersons()
	{
		MerchantResult merchant = new MerchantResult();
		merchantResultDto.setContactPersons(null);
		merchant.setContactPersons(new ArrayList<ContactPersonDto>());
		assertFalse(merchantResultDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsContactPersons()
	{
		MerchantResult merchant = new MerchantResult();
		merchantResultDto.setContactPersons(new ArrayList<ContactPersonDto>());
		merchant.setContactPersons(null);
		assertFalse(merchantResultDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsNullBillingRates()
	{
		MerchantResult merchant = new MerchantResult();
		merchantResultDto.setBillingRates(null);
		merchant.setBillingRates(new ArrayList<MerchantBillingRateDto>());
		assertFalse(merchantResultDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsBillingRates()
	{
		MerchantResult merchant = new MerchantResult();
		merchantResultDto.setBillingRates(new ArrayList<MerchantBillingRateDto>());
		merchant.setBillingRates(null);
		assertFalse(merchantResultDto.equals(merchant));
	}
	
	@Test
	public void testGetImageUrl()
	{
		merchantResultDto.setImageUrl(testString);;
		assertEquals(merchantResultDto.getImageUrl(), testString);
	}
	
	@Test
	public void testGetMerchantCode()
	{
		merchantResultDto.setMerchantCode(testString);;
		assertEquals(merchantResultDto.getMerchantCode(), testString);
	}
	
	@Test
	public void testGetMerchantNameEn()
	{
		merchantResultDto.setMerchantNameEn(testString);;
		assertEquals(merchantResultDto.getMerchantNameEn(), testString);
	}
	
	@Test
	public void testGetMerchantNameAr()
	{
		merchantResultDto.setMerchantNameAr(testString);;
		assertEquals(merchantResultDto.getMerchantNameAr(), testString);
	}
	
	@Test
	public void testGetExternalName()
	{
		merchantResultDto.setExternalName(testString);;
		assertEquals(merchantResultDto.getExternalName(), testString);
	}
	
	@Test
	public void testGetWhatYouGetEn()
	{
		merchantResultDto.setWhatYouGetEn(testString);;
		assertEquals(merchantResultDto.getWhatYouGetEn(), testString);
	}
	
	@Test
	public void testGetWhatYouGetAr()
	{
		merchantResultDto.setWhatYouGetAr(testString);;
		assertEquals(merchantResultDto.getWhatYouGetAr(), testString);
	}
	
	@Test
	public void testGetTnCEn()
	{
		merchantResultDto.setTnCEn(testString);;
		assertEquals(merchantResultDto.getTnCEn(), testString);
	}
	
	@Test
	public void testGetTnCAr()
	{
		merchantResultDto.setTnCAr(testString);;
		assertEquals(merchantResultDto.getTnCAr(), testString);
	}
	
	@Test
	public void testGetMerchantDescEn()
	{
		merchantResultDto.setMerchantDescEn(testString);;
		assertEquals(merchantResultDto.getMerchantDescEn(), testString);
	}
	
	@Test
	public void testGetMerchantDescAr()
	{
		merchantResultDto.setMerchantDescAr(testString);;
		assertEquals(merchantResultDto.getMerchantDescAr(), testString);
	}
	
	@Test
	public void testGetPartnerCode()
	{
		merchantResultDto.setPartnerCode(testString);;
		assertEquals(merchantResultDto.getPartnerCode(), testString);
	}
	
	@Test
	public void testGetCategory()
	{
		merchantResultDto.setCategory(testString);;
		assertEquals(merchantResultDto.getCategory(), testString);
	}
	
	@Test
	public void testGetBarcodeType()
	{
		merchantResultDto.setBarcodeType(testString);;
		assertEquals(merchantResultDto.getBarcodeType(), testString);
	}
	
	@Test
	public void testGetStatus()
	{
		merchantResultDto.setStatus(testString);;
		assertEquals(merchantResultDto.getStatus(), testString);
	}
	
	@Test
	public void testGetBillingRates()
	{
		List<MerchantBillingRateDto> billingRateDto = new ArrayList<MerchantBillingRateDto>();
		merchantResultDto.setBillingRates(billingRateDto);
		assertEquals(merchantResultDto.getBillingRates(), billingRateDto);
	}
	
	@Test
	public void testGetContactPersons()
	{
		List<ContactPersonDto> contactPersons = new ArrayList<ContactPersonDto>();
		merchantResultDto.setContactPersons(contactPersons);
		assertEquals(merchantResultDto.getContactPersons(), contactPersons);
	}
	
}
