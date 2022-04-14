package com.loyalty.marketplace.offers.utils;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import com.loyalty.marketplace.equivalentpoints.outbound.database.entity.ConversionRate;
import com.loyalty.marketplace.image.outbound.database.entity.MarketplaceImage;
import com.loyalty.marketplace.offers.decision.manager.outbound.dto.RuleResult;
import com.loyalty.marketplace.offers.helper.dto.DenominationLimitCounter;
import com.loyalty.marketplace.offers.helper.dto.LimitCounter;
import com.loyalty.marketplace.offers.member.management.outbound.dto.ParentChlidCustomer;
import com.loyalty.marketplace.offers.merchants.outbound.database.entity.Merchant;
import com.loyalty.marketplace.offers.outbound.database.entity.AccountOfferCount;
import com.loyalty.marketplace.offers.outbound.database.entity.AccountOfferCounts;
import com.loyalty.marketplace.offers.outbound.database.entity.BirthdayGiftTracker;
import com.loyalty.marketplace.offers.outbound.database.entity.DenominationCount;
import com.loyalty.marketplace.offers.outbound.database.entity.DenominationLimit;
import com.loyalty.marketplace.offers.outbound.database.entity.EligibleOfferDenomination;
import com.loyalty.marketplace.offers.outbound.database.entity.EligibleOfferStore;
import com.loyalty.marketplace.offers.outbound.database.entity.EligibleOffers;
import com.loyalty.marketplace.offers.outbound.database.entity.MemberOfferCount;
import com.loyalty.marketplace.offers.outbound.database.entity.MemberOfferCounts;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCounter;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCounters;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferLimit;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchaseHistory;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchasePaymentMethod;
import com.loyalty.marketplace.offers.outbound.database.entity.SubOffer;
import com.loyalty.marketplace.offers.outbound.dto.OfferCatalogResultResponseDto;
import com.loyalty.marketplace.offers.outbound.dto.PaymentMethodDto;
import com.loyalty.marketplace.offers.outbound.dto.StoreDto;
import com.loyalty.marketplace.offers.stores.outbound.database.entity.Store;
import com.loyalty.marketplace.outbound.database.entity.Category;
import com.loyalty.marketplace.outbound.database.entity.Denomination;
import com.loyalty.marketplace.outbound.database.entity.PaymentMethod;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherListResult;

/**
 * 
 * @author jaya.shukla
 *
 */
public class FilterValues {
	
	FilterValues(){
		
	}
	
	////////////////////////// OfferCatalog ////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param offerList
	 * @param predicate
	 * @return filtered specific OfferCatalog as per predicate from OfferLimit list
	 */
	public static OfferCatalog findAnyOfferWithinOfferList(List<OfferCatalog> offerList, Predicate<OfferCatalog> predicate) {
		
		return CollectionUtils.isNotEmpty(offerList)
				 ? offerList.stream().filter(predicate).findAny().orElse(null)	 
				 : null;
	}
	
	/**
	 * 
	 * @param offerList
	 * @param predicate
	 * @return filtered OfferCatalog list as per predicate from input OfferCatalog list
	 */
	public static List<OfferCatalog> filterOfferList(List<OfferCatalog> offerList, Predicate<OfferCatalog> predicate) {
		
		return CollectionUtils.isNotEmpty(offerList)
				 ? offerList.stream().filter(predicate).collect(Collectors.toList())	 
				 : null;
	}
	
	//////////////////////////EligibleOffers ////////////////////////////////////////////////////////////
	
	/**
	* 
	* @param offerList
	* @param predicate
	* @return filtered specific EligibleOffer as per predicate from EligibleOffers list
	*/
	public static EligibleOffers findAnyEligibleOfferWithinOfferList(List<EligibleOffers> offerList, Predicate<EligibleOffers> predicate) {
	
		return CollectionUtils.isNotEmpty(offerList)
			? offerList.stream().filter(predicate).findAny().orElse(null)	 
			: null;
	}
	
	/**
	* 
	* @param offerList
	* @param predicate
	* @return filtered EligibleOffers list as per predicate from input EligibleOffers list
	*/
	public static List<EligibleOffers> filterEligibleOfferList(List<EligibleOffers> offerList, Predicate<EligibleOffers> predicate) {
	
	return CollectionUtils.isNotEmpty(offerList)
		? offerList.stream().filter(predicate).collect(Collectors.toList())	 
		: null;
	}
	
