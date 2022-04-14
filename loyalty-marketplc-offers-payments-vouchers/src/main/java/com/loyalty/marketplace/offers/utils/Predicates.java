package com.loyalty.marketplace.offers.utils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import com.loyalty.marketplace.equivalentpoints.outbound.database.entity.ConversionRate;
import com.loyalty.marketplace.image.outbound.database.entity.MarketplaceImage;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.decision.manager.outbound.dto.RuleResult;
import com.loyalty.marketplace.offers.helper.dto.DenominationLimitCounter;
import com.loyalty.marketplace.offers.helper.dto.EligibilityInfo;
import com.loyalty.marketplace.offers.helper.dto.LimitCounter;
import com.loyalty.marketplace.offers.helper.dto.PurchaseCount;
import com.loyalty.marketplace.offers.inbound.dto.LimitDto;
import com.loyalty.marketplace.offers.inbound.dto.SubOfferDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.CustomerTypeEntity;
import com.loyalty.marketplace.offers.member.management.outbound.dto.ParentChlidCustomer;
import com.loyalty.marketplace.offers.member.management.outbound.dto.PaymentMethods;
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
import com.loyalty.marketplace.offers.stores.outbound.database.entity.ContactPerson;
import com.loyalty.marketplace.offers.stores.outbound.database.entity.Store;
import com.loyalty.marketplace.outbound.database.entity.Category;
import com.loyalty.marketplace.outbound.database.entity.Denomination;
import com.loyalty.marketplace.outbound.database.entity.PaymentMethod;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherListResult;

/**
 * This class creates all the predicates required for filter and map operations
 * 
 * @author jaya.shukla
 *
 */
public class Predicates {

	

	Predicates() {

	}

	/**
	 * 
	 * @return predicate for offers that are discount voucher
	 */
	public static Predicate<OfferCatalog> isDiscountVoucher() {
		return o -> Checks.checkIsDiscountVoucher(o.getOfferType().getOfferTypeId());
	}

	/**
	 * 
	 * @param offerId
	 * @return predicate for offers that have same offer id as input
	 */
	public static Predicate<OfferCatalog> sameIdForOffer(String offerId) {
		return o -> StringUtils.equals(o.getOfferId(), offerId);
	}

	/**
	 * 
	 * @return predicate for offers that have limits present
	 */
	public static Predicate<OfferCatalog> limitPresent() {
		return o -> !CollectionUtils.isEmpty(o.getLimit());
	}

	/**
	 * 
	 * @return predicate for offers that have customer segments present
	 */
	public static Predicate<OfferCatalog> isCustomerSegmentPresent() {
		return o -> !ObjectUtils.isEmpty(o.getCustomerSegments())
				&& (!CollectionUtils.isEmpty(o.getCustomerSegments().getEligibleTypes())
						|| !CollectionUtils.isEmpty(o.getCustomerSegments().getExclusionTypes()));
	}

	/**
	 * 
	 * @return predicate for offers that have limits for customer segments
	 */
	public static Predicate<OfferCatalog> isCustomerSegmentsInOfferLimits() {
		return o -> !ObjectUtils.isEmpty(o.getLimit()) && !ObjectUtils
				.isEmpty(FilterValues.findAnyLimitWithinLimitList(o.getLimit(), isCustomerSegmentInLimits()));
	}

	/**
	 * 
	 * @return predicate for offers with non empty partner code
	 */
	public static Predicate<OfferCatalog> notEmptyPartnerCode() {

		return o -> !ObjectUtils.isEmpty(o.getPartnerCode());
	}

	/**
	 * 
	 * @return predicate for offers with non empty offer id
	 */
	public static Predicate<OfferCatalog> notEmptyOfferTypeId() {

		return o -> !ObjectUtils.isEmpty(o.getOfferType()) && !StringUtils.isEmpty(o.getOfferType().getOfferTypeId());
	}

	/**
	 * 
	 * @param customerTypes
	 * @return predicate for offers where input list in defined customer types for
	 *         offer
	 */
	public static Predicate<OfferCatalog> validCustomerType(List<String> customerTypes) {

		return o -> Checks.checkListValuesInList(o.getCustomerTypes(), customerTypes);

	}
	
	/**
	 * 
	 * @param customerSegments
	 * @param ruleResult
	 * @return predicate for offers where member belongs to valid customer segment
	 */
	public static Predicate<OfferCatalog> validCustomerSegment(List<String> customerSegments, RuleResult ruleResult) {

		return o -> (!Checks.checkCinemaOffer(o.getRules())
				&& ObjectUtils.isEmpty(o.getCustomerSegments()))
		        || (CollectionUtils.isNotEmpty(customerSegments)
				&& Checks.checkListValuesInList(o.getCustomerSegments(), customerSegments)
				&& !ObjectUtils.isEmpty(ruleResult)
				&& !ruleResult.isEligibility());

	}

	/**
	 * 
	 * @param offerCounterList
	 * @param denomination
	 * @param couponQuantity
	 * @param resultResponse
	 * @param memberDetails
	 * @return predicate for offers where download limit is not crossed
	 */
	public static Predicate<OfferCatalog> downloadLimitLeft(List<OfferCounter> offerCounterList, Integer denomination,
			Integer couponQuantity, ResultResponse resultResponse, EligibilityInfo eligibilityInfo) {

		return o -> Checks.checkDownloadLimitLeft(
				FilterValues.findAnyOfferCounterinCounterList(offerCounterList, sameOfferIdForLimit(o.getOfferId())), o,
				couponQuantity, eligibilityInfo, denomination, resultResponse,
				Utilities.intersection(eligibilityInfo.getMemberDetails().getCustomerSegment(),
						MapValues.mapCustomerSegmentInOfferLimits(o.getLimit(), isCustomerSegmentInLimits())));
	}

