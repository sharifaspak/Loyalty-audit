package com.loyalty.marketplace.voucher.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest(classes = ListVoucherRequest.class)
@ActiveProfiles("unittest")
public class ListVoucherRequestTest {

	private ListVoucherRequest listVoucherRequest;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		listVoucherRequest = new ListVoucherRequest();
		listVoucherRequest.setBusinessIds(new ArrayList<>());
	}

	@Test
	public void testGetters() {
		assertNotNull(listVoucherRequest.getBusinessIds());
	}
	
	@Test
	public void testToString() {
		assertNotNull(listVoucherRequest.toString());
	}

}