    ////////////////////////// OfferLimit ////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param limitList
	 * @param predicate
	 * @return  filtered specific OfferLimit as per predicate from OfferLimit list
	 */
	public static OfferLimit findAnyLimitWithinLimitList(List<OfferLimit> limitList, Predicate<OfferLimit> predicate) {
		
		return CollectionUtils.isNotEmpty(limitList)
				 ? limitList.stream().filter(predicate).findAny().orElse(null)	 
				 : null;
	}
	
	/**
	 * 
	 * @param limitList
	 * @param predicate
	 * @return filtered OfferCatalog list as per predicate from input OfferCatalog list
	 */
	public static List<OfferLimit> filterOfferLimits(List<OfferLimit> limitList, Predicate<OfferLimit> predicate) {
		
		return CollectionUtils.isNotEmpty(limitList)
				 ? limitList.stream().filter(predicate).collect(Collectors.toList())	 
				 : null;
	}
	
    //////////////////////////OfferCounter ////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param counterList
	 * @param predicate
	 * @return  filtered specific OfferCounter as per predicate from OfferCounter list
	 */
	public static OfferCounter findAnyOfferCounterinCounterList(List<OfferCounter> counterList, Predicate<OfferCounter> predicate) {
		
		return CollectionUtils.isNotEmpty(counterList)
				 ? counterList.stream().filter(predicate).findAny().orElse(null)	 
				 : null;
	}
	
	/**
	 * 
	 * @param counterList
	 * @param predicate
	 * @return filtered OfferCatalog list as per predicate from input OfferCatalog list
	 */
	public static List<OfferCounter> filterOfferCounters(List<OfferCounter> counterList, Predicate<OfferCounter> predicate) {
		
		return CollectionUtils.isNotEmpty(counterList)
				 ? counterList.stream().filter(predicate).collect(Collectors.toList())	 
				 : null;
	}
	
	////////////////////////// PurchaseHistory ////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param purchaseList
	 * @param predicate
	 * @return filtered specific PurchaseHistory as per predicate from PurchaseHistory list
	 */
	public static PurchaseHistory findAnyPurchaseRecordinRecordList(List<PurchaseHistory> purchaseList, Predicate<PurchaseHistory> predicate) {
		
		return CollectionUtils.isNotEmpty(purchaseList)
				 ? purchaseList.stream().filter(predicate).findAny().orElse(null)	 
				 : null;
	}
	
	/**
	 * 
	 * @param purchaseList
	 * @param predicate
	 * @return filtered PurchaseHistory list as per predicate from input PurchaseHistory list
	 */
	public static List<PurchaseHistory> filterPurchaseRecords(List<PurchaseHistory> purchaseList, Predicate<PurchaseHistory> predicate) {
		
		return CollectionUtils.isNotEmpty(purchaseList)
				 ? purchaseList.stream().filter(predicate).collect(Collectors.toList())	 
				 : null;
	} 
		
	//////////////////////////PurchasePaymentMethod ////////////////////////////////////////////////////////////
		
	/**
	 * 
	 * @param purchasePaymentMethodList
	 * @param predicate
	 * @return filtered specific PurchasePaymentMethod as per predicate from PurchasePaymentMethod list
	 */
	public static PurchasePaymentMethod findAnyPurchasePaymentMethodWithinList(List<PurchasePaymentMethod> purchasePaymentMethodList, Predicate<PurchasePaymentMethod> predicate) {
	
		return CollectionUtils.isNotEmpty(purchasePaymentMethodList)
		? purchasePaymentMethodList.stream().filter(predicate).findAny().orElse(null)	 
		: null;
	}
	
	//////////////////////////ConversionRate ////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param conversionRateList
	 * @param predicate
	 * @return filtered specific ConversionRate as per predicate from ConversionRate list
	 */
	public static ConversionRate findAnyConversionRateWithinRateList(List<ConversionRate> conversionRateList, Predicate<ConversionRate> predicate) {
	
		return CollectionUtils.isNotEmpty(conversionRateList)
			 ? conversionRateList.stream().filter(predicate).findAny().orElse(null)	 
			 : null;
	}
	
