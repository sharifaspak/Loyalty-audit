package com.loyalty.marketplace.offers.helper.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = AmountInfo.class)
@ActiveProfiles("unittest")
public class AmountInfoTest {

	private AmountInfo amountInfo;
	
	@Before
	public void setUp(){
		amountInfo = new AmountInfo();
		amountInfo = new AmountInfo(0.0, 0, 0.0, 0, 0.0, 0.0, 0.0, 0.0);
		amountInfo.setOfferCost(0.0);
		amountInfo.setOfferPoints(0);
		amountInfo.setSpentAmount(0.0);
		amountInfo.setSpentPoints(0);
		amountInfo.setConvertedSpentPointsToAmount(0.0);
		amountInfo.setCost(0.0);
		amountInfo.setVatAmount(0.0);
		amountInfo.setPurchaseAmount(0.0);
	}
	
	@Test
	public void testGetters() {
		assertNotNull(amountInfo.getOfferCost());
		assertNotNull(amountInfo.getOfferPoints());
		assertNotNull(amountInfo.getSpentAmount());
		assertNotNull(amountInfo.getSpentPoints());
		assertNotNull(amountInfo.getConvertedSpentPointsToAmount());
		assertNotNull(amountInfo.getCost());
		assertNotNull(amountInfo.getVatAmount());
		assertNotNull(amountInfo.getPurchaseAmount());
	}
	
	@Test
	public void testToString() {
		assertNotNull(amountInfo.toString());
	}
	
}
