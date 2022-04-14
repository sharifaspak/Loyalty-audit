package com.loyalty.marketplace.offers.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.loyalty.marketplace.constants.MarketplaceConfigurationConstants;
import com.loyalty.marketplace.offers.constants.OffersRequestMappingConstants;
import com.loyalty.marketplace.offers.domain.model.AccountOfferCountersDomain;
import com.loyalty.marketplace.offers.domain.model.MemberOfferCountersDomain;
import com.loyalty.marketplace.offers.domain.model.OfferCountersDomain;
import com.loyalty.marketplace.offers.error.handler.ErrorRecords;
import com.loyalty.marketplace.offers.helper.dto.EligibilityInfo;
import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
import com.loyalty.marketplace.offers.outbound.database.entity.AccountOfferCounts;
import com.loyalty.marketplace.offers.outbound.database.entity.DenominationCount;
import com.loyalty.marketplace.offers.outbound.database.entity.MemberOfferCounts;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCounters;
import com.loyalty.marketplace.offers.utils.Checks;
import com.loyalty.marketplace.offers.utils.DomainConfiguration;
import com.loyalty.marketplace.offers.utils.FilterValues;
import com.loyalty.marketplace.offers.utils.MapValues;
import com.loyalty.marketplace.offers.utils.Predicates;
import com.loyalty.marketplace.offers.utils.ProcessValues;
import com.loyalty.marketplace.utils.MarketplaceException;

/**
 * 
 * @author jaya.shukla
 *
 */
@Component
public class CounterHelper {
	
	@Autowired
	RepositoryHelper repositoryHelper;
	
	@Autowired
	OfferCountersDomain offerCountersDomain;
	
	@Autowired
	MemberOfferCountersDomain memberOfferCountersDomain;
	
	@Autowired
	AccountOfferCountersDomain accountOfferCountersDomain;
	
	/**
	 * Saves the updated counters after successful purchase
	 * @param eligibilityInfo
	 * @param purchaseRequest
	 * @throws Exception 
	 */
	@Transactional(transactionManager = MarketplaceConfigurationConstants.MONGO_TRANSACTION_MANAGER, rollbackForClassName = { MarketplaceConfigurationConstants.EXCEPTION_CLASS })
	public void saveCounterDetails(EligibilityInfo eligibilityInfo, PurchaseRequestDto purchaseRequest) throws MarketplaceException {
		
		if(!ObjectUtils.isEmpty(eligibilityInfo)) {
			
			if(Checks.checkCinemaOffer(eligibilityInfo.getOffer().getRules())
			|| !CollectionUtils.isEmpty(eligibilityInfo.getErrorRecordsList())) {
				
				setAccountCounterList(eligibilityInfo, purchaseRequest);
				
			} else {
				
				setAccountCounter(eligibilityInfo, purchaseRequest);
			}
			
		}
		
	}

	/**
	 * Saves the list of updated counters
	 * @param eligibilityInfo
	 * @param purchaseRequest
	 * @throws Exception 
	 */
	private void setAccountCounterList(EligibilityInfo eligibilityInfo, PurchaseRequestDto purchaseRequest) throws MarketplaceException {
		
		if(!ObjectUtils.isEmpty(eligibilityInfo)) {
			
			saveOfferCounterList(eligibilityInfo, purchaseRequest);
			saveMemberOfferCounterList(eligibilityInfo, purchaseRequest);
			saveAccountOfferCounterList(eligibilityInfo, purchaseRequest);
			
		}
		
	}

