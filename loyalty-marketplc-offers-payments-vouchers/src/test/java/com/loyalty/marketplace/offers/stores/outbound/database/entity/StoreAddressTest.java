package com.loyalty.marketplace.offers.stores.outbound.database.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = StoreAddress.class)
@ActiveProfiles("unittest")
public class StoreAddressTest {

	private StoreAddress address;
	private String testString;
	
	@Before
	public void setUp(){
		address = new StoreAddress();
		testString = "Test String";
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(address.toString());
	}
	
	@Test
	public void testGetAddress()
	{
		address.setAddressEn(testString);;
		assertEquals(address.getAddressEn(), testString);
	}
	
	@Test
	public void testGetAddressAr()
	{
		address.setAddressAr(testString);;
		assertEquals(address.getAddressAr(), testString);
	}
	
}
