package com.loyalty.marketplace.offers.inbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = SubCategoryDto.class)
@ActiveProfiles("unittest")
public class SubCategoryDtoTest {

	private SubCategoryDto subCategoryDto;
	
	@Before
	public void setUp(){
		subCategoryDto = new SubCategoryDto();
		subCategoryDto.setSubCategoryId("");
		subCategoryDto.setSubCategoryNameEn("");
		subCategoryDto.setSubCategoryNameAr("");
		subCategoryDto.setParentCategory("");
	}
	
	@Test
	public void testGetters() {
		assertNotNull(subCategoryDto.getSubCategoryId());
		assertNotNull(subCategoryDto.getSubCategoryNameEn());
		assertNotNull(subCategoryDto.getSubCategoryNameAr());
		assertNotNull(subCategoryDto.getParentCategory());
	}
	
	@Test
	public void testToString() {
		assertNotNull(subCategoryDto.toString());
	}
	
}
