package com.loyalty.marketplace.offers.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.error.handler.ErrorRecords;
import com.loyalty.marketplace.offers.inbound.dto.DenominationLimitDto;
import com.loyalty.marketplace.offers.inbound.dto.LimitDto;
import com.loyalty.marketplace.offers.inbound.dto.SubOfferDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.BirthdayAccountsDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.ParentChlidCustomer;
import com.loyalty.marketplace.offers.member.management.outbound.dto.PaymentMethods;
import com.loyalty.marketplace.offers.merchants.outbound.database.entity.Merchant;
import com.loyalty.marketplace.offers.outbound.database.entity.DenominationLimit;
import com.loyalty.marketplace.offers.outbound.database.entity.EligibleOfferDenomination;
import com.loyalty.marketplace.offers.outbound.database.entity.EligibleOfferStore;
import com.loyalty.marketplace.offers.outbound.database.entity.EligibleOfferType;
import com.loyalty.marketplace.offers.outbound.database.entity.EligibleOffers;
import com.loyalty.marketplace.offers.outbound.database.entity.MemberOfferCounts;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCounters;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferLimit;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferType;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchaseHistory;
import com.loyalty.marketplace.offers.outbound.dto.OfferCatalogResultResponseDto;
import com.loyalty.marketplace.offers.stores.outbound.database.entity.ContactPerson;
import com.loyalty.marketplace.offers.stores.outbound.database.entity.Store;
import com.loyalty.marketplace.outbound.database.entity.Denomination;
import com.loyalty.marketplace.outbound.database.entity.DenominationValue;
import com.loyalty.marketplace.outbound.database.entity.PaymentMethod;

/**
 * 
 * @author jaya.shukla
 *
 */
public class MapValues {
	
	MapValues(){
		
	}
	
	/**
	 * 
	 * @param offerList
	 * @return list of offerId from list of offers
	 */
    public static List<String> mapOfferIdFromOfferList(List<OfferCatalog> offerList) {
		
		return CollectionUtils.isNotEmpty(offerList)
				 ? offerList.stream()
				   .map(OfferCatalog::getOfferId)
				   .collect(Collectors.toList())	 
				 : null;
	}
	
    /**
     * 
     * @param offerList
     * @param predicate
     * @return list of offerId for list of offers with limits
     */
	public static List<String> mapOfferIdList(List<OfferCatalog> offerList, Predicate<OfferCatalog> predicate) {
		
		return CollectionUtils.isNotEmpty(offerList)
				 ? offerList.stream()
				   .filter(predicate)
				   .map(OfferCatalog::getOfferId)
				   .collect(Collectors.toList())	 
				 : null;
	}
	
	/**
	 * 
	 * @param offerList
	 * @param predicate
	 * @return  list of partner codes from list of offers
	 */
	public static List<String> mapPartnerCodesFromOfferList(List<OfferCatalog> offerList, Predicate<OfferCatalog> predicate) {
		
		Set<String> partnerCodeSet = CollectionUtils.isNotEmpty(offerList)
					 ? offerList.stream().filter(predicate)
					   .map(OfferCatalog::getPartnerCode)
					   .collect(Collectors.toSet())	 
					 : null;
		
		if(!CollectionUtils.isEmpty(partnerCodeSet)) {
			
			partnerCodeSet.add(OfferConstants.SMILES.get());
			
		} 			   
					   
		return CollectionUtils.isNotEmpty(partnerCodeSet)
			  ? partnerCodeSet.stream().collect(Collectors.toList())
			  : Arrays.asList(OfferConstants.SMILES.get());
	}
	
	/**
	 * 
	 * @param offerList
	 * @param predicate
	 * @return  list of offerTypeId from list of offers
	 */
    public static List<String> mapOfferTypeIdFromOfferList(List<OfferCatalog> offerList, Predicate<OfferCatalog> predicate) {
		
    	Set<String> offerTypeSet = CollectionUtils.isNotEmpty(offerList)
				 ? offerList.stream().filter(predicate)
				   .map(OfferCatalog::getOfferType)
				   .map(OfferType::getOfferTypeId)
				   .collect(Collectors.toSet())	 
				 : null;
				   
	   return CollectionUtils.isNotEmpty(offerTypeSet)
				  ? offerTypeSet.stream().collect(Collectors.toList())
				  : null;
	}
	
    /**
     * 
     * @param limitList
     * @param predicate
     * @return list of customer segment from list of offer limits
     */
	public static List<String> mapCustomerSegmentInOfferLimits(List<OfferLimit> limitList, Predicate<OfferLimit> predicate) {
		
		Set<String> customerSegmentSet = CollectionUtils.isNotEmpty(limitList)
				 ? limitList.stream().filter(predicate)
				   .map(OfferLimit::getCustomerSegment)
				   .collect(Collectors.toSet())	 
				 : null;
				   
	   return CollectionUtils.isNotEmpty(customerSegmentSet)
				  ? customerSegmentSet.stream().collect(Collectors.toList())
				  : null;		   
	}
	
