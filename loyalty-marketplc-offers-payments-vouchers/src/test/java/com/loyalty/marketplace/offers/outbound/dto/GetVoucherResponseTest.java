package com.loyalty.marketplace.offers.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = GetVoucherResponse.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class GetVoucherResponseTest {

	private GetVoucherResponse getVoucherResponse;

	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);
		getVoucherResponse = new GetVoucherResponse();
		getVoucherResponse.setVoucherCode(new ArrayList<String>());
		getVoucherResponse.setStatus("");

	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {

		assertNotNull(getVoucherResponse.getVoucherCode());
		assertNotNull(getVoucherResponse.getStatus());

	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {

		assertNotNull(getVoucherResponse.toString());

	}

}
