package com.loyalty.marketplace.voucher.carrefour.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.voucher.carrefour.inbound.dto.CarrefourConfirmGCRequestRequest.DataHeader;

@SpringBootTest(classes = CarrefourConfirmGCRequestRequest.class)
@ActiveProfiles("unittest")
public class CarrefourConfirmGCRequestRequestTest {

	private CarrefourConfirmGCRequestRequest carrefourConfirmGCRequestRequest;
	private DataHeader dataHeader;

	@Before
	public void setUp() throws Exception {
		carrefourConfirmGCRequestRequest = new CarrefourConfirmGCRequestRequest();
		dataHeader = new CarrefourConfirmGCRequestRequest.DataHeader();

		carrefourConfirmGCRequestRequest.setApplicationHeader(new ApplicationHeader());
		carrefourConfirmGCRequestRequest.setDataHeader(dataHeader);

		dataHeader.setAdditionalInfo(new ArrayList<AdditionalInfo>());
		dataHeader.setCashierID("");
		dataHeader.setMerchantID("");
		dataHeader.setNote("");
		dataHeader.setReferenceNumber("");
		dataHeader.setSessionID("");
		dataHeader.setTerminalID("");
	}

	@Test
	public void testGetters() {
		assertNotNull(carrefourConfirmGCRequestRequest.getApplicationHeader());
		assertNotNull(carrefourConfirmGCRequestRequest.getDataHeader());

		assertNotNull(dataHeader.getAdditionalInfo());
		assertNotNull(dataHeader.getCashierID());
		assertNotNull(dataHeader.getMerchantID());
		assertNotNull(dataHeader.getNote());
		assertNotNull(dataHeader.getReferenceNumber());
		assertNotNull(dataHeader.getSessionID());
		assertNotNull(dataHeader.getTerminalID());
	}

	@Test
	public void testToString() {
		assertNotNull(carrefourConfirmGCRequestRequest.toString());
		assertNotNull(dataHeader.toString());
	}

}