	/**
	 * 
	 * @return predicate for offers where merchant is active and one of the linked
	 *         stores is active
	 */
	public static Predicate<OfferCatalog> activeMerchantAndStore() {

		return o -> !ObjectUtils.isEmpty(o.getMerchant())
				&& StringUtils.equalsIgnoreCase(o.getMerchant().getStatus(), OfferConstants.ACTIVE_STATUS.get())
				&& Checks.checkStoreLinked(o.getOfferStores());

	}

	/**
	 * 
	 * @param channelId
	 * @param subCardType
	 * @return predicate for offers where offer is a gift offer
	 */
	public static Predicate<OfferCatalog> giftOffer(String channelId, String subCardType) {

		return o -> Checks.checkIsGiftOffer(o.getGiftInfo(), channelId, subCardType);

	}

	/**
	 * 
	 * @return predicate for offers where offer is a gift offer
	 */
	public static Predicate<OfferCatalog> hasBirthdayOffer() {

		return o -> StringUtils.equalsIgnoreCase(o.getIsBirthdayGift(), OfferConstants.FLAG_SET.get());
	}
	
	
	/**
	 * 
	 * @return predicate for offers where offer is a cinema offer 
	 */
	public static Predicate<OfferCatalog> isCinemaOffer() {
		
		return o-> Checks.checkCinemaOffer(o.getRules());
	}
	
	/**
	 * 
	 * @param merchantCode
	 * @return predicate for OfferCatalog that has same merchantCode as input
	 */
	public static Predicate<OfferCatalog> sameMerchantForOffer(String merchantCode) {

		return o -> StringUtils.equals(o.getMerchant().getMerchantCode(), merchantCode);
	}
	
	/**
	 * 
	 * @param merchantCode
	 * @return predicate for OfferCatalog that has same categoryId as input
	 */
	public static Predicate<OfferCatalog> sameCategoryForOffer(String categoryId) {

		return o -> StringUtils.equals(o.getCategory().getCategoryId(), categoryId);
	}
	
	/**
	 * 
	 * @param merchantCode
	 * @return predicate for OfferCatalog that has any of the subCategoryId as input list
	 */
	public static Predicate<OfferCatalog> sameSubCategoryForOffer(List<String> subCategoryList) {

		return o ->CollectionUtils.containsAny(subCategoryList, Arrays.asList(o.getSubCategory().getCategoryId()));
	}
	
	/**
	 * 
	 * @return predicate for OfferCatalog that is a new offer
	 */
	public static Predicate<OfferCatalog> isNewOffer() {

		return o ->StringUtils.equalsIgnoreCase(o.getNewOffer(), OfferConstants.FLAG_SET.get());
	}
	
	/**
	 * 
	 * @return predicate for OfferCatalog that has grouped flag set
	 */
	public static Predicate<OfferCatalog> groupedFlagSet() {

		return o ->StringUtils.equalsIgnoreCase(o.getGroupedFlag(), OfferConstants.FLAG_SET.get());
	}
	
	/**
	 * 
	 * @return predicate for offer limit where limit is defined for customer segment
	 */
	public static Predicate<OfferLimit> isCustomerSegmentInLimits() {

		return l -> !ObjectUtils.isEmpty(l.getCustomerSegment());
	}

	/**
	 * 
	 * @return predicate for offer limit limit is not defined for customer segment
	 */
	public static Predicate<OfferLimit> noCustomerSegmentInLimits() {

		return l -> ObjectUtils.isEmpty(l.getCustomerSegment());
	}

	/**
	 * 
	 * @param customerSegmentList
	 * @return predicate for offer limit where customer segment is present in input list
	 */
	public static Predicate<OfferLimit> customerSegmentInListForLimit(List<String> customerSegmentList) {

		return l -> Utilities.presentInList(customerSegmentList, l.getCustomerSegment());
	}

	/**
	 * 
	 * @param customerSegment
	 * @return predicate for offer limit where customer segment is same as input customer segment
	 */
	public static Predicate<OfferLimit> sameCustomerSegmentForLimit(String customerSegment) {

		return l -> StringUtils.equalsIgnoreCase(l.getCustomerSegment(), customerSegment);
	}

	/**
	 * 
	 * @return predicate for offer limit with denomination limit present for offer
	 */
	public static Predicate<OfferLimit> offerDenominationLimitPresent() {

		return l -> CollectionUtils.isNotEmpty(l.getDenominationLimit());
	}

	/**
	 * 
	 * @return predicate for offer limit with denomination limit present for account
	 */
	public static Predicate<OfferLimit> accountDenominationLimitPresent() {

		return l -> CollectionUtils.isNotEmpty(l.getAccountDenominationLimit());
	}

	/**
	 * 
	 * @return predicate for offer limit with denomination limit present for membership
	 */
	public static Predicate<OfferLimit> memberDenominationLimitPresent() {

		return l -> CollectionUtils.isNotEmpty(l.getMemberDenominationLimit());
	}

	/**
	 * 
	 * @param offerId
	 * @return predicate for offer counter with same offerId as input
	 */
	public static Predicate<OfferCounter> sameOfferIdForLimit(String offerId) {

		return c -> StringUtils.equals(c.getOfferId(), offerId);
	}
	
	/**
	 * 
	 * @param offerIdList
	 * @return predicate for offer counter with offerId in input list
	 */
	public static Predicate<OfferCounter> offerIdInlistOrCinemaOfferCounter(List<String> offerIdList) {

		return c -> Utilities.presentInList(offerIdList, c.getOfferId())
				|| Checks.checkCinemaOffer(c.getRules());
	}

