package com.loyalty.marketplace.inbound.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CategoryRequestDto.class)
@ActiveProfiles("unittest")
public class CategoryRequestDtoTest {

	private CategoryRequestDto categoryRequestDto;
	
	@Before
	public void setUp(){
		categoryRequestDto = new CategoryRequestDto();
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(categoryRequestDto.toString());
	}
	
	@Test
	public void testGetCategories()
	{
		List<CategoryDto> categories = new ArrayList<CategoryDto>();
		categoryRequestDto.setCategories(categories);;
		assertEquals(categoryRequestDto.getCategories(), categories);
	}
	
}
