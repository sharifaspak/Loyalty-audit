package com.loyalty.marketplace.offers.memberactivity.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ActivityDescription.class)
@ActiveProfiles("unittest")
public class ActivityDescriptionTest {

	private ActivityDescription activityDescription;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		activityDescription = new ActivityDescription();
		activityDescription.setEnglish("");
		activityDescription.setArabic("");
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(activityDescription.getEnglish());
		assertNotNull(activityDescription.getArabic());
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(activityDescription.toString());
	}
	
}
