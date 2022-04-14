package com.loyalty.marketplace.offers.domain.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OfferExceptionCodes;
import com.loyalty.marketplace.offers.constants.OffersDBConstants;
import com.loyalty.marketplace.offers.helper.RepositoryHelper;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.outbound.database.entity.DenominationCount;
import com.loyalty.marketplace.offers.outbound.database.entity.MemberOfferCounts;
import com.loyalty.marketplace.utils.Logs;
import com.loyalty.marketplace.utils.MarketplaceException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@ToString
@NoArgsConstructor
@Component
public class MemberOfferCountersDomain {
	
	private static final Logger LOG = LoggerFactory.getLogger(MemberOfferCountersDomain.class);
	
	@Getter(AccessLevel.NONE)
	@Autowired
	ModelMapper modelMapper;

	@Getter(AccessLevel.NONE)
	@Autowired
	RepositoryHelper repositoryHelper;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	AuditService auditService;
	
	private String id;
	private String offerId;
	private String membershipCode;
	private Integer dailyCount;
	private Integer weeklyCount;
	private Integer monthlyCount;
	private Integer annualCount;
	private Integer totalCount;
	private Date lastPurchased;
	private List<DenominationCountDomain> denominationCount;
	
	public MemberOfferCountersDomain(MemberOfferCountersBuilder offerCounter) {
		
		this.id = offerCounter.id;
		this.offerId = offerCounter.offerId;
		this.membershipCode = offerCounter.membershipCode;
		this.dailyCount = offerCounter.dailyCount;
		this.weeklyCount = offerCounter.weeklyCount;
		this.monthlyCount = offerCounter.monthlyCount;
		this.annualCount = offerCounter.annualCount;
		this.totalCount = offerCounter.totalCount;
		this.lastPurchased = offerCounter.lastPurchased;
		this.denominationCount = offerCounter.denominationCount;

	}
	
	public static class MemberOfferCountersBuilder {
		
		private String id;
		private String offerId;
		private String membershipCode;
		private Integer dailyCount;
		private Integer weeklyCount;
		private Integer monthlyCount;
		private Integer annualCount;
		private Integer totalCount;
		private Date lastPurchased;
		private List<DenominationCountDomain> denominationCount;
				
		public MemberOfferCountersBuilder(String id) {
			this.id = id;
		}
		
		public MemberOfferCountersBuilder(String membershipCode, String offerId, Integer dailyCount,
				Integer weeklyCount, Integer monthlyCount, Integer annualCount, Integer totalCount) {
			
			this.membershipCode = membershipCode;
			this.offerId = offerId;
			this.dailyCount = dailyCount;
			this.weeklyCount = weeklyCount;
			this.monthlyCount = monthlyCount;
			this.annualCount = annualCount;
			this.totalCount = totalCount;
		}
		
		public MemberOfferCountersBuilder id(String id) {
			this.id = id;
			return this;
		}
		
		public MemberOfferCountersBuilder offerId(String offerId) {
			this.offerId = offerId;
			return this;
		}
		
		public MemberOfferCountersBuilder membershipCode(String membershipCode) {
			this.membershipCode = membershipCode;
			return this;
		}
		
		public MemberOfferCountersBuilder dailyCount(Integer dailyCount) {
			this.dailyCount = dailyCount;
			return this;
		}
		
		public MemberOfferCountersBuilder weeklyCount(Integer weeklyCount) {
			this.weeklyCount = weeklyCount;
			return this;
		}
		
		public MemberOfferCountersBuilder monthlyCount(Integer monthlyCount) {
			this.monthlyCount = monthlyCount;
			return this;
		}
		
		public MemberOfferCountersBuilder annualCount(Integer annualCount) {
			this.annualCount = annualCount;
			return this;
		}
		
		public MemberOfferCountersBuilder totalCount(Integer totalCount) {
			this.totalCount = totalCount;
			return this;
		}
		
		public MemberOfferCountersBuilder lastPurchased(Date lastPurchased) {
			this.lastPurchased = lastPurchased;
			return this;
		}
		
		public MemberOfferCountersBuilder denominationCount(List<DenominationCountDomain> denominationCount) {
			this.denominationCount = denominationCount;
			return this;
		}
		
