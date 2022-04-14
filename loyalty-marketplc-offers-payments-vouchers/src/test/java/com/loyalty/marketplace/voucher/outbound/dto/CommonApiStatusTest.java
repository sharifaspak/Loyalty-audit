package com.loyalty.marketplace.voucher.outbound.dto;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CommonApiStatus.class)
@ActiveProfiles("unittest")
public class CommonApiStatusTest {
	
	private CommonApiStatus commonApiStatus;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		commonApiStatus = new CommonApiStatus("externalTransactionId");
		commonApiStatus.setErrors(new ArrayList<Errors>());
		commonApiStatus.setExternalTransactionId("");
		commonApiStatus.setMessage("");
		commonApiStatus.setOverallStatus("");
		commonApiStatus.setStatusCode(0);
	}

	@Test
	public void testGetters() {
		assertNotNull(commonApiStatus.getErrors());
		assertNotNull(commonApiStatus.getExternalTransactionId());
		assertNotNull(commonApiStatus.getMessage());
		assertNotNull(commonApiStatus.getOverallStatus());
		assertNotNull(commonApiStatus.getStatusCode());
	}
	
	@Test
	public void testToString() {
		assertNotNull(commonApiStatus.toString());
	}

}