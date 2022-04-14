package com.loyalty.marketplace.offers.helper.dto;

import java.util.List;

import com.loyalty.marketplace.offers.decision.manager.outbound.dto.RuleResult;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.outbound.database.entity.AccountOfferCounts;
import com.loyalty.marketplace.offers.outbound.database.entity.EligibleOffers;
import com.loyalty.marketplace.offers.outbound.database.entity.MemberOfferCounts;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCounter;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCounters;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
public class EligibleOfferHelperDto {
	
	private Headers headers;
	
	private boolean isMember;
	private String accountNumber;
	private GetMemberResponse memberDetails;
	
	private boolean customerSegmentCheckRequired;
	private RuleResult ruleResult;
		
	private EligibleOffers offer;
	private List<EligibleOffers> offerList;
	
	private OfferCounter offerCounters;
	private List<String> counterOfferIdList;
	private List<String> commonSegmentNames;
	private List<OfferCounter> offerCounterList;
	
	private OfferCounters offerCounter;
	private List<OfferCounters> offerCountersList;
	private MemberOfferCounts memberOfferCounter;
	private List<MemberOfferCounts> memberOfferCounterList;
	private AccountOfferCounts accountOfferCounter;
	private List<AccountOfferCounts> accountOfferCounterList;
	
}
