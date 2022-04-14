package com.loyalty.marketplace.voucher.carrefour.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.voucher.carrefour.inbound.dto.CarrefourGCRequestRequest.DataHeader;

@SpringBootTest(classes = CarrefourGCRequestRequest.class)
@ActiveProfiles("unittest")
public class CarrefourGCRequestRequestTest {

	private CarrefourGCRequestRequest carrefourGCRequestRequest;
	private DataHeader dataHeader;

	@Before
	public void setUp() throws Exception {
		carrefourGCRequestRequest = new CarrefourGCRequestRequest();
		dataHeader = new CarrefourGCRequestRequest.DataHeader();

		carrefourGCRequestRequest.setApplicationHeader(new ApplicationHeader());
		carrefourGCRequestRequest.setDataHeader(dataHeader);

		dataHeader.setAdditionalInfo(new ArrayList<AdditionalInfo>());
		dataHeader.setAmount(BigDecimal.ZERO);
		dataHeader.setBasketAmount(BigDecimal.ZERO);
		dataHeader.setBasketCategories("");
		dataHeader.setCardNumber("");
		dataHeader.setCashierID("");
		dataHeader.setEan128("");
		dataHeader.setGenCode("");
		dataHeader.setMerchantID("");
		dataHeader.setMobileNo("");
		dataHeader.setMsgType("");
		dataHeader.setNote("");
		dataHeader.setPinCode("");
		dataHeader.setReason("");
		dataHeader.setSessionID("");
		dataHeader.setTerminalID("");
		dataHeader.setTrackToData("");
		dataHeader.setTransactionNumber("");
		dataHeader.setValidityDate("");

	}

	@Test
	public void testGetters() {
		assertNotNull(carrefourGCRequestRequest.getApplicationHeader());
		assertNotNull(carrefourGCRequestRequest.getDataHeader());

		assertNotNull(dataHeader.getAdditionalInfo());
		assertNotNull(dataHeader.getAmount());
		assertNotNull(dataHeader.getBasketAmount());
		assertNotNull(dataHeader.getBasketCategories());
		assertNotNull(dataHeader.getCardNumber());
		assertNotNull(dataHeader.getCashierID());
		assertNotNull(dataHeader.getEan128());
		assertNotNull(dataHeader.getGenCode());
		assertNotNull(dataHeader.getMerchantID());
		assertNotNull(dataHeader.getMobileNo());
		assertNotNull(dataHeader.getMsgType());
		assertNotNull(dataHeader.getNote());
		assertNotNull(dataHeader.getPinCode());
		assertNotNull(dataHeader.getReason());
		assertNotNull(dataHeader.getSessionID());
		assertNotNull(dataHeader.getTerminalID());
		assertNotNull(dataHeader.getTrackToData());
		assertNotNull(dataHeader.getTransactionNumber());
		assertNotNull(dataHeader.getValidityDate());

	}

	@Test
	public void testToString() {
		assertNotNull(carrefourGCRequestRequest.toString());
		assertNotNull(dataHeader.toString());
	}

}
