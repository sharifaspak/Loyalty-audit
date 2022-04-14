package com.loyalty.marketplace.offers.merchants.domain.model;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@SpringBootTest(classes = MerchantBillingRateDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class MerchantBillingRateDomainTest {

	private MerchantBillingRateDomain merchantBillingRateDomain;

	@Before
	public void setUp() {
	
		merchantBillingRateDomain = new MerchantBillingRateDomain("", 0.0, new Date(), new Date(), "", "");

	}

	/**
	 * 
	 * Test Getters
	 * 
	 */

	@Test
	public void testGetters() {

		assertNotNull(merchantBillingRateDomain.getId());
		assertNotNull(merchantBillingRateDomain.getRate());
		assertNotNull(merchantBillingRateDomain.getStartDate());
		assertNotNull(merchantBillingRateDomain.getEndDate());
		assertNotNull(merchantBillingRateDomain.getRateType());
		assertNotNull(merchantBillingRateDomain.getCurrency());
	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(merchantBillingRateDomain.toString());
	}
}
