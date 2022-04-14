package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@SpringBootTest(classes=AccountOfferCountDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class AccountOfferCountDomainTest {
	
	private AccountOfferCountDomain accountOfferCountDomain;

	@Before
	public void setUp() throws ParseException {
		
		MockitoAnnotations.initMocks(this);
		
		accountOfferCountDomain = new AccountOfferCountDomain();
		accountOfferCountDomain = new AccountOfferCountDomain("",
				"", 0, 0, 0,
				0, 0, new Date(), new ArrayList<>());

	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(accountOfferCountDomain.getAccountNumber());
		assertNotNull(accountOfferCountDomain.getMembershipCode());
		assertNotNull(accountOfferCountDomain.getDailyCount());
		assertNotNull(accountOfferCountDomain.getWeeklyCount());
		assertNotNull(accountOfferCountDomain.getMonthlyCount());
		assertNotNull(accountOfferCountDomain.getAnnualCount());
		assertNotNull(accountOfferCountDomain.getTotalCount());
		assertNotNull(accountOfferCountDomain.getLastPurchased());
		assertNotNull(accountOfferCountDomain.getDenominationCount());

	}
	
	/**
	 * 
	 * Test toString method
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(accountOfferCountDomain.toString());
	}	

	
}
