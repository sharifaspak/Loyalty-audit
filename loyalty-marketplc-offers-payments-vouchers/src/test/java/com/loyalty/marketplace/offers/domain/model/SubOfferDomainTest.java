package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = SubOfferDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class SubOfferDomainTest {

	private SubOfferDomain subOffer;

	@Before
	public void setUp() {

		subOffer = new SubOfferDomain();
		subOffer = new SubOfferDomain("", new SubOfferTitleDomain("", ""), new SubOfferDescDomain("", ""),
				new SubOfferValueDomain(1.0, 2, 3.0, 4));
	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {

		assertNotNull(subOffer.getSubOfferId());
		assertNotNull(subOffer.getSubOfferDesc());
		assertNotNull(subOffer.getSubOfferTitle());
		assertNotNull(subOffer.getSubOfferValue());

	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(subOffer.toString());
			
	}

}