	/**
	 * 
	 * @param limitList
	 * @return  list of denominations from list of offer limits
	 */
	public static List<Integer> mapLimitDenominationFromOfferLimit(List<OfferLimit> limitList) {
		
		List<Integer> limitDenominations = new ArrayList<>(1);
		List<OfferLimit> limitWithOfferDenominationLimit = FilterValues.filterOfferLimits(limitList, Predicates.offerDenominationLimitPresent());
		List<OfferLimit> limitWithAccountDenominationLimit = FilterValues.filterOfferLimits(limitList, Predicates.accountDenominationLimitPresent());
		List<OfferLimit> limitWithMemberDenominationLimit = FilterValues.filterOfferLimits(limitList, Predicates.memberDenominationLimitPresent());
		
		if(!CollectionUtils.isEmpty(limitWithOfferDenominationLimit)) {
			
			for(OfferLimit limit : limitWithOfferDenominationLimit) {
				
				limitDenominations.addAll(mapOfferDenominationInLimit(limit.getDenominationLimit()));
			}
			
		}
		
		if(!CollectionUtils.isEmpty(limitWithAccountDenominationLimit)) {
			
			for(OfferLimit limit : limitWithAccountDenominationLimit) {
				
				limitDenominations.addAll(mapOfferDenominationInLimit(limit.getAccountDenominationLimit()));
			}
			
		}
		
		if(!CollectionUtils.isEmpty(limitWithMemberDenominationLimit)) {
			
			for(OfferLimit limit : limitWithMemberDenominationLimit) {
				
				limitDenominations.addAll(mapOfferDenominationInLimit(limit.getMemberDenominationLimit()));
			}
			
		}
		
		return  !CollectionUtils.isEmpty(limitDenominations)
			  ? limitDenominations
			  : null;
	}
	
	/**
	 * 
	 * @param offerLimits
	 * @param customerSegmentNames
	 * @param level
	 * @return list of daily limit from list of offer denomination limit
	 */
	public static Integer mapDailyLimits(List<OfferLimit> offerLimits,
			List<String> customerSegmentNames, String level) {
		
		Integer limit = null;
		List<Integer> limits = null;
		
		if(StringUtils.equalsIgnoreCase(level, OfferConstants.OFFER_COUNTER.get())){
			
			limits = CollectionUtils.isNotEmpty(customerSegmentNames) && CollectionUtils.isNotEmpty(offerLimits)	
					? offerLimits.stream().filter(ol->!ObjectUtils.isEmpty(ol.getDailyLimit()))
					  .map(OfferLimit::getDailyLimit).collect(Collectors.toList())		
					: null;
			
		} else if(StringUtils.equalsIgnoreCase(level, OfferConstants.ACCOUNT_OFFER_COUNTER.get())){
			
			limits = CollectionUtils.isNotEmpty(customerSegmentNames) && CollectionUtils.isNotEmpty(offerLimits)	
					? offerLimits.stream().filter(ol->!ObjectUtils.isEmpty(ol.getAccountDailyLimit()))
					  .map(OfferLimit::getAccountDailyLimit).collect(Collectors.toList())		
					: null;
			
		} else if(StringUtils.equalsIgnoreCase(level, OfferConstants.MEMBER_OFFER_COUNTER.get())){
			
			limits = CollectionUtils.isNotEmpty(customerSegmentNames) && CollectionUtils.isNotEmpty(offerLimits)	
					? offerLimits.stream().filter(ol->!ObjectUtils.isEmpty(ol.getMemberDailyLimit()))
					  .map(OfferLimit::getMemberDailyLimit).collect(Collectors.toList())		
					: null;
			
		}  
		
		if(CollectionUtils.isNotEmpty(limits)) {
		  
			limit = Utilities.getMaxValueInIntegerList(limits);
			
		}
		
		
		return limit;
	}
	
	/**
	 * 
	 * @param offerLimits
	 * @param customerSegmentNames
	 * @param level
	 * @return list of weekly limit from list of offer denomination limit
	 */
	public static Integer mapWeeklyLimits(List<OfferLimit> offerLimits,
			List<String> customerSegmentNames, String level) {
		
		Integer limit = null;
		List<Integer> limits = null;
		boolean check = CollectionUtils.isNotEmpty(customerSegmentNames) && CollectionUtils.isNotEmpty(offerLimits);
		
		if(StringUtils.equalsIgnoreCase(level, OfferConstants.OFFER_COUNTER.get())){
			
			limits = check	
					? offerLimits.stream().filter(ol->Utilities.presentInList(customerSegmentNames, ol.getCustomerSegment())
							&& !ObjectUtils.isEmpty(ol.getWeeklyLimit()))
					  .map(OfferLimit::getWeeklyLimit).collect(Collectors.toList())		
					: null;
			
		} else if(StringUtils.equalsIgnoreCase(level, OfferConstants.ACCOUNT_OFFER_COUNTER.get())){
			
			limits = check	
					? offerLimits.stream().filter(ol->Utilities.presentInList(customerSegmentNames, ol.getCustomerSegment())
							&& !ObjectUtils.isEmpty(ol.getAccountWeeklyLimit()))
					  .map(OfferLimit::getAccountWeeklyLimit).collect(Collectors.toList())		
					: null;
			
		} else if(StringUtils.equalsIgnoreCase(level, OfferConstants.MEMBER_OFFER_COUNTER.get())){
			
			limits = check	
					? offerLimits.stream().filter(ol->Utilities.presentInList(customerSegmentNames, ol.getCustomerSegment())
							&& !ObjectUtils.isEmpty(ol.getMemberWeeklyLimit()))
					  .map(OfferLimit::getMemberWeeklyLimit).collect(Collectors.toList())		
					: null;
			
		}  
		
		if(CollectionUtils.isNotEmpty(limits)) {
		  
			limit = Utilities.getMaxValueInIntegerList(limits);
			
		}
		
		return limit;
	}
	