	/**
	 * 
	 * @param item
	 * @param startDate
	 * @param endDate
	 * @return predicate for PurchaseHistory with input purchase item and created date in input range
	 */
	public static Predicate<PurchaseHistory> samePurchaseItemAndSuccessStatus(String item) {

		return p -> StringUtils.equalsIgnoreCase(p.getPurchaseItem(), item)
				&& StringUtils.equals(p.getStatus(), OfferConstants.SUCCESS.get());
	}
	
	/**
	 * 
	 * @param itemList
	 * @param startDate
	 * @param endDate
	 * @return predicate for PurchaseItem in input list and in date range
	 */
	public static Predicate<PurchaseHistory> samePurchaseItemListInAnInclusiveDateRange(List<String> itemList, Date startDate,
			Date endDate) {

		return p -> Utilities.presentInList(itemList, p.getPurchaseItem())
				 && Checks.checkDateInRangeInclusive(endDate, startDate, p.getCreatedDate());
	}

	/**
	 * 
	 * @param offerId
	 * @return predicate for PurchaseHistory with input offerId
	 */
	public static Predicate<PurchaseHistory> sameOfferIdInPurchaseRecord(String offerId) {

		return p -> StringUtils.equals(offerId, p.getOfferId());
	}

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @return predicate for PurchaseHistory with duration in input date range
	 */
	public static Predicate<PurchaseHistory> purchasedInDuration(Date startDate, Date endDate) {

		return p -> Checks.checkDateInRangeInclusive(endDate, startDate, p.getCreatedDate());
	}

	/**
	 * 
	 * @param endDate
	 * @param startDate
	 * @param membershipCode
	 * @param accountNumber
	 * @return predicate for PurchaseHistory for a birthday offer purchase
	 */
	public static Predicate<PurchaseHistory> isBirthdayOfferPurchase(Date startDate, Date endDate, String accountNumber,
			String membershipCode) {

		return p -> !ObjectUtils.isEmpty(p.getAdditionalDetails()) && p.getAdditionalDetails().isBirthdayOffer()
				&& Checks.checkDateInRangeInclusive(endDate, startDate, p.getCreatedDate())
				&& StringUtils.equals(accountNumber, p.getAccountNumber())
				&& StringUtils.equals(membershipCode, p.getMembershipCode());
	}

	/**
	 * 
	 * @param purchaseItem
	 * @return predicate for PurchasePaymentMethod where purchaseItem matches input
	 */
	public static Predicate<PurchasePaymentMethod> matchesPurchaseItem(String purchaseItem) {

		return p -> StringUtils.equalsIgnoreCase(p.getPurchaseItem(), purchaseItem);
	}

	/**
	 * 
	 * @param offerId
	 * @return predicate for MarketplaceImage with input domainId
	 */
	public static Predicate<MarketplaceImage> sameDomainId(String offerId) {

		return i -> !ObjectUtils.isEmpty(i.getMerchantOfferImage())
				&& StringUtils.equalsIgnoreCase(offerId, i.getMerchantOfferImage().getDomainId());
	}

	/**
	 * 
	 * @param accountNumber
	 * @return predicate for RuleResult with same account number
	 */
	public static Predicate<RuleResult> sameAccountNumberForRule(String accountNumber) {

		return r -> StringUtils.equals(accountNumber, r.getAccountNumber());
	}

	/**
	 * 
	 * @param description
	 * @return predicate for PaymentMethod with same description
	 */
	public static Predicate<PaymentMethod> sameDescriptionForPaymentMethod(String description) {

		return p -> StringUtils.equalsIgnoreCase(p.getDescription(), description);
	}

	/**
	 * 
	 * @return predicate for Store that have active status
	 */
	public static Predicate<Store> activeStores() {

		return s -> StringUtils.equalsIgnoreCase(s.getStatus(), OfferConstants.ACTIVE_STATUS.get());
	}

	/**
	 * 
	 * @param storeCode
	 * @return predicate for Store that have same storCode as input
	 */
	public static Predicate<Store> sameStoreCode(String storeCode) {

		return s -> StringUtils.equals(s.getStoreCode(), storeCode);
	}

	/**
	 * 
	 * @param storeIdList
	 * @return predicate for StoreDto that have same storCode in input list
	 */
	public static Predicate<StoreDto> storeCodeInList(List<String> storeIdList) {

		return s -> Utilities.presentInList(storeIdList, s.getStoreCode());
	}

	/**
	 * 
	 * @return predicate for StoreDto that have location of the store present
	 */
	public static Predicate<StoreDto> storeLocationPresent() {

		return s -> !ObjectUtils.isEmpty(s.getStoreCoordinates())
				&& !StringUtils.isEmpty(s.getStoreCoordinates().get(0))
				&& !StringUtils.isEmpty(s.getStoreCoordinates().get(1));
	}

	/**
	 * 
	 * @param dirhamValue
	 * @return predicate for DenominationLimitCounter that same denomination value as input
	 */
	public static Predicate<DenominationLimitCounter> sameDenominationDirhamValue(Integer dirhamValue) {

		return d -> dirhamValue.equals(d.getDenomination());
	}

	/**
	 * 
	 * @param denomination
	 * @return predicate for DenominationCount that has same denomination in the counter
	 */
	public static Predicate<DenominationCount> sameDenominationForOfferCounter(Integer denomination) {

		return d -> d.getDenomination().equals(denomination);
	}

	/**
	 * 
	 * @param denomination
	 * @return predicate for DenominationLimit that has same denomination in the limit
	 */
	public static Predicate<DenominationLimit> sameDenominationForOfferLimit(Integer denomination) {

		return d -> d.getDenomination().equals(denomination);
	}

	/**
	 * 
	 * @return predicate for DenominationLimit where daily limit in denomination is not null
	 */
	public static Predicate<DenominationLimit> denominationDailyLimitNotNull() {

		return d -> !ObjectUtils.isEmpty(d.getDailyLimit());
	}

