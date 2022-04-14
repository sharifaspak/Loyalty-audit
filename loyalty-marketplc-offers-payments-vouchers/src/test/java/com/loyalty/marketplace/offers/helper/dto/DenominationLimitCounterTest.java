package com.loyalty.marketplace.offers.helper.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = DenominationLimitCounter.class)
@ActiveProfiles("unittest")
public class DenominationLimitCounterTest {

	private DenominationLimitCounter denominationLimitCounter;
	
	@Before
	public void setUp(){
		
		denominationLimitCounter = new DenominationLimitCounter();
		denominationLimitCounter.setDenomination(0);
		denominationLimitCounter.setOfferAnnual(0);
		denominationLimitCounter.setOfferDaily(0);
		denominationLimitCounter.setOfferWeekly(0);
		denominationLimitCounter.setOfferMonthly(0);
		denominationLimitCounter.setOfferTotal(0);
		denominationLimitCounter.setAccountAnnual(0);
		denominationLimitCounter.setAccountDaily(0);
		denominationLimitCounter.setAccountWeekly(0);
		denominationLimitCounter.setAccountMonthly(0);
		denominationLimitCounter.setAccountTotal(0);
		denominationLimitCounter.setMemberAnnual(0);
		denominationLimitCounter.setMemberDaily(0);
		denominationLimitCounter.setMemberWeekly(0);
		denominationLimitCounter.setMemberMonthly(0);
		denominationLimitCounter.setMemberTotal(0);
    
	}
	
	@Test
	public void testGetters() {
		assertNotNull(denominationLimitCounter.getOfferDaily());
		assertNotNull(denominationLimitCounter.getOfferMonthly());
		assertNotNull(denominationLimitCounter.getOfferWeekly());
		assertNotNull(denominationLimitCounter.getOfferAnnual());
		assertNotNull(denominationLimitCounter.getOfferTotal());
		assertNotNull(denominationLimitCounter.getAccountDaily());
		assertNotNull(denominationLimitCounter.getAccountMonthly());
		assertNotNull(denominationLimitCounter.getAccountWeekly());
		assertNotNull(denominationLimitCounter.getAccountAnnual());
		assertNotNull(denominationLimitCounter.getAccountTotal());
		assertNotNull(denominationLimitCounter.getMemberDaily());
		assertNotNull(denominationLimitCounter.getMemberWeekly());
		assertNotNull(denominationLimitCounter.getMemberMonthly());
		assertNotNull(denominationLimitCounter.getMemberAnnual());
		assertNotNull(denominationLimitCounter.getMemberTotal());
		assertNotNull(denominationLimitCounter.getDenomination());
	}
	
	@Test
	public void testToString() {
		assertNotNull(denominationLimitCounter.toString());
	}
	
}