	/**
	 * 
	 * @param conversionRateList
	 * @param predicate
	 * @return filtered ConversionRate list as per predicate from input ConversionRate list
	 */
    public static List<ConversionRate> filterConversionRateList(List<ConversionRate> conversionRateList, Predicate<ConversionRate> predicate) {
		
		return CollectionUtils.isNotEmpty(conversionRateList)
			 ? conversionRateList.stream().filter(predicate).collect(Collectors.toList())	 
			 : null;
	}
    
    //////////////////////////MarketplaceImage //////////////////////////////////////////////////////////// 
    
    /**
     * 
     * @param imageList
     * @param predicate
     * @return filtered MarketplaceImage list as per predicate from input MarketplaceImage list
     */
    public static List<MarketplaceImage> filterImageList(List<MarketplaceImage> imageList, Predicate<MarketplaceImage> predicate) {
		
		 return CollectionUtils.isNotEmpty(imageList)
				 ? imageList.stream().filter(predicate).collect(Collectors.toList())	 
				 : null;
	}

    //////////////////////////RuleResult //////////////////////////////////////////////////////////// 
    
    /**
     * 
     * @param rulesResultList
     * @param predicate
     * @return filtered specific RuleResult as per predicate from RuleResult list
     */
	public static RuleResult findAnyRuleResultWithinRuleList(List<RuleResult> rulesResultList,
			Predicate<RuleResult> predicate) {
		
		return CollectionUtils.isNotEmpty(rulesResultList)
				 ? rulesResultList.stream().filter(predicate).findAny().orElse(null)
				 : null;
	}
	
	//////////////////////////PaymentMethod ////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param paymentMethodList
	 * @param predicate
	 * @return filtered specific PaymentMethod as per predicate from PaymentMethod list
	 */
	public static PaymentMethod findAnyPaymentMethodinMethodList(List<PaymentMethod> paymentMethodList, Predicate<PaymentMethod> predicate) {
			
			return CollectionUtils.isNotEmpty(paymentMethodList)
				 ? paymentMethodList.stream().filter(predicate).findAny().orElse(null)	 
				 : null;
	}
	
	//////////////////////////StoreDto ////////////////////////////////////////////////////////////
		
	/**
	 * 
	 * @param offerStores
	 * @param predicate
	 * @return filtered specific StoreDto as per predicate from StoreDto list
	 */
	public static StoreDto findAnyStoreDtoInList(List<StoreDto> offerStores, Predicate<StoreDto> predicate) {
		
		return CollectionUtils.isNotEmpty(offerStores)
			 ? offerStores.stream().filter(predicate).findAny().orElse(null)	 
			 : null;
	}

	/**
	 * 
	 * @param storeDtoList
	 * @param predicate
	 * @return filtered StoreDto list as per predicate from input StoreDto list
	 */
	public static List<StoreDto> filterStoreDtoList(List<StoreDto> storeDtoList, Predicate<StoreDto> predicate) {
	
		return CollectionUtils.isNotEmpty(storeDtoList)
			? storeDtoList.stream().filter(predicate).collect(Collectors.toList())	 
			: null;
	}
	
	//////////////////////////Store ////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param offerStores
	 * @param predicate
	 * @return filtered specific Store as per predicate from Store list
	 */
	public static Store findAnyStoreInList(List<Store> offerStores, Predicate<Store> predicate) {
	
		return CollectionUtils.isNotEmpty(offerStores)
			 ? offerStores.stream().filter(predicate).findAny().orElse(null)	 
			 : null;
	}

	//////////////////////////DenominationLimitCounter ////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param denominationLimitCounterList
	 * @param predicate
	 * @return filtered specific DenominationLimitCounter as per predicate from DenominationLimitCounter list
	 */
	public static DenominationLimitCounter findDenominationDtoInDenominationDtoList(
			List<DenominationLimitCounter> denominationLimitCounterList, Predicate<DenominationLimitCounter> predicate) {
		
		return CollectionUtils.isNotEmpty(denominationLimitCounterList)
				? denominationLimitCounterList.stream().filter(predicate).findAny().orElse(null)
				: null;
	}
	
