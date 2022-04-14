package com.loyalty.marketplace.voucher.carrefour.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CarrefourGCRequestResponse.class)
@ActiveProfiles("unittest")
public class CarrefourGCRequestResponseTest {

	private CarrefourGCRequestResponse.ResponseData responseData;

	@Before
	public void setUp() throws Exception {
		CarrefourGCRequestResponse carrefourGCRequestResponse = new CarrefourGCRequestResponse();
		responseData = new CarrefourGCRequestResponse.ResponseData();

		responseData.setAddedPoints(BigDecimal.ZERO);
		responseData.setAmount(BigDecimal.ZERO);
		responseData.setBalance(BigDecimal.ZERO);
		responseData.setBonusAmount(BigDecimal.ZERO);
		responseData.setEarliestExpiryAmount(BigDecimal.ZERO);
		responseData.setInitialBalance(BigDecimal.ZERO);
		responseData.setPointBalance(BigDecimal.ZERO);
		responseData.setPreviousBalance(BigDecimal.ZERO);
		responseData.setRedeemedPoints(BigDecimal.ZERO);
		responseData.setPreviousPoints(BigDecimal.ZERO);
		responseData.setBarCodeNumber("");
		responseData.setCardNumber("");
		responseData.setCardType("");
		responseData.setCurrency("");
		responseData.setEarliestExpiryDate("");
		responseData.setExpireDate("");
		responseData.setIsSuccessful("");
		responseData.setMobileNo("");
		responseData.setNote("");
		responseData.setPinCode("");
		responseData.setReferenceNumber("");
		responseData.setTransactionID("");
	}

	@Test
	public void test() {
		assertNotNull(responseData.getAddedPoints());
		assertNotNull(responseData.getAdditionalInfo());
		assertNotNull(responseData.getAmount());
		assertNotNull(responseData.getBalance());
		assertNotNull(responseData.getBarCodeNumber());
		assertNotNull(responseData.getBonusAmount());
		assertNotNull(responseData.getCardNumber());
		assertNotNull(responseData.getCardType());
		assertNotNull(responseData.getCurrency());
		assertNotNull(responseData.getEarliestExpiryAmount());
		assertNotNull(responseData.getEarliestExpiryDate());
		assertNotNull(responseData.getExpireDate());
		assertNotNull(responseData.getInitialBalance());
		assertNotNull(responseData.getIsSuccessful());
		assertNotNull(responseData.getMobileNo());
		assertNotNull(responseData.getNote());
		assertNotNull(responseData.getPinCode());
		assertNotNull(responseData.getPointBalance());
		assertNotNull(responseData.getPreviousBalance());
		assertNotNull(responseData.getPreviousPoints());
		assertNotNull(responseData.getRedeemedPoints());
		assertNotNull(responseData.getReferenceNumber());
		assertNotNull(responseData.getTransactionID());
	}
}
