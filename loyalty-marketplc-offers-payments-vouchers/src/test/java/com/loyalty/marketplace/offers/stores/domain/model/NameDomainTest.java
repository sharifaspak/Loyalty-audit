package com.loyalty.marketplace.offers.stores.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = NameDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class NameDomainTest {

	private NameDomain name;

	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);
		name = new NameDomain("En", "Ar");

	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {

		assertNotNull(name.getEnglish());
		assertNotNull(name.getEnglish());

	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(name.toString());
	}

}