	/**
	 * 
	 * @return predicate for DenominationLimit where weekly limit in denomination is not null
	 */
	public static Predicate<DenominationLimit> denominationWeeklyLimitNotNull() {

		return d -> !ObjectUtils.isEmpty(d.getWeeklyLimit());
	}

	/**
	 * 
	 * @return predicate for DenominationLimit where monthly limit in denomination is not null
	 */
	public static Predicate<DenominationLimit> denominationMonthlyLimitNotNull() {

		return d -> !ObjectUtils.isEmpty(d.getMonthlyLimit());
	}

	/**
	 * 
	 * @return predicate for DenominationLimit where annual limit in denomination is not null
	 */
	public static Predicate<DenominationLimit> denominationAnnualLimitNotNull() {

		return d -> !ObjectUtils.isEmpty(d.getAnnualLimit());
	}

	/**
	 * 
	 * @return predicate for DenominationLimit where total limit in denomination is not null
	 */
	public static Predicate<DenominationLimit> denominationTotalLimitNotNull() {

		return d -> !ObjectUtils.isEmpty(d.getTotalLimit());
	}

	/**
	 * 
	 * @param accountNumber
	 * @param membershipCode
	 * @return predicate for AccountOfferCount that has same account number and membershipCode as input
	 */
	public static Predicate<AccountOfferCount> sameAccountNumberAndMembershipCodeForCounter(String accountNumber,
			String membershipCode) {

		return a -> StringUtils.equals(accountNumber, a.getAccountNumber())
				&& StringUtils.equals(membershipCode, a.getMembershipCode());
	}

	/**
	 * 
	 * @param membershipCode
	 * @return predicate for MemberOfferCount that has same membershipCode as input
	 */
	public static Predicate<MemberOfferCount> sameMembershipCodeForCounter(String membershipCode) {

		return m -> StringUtils.equals(membershipCode, m.getMembershipCode());
	}

	/**
	 * 
	 * @param denominationId
	 * @return predicate for Denomination that has same denominationId as input
	 */
	public static Predicate<Denomination> sameDenominationIdForDenomination(String denominationId) {

		return d -> StringUtils.equals(denominationId, d.getDenominationId());
	}

	/**
	 * 
	 * @param dirhamValue
	 * @return predicate for Denomination that has same dirhamValue as input
	 */
	public static Predicate<Denomination> sameDirhamValueForDenomination(Integer dirhamValue) {

		return d -> d.getDenominationValue().getDirhamValue().equals(dirhamValue);
	}

	/**
	 * 
	 * @param categoryId
	 * @return predicate for Category that has same categoryId as input
	 */
	public static Predicate<Category> sameCategoryId(String categoryId) {

		return c -> StringUtils.equals(c.getCategoryId(), categoryId);
	}

	/**
	 * 
	 * @param categoryIdList
	 * @return predicate for Category that has categoryId present in input list
	 */
	public static Predicate<Category> presentInCatgeoryList(List<String> categoryIdList) {

		return c -> Utilities.presentInList(categoryIdList, c.getCategoryId());
	}

	/**
	 * 
	 * @param merchantCode
	 * @return predicate for Merchant that has same merchantCode as input
	 */
	public static Predicate<Merchant> sameMerchantCode(String merchantCode) {

		return m -> StringUtils.equals(m.getMerchantCode(), merchantCode);
	}

	/**
	 * 
	 * @param accountNumber
	 * @param membershipCode
	 * @return predicate for BirthdayGiftTracker that has same accountNumber and membershipCode as in input
	 */
	public static Predicate<BirthdayGiftTracker> sameAccountNumberAndMembershipCodeInTracker(String accountNumber,
			String membershipCode) {

		return b -> StringUtils.equals(accountNumber, b.getAccountNumber())
				&& StringUtils.equals(membershipCode, b.getMembershipCode());
	}

	/**
	 * 
	 * @param offerTypeId
	 * @return predicate for OfferCatalogResultResponseDto that has same offerTypeId as input
	 */
	public static Predicate<OfferCatalogResultResponseDto> sameOfferTypeId(List<String> offerTypeIdList) {

		return o -> Utilities.presentInList(offerTypeIdList, o.getOfferTypeId());
	}

	/**
	 * 
	 * @return predicate for OfferCatalogResultResponseDto that are featured offers
	 */
	public static Predicate<OfferCatalogResultResponseDto> featuredOffers() {

		return o -> StringUtils.equalsIgnoreCase(o.getIsFeatured(), OfferConstants.FLAG_SET.get());
	}

	/**
	 * 
	 * @return predicate for OfferCatalogResultResponseDto that are not featured offers
	 */
	public static Predicate<OfferCatalogResultResponseDto> nonFeaturedOffers() {

		return o -> StringUtils.equalsIgnoreCase(o.getIsFeatured(), OfferConstants.FLAG_NOT_SET.get());
	}

	/**
	 * 
	 * @param offerId
	 * @return predicate for OfferCatalogResultResponseDto that have same offerId as input
	 */
	public static Predicate<OfferCatalogResultResponseDto> sameOfferIdForOfferResponse(String offerId) {

		return o -> StringUtils.equals(offerId, o.getOfferId());
	}

	/**
	 * 
	 * @return predicate for OfferCatalogResultResponseDto that have no stores with location present
	 */
	public static Predicate<OfferCatalogResultResponseDto> noStoresWithLocationInOffer() {

		return o -> ObjectUtils.isEmpty(FilterValues.findAnyStoreDtoInList(o.getOfferStores(), storeLocationPresent()));
	}

