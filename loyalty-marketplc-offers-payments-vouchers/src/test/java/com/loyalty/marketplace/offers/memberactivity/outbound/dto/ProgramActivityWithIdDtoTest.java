package com.loyalty.marketplace.offers.memberactivity.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.ActivityDescription;
import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.ActivityName;

@SpringBootTest(classes = ProgramActivityWithIdDto.class)
@ActiveProfiles("unittest")
public class ProgramActivityWithIdDtoTest {

	private ProgramActivityWithIdDto programActivityWithIdDto;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		programActivityWithIdDto = new ProgramActivityWithIdDto();
		programActivityWithIdDto.setActivityId("");
		programActivityWithIdDto.setActivityName(new ActivityName());
		programActivityWithIdDto.setActivityDescription(new ActivityDescription());
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(programActivityWithIdDto.getActivityId());
		assertNotNull(programActivityWithIdDto.getActivityName());
		assertNotNull(programActivityWithIdDto.getActivityDescription());
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(programActivityWithIdDto.toString());
	}
	
}
