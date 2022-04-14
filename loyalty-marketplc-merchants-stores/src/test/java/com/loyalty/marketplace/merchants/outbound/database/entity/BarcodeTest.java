package com.loyalty.marketplace.merchants.outbound.database.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = Barcode.class)
@ActiveProfiles("unittest")
public class BarcodeTest {

	private Barcode barcode;
	private String testString;
	private Date testDate;
	
	@Before
	public void setUp(){
		barcode = new Barcode();
		testString = "testValue";
		testDate = new Date();
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(barcode.toString());
	}
	
	@Test
	public void testGetId()
	{
		barcode.setId(testString);
		assertEquals(barcode.getId(), testString);
	}
	
	@Test
	public void testGetName()
	{
		barcode.setName(testString);
		assertEquals(barcode.getName(), testString);
	}
	
	@Test
	public void testGetDescription()
	{
		barcode.setDescription(testString);
		assertEquals(barcode.getDescription(), testString);
	}
	
	@Test
	public void testGetUsrCreated()
	{
		barcode.setUsrCreated(testString);
		assertEquals(barcode.getUsrCreated(), testString);
	}
	
	@Test
	public void testGetUsrUpdated()
	{
		barcode.setUsrUpdated(testString);
		assertEquals(barcode.getUsrUpdated(), testString);
	}
	
	@Test
	public void testGetDtCreated()
	{ 
		barcode.setDtCreated(testDate);
		assertEquals(barcode.getDtCreated(), testDate);
	}
	
	@Test
	public void testGetDtUpdated()
	{ 
		barcode.setDtUpdated(testDate);
		assertEquals(barcode.getDtUpdated(), testDate);
	}
	
}
