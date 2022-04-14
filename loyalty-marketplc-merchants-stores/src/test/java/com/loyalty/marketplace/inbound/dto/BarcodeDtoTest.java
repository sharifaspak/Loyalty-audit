package com.loyalty.marketplace.inbound.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.merchants.outbound.database.entity.Barcode;

@SpringBootTest(classes = BarcodeDto.class)
@ActiveProfiles("unittest")
public class BarcodeDtoTest {

	private BarcodeDto barcodeDto;
	private String testString;
	private String originalString;
	private Date testDate;
	
	@Before
	public void setUp(){
		barcodeDto = new BarcodeDto();
		testString = "Test String";
		originalString = "Original String";
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(barcodeDto.toString());
	}
	
	@Test
	public void testHashCode()
	{
		assertNotNull(barcodeDto.hashCode());
	}
	
	@Test
	public void testEquals()
	{
		assertTrue(barcodeDto.equals(new BarcodeDto()));
	}
	
	@Test
	public void testEqualsObject()
	{
		BarcodeDto barcode = barcodeDto;
		assertTrue(barcodeDto.equals(barcode));
	}
	
	@Test
	public void testNotEqualsObject()
	{
		BarcodeDto barcode = null;
		assertFalse(barcodeDto.equals(barcode));
	}
	
	@Test
	@SuppressWarnings("unlikely-arg-type")
	public void testNotEqualsObjectClass()
	{
		assertFalse(barcodeDto.equals(new Barcode()));
	}
	
	@Test
	public void testNotEqualsNullName()
	{
		BarcodeDto barcode = new BarcodeDto();
		barcodeDto.setName(null);
		barcode.setName(testString);
		assertFalse(barcodeDto.equals(barcode));
	}
	
	@Test
	public void testNotEqualsName()
	{
		BarcodeDto barcode = new BarcodeDto();
		barcodeDto.setName(originalString);
		barcode.setName(testString);
		assertFalse(barcodeDto.equals(barcode));
	}
	
	@Test
	public void testNotEqualsNullDescription()
	{
		BarcodeDto barcode = new BarcodeDto();
		barcodeDto.setDescription(null);
		barcode.setDescription(testString);
		assertFalse(barcodeDto.equals(barcode));
	}
	
	@Test
	public void testNotEqualsDescription()
	{
		BarcodeDto barcode = new BarcodeDto();
		barcodeDto.setDescription(originalString);
		barcode.setDescription(testString);
		assertFalse(barcodeDto.equals(barcode));
	}
	
	@Test
	public void testGetId()
	{
		barcodeDto.setId(testString);;
		assertEquals(barcodeDto.getId(), testString);
	}
	
	@Test
	public void testGetUsrCreated()
	{
		barcodeDto.setUsrCreated(testString);;
		assertEquals(barcodeDto.getUsrCreated(), testString);
	}
	
	@Test
	public void testGetUsrUpdated()
	{
		barcodeDto.setUsrUpdated(testString);;
		assertEquals(barcodeDto.getUsrUpdated(), testString);
	}
	
	@Test
	public void testGetDtCreated()
	{
		testDate = new Date();
		barcodeDto.setDtCreated(testDate);;
		assertEquals(barcodeDto.getDtCreated(), testDate);
	}
	
	@Test
	public void testGetDtUpdated()
	{
		testDate = new Date();
		barcodeDto.setDtUpdated(testDate);;
		assertEquals(barcodeDto.getDtUpdated(), testDate);
	}
	
	@Test
	public void testGetName()
	{
		barcodeDto.setName(testString);;
		assertEquals(barcodeDto.getName(), testString);
	}
	
	@Test
	public void testGetDescription()
	{
		barcodeDto.setDescription(testString);;
		assertEquals(barcodeDto.getDescription(), testString);
	}
	
}