	/**
	 * Save list of all offer counters
	 * @param eligibilityInfo
	 * @param purchaseRequest
	 * @throws MarketplaceException 
	 */
	private void saveOfferCounterList(EligibilityInfo eligibilityInfo, PurchaseRequestDto purchaseRequest) throws MarketplaceException {
		
		List<OfferCountersDomain> counterDomainList = null;
		
		if(!CollectionUtils.isEmpty(eligibilityInfo.getOfferCountersList())) {
			
			boolean checkStatus = false;
			counterDomainList = new ArrayList<>(eligibilityInfo.getOfferCountersList().size());
			
			for(OfferCounters offerCounter : eligibilityInfo.getOfferCountersList()) {
				
				if(StringUtils.equals(offerCounter.getOfferId(), purchaseRequest.getOfferId())) {
					
					offerCounter.setDailyCount(offerCounter.getDailyCount()+purchaseRequest.getCouponQuantity());
					offerCounter.setWeeklyCount(offerCounter.getWeeklyCount()+purchaseRequest.getCouponQuantity());
					offerCounter.setMonthlyCount(offerCounter.getMonthlyCount()+purchaseRequest.getCouponQuantity());
					offerCounter.setAnnualCount(offerCounter.getAnnualCount()+purchaseRequest.getCouponQuantity());
					offerCounter.setTotalCount(offerCounter.getTotalCount()+purchaseRequest.getCouponQuantity());
					offerCounter.setLastPurchased(new Date());
					updateOfferCounterDenomination(offerCounter, purchaseRequest);
					checkStatus = true;
				}
				
				counterDomainList.add(DomainConfiguration.getOfferCountersDomain(offerCounter, null, null, null, null));
			}
			
			if(!checkStatus) {
				
				eligibilityInfo.setOfferCounter(ProcessValues.getNewOfferCounter(purchaseRequest.getOfferId(), eligibilityInfo.getOffer().getRules(), purchaseRequest.getVoucherDenomination(), purchaseRequest.getCouponQuantity()));
				eligibilityInfo.getOfferCountersList().add(eligibilityInfo.getOfferCounter());
				counterDomainList.add(DomainConfiguration.getOfferCountersDomain(eligibilityInfo.getOfferCounter(), null, null, null, null));
			}
			
		} else {
			
			counterDomainList = new ArrayList<>(1);
			eligibilityInfo.setOfferCounter(ProcessValues.getNewOfferCounter(purchaseRequest.getOfferId(), eligibilityInfo.getOffer().getRules(), purchaseRequest.getVoucherDenomination(), purchaseRequest.getCouponQuantity()));
			eligibilityInfo.setOfferCountersList(new ArrayList<>(1));
			eligibilityInfo.getOfferCountersList().add(eligibilityInfo.getOfferCounter());
			counterDomainList.add(DomainConfiguration.getOfferCountersDomain(eligibilityInfo.getOfferCounter(), null, null, null, null));
			
		}
		
		if(!CollectionUtils.isEmpty(counterDomainList)) {
			
			eligibilityInfo.setOfferCountersList(offerCountersDomain.saveAllOfferCounters(counterDomainList, OffersRequestMappingConstants.PURCHASE, eligibilityInfo.getHeaders()));
			eligibilityInfo.setOfferCounter(FilterValues.findAnyOfferCounterInList(eligibilityInfo.getOfferCountersList(), Predicates.isCounterWithOfferId(eligibilityInfo.getOffer().getOfferId())));
			
		}
		
	}
	
	/**
	 * Updates the existing list of denomination of each offer counter
	 * @param offerCounter
	 * @param purchaseRequest
	 */
	private void updateOfferCounterDenomination(OfferCounters offerCounter, PurchaseRequestDto purchaseRequest) {
		
		if(!ObjectUtils.isEmpty(offerCounter)
		&& !ObjectUtils.isEmpty(purchaseRequest)
		&& !ObjectUtils.isEmpty(purchaseRequest.getVoucherDenomination())) {
			
			if(!CollectionUtils.isEmpty(offerCounter.getDenominationCount())) {
				
				boolean denominationStatus = false;
				
				for(DenominationCount denominationCount : offerCounter.getDenominationCount()) {
					
					if(denominationCount.getDenomination().equals(purchaseRequest.getVoucherDenomination())) {
						
						denominationCount.setDailyCount(denominationCount.getDailyCount()+purchaseRequest.getCouponQuantity());
						denominationCount.setWeeklyCount(denominationCount.getWeeklyCount()+purchaseRequest.getCouponQuantity());
						denominationCount.setMonthlyCount(denominationCount.getMonthlyCount()+purchaseRequest.getCouponQuantity());
						denominationCount.setAnnualCount(denominationCount.getAnnualCount()+purchaseRequest.getCouponQuantity());
						denominationCount.setTotalCount(denominationCount.getTotalCount()+purchaseRequest.getCouponQuantity());
						denominationCount.setLastPurchased(new Date());
						denominationStatus = true;
					}
					
				}
				
				if(!denominationStatus) {
					
					offerCounter.getDenominationCount().add(ProcessValues.getNewDenominationCount(purchaseRequest.getVoucherDenomination(), purchaseRequest.getCouponQuantity()));
				}
				
			} else {
				
				offerCounter.setDenominationCount(new ArrayList<>(1));
				offerCounter.getDenominationCount().add(ProcessValues.getNewDenominationCount(purchaseRequest.getVoucherDenomination(), purchaseRequest.getCouponQuantity()));
				
			}
			
		}
		
		if(null==offerCounter.getDenominationCount()) {
			
			offerCounter.setDenominationCount(new ArrayList<>(1));
		}
		
	}

