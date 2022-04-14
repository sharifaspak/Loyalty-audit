package com.loyalty.marketplace.offers.stores.outbound.database.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ContactPerson.class)
@ActiveProfiles("unittest")
public class ContactPersonTest {

	private ContactPerson contactPerson;
	private String testString;
	private Integer testInt;
	
	@Before
	public void setUp(){
		contactPerson = new ContactPerson();
		testString = "Test String";
		testInt = 99;
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(contactPerson.toString());
	}
	
	@Test
	public void testGetPassword()
	{
		contactPerson.setPassword(testString);
		assertEquals(contactPerson.getPassword(), testString);
	}
	
	@Test
	public void testGetPin()
	{
		contactPerson.setPin(testInt);;
		assertEquals(contactPerson.getPin(), testInt);
	}
	
	@Test
	public void testGetEmailId()
	{
		contactPerson.setEmailId(testString);;
		assertEquals(contactPerson.getEmailId(), testString);
	}
	
	@Test
	public void testGetMobileNumber()
	{
		contactPerson.setMobileNumber(testString);;
		assertEquals(contactPerson.getMobileNumber(), testString);
	}

	@Test
	public void testGetFirstName()
	{
		contactPerson.setFirstName(testString);;
		assertEquals(contactPerson.getFirstName(), testString);
	}
	
	@Test
	public void testGetLastName()
	{
		contactPerson.setLastName(testString);;
		assertEquals(contactPerson.getLastName(), testString);
	}
	
	@Test
	public void testGetFaxNumber()
	{
		contactPerson.setFaxNumber(testString);;
		assertEquals(contactPerson.getFaxNumber(), testString);
	}
	
	@Test
	public void testGetUserName()
	{
		contactPerson.setUserName(testString);;
		assertEquals(contactPerson.getUserName(), testString);
	}
	
}