	/**
	 * 
	 * @return predicate for OfferCatalogResultResponseDto that have stores with location present
	 */
	public static Predicate<OfferCatalogResultResponseDto> storeWithLocationInOffer() {

		return o -> !ObjectUtils
				.isEmpty(FilterValues.findAnyStoreDtoInList(o.getOfferStores(), storeLocationPresent()));
	}

	/**
	 * 
	 * @param memberPayamentMethods
	 * @return predicate for PaymentMethod that are present in payment methods for member
	 */
	public static Predicate<PaymentMethod> presentInMemberPaymentMethod(List<PaymentMethods> memberPayamentMethods) {

		List<String> memberMethods = MapValues.mapMemberPaymentMethods(memberPayamentMethods);
		return p -> Utilities.presentInList(memberMethods, p.getDescription());
	}

	/**
	 * 
	 * @return predicate for LimitDto that have customer segment present in limit
	 */
	public static Predicate<LimitDto> limitWithCustomerSegmentPresent() {

		return l -> !StringUtils.isEmpty(l.getCustomerSegment());
	}

	/**
	 * 
	 * @param id
	 * @param voucherCode
	 * @return predicate for VoucherListResult that have same Id and voucher code for transaction
	 */
	public static Predicate<VoucherListResult> sameIdAndVoucherCodeForTransaction(String id, List<String> voucherCode) {

		return v -> Utilities.isEqual(v.getUuid(), id) && Utilities.presentInList(voucherCode, v.getVoucherCode());
	}

	/**
	 * 
	 * @param memberPaymentMethods
	 * @return predicate for PaymentMethodDto than are common with eligible payment methods for member
	 */
	public static Predicate<PaymentMethodDto> paymentMethodInEligiblePaymentMethodsForMember(
			List<String> memberPaymentMethods) {

		return p -> Utilities.presentInList(memberPaymentMethods, p.getDescription());
	}

	/**
	 * 
	 * @param subOfferId
	 * @return predicate for SubOffer that have same subOfferId as input
	 */
	public static Predicate<SubOffer> sameSubOfferId(String subOfferId) {

		return s -> StringUtils.equals(subOfferId, s.getSubOfferId());
	}
	
	/**
	 * 
	 * @param subOfferId
	 * @return predicate for SubOffer input request that have same subOfferId as input
	 */
	public static Predicate<SubOfferDto> sameInputSubOfferId(String subOfferId) {

		return s -> StringUtils.equals(subOfferId, s.getSubOfferId());
	}

	/**
	 * 
	 * @param customerType
	 * @return predicate for ParentChlidCustomer that have same customer type as input
	 */
	public static Predicate<ParentChlidCustomer> sameCustomerTpe(String customerType) {

		return c -> StringUtils.equalsIgnoreCase(c.getChild(), customerType);
	}

	/**
	 * 
	 * @param offerId
	 * @return predicate for LimitCounter that have same offerId as input
	 */
	public static Predicate<LimitCounter> sameOfferIdForLimitCounter(String offerId) {

		return lc -> !ObjectUtils.isEmpty(lc) && !StringUtils.isEmpty(lc.getOfferId()) && !StringUtils.isEmpty(offerId)
				&& StringUtils.equals(lc.getOfferId(), offerId);
	}

	/**
	 * 
	 * @return predicate for ContactPerson with non empty mobile number
	 */
	public static Predicate<ContactPerson> nonEmptyMobileNumber() {

		return c -> !StringUtils.isEmpty(c.getMobileNumber());
	}

	
	/**		
	 * 
	 * @param predicate for OfferCatalog with any of the input values present in tags
	 * @return
	 */
	public static Predicate<OfferCatalog> anyValuePresentInTags(List<String> values) {
		
		return o->!ObjectUtils.isEmpty(o.getTags())
				&& Checks.checkAreaPresentInTags(values, o.getTags());
	}
	
	/**
	 * 
	 * @return predicate for eligible offers that have limits present
	 */
	public static Predicate<EligibleOffers> limitPresentForEligibleOffers() {
		
		return o -> !CollectionUtils.isEmpty(o.getLimit());
	}

	
	/**
	 * 
	 * @param customerTypes
	 * @return predicate for eligible offers where input list in defined customer types for
	 *         offer
	 */
	public static Predicate<EligibleOffers> validCustomerTypeForEligibleOffers(List<String> customerTypes) {

		return o -> Checks.checkListValuesInList(o.getCustomerTypes(), customerTypes);

	}
	
	/**
	 * 
	 * @param customerSegments
	 * @param ruleResult
	 * @return predicate for eligible offers where member belongs to valid customer segment
	 */
	public static Predicate<EligibleOffers> validCustomerSegmentForEligibleOffers(List<String> customerSegments, RuleResult ruleResult) {

		return o -> (!Checks.checkCinemaOffer(o.getRules())
				&& ObjectUtils.isEmpty(o.getCustomerSegments()))
		        || (CollectionUtils.isNotEmpty(customerSegments)
				&& Checks.checkListValuesInList(o.getCustomerSegments(), customerSegments)
				&& !ObjectUtils.isEmpty(ruleResult)
				&& !ruleResult.isEligibility());

	}
	
	/**
	 * 
	 * @return predicate for EligibleOffers where offer is a cinema offer 
	 */
	public static Predicate<EligibleOffers> isCinemaOfferEligibleOffer() {
		
		return o-> Checks.checkCinemaOffer(o.getRules());
	}
	
	/**
	 * 
	 * @param merchantCode
	 * @return predicate for EligibleOffers  that has same merchantCode as input
	 */
	public static Predicate<EligibleOffers> sameMerchantForEligibleOffers(String merchantCode) {

		return o -> StringUtils.equals(o.getMerchant().getMerchantCode(), merchantCode);
	}
	
