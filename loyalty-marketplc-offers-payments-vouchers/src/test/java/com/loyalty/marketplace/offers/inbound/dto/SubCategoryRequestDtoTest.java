package com.loyalty.marketplace.offers.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = SubCategoryRequestDto.class)
@ActiveProfiles("unittest")
public class SubCategoryRequestDtoTest {

	private SubCategoryRequestDto subCategoryRequestDto;
	
	@Before
	public void setUp(){
		subCategoryRequestDto = new SubCategoryRequestDto();
		subCategoryRequestDto.setSubCategories(new ArrayList<SubCategoryDto>());
	}
	
	@Test
	public void testGetters() {
		assertNotNull(subCategoryRequestDto.getSubCategories());
	}
	
	@Test
	public void testToString() {
		assertNotNull(subCategoryRequestDto.toString());
	}
	
}