	/**
	 * Save list of all member offer counters
	 * @param eligibilityInfo
	 * @param purchaseRequest
	 * @throws Exception 
	 */
	private void saveMemberOfferCounterList(EligibilityInfo eligibilityInfo, PurchaseRequestDto purchaseRequest) throws MarketplaceException {
		
		List<MemberOfferCountersDomain> counterDomainList = null;
		if(!CollectionUtils.isEmpty(eligibilityInfo.getMemberOfferCounterList())) {
			
			boolean checkStatus = false;
			counterDomainList = new ArrayList<>(eligibilityInfo.getMemberOfferCounterList().size());
			
			for(MemberOfferCounts memberOfferCounter : eligibilityInfo.getMemberOfferCounterList()) {
				
				if(StringUtils.equals(memberOfferCounter.getOfferId(), purchaseRequest.getOfferId())
				&& StringUtils.equals(memberOfferCounter.getMembershipCode(), purchaseRequest.getMembershipCode())) {
					
					memberOfferCounter.setDailyCount(memberOfferCounter.getDailyCount()+purchaseRequest.getCouponQuantity());
					memberOfferCounter.setWeeklyCount(memberOfferCounter.getWeeklyCount()+purchaseRequest.getCouponQuantity());
					memberOfferCounter.setMonthlyCount(memberOfferCounter.getMonthlyCount()+purchaseRequest.getCouponQuantity());
					memberOfferCounter.setAnnualCount(memberOfferCounter.getAnnualCount()+purchaseRequest.getCouponQuantity());
					memberOfferCounter.setTotalCount(memberOfferCounter.getTotalCount()+purchaseRequest.getCouponQuantity());
					memberOfferCounter.setLastPurchased(new Date());
					updateMemberDenominationList(memberOfferCounter, purchaseRequest);
					checkStatus = true;
				}

				counterDomainList.add(DomainConfiguration.getMemberOfferCountersDomain(memberOfferCounter, null, null, null, null));
			}
			
			if(!checkStatus) {
				
				eligibilityInfo.setMemberOfferCounter(ProcessValues.getNewMemberOfferCounter(purchaseRequest.getOfferId(), purchaseRequest.getMembershipCode(), purchaseRequest.getVoucherDenomination(), purchaseRequest.getCouponQuantity()));
				eligibilityInfo.getMemberOfferCounterList().add(eligibilityInfo.getMemberOfferCounter());
				counterDomainList.add(DomainConfiguration.getMemberOfferCountersDomain(eligibilityInfo.getMemberOfferCounter(), null, null, null, null)); 
				
			}
			
			
		} else {
			
			counterDomainList = new ArrayList<>(1);
			eligibilityInfo.setMemberOfferCounter(ProcessValues.getNewMemberOfferCounter(purchaseRequest.getOfferId(), purchaseRequest.getMembershipCode(), purchaseRequest.getVoucherDenomination(), purchaseRequest.getCouponQuantity()));
			eligibilityInfo.setMemberOfferCounterList(new ArrayList<>(1));
			eligibilityInfo.getMemberOfferCounterList().add(eligibilityInfo.getMemberOfferCounter());
			counterDomainList.add(DomainConfiguration.getMemberOfferCountersDomain(eligibilityInfo.getMemberOfferCounter(), null, null, null, null));
		}
		
		if(!CollectionUtils.isEmpty(counterDomainList)) {
			
			eligibilityInfo.setMemberOfferCounterList(memberOfferCountersDomain.saveAllMemberOfferCounters(counterDomainList, OffersRequestMappingConstants.PURCHASE, eligibilityInfo.getHeaders()));
			eligibilityInfo.setMemberOfferCounter(FilterValues.findAnyMemberOfferCounterInList(eligibilityInfo.getMemberOfferCounterList(), Predicates.isCounterWithMembershipCodeAndOfferId(eligibilityInfo.getMemberDetails().getMembershipCode(), eligibilityInfo.getOffer().getOfferId())));
			
		}
		
	}
	
	/**
	 * Update the list of denominations for member offer counter
	 * @param memberOfferCounter
	 * @param purchaseRequest
	 */
	private void updateMemberDenominationList(MemberOfferCounts memberOfferCounter,
			PurchaseRequestDto purchaseRequest) {
		
		if(!ObjectUtils.isEmpty(memberOfferCounter)
		&& !ObjectUtils.isEmpty(purchaseRequest)
		&& !ObjectUtils.isEmpty(purchaseRequest.getVoucherDenomination())) {
			
			if(!CollectionUtils.isEmpty(memberOfferCounter.getDenominationCount())) {
				
				boolean denominationStatus = false;
				
				for(DenominationCount denominationCount : memberOfferCounter.getDenominationCount()) {
					
					if(denominationCount.getDenomination().equals(purchaseRequest.getVoucherDenomination())) {
						
						denominationCount.setDailyCount(denominationCount.getDailyCount()+purchaseRequest.getCouponQuantity());
						denominationCount.setWeeklyCount(denominationCount.getWeeklyCount()+purchaseRequest.getCouponQuantity());
						denominationCount.setMonthlyCount(denominationCount.getMonthlyCount()+purchaseRequest.getCouponQuantity());
						denominationCount.setAnnualCount(denominationCount.getAnnualCount()+purchaseRequest.getCouponQuantity());
						denominationCount.setTotalCount(denominationCount.getTotalCount()+purchaseRequest.getCouponQuantity());
						denominationCount.setLastPurchased(new Date());
						denominationStatus = true;
					}
					
				}
				
				if(!denominationStatus) {
					
					memberOfferCounter.getDenominationCount().add(ProcessValues.getNewDenominationCount(purchaseRequest.getVoucherDenomination(), purchaseRequest.getCouponQuantity()));
				}
				
			} else {
				
				memberOfferCounter.setDenominationCount(new ArrayList<>(1));
				memberOfferCounter.getDenominationCount().add(ProcessValues.getNewDenominationCount(purchaseRequest.getVoucherDenomination(), purchaseRequest.getCouponQuantity()));
				
			}
			
		}
		
		if(null==memberOfferCounter.getDenominationCount()) {
			
			memberOfferCounter.setDenominationCount(new ArrayList<>(1));
		}
	}

