package com.loyalty.marketplace.voucher.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest(classes = BurnVoucherRequest.class)
@ActiveProfiles("unittest")
public class BurnVoucherRequestTest {

	private BurnVoucherRequest burnVoucherRequest;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		burnVoucherRequest = new BurnVoucherRequest();
		burnVoucherRequest.setInvoiceId("");	
		burnVoucherRequest.setRemarks("");		
		burnVoucherRequest.setStorePin(0);
		burnVoucherRequest.setVoucherCodes(new ArrayList<String>());
	}

	@Test
	public void testGetters() {
		assertNotNull(burnVoucherRequest.getInvoiceId());		
		assertNotNull(burnVoucherRequest.getRemarks());		
		assertNotNull(burnVoucherRequest.getStorePin());
		assertNotNull(burnVoucherRequest.getVoucherCodes());
	}
	
	@Test
	public void testToString() {
		assertNotNull(burnVoucherRequest.toString());
	}

}