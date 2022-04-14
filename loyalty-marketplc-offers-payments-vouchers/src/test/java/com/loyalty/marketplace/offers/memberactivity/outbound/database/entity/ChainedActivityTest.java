package com.loyalty.marketplace.offers.memberactivity.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ChainedActivity.class)
@ActiveProfiles("unittest")
public class ChainedActivityTest {

	private ChainedActivity chainedActivity;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		chainedActivity = new ChainedActivity();
		chainedActivity.setPartnerActivityId("");
		chainedActivity.setSequence(0);
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(chainedActivity.getPartnerActivityId());
		assertNotNull(chainedActivity.getSequence());
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(chainedActivity.toString());
	}
	
}
