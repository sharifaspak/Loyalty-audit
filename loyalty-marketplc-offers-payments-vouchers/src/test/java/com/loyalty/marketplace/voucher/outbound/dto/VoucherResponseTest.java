package com.loyalty.marketplace.voucher.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.outbound.dto.CommonApiStatus;
import com.loyalty.marketplace.outbound.dto.Result;

@SpringBootTest(classes = VoucherResponse.class)
@ActiveProfiles("unittest")
public class VoucherResponseTest {

	private VoucherResponse voucherResponse;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		voucherResponse = new VoucherResponse("externalTransactionId");
		voucherResponse.setApiStatus(new CommonApiStatus("externalTransactionId"));
		voucherResponse.setBulkErrorAPIResponse(new ArrayList<>());
		voucherResponse.setErrorAPIResponse(0, "");
		voucherResponse.setResult(new Result());
		voucherResponse.setResult("", "");
		voucherResponse.setVoucherResult(new ArrayList<VoucherResult>());
		voucherResponse.addErrorAPIResponse(0, "");
	}

	@Test
	public void testGetters() {
		assertNotNull(voucherResponse.getApiStatus());
		assertNotNull(voucherResponse.getResult());
		assertNotNull(voucherResponse.getVoucherResult());
	}

	@Test
	public void testToString() {
		assertNotNull(voucherResponse.toString());
	}

}