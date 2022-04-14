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
import com.loyalty.marketplace.offers.outbound.database.entity.AccountOfferCounts;
import com.loyalty.marketplace.offers.outbound.database.entity.DenominationCount;
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
public class AccountOfferCountersDomain {
	
	private static final Logger LOG = LoggerFactory.getLogger(AccountOfferCountersDomain.class);
	
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
	private String accountNumber;
	private Integer dailyCount;
	private Integer weeklyCount;
	private Integer monthlyCount;
	private Integer annualCount;
	private Integer totalCount;
	private Date lastPurchased;
	private List<DenominationCountDomain> denominationCount;
	private OfferCountersDomain offerCounter;
	private MemberOfferCountersDomain memberOfferCounter;
	
	
	public AccountOfferCountersDomain(AccountOfferCountersBuilder counter) {
		
		this.id = counter.id;
		this.offerId = counter.offerId;
		this.membershipCode = counter.membershipCode;
		this.accountNumber = counter.accountNumber;
		this.dailyCount = counter.dailyCount;
		this.weeklyCount = counter.weeklyCount;
		this.monthlyCount = counter.monthlyCount;
		this.annualCount = counter.annualCount;
		this.totalCount = counter.totalCount;
		this.lastPurchased = counter.lastPurchased;
		this.denominationCount = counter.denominationCount;
		this.offerCounter = counter.offerCounter;
		this.memberOfferCounter = counter.memberOfferCounter;

	}
	
	public static class AccountOfferCountersBuilder {
		
		private String id;
		private String offerId;
		private String membershipCode;
		private String accountNumber;
		private Integer dailyCount;
		private Integer weeklyCount;
		private Integer monthlyCount;
		private Integer annualCount;
		private Integer totalCount;
		private Date lastPurchased;
		private List<DenominationCountDomain> denominationCount;
		private OfferCountersDomain offerCounter;
		private MemberOfferCountersDomain memberOfferCounter;
				
		public AccountOfferCountersBuilder(String id) {
			this.id = id;
		}
		
		public AccountOfferCountersBuilder(String accountNumber, String offerId, Integer dailyCount,
				Integer weeklyCount, Integer monthlyCount, Integer annualCount, Integer totalCount) {
			
			this.accountNumber = accountNumber;
			this.offerId = offerId;
			this.dailyCount = dailyCount;
			this.weeklyCount = weeklyCount;
			this.monthlyCount = monthlyCount;
			this.annualCount = annualCount;
			this.totalCount = totalCount;
		}
		
		public AccountOfferCountersBuilder id(String id) {
			this.id = id;
			return this;
		}
		
		public AccountOfferCountersBuilder offerId(String offerId) {
			this.offerId = offerId;
			return this;
		}
		
		public AccountOfferCountersBuilder accountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
			return this;
		}
		
		public AccountOfferCountersBuilder membershipCode(String membershipCode) {
			this.membershipCode = membershipCode;
			return this;
		}
		
		public AccountOfferCountersBuilder dailyCount(Integer dailyCount) {
			this.dailyCount = dailyCount;
			return this;
		}
		
		public AccountOfferCountersBuilder weeklyCount(Integer weeklyCount) {
			this.weeklyCount = weeklyCount;
			return this;
		}
		
		public AccountOfferCountersBuilder monthlyCount(Integer monthlyCount) {
			this.monthlyCount = monthlyCount;
			return this;
		}
		
		public AccountOfferCountersBuilder annualCount(Integer annualCount) {
			this.annualCount = annualCount;
			return this;
		}
		
		public AccountOfferCountersBuilder totalCount(Integer totalCount) {
			this.totalCount = totalCount;
			return this;
		}
		
		public AccountOfferCountersBuilder lastPurchased(Date lastPurchased) {
			this.lastPurchased = lastPurchased;
			return this;
		}
		
		public AccountOfferCountersBuilder denominationCount(List<DenominationCountDomain> denominationCount) {
			this.denominationCount = denominationCount;
			return this;
		}
		
		public AccountOfferCountersBuilder offerCounter(OfferCountersDomain offerCounter) {
			this.offerCounter = offerCounter;
			return this;
		}
		
