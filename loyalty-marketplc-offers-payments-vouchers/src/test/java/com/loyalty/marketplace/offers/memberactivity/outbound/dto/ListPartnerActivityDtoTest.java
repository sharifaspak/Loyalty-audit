package com.loyalty.marketplace.offers.memberactivity.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.offers.memberactivity.inbound.dto.PartnerActivityDto;

@SpringBootTest(classes = ListPartnerActivityDto.class)
@ActiveProfiles("unittest")
public class ListPartnerActivityDtoTest {

	private ListPartnerActivityDto listPartnerActivityDto;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		listPartnerActivityDto = new ListPartnerActivityDto();
		listPartnerActivityDto.setListPartnerActivity(new ArrayList<PartnerActivityDto>() );
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(listPartnerActivityDto.getListPartnerActivity());
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(listPartnerActivityDto.toString());
	}
	
}