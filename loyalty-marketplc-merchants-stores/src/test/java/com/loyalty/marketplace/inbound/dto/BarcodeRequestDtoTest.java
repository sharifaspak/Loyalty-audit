package com.loyalty.marketplace.inbound.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = BarcodeRequestDto.class)
@ActiveProfiles("unittest")
public class BarcodeRequestDtoTest {

	private BarcodeRequestDto barcodeRequestDto;
	
	@Before
	public void setUp(){
		barcodeRequestDto = new BarcodeRequestDto();
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(barcodeRequestDto.toString());
	}
	
	@Test
	public void testGetBarcodes()
	{
		List<BarcodeDto> barcodes = new ArrayList<BarcodeDto>();
		barcodeRequestDto.setBarcodes(barcodes);;
		assertEquals(barcodeRequestDto.getBarcodes(), barcodes);
	}
	
	
}
