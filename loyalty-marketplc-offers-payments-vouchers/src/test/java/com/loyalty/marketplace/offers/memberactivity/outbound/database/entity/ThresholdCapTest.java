package com.loyalty.marketplace.offers.memberactivity.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.offers.memberactivity.inbound.dto.UOMDto;

@SpringBootTest(classes = ThresholdCap.class)
@ActiveProfiles("unittest")
public class ThresholdCapTest {

	private ThresholdCap thresholdCap;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		thresholdCap = new ThresholdCap();
		thresholdCap.setThresholdCapType("");
		thresholdCap.setThresholdCapTypeDescription("");
		thresholdCap.setThresholdCapValue(100L);
		thresholdCap.setDuration(100L);
		thresholdCap.setUom(new UOMDto());
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(thresholdCap.getThresholdCapType());
		assertNotNull(thresholdCap.getThresholdCapTypeDescription());
		assertNotNull(thresholdCap.getThresholdCapValue());
		assertNotNull(thresholdCap.getDuration());
		assertNotNull(thresholdCap.getUom());
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(thresholdCap.toString());
	}
	
}
