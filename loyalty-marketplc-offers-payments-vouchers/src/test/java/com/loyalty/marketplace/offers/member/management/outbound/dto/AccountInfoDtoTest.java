package com.loyalty.marketplace.offers.member.management.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = AccountInfoDto.class)
@ActiveProfiles("unittest")
public class AccountInfoDtoTest {

	private AccountInfoDto accountInfo;
	
	@Before
	public void setUp(){
		accountInfo = new AccountInfoDto();
		accountInfo.setAccountId("");
		accountInfo.setChannelId("");
		accountInfo.setAgeEligibleFlag(true);
		accountInfo.setCobrandedCardDetails(new ArrayList<>());
		accountInfo.setCrmInfo(new CRMResponse());
		accountInfo.setEligibleFeatures(new ArrayList<>());
		accountInfo.setEmailVerification(new EmailVerificationDto());
		accountInfo.setNonEligibleFeatures(new ArrayList<>());
		accountInfo.setInterest(new ArrayList<>());
		accountInfo.setLoginDate(new Date());
		accountInfo.setReferralBonusAccount(new ReferralBonusDetails());
		accountInfo.setReferralCode("");
		accountInfo.setSharingBonusCounterRemaining(0);
		accountInfo.setAccountNumber("100L");
		accountInfo.setCustomerType(new ArrayList<>());
		accountInfo.setLoyaltyId("");
		accountInfo.setDeviceId("");
		accountInfo.setLanguage("");
		accountInfo.setUilanguage("");
		accountInfo.setMobileNumber("100L");
		accountInfo.setAccountAuthority("");
		accountInfo.setAccountStartDate(new Date());
		accountInfo.setEmail("");
		accountInfo.setFirstName("");
		accountInfo.setLastName("");
		accountInfo.setMiddleName("");
		accountInfo.setAccountStatus("");
		accountInfo.setGender("");
		accountInfo.setNationality("");
		accountInfo.setDob(new Date());
		accountInfo.setBlocked(true);
		accountInfo.setBlockReason("");
		accountInfo.setDocumentId("");
		accountInfo.setPartyId("");
		accountInfo.setNumberType("");
		accountInfo.setFirstAccessFlag(true);
		accountInfo.setPrimary(true);
		accountInfo.setAtgUserName("");
		accountInfo.setSubscribtionStatus("");
		accountInfo.setLifetimeSavings(100.25);
		accountInfo.setTotalPoints(0);
		accountInfo.setNextExpiryDate(new Date());
		accountInfo.setNextExpiryPoints(0);
		accountInfo.setCeasedPoints(0);
		accountInfo.setEligiblePaymentMethod(new ArrayList<>());
		accountInfo.setCeasedPoints(0);
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(accountInfo.getAccountId());
		assertNotNull(accountInfo.getChannelId());
		assertNotNull(accountInfo.isAgeEligibleFlag());
		assertNotNull(accountInfo.getCobrandedCardDetails());
		assertNotNull(accountInfo.getCrmInfo());
		assertNotNull(accountInfo.getEligibleFeatures());
		assertNotNull(accountInfo.getEmailVerification());
		assertNotNull(accountInfo.getNonEligibleFeatures());
		assertNotNull(accountInfo.getInterest());
		assertNotNull(accountInfo.getLoginDate());
		assertNotNull(accountInfo.getReferralBonusAccount());
		assertNotNull(accountInfo.getReferralCode());
		assertNotNull(accountInfo.getSharingBonusCounterRemaining());
		assertNotNull(accountInfo.getChannelId());
		assertNotNull(accountInfo.getAccountNumber());
		assertNotNull(accountInfo.getCustomerType());
		assertNotNull(accountInfo.getLoyaltyId());
		assertNotNull(accountInfo.getDeviceId());
		assertNotNull(accountInfo.getLanguage());
		assertNotNull(accountInfo.getUilanguage());
		assertNotNull(accountInfo.getMobileNumber());
		assertNotNull(accountInfo.getAccountAuthority());
		assertNotNull(accountInfo.getAccountStartDate());
		assertNotNull(accountInfo.getEmail());
		assertNotNull(accountInfo.getFirstName());
		assertNotNull(accountInfo.getLastName());
		assertNotNull(accountInfo.getMiddleName());
		assertNotNull(accountInfo.getAccountStatus());
		assertNotNull(accountInfo.getGender());
		assertNotNull(accountInfo.getNationality());
		assertNotNull(accountInfo.getDob());
		assertNotNull(accountInfo.getBlockReason());
		assertNotNull(accountInfo.getDocumentId());
		assertNotNull(accountInfo.getPartyId());
		assertNotNull(accountInfo.getNumberType());
		assertNotNull(accountInfo.getAtgUserName());
		assertNotNull(accountInfo.getSubscribtionStatus());
		assertNotNull(accountInfo.getLifetimeSavings());
		assertNotNull(accountInfo.getTotalPoints());
		assertNotNull(accountInfo.getNextExpiryDate());
		assertNotNull(accountInfo.getNextExpiryPoints());
		assertNotNull(accountInfo.getCeasedPoints());
		assertNotNull(accountInfo.getEligiblePaymentMethod());
	
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(accountInfo.toString());
	}
	
}