	//////////////////////////DenominationLimit////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param denominationLimitList
	 * @param predicate
	 * @return filtered specific DenominationLimit as per predicate from DenominationLimit list
	 */
	public static DenominationLimit findAnyDenominationLimitInDenominationLimitList(
	List<DenominationLimit> denominationLimitList, Predicate<DenominationLimit> predicate) {
	
		return CollectionUtils.isNotEmpty(denominationLimitList)
			? denominationLimitList.stream().filter(predicate).findAny().orElse(null)
			: null;
	}

	//////////////////////////DenominationCount////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param denominationCountList
	 * @param predicate
	 * @return filtered specific DenominationCount as per predicate from DenominationCount list
	 */
	public static DenominationCount findAnyDenominationCounterInDenominationCountList(
			List<DenominationCount> denominationCountList, Predicate<DenominationCount> predicate) {
		
		return CollectionUtils.isNotEmpty(denominationCountList)
				? denominationCountList.stream().filter(predicate).findAny().orElse(null)
				: null;
	}

	//////////////////////////AccountOfferCount////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param accountCounterList
	 * @param predicate
	 * @return filtered specific AccountOfferCount as per predicate from AccountOfferCount list
	 */
	public static AccountOfferCount findAccountOfferCountInOfferAccountCounterList(
			List<AccountOfferCount> accountCounterList, Predicate<AccountOfferCount> predicate) {
		
		return CollectionUtils.isNotEmpty(accountCounterList)
				? accountCounterList.stream().filter(predicate).findAny().orElse(null)
				: null;
	}
	
	//////////////////////////MemberOfferCount////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param memberCounterList
	 * @param predicate
	 * @return filtered specific MemberOfferCount as per predicate from MemberOfferCount list
	 */
	public static MemberOfferCount findMemberOfferCountInOfferMemberCounterList(
		List<MemberOfferCount> memberCounterList, Predicate<MemberOfferCount> predicate) {
		
		return CollectionUtils.isNotEmpty(memberCounterList)
			? memberCounterList.stream().filter(predicate).findAny().orElse(null)
			: null;
	}

	//////////////////////////Denomination////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param denominationList
	 * @param predicate
	 * @return filtered specific Denomination as per predicate from Denomination list
	 */
	public static Denomination findAnyDenominationInDenominationList(List<Denomination> denominationList,
			Predicate<Denomination> predicate) {
		
		return CollectionUtils.isNotEmpty(denominationList)
				? denominationList.stream().filter(predicate).findAny().orElse(null)
				: null;
	}
	
	//////////////////////////EligibleOfferDenomination////////////////////////////////////////////////////////////
		
	/**
	* 
	* @param denominationList
	* @param predicate
	* @return filtered specific Denomination as per predicate from Denomination list
	*/
	public static EligibleOfferDenomination findAnyEligibleOfferDenominationInDenominationList(List<EligibleOfferDenomination> denominationList,
				Predicate<EligibleOfferDenomination> predicate) {
	
		return CollectionUtils.isNotEmpty(denominationList)
			? denominationList.stream().filter(predicate).findAny().orElse(null)
			: null;
	}

	//////////////////////////Category////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param categoryList
	 * @param predicate
	 * @return filtered specific Category as per predicate from Category list
	 */
	public static Category findCategoryInCategoryList(List<Category> categoryList, Predicate<Category> predicate) {
		
		return CollectionUtils.isNotEmpty(categoryList)
				? categoryList.stream().filter(predicate).findAny().orElse(null)
				: null;
	}
	
	/**
	 * 
	 * @param categoryList
	 * @param predicate
	 * @return filtered Category list as per predicate from input Category list
	 */
	public static List<Category> filterCategoryList(List<Category> categoryList, Predicate<Category> predicate) {
		
		return CollectionUtils.isNotEmpty(categoryList)
			? categoryList.stream().filter(predicate).collect(Collectors.toList())	 
			: null;
	}

	//////////////////////////Merchant////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param merchantList
	 * @param predicate
	 * @return filtered specific Merchant as per predicate from Merchant list
	 */
	public static Merchant findAnyMerchantInMerchantList(List<Merchant> merchantList,
			Predicate<Merchant> predicate) {
		
		return CollectionUtils.isNotEmpty(merchantList)
				? merchantList.stream().filter(predicate).findAny().orElse(null)
				: null;
	}

