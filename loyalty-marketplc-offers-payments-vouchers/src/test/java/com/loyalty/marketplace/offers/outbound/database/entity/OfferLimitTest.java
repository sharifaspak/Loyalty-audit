package com.loyalty.marketplace.offers.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = OfferLimit.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OfferLimitTest {

	@InjectMocks
	OfferLimit offerLimitMock;

	private OfferLimit offerLimit;

	@Before
	public void setUp() {
		offerLimit = new OfferLimit();
		offerLimit.setCustomerSegment("");
		offerLimit.setDownloadLimit(0);
		offerLimit.setCouponQuantity(0);
		offerLimit.setDailyLimit(0);
		offerLimit.setWeeklyLimit(0);
		offerLimit.setMonthlyLimit(0);
		offerLimit.setAnnualLimit(0);
		offerLimit.setDenominationLimit(new ArrayList<>());
		offerLimit.setAccountDailyLimit(0);
		offerLimit.setAccountWeeklyLimit(0);
		offerLimit.setAccountMonthlyLimit(0);
		offerLimit.setAccountAnnualLimit(0);
		offerLimit.setAccountTotalLimit(0);
		offerLimit.setAccountDenominationLimit(new ArrayList<>());
		offerLimit.setMemberDailyLimit(0);
		offerLimit.setMemberWeeklyLimit(0);
		offerLimit.setMemberMonthlyLimit(0);
		offerLimit.setMemberAnnualLimit(0);
		offerLimit.setMemberTotalLimit(0);
		offerLimit.setMemberDenominationLimit(new ArrayList<>());

	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(offerLimit.getCustomerSegment());
		assertNotNull(offerLimit.getDownloadLimit());
		assertNotNull(offerLimit.getCouponQuantity());
		assertNotNull(offerLimit.getDailyLimit());
		assertNotNull(offerLimit.getWeeklyLimit());
		assertNotNull(offerLimit.getMonthlyLimit());
		assertNotNull(offerLimit.getAnnualLimit());
		assertNotNull(offerLimit.getDenominationLimit());
		assertNotNull(offerLimit.getAccountDailyLimit());
		assertNotNull(offerLimit.getAccountWeeklyLimit());
		assertNotNull(offerLimit.getAccountMonthlyLimit());
		assertNotNull(offerLimit.getAccountAnnualLimit());
		assertNotNull(offerLimit.getAccountTotalLimit());
		assertNotNull(offerLimit.getAccountDenominationLimit());
		assertNotNull(offerLimit.getMemberDailyLimit());
		assertNotNull(offerLimit.getMemberWeeklyLimit());
		assertNotNull(offerLimit.getMemberMonthlyLimit());
		assertNotNull(offerLimit.getMemberAnnualLimit());
		assertNotNull(offerLimit.getMemberTotalLimit());
		assertNotNull(offerLimit.getMemberDenominationLimit());

	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {

		assertNotNull(offerLimit.toString());

	}

}
