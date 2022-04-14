package com.loyalty.marketplace.merchants.outbound.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.outbound.dto.CommonApiStatus;
import com.loyalty.marketplace.outbound.dto.Result;

@SpringBootTest(classes = ListMerchantImagesResponseTest.class)
@ActiveProfiles("unittest")
public class ListMerchantImagesResponseTest {

	private ListMerchantImagesResponse merchantImagesResponseDto;
	private String extTransactionId;
	
	@Before
	public void setUp(){
		extTransactionId = "123";
		merchantImagesResponseDto = new ListMerchantImagesResponse();
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(merchantImagesResponseDto.toString());
	}
	
	@Test
	public void testGetApiStatus()
	{
		CommonApiStatus commonApiStatus = new CommonApiStatus(extTransactionId);
		merchantImagesResponseDto.setApiStatus(commonApiStatus);
		assertEquals(merchantImagesResponseDto.getApiStatus(), commonApiStatus);
	}
	
	@Test
	public void testGetResult()
	{
		Result result = new Result();
		merchantImagesResponseDto.setResult(result);
		assertEquals(merchantImagesResponseDto.getResult(), result);
	}
	
	@Test
	public void testGetListMerchantImages()
	{
		List<MerchantImagesResult> listmerchantImagesResult = new ArrayList<MerchantImagesResult>();
		merchantImagesResponseDto.setListMerchantImages(listmerchantImagesResult);
		assertEquals(merchantImagesResponseDto.getListMerchantImages(), listmerchantImagesResult);
	}
	
}