	//////////////////////////BirthdayGiftTracker////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param birthdayTrackerList
	 * @param predicate
	 * @return filtered specific BirthdayGiftTracker as per predicate from BirthdayGiftTracker list
	 */
	public static BirthdayGiftTracker findAnyBirthdayTrackerInList(List<BirthdayGiftTracker> birthdayTrackerList,
			Predicate<BirthdayGiftTracker> predicate) {
		
		return CollectionUtils.isNotEmpty(birthdayTrackerList)
				? birthdayTrackerList.stream().filter(predicate).findAny().orElse(null)
				: null;
	}
	
	//////////////////////////OfferCatalogResultResponseDto////////////////////////////////////////////////////////////
		
	/**
	 * 
	 * @param offerList
	 * @param predicate
	 * @return filtered specific OfferCatalogResultResponseDto as per predicate from OfferCatalogResultResponseDto list
	 */
	public static OfferCatalogResultResponseDto findAnyOfferResponseWithinOfferResponseList(List<OfferCatalogResultResponseDto> offerList, Predicate<OfferCatalogResultResponseDto> predicate) {
		
		return CollectionUtils.isNotEmpty(offerList)
				 ? offerList.stream().filter(predicate).findAny().orElse(null)	 
				 : null;
	}
	
	/**
	 * 
	 * @param offerList
	 * @param predicate
	 * @return filtered OfferCatalogResultResponseDto list as per predicate from input OfferCatalogResultResponseDto list
	 */
	public static List<OfferCatalogResultResponseDto> filterOfferResponseList(List<OfferCatalogResultResponseDto> offerList, Predicate<OfferCatalogResultResponseDto> predicate) {
	
		return CollectionUtils.isNotEmpty(offerList)
			? offerList.stream().filter(predicate).collect(Collectors.toList())	 
			: null;
	}

	//////////////////////////VoucherListResult////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param voucherDetails
	 * @param predicate
	 * @return filtered VoucherListResult list as per predicate from input VoucherListResult list
	 */
	public static List<VoucherListResult> filterVoucherDetailsList(List<VoucherListResult> voucherDetails,
			Predicate<VoucherListResult> predicate) {
		
		return CollectionUtils.isNotEmpty(voucherDetails)
			 ? voucherDetails.stream()
			  .filter(predicate)
			  .collect(Collectors.toList())
			 : null;
			  
	}
	
	//////////////////////////PaymentMethodDto////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param paymentMethods
	 * @param predicate
	 * @return filtered PaymentMethodDto list as per predicate from input PaymentMethodDto list
	 */
	public static List<PaymentMethodDto> filterPaymenMethodDtoList(List<PaymentMethodDto> paymentMethods,
			Predicate<PaymentMethodDto> predicate) {
		
		return CollectionUtils.isNotEmpty(paymentMethods)
			? paymentMethods.stream()
			 .filter(predicate)
			 .collect(Collectors.toList())
			: null;
			 
	}
	
	//////////////////////////SubOffer////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param subOffer
	 * @param predicate
	 * @return filtered specific SubOffer as per predicate from SubOffer list
	 */
	public static SubOffer findAnySubOfferInList(List<SubOffer> subOffer, Predicate<SubOffer> predicate) {
		
		return !CollectionUtils.isEmpty(subOffer)
			? subOffer.stream()
			 .filter(predicate)
			 .findAny().orElse(null)
			: null;		
		
	}
	
	//////////////////////////ParentChlidCustomer////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param customerTypeDetails
	 * @param predicate
	 * @return filtered specific ParentChlidCustomer as per predicate from ParentChlidCustomer list
	 */
	public static ParentChlidCustomer findAnyParentChildCustomerTupeInList(List<ParentChlidCustomer> customerTypeDetails, 
			Predicate<ParentChlidCustomer> predicate) {
		
		return CollectionUtils.isNotEmpty(customerTypeDetails)
				? customerTypeDetails.stream()
				.filter(predicate)
				.findAny().orElse(null)
				: null;
		
	}
	
	//////////////////////////LimitCounter ////////////////////////////////////////////////////////////
		
	/**
	 * 
	 * @param limitCounterList
	 * @param predicate
	 * @return filtered specific LimitCounter as per predicate from LimitCounter list
	 */
	public static LimitCounter findAnyLimitCounterWithinList(List<LimitCounter> limitCounterList, Predicate<LimitCounter> predicate) {
	
		return CollectionUtils.isNotEmpty(limitCounterList)
			? limitCounterList.stream().filter(predicate).findAny().orElse(null)	 
			: null;
	}
	
