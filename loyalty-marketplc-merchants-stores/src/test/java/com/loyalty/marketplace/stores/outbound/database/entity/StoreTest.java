package com.loyalty.marketplace.stores.outbound.database.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = Store.class)
@ActiveProfiles("unittest")
public class StoreTest {

	private Store store;
	private String testString;
	private Date testDate;
	
	@Before
	public void setUp(){
		store = new Store();
		testString = "Test String";
		testDate = new Date();
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(store.toString());
	}
	
	@Test
	public void testGetId()
	{
		store.setId(testString);;
		assertEquals(store.getId(), testString);
	}
	
	@Test
	public void testGetProgramCode()
	{
		store.setProgramCode(testString);;
		assertEquals(store.getProgramCode(), testString);
	}
	
	@Test
	public void testGetStoreCode()
	{
		store.setStoreCode(testString);;
		assertEquals(store.getStoreCode(), testString);
	}
	
	@Test
	public void testGetDescription()
	{
		StoreDescription description = new StoreDescription();
		description.setDescription(testString);
		description.setDescriptionAr(testString);
		store.setDescription(description);;
		assertEquals(store.getDescription(), description);
	}
	
	@Test
	public void getAddress()
	{
		StoreAddress address = new StoreAddress();
		address.setAddress(testString);
		address.setAddressAr(testString);
		store.setAddress(address);;
		assertEquals(store.getAddress(), address);
	}
	
	@Test
	public void testGetLatitude()
	{
		store.setLatitude(testString);;
		assertEquals(store.getLatitude(), testString);
	}
	
	@Test
	public void testGetLongitude()
	{
		store.setLongitude(testString);;
		assertEquals(store.getLongitude(), testString);
	}
	
	@Test
	public void testGetMerchantCode()
	{
		store.setMerchantCode(testString);;
		assertEquals(store.getMerchantCode(), testString);
	}
	
	@Test
	public void testGetStatus()
	{
		store.setStatus(testString);;
		assertEquals(store.getStatus(), testString);
	}
	
	@Test
	public void testGetContactPersons()
	{
		List<ContactPerson> contactPersons = new ArrayList<ContactPerson>();
		store.setContactPersons(contactPersons);
		assertEquals(store.getContactPersons(), contactPersons);
	}
	
	@Test
	public void testGetUsrCreated()
	{
		store.setUsrCreated(testString);
		assertEquals(store.getUsrCreated(), testString);
	}
	
	@Test
	public void testGetUsrUpdated()
	{
		store.setUsrUpdated(testString);
		assertEquals(store.getUsrUpdated(), testString);
	}
	
	@Test
	public void testGetDtCreated()
	{ 
		store.setDtCreated(testDate);
		assertEquals(store.getDtCreated(), testDate);
	}
	
	@Test
	public void testGetDtUpdated()
	{ 
		store.setDtUpdated(testDate);
		assertEquals(store.getDtUpdated(), testDate);
	}
	
}
