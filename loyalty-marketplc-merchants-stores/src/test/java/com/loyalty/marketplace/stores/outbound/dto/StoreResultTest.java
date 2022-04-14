package com.loyalty.marketplace.stores.outbound.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.inbound.dto.ContactPersonDto;

@SpringBootTest(classes = StoreResult.class)
@ActiveProfiles("unittest")
public class StoreResultTest {

	private StoreResult storeResult;
	private String testString;
	
	@Before
	public void setUp(){
		storeResult = new StoreResult();
		testString = "Test String";
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(storeResult.toString());
	}
	
	@Test
	public void testGetStoreCode()
	{
		storeResult.setStoreCode(testString);;
		assertEquals(storeResult.getStoreCode(), testString);
	}
	
	@Test
	public void testGetMerchantName()
	{
		storeResult.setMerchantName(testString);;
		assertEquals(storeResult.getMerchantName(), testString);
	}
	
	@Test
	public void testGetStatus()
	{
		storeResult.setStatus(testString);;
		assertEquals(storeResult.getStatus(), testString);
	}
	
	@Test
	public void testGetDescriptionAr()
	{
		storeResult.setDescriptionAr(testString);;
		assertEquals(storeResult.getDescriptionAr(), testString);
	}
	
	@Test
	public void testGetDescription()
	{
		storeResult.setDescription(testString);;
		assertEquals(storeResult.getDescription(), testString);
	}
	
	@Test
	public void testGetAddress()
	{
		storeResult.setAddress(testString);;
		assertEquals(storeResult.getAddress(), testString);
	}
	
	@Test
	public void testGetAddressAr()
	{
		storeResult.setAddressAr(testString);;
		assertEquals(storeResult.getAddressAr(), testString);
	}
	
	@Test
	public void testGetLongitude()
	{
		storeResult.setLongitude(testString);;
		assertEquals(storeResult.getLongitude(), testString);
	}
	
	@Test
	public void testGetLatitude()
	{
		storeResult.setLatitude(testString);;
		assertEquals(storeResult.getLatitude(), testString);
	}
	
	@Test
	public void testGetMerchantCode()
	{
		storeResult.setMerchantCode(testString);;
		assertEquals(storeResult.getMerchantCode(), testString);
	}
	
	@Test
	public void testGetContactPersons()
	{
		List<ContactPersonDto> contactPersons = new ArrayList<ContactPersonDto>();
		storeResult.setContactPersons(contactPersons);;
		assertEquals(storeResult.getContactPersons(), contactPersons);
	}
	
}