	/**
	 * 
	 * @param merchantCode
	 * @return predicate for EligibleOffers that has same categoryId as input
	 */
	public static Predicate<EligibleOffers> sameCategoryForEligibleOffers(String categoryId) {

		return o -> StringUtils.equals(o.getCategory().getCategoryId(), categoryId);
	}
	
	/**
	 * 
	 * @param merchantCode
	 * @return predicate for EligibleOffers that has any of the subCategoryId as input list
	 */
	public static Predicate<EligibleOffers> sameSubCategoryForEligibleOffers(List<String> subCategoryList) {

		return o ->CollectionUtils.containsAny(subCategoryList, Arrays.asList(o.getSubCategory().getCategoryId()));
	}
	
	/**
	 * 
	 * @return predicate for EligibleOffers that is a new offer
	 */
	public static Predicate<EligibleOffers> isNewOfferForEligibleOffers() {

		return o ->StringUtils.equalsIgnoreCase(o.getNewOffer(), OfferConstants.FLAG_SET.get());
	}
	
	/**
	 * 
	 * @return predicate for EligibleOffers that has grouped flag set
	 */
	public static Predicate<EligibleOffers> groupedFlagSetForEligibleOffers() {

		return o ->StringUtils.equalsIgnoreCase(o.getGroupedFlag(), OfferConstants.FLAG_SET.get());
	}
	
	/**		
	 * 
	 * @param predicate for EligibleOffers with any of the input values present in tags
	 * @return
	 */
	public static Predicate<EligibleOffers> anyValuePresentInTagsForEligibleOffers(List<String> values) {
		
		return o->!ObjectUtils.isEmpty(o.getTags())
				&& Checks.checkAreaPresentInTags(values, o.getTags());
	}

	/**
	 * 
	 * @return predicate for eligible offers with non empty partner code
	 */
	public static Predicate<EligibleOffers> notEmptyPartnerCodeForEligibleOffer() {

		return o -> !ObjectUtils.isEmpty(o.getPartnerCode());
	}
	
	/**
	 * 
	 * @return predicate for eligible offers with non empty offer type id
	 */
	public static Predicate<EligibleOffers> notEmptyOfferTypeIdForEligibleOffer() {

		return o -> !ObjectUtils.isEmpty(o.getOfferType()) && !StringUtils.isEmpty(o.getOfferType().getOfferTypeId());
	}

	
	/**
	 * 
	 * @return predicate for eligible offers with non empty offer id
	 */
	public static Predicate<EligibleOffers> notEmptyOfferIdForEligibleOffer() {

		return o -> !StringUtils.isEmpty(o.getOfferId());
	}
	
	/**
	 * 
	 * @return predicate for eligible offers that have customer segments present
	 */
	public static Predicate<EligibleOffers> isCustomerSegmentPresentInEligibleOffer() {
		return o -> !ObjectUtils.isEmpty(o.getCustomerSegments())
				&& (!CollectionUtils.isEmpty(o.getCustomerSegments().getEligibleTypes())
						|| !CollectionUtils.isEmpty(o.getCustomerSegments().getExclusionTypes()));
	}

	/**
	 * 
	 * @return predicate for eligible offers that have limits for customer segments
	 */
	public static Predicate<EligibleOffers> isCustomerSegmentsInEligibleOfferLimits() {
		return o -> !ObjectUtils.isEmpty(o.getLimit()) && !ObjectUtils
				.isEmpty(FilterValues.findAnyLimitWithinLimitList(o.getLimit(), isCustomerSegmentInLimits()));
	}

	/**
	 * 
	 * @return predicate for eligible offers that are discount voucher
	 */
	public static Predicate<EligibleOffers> isEligibleOfferDiscountVoucher() {
		return o -> Checks.checkIsDiscountVoucher(o.getOfferType().getOfferTypeId());
	}

	/**
	 * 
	 * @param channelId
	 * @return predicate for eligible offers that present in same channel
	 */
	public static Predicate<EligibleOffers> availableInChannelId(String channelId) {
		
		return o->Utilities.presentInList(o.getAvailableInPortals(), channelId);
	}
	
	/**
	 * 
	 * @param denominationId
	 * @return predicate for EligibleOfferDenomination that has same denominationId as input
	 */
	public static Predicate<EligibleOfferDenomination> sameEligibleOfferDenominationIdForDenomination(String denominationId) {

		return d -> StringUtils.equals(denominationId, d.getDenominationId());
	}
	
	/**
	 * 
	 * @return predicate for Store that have active status
	 */
	public static Predicate<EligibleOfferStore> activeEligibleOfferStores() {

		return s -> StringUtils.equalsIgnoreCase(s.getStatus(), OfferConstants.ACTIVE_STATUS.get());
	}
	
	/**
	 * 
	 * @param storeCode
	 * @return predicate for Store that have same storCode as input
	 */
	public static Predicate<EligibleOfferStore> sameEligibleOfferStoreCode(String storeCode) {

		return s -> StringUtils.equals(s.getStoreCode(), storeCode);
	}

	/**
	 * 
	 * @param channelId
	 * @return predicate for ConversionRate where channel is equal to input channelId
	 */
	public static Predicate<ConversionRate> sameChannelId(String channelId) {
		
		return c->!StringUtils.isEmpty(c.getChannel())
				&& StringUtils.equalsIgnoreCase(channelId, c.getChannel());
	}
	
	/**
	 * 
	 * @param channelId
	 * @return predicate for ConversionRate where no channel is set
	 */
	public static Predicate<ConversionRate> noChannelSet() {
		
		return c->StringUtils.isEmpty(c.getChannel());
	}
	
	/**
	 * 
	 * @return Predicate for VoucherListResult for cancelled vouchers
	 */
	public static Predicate<VoucherListResult> isVoucherCancelled() {
		
		return v-> v.isBlackListed();
	}
	
