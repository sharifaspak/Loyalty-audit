package com.loyalty.marketplace.offers.memberactivity.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = UOMDto.class)
@ActiveProfiles("unittest")
public class UOMDtoTest {

	private UOMDto uOMDto;
	
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		uOMDto = new UOMDto();
		uOMDto.setCode("");
		uOMDto.setName("");
		uOMDto.setDescription("");
		uOMDto.setStartDate(new Date());
		uOMDto.setEndDate(new Date());
		
		UOMDto uOM = new UOMDto("", "", "", new Date(), new Date());
		uOM.setCode("");
		
	}
	

	@Test
	public void testGetters() {
		assertNotNull(uOMDto.getCode());
		assertNotNull(uOMDto.getName());
		assertNotNull(uOMDto.getDescription());
		assertNotNull(uOMDto.getStartDate());
		assertNotNull(uOMDto.getEndDate());
		
	}

	
	@Test
	public void testToString() {
		assertNotNull(uOMDto.toString());
	}
	
}