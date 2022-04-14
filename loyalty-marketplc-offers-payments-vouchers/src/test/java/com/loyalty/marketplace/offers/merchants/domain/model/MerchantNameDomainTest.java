package com.loyalty.marketplace.offers.merchants.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = MerchantNameDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class MerchantNameDomainTest {

	private MerchantNameDomain merchantNameDomain;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		merchantNameDomain = new MerchantNameDomain("", "");
	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {

		assertNotNull(merchantNameDomain.getEnglish());
		assertNotNull(merchantNameDomain.getArabic());

	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(merchantNameDomain.toString());
	}

}
