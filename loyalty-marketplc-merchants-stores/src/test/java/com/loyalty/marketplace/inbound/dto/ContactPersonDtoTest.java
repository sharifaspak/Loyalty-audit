package com.loyalty.marketplace.inbound.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.merchants.outbound.database.entity.ContactPerson;

@SpringBootTest(classes = ContactPersonDto.class)
@ActiveProfiles("unittest")
public class ContactPersonDtoTest {

	private ContactPersonDto contactPersonDto;
	private String testString;
	private String originalString;
	
	@Before
	public void setUp(){
		contactPersonDto = new ContactPersonDto();
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
		assertTrue(contactPersonDto.equals(new ContactPersonDto()));
	}
	
	@Test
	public void testEqualsObject()
	{
		ContactPersonDto contactPerson = contactPersonDto;
		assertTrue(contactPersonDto.equals(contactPerson));
	}
	
	@Test
	public void testNotEqualsObject()
	{
		ContactPersonDto contactPerson = null;
		assertFalse(contactPersonDto.equals(contactPerson));
	}
	
	@Test
	@SuppressWarnings("unlikely-arg-type")
	public void testNotEqualsObjectClass()
	{
		assertFalse(contactPersonDto.equals(new ContactPerson()));
	}
	
	@Test
	public void testNotEqualsNullEmailId()
	{
		ContactPersonDto contactPerson = new ContactPersonDto();
		contactPersonDto.setEmailId(null);
		contactPerson.setEmailId(testString);
		assertFalse(contactPersonDto.equals(contactPerson));
	}
	
	@Test
	public void testNotEqualsEmailId()
	{
		ContactPersonDto contactPerson = new ContactPersonDto();
		contactPersonDto.setEmailId(originalString);
		contactPerson.setEmailId(testString);
		assertFalse(contactPersonDto.equals(contactPerson));
	}
	
	@Test
	public void testNotEqualsNullFirstName()
	{
		ContactPersonDto contactPerson = new ContactPersonDto();
		contactPersonDto.setFirstName(null);
		contactPerson.setFirstName(testString);
		assertFalse(contactPersonDto.equals(contactPerson));
	}
	
	@Test
	public void testNotEqualsFirstName()
	{
		ContactPersonDto contactPerson = new ContactPersonDto();
		contactPersonDto.setFirstName(originalString);
		contactPerson.setFirstName(testString);
		assertFalse(contactPersonDto.equals(contactPerson));
	}
	
	@Test
	public void testNotEqualsNullLastName()
	{
		ContactPersonDto contactPerson = new ContactPersonDto();
		contactPersonDto.setLastName(null);
		contactPerson.setLastName(testString);
		assertFalse(contactPersonDto.equals(contactPerson));
	}
	
	@Test
	public void testNotEqualsLastName()
	{
		ContactPersonDto contactPerson = new ContactPersonDto();
		contactPersonDto.setLastName(originalString);
		contactPerson.setLastName(testString);
		assertFalse(contactPersonDto.equals(contactPerson));
	}
	
	@Test
	public void testNotEqualsNullMobileNumber()
	{
		ContactPersonDto contactPerson = new ContactPersonDto();
		contactPersonDto.setMobileNumber(null);
		contactPerson.setMobileNumber(testString);
		assertFalse(contactPersonDto.equals(contactPerson));
	}
	
	@Test
	public void testNotEqualsMobileNumber()
	{
		ContactPersonDto contactPerson = new ContactPersonDto();
		contactPersonDto.setMobileNumber(originalString);
		contactPerson.setMobileNumber(testString);
		assertFalse(contactPersonDto.equals(contactPerson));
	}
	
	@Test
	public void testNotEqualsNullFaxNumber()
	{
		ContactPersonDto contactPerson = new ContactPersonDto();
		contactPersonDto.setFaxNumber(null);
		contactPerson.setFaxNumber(testString);
		assertFalse(contactPersonDto.equals(contactPerson));
	}
	
	@Test
	public void testNotEqualsFaxNumber()
	{
		ContactPersonDto contactPerson = new ContactPersonDto();
		contactPersonDto.setFaxNumber(originalString);
		contactPerson.setFaxNumber(testString);
		assertFalse(contactPersonDto.equals(contactPerson));
	}
	
	@Test
	public void testNotEqualsNullUserName()
	{
		ContactPersonDto contactPerson = new ContactPersonDto();
		contactPersonDto.setUserName(null);
		contactPerson.setUserName(testString);
		assertFalse(contactPersonDto.equals(contactPerson));
	}
	
	@Test
	public void testNotEqualsUserName()
	{
		ContactPersonDto contactPerson = new ContactPersonDto();
		contactPersonDto.setUserName(originalString);
		contactPerson.setUserName(testString);
		assertFalse(contactPersonDto.equals(contactPerson));
	}
	
	@Test
	public void testGetEmailId()
	{
		contactPersonDto.setEmailId(testString);;
		assertEquals(contactPersonDto.getEmailId(), testString);
	}
	
	@Test
	public void testGetFirstName()
	{
		contactPersonDto.setFirstName(testString);;
		assertEquals(contactPersonDto.getFirstName(), testString);
	}
	
	@Test
	public void testGetLastName()
	{
		contactPersonDto.setLastName(testString);;
		assertEquals(contactPersonDto.getLastName(), testString);
	}
	
	@Test
	public void testGetMobileNumber()
	{
		contactPersonDto.setMobileNumber(testString);;
		assertEquals(contactPersonDto.getMobileNumber(), testString);
	}
	
	@Test
	public void testGetFaxNumber()
	{
		contactPersonDto.setFaxNumber(testString);;
		assertEquals(contactPersonDto.getFaxNumber(), testString);
	}
	
	@Test
	public void testGetUserName()
	{
		contactPersonDto.setUserName(testString);;
		assertEquals(contactPersonDto.getUserName(), testString);
	}
	
}
