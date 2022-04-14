package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@SpringBootTest(classes=DenominationCountDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class DenominationCountDomainTest {
	
	private DenominationCountDomain denominationCountDomain;

	@Before
	public void setUp() throws ParseException {
		
		MockitoAnnotations.initMocks(this);
		
		denominationCountDomain = new DenominationCountDomain();
		denominationCountDomain = new DenominationCountDomain(0, 
				0, 0, 0, 0, 0, new Date());

	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(denominationCountDomain.getDenomination());
		assertNotNull(denominationCountDomain.getDailyCount());
		assertNotNull(denominationCountDomain.getWeeklyCount());
		assertNotNull(denominationCountDomain.getMonthlyCount());
		assertNotNull(denominationCountDomain.getAnnualCount());
		assertNotNull(denominationCountDomain.getTotalCount());
		assertNotNull(denominationCountDomain.getLastPurchased());

	}
	
	/**
	 * 
	 * Test toString method
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(denominationCountDomain.toString());
	}	

	
}
