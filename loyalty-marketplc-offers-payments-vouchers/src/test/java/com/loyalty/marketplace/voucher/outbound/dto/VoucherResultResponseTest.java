package com.loyalty.marketplace.voucher.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = VoucherResultResponse.class)
@ActiveProfiles("unittest")
public class VoucherResultResponseTest {
	
	private VoucherResultResponse voucherResultResponse;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		voucherResultResponse = new VoucherResultResponse("externalTransactionId");
		voucherResultResponse.setApiStatus(new CommonApiStatus("externalTransactionId"));
		voucherResultResponse.setBulkErrorAPIResponse(new ArrayList<Errors>());
		voucherResultResponse.setErrorAPIResponse(0, "");
		voucherResultResponse.setResult(new Object());
		voucherResultResponse.addErrorAPIResponse(0, "");
	}

	@Test
	public void testGetters() {
		assertNotNull(voucherResultResponse.getApiStatus());
		assertNotNull(voucherResultResponse.getResult());
	}
	
	@Test
	public void testToString() {
		assertNotNull(voucherResultResponse.toString());
	}

}