	/**
	 * 
	 * @return Predicates for OfferCounters for cinema offer
	 */
	public static Predicate<OfferCounters> isOfferCounterForCinemaOffer() {
		
		return o-> Checks.checkCinemaOffer(o.getRules());
	}
	
	/**
	 * 
	 * @param membershipCode
	 * @param offerId
	 * @return Predicates for offer counter with same offer id
	 */
	public static Predicate<OfferCounters> isCounterWithOfferId(String offerId) {
		
		return o -> StringUtils.equals(o.getOfferId(), offerId);
	}
	
	/**
	 * 
	 * @param membershipCode
	 * @param offerId
	 * @return Predicates for member offer counter with same membership code and offer id
	 */
	public static Predicate<MemberOfferCounts> isCounterWithMembershipCodeAndOfferId(String membershipCode, 
			String offerId) {
		
		return m -> StringUtils.equals(m.getMembershipCode(), membershipCode)
				 && StringUtils.equals(m.getOfferId(), offerId);
	}
	
	/**
	 * 
	 * @param accountNumber
	 * @param membershipCode
	 * @param offerId
	 * @return Predicates for account offer counter with same account number, membership code and offer id 
	 */
	public static Predicate<AccountOfferCounts> isCounterWithAccountNumberAndOfferId(String accountNumber,
			String membershipCode, String offerId) {
		
		return a -> StringUtils.equals(a.getAccountNumber(), accountNumber)
				 && StringUtils.equals(a.getMembershipCode(), membershipCode)
				 && StringUtils.equals(a.getOfferId(), offerId);
	}
	
	/**
	 * 
	 * @param accountNumber
	 * @param membershipCode
	 * @param offerId
	 * @return Predicates for account offer counter with same offer id 
	 */
	public static Predicate<AccountOfferCounts> isAccountCounterWithOfferId(String offerId) {
		
		return a -> StringUtils.equals(a.getOfferId(), offerId);
	}
	
	/**
	 * 
	 * @param accountNumber
	 * @param membershipCode
	 * @param offerId
	 * @return Predicates for account offer counter in  account number list, same membership code and offer id 
	 */
	public static Predicate<AccountOfferCounts> isCounterWithAccountNumberListAndMembershipCodeAndOfferId(List<String> accountNumberList,
			String membershipCode, String offerId) {
		
		return a -> CollectionUtils.containsAny(accountNumberList, Arrays.asList(a.getAccountNumber()))
				 && StringUtils.equals(a.getMembershipCode(), membershipCode)
				 && StringUtils.equals(a.getOfferId(), offerId);
	}
	
	/**
	 * 
	 * @param id
	 * @return Predicate for MemberOfferCounts with same id
	 */
	public static Predicate<MemberOfferCounts> isMemberCounterWithSameIdOrMembershipCode(String id, String membershipCode, String offerId) {
		
		return m -> StringUtils.equals(id, m.getId())
				|| (StringUtils.equals(membershipCode, m.getMembershipCode())
				&& StringUtils.equals(offerId, m.getOfferId()));
	}
	
	/**
	 * 
	 * @param id
	 * @return Predicate for MemberOfferCounts with same membership code
	 */
	public static Predicate<MemberOfferCounts> isMemberCounterWithOfferIdAndMembershipCode(String offerId, String membershipCode) {
		
		return m -> StringUtils.equals(offerId, m.getOfferId())
			&& StringUtils.equals(membershipCode, m.getMembershipCode());
	}
	
	
	/**
	 * 
	 * @param id
	 * @return Predicate for OfferCounters with same id
	 */
	public static Predicate<OfferCounters> isOffferCounterWithSameIdOrOfferId(String id, String offerId) {
		
		return o->StringUtils.equals(id, o.getId())
			   || StringUtils.equals(offerId, o.getOfferId());
	}
	
	/**
	 * 
	 * @return Predicates for OfferCounters for cinema offer
	 */
	public static Predicate<OfferCounters> nonEmptyOfferIdInOfferCounter() {
		
		return o-> !StringUtils.isEmpty(o.getOfferId());
	}

	/***
	 * 
	 * @param partnerCode
	 * @param channelId
	 * @param value
	 * @return Predicate for conversion rate that is applicable for partner code list and channel id
	 */
	public static Predicate<ConversionRate> applicableRateForPartnerListAndChannelId(List<String> partnerCodeList, String channelId) {
		
		return c-> (CollectionUtils.containsAny(partnerCodeList, Arrays.asList(c.getPartnerCode())) && StringUtils.equals(channelId, c.getChannel()))
				|| (CollectionUtils.containsAny(partnerCodeList, Arrays.asList(c.getPartnerCode())) && StringUtils.isEmpty(c.getChannel())) 
				|| (StringUtils.equals(OfferConstants.SMILES.get(), c.getPartnerCode()) && StringUtils.equals(channelId, c.getChannel()))
				|| (StringUtils.equals(OfferConstants.SMILES.get(), c.getPartnerCode()) && StringUtils.isEmpty(c.getChannel()));
	}
	
	/***
	 * 
	 * @param partnerCode
	 * @param channelId
	 * @param value
	 * @return Predicate for conversion rate that is applicable for non-smiles partner code and channel id present
	 */
	public static Predicate<ConversionRate> nonSmilesWithChannelId() {
		
		return c-> !StringUtils.equals(OfferConstants.SMILES.get(), c.getPartnerCode()) 
				&& !StringUtils.isEmpty(c.getChannel());
	}
	
	/***
	 * 
	 * @param partnerCode
	 * @param channelId
	 * @param value
	 * @return Predicate for conversion rate that is applicable for non-smiles partner code and channel id not present
	 */
	public static Predicate<ConversionRate> nonSmilesWithEmptyChannelId() {
		
		return c-> !StringUtils.equals(OfferConstants.SMILES.get(), c.getPartnerCode()) 
				&& StringUtils.isEmpty(c.getChannel());
	}
	
