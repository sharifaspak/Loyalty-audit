package com.loyalty.marketplace.offers.helper.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = AmountPoints.class)
@ActiveProfiles("unittest")
public class AmountPointsTest {

	private AmountPoints amountPoints;
	
	@Before
	public void setUp(){
		amountPoints = new AmountPoints();
		amountPoints.setPoints(0);
		amountPoints.setAmount(0.0);
	}
	
	@Test
	public void testGetters() {
		assertNotNull(amountPoints.getPoints());
		assertNotNull(amountPoints.getAmount());
	}
	
	@Test
	public void testToString() {
		assertNotNull(amountPoints.toString());
	}
	
}
