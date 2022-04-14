package com.loyalty.marketplace.offers.decision.manager.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = MemberDetails.class)
@ActiveProfiles("unittest")
public class MemberDetailsTest {

	private MemberDetails memberDetails;

	@Before
	public void setUp() {
		memberDetails = new MemberDetails();
		memberDetails.setAccountNumber("");
		memberDetails.setCoBrand(new ArrayList<String>());
		memberDetails.setCustomerType(new ArrayList<String>());
		memberDetails.setHasCobranded(true);
		memberDetails.setMembershipCode("");
		memberDetails.setSubscribed(true);
		memberDetails.setTierLevelName("");
		memberDetails.setBillPaymentAmount(0.0);
		memberDetails.setRechargeAmount(0.0);
		memberDetails.setActiveCoBranded(true);
	}

	/**
	 * Test Getters
	 */
	@Test
	public void testGetters() {
		assertNotNull(memberDetails.isHasCobranded());
		assertNotNull(memberDetails.isSubscribed());
		assertNotNull(memberDetails.getAccountNumber());
		assertNotNull(memberDetails.getCoBrand());
		assertNotNull(memberDetails.getCustomerType());
		assertNotNull(memberDetails.getMembershipCode());
		assertNotNull(memberDetails.getTierLevelName());
		assertNotNull(memberDetails.getBillPaymentAmount());
		assertNotNull(memberDetails.getRechargeAmount());
		assertNotNull(memberDetails.isActiveCoBranded());
	}

	/**
	 * Test toString
	 */
	@Test
	public void testToString() {
		assertNotNull(memberDetails.toString());
	}
	
}
