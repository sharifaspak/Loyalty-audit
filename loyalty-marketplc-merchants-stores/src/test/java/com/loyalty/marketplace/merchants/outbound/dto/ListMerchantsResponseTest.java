package com.loyalty.marketplace.merchants.outbound.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ListMerchantsResponse.class)
@ActiveProfiles("unittest")
public class ListMerchantsResponseTest {

	private ListMerchantsResponse merchantsResponseDto;
	private List<MerchantResult> testObject;
	private String extTransactionId;
	
	@Before
	public void setUp(){
		extTransactionId = "123";
		merchantsResponseDto = new ListMerchantsResponse(extTransactionId);
		testObject = new ArrayList<MerchantResult>();
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(merchantsResponseDto.toString());
	}
	
	@Test
	public void testGetListMerchants()
	{
		merchantsResponseDto.setListMerchants(testObject);
		assertEquals(merchantsResponseDto.getListMerchants(), testObject);
	}
	
}
