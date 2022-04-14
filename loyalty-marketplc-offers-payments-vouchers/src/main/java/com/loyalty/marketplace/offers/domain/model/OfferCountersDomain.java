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
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCounters;
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
public class OfferCountersDomain {
	
	private static final Logger LOG = LoggerFactory.getLogger(OfferCountersDomain.class);
	
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
	
	public OfferCountersDomain(OfferCounterBuilder offerCounter) {
		
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
		
		public OfferCounterBuilder id(String id) {
			this.id = id;
			return this;
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
		
		public OfferCountersDomain build() {
			return new OfferCountersDomain(this);
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
	public OfferCounters saveUpdateOfferCounter(OfferCountersDomain offerCounterDomain,
			OfferCounters offerCounter, String action, Headers headers, String api) throws MarketplaceException{
		
		OfferCounters savedOfferCounter = null;
		OfferCounters offerCounterToSave = null;
		try {			
			
			offerCounterToSave = modelMapper.map(offerCounterDomain, OfferCounters.class);
			
			if(!ObjectUtils.isEmpty(offerCounterToSave)
			&& null==offerCounterToSave.getDenominationCount()) {
				
				offerCounterToSave.setDenominationCount(new ArrayList<>(1));
				offerCounterToSave.getDenominationCount().add(new DenominationCount());
			}
			
			LOG.info("offerCounterToSave : {}", offerCounterToSave);
			savedOfferCounter = repositoryHelper.saveOfferCounters(offerCounterToSave);	
			
			if(action.equals(OfferConstants.UPDATE_ACTION.get())) {
				
				auditService.updateDataAudit(OffersDBConstants.OFFER_COUNTERS, savedOfferCounter, api, offerCounter,  headers.getExternalTransactionId(), headers.getUserName());
			}
			
		} catch (Exception e) {
		
			String log = Logs.logForVariable(OfferConstants.DB_ERROR_OFFER_COUNTER.get(), e.getMessage());
			LOG.error(log);
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.SAVE_OFFER_COUNTER_METHOD.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.SAVE_OFFER_COUNTER_EXCEPTION);
		}
		
		return savedOfferCounter;
	}

	/**
	 * 
	 * @param offerCounterDomainList
	 * @param action
	 * @param api
	 * @param headers
	 * @return saved counter list
	 * @throws MarketplaceException 
	 */
	public List<OfferCounters> saveAllOfferCounters(List<OfferCountersDomain> offerCounterDomainList, String api, Headers headers) throws MarketplaceException {
		
		List<OfferCounters> savedOfferCounterList = null;
		
		try {
			
			List<OfferCounters> offerCountersToSave = new ArrayList<>(offerCounterDomainList.size());
			offerCounterDomainList.forEach(o->offerCountersToSave.add(modelMapper.map(o, OfferCounters.class)));
			List<OfferCounters> originalCountersList = new ArrayList<>(offerCounterDomainList.size());
			
			if(!CollectionUtils.isEmpty(offerCountersToSave)) {
				
				offerCountersToSave.forEach(o-> {
					
					if(!ObjectUtils.isEmpty(o)
					&& null==o.getDenominationCount()) {
						
						o.setDenominationCount(new ArrayList<>(1));
						o.getDenominationCount().add(new DenominationCount());
					}
					
				});
			}
			LOG.info("offerCountersToSave : {}", offerCountersToSave);
			originalCountersList.addAll(offerCountersToSave);
			savedOfferCounterList = repositoryHelper.saveAllOfferCounter(offerCountersToSave);
			auditService.updateDataAudit(OffersDBConstants.OFFER_COUNTERS, savedOfferCounterList, api, originalCountersList, headers.getExternalTransactionId(), headers.getUserName());
			
		} catch(Exception e) {
			
			String log = Logs.logForVariable(OfferConstants.DB_ERROR_OFFER_COUNTER_LIST.get(), e.getMessage());
			LOG.error(log);
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.SAVE_ALL_OFFER_COUNTERS_METHOD.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.SAVE_OFFER_COUNTER_LIST_EXCEPTION); 
		}
		
		return savedOfferCounterList;
		
	}
	
	
	
		
}
