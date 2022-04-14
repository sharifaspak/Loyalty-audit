package com.loyalty.marketplace.offers.merchants.domain.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = MerchantDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class MerchantDomainTest {

	private MerchantDomain merchantDomain;

	@Before
	public void setUp() {

		merchantDomain = new MerchantDomain();

	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {

		merchantDomain = new MerchantDomain.MerchantBuilder("").build();

		assertNull(merchantDomain.getProgramCode());
		assertNull(merchantDomain.getMerchantCode());
		assertNull(merchantDomain.getMerchantName());
		assertNull(merchantDomain.getPartnerCode());
		assertNull(merchantDomain.getCategoryId());
		assertNull(merchantDomain.getBarcodeType());
		assertNull(merchantDomain.getStatus());
		assertNull(merchantDomain.getWhatYouGet());
		assertNull(merchantDomain.getTnC());
		assertNull(merchantDomain.getMerchantDescription());
		assertNull(merchantDomain.getExternalName());
		assertNull(merchantDomain.getUsrCreated());
		assertNull(merchantDomain.getUsrUpdated());
		assertNull(merchantDomain.getDtCreated());
		assertNull(merchantDomain.getDtUpdated());
		assertNull(merchantDomain.getContactPersons());
		assertNull(merchantDomain.getBillingRates());
		assertNotNull(merchantDomain);
	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(merchantDomain.toString());
	}
}