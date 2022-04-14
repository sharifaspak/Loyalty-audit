package com.loyalty.marketplace.offers.stores.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = StoreContactPersonDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class StoreContactPersonDomainTest {
	
	private StoreContactPersonDomain storeContactPersonDomain;

	@Before
	public void setUp() {
		
		storeContactPersonDomain = new StoreContactPersonDomain("", 
				"", "", "", "", "", "");

	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
    @Test
	public void testGetters() {

		assertNotNull(storeContactPersonDomain.getEmailId());
		assertNotNull(storeContactPersonDomain.getMobileNumber());
		assertNotNull(storeContactPersonDomain.getFirstName());
		assertNotNull(storeContactPersonDomain.getLastName());
		assertNotNull(storeContactPersonDomain.getFaxNumber());
		assertNotNull(storeContactPersonDomain.getUserName());
		assertNotNull(storeContactPersonDomain.getPassword());

	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(storeContactPersonDomain.toString());
	}

}