	/**
	 * 
	 * @param offerLimits
	 * @param customerSegmentNames
	 * @param level
	 * @return list of monthly limit from list of offer denomination limit
	 */
	public static Integer mapMonthlyLimits(List<OfferLimit> offerLimits,
			List<String> customerSegmentNames, String level) {
		
		Integer limit = null;
		List<Integer> limits = null;
		boolean check = CollectionUtils.isNotEmpty(customerSegmentNames) && CollectionUtils.isNotEmpty(offerLimits);
		
		if(StringUtils.equalsIgnoreCase(level, OfferConstants.OFFER_COUNTER.get())){
			
			limits = check 	
					? offerLimits.stream().filter(ol->Utilities.presentInList(customerSegmentNames, ol.getCustomerSegment())
							&& !ObjectUtils.isEmpty(ol.getMonthlyLimit()))
					  .map(OfferLimit::getMonthlyLimit).collect(Collectors.toList())		
					: null;
			
		} else if(StringUtils.equalsIgnoreCase(level, OfferConstants.ACCOUNT_OFFER_COUNTER.get())){
			
			limits = check 	
					? offerLimits.stream().filter(ol->Utilities.presentInList(customerSegmentNames, ol.getCustomerSegment())
							&& !ObjectUtils.isEmpty(ol.getAccountMonthlyLimit()))
					  .map(OfferLimit::getAccountMonthlyLimit).collect(Collectors.toList())		
					: null;
			
		} else if(StringUtils.equalsIgnoreCase(level, OfferConstants.MEMBER_OFFER_COUNTER.get())){
			
			limits = check 	
					? offerLimits.stream().filter(ol->Utilities.presentInList(customerSegmentNames, ol.getCustomerSegment())
							&& !ObjectUtils.isEmpty(ol.getMemberMonthlyLimit()))
					  .map(OfferLimit::getMemberMonthlyLimit).collect(Collectors.toList())		
					: null;
			
		}  
		
		if(CollectionUtils.isNotEmpty(limits)) {
		  
			limit = Utilities.getMaxValueInIntegerList(limits);
			
		}
		
		return limit;
	}
	
	/**
	 * 
	 * @param offerLimits
	 * @param customerSegmentNames
	 * @param level
	 * @return list of annual limit from list of offer denomination limit
	 */
	public static Integer mapAnnualLimits(List<OfferLimit> offerLimits,
			List<String> customerSegmentNames, String level) {
		
		Integer limit = null;
		List<Integer> limits = null;
		boolean check = CollectionUtils.isNotEmpty(customerSegmentNames) && CollectionUtils.isNotEmpty(offerLimits);
		
		if(StringUtils.equalsIgnoreCase(level, OfferConstants.OFFER_COUNTER.get())){
			
			limits = check	
					? offerLimits.stream().filter(ol->Utilities.presentInList(customerSegmentNames, ol.getCustomerSegment())
							&& !ObjectUtils.isEmpty(ol.getAnnualLimit()))
					  .map(OfferLimit::getAnnualLimit).collect(Collectors.toList())		
					: null;
			
		} else if(StringUtils.equalsIgnoreCase(level, OfferConstants.ACCOUNT_OFFER_COUNTER.get())){
			
			limits = check	
					? offerLimits.stream().filter(ol->Utilities.presentInList(customerSegmentNames, ol.getCustomerSegment())
							&& !ObjectUtils.isEmpty(ol.getAccountAnnualLimit()))
					  .map(OfferLimit::getAccountAnnualLimit).collect(Collectors.toList())		
					: null;
			
		} else if(StringUtils.equalsIgnoreCase(level, OfferConstants.MEMBER_OFFER_COUNTER.get())){
			
			limits = check	
					? offerLimits.stream().filter(ol->Utilities.presentInList(customerSegmentNames, ol.getCustomerSegment())
							&& !ObjectUtils.isEmpty(ol.getMemberAnnualLimit()))
					  .map(OfferLimit::getMemberAnnualLimit).collect(Collectors.toList())		
					: null;
			
		}  
		
		if(CollectionUtils.isNotEmpty(limits)) {
			
			limit = Utilities.getMaxValueInIntegerList(limits);
			
		}
		
		return limit;
	}
	
