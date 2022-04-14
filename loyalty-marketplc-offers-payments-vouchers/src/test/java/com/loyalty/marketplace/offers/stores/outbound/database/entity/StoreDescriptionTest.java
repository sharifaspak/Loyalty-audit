package com.loyalty.marketplace.offers.stores.outbound.database.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = StoreDescription.class)
@ActiveProfiles("unittest")
public class StoreDescriptionTest {

	private StoreDescription description;
	private String testString;
	
	@Before
	public void setUp(){
		description = new StoreDescription();
		testString = "Test String";
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(description.toString());
	}
	
	@Test
	public void testGetDescription()
	{
		description.setStoreDescriptionEn(testString);;
		assertEquals(description.getStoreDescriptionEn(), testString);
	}
	
	@Test
	public void testGetDescriptionAr()
	{
		description.setStoreDescriptionAr(testString);;
		assertEquals(description.getStoreDescriptionAr(), testString);
	}
	
}
