package com.loyalty.marketplace.merchants.outbound.database.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = Category.class)
@ActiveProfiles("unittest")
public class CategoryTest {

	private Category category;
	private String testString;
	private Date testDate;
	
	@Before
	public void setUp(){
		category = new Category();
		testString = "testValue";
		testDate = new Date();
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(category.toString());
	}
	
	@Test
	public void testGetId()
	{
		category.setId(testString);
		assertEquals(category.getId(), testString);
	}
	
	@Test
	public void testGetCategoryName()
	{
		Name name = new Name();
		name.setArabic(testString);
		name.setEnglish(testString);
		category.setName(name);
		assertEquals(category.getName(), name);
	}
	
	@Test
	public void testGetUsrCreated()
	{
		category.setUsrCreated(testString);
		assertEquals(category.getUsrCreated(), testString);
	}
	
	@Test
	public void testGetUsrUpdated()
	{
		category.setUsrUpdated(testString);
		assertEquals(category.getUsrUpdated(), testString);
	}
	
	@Test
	public void testGetDtCreated()
	{ 
		category.setDtCreated(testDate);
		assertEquals(category.getDtCreated(), testDate);
	}
	
	@Test
	public void testGetDtUpdated()
	{ 
		category.setDtUpdated(testDate);
		assertEquals(category.getDtUpdated(), testDate);
	}
	
}