	/**
	 * 
	 * @param offerLimits
	 * @param customerSegmentNames
	 * @param level
	 * @return list of total limit from list of offer denomination limit
	 */
	public static Integer mapTotalLimits(List<OfferLimit> offerLimits,
			List<String> customerSegmentNames, String level) {
		
		Integer limit = null;
		List<Integer> limits = null;
		
		boolean check = CollectionUtils.isNotEmpty(customerSegmentNames) && CollectionUtils.isNotEmpty(offerLimits);
		
		if(StringUtils.equalsIgnoreCase(level, OfferConstants.OFFER_COUNTER.get())){
			
			limits = check	
					? offerLimits.stream().filter(ol->Utilities.presentInList(customerSegmentNames, ol.getCustomerSegment())
							&& !ObjectUtils.isEmpty(ol.getDownloadLimit()))
					  .map(OfferLimit::getDownloadLimit).collect(Collectors.toList())		
					: null;
			
		} else if(StringUtils.equalsIgnoreCase(level, OfferConstants.ACCOUNT_OFFER_COUNTER.get())){
			
			limits = check	
					? offerLimits.stream().filter(ol->Utilities.presentInList(customerSegmentNames, ol.getCustomerSegment())
							&& !ObjectUtils.isEmpty(ol.getAccountTotalLimit()))
					  .map(OfferLimit::getAccountTotalLimit).collect(Collectors.toList())		
					: null;
			
		} else if(StringUtils.equalsIgnoreCase(level, OfferConstants.MEMBER_OFFER_COUNTER.get())){
			
			limits = check	
					? offerLimits.stream().filter(ol->Utilities.presentInList(customerSegmentNames, ol.getCustomerSegment())
							&& !ObjectUtils.isEmpty(ol.getMemberTotalLimit()))
					  .map(OfferLimit::getMemberTotalLimit).collect(Collectors.toList())		
					: null;
			
		}  
		
		if(CollectionUtils.isNotEmpty(limits)) {
		  
			limit = Utilities.getMaxValueInIntegerList(limits);
			
		}
		
		return limit;
	}
	
	/**
	 * 
	 * @param denominationList
	 * @return list of denomination from list of offer denomination limit
	 */
	private static List<Integer> mapOfferDenominationInLimit(List<DenominationLimit> denominationList) {
		
		Set<Integer> limitDenominationsSet = 
			   !CollectionUtils.isEmpty(denominationList)
			   ?  denominationList.stream()
				 .map(DenominationLimit::getDenomination)
				 .collect(Collectors.toSet())	   
			   : null;
				 
		return CollectionUtils.isNotEmpty(limitDenominationsSet)
			 ? limitDenominationsSet.stream().collect(Collectors.toList())
			 : null;
	}
	
	/**
	 * 
	 * @param denominationList
	 * @return list of daily limits from list of denomination limit
	 */
    public static Integer mapDailyLimitForDenomination(List<DenominationLimit> denominationList) {
		
		List<Integer> quantityList = CollectionUtils.isNotEmpty(denominationList)
				 ? denominationList.stream().filter(d->!ObjectUtils.isEmpty(d.getDailyLimit()))
				   .map(DenominationLimit::getDailyLimit).collect(Collectors.toList())		 
				 : null;
				   
		return CollectionUtils.isNotEmpty(quantityList)
			 ? Utilities.getMaxValueInIntegerList(quantityList)
			 : null;
	}
    
    /**
	 * 
	 * @param denominationList
	 * @return list of weekly limits from list of denomination limit
	 */
	public static Integer mapWeeklyLimitForDenomination(List<DenominationLimit> denominationList) {
		
		List<Integer> quantityList = CollectionUtils.isNotEmpty(denominationList)
				 ? denominationList.stream().filter(d->!ObjectUtils.isEmpty(d.getWeeklyLimit()))
				   .map(DenominationLimit::getWeeklyLimit).collect(Collectors.toList())		 
				 : null;
				   
		return null!=quantityList && !quantityList.isEmpty()
			 ? Utilities.getMaxValueInIntegerList(quantityList)
			 : null;
	}
	
	/**
	 * 
	 * @param denominationList
	 * @return list of monthly limits from list of denomination limit
	 */
	public static Integer mapMonthlyLimitForDenomination(List<DenominationLimit> denominationList) {
		
		List<Integer> quantityList = CollectionUtils.isNotEmpty(denominationList)
				 ? denominationList.stream().filter(d->!ObjectUtils.isEmpty(d.getMonthlyLimit()))
				   .map(DenominationLimit::getMonthlyLimit).collect(Collectors.toList())		 
				 : null;
				   
		return null!=quantityList && !quantityList.isEmpty()
			 ? Utilities.getMaxValueInIntegerList(quantityList)
			 : null;
	}
	
	/**
	 * 
	 * @param denominationList
	 * @return list of annual limits from list of denomination limit
	 */
	public static Integer mapAnnualLimitForDenomination(List<DenominationLimit> denominationList) {
		
		List<Integer> quantityList = CollectionUtils.isNotEmpty(denominationList)
				 ? denominationList.stream().filter(d->!ObjectUtils.isEmpty(d.getAnnualLimit()))
				   .map(DenominationLimit::getAnnualLimit).collect(Collectors.toList())		 
				 : null;
				   
		return null!=quantityList && !quantityList.isEmpty()
			 ? Utilities.getMaxValueInIntegerList(quantityList)
			 : null;
	}
	