	/**
	 * Save list of all account offer counters
	 * @param eligibilityInfo
	 * @param purchaseRequest
	 * @throws MarketplaceException 
	 */
	private void saveAccountOfferCounterList(EligibilityInfo eligibilityInfo, PurchaseRequestDto purchaseRequest) throws MarketplaceException {
		
		List<AccountOfferCountersDomain> counterDomainList = null;
		if(!CollectionUtils.isEmpty(eligibilityInfo.getAccountOfferCounterList())) {
			
			boolean checkStatus = false;
			counterDomainList = new ArrayList<>(eligibilityInfo.getAccountOfferCounterList().size());
			for(AccountOfferCounts accountOfferCounter : eligibilityInfo.getAccountOfferCounterList()) {
				
				if(StringUtils.equals(accountOfferCounter.getOfferId(), purchaseRequest.getOfferId())
				&& StringUtils.equals(accountOfferCounter.getMembershipCode(), purchaseRequest.getMembershipCode())
				&& StringUtils.equals(accountOfferCounter.getAccountNumber(), purchaseRequest.getAccountNumber())) {
					
					accountOfferCounter.setDailyCount(accountOfferCounter.getDailyCount()+purchaseRequest.getCouponQuantity());
					accountOfferCounter.setWeeklyCount(accountOfferCounter.getWeeklyCount()+purchaseRequest.getCouponQuantity());
					accountOfferCounter.setMonthlyCount(accountOfferCounter.getMonthlyCount()+purchaseRequest.getCouponQuantity());
					accountOfferCounter.setAnnualCount(accountOfferCounter.getAnnualCount()+purchaseRequest.getCouponQuantity());
					accountOfferCounter.setTotalCount(accountOfferCounter.getTotalCount()+purchaseRequest.getCouponQuantity());
					accountOfferCounter.setLastPurchased(new Date());
					updateAccountOfferCounterDenomination(accountOfferCounter, purchaseRequest);
					checkStatus = true;
				}
				
				counterDomainList.add(DomainConfiguration.getAccountOfferCountersDomain(accountOfferCounter, null, 
						FilterValues.findAnyOfferCounterInList(eligibilityInfo.getOfferCountersList(), Predicates.isCounterWithOfferId(accountOfferCounter.getOfferId())), null,
						FilterValues.findAnyMemberOfferCounterInList(eligibilityInfo.getMemberOfferCounterList(), Predicates.isCounterWithMembershipCodeAndOfferId(accountOfferCounter.getMembershipCode(), accountOfferCounter.getOfferId())), null));
				
			}
			
			if(!checkStatus) {
				
				eligibilityInfo.setAccountOfferCounter(ProcessValues.getNewAccountOfferCounter(purchaseRequest.getOfferId(), purchaseRequest.getAccountNumber(), purchaseRequest.getMembershipCode(), purchaseRequest.getVoucherDenomination(), purchaseRequest.getCouponQuantity()));
				eligibilityInfo.getAccountOfferCounterList().add(eligibilityInfo.getAccountOfferCounter());
				counterDomainList.add(DomainConfiguration.getAccountOfferCountersDomain(eligibilityInfo.getAccountOfferCounter(), null, 
						FilterValues.findAnyOfferCounterInList(eligibilityInfo.getOfferCountersList(), Predicates.isCounterWithOfferId(eligibilityInfo.getAccountOfferCounter().getOfferId())), null, 
						FilterValues.findAnyMemberOfferCounterInList(eligibilityInfo.getMemberOfferCounterList(), Predicates.isCounterWithMembershipCodeAndOfferId(eligibilityInfo.getAccountOfferCounter().getMembershipCode(), eligibilityInfo.getAccountOfferCounter().getOfferId())), null));
			}
			
		} else {
			
			counterDomainList = new ArrayList<>(1);
			eligibilityInfo.setAccountOfferCounter(ProcessValues.getNewAccountOfferCounter(purchaseRequest.getOfferId(), purchaseRequest.getAccountNumber(), purchaseRequest.getMembershipCode(), purchaseRequest.getVoucherDenomination(), purchaseRequest.getCouponQuantity()));
			eligibilityInfo.setAccountOfferCounterList(new ArrayList<>(1));
			eligibilityInfo.getAccountOfferCounterList().add(eligibilityInfo.getAccountOfferCounter());
			counterDomainList.add(DomainConfiguration.getAccountOfferCountersDomain(eligibilityInfo.getAccountOfferCounter(), null, 
					FilterValues.findAnyOfferCounterInList(eligibilityInfo.getOfferCountersList(), Predicates.isCounterWithOfferId(eligibilityInfo.getAccountOfferCounter().getOfferId())), null, 
					FilterValues.findAnyMemberOfferCounterInList(eligibilityInfo.getMemberOfferCounterList(), Predicates.isCounterWithMembershipCodeAndOfferId(eligibilityInfo.getAccountOfferCounter().getMembershipCode(), eligibilityInfo.getAccountOfferCounter().getOfferId())), null));
			
		}
		
		if(!CollectionUtils.isEmpty(counterDomainList)) {
			
			eligibilityInfo.setAccountOfferCounterList(accountOfferCountersDomain.saveAllAccountOfferCounters(counterDomainList, OffersRequestMappingConstants.PURCHASE, eligibilityInfo.getHeaders()));
			eligibilityInfo.setAccountOfferCounter(FilterValues.findAccountOfferCounterInOfferAccountCounterList(eligibilityInfo.getAccountOfferCounterList(), Predicates.isCounterWithAccountNumberAndOfferId(purchaseRequest.getAccountNumber(), purchaseRequest.getMembershipCode(), purchaseRequest.getOfferId())));
		}
		
	}

