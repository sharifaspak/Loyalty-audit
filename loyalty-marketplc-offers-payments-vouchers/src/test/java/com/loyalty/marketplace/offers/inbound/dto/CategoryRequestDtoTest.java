package com.loyalty.marketplace.offers.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

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
		categoryRequestDto.setCategories(new ArrayList<CategoryDto>());
	}
	
	@Test
	public void testGetters() {
		assertNotNull(categoryRequestDto.getCategories());
	}
	
	@Test
	public void testToString() {
		assertNotNull(categoryRequestDto.toString());
	}
	
}
