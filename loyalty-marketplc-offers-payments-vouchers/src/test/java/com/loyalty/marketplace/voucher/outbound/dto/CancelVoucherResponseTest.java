package com.loyalty.marketplace.voucher.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CancelVoucherResponse.class)
@ActiveProfiles("unittest")
public class CancelVoucherResponseTest {

	private CancelVoucherResponse cancelVoucherResponse;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		cancelVoucherResponse = new CancelVoucherResponse();
		cancelVoucherResponse.setStatus(false);
	}

	@Test
	public void testGetter() {
		assertNotNull(cancelVoucherResponse.isStatus());
	}
	
	@Test
	public void testToString() {
		assertNotNull(cancelVoucherResponse.toString());
	}

}