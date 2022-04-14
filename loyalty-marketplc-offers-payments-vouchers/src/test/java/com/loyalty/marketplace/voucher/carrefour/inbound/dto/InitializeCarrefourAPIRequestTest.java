package com.loyalty.marketplace.voucher.carrefour.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.voucher.carrefour.inbound.dto.InitializeCarrefourAPIRequest.DataHeader;

@SpringBootTest(classes = InitializeCarrefourAPIRequest.class)
@ActiveProfiles("unittest")
public class InitializeCarrefourAPIRequestTest {

	private InitializeCarrefourAPIRequest initializeCarrefourAPIRequest;
	private DataHeader dataHeader;

	@Before
	public void setUp() throws Exception {
		initializeCarrefourAPIRequest = new InitializeCarrefourAPIRequest();
		dataHeader = new InitializeCarrefourAPIRequest.DataHeader();

		initializeCarrefourAPIRequest.setApplicationHeader(new ApplicationHeader());
		initializeCarrefourAPIRequest.setDataHeader(dataHeader);

		dataHeader.setAdditionalInfo(new ArrayList<AdditionalInfo>());
		dataHeader.setCashierID("");
		dataHeader.setMerchantID("");
		dataHeader.setPassphrase("");
		dataHeader.setTerminalID("");
	}

	@Test
	public void testGetters() {
		assertNotNull(initializeCarrefourAPIRequest.getApplicationHeader());
		assertNotNull(initializeCarrefourAPIRequest.getDataHeader());

		assertNotNull(dataHeader.getAdditionalInfo());
		assertNotNull(dataHeader.getCashierID());
		assertNotNull(dataHeader.getMerchantID());
		assertNotNull(dataHeader.getPassphrase());
		assertNotNull(dataHeader.getTerminalID());
	}

	@Test
	public void testToString() {
		assertNotNull(initializeCarrefourAPIRequest.toString());
		assertNotNull(dataHeader.toString());
	}

}
