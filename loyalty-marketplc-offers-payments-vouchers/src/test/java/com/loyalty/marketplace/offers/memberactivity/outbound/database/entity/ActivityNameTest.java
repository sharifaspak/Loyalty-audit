package com.loyalty.marketplace.offers.memberactivity.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ActivityName.class)
@ActiveProfiles("unittest")
public class ActivityNameTest {

	private ActivityName activityName;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		activityName = new ActivityName();
		activityName.setEnglish("");
		activityName.setArabic("");
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(activityName.getEnglish());
		assertNotNull(activityName.getArabic());
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(activityName.toString());
	}
	
}