	/**
	 * 
	 * @param denominationList
	 * @return list of total limits from list of denomination limit
	 */
	public static Integer mapTotalLimitForDenomination(List<DenominationLimit> denominationList) {
		
		List<Integer> quantityList = CollectionUtils.isNotEmpty(denominationList)
				 ? denominationList.stream().filter(d->!ObjectUtils.isEmpty(d.getTotalLimit()))
				   .map(DenominationLimit::getTotalLimit).collect(Collectors.toList())		 
				 : null;
				   
		return null!=quantityList && !quantityList.isEmpty()
			 ? Utilities.getMaxValueInIntegerList(quantityList)
			 : null;
	}
    
	/**
	 * 
	 * @param purchaseList
	 * @return list of id from list of purchase history
	 */
	public static List<String> mapPurchaseIdFromTransactionList(List<PurchaseHistory> purchaseList) {
		
		return CollectionUtils.isNotEmpty(purchaseList)
			 ? purchaseList.stream()
			  .map(PurchaseHistory::getId)
			  .collect(Collectors.toList())
			 : null;
	}
	
	/**
	 * 
	 * @param purchaseList
	 * @return list of payment methods from list of purchase history
	 */
	public static Set<String> mapAllPaymentMethodsFromPurchaseList(List<PurchaseHistory> purchaseList) {
		
		return CollectionUtils.isNotEmpty(purchaseList)
			 ? purchaseList.stream().map(PurchaseHistory::getPaymentMethod).collect(Collectors.toSet())
			 :null;
	}
	
	/**
	 * 
	 * @param records
	 * @return list of all partner codes from purchase history records
	 */
	public static List<String> mapAllPartnerCodesFromPurchaseHistory(List<PurchaseHistory> records) {
		
		Set<String> partnerCodeSet = !CollectionUtils.isEmpty(records)
				? records.stream().map(PurchaseHistory::getPartnerCode).collect(Collectors.toSet())
				: null;		
		
		return !CollectionUtils.isEmpty(partnerCodeSet)
			  ? partnerCodeSet.stream().collect(Collectors.toList())
			  : null;
	}

	/**
	 * 
	 * @param storeList
	 * @param predicate
	 * @return list of store codes from list of stores
	 */
	public static List<String> mapStoreCodesFromStoreList(List<Store> storeList, Predicate<Store> predicate) {
		
		return CollectionUtils.isNotEmpty(storeList)
			 ? storeList.stream().filter(predicate)
			   .map(Store::getStoreCode).collect(Collectors.toList())
			 : null;
	}
	
	/**
	 * 
	 * @param store
	 * @return list of contact numbers from list of stores
	 */
	public static List<String> mapAllStoreContactNumbers(Store store, Predicate<ContactPerson> predicate) {
		
		return !ObjectUtils.isEmpty(store) 
			&& CollectionUtils.isNotEmpty(store.getContactPersons())
			? store.getContactPersons().stream().filter(predicate)
			 .map(ContactPerson::getMobileNumber)
        	 .collect(Collectors.toList())
			: null;
	}

	/**
	 * 
	 * @param denominationList
	 * @return list of dirham values from list of denominations
	 */
	public static List<Integer> mapDenominationDirhamValues(List<Denomination> denominationList) {
		
		return CollectionUtils.isNotEmpty(denominationList)
			 ? denominationList.stream()
			   .map(Denomination::getDenominationValue)
			   .map(DenominationValue::getDirhamValue)
			   .collect(Collectors.toList())
			 : null;
	}
	
	/**
	 * 
	 * @param offerLimitList
	 * @param predicate
	 * @return list of denomination limit for specific denomination from list of offer limit
	 */
	public static List<DenominationLimit> mapAllSameDenominationLimitForoffer(List<OfferLimit> offerLimitList, Predicate<DenominationLimit> predicate) {
		
		List<DenominationLimit> offerDenominationLimit = null;
		
		if(!CollectionUtils.isEmpty(offerLimitList)) {
			
			offerDenominationLimit = new ArrayList<>(1);
			
			for(OfferLimit limit : offerLimitList) {
				
				DenominationLimit denominationLimit = FilterValues.findAnyDenominationLimitInDenominationLimitList(limit.getDenominationLimit(), predicate); 
			    
				if(!ObjectUtils.isEmpty(denominationLimit)) {
					
					offerDenominationLimit.add(denominationLimit);
				}
				
			}
			
		}
		
		return offerDenominationLimit;
	}

	/**
	 * 
	 * @param offerLimitList
	 * @param predicate
	 * @return
	 */
	public static List<DenominationLimit> mapAllSameDenominationLimitForAccount(List<OfferLimit> offerLimitList, Predicate<DenominationLimit> predicate) {
		
		List<DenominationLimit> offerDenominationLimit = null;
		
		if(!CollectionUtils.isEmpty(offerLimitList)) {
			
			offerDenominationLimit = new ArrayList<>(1);
			
			for(OfferLimit limit : offerLimitList) {
				
				DenominationLimit denominationLimit = FilterValues.findAnyDenominationLimitInDenominationLimitList(limit.getAccountDenominationLimit(), predicate); 
			    
				if(!ObjectUtils.isEmpty(denominationLimit)) {
					
					offerDenominationLimit.add(denominationLimit);
				}
				
			}
			
		}
		
		return offerDenominationLimit;
	}
	
