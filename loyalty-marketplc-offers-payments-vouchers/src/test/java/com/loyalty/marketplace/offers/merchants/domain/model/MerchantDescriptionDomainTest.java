package com.loyalty.marketplace.offers.merchants.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = MerchantDescriptionDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class MerchantDescriptionDomainTest {

	private MerchantDescriptionDomain merchantDescriptionDomain;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		merchantDescriptionDomain = new MerchantDescriptionDomain("", "");
	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {

		assertNotNull(merchantDescriptionDomain.getEnglish());
		assertNotNull(merchantDescriptionDomain.getArabic());

	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(merchantDescriptionDomain.toString());
	}

}