		public MemberOfferCountersDomain build() {
			return new MemberOfferCountersDomain(this);
		}
	
	}
	
	/**
	 * 
	 * @param userName 
	 * @param externalTransactionId 
	 * @param offerPurchaseDomain
	 * @param resultResponse
	 * @return saves a new offer purchase history record
	 * @throws MarketplaceException
	 */
	public MemberOfferCounts saveUpdateMemberOfferCounter(MemberOfferCountersDomain offerCounterDomain,
			MemberOfferCounts offerCounter, String action, Headers headers, String api) throws MarketplaceException{
		
		MemberOfferCounts savedOfferCounter = null;
		
		try {			
			
			MemberOfferCounts memberOfferCounterToSave = modelMapper.map(offerCounterDomain, MemberOfferCounts.class);
			
			if(null==memberOfferCounterToSave.getDenominationCount()) {
				
				memberOfferCounterToSave.setDenominationCount(new ArrayList<>(1));
				memberOfferCounterToSave.getDenominationCount().add(new DenominationCount());
			}
			LOG.info("memberofferCountersToSave : {}", memberOfferCounterToSave);
			savedOfferCounter = repositoryHelper.saveMemberOfferCounters(memberOfferCounterToSave);	
			
			if(action.equals(OfferConstants.UPDATE_ACTION.get())) {
				
				auditService.updateDataAudit(OffersDBConstants.OFFER_COUNTERS, savedOfferCounter, api, offerCounter,  headers.getExternalTransactionId(), headers.getUserName());
			}
			
			
		} catch (Exception e) {
		
			
			String log = Logs.logForVariable(OfferConstants.DB_ERROR_MEMBER_OFFER_COUNTER.get(), e.getMessage());
			LOG.error(log);
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.SAVE_MEMBER_OFFER_COUNTER_METHOD.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.SAVE_MEMBER_OFFER_COUNTER_EXCEPTION);
		
		}
		
		return savedOfferCounter;
	}

	/**
	 * 
	 * @param memberOfferCounterDomainList
	 * @param api
	 * @param headers
	 * @return saved member counter list
	 * @throws MarketplaceException 
	 */
	public List<MemberOfferCounts> saveAllMemberOfferCounters(List<MemberOfferCountersDomain> memberOfferCounterDomainList, String api, Headers headers) throws MarketplaceException {
		
		List<MemberOfferCounts> savedMemberOfferCounterList = null;
		
		try {
			List<MemberOfferCounts> memberofferCountersToSave = new ArrayList<>(memberOfferCounterDomainList.size());
			memberOfferCounterDomainList.forEach(o->memberofferCountersToSave.add(modelMapper.map(o, MemberOfferCounts.class)));
			List<MemberOfferCounts> originalMemberCountersList = new ArrayList<>(memberOfferCounterDomainList.size());
			if(!CollectionUtils.isEmpty(memberofferCountersToSave)) {
				
				memberofferCountersToSave.forEach(m-> {
					
					if(!ObjectUtils.isEmpty(m)
					&& null==m.getDenominationCount()) {
						
						m.setDenominationCount(new ArrayList<>(1));
						m.getDenominationCount().add(new DenominationCount());
					}
					
				});
			}
			originalMemberCountersList.addAll(memberofferCountersToSave);
			LOG.info("memberofferCountersToSave : {}", memberofferCountersToSave);
			savedMemberOfferCounterList = repositoryHelper.saveAllMemberOfferCounters(memberofferCountersToSave);
			auditService.updateDataAudit(OffersDBConstants.OFFER_COUNTERS, savedMemberOfferCounterList, api, originalMemberCountersList, headers.getExternalTransactionId(), headers.getUserName());
			
		} catch(Exception e) {
			
			String log = Logs.logForVariable(OfferConstants.DB_ERROR_MEMBER_OFFER_COUNTER_LIST.get(), e.getMessage());
			LOG.error(log);
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.SAVE_ALL_MEMBER_OFFER_COUNTERS_METHOD.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.SAVE_MEMBER_OFFER_COUNTER_LIST_EXCEPTION); 
			
		}
		
		return savedMemberOfferCounterList;
		
	}
			
}
