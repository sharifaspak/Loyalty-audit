package com.loyalty.marketplace.offers.merchants.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = MerchantContactPersonDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class MerchantContactPersonDomainTest {

	private MerchantContactPersonDomain merchantContactPersonDomain;

	@Before
	public void setUp() {
		
		merchantContactPersonDomain = new MerchantContactPersonDomain("", "", "", "", "", "", "");

	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
    @Test
	public void testGetters() {

		assertNotNull(merchantContactPersonDomain.getEmailId());
		assertNotNull(merchantContactPersonDomain.getFirstName());
		assertNotNull(merchantContactPersonDomain.getLastName());
		assertNotNull(merchantContactPersonDomain.getMobileNumber());
		assertNotNull(merchantContactPersonDomain.getFaxNumber());
		assertNotNull(merchantContactPersonDomain.getUserName());
		assertNotNull(merchantContactPersonDomain.getPassword());
	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(merchantContactPersonDomain.toString());
	}
}