	/**
	 * 
	 * @param accountOfferCounter
	 * @param purchaseRequest
	 */
	private void updateAccountOfferCounterDenomination(AccountOfferCounts accountOfferCounter,
			PurchaseRequestDto purchaseRequest) {
		
		if(!ObjectUtils.isEmpty(accountOfferCounter)
		&& !ObjectUtils.isEmpty(purchaseRequest)
		&& !ObjectUtils.isEmpty(purchaseRequest.getVoucherDenomination())) {
			
			if(!CollectionUtils.isEmpty(accountOfferCounter.getDenominationCount())) {
				
				boolean denominationStatus = false;
				
				for(DenominationCount denominationCount : accountOfferCounter.getDenominationCount()) {
					
					if(denominationCount.getDenomination().equals(purchaseRequest.getVoucherDenomination())) {
						
						denominationCount.setDailyCount(denominationCount.getDailyCount()+purchaseRequest.getCouponQuantity());
						denominationCount.setWeeklyCount(denominationCount.getWeeklyCount()+purchaseRequest.getCouponQuantity());
						denominationCount.setMonthlyCount(denominationCount.getMonthlyCount()+purchaseRequest.getCouponQuantity());
						denominationCount.setAnnualCount(denominationCount.getAnnualCount()+purchaseRequest.getCouponQuantity());
						denominationCount.setTotalCount(denominationCount.getTotalCount()+purchaseRequest.getCouponQuantity());
						denominationCount.setLastPurchased(new Date());
						denominationStatus = true;
					}
					
				}
				
				if(!denominationStatus) {
					
					accountOfferCounter.getDenominationCount().add(ProcessValues.getNewDenominationCount(purchaseRequest.getVoucherDenomination(), purchaseRequest.getCouponQuantity()));
				}
				
			} else {
				
				accountOfferCounter.setDenominationCount(new ArrayList<>());
				accountOfferCounter.getDenominationCount().add(ProcessValues.getNewDenominationCount(purchaseRequest.getVoucherDenomination(), purchaseRequest.getCouponQuantity()));
				
			}
			
		}
		
		if(null==accountOfferCounter.getDenominationCount()) {
			
			accountOfferCounter.setDenominationCount(new ArrayList<>(1));
		}
	}

