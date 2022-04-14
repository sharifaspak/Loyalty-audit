package com.loyalty.marketplace.offers.inbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CategoryDto.class)
@ActiveProfiles("unittest")
public class CategoryDtoTest {

	private CategoryDto categoryDto;
	
	@Before
	public void setUp(){
		categoryDto = new CategoryDto();
		categoryDto.setCategoryId("");
		categoryDto.setCategoryNameEn("");
		categoryDto.setCategoryNameAr("");
	}
	
	@Test
	public void testGetters() {
		assertNotNull(categoryDto.getCategoryId());
		assertNotNull(categoryDto.getCategoryNameEn());
		assertNotNull(categoryDto.getCategoryNameAr());
	}
	
	@Test
	public void testToString() {
		assertNotNull(categoryDto.toString());
	}
	
}
