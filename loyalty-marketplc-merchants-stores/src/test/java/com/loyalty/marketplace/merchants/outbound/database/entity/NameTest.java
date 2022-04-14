package com.loyalty.marketplace.merchants.outbound.database.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = Name.class)
@ActiveProfiles("unittest")
public class NameTest {

	private Name name;
	private String testString;
	
	@Before
	public void setUp(){
		name = new Name();
		testString = "testValue";
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(name.toString());
	}
	
	@Test
	public void testGetEnglish()
	{
		name.setEnglish(testString);
		assertEquals(name.getEnglish(), testString);
	}
	
	@Test
	public void testGetArabic()
	{
		name.setArabic(testString);
		assertEquals(name.getArabic(), testString);
	}
	
}
