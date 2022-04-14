package com.loyalty.marketplace.offers.memberactivity.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = RateType.class)
@ActiveProfiles("unittest")
public class RateTypeTest {

	private RateType rateType;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		rateType = new RateType();
		rateType.setName("");
		rateType.setDescription("");
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(rateType.getName());
		assertNotNull(rateType.getDescription());
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(rateType.toString());
	}
	
}
