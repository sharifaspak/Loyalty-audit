package com.loyalty.marketplace.offers.member.management.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = GetMemberResponseDto.class)
@ActiveProfiles("unittest")
public class GetMemberResponseDtoTest {

	private GetMemberResponseDto getMemberResponseDto;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		getMemberResponseDto = new GetMemberResponseDto();
		
		getMemberResponseDto.setTransactionId("");
		getMemberResponseDto.setAccountsInfo(new ArrayList<AccountInfoDto>());
		getMemberResponseDto.setMemberInfo(new MembershipDetailsDto());
		
		GetMemberResponseDto member = new GetMemberResponseDto("", new ArrayList<AccountInfoDto>(), new MembershipDetailsDto());
		member.setTransactionId("");
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(getMemberResponseDto.getTransactionId());
		assertNotNull(getMemberResponseDto.getAccountsInfo());
		assertNotNull(getMemberResponseDto.getMemberInfo());
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(getMemberResponseDto.toString());
	}
	
}
