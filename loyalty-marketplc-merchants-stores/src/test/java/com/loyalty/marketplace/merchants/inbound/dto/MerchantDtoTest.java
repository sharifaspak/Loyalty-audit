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

import com.loyalty.marketplace.inbound.dto.ContactPersonDto;
import com.loyalty.marketplace.merchants.outbound.database.entity.Merchant;

@SpringBootTest(classes = MerchantDto.class)
@ActiveProfiles("unittest")
public class MerchantDtoTest {

	private MerchantDto merchantDto;
	private String testString;
	private String originalString;
	
	@Before
	public void setUp(){
		merchantDto = new MerchantDto();
		testString = "Test String";
		originalString = "Original String";
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(merchantDto.toString());
	}
	
	@Test
	public void testHashCode()
	{
		assertNotNull(merchantDto.hashCode());
	}
	
	@Test
	public void testEquals()
	{
		assertTrue(merchantDto.equals(new MerchantDto()));
	}
	
	@Test
	public void testEqualsObject()
	{
		MerchantDto merchant = merchantDto;
		assertTrue(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsObject()
	{
		MerchantDto merchant = null;
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	@SuppressWarnings("unlikely-arg-type")
	public void testNotEqualsObjectClass()
	{
		assertFalse(merchantDto.equals(new Merchant()));
	}
	
	@Test
	public void testNotEqualsNullBarcodeType()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setBarcodeType(null);
		merchant.setBarcodeType(testString);
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsBarcodeType()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setBarcodeType(originalString);
		merchant.setBarcodeType(testString);
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsNullCategory()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setCategory(null);
		merchant.setCategory(testString);
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsCategory()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setCategory(originalString);
		merchant.setCategory(testString);
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsNullContactPersons()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setContactPersons(null);
		merchant.setContactPersons(new ArrayList<ContactPersonDto>());
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsContactPersons()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setContactPersons(new ArrayList<ContactPersonDto>());
		merchant.setContactPersons(null);
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsNullDiscountBillingRates()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setDiscountBillingRates(null);
		merchant.setDiscountBillingRates(new ArrayList<MerchantBillingRateDto>());
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsDiscountBillingRates()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setDiscountBillingRates(new ArrayList<MerchantBillingRateDto>());
		merchant.setDiscountBillingRates(null);
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsNullExternalName()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setExternalName(null);
		merchant.setExternalName(testString);
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsExternalName()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setExternalName(originalString);
		merchant.setExternalName(testString);
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsNullMerchantCode()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setMerchantCode(null);
		merchant.setMerchantCode(testString);
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsMerchantCode()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setMerchantCode(originalString);
		merchant.setMerchantCode(testString);
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsNullMerchantDescAr()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setMerchantDescAr(null);
		merchant.setMerchantDescAr(testString);
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsMerchantDescAr()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setMerchantDescAr(originalString);
		merchant.setMerchantDescAr(testString);
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsNullMerchantDescEn()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setMerchantDescEn(null);
		merchant.setMerchantDescEn(testString);
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsMerchantDescEn()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setMerchantDescEn(originalString);
		merchant.setMerchantDescEn(testString);
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsNullMerchantNameAr()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setMerchantNameAr(null);
		merchant.setMerchantNameAr(testString);
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsMerchantNameAr()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setMerchantNameAr(originalString);
		merchant.setMerchantNameAr(testString);
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsNullMerchantNameEn()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setMerchantNameEn(null);
		merchant.setMerchantNameEn(testString);
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsMerchantNameEn()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setMerchantNameEn(originalString);
		merchant.setMerchantNameEn(testString);
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsNullPartnerCode()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setPartnerCode(null);
		merchant.setPartnerCode(testString);
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsPartnerCode()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setPartnerCode(originalString);
		merchant.setPartnerCode(testString);
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsNullTnCAr()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setTnCAr(null);
		merchant.setTnCAr(testString);
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsTnCAr()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setTnCAr(originalString);
		merchant.setTnCAr(testString);
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsNullTnCEn()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setTnCEn(null);
		merchant.setTnCEn(testString);
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsTnCEn()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setTnCEn(originalString);
		merchant.setTnCEn(testString);
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsNullWhatYouGetAr()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setWhatYouGetAr(null);
		merchant.setWhatYouGetAr(testString);
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsWhatYouGetAr()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setWhatYouGetAr(originalString);
		merchant.setWhatYouGetAr(testString);
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsNullWhatYouGetEn()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setWhatYouGetEn(null);
		merchant.setWhatYouGetEn(testString);
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsWhatYouGetEn()
	{
		MerchantDto merchant = new MerchantDto();
		merchantDto.setWhatYouGetEn(originalString);
		merchant.setWhatYouGetEn(testString);
		assertFalse(merchantDto.equals(merchant));
	}
	
	@Test
	public void testGetMerchantCode()
	{
		merchantDto.setMerchantCode(testString);;
		assertEquals(merchantDto.getMerchantCode(), testString);
	}
	
	@Test
	public void testGetMerchantNameEn()
	{
		merchantDto.setMerchantNameEn(testString);;
		assertEquals(merchantDto.getMerchantNameEn(), testString);
	}
	
	@Test
	public void testGetMerchantNameAr()
	{
		merchantDto.setMerchantNameAr(testString);;
		assertEquals(merchantDto.getMerchantNameAr(), testString);
	}
	
	@Test
	public void testGetExternalName()
	{
		merchantDto.setExternalName(testString);;
		assertEquals(merchantDto.getExternalName(), testString);
	}
	
	@Test
	public void testGetWhatYouGetEn()
	{
		merchantDto.setWhatYouGetEn(testString);;
		assertEquals(merchantDto.getWhatYouGetEn(), testString);
	}
	
	@Test
	public void testGetWhatYouGetAr()
	{
		merchantDto.setWhatYouGetAr(testString);;
		assertEquals(merchantDto.getWhatYouGetAr(), testString);
	}
	
	@Test
	public void testGetTnCEn()
	{
		merchantDto.setTnCEn(testString);;
		assertEquals(merchantDto.getTnCEn(), testString);
	}
	
	@Test
	public void testGetTnCAr()
	{
		merchantDto.setTnCAr(testString);;
		assertEquals(merchantDto.getTnCAr(), testString);
	}
	
	@Test
	public void testGetMerchantDescEn()
	{
		merchantDto.setMerchantDescEn(testString);;
		assertEquals(merchantDto.getMerchantDescEn(), testString);
	}
	
	@Test
	public void testGetMerchantDescAr()
	{
		merchantDto.setMerchantDescAr(testString);;
		assertEquals(merchantDto.getMerchantDescAr(), testString);
	}
	
	@Test
	public void testGetPartnerCode()
	{
		merchantDto.setPartnerCode(testString);;
		assertEquals(merchantDto.getPartnerCode(), testString);
	}
	
	@Test
	public void testGetCategory()
	{
		merchantDto.setCategory(testString);;
		assertEquals(merchantDto.getCategory(), testString);
	}
	
	@Test
	public void testGetBarcodeType()
	{
		merchantDto.setBarcodeType(testString);;
		assertEquals(merchantDto.getBarcodeType(), testString);
	}
	
	@Test
	public void testGetDiscountBillingRates()
	{
		List<MerchantBillingRateDto> billingRates = new ArrayList<MerchantBillingRateDto>();
		merchantDto.setDiscountBillingRates(billingRates);;
		assertEquals(merchantDto.getDiscountBillingRates(), billingRates);
	}
	
	@Test
	public void getContactPersons()
	{
		List<ContactPersonDto> contactPerson = new ArrayList<ContactPersonDto>();
		merchantDto.setContactPersons(contactPerson);;
		assertEquals(merchantDto.getContactPersons(), contactPerson);
	}
	
}
