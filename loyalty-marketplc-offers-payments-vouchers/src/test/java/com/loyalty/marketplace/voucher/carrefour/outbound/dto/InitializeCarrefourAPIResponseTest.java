package com.loyalty.marketplace.voucher.carrefour.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.voucher.carrefour.inbound.dto.AckMessage;
import com.loyalty.marketplace.voucher.carrefour.inbound.dto.AdditionalInfo;
import com.loyalty.marketplace.voucher.carrefour.outbound.dto.InitializeCarrefourAPIResponse.ResponseData;

@SpringBootTest(classes = InitializeCarrefourAPIResponse.class)
@ActiveProfiles("unittest")
public class InitializeCarrefourAPIResponseTest {

	private InitializeCarrefourAPIResponse initializeCarrefourAPIResponse;
	private ResponseData responseData;

	@Before
	public void setUp() throws Exception {
		initializeCarrefourAPIResponse = new InitializeCarrefourAPIResponse();
		responseData = new InitializeCarrefourAPIResponse.ResponseData();

		initializeCarrefourAPIResponse.setResponseData(responseData);
		initializeCarrefourAPIResponse.setAckMessage(new AckMessage());

		responseData.setAdditionalInfo(new ArrayList<AdditionalInfo>());
		responseData.setIsSuccessful("");
		responseData.setSessionID("");
		responseData.setTransactionID("");
	}

	@Test
	public void test() {
		assertNotNull(initializeCarrefourAPIResponse.getAckMessage());
		assertNotNull(initializeCarrefourAPIResponse.getResponseData());

		assertNotNull(responseData.getAdditionalInfo());
		assertNotNull(responseData.getIsSuccessful());
		assertNotNull(responseData.getSessionID());
		assertNotNull(responseData.getTransactionID());
	}

	@Test
	public void testToString() {
		assertNotNull(initializeCarrefourAPIResponse.toString());
		assertNotNull(responseData.toString());
	}

}
