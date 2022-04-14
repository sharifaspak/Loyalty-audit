package com.loyalty.marketplace.offers.stores.domain.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = StoreDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class StoreDomainTest {

	private StoreDomain storeDomain;

	@Before
	public void setUp() {
		
		storeDomain = new StoreDomain();

	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {

		storeDomain = new StoreDomain.StoreBuilder("").build();
		assertNull(storeDomain.getProgramCode());
		assertNull(storeDomain.getStoreCode());
		assertNull(storeDomain.getStoreName());
		assertNull(storeDomain.getMerchantCode());
		assertNull(storeDomain.getStatus());
		assertNull(storeDomain.getAddress());
		assertNull(storeDomain.getUsrCreated());
		assertNull(storeDomain.getUsrUpdated());
		assertNull(storeDomain.getDtCreated());
		assertNull(storeDomain.getDtUpdated());
		assertNull(storeDomain.getContactPersons());
		assertNotNull(storeDomain);

	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(storeDomain.toString());
	}

}
