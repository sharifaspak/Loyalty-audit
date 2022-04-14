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
import com.loyalty.marketplace.stores.outbound.dto.StoreResult;

@SpringBootTest(classes = MerchantStoreOfferResultTest.class)
@ActiveProfiles("unittest")
public class MerchantStoreOfferResultTest {


	private MerchantStoreOfferResult storeOfferResultDto;
	private String testString;
	private String originalString;
	
	@Before
	public void setUp(){
		storeOfferResultDto = new MerchantStoreOfferResult();
		testString = "Test String";
		originalString = "Original String";
	}

	@Test
	public void testToString()
	{
		assertNotNull(storeOfferResultDto.toString());
	}
	
	@Test
	public void testHashCode()
	{
		assertNotNull(storeOfferResultDto.hashCode());
	}
	
	@Test
	public void testEquals()
	{
		assertTrue(storeOfferResultDto.equals(new MerchantStoreOfferResult()));
	}
	
	@Test
	public void testEqualsObject()
	{
		MerchantStoreOfferResult merchantStoreOfferResult = storeOfferResultDto;
		assertTrue(storeOfferResultDto.equals(merchantStoreOfferResult));
	}
	
	@Test
	public void testNotEqualsObject()
	{
		MerchantStoreOfferResult merchantStoreOfferResult = null;
		assertFalse(storeOfferResultDto.equals(merchantStoreOfferResult));
	}
	
	@Test
	@SuppressWarnings("unlikely-arg-type")
	public void testNotEqualsObjectClass()
	{
		assertFalse(storeOfferResultDto.equals(new Merchant()));
	}
	
