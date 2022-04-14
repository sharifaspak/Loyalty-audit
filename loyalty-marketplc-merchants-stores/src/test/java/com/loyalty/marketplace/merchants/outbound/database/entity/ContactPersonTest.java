package com.loyalty.marketplace.merchants.outbound.database.entity;

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
	
	@Before
	public void setUp(){
		contactPerson = new ContactPerson();
		testString = "testValue";
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(contactPerson.toString());
	}
	
	@Test
	public void testGetEmailId()
	{
		contactPerson.setEmailId(testString);
		assertEquals(contactPerson.getEmailId(), testString);
	}
	
	@Test
	public void testGetFirstName()
	{
		contactPerson.setFirstName(testString);
		assertEquals(contactPerson.getFirstName(), testString);
	}
	
	@Test
	public void testGetLastName()
	{
		contactPerson.setLastName(testString);
		assertEquals(contactPerson.getLastName(), testString);
	}
	
	@Test
	public void testGetMobileNumber()
	{
		contactPerson.setMobileNumber(testString);
		assertEquals(contactPerson.getMobileNumber(), testString);
	}
	
	@Test
	public void testGetFaxNumber()
	{
		contactPerson.setFaxNumber(testString);
		assertEquals(contactPerson.getFaxNumber(), testString);
	}
	
	@Test
	public void testGetUserName()
	{
		contactPerson.setUserName(testString);
		assertEquals(contactPerson.getUserName(), testString);
	}
	
}
