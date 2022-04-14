package com.loyalty.marketplace.offers.member.management.inbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = MemberDetailRequestDto.class)
@ActiveProfiles("unittest")
public class MemberDetailRequestDtoTest {

	private MemberDetailRequestDto memberDetails;
	
	@Before
	public void setUp(){
		memberDetails = new MemberDetailRequestDto();
		memberDetails.setAccountNumber("100");
		memberDetails.setDocumentId("") ;
		memberDetails.setMembershipCode(""); 
		memberDetails.setEmailId(""); 
		memberDetails.setLoyaltyId(""); 
		memberDetails.setAtgUserName("");
		memberDetails.setIncludeCrmInfo(true); 
		memberDetails.setIncludeEligiblePaymentMethods(true);
		memberDetails.setIncludeEligibilityMatrix(true); 
		memberDetails.setIncludeLinkedAccount(true); 
		memberDetails.setIncludeAccountInterest(true);
		memberDetails.setIncludeCancelledAccount(true);
		memberDetails.setIncludeLoginInfo(true);
		memberDetails.setCallCustomerInterest(true); 
		memberDetails.setCallSubscription(true); 
		memberDetails.setCallMemberActivity(true); 
		memberDetails.setCallPointsBank(true); 
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(memberDetails.getAccountNumber());
		assertNotNull(memberDetails.getDocumentId());
		assertNotNull(memberDetails.getMembershipCode()); 
		assertNotNull(memberDetails.getEmailId()); 
		assertNotNull(memberDetails.getLoyaltyId()); 
		assertNotNull(memberDetails.getAtgUserName());
		assertNotNull(memberDetails.isIncludeCrmInfo()); 
		assertNotNull(memberDetails.isIncludeEligiblePaymentMethods());
		assertNotNull(memberDetails.isIncludeEligibilityMatrix()); 
		assertNotNull(memberDetails.isIncludeLinkedAccount()); 
		assertNotNull(memberDetails.isIncludeAccountInterest());
		assertNotNull(memberDetails.isIncludeCancelledAccount());
		assertNotNull(memberDetails.isIncludeLoginInfo());
		assertNotNull(memberDetails.isCallCustomerInterest()); 
		assertNotNull(memberDetails.isCallSubscription()); 
		assertNotNull(memberDetails.isCallMemberActivity()); 
		assertNotNull(memberDetails.isCallPointsBank());
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(memberDetails.toString());
	}
	
}
