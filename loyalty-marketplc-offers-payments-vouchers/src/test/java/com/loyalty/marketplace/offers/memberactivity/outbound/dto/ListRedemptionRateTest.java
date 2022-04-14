package com.loyalty.marketplace.offers.memberactivity.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.RedemptionRate;

@SpringBootTest(classes = ListRedemptionRate.class)
@ActiveProfiles("unittest")
public class ListRedemptionRateTest {

	private ListRedemptionRate listRedemptionRate;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		listRedemptionRate = new ListRedemptionRate();
		listRedemptionRate.setRedemptionRateList(new ArrayList<RedemptionRate>());
	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(listRedemptionRate.getRedemptionRateList());
	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(listRedemptionRate.toString());
	}

}