package com.loyalty.marketplace.offers.helper.dto;

import java.util.List;

import com.loyalty.marketplace.equivalentpoints.outbound.database.entity.ConversionRate;
import com.loyalty.marketplace.offers.decision.manager.outbound.dto.RuleResult;
import com.loyalty.marketplace.offers.error.handler.ErrorRecords;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponseDto;
import com.loyalty.marketplace.offers.outbound.database.entity.AccountOfferCounts;
import com.loyalty.marketplace.offers.outbound.database.entity.AdditionalDetails;
import com.loyalty.marketplace.offers.outbound.database.entity.MemberOfferCounts;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCounter;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCounters;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchaseHistory;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchasePaymentMethod;
import com.loyalty.marketplace.offers.outbound.database.entity.SubOffer;
import com.loyalty.marketplace.outbound.database.entity.Denomination;

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
public class EligibilityInfo {
	
	private Headers headers;
	private boolean isMember;
	
	private String accountNumber;
	private RuleResult ruleResult;
	
	private OfferCatalog offer;
	private List<OfferCatalog> offerList;
	
	private GetMemberResponse memberDetails;
	private GetMemberResponseDto memberDetailsDto;
	
	private Denomination denomination;
	private SubOffer subOffer;
	
	private PurchasePaymentMethod purchasepaymentMethod;
	
	private AdditionalDetails additionalDetails;
	private AmountInfo amountInfo;
	
	private OfferCounter offerCounters;
	private List<OfferCounter> offerCounterList;
	
	private List<String> counterOfferIdList;
	
	private List<PurchaseHistory> purchaseHistoryList;
	private List<ConversionRate> conversionRateList;
	private boolean customerSegmentCheckRequired; 
	private List<String> commonSegmentNames;
	
	private List<LimitCounter> offerLimitCounterLimitList;
	private List<LimitCounter> offerLimitCounterCounterList;
	
	private List<ErrorRecords> errorRecordsList;
	
	private boolean isBirthdayInfoRequired;
	private BirthdayDurationInfoDto birthdayDurationInfoDto;
	
	private Integer recordCount;
	
	private OfferCounters offerCounter;
	private List<OfferCounters> offerCountersList;
	private MemberOfferCounts memberOfferCounter;
	private List<MemberOfferCounts> memberOfferCounterList;
	private AccountOfferCounts accountOfferCounter;
	private List<AccountOfferCounts> accountOfferCounterList;
	
}