	/**
	 * 
	 * @param offerLimitList
	 * @param predicate
	 * @return
	 */
	public static List<DenominationLimit> mapAllSameDenominationLimitForMember(List<OfferLimit> offerLimitList, Predicate<DenominationLimit> predicate) {
		
		List<DenominationLimit> offerDenominationLimit = null;
		
		if(!CollectionUtils.isEmpty(offerLimitList)) {
			
			offerDenominationLimit = new ArrayList<>(1);
			
			for(OfferLimit limit : offerLimitList) {
				
				DenominationLimit denominationLimit = FilterValues.findAnyDenominationLimitInDenominationLimitList(limit.getMemberDenominationLimit(), predicate); 
			    
				if(!ObjectUtils.isEmpty(denominationLimit)) {
					
					offerDenominationLimit.add(denominationLimit);
				}
				
			}
			
		}
		
		return offerDenominationLimit;
	}

	/**
	 * 
	 * @param denominationLimitList
	 * @param predicate
	 * @return
	 */
	public static List<Integer> getDailyLimitFromDenominationLimits(List<DenominationLimit> denominationLimitList, Predicate<DenominationLimit> predicate) {
		
		return !CollectionUtils.isEmpty(denominationLimitList)
			? denominationLimitList.stream()
			  .filter(predicate)
			  .map(DenominationLimit::getDailyLimit)
			  .collect(Collectors.toList())
			: null;
	}
	
	/**
	 * 
	 * @param denominationLimitList
	 * @param predicate
	 * @return
	 */
	public static List<Integer> getWeeklyLimitFromDenominationLimits(List<DenominationLimit> denominationLimitList, Predicate<DenominationLimit> predicate) {
		
		return !CollectionUtils.isEmpty(denominationLimitList)
				? denominationLimitList.stream()
				  .filter(predicate)
				  .map(DenominationLimit::getWeeklyLimit)
				  .collect(Collectors.toList())
				: null;
	}

	/**
	 * 
	 * @param denominationLimitList
	 * @param predicate
	 * @return
	 */
	public static List<Integer> getMonthlyLimitFromDenominationLimits(List<DenominationLimit> denominationLimitList, Predicate<DenominationLimit> predicate) {
		
		return !CollectionUtils.isEmpty(denominationLimitList)
				? denominationLimitList.stream()
				  .filter(predicate)
				  .map(DenominationLimit::getMonthlyLimit)
				  .collect(Collectors.toList())
				: null;
	}

	/**
	 * 
	 * @param denominationLimitList
	 * @param predicate
	 * @return
	 */
	public static List<Integer> getAnnualLimitFromDenominationLimits(List<DenominationLimit> denominationLimitList, Predicate<DenominationLimit> predicate) {
		
		return !CollectionUtils.isEmpty(denominationLimitList)
				? denominationLimitList.stream()
				  .filter(predicate)
				  .map(DenominationLimit::getAnnualLimit)
				  .collect(Collectors.toList())
				: null;
	}

	/**
	 * 
	 * @param denominationLimitList
	 * @param predicate
	 * @return
	 */
	public static List<Integer> getTotalLimitFromDenominationLimits(List<DenominationLimit> denominationLimitList, Predicate<DenominationLimit> predicate) {
		
		return !CollectionUtils.isEmpty(denominationLimitList)
				? denominationLimitList.stream()
				  .filter(predicate)
				  .map(DenominationLimit::getTotalLimit)
				  .collect(Collectors.toList())
				: null;
	}
	
	/**
	 * 
	 * @param birthdayAccountsList
	 * @return
	 */
	public static List<String> getAllAccountNumbersFromBirthdayAccountList(
			List<BirthdayAccountsDto> birthdayAccountsList) {
		
		return !CollectionUtils.isEmpty(birthdayAccountsList)
				? birthdayAccountsList.stream()
				  .map(BirthdayAccountsDto::getAccountNumber)
				  .collect(Collectors.toList())
				: null;
	}
	
	/**
	 * 
	 * @param birthdayAccountsList
	 * @return
	 */
	public static List<String> getAllMembershipCodeFromBirthdayAccountList(
			List<BirthdayAccountsDto> birthdayAccountsList) {
		
		return !CollectionUtils.isEmpty(birthdayAccountsList)
				? birthdayAccountsList.stream()
				  .map(BirthdayAccountsDto::getMembershipCode)
				  .collect(Collectors.toList())
				: null;
	}
	
	/**
	 * 
	 * @param merchantList
	 * @return
	 */
	public static List<String> getMerchantCodes(List<Merchant> merchantList) {
		
		return !CollectionUtils.isEmpty(merchantList)
				? merchantList.stream()
				  .map(Merchant::getMerchantCode)
				  .collect(Collectors.toList())
				: null;
	}
	
	/**
	 * 
	 * @param offerList
	 * @param predicate
	 * @return
	 */
	public static List<String> mapOfferIdFromOfferResponseList(List<OfferCatalogResultResponseDto> offerList,
			Predicate<OfferCatalogResultResponseDto> predicate) {
		
		return !CollectionUtils.isEmpty(offerList)
				? offerList.stream()
				  .filter(predicate) 		
				  .map(OfferCatalogResultResponseDto::getOfferId)
				  .collect(Collectors.toList())
				: null;
	}

