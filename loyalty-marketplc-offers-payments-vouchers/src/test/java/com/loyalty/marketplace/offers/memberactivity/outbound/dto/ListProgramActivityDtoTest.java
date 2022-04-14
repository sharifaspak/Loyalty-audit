package com.loyalty.marketplace.offers.memberactivity.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ListProgramActivityDto.class)
@ActiveProfiles("unittest")
public class ListProgramActivityDtoTest {

	private ListProgramActivityDto listProgramActivityDto;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		listProgramActivityDto = new ListProgramActivityDto();
		listProgramActivityDto.setListProgramActivity(new ArrayList<ProgramActivityWithIdDto>());
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(listProgramActivityDto.getListProgramActivity());
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(listProgramActivityDto.toString());
	}
	
}
