package com.loyalty.marketplace.merchants.outbound.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ListMerchantsStoreOfferResponseTest.class)
@ActiveProfiles("unittest")
public class ListMerchantsStoreOfferResponseTest {

	private ListMerchantsStoreOfferResponse merchantsStoreOfferResponseDto;
	private MerchantStoreOfferResult testObject;
	private String extTransactionId;
	
	@Before
	public void setUp(){
		extTransactionId = "123";
		merchantsStoreOfferResponseDto = new ListMerchantsStoreOfferResponse(extTransactionId);
		testObject = new MerchantStoreOfferResult();
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(merchantsStoreOfferResponseDto.toString());
	}
	
	@Test
	public void testGetListMerchants()
	{
		merchantsStoreOfferResponseDto.setListMerchants(testObject);
		assertEquals(merchantsStoreOfferResponseDto.getListMerchants(), testObject);
	}
	
}