	/**
	 * 
	 * @param offerPaymentMethods
	 * @param predicate
	 * @return
	 */
	public static List<String> mapEligiblePaymentMethods(List<PaymentMethod> offerPaymentMethods,
			Predicate<PaymentMethod> predicate) {
		
		return CollectionUtils.isNotEmpty(offerPaymentMethods) 
			? offerPaymentMethods.stream()
			.filter(predicate)
			.map(PaymentMethod::getDescription)
			.collect(Collectors.toList())
			: null;
			
	}
	
	/**
	 * 
	 * @param eligiblePaymentMethods
	 * @return
	 */
	public static List<String> mapMemberPaymentMethods(List<PaymentMethods> eligiblePaymentMethods) {
		
		return CollectionUtils.isNotEmpty(eligiblePaymentMethods) 
			 ? eligiblePaymentMethods.stream()
			  .map(PaymentMethods::getDescription)
			  .collect(Collectors.toList())
			 : null;
	}
	
	/**
	 * 
	 * @param customerTypeDetails
	 * @return
	 */
	public static List<String> mapCustomerTypeValues(List<ParentChlidCustomer> customerTypeDetails) {
		
		return CollectionUtils.isNotEmpty(customerTypeDetails)
			 ? customerTypeDetails.stream()
			  .map(ParentChlidCustomer::getChild)
			  .collect(Collectors.toList())
			 : null;
		
	}
	
	/**
	 * 
	 * @param limit
	 * @param predicate
	 * @return
	 */
	public static List<String> mapCustomerSegmentNamesFromLimit(List<LimitDto> limit, Predicate<LimitDto> predicate) {
			
			return CollectionUtils.isNotEmpty(limit) 
				 ? limit.stream()
				  .filter(predicate)
				  .map(LimitDto::getCustomerSegment).collect(Collectors.toList())
				 : null;
	 }
	
	/**
	 * 
	 * @param denominationLimit
	 * @return list of denomination limit for specific denomination from list of DenominationLimitDto
	 */
	public static List<Integer> mapDenominationValuesFromLimit(List<DenominationLimitDto> denominationLimit) {
			
			return CollectionUtils.isNotEmpty(denominationLimit)
				 ? denominationLimit.stream()
				  .map(DenominationLimitDto::getDenomination)
				  .collect(Collectors.toList())
				 : null;
	 }

	/**
	 * 
	 * @param offerStores
	 * @return list of store codes from list of stores
	 */
	public static List<String> mapAllStoreCodes(List<Store> stores) {
		
		return CollectionUtils.isNotEmpty(stores)
				 ? stores.stream()
				  .map(Store::getStoreCode)
				  .collect(Collectors.toList())
				 : null;
	}
	
	/**
	 * 
	 * @param offerStores
	 * @return list of store codes from list of stores
	 */
	public static List<String> mapAllDirhamValues(List<Denomination> denominations) {
		
		List<Integer> dirhamValues = CollectionUtils.isNotEmpty(denominations)
							      ? denominations.stream()
									  .map(Denomination::getDenominationValue)
									  .map(DenominationValue::getDirhamValue)
									  .collect(Collectors.toList())
								  : null;
		
		return !CollectionUtils.isEmpty(dirhamValues) 
			? dirhamValues.stream().map(d->String.valueOf(d)).collect(Collectors.toList())
			: null;
	}
	
	/**
     * 
     * @param offerList
     * @param predicate
     * @return list of offerId for list of offers with limits
     */
	public static List<String> mapEligibleOfferIdList(List<EligibleOffers> offerList, Predicate<EligibleOffers> predicate) {
		
		return CollectionUtils.isNotEmpty(offerList)
				 ? offerList.stream()
				   .filter(predicate)
				   .map(EligibleOffers::getOfferId)
				   .collect(Collectors.toList())	 
				 : null;
	}
	
	
	/**
	 * 
	 * @param offerList
	 * @param predicate
	 * @return  list of partner codes from list of eligible offers
	 */
	public static List<String> mapPartnerCodesFromEligibleOfferList(List<EligibleOffers> offerList, Predicate<EligibleOffers> predicate) {
		
		Set<String> partnerCodeSet = CollectionUtils.isNotEmpty(offerList)
					 ? offerList.stream().filter(predicate)
					   .map(EligibleOffers::getPartnerCode)
					   .collect(Collectors.toSet())	 
					 : null;
		
		if(!CollectionUtils.isEmpty(partnerCodeSet)) {
			
			partnerCodeSet.add(OfferConstants.SMILES.get());
			
		} 			   
					   
		return CollectionUtils.isNotEmpty(partnerCodeSet)
			  ? partnerCodeSet.stream().collect(Collectors.toList())
			  : Arrays.asList(OfferConstants.SMILES.get());
	}
	
	/**
	 * 
	 * @param offerList
	 * @param predicate
	 * @return  list of offerTypeId from list of eligible offers
	 */
    public static List<String> mapOfferTypeIdFromEligibleOfferList(List<EligibleOffers> offerList, Predicate<EligibleOffers> predicate) {
		
    	Set<String> offerTypeSet = CollectionUtils.isNotEmpty(offerList)
				 ? offerList.stream().filter(predicate)
				   .map(EligibleOffers::getOfferType)
				   .map(EligibleOfferType::getOfferTypeId)
				   .collect(Collectors.toSet())	 
				 : null;
				   
	   return CollectionUtils.isNotEmpty(offerTypeSet)
				  ? offerTypeSet.stream().collect(Collectors.toList())
				  : null;
	}
    
