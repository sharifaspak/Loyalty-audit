package com.loyalty.marketplace.offers.member.management.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = GetMemberResponse.class)
@ActiveProfiles("unittest")
public class GetMemberResponseTest {

	private GetMemberResponse getMemberResponse;

	@Before
	public void setUp() {
		getMemberResponse = new GetMemberResponse();
		getMemberResponse.setAccountId("");
		getMemberResponse.setMembershipCode("100");
		getMemberResponse.setAccountNumber("");
		getMemberResponse.setChannelId("");
		getMemberResponse.setEmailVerificationStatus("");
		getMemberResponse.setTierLevelName("");
		getMemberResponse.setFirstName("");
		getMemberResponse.setLastName("");
		getMemberResponse.setGender("");
		getMemberResponse.setNationality("");
		getMemberResponse.setNumberType("");
		getMemberResponse.setAccountStatus("");
		getMemberResponse.setEmail("");
		getMemberResponse.setTotalAccountPoints(0);
		getMemberResponse.setTotalTierPoints(0);
		getMemberResponse.setTop3Account(true);
		getMemberResponse.setHasCoBranded(true);
		getMemberResponse.setAgeEligibleFlag(true);
		getMemberResponse.setFirstAccess(true);
		getMemberResponse.setSubscribed(true);
		getMemberResponse.setPrimaryAccount(true);
		getMemberResponse.setCustomerType(new ArrayList<>());
		getMemberResponse.setCustomerSegment(new ArrayList<>());
		getMemberResponse.setCobrandedCardDetails(new ArrayList<>());
		getMemberResponse.setEligibleFeatures(new ArrayList<>());
		getMemberResponse.setNonEligibleFeatures(new ArrayList<>());
		getMemberResponse.setEligiblePaymentMethod(new ArrayList<>());
		getMemberResponse.setDob(new Date());
		getMemberResponse.setAccountStartDate(new Date());
		getMemberResponse.setReferralAccountNumber("");
		getMemberResponse.setReferralBonusCode("");
		
	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(getMemberResponse.getAccountId());
		assertNotNull(getMemberResponse.getAccountNumber());
		assertNotNull(getMemberResponse.getMembershipCode());
		assertNotNull(getMemberResponse.getChannelId());
		assertNotNull(getMemberResponse.getEmailVerificationStatus());
		assertNotNull(getMemberResponse.getTierLevelName());
		assertNotNull(getMemberResponse.getFirstName());
		assertNotNull(getMemberResponse.getLastName());
		assertNotNull(getMemberResponse.getGender());
		assertNotNull(getMemberResponse.getNationality());
		assertNotNull(getMemberResponse.getNumberType());
		assertNotNull(getMemberResponse.getAccountStatus());
		assertNotNull(getMemberResponse.getEmail());
	    assertNotNull(getMemberResponse.getTotalAccountPoints());
		assertNotNull(getMemberResponse.getTotalTierPoints());
		assertNotNull(getMemberResponse.isTop3Account());
		assertNotNull(getMemberResponse.isHasCoBranded());
		assertNotNull(getMemberResponse.isAgeEligibleFlag());
		assertNotNull(getMemberResponse.isFirstAccess());
		assertNotNull(getMemberResponse.isSubscribed());
		assertNotNull(getMemberResponse.isPrimaryAccount());
		assertNotNull(getMemberResponse.getCustomerType());
		assertNotNull(getMemberResponse.getCobrandedCardDetails());
		assertNotNull(getMemberResponse.getCustomerSegment());
		assertNotNull(getMemberResponse.getCobrandedCardDetails());
		assertNotNull(getMemberResponse.getEligibleFeatures());
		assertNotNull(getMemberResponse.getNonEligibleFeatures());
		assertNotNull(getMemberResponse.getEligiblePaymentMethod());
		assertNotNull(getMemberResponse.getDob());
		assertNotNull(getMemberResponse.getAccountStartDate());
		assertNotNull(getMemberResponse.getReferralAccountNumber());
		assertNotNull(getMemberResponse.getReferralBonusCode());
	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(getMemberResponse.toString());
	}

}
