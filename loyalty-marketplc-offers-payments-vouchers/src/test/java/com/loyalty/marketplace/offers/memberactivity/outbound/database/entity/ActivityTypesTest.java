package com.loyalty.marketplace.offers.memberactivity.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ActivityTypes.class)
@ActiveProfiles("unittest")
public class ActivityTypesTest {

	private ActivityTypes activityTypes;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		activityTypes = new ActivityTypes();
		activityTypes.setName(new ActivityName());
		activityTypes.setDescription(new ActivityDescription());
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(activityTypes.getName());
		assertNotNull(activityTypes.getDescription());
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(activityTypes.toString());
	}
	
}