    /**
	 * 
	 * @param denominationList
	 * @return list of dirham values from list of eligible offer denominations
	 */
	public static List<Integer> mapEligibleOfferDenominationDirhamValues(List<EligibleOfferDenomination> denominationList) {
		
		return CollectionUtils.isNotEmpty(denominationList)
			 ? denominationList.stream()
			   .map(EligibleOfferDenomination::getDenominationValue)
			   .map(DenominationValue::getDirhamValue)
			   .collect(Collectors.toList())
			 : null;
	}
	
	/**
	 * 
	 * @param storeList
	 * @param predicate
	 * @return list of store codes from list of stores
	 */
	public static List<String> mapEligibleOfferStoreCodesFromStoreList(List<EligibleOfferStore> storeList, Predicate<EligibleOfferStore> predicate) {
		
		return CollectionUtils.isNotEmpty(storeList)
			 ? storeList.stream().filter(predicate)
			   .map(EligibleOfferStore::getStoreCode).collect(Collectors.toList())
			 : null;
	}
	
	/**
	 * 
	 * @param store
	 * @return list of contact numbers from list of stores
	 */
	public static List<String> mapAllEligibleOfferStoreContactNumbers(EligibleOfferStore store, Predicate<ContactPerson> predicate) {
		
		return !ObjectUtils.isEmpty(store) 
			&& CollectionUtils.isNotEmpty(store.getContactPersons())
			? store.getContactPersons().stream().filter(predicate)
			 .map(ContactPerson::getMobileNumber)
        	 .collect(Collectors.toList())
			: null;
	}

	/**
	 * 
	 * @param subOffer
	 * @param sameSubOfferId
	 * @return list of subOfferId that matches input predicate
	 */
	public static List<String> mapSubOfferIdList(List<SubOfferDto> subOfferList, Predicate<SubOfferDto> predicate) {
		
		return CollectionUtils.isNotEmpty(subOfferList)
				? subOfferList.stream()
				 .filter(predicate)
				 .map(SubOfferDto::getSubOfferId)
	        	 .collect(Collectors.toList())
				: null;
	}
	
	/**
	 * 
	 * @param offerCounterList
	 * @return list of offer id from offer counter list
	 */
	public static List<String> mapOfferIdFromOfferCounterList(List<OfferCounters> offerCounterList, Predicate<OfferCounters> predicate) {
		
		return CollectionUtils.isNotEmpty(offerCounterList)
			? offerCounterList.stream()
			 .filter(predicate)		
			 .map(OfferCounters::getOfferId)
        	 .collect(Collectors.toList())
			: null;
	}
	
	/**
	 * 
	 * @param offerCounterList
	 * @return list of offer id from member offer counter list
	 */
	public static List<String> mapOfferIdFromMemberOfferCounterList(List<MemberOfferCounts> offerCounterList) {
		
		return CollectionUtils.isNotEmpty(offerCounterList)
			? offerCounterList.stream()
			 .map(MemberOfferCounts::getOfferId)
        	 .collect(Collectors.toSet())
        	 .stream()
        	 .collect(Collectors.toList())
			: null;
	}
	
	/**
	 * 
	 * @param errorRecordsList
	 * @return list of account numbers from error records
	 */
	public static List<String> mapAccountNumberFromErrorRecords(List<ErrorRecords> errorRecordsList) {
		
		return CollectionUtils.isNotEmpty(errorRecordsList)
				? errorRecordsList.stream()
				 .map(ErrorRecords::getAccountNumber)
				 .collect(Collectors.toSet())
				 .stream()
	        	 .collect(Collectors.toList())
				: null;
	}

	/**
	 * 
	 * @param errorRecordsList
	 * @return list of membership code from error records
	 */
	public static List<String> mapMembershipCodeFromErrorRecords(List<ErrorRecords> errorRecordsList) {
		
		return CollectionUtils.isNotEmpty(errorRecordsList)
				? errorRecordsList.stream()
				 .map(ErrorRecords::getMembershipCode)
				 .collect(Collectors.toSet())
				 .stream()
	        	 .collect(Collectors.toList())
				: null;
	}
	
	/**
	 * 
	 * @param errorRecordsList
	 * @return list of offer Id from error records
	 */
	public static List<String> mapOfferIdFromErrorRecords(List<ErrorRecords> errorRecordsList) {
		
		return CollectionUtils.isNotEmpty(errorRecordsList)
				? errorRecordsList.stream()
				 .map(ErrorRecords::getOfferId)
				 .collect(Collectors.toSet())
				 .stream()
	        	 .collect(Collectors.toList())
				: null;
	}

	/**
	 * 
	 * @param offerPaymentMethods
	 * @return list of PaymentMethodIsa from list of PaymentMethods
	 */
	public static List<String> mapAllPaymentMethodIds(List<PaymentMethod> paymentMethods) {
		
		return CollectionUtils.isNotEmpty(paymentMethods)
				 ? paymentMethods.stream()
				  .map(PaymentMethod::getPaymentMethodId)
				  .collect(Collectors.toList())
				 : null;
	}
}
