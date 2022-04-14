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
import com.loyalty.marketplace.stores.outbound.database.entity.ContactPerson;

@SpringBootTest(classes = MarketPlaceContactPersonDto.class)
@ActiveProfiles("unittest")
public class MarketPlaceContactPersonDtoTest {

	private MarketPlaceContactPersonDto contactPersonDto;
	private String testString;
	private String originalString;
	
	@Before
	public void setUp(){
		contactPersonDto = new MarketPlaceContactPersonDto();
		testString = "Test String";
		originalString = "Original String";
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(contactPersonDto.toString());
	}
	
	@Test
	public void testHashCode()
	{
		assertNotNull(contactPersonDto.hashCode());
	}
	
	@Test
	public void testEquals()
	{
		assertTrue(contactPersonDto.equals(new MarketPlaceContactPersonDto()));
	}
	
	@Test
	public void testEqualsObject()
	{
		MarketPlaceContactPersonDto merchant = contactPersonDto;
		assertTrue(contactPersonDto.equals(merchant));
	}
	
	@Test
	public void testNotEqualsObject()
	{
		MarketPlaceContactPersonDto merchant = null;
		assertFalse(contactPersonDto.equals(merchant));
	}
	
	@Test
	@SuppressWarnings("unlikely-arg-type")
	public void testNotEqualsObjectClass()
	{
		assertFalse(contactPersonDto.equals(new ContactPerson()));
	}
	
	@Test
	public void testNotEqualsNullMerchantCode()
	{
		MarketPlaceContactPersonDto contactPerson = new MarketPlaceContactPersonDto();
		contactPersonDto.setMerchantCode(null);
		contactPerson.setMerchantCode(testString);
		assertFalse(contactPersonDto.equals(contactPerson));
	}
	
	@Test
	public void testNotEqualsMerchantCode()
	{
		MarketPlaceContactPersonDto contactPerson = new MarketPlaceContactPersonDto();
		contactPersonDto.setMerchantCode(originalString);
		contactPerson.setMerchantCode(testString);
		assertFalse(contactPersonDto.equals(contactPerson));
	}
	
	@Test
	public void testNotEqualsNullStoreCode()
	{
		MarketPlaceContactPersonDto contactPerson = new MarketPlaceContactPersonDto();
		contactPersonDto.setStoreCode(null);
		contactPerson.setStoreCode(testString);
		assertFalse(contactPersonDto.equals(contactPerson));
	}
	
	@Test
	public void testNotEqualsStoreCode()
	{
		MarketPlaceContactPersonDto contactPerson = new MarketPlaceContactPersonDto();
		contactPersonDto.setStoreCode(originalString);
		contactPerson.setStoreCode(testString);
		assertFalse(contactPersonDto.equals(contactPerson));
	}
	
	@Test
	public void testNotEqualsNullContactPersons()
	{
		MarketPlaceContactPersonDto contactPerson = new MarketPlaceContactPersonDto();
		contactPersonDto.setContactPersons(null);
		contactPerson.setContactPersons(new ArrayList<ContactPersonDto>());
		assertFalse(contactPersonDto.equals(contactPerson));
	}
	
	@Test
	public void testNotEqualsContactPersons()
	{
		MarketPlaceContactPersonDto contactPerson = new MarketPlaceContactPersonDto();
		contactPersonDto.setContactPersons(new ArrayList<ContactPersonDto>());
		contactPerson.setContactPersons(null);
		assertFalse(contactPersonDto.equals(contactPerson));
	}
	
	@Test
	public void testGetMerchantCode()
	{
		contactPersonDto.setMerchantCode(testString);;
		assertEquals(contactPersonDto.getMerchantCode(), testString);
	}
	
	@Test
	public void testGetStoreCode()
	{
		contactPersonDto.setStoreCode(testString);;
		assertEquals(contactPersonDto.getStoreCode(), testString);
	}
	
	@Test
	public void testGetContactPersons()
	{
		List<ContactPersonDto> contactPersons = new ArrayList<ContactPersonDto>();
		contactPersonDto.setContactPersons(contactPersons);;
		assertEquals(contactPersonDto.getContactPersons(), contactPersons);
	}
	
}
