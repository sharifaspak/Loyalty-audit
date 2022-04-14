package com.loyalty.marketplace.offers.member.management.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = EligibilitySectionDto.class)
@ActiveProfiles("unittest")
public class EligibilitySectionDtoTest {

	private EligibilitySectionDto eligibilitySectionDto;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		eligibilitySectionDto = new EligibilitySectionDto();
		
		eligibilitySectionDto.setSectionId("");
		eligibilitySectionDto.setSectionName("");
		eligibilitySectionDto.setSectionDescription("");
		eligibilitySectionDto.setSectionType("");
		
		EligibilitySectionDto eligibility = new EligibilitySectionDto("","","","");
		eligibility.setSectionId("");
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(eligibilitySectionDto.getSectionId());
		assertNotNull(eligibilitySectionDto.getSectionName());
		assertNotNull(eligibilitySectionDto.getSectionDescription());
		assertNotNull(eligibilitySectionDto.getSectionType());
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(eligibilitySectionDto.toString());
	}
	
}
