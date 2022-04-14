package com.loyalty.marketplace.offers.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ListValuesDto.class)
@ActiveProfiles("unittest")
public class ListValuesDtoTest {

	private ListValuesDto listValuesDto;
	
	@Before
	public void setUp(){
		listValuesDto = new ListValuesDto();
		listValuesDto.setEligibleTypes(new ArrayList<>(1));
		listValuesDto.setExclusionTypes(new ArrayList<>(1));
	}
	
	@Test
	public void testGetters() {
		assertNotNull(listValuesDto.getEligibleTypes());
		assertNotNull(listValuesDto.getExclusionTypes());
		
	}
	
	@Test
	public void testToString() {
		assertNotNull(listValuesDto.toString());
	}
	
}
