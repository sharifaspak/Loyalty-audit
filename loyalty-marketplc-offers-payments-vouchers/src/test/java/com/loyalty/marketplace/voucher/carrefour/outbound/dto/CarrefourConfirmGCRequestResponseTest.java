package com.loyalty.marketplace.voucher.carrefour.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.voucher.carrefour.inbound.dto.AckMessage;
import com.loyalty.marketplace.voucher.carrefour.inbound.dto.AdditionalInfo;
import com.loyalty.marketplace.voucher.carrefour.outbound.dto.CarrefourConfirmGCRequestResponse.ResponseData;

@SpringBootTest(classes = CarrefourConfirmGCRequestResponse.class)
@ActiveProfiles("unittest")
public class CarrefourConfirmGCRequestResponseTest {

	private CarrefourConfirmGCRequestResponse carrefourConfirmGCRequestResponse;
	private ResponseData responseData;

	@Before
	public void setUp() throws Exception {
		carrefourConfirmGCRequestResponse = new CarrefourConfirmGCRequestResponse();
		responseData = new CarrefourConfirmGCRequestResponse.ResponseData();

		carrefourConfirmGCRequestResponse.setAckMessage(new AckMessage());
		carrefourConfirmGCRequestResponse.setResponseData(responseData);

		responseData.setAdditionalInfo(new ArrayList<AdditionalInfo>());
		responseData.setBalance(BigDecimal.ZERO);
		responseData.setPointBalance(BigDecimal.ZERO);
		responseData.setIsSuccessful("");
		responseData.setNote("");
		responseData.setPinCode("");
		responseData.setReferenceNumber("");
		responseData.setTransactionID("");
	}

	@Test
	public void test() {
		assertNotNull(carrefourConfirmGCRequestResponse.getAckMessage());
		assertNotNull(carrefourConfirmGCRequestResponse.getResponseData());

		assertNotNull(responseData.getAdditionalInfo());
		assertNotNull(responseData.getBalance());
		assertNotNull(responseData.getIsSuccessful());
		assertNotNull(responseData.getNote());
		assertNotNull(responseData.getPinCode());
		assertNotNull(responseData.getPointBalance());
		assertNotNull(responseData.getReferenceNumber());
		assertNotNull(responseData.getTransactionID());
	}

	@Test
	public void testToString() {
		assertNotNull(carrefourConfirmGCRequestResponse.toString());
		assertNotNull(responseData.toString());
	}

}