		public AccountOfferCountersBuilder memberOfferCounter(MemberOfferCountersDomain memberOfferCounter) {
			this.memberOfferCounter = memberOfferCounter;
			return this;
		}
		
		public AccountOfferCountersDomain build() {
			return new AccountOfferCountersDomain(this);
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
	public AccountOfferCounts saveUpdateAccountOfferCounter(AccountOfferCountersDomain offerCounterDomain,
			AccountOfferCounts offerCounter, String action, Headers headers, String api) throws MarketplaceException{
		
		AccountOfferCounts savedOfferCounter = null;
		AccountOfferCounts accountOfferCounterToSave = null;
		try {			
			
			accountOfferCounterToSave = modelMapper.map(offerCounterDomain, AccountOfferCounts.class);
			
			if(!ObjectUtils.isEmpty(accountOfferCounterToSave)
			&& null==accountOfferCounterToSave.getDenominationCount()) {
				
				accountOfferCounterToSave.setDenominationCount(new ArrayList<>(1));
				accountOfferCounterToSave.getDenominationCount().add(new DenominationCount());
			}
			LOG.info("accountOfferCounterToSave : {}", accountOfferCounterToSave);
			savedOfferCounter = repositoryHelper.saveAccountOfferCounters(accountOfferCounterToSave);	
			
			if(action.equals(OfferConstants.UPDATE_ACTION.get())) {
				
				auditService.updateDataAudit(OffersDBConstants.OFFER_COUNTERS, savedOfferCounter, api, offerCounter,  headers.getExternalTransactionId(), headers.getUserName());
			}
			
			
		} catch (Exception e) {
		
			String log = Logs.logForVariable(OfferConstants.DB_ERROR_ACCOUNT_OFFER_COUNTER.get(), e.getMessage());
			LOG.error(log);
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.SAVE_ACCOUNT_OFFER_COUNTER_METHOD.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.SAVE_ACCOUNT_OFFER_COUNTER_EXCEPTION);
		}
		
		return savedOfferCounter;
	}

	/**
	 * 
	 * @param accountOfferCounterDomainList
	 * @param api
	 * @param headers
	 * @return saved account counter list 
	 * @throws MarketplaceException 
	 */
	public List<AccountOfferCounts> saveAllAccountOfferCounters(List<AccountOfferCountersDomain> accountOfferCounterDomainList, String api, Headers headers) throws MarketplaceException {
		
		List<AccountOfferCounts> savedAccountOfferCounterList = null;
		
		try {
			List<AccountOfferCounts> accountofferCountersToSave = new ArrayList<>(accountOfferCounterDomainList.size());
			accountOfferCounterDomainList.forEach(o->accountofferCountersToSave.add(modelMapper.map(o, AccountOfferCounts.class)));
			List<AccountOfferCounts> originalAccountCountersList = new ArrayList<>(accountOfferCounterDomainList.size());
			
			if(!CollectionUtils.isEmpty(accountofferCountersToSave)) {
				
				accountofferCountersToSave.forEach(a-> {
					
					if(!ObjectUtils.isEmpty(a)
					&& null==a.getDenominationCount()) {
						
						a.setDenominationCount(new ArrayList<>(1));
						a.getDenominationCount().add(new DenominationCount());
					}
					
				});
			}
			LOG.info("accountofferCountersToSave : {}", accountofferCountersToSave);
			originalAccountCountersList.addAll(accountofferCountersToSave);
			savedAccountOfferCounterList = repositoryHelper.saveAllAccountOfferCounters(accountofferCountersToSave);
			auditService.updateDataAudit(OffersDBConstants.OFFER_COUNTERS, savedAccountOfferCounterList, api, originalAccountCountersList, headers.getExternalTransactionId(), headers.getUserName());
			
		} catch(Exception e) {
			
			String log = Logs.logForVariable(OfferConstants.DB_ERROR_ACCOUNT_OFFER_COUNTER_LIST.get(), e.getMessage());
			LOG.error(log);
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.SAVE_ALL_ACCOUNT_OFFER_COUNTERS_METHOD.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.SAVE_ACCOUNT_OFFER_COUNTER_LIST_EXCEPTION);
			
		}
		
		return savedAccountOfferCounterList;
	}
			
}
