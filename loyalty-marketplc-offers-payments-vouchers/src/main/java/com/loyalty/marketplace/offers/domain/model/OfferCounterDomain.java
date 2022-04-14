package com.loyalty.marketplace.offers.domain.model;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OffersDBConstants;
import com.loyalty.marketplace.offers.helper.RepositoryHelper;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCounter;
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
public class OfferCounterDomain {
	
	private static final Logger LOG = LoggerFactory.getLogger(OfferCounterDomain.class);
	
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
	private List<String> rules;
	private Integer dailyCount;
	private Integer weeklyCount;
	private Integer monthlyCount;
	private Integer annualCount;
	private Integer totalCount;
	private Date lastPurchased;
	private List<DenominationCountDomain> denominationCount;
	private List<MemberOfferCountDomain> memberOfferCount;
	private List<AccountOfferCountDomain> accountOfferCount;
	
	public OfferCounterDomain(OfferCounterBuilder offerCounter) {
		
		this.id = offerCounter.id;
		this.offerId = offerCounter.offerId;
		this.rules = offerCounter.rules;
		this.dailyCount = offerCounter.dailyCount;
		this.weeklyCount = offerCounter.weeklyCount;
		this.monthlyCount = offerCounter.monthlyCount;
		this.annualCount = offerCounter.annualCount;
		this.totalCount = offerCounter.totalCount;
		this.lastPurchased = offerCounter.lastPurchased;
		this.denominationCount = offerCounter.denominationCount;
		this.memberOfferCount = offerCounter.memberOfferCount;
		this.accountOfferCount = offerCounter.accountOfferCount;
	}
	
	public static class OfferCounterBuilder {
		
		private String id;
		private String offerId;
		private List<String> rules;
		private Integer dailyCount;
		private Integer weeklyCount;
		private Integer monthlyCount;
		private Integer annualCount;
		private Integer totalCount;
		private Date lastPurchased;
		private List<DenominationCountDomain> denominationCount;
		private List<MemberOfferCountDomain> memberOfferCount;
		private List<AccountOfferCountDomain> accountOfferCount;
		
		public OfferCounterBuilder(String id) {
			this.id = id;
		}
		
		public OfferCounterBuilder(String offerId, Integer dailyCount,
				Integer weeklyCount, Integer monthlyCount, Integer annualCount, Integer totalCount) {
			
			this.offerId = offerId;
			this.dailyCount = dailyCount;
			this.weeklyCount = weeklyCount;
			this.monthlyCount = monthlyCount;
			this.annualCount = annualCount;
			this.totalCount = totalCount;
		}
		
		public OfferCounterBuilder offerId(String offerId) {
			this.offerId = offerId;
			return this;
		}
		
		public OfferCounterBuilder rules(List<String> rules) {
			this.rules = rules;
			return this;
		}
		
		public OfferCounterBuilder dailyCount(Integer dailyCount) {
			this.dailyCount = dailyCount;
			return this;
		}
		
		public OfferCounterBuilder weeklyCount(Integer weeklyCount) {
			this.weeklyCount = weeklyCount;
			return this;
		}
		
		public OfferCounterBuilder monthlyCount(Integer monthlyCount) {
			this.monthlyCount = monthlyCount;
			return this;
		}
		
		public OfferCounterBuilder annualCount(Integer annualCount) {
			this.annualCount = annualCount;
			return this;
		}
		
		public OfferCounterBuilder totalCount(Integer totalCount) {
			this.totalCount = totalCount;
			return this;
		}
		
		public OfferCounterBuilder lastPurchased(Date lastPurchased) {
			this.lastPurchased = lastPurchased;
			return this;
		}
		
		public OfferCounterBuilder denominationCount(List<DenominationCountDomain> denominationCount) {
			this.denominationCount = denominationCount;
			return this;
		}
		
		public OfferCounterBuilder memberOfferCount(List<MemberOfferCountDomain> memberOfferCount) {
			this.memberOfferCount = memberOfferCount;
			return this;
		}
		
		public OfferCounterBuilder accountOfferCount(List<AccountOfferCountDomain> accountOfferCount) {
			this.accountOfferCount = accountOfferCount;
			return this;
		}
		
		public OfferCounterDomain build() {
			return new OfferCounterDomain(this);
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
	public OfferCounter saveUpdateOfferCounter(OfferCounterDomain offerCounterDomain,
			OfferCounter offerCounter, String action, Headers headers, String api){
		
		OfferCounter savedOfferCounter = null;
		OfferCounter offerCounterToSave = null;
		try {			
			
			offerCounterToSave = modelMapper.map(offerCounterDomain, OfferCounter.class);
			savedOfferCounter = repositoryHelper.saveOfferCounter(offerCounterToSave);	
			
			if(action.equals(OfferConstants.INSERT_ACTION.get())) {
				
//				auditService.insertDataAudit(api, savedOfferCounter, OffersDBConstants.OFFER_COUNTER, headers.getExternalTransactionId(), headers.getUserName());
			
			} else if(action.equals(OfferConstants.UPDATE_ACTION.get())) {
				
				auditService.updateDataAudit(api, savedOfferCounter, OffersDBConstants.OFFER_COUNTER, offerCounter,  headers.getExternalTransactionId(), headers.getUserName());
			}
			
			
		} catch (Exception e) {
		
			LOG.error("Error while adding offer counter : {}", e.getMessage());
		
		}
		
		return savedOfferCounter;
	}
	
	
	
		
}
