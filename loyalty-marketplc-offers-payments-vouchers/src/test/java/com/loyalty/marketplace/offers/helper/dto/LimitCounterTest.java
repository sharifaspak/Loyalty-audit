package com.loyalty.marketplace.offers.helper.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = LimitCounter.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class LimitCounterTest {

	private LimitCounter limitCounter;

	@Before
	public void setUp() {
		limitCounter = new LimitCounter();
		limitCounter.setCouponQuantity(0);
		limitCounter.setOfferDaily(0);
		limitCounter.setOfferWeekly(0);
		limitCounter.setOfferMonthly(0);
		limitCounter.setOfferAnnual(0);
		limitCounter.setOfferTotal(0);
		limitCounter.setOfferDenominationDaily(0);
		limitCounter.setOfferDenominationWeekly(0);
		limitCounter.setOfferDenominationMonthly(0);
		limitCounter.setOfferDenominationAnnual(0);
		limitCounter.setOfferDenominationTotal(0);
		limitCounter.setAccountOfferDaily(0);
		limitCounter.setAccountOfferWeekly(0);
		limitCounter.setAccountOfferMonthly(0);
		limitCounter.setAccountOfferAnnual(0);
		limitCounter.setAccountOfferTotal(0);
		limitCounter.setAccountDenominationDaily(0);
		limitCounter.setAccountDenominationWeekly(0);
		limitCounter.setAccountDenominationMonthly(0);
		limitCounter.setAccountDenominationAnnual(0);
		limitCounter.setAccountDenominationTotal(0);
		limitCounter.setMemberOfferDaily(0);
		limitCounter.setMemberOfferWeekly(0);
		limitCounter.setMemberOfferMonthly(0);
		limitCounter.setMemberOfferAnnual(0);
		limitCounter.setMemberOfferTotal(0);
		limitCounter.setMemberDenominationDaily(0);
		limitCounter.setMemberDenominationWeekly(0);
		limitCounter.setMemberDenominationMonthly(0);
		limitCounter.setMemberDenominationAnnual(0);
		limitCounter.setMemberDenominationTotal(0);
		
    }

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(limitCounter.getCouponQuantity());
		assertNotNull(limitCounter.getOfferDaily());
		assertNotNull(limitCounter.getOfferWeekly());
		assertNotNull(limitCounter.getOfferMonthly());
		assertNotNull(limitCounter.getOfferAnnual());
		assertNotNull(limitCounter.getOfferTotal());
		assertNotNull(limitCounter.getOfferDenominationDaily());
		assertNotNull(limitCounter.getOfferDenominationWeekly());
		assertNotNull(limitCounter.getOfferDenominationMonthly());
		assertNotNull(limitCounter.getOfferDenominationAnnual());
		assertNotNull(limitCounter.getOfferDenominationTotal());
		assertNotNull(limitCounter.getAccountOfferDaily());
		assertNotNull(limitCounter.getAccountOfferWeekly());
		assertNotNull(limitCounter.getAccountOfferMonthly());
		assertNotNull(limitCounter.getAccountOfferAnnual());
		assertNotNull(limitCounter.getAccountOfferTotal());
		assertNotNull(limitCounter.getAccountDenominationDaily());
		assertNotNull(limitCounter.getAccountDenominationWeekly());
		assertNotNull(limitCounter.getAccountDenominationMonthly());
		assertNotNull(limitCounter.getAccountDenominationAnnual());
		assertNotNull(limitCounter.getAccountDenominationTotal());
		assertNotNull(limitCounter.getMemberOfferDaily());
		assertNotNull(limitCounter.getMemberOfferWeekly());
		assertNotNull(limitCounter.getMemberOfferMonthly());
		assertNotNull(limitCounter.getMemberOfferAnnual());
		assertNotNull(limitCounter.getMemberOfferTotal());
		assertNotNull(limitCounter.getMemberDenominationDaily());
		assertNotNull(limitCounter.getMemberDenominationWeekly());
		assertNotNull(limitCounter.getMemberDenominationMonthly());
		assertNotNull(limitCounter.getMemberDenominationAnnual());
		assertNotNull(limitCounter.getMemberDenominationTotal());
		

	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {

		assertNotNull(limitCounter.toString());

	}

}