	/**
	 * Sets account offer counter after purchase
	 * @param accountOfferCounter
	 * @param memberDetails
	 * @param offer
	 * @param couponQuantity
	 * @param voucherDenomination
	 * @param headers
	 * @param errorRecordsList
	 * @throws Exception 
	 */
	private void setAccountCounter(EligibilityInfo eligibilityInfo, PurchaseRequestDto purchaseRequest) throws MarketplaceException {
		
		OfferCountersDomain offerCounterDom = DomainConfiguration.getOfferCountersDomain(eligibilityInfo.getOfferCounter(), purchaseRequest.getCouponQuantity(), eligibilityInfo.getOffer().getOfferId(), purchaseRequest.getVoucherDenomination(), eligibilityInfo.getOffer().getRules());
		eligibilityInfo.setOfferCounter(offerCountersDomain.saveUpdateOfferCounter(offerCounterDom, eligibilityInfo.getOfferCounter(), ProcessValues.getAction(eligibilityInfo.getOfferCounter()), eligibilityInfo.getHeaders(), OffersRequestMappingConstants.PURCHASE));
		MemberOfferCountersDomain memberOfferCounterDomain = DomainConfiguration.getMemberOfferCountersDomain(eligibilityInfo.getMemberOfferCounter(), purchaseRequest.getCouponQuantity(), eligibilityInfo.getOffer().getOfferId(), eligibilityInfo.getMemberDetails().getMembershipCode(), purchaseRequest.getVoucherDenomination());
		eligibilityInfo.setMemberOfferCounter(memberOfferCountersDomain.saveUpdateMemberOfferCounter(memberOfferCounterDomain, eligibilityInfo.getMemberOfferCounter(), ProcessValues.getAction(eligibilityInfo.getMemberOfferCounter()), eligibilityInfo.getHeaders(), OffersRequestMappingConstants.PURCHASE));
		
		AccountOfferCountersDomain accountOfferCounterDomain = DomainConfiguration.getAccountOfferCountersDomain(eligibilityInfo.getAccountOfferCounter(), purchaseRequest.getCouponQuantity(), eligibilityInfo.getOfferCounter(), eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberOfferCounter(), purchaseRequest.getVoucherDenomination());
		eligibilityInfo.setAccountOfferCounter(!ObjectUtils.isEmpty(eligibilityInfo.getOfferCounter()) && !ObjectUtils.isEmpty(eligibilityInfo.getMemberOfferCounter()) 
				? accountOfferCountersDomain.saveUpdateAccountOfferCounter(accountOfferCounterDomain, eligibilityInfo.getAccountOfferCounter(), ProcessValues.getAction(eligibilityInfo.getAccountOfferCounter()), eligibilityInfo.getHeaders(), OffersRequestMappingConstants.PURCHASE)
				: null);
		
	}
	
	/**
	 * Resets the counter for current offer,account and membershipcode
	 * @param eligibilityInfo
	 */
	public void resetSpecificOfferCounters(EligibilityInfo eligibilityInfo) {
		
		eligibilityInfo.setErrorRecordsList(Checks.checkCinemaOffer(eligibilityInfo.getOffer().getRules())
				? repositoryHelper.getErrorRecordsForCurrentAndCinemaOffers(eligibilityInfo.getOffer().getOfferId(),
						eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberDetails().getMembershipCode())
				: repositoryHelper.getErrorRecordsForCurrentOffer(eligibilityInfo.getOffer().getOfferId(),
						eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberDetails().getMembershipCode()));
		
		setCounterValuesForPurchase(eligibilityInfo);
		ProcessValues.setErrorCodeValuesAndResetInCounter(eligibilityInfo);
		eligibilityInfo.setAccountOfferCounter(FilterValues.findAnyAccountOfferCounterInList(eligibilityInfo.getAccountOfferCounterList(), Predicates.isCounterWithAccountNumberAndOfferId(eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberDetails().getMembershipCode(), eligibilityInfo.getOffer().getOfferId())));
		eligibilityInfo.setMemberOfferCounter(FilterValues.findAnyMemberOfferCounterInList(eligibilityInfo.getMemberOfferCounterList(), Predicates.isCounterWithMembershipCodeAndOfferId(eligibilityInfo.getMemberDetails().getMembershipCode(), eligibilityInfo.getOffer().getOfferId())));
		eligibilityInfo.setOfferCounter(FilterValues.findAnyOfferCounterInList(eligibilityInfo.getOfferCountersList(), Predicates.isCounterWithOfferId(eligibilityInfo.getOffer().getOfferId())));
		
	}
	
