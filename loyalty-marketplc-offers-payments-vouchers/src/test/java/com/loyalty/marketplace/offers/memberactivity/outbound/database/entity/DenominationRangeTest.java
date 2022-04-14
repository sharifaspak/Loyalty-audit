package com.loyalty.marketplace.offers.memberactivity.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = DenominationRange.class)
@ActiveProfiles("unittest")
public class DenominationRangeTest {

	private DenominationRange denominationRange;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		denominationRange = new DenominationRange();
		denominationRange.setLowValue("");
		denominationRange.setHighValue("");
	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(denominationRange.getLowValue());
		assertNotNull(denominationRange.getHighValue());
	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(denominationRange.toString());
	}

}
