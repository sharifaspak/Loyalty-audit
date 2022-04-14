package com.loyalty.marketplace.stores.inbound.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.inbound.dto.ContactPersonDto;

@SpringBootTest(classes = StoreDto.class)
@ActiveProfiles("unittest")
public class StoreDtoTest {

	private StoreDto storeDto;
	private String testString;
	
	@Before
	public void setUp(){
		storeDto = new StoreDto();
		testString = "Test String";
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(storeDto.toString());
	}
	
	@Test
	public void testGetStoreCode()
	{
		storeDto.setStoreCode(testString);;
		assertEquals(storeDto.getStoreCode(), testString);
	}
	
	@Test
	public void testGetDescriptionAr()
	{
		storeDto.setDescriptionAr(testString);;
		assertEquals(storeDto.getDescriptionAr(), testString);
	}
	
	@Test
	public void testGetDescriptionEn()
	{
		storeDto.setDescriptionEn(testString);;
		assertEquals(storeDto.getDescription(), testString);
	}
	
	@Test
	public void testGetAddress()
	{
		storeDto.setAddress(testString);;
		assertEquals(storeDto.getAddress(), testString);
	}
	
	@Test
	public void testGetAddressAr()
	{
		storeDto.setAddressAr(testString);;
		assertEquals(storeDto.getAddressAr(), testString);
	}
	
	@Test
	public void testGetLongitude()
	{
		storeDto.setLongitude(testString);;
		assertEquals(storeDto.getLongitude(), testString);
	}
	
	@Test
	public void testGetLatitude()
	{
		storeDto.setLatitude(testString);;
		assertEquals(storeDto.getLatitude(), testString);
	}
	
	@Test
	public void testGetMerchantCode()
	{
		storeDto.setMerchantCode(testString);;
		assertEquals(storeDto.getMerchantCode(), testString);
	}
	
	@Test
	public void testGetContactPersons()
	{
		List<ContactPersonDto> contactPersons = new ArrayList<ContactPersonDto>();
		storeDto.setContactPersons(contactPersons);
		assertEquals(storeDto.getContactPersons(), contactPersons);
	}
	
}