	/**
	 * Sets the values of counters for current purchase 
	 * @param eligibilityInfo
	 */
	public void setCounterValuesForPurchase(EligibilityInfo eligibilityInfo) {
		
		if(!ObjectUtils.isEmpty(eligibilityInfo)) {
			
			if(Checks.checkCinemaOffer(eligibilityInfo.getOffer().getRules())) {
				
				getCountersForCinemaOffers(eligibilityInfo);
				
				
			} else {
				
				getCountersForNonCinemaOffers(eligibilityInfo);
				
			}
			
			if(!CollectionUtils.isEmpty(eligibilityInfo.getAccountOfferCounterList())) {
				
				List<String> membershipCodeForWrongReference = eligibilityInfo.getAccountOfferCounterList()
						.stream().filter(a->ObjectUtils.isEmpty(a.getMemberOfferCounter()))
						.map(AccountOfferCounts::getMembershipCode)
						.collect(Collectors.toList());
				
				List<String> offerIdForWrongReference = eligibilityInfo.getAccountOfferCounterList()
						.stream().filter(a->ObjectUtils.isEmpty(a.getMemberOfferCounter()))
						.map(AccountOfferCounts::getOfferId)
						.collect(Collectors.toList());
				
				List<MemberOfferCounts> memberOfferCounterList = null;
				
				if(!CollectionUtils.isEmpty(membershipCodeForWrongReference)
				&& !CollectionUtils.isEmpty(offerIdForWrongReference)) {
					
					memberOfferCounterList = repositoryHelper.fetchMemberOfferCounterForMembershipListAndOffer(membershipCodeForWrongReference, offerIdForWrongReference);
				}
				
				for(AccountOfferCounts  counter : eligibilityInfo.getAccountOfferCounterList()) {
					
					if(ObjectUtils.isEmpty(counter.getMemberOfferCounter())
					&& !CollectionUtils.isEmpty(memberOfferCounterList)) {
						
						counter.setMemberOfferCounter(memberOfferCounterList.stream()
								.filter(m->m.getMembershipCode().equals(counter.getMembershipCode())
									&& m.getOfferId().equals(counter.getOfferId()))
							     .findAny().orElse(null));
						
					}
					
					setCounterValuesForAccountOfferCounter(counter, eligibilityInfo);
					
				}
				
			} 
			
		}
		
	}

	/**
	 * Sets counter values for the account offer counter
	 * @param counter
	 * @param eligibilityInfo
	 */
	private void setCounterValuesForAccountOfferCounter(AccountOfferCounts counter, EligibilityInfo eligibilityInfo) {
		
		if(ObjectUtils.isEmpty(FilterValues.findAnyMemberOfferCounterInList(eligibilityInfo.getMemberOfferCounterList(), Predicates.isMemberCounterWithSameIdOrMembershipCode(counter.getMemberOfferCounter().getId(), counter.getMembershipCode(), counter.getOfferId())))) {
			
			if(CollectionUtils.isEmpty(eligibilityInfo.getMemberOfferCounterList())){
				
				eligibilityInfo.setMemberOfferCounterList(new ArrayList<>(1));
				
			}
			
			eligibilityInfo.getMemberOfferCounterList().add(counter.getMemberOfferCounter());
		
		}
		
		if(ObjectUtils.isEmpty(FilterValues.findAnyOfferCounterInList(eligibilityInfo.getOfferCountersList(), Predicates.isOffferCounterWithSameIdOrOfferId(counter.getOfferCounter().getId(), counter.getOfferId())))) {
			
			if(CollectionUtils.isEmpty(eligibilityInfo.getOfferCountersList())){
				
				eligibilityInfo.setOfferCounterList(new ArrayList<>(1));
				
			} 
			
			eligibilityInfo.getOfferCountersList().add(counter.getOfferCounter());
		
	    }
		
	}

	/**
	 * Fetches the counters for flow based on error records non-cinema offer
	 * @param eligibilityInfo
	 */
	private void getCountersForCinemaOffers(EligibilityInfo eligibilityInfo) {
		
		if(!ObjectUtils.isEmpty(eligibilityInfo)) {
			
			eligibilityInfo.setOfferCountersList(repositoryHelper.findCinemaOfferCounters());
			eligibilityInfo.setCounterOfferIdList(MapValues.mapOfferIdFromOfferCounterList(eligibilityInfo.getOfferCountersList(), Predicates.nonEmptyOfferIdInOfferCounter()));
			
			List<String> accountNumberList = MapValues.mapAccountNumberFromErrorRecords(eligibilityInfo.getErrorRecordsList());
			
			if(CollectionUtils.isEmpty(accountNumberList)) {
				
				accountNumberList = new ArrayList<>(1); 
				accountNumberList.add(eligibilityInfo.getMemberDetails().getAccountNumber());
				
			} else if(!CollectionUtils.containsAny(accountNumberList, Arrays.asList(eligibilityInfo.getMemberDetails().getAccountNumber()))){
				
				accountNumberList.add(eligibilityInfo.getMemberDetails().getAccountNumber());
			}
			
			List<String> membershipCodeList = MapValues.mapMembershipCodeFromErrorRecords(eligibilityInfo.getErrorRecordsList());
			
			if(CollectionUtils.isEmpty(membershipCodeList)) {
				
				membershipCodeList = new ArrayList<>(1); 
				membershipCodeList.add(eligibilityInfo.getMemberDetails().getMembershipCode());
				
			} else if(!CollectionUtils.containsAny(membershipCodeList, Arrays.asList(eligibilityInfo.getMemberDetails().getMembershipCode()))){
				
				membershipCodeList.add(eligibilityInfo.getMemberDetails().getMembershipCode());
			}
			
			eligibilityInfo.setMemberOfferCounterList(repositoryHelper.getMemberCountersForAllInErrorRecordsAndCurrent(membershipCodeList, eligibilityInfo.getCounterOfferIdList()));
			eligibilityInfo.setAccountOfferCounterList(repositoryHelper.getAccountCountersForAllInErrorRecordsAndCurrent(accountNumberList, membershipCodeList, eligibilityInfo.getCounterOfferIdList()));
		}
		
	}

