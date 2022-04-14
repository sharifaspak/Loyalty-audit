package com.loyalty.marketplace.offers.merchants.domain.model;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = BarcodeDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class BarcodeDomainTest {

	private BarcodeDomain barcodeDomain;

	private String id;
	private String name;
	private String description;
	private String usrCreated;
	private String usrUpdated;
	private Date dtCreated;
	private Date dtUpdated;

	@Before
	public void setUp() {

		id = "id";
		name = "name";
		description = "description";
		usrCreated = "usrCreated";
		usrUpdated = "usrUpdated";
		dtCreated = new Date();
		dtUpdated = new Date();
		MockitoAnnotations.initMocks(this);
		barcodeDomain = new BarcodeDomain(id, name, description, usrCreated, usrUpdated, dtCreated, dtUpdated);

	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {

		assertNotNull(barcodeDomain.getId());
		assertNotNull(barcodeDomain.getName());
		assertNotNull(barcodeDomain.getDescription());
		assertNotNull(barcodeDomain.getUsrCreated());
		assertNotNull(barcodeDomain.getUsrUpdated());
		assertNotNull(barcodeDomain.getDtCreated());
		assertNotNull(barcodeDomain.getDtUpdated());

	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(barcodeDomain.toString());
	}
}
