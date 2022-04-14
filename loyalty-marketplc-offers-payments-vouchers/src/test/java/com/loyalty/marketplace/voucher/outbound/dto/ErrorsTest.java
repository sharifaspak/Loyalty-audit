package com.loyalty.marketplace.voucher.outbound.dto;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = Errors.class)
@ActiveProfiles("unittest")
public class ErrorsTest {

	private Errors errors;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		errors = new Errors();
		errors.setCode(0);
		errors.setMessage("");
	}

	@Test
	public void testGetters() {
		assertNotNull(errors.getCode());
		assertNotNull(errors.getMessage());
	}

	@Test
	public void testToString() {
		assertNotNull(errors.toString());
	}

}