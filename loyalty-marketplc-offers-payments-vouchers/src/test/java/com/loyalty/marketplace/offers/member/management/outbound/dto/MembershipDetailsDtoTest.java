package com.loyalty.marketplace.offers.member.management.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = MembershipDetailsDto.class)
@ActiveProfiles("unittest")
public class MembershipDetailsDtoTest {

	private MembershipDetailsDto membershipDetailsDto;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		membershipDetailsDto = new MembershipDetailsDto();
		membershipDetailsDto.setAtgUsername("");
		membershipDetailsDto.setBlockedReason("");
		membershipDetailsDto.setCoBrandedCard(true);
		membershipDetailsDto.setMembershipNotes(new ArrayList<>());
		membershipDetailsDto.setTop3Account(new ArrayList<>());
		membershipDetailsDto.setTierLevelInArabic("");
		membershipDetailsDto.setPrimaryAccount("");
		membershipDetailsDto.setTierLevel("");
		membershipDetailsDto.setTotalTierPoints(100);
		membershipDetailsDto.setMembershipCode("");
		membershipDetailsDto.setAtgUsername("");
		membershipDetailsDto.setStartDate(new Date());
		membershipDetailsDto.setEndDate(new Date());
		membershipDetailsDto.setStatus("");
		membershipDetailsDto.setFlagBlocked(true);
		membershipDetailsDto.setBlockedReason("");
		membershipDetailsDto.setFirstAccessFlag(true);
		membershipDetailsDto.setPointsTonextTierLevel(100);
		membershipDetailsDto.setLifetimeSavings(100.25);
		membershipDetailsDto.setTotalPoints(100);
		membershipDetailsDto.setNextExpiryDate(new Date());
		membershipDetailsDto.setNextExpiryPoints(100);
		membershipDetailsDto.setTop3Account(new ArrayList<String>());
		membershipDetailsDto.setPrimaryAccount("");
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(membershipDetailsDto.getAtgUsername());
		assertNotNull(membershipDetailsDto.getBlockedReason());
		assertNotNull(membershipDetailsDto.isCoBrandedCard());
		assertNotNull(membershipDetailsDto.getMembershipNotes());
		assertNotNull(membershipDetailsDto.getTop3Account());
		assertNotNull(membershipDetailsDto.getTierLevelInArabic());
		assertNotNull(membershipDetailsDto.getPrimaryAccount());
		assertNotNull(membershipDetailsDto.getTierLevel());
		assertNotNull(membershipDetailsDto.getTotalTierPoints());
		assertNotNull(membershipDetailsDto.getMembershipCode());
		assertNotNull(membershipDetailsDto.getAtgUsername());
		assertNotNull(membershipDetailsDto.getStartDate());
		assertNotNull(membershipDetailsDto.getEndDate());
		assertNotNull(membershipDetailsDto.getStatus());
		assertNotNull(membershipDetailsDto.isFlagBlocked());
		assertNotNull(membershipDetailsDto.getBlockedReason());
		assertNotNull(membershipDetailsDto.isFirstAccessFlag());
		assertNotNull(membershipDetailsDto.getPointsTonextTierLevel());
		assertNotNull(membershipDetailsDto.getLifetimeSavings());
		assertNotNull(membershipDetailsDto.getTotalPoints());
		assertNotNull(membershipDetailsDto.getNextExpiryDate());
		assertNotNull(membershipDetailsDto.getNextExpiryPoints());
		assertNotNull(membershipDetailsDto.getTop3Account());
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(membershipDetailsDto.toString());
	}
	
}
