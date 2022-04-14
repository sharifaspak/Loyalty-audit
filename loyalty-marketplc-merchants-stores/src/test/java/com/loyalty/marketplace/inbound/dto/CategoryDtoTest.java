package com.loyalty.marketplace.inbound.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.outbound.database.entity.Category;

@SpringBootTest(classes = CategoryDto.class)
@ActiveProfiles("unittest")
public class CategoryDtoTest {

	private CategoryDto categoryDto;
	private String testString;
	private String originalString;
	
	@Before
	public void setUp(){
		categoryDto = new CategoryDto();
		testString = "Test String";
		originalString = "Original String";
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(categoryDto.toString());
	}
	
	@Test
	public void testHashCode()
	{
		assertNotNull(categoryDto.hashCode());
	}
	
	@Test
	public void testEquals()
	{
		assertTrue(categoryDto.equals(new CategoryDto()));
	}
	
	@Test
	public void testEqualsObject()
	{
		CategoryDto category = categoryDto;
		assertTrue(categoryDto.equals(category));
	}
	
	@Test
	public void testNotEqualsObject()
	{
		CategoryDto category = null;
		assertFalse(categoryDto.equals(category));
	}
	
	@Test
	@SuppressWarnings("unlikely-arg-type")
	public void testNotEqualsObjectClass()
	{
		assertFalse(categoryDto.equals(new Category()));
	}
	
	@Test
	public void testNotEqualsNullCategoryName()
	{
		CategoryDto category = new CategoryDto();
		categoryDto.setCategoryNameEn(null);
		category.setCategoryNameEn(testString);
		assertFalse(categoryDto.equals(category));
	}
	
	@Test
	public void testNotEqualsCategoryName()
	{
		CategoryDto category = new CategoryDto();
		categoryDto.setCategoryNameEn(originalString);
		category.setCategoryNameEn(testString);
		assertFalse(categoryDto.equals(category));
	}
	
	@Test
	public void testNotEqualsNullCategoryNameArb()
	{
		CategoryDto category = new CategoryDto();
		categoryDto.setCategoryNameAr(null);
		category.setCategoryNameAr(testString);
		assertFalse(categoryDto.equals(category));
	}
	
	@Test
	public void testNotEqualsCategoryNameArb()
	{
		CategoryDto category = new CategoryDto();
		categoryDto.setCategoryNameAr(originalString);
		category.setCategoryNameAr(testString);
		assertFalse(categoryDto.equals(category));
	}
	
	
	@Test
	public void testGetCategoryName()
	{
		categoryDto.setCategoryNameEn(testString);;
		assertEquals(categoryDto.getCategoryNameEn(), testString);
	}
	
	@Test
	public void testgetCategoryNameAr()
	{
		categoryDto.setCategoryNameAr(testString);;
		assertEquals(categoryDto.getCategoryNameAr(), testString);
	}
	
}