	/**
	 * 
	 * @param offerStores
	 * @param predicate
	 * @return filtered specific Store as per predicate from Store list
	 */
	public static EligibleOfferStore findAnyEligibleOfferStoreInList(List<EligibleOfferStore> offerStores, Predicate<EligibleOfferStore> predicate) {
	
		return CollectionUtils.isNotEmpty(offerStores)
			 ? offerStores.stream().filter(predicate).findAny().orElse(null)	 
			 : null;
	}
	
	
	/**
	 * 
	 * @param memberOfferCounterList
	 * @param predicate
	 * @return filtered specific OfferCounters as per predicate from OfferCounters list
	 */
	public static OfferCounters findAnyOfferCounterInList(List<OfferCounters> offerCounterList,
			Predicate<OfferCounters> predicate) {
		
		return CollectionUtils.isNotEmpty(offerCounterList)
			 ? offerCounterList.stream().filter(predicate).findAny().orElse(null) 
			 : null;
	}
	
	/**
	 * 
	 * @param accountOfferCounterList
	 * @param predicate
	 * @return filtered specific AccountOfferCounts as per predicate from AccountOfferCounts list
	 */
	public static List<AccountOfferCounts> filterAccountOfferCounterList(List<AccountOfferCounts> accountOfferCounterList,
			Predicate<AccountOfferCounts> predicate) {
		
		return CollectionUtils.isNotEmpty(accountOfferCounterList)
			 ? accountOfferCounterList.stream().filter(predicate).collect(Collectors.toList()) 
			 : null;
	}
	
	/**
	 * 
	 * @param accountOfferCounterList
	 * @param predicate
	 * @return filtered specific AccountOfferCounts as per predicate from AccountOfferCounts list
	 */
	public static AccountOfferCounts findAnyAccountOfferCounterInList(List<AccountOfferCounts> accountOfferCounterList,
			Predicate<AccountOfferCounts> predicate) {
		
		return CollectionUtils.isNotEmpty(accountOfferCounterList)
			 ? accountOfferCounterList.stream().filter(predicate).findAny().orElse(null) 
			 : null;
	}
	
	/**
	 * 
	 * @param memberOfferCounterList
	 * @param predicate
	 * @return filtered specific MemberOfferCounts as per predicate from MemberOfferCounts list
	 */
	public static MemberOfferCounts findAnyMemberOfferCounterInList(List<MemberOfferCounts> memberOfferCounterList,
			Predicate<MemberOfferCounts> predicate) {
		
		return CollectionUtils.isNotEmpty(memberOfferCounterList)
			 ? memberOfferCounterList.stream().filter(predicate).findAny().orElse(null) 
			 : null;
	}
	
	/**
	 * 
	 * @param accountCounterList
	 * @param predicate
	 * @return filtered specific AccountOfferCount as per predicate from AccountOfferCount list
	 */
	public static AccountOfferCounts findAccountOfferCounterInOfferAccountCounterList(
			List<AccountOfferCounts> accountCounterList, Predicate<AccountOfferCounts> predicate) {
		
		return CollectionUtils.isNotEmpty(accountCounterList)
				? accountCounterList.stream().filter(predicate).findAny().orElse(null)
				: null;
	}
	
	/**
	 * 
	 * @param list
	 * @param predicate
	 * @return filtered string list from predicate list
	 */
	public static List<String> filterStringListWithCondition(List<String> list,
			Predicate<String> predicate) {
		
		return CollectionUtils.isNotEmpty(list)
			 ? list.stream().filter(predicate).collect(Collectors.toList())	 
			 : null;
	}
	
	//////////////////////////PaymentMethod ////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param offerPaymentMethods
	 * @param predicate
	 * @return filtered specific paymentMethod as per predicate from paymentMethod list
	 */
	public static PaymentMethod findAnyPaymentMethodInList(List<PaymentMethod> offerPaymentMethods, Predicate<PaymentMethod> predicate) {
	
		return CollectionUtils.isNotEmpty(offerPaymentMethods)
			 ? offerPaymentMethods.stream().filter(predicate).findAny().orElse(null)	 
			 : null;
	}
	
}