	@Test
	public void testNotEqualsNullMerchantCode()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setMerchantCode(null);
		merchantResult.setMerchantCode(testString);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsMerchantCode()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setMerchantCode(originalString);
		merchantResult.setMerchantCode(testString);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsNullMerchantNameEn()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setMerchantNameEn(null);
		merchantResult.setMerchantNameEn(testString);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsMerchantNameEn()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setMerchantNameEn(originalString);
		merchantResult.setMerchantNameEn(testString);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsNullMerchantNameAr()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setMerchantNameAr(null);
		merchantResult.setMerchantNameAr(testString);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsMerchantNameAr()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setMerchantNameAr(originalString);
		merchantResult.setMerchantNameAr(testString);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsNullExternalName()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setExternalName(null);
		merchantResult.setExternalName(testString);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsExternalName()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setExternalName(originalString);
		merchantResult.setExternalName(testString);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsNullWhatYouGetEn()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setWhatYouGetEn(null);
		merchantResult.setWhatYouGetEn(testString);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsWhatYouGetEn()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setWhatYouGetEn(originalString);
		merchantResult.setWhatYouGetEn(testString);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsNullWhatYouGetAr()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setWhatYouGetAr(null);
		merchantResult.setWhatYouGetAr(testString);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsWhatYouGetAr()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setWhatYouGetAr(originalString);
		merchantResult.setWhatYouGetAr(testString);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsNullTnCEn()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setTnCEn(null);
		merchantResult.setTnCEn(testString);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsTnCEn()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setTnCEn(originalString);
		merchantResult.setTnCEn(testString);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsNullTnCAr()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setTnCAr(null);
		merchantResult.setTnCAr(testString);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsTnCAr()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setTnCAr(originalString);
		merchantResult.setTnCAr(testString);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsNullMerchantDescEn()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setMerchantDescEn(null);
		merchantResult.setMerchantDescEn(testString);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsMerchantDescEn()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setMerchantDescEn(originalString);
		merchantResult.setMerchantDescEn(testString);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsNullMerchantDescAr()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setMerchantDescAr(null);
		merchantResult.setMerchantDescAr(testString);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsMerchantDescAr()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setMerchantDescAr(originalString);
		merchantResult.setMerchantDescAr(testString);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsNullPartnerCode()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setPartnerCode(null);
		merchantResult.setPartnerCode(testString);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsPartnerCode()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setPartnerCode(originalString);
		merchantResult.setPartnerCode(testString);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsNullCategory()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setCategory(null);
		merchantResult.setCategory(testString);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsCategory()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setCategory(originalString);
		merchantResult.setCategory(testString);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsNullBarcodeType()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setBarcodeType(null);
		merchantResult.setBarcodeType(testString);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsBarcodeType()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setBarcodeType(originalString);
		merchantResult.setBarcodeType(testString);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsNullStatus()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setStatus(null);
		merchantResult.setStatus(testString);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsStatus()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setStatus(originalString);
		merchantResult.setStatus(testString);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsNullContactPersons()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setContactPersons(null);
		merchantResult.setContactPersons(new ArrayList<ContactPersonDto>());
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsContactPersons()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setContactPersons(new ArrayList<ContactPersonDto>());
		merchantResult.setContactPersons(null);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsNullBillingRates()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setBillingRates(null);
		merchantResult.setBillingRates(new ArrayList<MerchantBillingRateDto>());
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsBillingRates()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setBillingRates(new ArrayList<MerchantBillingRateDto>());
		merchantResult.setBillingRates(null);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}

	@Test
	public void testNotEqualsNullMerchantOffers()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setOffers(null);
		merchantResult.setOffers(new ArrayList<MerchantOffers>());
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsMerchantOffers()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setOffers(new ArrayList<MerchantOffers>());
		merchantResult.setOffers(null);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsNullStores()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setStores(null);
		merchantResult.setStores(new ArrayList<StoreResult>());
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testNotEqualsStores()
	{
		MerchantStoreOfferResult merchantResult = new MerchantStoreOfferResult();
		storeOfferResultDto.setStores(new ArrayList<StoreResult>());
		merchantResult.setStores(null);
		assertFalse(storeOfferResultDto.equals(merchantResult));
	}
	
	@Test
	public void testGetMerchantCode()
	{
		storeOfferResultDto.setMerchantCode(testString);;
		assertEquals(storeOfferResultDto.getMerchantCode(), testString);
	}
	
	@Test
	public void testGetMerchantNameEn()
	{
		storeOfferResultDto.setMerchantNameEn(testString);;
		assertEquals(storeOfferResultDto.getMerchantNameEn(), testString);
	}
	
	@Test
	public void testGetMerchantNameAr()
	{
		storeOfferResultDto.setMerchantNameAr(testString);;
		assertEquals(storeOfferResultDto.getMerchantNameAr(), testString);
	}
	
	@Test
	public void testGetExternalName()
	{
		storeOfferResultDto.setExternalName(testString);;
		assertEquals(storeOfferResultDto.getExternalName(), testString);
	}
	
	@Test
	public void testGetWhatYouGetEn()
	{
		storeOfferResultDto.setWhatYouGetEn(testString);;
		assertEquals(storeOfferResultDto.getWhatYouGetEn(), testString);
	}
	
	@Test
	public void testGetWhatYouGetAr()
	{
		storeOfferResultDto.setWhatYouGetAr(testString);;
		assertEquals(storeOfferResultDto.getWhatYouGetAr(), testString);
	}
	
	@Test
	public void testGetTnCEn()
	{
		storeOfferResultDto.setTnCEn(testString);;
		assertEquals(storeOfferResultDto.getTnCEn(), testString);
	}
	
	@Test
	public void testGetTnCAr()
	{
		storeOfferResultDto.setTnCAr(testString);;
		assertEquals(storeOfferResultDto.getTnCAr(), testString);
	}
	
	@Test
	public void testGetMerchantDescEn()
	{
		storeOfferResultDto.setMerchantDescEn(testString);;
		assertEquals(storeOfferResultDto.getMerchantDescEn(), testString);
	}
	
	@Test
	public void testGetMerchantDescAr()
	{
		storeOfferResultDto.setMerchantDescAr(testString);;
		assertEquals(storeOfferResultDto.getMerchantDescAr(), testString);
	}
	
	@Test
	public void testGetPartnerCode()
	{
		storeOfferResultDto.setPartnerCode(testString);;
		assertEquals(storeOfferResultDto.getPartnerCode(), testString);
	}
	
	@Test
	public void testGetCategory()
	{
		storeOfferResultDto.setCategory(testString);;
		assertEquals(storeOfferResultDto.getCategory(), testString);
	}
	
	@Test
	public void testGetBarcodeType()
	{
		storeOfferResultDto.setBarcodeType(testString);;
		assertEquals(storeOfferResultDto.getBarcodeType(), testString);
	}
	
	@Test
	public void testGetStatus()
	{
		storeOfferResultDto.setStatus(testString);;
		assertEquals(storeOfferResultDto.getStatus(), testString);
	}
	
	@Test
	public void testGetBillingRates()
	{
		List<MerchantBillingRateDto> billingRateDto = new ArrayList<MerchantBillingRateDto>();
		storeOfferResultDto.setBillingRates(billingRateDto);
		assertEquals(storeOfferResultDto.getBillingRates(), billingRateDto);
	}
	
	@Test
	public void testGetContactPersons()
	{
		List<ContactPersonDto> contactPersons = new ArrayList<ContactPersonDto>();
		storeOfferResultDto.setContactPersons(contactPersons);
		assertEquals(storeOfferResultDto.getContactPersons(), contactPersons);
	}
	
	@Test
	public void testGetMerchantOffers()
	{
		List<MerchantOffers> merchantOffers = new ArrayList<MerchantOffers>();
		storeOfferResultDto.setOffers(merchantOffers);
		assertEquals(storeOfferResultDto.getOffers(), merchantOffers);
	}
	
	@Test
	public void testGetStores()
	{
		List<StoreResult> stores = new ArrayList<StoreResult>();
		storeOfferResultDto.setStores(stores);
		assertEquals(storeOfferResultDto.getStores(), stores);
	}
	
}
