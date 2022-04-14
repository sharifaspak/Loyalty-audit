package com.loyalty.marketplace.offers.helper.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.offers.decision.manager.outbound.dto.RuleResult;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponseDto;
import com.loyalty.marketplace.offers.outbound.database.entity.AdditionalDetails;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCounter;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchasePaymentMethod;
import com.loyalty.marketplace.offers.outbound.database.entity.SubOffer;
import com.loyalty.marketplace.outbound.database.entity.Denomination;

@SpringBootTest(classes = EligibilityInfo.class)
@ActiveProfiles("unittest")
public class EligibilityInfoTest {

	private EligibilityInfo eligibilityInfo;
	
	@Before
	public void setUp(){
		eligibilityInfo = new EligibilityInfo();
		eligibilityInfo.setHeaders(new Headers());
		eligibilityInfo.setBirthdayInfoRequired(true);
		eligibilityInfo.setMember(true);
		eligibilityInfo.setAccountNumber("");
		eligibilityInfo.setRuleResult(new RuleResult());
		eligibilityInfo.setCommonSegmentNames(new ArrayList<>());
		eligibilityInfo.setOffer(new OfferCatalog());
		eligibilityInfo.setOfferList(new ArrayList<>());
		eligibilityInfo.setMemberDetails(new GetMemberResponse());
		eligibilityInfo.setMemberDetailsDto(new GetMemberResponseDto());
		eligibilityInfo.setDenomination(new Denomination());
		eligibilityInfo.setSubOffer(new SubOffer());
		eligibilityInfo.setPurchasepaymentMethod(new PurchasePaymentMethod());
		eligibilityInfo.setAmountInfo(new AmountInfo());
		eligibilityInfo.setAdditionalDetails(new AdditionalDetails());
		eligibilityInfo.setOfferCounters(new OfferCounter());
		eligibilityInfo.setCounterOfferIdList(new ArrayList<>());
		eligibilityInfo.setOfferCounterList(new ArrayList<>());
		eligibilityInfo.setPurchaseHistoryList(new ArrayList<>());
		eligibilityInfo.setConversionRateList(new ArrayList<>());
		eligibilityInfo.setCustomerSegmentCheckRequired(true);
		eligibilityInfo.setOfferLimitCounterLimitList(new ArrayList<>());
		eligibilityInfo.setOfferLimitCounterCounterList(new ArrayList<>());
		
	}
	
	@Test
	public void testGetters() {
		assertNotNull(eligibilityInfo.getHeaders());
		assertNotNull(eligibilityInfo.isBirthdayInfoRequired());
		assertNotNull(eligibilityInfo.isMember());
		assertNotNull(eligibilityInfo.getAccountNumber());
		assertNotNull(eligibilityInfo.getCommonSegmentNames());
		assertNotNull(eligibilityInfo.getRuleResult());
		assertNotNull(eligibilityInfo.getOffer());
		assertNotNull(eligibilityInfo.getOfferList());
		assertNotNull(eligibilityInfo.getMemberDetails());
		assertNotNull(eligibilityInfo.getMemberDetailsDto());
		assertNotNull(eligibilityInfo.getDenomination());
		assertNotNull(eligibilityInfo.getSubOffer());
		assertNotNull(eligibilityInfo.getPurchasepaymentMethod());
		assertNotNull(eligibilityInfo.getAmountInfo());
		assertNotNull(eligibilityInfo.getAdditionalDetails());
		assertNotNull(eligibilityInfo.getOfferCounters());
		assertNotNull(eligibilityInfo.getCounterOfferIdList());
		assertNotNull(eligibilityInfo.getOfferCounterList());
		assertNotNull(eligibilityInfo.getPurchaseHistoryList());
		assertNotNull(eligibilityInfo.getConversionRateList());
		assertNotNull(eligibilityInfo.isCustomerSegmentCheckRequired());
		assertNotNull(eligibilityInfo.getOfferLimitCounterLimitList());
		assertNotNull(eligibilityInfo.getOfferLimitCounterCounterList());
	}
	
	@Test
	public void testToString() {
		assertNotNull(eligibilityInfo.toString());
	}
	
}