	/***
	 * 
	 * @param partnerCode
	 * @param channelId
	 * @param value
	 * @return Predicate for conversion rate that is applicable for smiles partner code and channel id present
	 */
	public static Predicate<ConversionRate> smilesWithChannelId() {
		
		return c-> StringUtils.equals(OfferConstants.SMILES.get(), c.getPartnerCode()) 
				&& !StringUtils.isEmpty(c.getChannel());
	}
	
	/***
	 * 
	 * @param partnerCode
	 * @param channelId
	 * @param value
	 * @return Predicate for conversion rate that is applicable for smiles partner code and channel id not present
	 */
	public static Predicate<ConversionRate> smilesWithEmptyChannelId() {
		
		return c-> StringUtils.equals(OfferConstants.SMILES.get(), c.getPartnerCode()) 
				&& StringUtils.isEmpty(c.getChannel());
	}
	
	/**
	 * 
	 * @return Predicate for ConversionRate for applicable conversion rate for partner and channel
	 */
	public static Predicate<ConversionRate> getApplicableCoversionRateForPartnerAndChannel(){
		
		return nonSmilesWithChannelId().or(nonSmilesWithEmptyChannelId())
		   .or(smilesWithChannelId()).or(smilesWithEmptyChannelId());
	}

	
	/**
	 * 
	 * @return Predicates for EligibleOffers with offer id present
	 */
	public static Predicate<EligibleOffers> isOfferIdPresentInEligibleOffer() {
		
		return o -> StringUtils.isNotEmpty(o.getOfferId());
	}
	
	/**
	 * 
	 * @return predicate for offers where offer is not a cinema offer 
	 */
	public static Predicate<OfferCatalog> isNotCinemaOffer() {
		
		return o-> !Checks.checkCinemaOffer(o.getRules());
	}
	
	/**
	 * 
	 * @return predicate for EligibleOffers where offer is not a cinema offer 
	 */
	public static Predicate<EligibleOffers> isNotCinemaOfferEligibleOffer() {
		
		return o-> !Checks.checkCinemaOffer(o.getRules());
	}
	
	/**
	 * 
	 * @param accountOfferCounterList
	 * @param membershipCode
	 * @param string 
	 * @return Predicates for offer Id that do not not have account counter present 
	 */
	public static Predicate<String> accountCounterNotPresent(List<AccountOfferCounts> accountOfferCounterList,
			String accountNumber, String membershipCode) {
		
		return s-> ObjectUtils.isEmpty(FilterValues.findAccountOfferCounterInOfferAccountCounterList(accountOfferCounterList, Predicates.isCounterWithAccountNumberAndOfferId(accountNumber, membershipCode, s)));
	}
	
	/**
	 * 
	 * @param offerCounterList
	 * @param denomination
	 * @param couponQuantity
	 * @param resultResponse
	 * @param memberDetails
	 * @return predicate for offers where download limit is not crossed
	 */
	public static Predicate<OfferCatalog> downloadLimitLeft(Integer denomination,
			Integer couponQuantity, EligibilityInfo eligibilityInfo, PurchaseCount purchaseCount, ResultResponse resultResponse) {

		return o -> Checks.checkDownloadLimitEligibility(o, denomination, couponQuantity, purchaseCount, eligibilityInfo, resultResponse); 
		
	}
	
	/**
	 * 
	 * @return Predicate for conversion rate for input value in amount range
	 */
	public static Predicate<ConversionRate> forAmountValueInRange(Double value) {
		
		return c-> Checks.checkDoubleBetweenInclusive(c.getHighValue(), c.getLowValue(), value);
	}
	
	/**
	 * 
	 * @return Predicate for conversion rate for input value in point range
	 */
	public static Predicate<ConversionRate> forPointValueInRange(Double value) {
		
		return c-> Checks.checkDoubleBetweenInclusive(c.getPointEnd(), c.getPointStart(), value);
	}

	/**
	 * 
	 * @param offerType
	 * @return Predicate for EligibleOffer with same offer type as input
	 */
	public static Predicate<EligibleOffers> sameOfferTypeInEligibleOffer(String offerType) {
		
		List<String> offerTypeList = ProcessValues.getOfferTypeIdFromOfferTypeItem(offerType);
		
		return e->!ObjectUtils.isEmpty(e) 
			   && !ObjectUtils.isEmpty(e.getOfferType())
			   && Utilities.presentInList(offerTypeList, e.getOfferType().getOfferTypeId());
	}
	
	/**
	 * 
	 * @param paymentMethodId
	 * @return predicate for PaymentMethod that have same paymentMethodId as input
	 */
	public static Predicate<PaymentMethod> samePaymentMethodId(String paymentMethodId) {

		return s -> StringUtils.equals(s.getPaymentMethodId(), paymentMethodId);
	}

	public static Predicate<PaymentMethod> samePaymentMethodIdInList(List<PaymentMethod> offerPaymentList) {
		
		return p-> offerPaymentList.stream().map(PaymentMethod::getPaymentMethodId).collect(Collectors.toList())
				.contains(p.getPaymentMethodId());
	}
	
	/**
	 * 
	 * @param customerTypeList
	 * @param paymentMethods
	 * @return Predicate for Customer Type so that there some eligible payment methods for customer
	 */
	public static Predicate<String> eligiblePaymentMethodsAvailable(List<CustomerTypeEntity> customerTypeList,
			List<PaymentMethod> paymentMethods) {
		
		return c-> !CollectionUtils.isEmpty(ProcessValues.getCommonPaymentMethods(c, customerTypeList, paymentMethods));
	}
	
}