	/***
	 * Fetches the counters for flow based on error records for non-cinema offer
	 * @param eligibilityInfo
	 */
	private void getCountersForNonCinemaOffers(EligibilityInfo eligibilityInfo) {
		
		if(!ObjectUtils.isEmpty(eligibilityInfo)) {
			
			List<String> accountNumberList = MapValues.mapAccountNumberFromErrorRecords(eligibilityInfo.getErrorRecordsList());
			
			if(CollectionUtils.isEmpty(accountNumberList)) {
				
				accountNumberList = new ArrayList<>(1);
				accountNumberList.add(eligibilityInfo.getMemberDetails().getAccountNumber());
				
			} else if(!CollectionUtils.containsAny(accountNumberList, Arrays.asList(eligibilityInfo.getMemberDetails().getAccountNumber()))){
				
				accountNumberList.add(eligibilityInfo.getMemberDetails().getAccountNumber());
			}
			
			List<String> membershipCodeList = MapValues.mapMembershipCodeFromErrorRecords(eligibilityInfo.getErrorRecordsList());
			
			if(CollectionUtils.isEmpty(membershipCodeList)) {
				
				membershipCodeList = new ArrayList<>(1); 
				membershipCodeList.add(eligibilityInfo.getMemberDetails().getMembershipCode());
				
			} else if(!CollectionUtils.containsAny(membershipCodeList, Arrays.asList(eligibilityInfo.getMemberDetails().getMembershipCode()))){
				
				membershipCodeList.add(eligibilityInfo.getMemberDetails().getMembershipCode());
			}
			
			List<String> offerIdList = MapValues.mapOfferIdFromErrorRecords(eligibilityInfo.getErrorRecordsList());
			
			if(CollectionUtils.isEmpty(offerIdList)) {
				
				offerIdList = new ArrayList<>(1); 
				offerIdList.add(eligibilityInfo.getOffer().getOfferId());
				
			} else if(!CollectionUtils.containsAny(offerIdList, Arrays.asList(eligibilityInfo.getOffer().getOfferId()))){
				
				offerIdList.add(eligibilityInfo.getOffer().getOfferId());
			}
			
			eligibilityInfo.setAccountOfferCounterList(repositoryHelper.getAccountCountersForAllInErrorRecordsAndCurrent(accountNumberList, membershipCodeList, offerIdList));
			
			setOfferAndMemberOfferCounterList(eligibilityInfo, membershipCodeList, offerIdList);			
		}
		
	}

	/**
	 * Sets values for offer counter list and member offer counter list
	 * @param eligibilityInfo
	 * @param membershipCodeList
	 * @param offerIdList
	 */
	private void setOfferAndMemberOfferCounterList(EligibilityInfo eligibilityInfo, List<String> membershipCodeList, List<String> offerIdList) {
		
		if(!CollectionUtils.isEmpty(eligibilityInfo.getErrorRecordsList())) {
			
			membershipCodeList = new ArrayList<>(eligibilityInfo.getErrorRecordsList().size());
			offerIdList = new ArrayList<>(eligibilityInfo.getErrorRecordsList().size());
			
			for(ErrorRecords record : eligibilityInfo.getErrorRecordsList()) {
				
				if(!Checks.checkAccountCounterPresent(eligibilityInfo.getAccountOfferCounterList(), record.getAccountNumber(), record.getMembershipCode(), record.getOfferId())) {
					
					membershipCodeList.add(record.getMembershipCode());
					offerIdList.add(record.getOfferId());
				}
				
			}
			
		}
		
		if(CollectionUtils.isEmpty(membershipCodeList) || !membershipCodeList.contains(eligibilityInfo.getMemberDetails().getMembershipCode())) {
			
			membershipCodeList.add(eligibilityInfo.getMemberDetails().getMembershipCode());
			
		}
		
		if(CollectionUtils.isEmpty(offerIdList) || !offerIdList.contains(eligibilityInfo.getOffer().getOfferId())) {
			
			offerIdList.add(eligibilityInfo.getOffer().getOfferId());
			
		}
		
		eligibilityInfo.setMemberOfferCounterList(repositoryHelper.getMemberCountersForAllInErrorRecordsAndCurrent(membershipCodeList, offerIdList));
		eligibilityInfo.setOfferCountersList(repositoryHelper.getOfferCountersForAllInErrorRecordsAndCurrent(offerIdList));

	}
 	
	
	
}
