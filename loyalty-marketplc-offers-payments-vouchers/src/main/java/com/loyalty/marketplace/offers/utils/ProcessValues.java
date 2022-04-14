package com.loyalty.marketplace.offers.utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.ObjectUtils;

import com.loyalty.marketplace.equivalentpoints.outbound.database.entity.ConversionRate;
import com.loyalty.marketplace.gifting.helper.dto.GoldCertificateDto;
import com.loyalty.marketplace.gifting.outbound.database.entity.OfferGiftValues;
import com.loyalty.marketplace.image.outbound.database.entity.MarketplaceImage;
import com.loyalty.marketplace.mapping.utility.ObjectMapperUtils;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OfferErrorCodes;
import com.loyalty.marketplace.offers.constants.OfferSuccessCodes;
import com.loyalty.marketplace.offers.constants.OffersConfigurationConstants;
import com.loyalty.marketplace.offers.constants.OffersListConstants;
import com.loyalty.marketplace.offers.decision.manager.outbound.dto.PromotionalGiftResult;
import com.loyalty.marketplace.offers.domain.model.GiftInfoDomain;
import com.loyalty.marketplace.offers.error.handler.ErrorRecords;
import com.loyalty.marketplace.offers.helper.dto.AmountInfo;
import com.loyalty.marketplace.offers.helper.dto.BirthdayDurationInfoDto;
import com.loyalty.marketplace.offers.helper.dto.DenominationLimitCounter;
import com.loyalty.marketplace.offers.helper.dto.EligibilityInfo;
import com.loyalty.marketplace.offers.helper.dto.EligibleOfferHelperDto;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.helper.dto.LifetimeSavingsHelperDto;
import com.loyalty.marketplace.offers.helper.dto.LimitCounter;
import com.loyalty.marketplace.offers.helper.dto.LimitCounterWithDenominationList;
import com.loyalty.marketplace.offers.helper.dto.OfferReferences;
import com.loyalty.marketplace.offers.helper.dto.OfferStoreDistanceDto;
import com.loyalty.marketplace.offers.helper.dto.PromotionalGiftHelper;
import com.loyalty.marketplace.offers.helper.dto.PurchaseCount;
import com.loyalty.marketplace.offers.helper.dto.PurchaseExtraAttributes;
import com.loyalty.marketplace.offers.helper.dto.TimeLimits;
import com.loyalty.marketplace.offers.inbound.dto.EligibleOffersFiltersRequest;
import com.loyalty.marketplace.offers.inbound.dto.OfferCatalogDto;
import com.loyalty.marketplace.offers.inbound.dto.OfferCountDto;
import com.loyalty.marketplace.offers.inbound.dto.PromotionalGiftRequestDto;
import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
import com.loyalty.marketplace.offers.inbound.dto.TransactionRequestDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.AccountInfoDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.CatSubCat;
import com.loyalty.marketplace.offers.member.management.outbound.dto.CobrandedCardDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.CustomerTypeEntity;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponseDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.OfferBenefit;
import com.loyalty.marketplace.offers.member.management.outbound.dto.ParentChlidCustomer;
import com.loyalty.marketplace.offers.member.management.outbound.dto.PaymentMethods;
import com.loyalty.marketplace.offers.outbound.database.entity.AccountOfferCount;
import com.loyalty.marketplace.offers.outbound.database.entity.AccountOfferCounts;
import com.loyalty.marketplace.offers.outbound.database.entity.AdditionalDetails;
import com.loyalty.marketplace.offers.outbound.database.entity.BirthdayInfo;
import com.loyalty.marketplace.offers.outbound.database.entity.DenominationCount;
import com.loyalty.marketplace.offers.outbound.database.entity.DenominationLimit;
import com.loyalty.marketplace.offers.outbound.database.entity.EligibleOfferDenomination;
import com.loyalty.marketplace.offers.outbound.database.entity.EligibleOfferRating;
import com.loyalty.marketplace.offers.outbound.database.entity.EligibleOfferStore;
import com.loyalty.marketplace.offers.outbound.database.entity.EligibleOffers;
import com.loyalty.marketplace.offers.outbound.database.entity.GiftInfo;
import com.loyalty.marketplace.offers.outbound.database.entity.MarketplaceImageUrl;
import com.loyalty.marketplace.offers.outbound.database.entity.MemberComment;
import com.loyalty.marketplace.offers.outbound.database.entity.MemberOfferCount;
import com.loyalty.marketplace.offers.outbound.database.entity.MemberOfferCounts;
import com.loyalty.marketplace.offers.outbound.database.entity.MemberRating;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCounter;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCounters;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferLimit;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferRating;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchaseHistory;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchasePaymentMethod;
import com.loyalty.marketplace.offers.outbound.database.entity.SubOffer;
import com.loyalty.marketplace.offers.outbound.database.entity.SubscriptionValues;
import com.loyalty.marketplace.offers.outbound.dto.DenominationDto;
import com.loyalty.marketplace.offers.outbound.dto.Eligibility;
import com.loyalty.marketplace.offers.outbound.dto.ImageUrlDto;
import com.loyalty.marketplace.offers.outbound.dto.LimitResponseDto;
import com.loyalty.marketplace.offers.outbound.dto.MemberCommentDto;
import com.loyalty.marketplace.offers.outbound.dto.MemberRatingDto;
import com.loyalty.marketplace.offers.outbound.dto.OfferCatalogResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.OfferCatalogResultResponseDto;
import com.loyalty.marketplace.offers.outbound.dto.PaymentMethodDto;
import com.loyalty.marketplace.offers.outbound.dto.PromotionalGiftResponseDto;
import com.loyalty.marketplace.offers.outbound.dto.PromotionalGiftResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.PurchaseResponseDto;
import com.loyalty.marketplace.offers.outbound.dto.PurchaseResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.RuleFailure;
import com.loyalty.marketplace.offers.outbound.dto.StoreDto;
import com.loyalty.marketplace.offers.outbound.dto.SubOfferDto;
import com.loyalty.marketplace.offers.outbound.service.dto.IncludeMemberDetails;
import com.loyalty.marketplace.offers.stores.outbound.database.entity.Store;
import com.loyalty.marketplace.outbound.database.entity.Denomination;
import com.loyalty.marketplace.outbound.database.entity.PaymentMethod;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.payment.inbound.dto.PromoCodeApply;
import com.loyalty.marketplace.payment.outbound.dto.PaymentResponse;
import com.loyalty.marketplace.subscription.outbound.dto.BenefitsResponseDto;
import com.loyalty.marketplace.subscription.outbound.dto.OfferCategoryResponseDto;
import com.loyalty.marketplace.subscription.outbound.dto.TitleResponseDto;
import com.loyalty.marketplace.utils.Logs;
import com.mongodb.client.result.UpdateResult;


/**
 * 
 * @author jaya.shukla
 *
 */
public class ProcessValues {
	
	private static final Logger LOG = LoggerFactory.getLogger(ProcessValues.class);
	
	ProcessValues(){
		
	}
	
	/**
	 * 
	 * @param getMemberResponse
	 * @param accountNumber
	 * @return combined account and member info mapped from response received from member management
	 */
	public static GetMemberResponse getMemberInfo(GetMemberResponseDto getMemberResponse, String accountNumber) {
		
		GetMemberResponse getMemberDetails = null;
		
		if(null!=getMemberResponse && null!=getMemberResponse.getAccountsInfo()
				&& !getMemberResponse.getAccountsInfo().isEmpty()) {
			
			AccountInfoDto account = getMemberResponse.getAccountsInfo().stream()
			.filter(a->a.getAccountNumber().equals(accountNumber))
			.findAny().orElse(null);
			
			if(null!=account && null!=getMemberResponse.getMemberInfo()) {
				
				getMemberDetails = new GetMemberResponse();
				getMemberDetails.setCustomerType(account.getCustomerType());
				getMemberDetails.setTotalAccountPoints(account.getTotalPoints());
				getMemberDetails.setEligibleFeatures(account.getEligibleFeatures());
				getMemberDetails.setEligiblePaymentMethod(account.getEligiblePaymentMethod());
				getMemberDetails.setAccountId(account.getAccountId());
				getMemberDetails.setChannelId(account.getChannelId());
				getMemberDetails.setFirstName(account.getFirstName());
				getMemberDetails.setLastName(account.getLastName());
				getMemberDetails.setAccountStatus(account.getAccountStatus());
				getMemberDetails.setAgeEligibleFlag(account.isAgeEligibleFlag());
				getMemberDetails.setDob(account.getDob());
				getMemberDetails.setGender(account.getGender());
				getMemberDetails.setAccountNumber(accountNumber);
				getMemberDetails.setSubscribed(StringUtils.equalsIgnoreCase(OfferConstants.TRUE.get(), account.getSubscribtionStatus()));
				getMemberDetails.setNationality(account.getNationality());
				getMemberDetails.setNumberType(account.getNumberType());
				getMemberDetails.setPrimaryAccount(account.isPrimary());
				getMemberDetails.setFirstAccess(account.isFirstAccessFlag());
				getMemberDetails.setEmailVerificationStatus(!ObjectUtils.isEmpty(account.getEmailVerification()) ? Utilities.getStringValueOrNull(account.getEmailVerification().getVerificationStatus()) : null);
				getMemberDetails.setCobrandedCardDetails(account.getCobrandedCardDetails());
				getMemberDetails.setTotalTierPoints(getMemberResponse.getMemberInfo().getTotalPoints());
				getMemberDetails.setTop3Account(Utilities.presentInList(getMemberResponse.getMemberInfo().getTop3Account(), accountNumber));
				getMemberDetails.setMembershipCode(getMemberResponse.getMemberInfo().getMembershipCode());
				getMemberDetails.setTierLevelName(getMemberResponse.getMemberInfo().getTierLevel());
				getMemberDetails.setReferralAccountNumber(!ObjectUtils.isEmpty(account.getReferralBonusAccount())
						 ?account.getReferralBonusAccount().getAccountNumber()
						 : null);
				getMemberDetails.setReferralBonusCode(account.getReferralCode());
				getMemberDetails.setLanguage(account.getLanguage());
				getMemberDetails.setUiLanguage(account.getUilanguage());
				getMemberDetails.setEmail(account.getEmail());
				getMemberDetails.setLastLoginDate(account.getLoginDate());
				getMemberDetails.setPartyId(account.getPartyId());
				setBenefitsForAccount(getMemberDetails, account);
				LOG.info("benefits : {}", getMemberDetails.getBenefits());
			}
				
		}
				
		return getMemberDetails;
	}
	
	/**
	 * Set benefit values from account in getMemberDetails
	 * @param getMemberDetails
	 * @param account
	 */
	private static void setBenefitsForAccount(GetMemberResponse getMemberDetails, AccountInfoDto account) {
		
		if(!CollectionUtils.isEmpty(account.getBenefits())) {
			
			getMemberDetails.setBenefits(new ArrayList<>(account.getBenefits().size()));
			OfferBenefit offerBenefit = null;
			
			for(BenefitsResponseDto benefit : account.getBenefits()) {
				
				offerBenefit = new OfferBenefit();
				
				if(!ObjectUtils.isEmpty(benefit.getOfferType())) {
					offerBenefit.setOfferType(benefit.getOfferType().getId());
				}
				
				setCategoryBenefits(offerBenefit, benefit);
				getMemberDetails.getBenefits().add(offerBenefit);
			}
		}
		
	}

	/**
	 * Set category benefits in offerBeneft
	 * @param offerBenefit
	 * @param benefit
	 */
	private static void setCategoryBenefits(OfferBenefit offerBenefit, BenefitsResponseDto benefit) {
		
		if(!CollectionUtils.isEmpty(benefit.getOfferCategory())) {
			
			offerBenefit.setCatSubCat(new ArrayList<>(benefit.getOfferCategory().size()));
			CatSubCat catSubCat = null;
			for(OfferCategoryResponseDto offerCategory : benefit.getOfferCategory()) {
				
				catSubCat = new CatSubCat();
				
				if(!ObjectUtils.isEmpty(offerCategory.getCategory())) {
					
					catSubCat.setCategory(offerCategory.getCategory().getId());
				}
				
				if(!CollectionUtils.isEmpty(offerCategory.getSubCategory())) {
					catSubCat.setSubCategory(offerCategory.getSubCategory().stream()
							.map(TitleResponseDto::getId).collect(Collectors.toList()));
				}
				
				offerBenefit.getCatSubCat().add(catSubCat);
			}
			
		}
		
	}
    
	/**
     * 
     * @return flags set for information required from member management for purchase
     * 
     */
    public static IncludeMemberDetails getIncludeMemberDetailsForPayment() {
		
		IncludeMemberDetails includeMemberDetails = new IncludeMemberDetails();
		includeMemberDetails.setIncludeEligibilityMatrix(true);
		includeMemberDetails.setIncludePaymentMethods(true);
		includeMemberDetails.setIncludePointsBankInfo(false);
		includeMemberDetails.setIncludeMemberActivityInfo(false);
		includeMemberDetails.setIncludeSubscriptionInfo(true);
		includeMemberDetails.setIncludeCustomerInterestInfo(false);
		includeMemberDetails.setIncludeReferralBonusAccount(true);
		includeMemberDetails.setIncludeLinkedAccount(true);
		return includeMemberDetails;
	}
    
    /**
     * 
     * @param conversionRateList
     * @param value
     * @return amount converted to equivalent points
     */
    public static Integer getEquivalentPoints(List<ConversionRate> conversionRateList, Double value) {
		
		Integer equivalentPoints = null;
		List<ConversionRate> configuredConversionRates = FilterValues.filterConversionRateList(conversionRateList, Predicates.forAmountValueInRange(value)); 
		ConversionRate applicableRate = FilterValues.findAnyConversionRateWithinRateList(configuredConversionRates, Predicates.getApplicableCoversionRateForPartnerAndChannel());					
		
		if(!ObjectUtils.isEmpty(applicableRate) && !ObjectUtils.isEmpty(applicableRate.getValuePerPoint())) {
			
			equivalentPoints = (int) (Math.ceil((value/applicableRate.getValuePerPoint()))); 
		}					
							
		return equivalentPoints;	
	}
        
    /**
     * 
     * @param purchaseRequest
     * @param offerCatalog
     * @param subOffer
     * @return aed value for an offer
     */
    public static Double getAmountValue(PurchaseRequestDto purchaseRequest, OfferCatalog offerCatalog,
			SubOffer subOffer) {
		
		boolean isDealVoucher = Checks.checkIsDealVoucher(purchaseRequest.getSelectedPaymentItem()); 
		boolean isDenomination = Checks.checkIsDenomination(purchaseRequest.getVoucherDenomination());
		
		Double dealVoucherAmount = isDealVoucher && !ObjectUtils.isEmpty(subOffer) 
				&& !ObjectUtils.isEmpty(subOffer.getSubOfferValue()) && !ObjectUtils.isEmpty(subOffer.getSubOfferValue().getNewCost())
				? subOffer.getSubOfferValue().getNewCost()
				: null;
		Double otherVoucherAmount = null;
		
		if(!isDealVoucher) {
			
			otherVoucherAmount = isDenomination 
			? Double.valueOf(purchaseRequest.getVoucherDenomination()) 
			: offerCatalog.getOfferValues().getCost();
			
		}
		
		return isDealVoucher ? dealVoucherAmount : otherVoucherAmount;
	}
    
    /**
     * 
     * @param method
     * @return transaction type for a purchase transaction
     */
    public static String getTransactionType(String method) {
		
		   return Checks.checkPaymentMethodFullPoints(method)
				   ? OfferConstants.REDEMPTION.get()
				   : OfferConstants.PURCHASE.get();
		   
	}
    
    /**
     * 
     * @param offerId 
     * @param offerLimitList
     * @param denomination
     * @param isCustomerSegment
     * @param customerSegmentNames
     * @return offer limit applicable for the offer
     */
    public static LimitCounter getOfferLimitForCurrentOffer(String offerId, List<OfferLimit> offerLimitList,
			Integer denomination, boolean isCustomerSegment, List<String> customerSegmentNames) {
		
		LimitCounter limit = null;
		if(CollectionUtils.isNotEmpty(offerLimitList)) {
		
			limit = new LimitCounter();
			boolean isDenomination = !ObjectUtils.isEmpty(denomination);
			if(!isCustomerSegment) {
				
				OfferLimit offerLimit = FilterValues.findAnyLimitWithinLimitList(offerLimitList, Predicates.noCustomerSegmentInLimits());
				
				if(!ObjectUtils.isEmpty(offerLimit)) {
					
					limit.setOfferId(offerId);	
					if(!ObjectUtils.isEmpty(offerLimit.getCouponQuantity())) {
						
						limit.setCouponQuantity(offerLimit.getCouponQuantity());
					}
					
					setOfferLimitWithoutCustomerSegment(limit, offerLimit);
					DenominationLimit denominationLimit = FilterValues.findAnyDenominationLimitInDenominationLimitList(offerLimit.getDenominationLimit(), Predicates.sameDenominationForOfferLimit(denomination));
					setOfferDenominationLimitWithoutCustomerSegment(isDenomination, limit, denominationLimit);
					
					setAccountOfferLimitWithoutCustomerSegment(limit, offerLimit);
					denominationLimit = FilterValues.findAnyDenominationLimitInDenominationLimitList(offerLimit.getAccountDenominationLimit(), Predicates.sameDenominationForOfferLimit(denomination));
					setAccountOfferDenominationLimitWithoutCustomerSegment(isDenomination, limit, denominationLimit);
					
					setMemberOfferLimitWithoutCustomerSegment(limit, offerLimit);
					denominationLimit = FilterValues.findAnyDenominationLimitInDenominationLimitList(offerLimit.getMemberDenominationLimit(), Predicates.sameDenominationForOfferLimit(denomination));
					setMemberOfferDenominationLimitWithoutCustomerSegment(isDenomination, limit, denominationLimit);
					
				}
				
			} else {
				
				if(CollectionUtils.isNotEmpty(customerSegmentNames)) {
					
					List<OfferLimit> offerLimits = FilterValues.filterOfferLimits(offerLimitList, Predicates.customerSegmentInListForLimit(customerSegmentNames));
					
					limit.setCouponQuantity(getCouponQuanityForCustomerSegment(offerLimits));
					limit.setOfferDaily(getOfferLimitsForCustomerSegment(offerLimits,customerSegmentNames, OfferConstants.OFFER_COUNTER.get(), OfferConstants.DAILY_LIMIT.get()));
					limit.setOfferWeekly(getOfferLimitsForCustomerSegment(offerLimits, customerSegmentNames, OfferConstants.OFFER_COUNTER.get(), OfferConstants.WEEKLY_LIMIT.get()));
					limit.setOfferMonthly(getOfferLimitsForCustomerSegment(offerLimits, customerSegmentNames, OfferConstants.OFFER_COUNTER.get(), OfferConstants.MONTHLY_LIMIT.get()));
					limit.setOfferAnnual(getOfferLimitsForCustomerSegment(offerLimits, customerSegmentNames, OfferConstants.OFFER_COUNTER.get(), OfferConstants.ANNUAL_LIMIT.get()));
					limit.setOfferTotal(getOfferLimitsForCustomerSegment(offerLimits, customerSegmentNames, OfferConstants.OFFER_COUNTER.get(), OfferConstants.TOTAL_LIMIT.get()));
					
					setOfferDenominationLimitWithCustomerSegment(isDenomination, limit, offerLimits, denomination);
					
					limit.setAccountOfferDaily(getOfferLimitsForCustomerSegment(offerLimits, customerSegmentNames, OfferConstants.ACCOUNT_OFFER_COUNTER.get(), OfferConstants.DAILY_LIMIT.get()));
					limit.setAccountOfferWeekly(getOfferLimitsForCustomerSegment(offerLimits, customerSegmentNames, OfferConstants.ACCOUNT_OFFER_COUNTER.get(), OfferConstants.WEEKLY_LIMIT.get()));
					limit.setAccountOfferMonthly(getOfferLimitsForCustomerSegment(offerLimits, customerSegmentNames, OfferConstants.ACCOUNT_OFFER_COUNTER.get(), OfferConstants.MONTHLY_LIMIT.get()));
					limit.setAccountOfferAnnual(getOfferLimitsForCustomerSegment(offerLimits, customerSegmentNames, OfferConstants.ACCOUNT_OFFER_COUNTER.get(), OfferConstants.ANNUAL_LIMIT.get()));
					limit.setAccountOfferTotal(getOfferLimitsForCustomerSegment(offerLimits, customerSegmentNames, OfferConstants.ACCOUNT_OFFER_COUNTER.get(), OfferConstants.TOTAL_LIMIT.get()));
					
					setAccountOfferDenominationLimitWithCustomerSegment(isDenomination, limit, offerLimits, denomination);
					
					limit.setMemberOfferDaily(getOfferLimitsForCustomerSegment(offerLimits, customerSegmentNames, OfferConstants.MEMBER_OFFER_COUNTER.get(), OfferConstants.DAILY_LIMIT.get()));
					limit.setMemberOfferWeekly(getOfferLimitsForCustomerSegment(offerLimits, customerSegmentNames, OfferConstants.MEMBER_OFFER_COUNTER.get(), OfferConstants.WEEKLY_LIMIT.get()));
					limit.setMemberOfferMonthly(getOfferLimitsForCustomerSegment(offerLimits, customerSegmentNames, OfferConstants.MEMBER_OFFER_COUNTER.get(), OfferConstants.MONTHLY_LIMIT.get()));
					limit.setMemberOfferAnnual(getOfferLimitsForCustomerSegment(offerLimits, customerSegmentNames, OfferConstants.MEMBER_OFFER_COUNTER.get(), OfferConstants.ANNUAL_LIMIT.get()));
					limit.setMemberOfferTotal(getOfferLimitsForCustomerSegment(offerLimits, customerSegmentNames, OfferConstants.MEMBER_OFFER_COUNTER.get(), OfferConstants.TOTAL_LIMIT.get()));
					
					setMemberOfferDenominationLimitWithCustomerSegment(isDenomination, limit, offerLimits, denomination);
					
				}
				
			}

		}
		
		return limit;
	}
    
    /**
     * 
     * @param offerLimits
     * @return coupon quantity applicable for the customer segment
     */
    public static Integer getCouponQuanityForCustomerSegment(List<OfferLimit> offerLimits) {
		
		List<Integer> quantityList = CollectionUtils.isNotEmpty(offerLimits)
			 ? offerLimits.stream().filter(o->!ObjectUtils.isEmpty(o.getCouponQuantity()))
			   .map(OfferLimit::getCouponQuantity).collect(Collectors.toList())		 
			 : null;
			   
		return CollectionUtils.isNotEmpty(quantityList)
			 ? Utilities.getMaxValueInIntegerList(quantityList)
			 : null;		 
			   
	}
    
    /**
     * 
     * @param offerLimits
     * @param customerSegmentNames
     * @param level
     * @param unit
     * @return offer limit applicable for customer segment
     */
    private static Integer getOfferLimitsForCustomerSegment(List<OfferLimit> offerLimits,
			List<String> customerSegmentNames, String level, String unit) {
	    
		Integer limit = null;
		
		if(StringUtils.equalsIgnoreCase(unit, OfferConstants.DAILY_LIMIT.get())) {
			
			limit = MapValues.mapDailyLimits(offerLimits, customerSegmentNames, level);
			
		} else if(StringUtils.equalsIgnoreCase(unit, OfferConstants.WEEKLY_LIMIT.get())) {
			
			limit = MapValues.mapWeeklyLimits(offerLimits, customerSegmentNames, level);
			
		} else if(StringUtils.equalsIgnoreCase(unit, OfferConstants.MONTHLY_LIMIT.get())) {
			
			limit = MapValues.mapMonthlyLimits(offerLimits, customerSegmentNames, level);
			
		} else if(StringUtils.equalsIgnoreCase(unit, OfferConstants.ANNUAL_LIMIT.get())) {
			
			limit = MapValues.mapAnnualLimits(offerLimits, customerSegmentNames, level);
			
		} else if(StringUtils.equalsIgnoreCase(unit, OfferConstants.TOTAL_LIMIT.get())) {
			
			limit = MapValues.mapTotalLimits(offerLimits, customerSegmentNames, level);
			
		}   
		
		return limit;
	}
	
	/**
	 * Sets default offer limit
	 * @param limit
	 * @param offerLimit
	 */
	private static void setOfferLimitWithoutCustomerSegment(LimitCounter limit, OfferLimit offerLimit) {
		
		if(!ObjectUtils.isEmpty(limit) && !ObjectUtils.isEmpty(offerLimit)) {
			
			if(!ObjectUtils.isEmpty(offerLimit.getDailyLimit())) {
				
				limit.setOfferDaily(offerLimit.getDailyLimit());
			}
			
			if(!ObjectUtils.isEmpty(offerLimit.getWeeklyLimit())) {
				
				limit.setOfferWeekly(offerLimit.getWeeklyLimit());
			}

			if(!ObjectUtils.isEmpty(offerLimit.getMonthlyLimit())) {
				
				limit.setOfferMonthly(offerLimit.getMonthlyLimit());
			}
			
			if(!ObjectUtils.isEmpty(offerLimit.getAnnualLimit())) {
				
				limit.setOfferAnnual(offerLimit.getAnnualLimit());
			}
			
			if(!ObjectUtils.isEmpty(offerLimit.getDownloadLimit())) {
				
				limit.setOfferTotal(offerLimit.getDownloadLimit());
			}
			
		}
		
	}
		
	/**
	 * Sets default account offer limit
	 * @param limit
	 * @param offerLimit
	 */
	private static void setAccountOfferLimitWithoutCustomerSegment(LimitCounter limit, OfferLimit offerLimit) {
		
		if(!ObjectUtils.isEmpty(limit) && !ObjectUtils.isEmpty(offerLimit)) {
			
			if(!ObjectUtils.isEmpty(offerLimit.getAccountDailyLimit())) {
				
				limit.setAccountOfferDaily(offerLimit.getAccountDailyLimit());
			}
			
			if(!ObjectUtils.isEmpty(offerLimit.getAccountWeeklyLimit())) {
				
				limit.setAccountOfferWeekly(offerLimit.getAccountWeeklyLimit());
			}

			if(!ObjectUtils.isEmpty(offerLimit.getAccountMonthlyLimit())) {
				
				limit.setAccountOfferMonthly(offerLimit.getAccountMonthlyLimit());
			}
			
			if(!ObjectUtils.isEmpty(offerLimit.getAccountAnnualLimit())) {
				
				limit.setAccountOfferAnnual(offerLimit.getAccountAnnualLimit());
			}
			
			if(!ObjectUtils.isEmpty(offerLimit.getAccountTotalLimit())) {
				
				limit.setAccountOfferTotal(offerLimit.getAccountTotalLimit());
			}

		}
		
	}
	
	/**
	 * Sets default member offer limit
	 * @param limit
	 * @param offerLimit
	 */
	private static void setMemberOfferLimitWithoutCustomerSegment(LimitCounter limit, OfferLimit offerLimit) {
		
		if(!ObjectUtils.isEmpty(limit) && !ObjectUtils.isEmpty(offerLimit)) {
			
			if(!ObjectUtils.isEmpty(offerLimit.getMemberDailyLimit())) {
				
				limit.setMemberOfferDaily(offerLimit.getMemberDailyLimit());
			}
			
			if(!ObjectUtils.isEmpty(offerLimit.getMemberWeeklyLimit())) {
				
				limit.setMemberOfferWeekly(offerLimit.getMemberWeeklyLimit());
			}

			if(!ObjectUtils.isEmpty(offerLimit.getMemberMonthlyLimit())) {
				
				limit.setMemberOfferMonthly(offerLimit.getMemberMonthlyLimit());
			}
			
			if(!ObjectUtils.isEmpty(offerLimit.getMemberAnnualLimit())) {
				
				limit.setMemberOfferAnnual(offerLimit.getMemberAnnualLimit());
			}
			
			if(!ObjectUtils.isEmpty(offerLimit.getMemberTotalLimit())) {
				
				limit.setMemberOfferTotal(offerLimit.getMemberTotalLimit());
			}
			
		}
		
	}

	/**
	 * Sets default offer denomination limit
	 * @param isDenomination
	 * @param limit
	 * @param offerLimit
	 * @param denomination
	 */
	private static void setOfferDenominationLimitWithoutCustomerSegment(boolean isDenomination, LimitCounter limit,
			DenominationLimit denominationLimit) {
		
		if(isDenomination && !ObjectUtils.isEmpty(limit) && !ObjectUtils.isEmpty(denominationLimit)) {
			
			if(!ObjectUtils.isEmpty(denominationLimit.getDailyLimit())) {
				
				limit.setOfferDenominationDaily(denominationLimit.getDailyLimit());
			}
			
			if(!ObjectUtils.isEmpty(denominationLimit.getWeeklyLimit())) {
				
				limit.setOfferDenominationWeekly(denominationLimit.getWeeklyLimit());
			}

			if(!ObjectUtils.isEmpty(denominationLimit.getMonthlyLimit())) {
				
				limit.setOfferDenominationMonthly(denominationLimit.getMonthlyLimit());
			}
			
			if(!ObjectUtils.isEmpty(denominationLimit.getAnnualLimit())) {
				
				limit.setOfferDenominationAnnual(denominationLimit.getAnnualLimit());
			}
			
			if(!ObjectUtils.isEmpty(denominationLimit.getTotalLimit())) {
				
				limit.setOfferDenominationTotal(denominationLimit.getTotalLimit());
			}
				
		}
		
	}
	
	/**
	 * Sets default account denomination limit
	 * @param isDenomination
	 * @param limit
	 * @param offerLimit
	 * @param denomination
	 */
	private static void setAccountOfferDenominationLimitWithoutCustomerSegment(boolean isDenomination, LimitCounter limit,
                  DenominationLimit denominationLimit) {
		
		if(isDenomination && !ObjectUtils.isEmpty(limit) && !ObjectUtils.isEmpty(denominationLimit)) {
			
			if(!ObjectUtils.isEmpty(denominationLimit.getDailyLimit())) {
				
				limit.setAccountDenominationDaily(denominationLimit.getDailyLimit());
			}
			
			if(!ObjectUtils.isEmpty(denominationLimit.getWeeklyLimit())) {
				
				limit.setAccountDenominationWeekly(denominationLimit.getWeeklyLimit());
			}

			if(!ObjectUtils.isEmpty(denominationLimit.getMonthlyLimit())) {
				
				limit.setAccountDenominationMonthly(denominationLimit.getMonthlyLimit());
			}
			
			if(!ObjectUtils.isEmpty(denominationLimit.getAnnualLimit())) {
				
				limit.setAccountDenominationAnnual(denominationLimit.getAnnualLimit());
			}
			
			if(!ObjectUtils.isEmpty(denominationLimit.getTotalLimit())) {
				
				limit.setAccountDenominationTotal(denominationLimit.getTotalLimit());
			}
				
		}
		
	}
	
	/**
	 * Sets default member denomination limit
	 * @param isDenomination
	 * @param limit
	 * @param offerLimit
	 * @param denomination
	 */
	private static void setMemberOfferDenominationLimitWithoutCustomerSegment(boolean isDenomination, LimitCounter limit,
			DenominationLimit denominationLimit) {
		
		if(isDenomination && !ObjectUtils.isEmpty(limit) && !ObjectUtils.isEmpty(denominationLimit)) {
			
			if(!ObjectUtils.isEmpty(denominationLimit.getDailyLimit())) {
				
				limit.setMemberDenominationDaily(denominationLimit.getDailyLimit());
			}
			
			if(!ObjectUtils.isEmpty(denominationLimit.getWeeklyLimit())) {
				
				limit.setMemberDenominationWeekly(denominationLimit.getWeeklyLimit());
			}

			if(!ObjectUtils.isEmpty(denominationLimit.getMonthlyLimit())) {
				
				limit.setMemberDenominationMonthly(denominationLimit.getMonthlyLimit());
			}
			
			if(!ObjectUtils.isEmpty(denominationLimit.getAnnualLimit())) {
				
				limit.setMemberDenominationAnnual(denominationLimit.getAnnualLimit());
			}
			
			if(!ObjectUtils.isEmpty(denominationLimit.getTotalLimit())) {
				
				limit.setMemberDenominationTotal(denominationLimit.getTotalLimit());
			}
				
		}
		
	}
	
	/**
	 * Sets offer denomination limit for customer segment
	 * @param isDenomination
	 * @param limit
	 * @param offerLimits
	 * @param denomination
	 * @param customerSegmentNames
	 */
	private static void setOfferDenominationLimitWithCustomerSegment(boolean isDenomination, LimitCounter limit, List<OfferLimit> offerLimits, Integer denomination) {
		
		if(isDenomination && !ObjectUtils.isEmpty(limit)) {
		   
			List<DenominationLimit> denominationList = filterOfferDenominationLimitFromSegmentOfferLimit(offerLimits, denomination); 
					
			if(CollectionUtils.isNotEmpty(denominationList)) {
				
				limit.setAccountDenominationDaily(MapValues.mapDailyLimitForDenomination(denominationList));
				limit.setAccountDenominationWeekly(MapValues.mapWeeklyLimitForDenomination(denominationList));
				limit.setAccountDenominationMonthly(MapValues.mapMonthlyLimitForDenomination(denominationList));
				limit.setAccountDenominationAnnual(MapValues.mapAnnualLimitForDenomination(denominationList));
				limit.setAccountDenominationTotal(MapValues.mapTotalLimitForDenomination(denominationList));
				
			}		
			
		}
		
	}
	
	/**
	 * Sets account denomination limit for customer segment
	 * @param isDenomination
	 * @param limit
	 * @param offerLimits
	 * @param denomination
	 * @param customerSegmentNames
	 */
	private static void setAccountOfferDenominationLimitWithCustomerSegment(boolean isDenomination, LimitCounter limit, List<OfferLimit> offerLimits, Integer denomination) {
		
		if(isDenomination && !ObjectUtils.isEmpty(limit)) {
		   
			List<DenominationLimit> denominationList = filterAccountOfferDenominationLimitFromSegmentOfferLimit(offerLimits, denomination); 
					
			if(CollectionUtils.isNotEmpty(denominationList)) {
				
				limit.setAccountDenominationDaily(MapValues.mapDailyLimitForDenomination(denominationList));
				limit.setAccountDenominationWeekly(MapValues.mapWeeklyLimitForDenomination(denominationList));
				limit.setAccountDenominationMonthly(MapValues.mapMonthlyLimitForDenomination(denominationList));
				limit.setAccountDenominationAnnual(MapValues.mapAnnualLimitForDenomination(denominationList));
				limit.setAccountDenominationTotal(MapValues.mapTotalLimitForDenomination(denominationList));
				
			}		
			
		}
		
	}
	
	/**
	 * Sets member denomination limit for customer segment
	 * @param isDenomination
	 * @param limit
	 * @param offerLimits
	 * @param denomination
	 * @param customerSegmentNames
	 */
	private static void setMemberOfferDenominationLimitWithCustomerSegment(boolean isDenomination, LimitCounter limit, List<OfferLimit> offerLimits, Integer denomination) {
		
		if(isDenomination && !ObjectUtils.isEmpty(limit)) {
		   
			List<DenominationLimit> denominationList = filterMemberOfferDenominationLimitFromSegmentOfferLimit(offerLimits, denomination); 
					
			if(CollectionUtils.isNotEmpty(denominationList)) {
				
				limit.setMemberDenominationDaily(MapValues.mapDailyLimitForDenomination(denominationList));
				limit.setMemberDenominationWeekly(MapValues.mapWeeklyLimitForDenomination(denominationList));
				limit.setMemberDenominationMonthly(MapValues.mapMonthlyLimitForDenomination(denominationList));
				limit.setMemberDenominationAnnual(MapValues.mapAnnualLimitForDenomination(denominationList));
				limit.setMemberDenominationTotal(MapValues.mapTotalLimitForDenomination(denominationList));
				
			}		
			
		}
		
	}
	
	/**
	 * 
	 * @param offerLimits
	 * @param denomination
	 * @return filtered member denomination limit for customer segment
	 */
	private static List<DenominationLimit> filterMemberOfferDenominationLimitFromSegmentOfferLimit(List<OfferLimit> offerLimits,
			Integer denomination) {
		
		List<DenominationLimit> denominationList = null;
		
		if(CollectionUtils.isNotEmpty(offerLimits)) {
		
			denominationList = new ArrayList<>(offerLimits.size());
			
			for(OfferLimit limit : offerLimits) {
				
				DenominationLimit denominationLimit = CollectionUtils.isNotEmpty(limit.getMemberDenominationLimit())
					 ? FilterValues.findAnyDenominationLimitInDenominationLimitList(limit.getMemberDenominationLimit(), Predicates.sameDenominationForOfferLimit(denomination))	
				     : null;
				
				if(!ObjectUtils.isEmpty(denominationLimit)) {
					
					denominationList.add(denominationLimit);
					
				}
				
			}
			
		}
		
		return denominationList;
	}
    
	/**
	 * 
	 * @param offerLimits
	 * @param denomination
	 * @return filtered account denomination limit for customer segment
	 */
    private static List<DenominationLimit> filterAccountOfferDenominationLimitFromSegmentOfferLimit(List<OfferLimit> offerLimits,
			Integer denomination) {
		
		List<DenominationLimit> denominationList = null;
		
		if(CollectionUtils.isNotEmpty(offerLimits)) {
		
			denominationList = new ArrayList<>(offerLimits.size());
			
			for(OfferLimit limit : offerLimits) {
				
				DenominationLimit denominationLimit = CollectionUtils.isNotEmpty(limit.getAccountDenominationLimit())
					 ? FilterValues.findAnyDenominationLimitInDenominationLimitList(limit.getAccountDenominationLimit(), Predicates.sameDenominationForOfferLimit(denomination))	
				     : null;
				
				if(!ObjectUtils.isEmpty(denominationLimit)) {
					
					denominationList.add(denominationLimit);
					
				}
				
			}
			
		}
		
		return denominationList;
	}
    
    /**
     * 
     * @param offerLimits
     * @param denomination
     * @return filtered offer denomination limit for customer segment
     */
    private static List<DenominationLimit> filterOfferDenominationLimitFromSegmentOfferLimit(List<OfferLimit> offerLimits,
			Integer denomination) {
		
		List<DenominationLimit> denominationList = null;
		
		if(CollectionUtils.isNotEmpty(offerLimits)) {
		
			denominationList = new ArrayList<>(offerLimits.size());
			
			for(OfferLimit limit : offerLimits) {
				
				DenominationLimit denominationLimit = CollectionUtils.isNotEmpty(limit.getDenominationLimit())
					 ? FilterValues.findAnyDenominationLimitInDenominationLimitList(limit.getDenominationLimit(), Predicates.sameDenominationForOfferLimit(denomination))	
				     : null;
				
				if(!ObjectUtils.isEmpty(denominationLimit)) {
					
					denominationList.add(denominationLimit);
					
				}
				
			}
			
		}
		
		return denominationList;
	}
	
    /**
     * 
     * @param spentPoints
     * @param transactionType
     * @return spent points value to be saved to repository
     */
    public static Integer getSpentPointsValue(Integer spentPoints, String transactionType) {
 		
		return !ObjectUtils.isEmpty(spentPoints) && !StringUtils.isEmpty(transactionType) 
			   && StringUtils.equalsIgnoreCase(transactionType, OfferConstants.REDEMPTION.get())	
			 ? spentPoints*(-1)
			 : spentPoints;
	}
    
    /**
     * 
     * @param offerId 
     * @param offerCounter
     * @param denomination
     * @param couponQuantity
     * @param accountNumber
     * @param membershipCode
     * @return counter values for offer
     */
    public static LimitCounter getCurrentOfferCounter(String offerId, OfferCounter offerCounter, Integer denomination, Integer couponQuantity, String accountNumber, String membershipCode) {
		
    	LimitCounter counter = new LimitCounter();
		boolean isDenomination = !ObjectUtils.isEmpty(denomination);
		counter.setCouponQuantity(couponQuantity);
		
		if(!ObjectUtils.isEmpty(offerCounter)) {
			
			counter.setOfferId(offerId);
			DenominationCount denominationCount = isDenomination
					? FilterValues.findAnyDenominationCounterInDenominationCountList(offerCounter.getDenominationCount(), Predicates.sameDenominationForOfferCounter(denomination))
			        : null;
			boolean isDenominationCounter = null!=denominationCount;		
			
			counter.setOfferDaily(Utilities.addTwoIntegers(offerCounter.getDailyCount(), couponQuantity));
			counter.setOfferWeekly(Utilities.addTwoIntegers(offerCounter.getWeeklyCount(), couponQuantity));
			counter.setOfferMonthly(Utilities.addTwoIntegers(offerCounter.getMonthlyCount(), couponQuantity));
			counter.setOfferAnnual(Utilities.addTwoIntegers(offerCounter.getAnnualCount(), couponQuantity));
			counter.setOfferTotal(Utilities.addTwoIntegers(offerCounter.getTotalCount(), couponQuantity));
			counter.setOfferDenominationDaily(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getDailyCount() : null, couponQuantity));
			counter.setOfferDenominationWeekly(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getWeeklyCount() : null, couponQuantity));
			counter.setOfferDenominationMonthly(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getMonthlyCount() : null, couponQuantity));
			counter.setOfferDenominationAnnual(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getAnnualCount() : null, couponQuantity));
			counter.setOfferDenominationTotal(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getTotalCount() : null, couponQuantity));
		    setAccountOfferCount(counter, offerCounter.getAccountOfferCount(), isDenomination, accountNumber, membershipCode, denomination, couponQuantity);
		    setMemberOfferCount(counter, offerCounter.getMemberOfferCount(), isDenomination, membershipCode, denomination, couponQuantity);
				
		} else {
			
			setOfferCounterForNonExistingOfferCounter(counter, isDenomination, couponQuantity);
			
		}
		
		return counter;	
    }
    
    /**
     * Sets account offer count for current account
     * @param counter
     * @param accountOfferCountList
     * @param isDenomination
     * @param accountNumber
     * @param membershipCode
     * @param denomination
     * @param couponQuantity
     */
    private static void setAccountOfferCount(LimitCounter counter, List<AccountOfferCount> accountOfferCountList, boolean isDenomination,
			String accountNumber, String membershipCode, Integer denomination, Integer couponQuantity) {
		
		AccountOfferCount accountOfferCount = FilterValues.findAccountOfferCountInOfferAccountCounterList(accountOfferCountList, Predicates.sameAccountNumberAndMembershipCodeForCounter(accountNumber, membershipCode));
		
		if(!ObjectUtils.isEmpty(accountOfferCount)) {
			
			DenominationCount denominationCount = isDenomination
					? FilterValues.findAnyDenominationCounterInDenominationCountList(accountOfferCount.getDenominationCount(), Predicates.sameDenominationForOfferCounter(denomination))		
			        : null;
					
			boolean isDenominationCounter = null!=denominationCount;		
			
			counter.setAccountOfferDaily(Utilities.addTwoIntegers(accountOfferCount.getDailyCount(), couponQuantity));
			counter.setAccountOfferWeekly(Utilities.addTwoIntegers(accountOfferCount.getWeeklyCount(), couponQuantity));
			counter.setAccountOfferMonthly(Utilities.addTwoIntegers(accountOfferCount.getMonthlyCount(), couponQuantity));
			counter.setAccountOfferAnnual(Utilities.addTwoIntegers(accountOfferCount.getAnnualCount(), couponQuantity));
			counter.setAccountOfferTotal(Utilities.addTwoIntegers(accountOfferCount.getTotalCount(), couponQuantity));
			counter.setAccountDenominationDaily(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getDailyCount() : null, couponQuantity));
			counter.setAccountDenominationWeekly(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getWeeklyCount() : null, couponQuantity));
			counter.setAccountDenominationMonthly(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getMonthlyCount() : null, couponQuantity));
			counter.setAccountDenominationAnnual(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getAnnualCount() : null, couponQuantity));
			counter.setAccountDenominationTotal(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getTotalCount() : null, couponQuantity));
		
		} else {
			
			setAccountOfferCounterForNonExistingAccountCounter(counter, isDenomination, couponQuantity);
			
		} 
			
	}
    
    /**
     * Sets member offer count for current member
     * @param counter
     * @param memberOfferCountList
     * @param isDenomination
     * @param membershipCode
     * @param denomination
     * @param couponQuantity
     */
    private static void setMemberOfferCount(LimitCounter counter, List<MemberOfferCount> memberOfferCountList,
			boolean isDenomination, String membershipCode, Integer denomination, Integer couponQuantity) {
		
    	MemberOfferCount memberOfferCount = FilterValues.findMemberOfferCountInOfferMemberCounterList(memberOfferCountList, Predicates.sameMembershipCodeForCounter(membershipCode));
				
		if(!ObjectUtils.isEmpty(memberOfferCount)) {
			
			DenominationCount denominationCount = isDenomination
					? FilterValues.findAnyDenominationCounterInDenominationCountList(memberOfferCount.getDenominationCount(), Predicates.sameDenominationForOfferCounter(denomination))
			        : null;
					
			boolean isDenominationCounter = null!=denominationCount;		
			
			counter.setMemberOfferDaily(Utilities.addTwoIntegers(memberOfferCount.getDailyCount(), couponQuantity));
			counter.setMemberOfferWeekly(Utilities.addTwoIntegers(memberOfferCount.getWeeklyCount(), couponQuantity));
			counter.setMemberOfferMonthly(Utilities.addTwoIntegers(memberOfferCount.getMonthlyCount(), couponQuantity));
			counter.setMemberOfferAnnual(Utilities.addTwoIntegers(memberOfferCount.getAnnualCount(), couponQuantity));
			counter.setMemberOfferTotal(Utilities.addTwoIntegers(memberOfferCount.getTotalCount(), couponQuantity));
			counter.setMemberDenominationDaily(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getDailyCount() : null, couponQuantity));
			counter.setMemberDenominationWeekly(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getWeeklyCount() : null, couponQuantity));
			counter.setMemberDenominationMonthly(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getMonthlyCount() : null, couponQuantity));
			counter.setMemberDenominationAnnual(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getAnnualCount() : null, couponQuantity));
			counter.setMemberDenominationTotal(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getTotalCount() : null, couponQuantity));
			
		} else {
			
			setMemberOfferCounterForNonExistingMemberCounter(counter, isDenomination, couponQuantity);
		}
		
	}
    
    /**
     *
     * @param isDenomination
     * @param isDenominationCounter
     * @param value
     * @param couponQuantity
     * @return offer denomination value for counter
     */
    private static Integer getCounterValueForDenomination(boolean isDenomination, boolean isDenominationCounter,
			Integer value, Integer couponQuantity) {
		
		Integer counterValue = null;
		
		if(isDenomination && isDenominationCounter) {
			
			counterValue = Utilities.addTwoIntegers(value, couponQuantity);
			
		} else if(isDenomination) {
			
			counterValue = couponQuantity;
		
		} else {
			
			counterValue = OffersConfigurationConstants.ZERO_INTEGER;
			
		}
		
		return counterValue;
	}

    /**
     * Sets denomination value for offer for new counter
     * @param counter
     * @param isDenomination
     * @param couponQuantity
     */
	private static void setOfferCounterForNonExistingOfferCounter(LimitCounter counter, boolean isDenomination, Integer couponQuantity) {
		
		counter.setOfferDaily(couponQuantity);
		counter.setOfferWeekly(couponQuantity);
		counter.setOfferMonthly(couponQuantity);
		counter.setOfferAnnual(couponQuantity);
		counter.setOfferTotal(couponQuantity);
		counter.setOfferDenominationDaily(isDenomination?couponQuantity:0);
		counter.setOfferDenominationWeekly(isDenomination?couponQuantity:0);
		counter.setOfferDenominationMonthly(isDenomination?couponQuantity:0);
		counter.setOfferDenominationAnnual(isDenomination?couponQuantity:0);
		counter.setOfferDenominationTotal(isDenomination?couponQuantity:0);
		setAccountOfferCounterForNonExistingAccountCounter(counter, isDenomination, couponQuantity);
	    setMemberOfferCounterForNonExistingMemberCounter(counter, isDenomination, couponQuantity);
		
	}
	
	/**
	 * Sets denomination value for account for new counter
	 * @param counter
	 * @param isDenomination
	 * @param couponQuantity
	 */
    private static void setAccountOfferCounterForNonExistingAccountCounter(LimitCounter counter, boolean isDenomination, Integer couponQuantity) {
		
		counter.setAccountOfferDaily(couponQuantity);
		counter.setAccountOfferWeekly(couponQuantity);
		counter.setAccountOfferMonthly(couponQuantity);
		counter.setAccountOfferAnnual(couponQuantity);
		counter.setAccountOfferTotal(couponQuantity);
		counter.setAccountDenominationDaily(isDenomination? couponQuantity : 0);
		counter.setAccountDenominationWeekly(isDenomination? couponQuantity : 0);
		counter.setAccountDenominationMonthly(isDenomination? couponQuantity : 0);
		counter.setAccountDenominationAnnual(isDenomination? couponQuantity : 0);
		counter.setAccountDenominationTotal(isDenomination? couponQuantity : 0);
		
	}
    
    /**
     * Sets denomination value for member for new counter
     * @param counter
     * @param isDenomination
     * @param couponQuantity
     */
    private static void setMemberOfferCounterForNonExistingMemberCounter(LimitCounter counter, boolean isDenomination, Integer couponQuantity) {
		
		counter.setMemberOfferDaily(couponQuantity);
		counter.setMemberOfferWeekly(couponQuantity);
		counter.setMemberOfferMonthly(couponQuantity);
		counter.setMemberOfferAnnual(couponQuantity);
		counter.setMemberOfferTotal(couponQuantity);
		counter.setMemberDenominationDaily(isDenomination?couponQuantity:0);
		counter.setMemberDenominationWeekly(isDenomination?couponQuantity:0);
		counter.setMemberDenominationMonthly(isDenomination?couponQuantity:0);
		counter.setMemberDenominationAnnual(isDenomination?couponQuantity:0);
		counter.setMemberDenominationTotal(isDenomination?couponQuantity:0);
		
	}
    
    /**
     * 
     * @param unit
     * @return error codes for counter check
     */
    public static List<OfferErrorCodes> getCounterCheckErrorCodes(String unit) {
		
		List<OfferErrorCodes> errorList = null;
		
		if(unit.equalsIgnoreCase(OfferConstants.OFFER_COUNTER.get())) {
			
			errorList = Arrays.asList(OfferErrorCodes.OFFER_DAILY_LIMIT_EXCEEDED, OfferErrorCodes.OFFER_WEEKLY_LIMIT_EXCEEDED,
					OfferErrorCodes.OFFER_MONTHLY_LIMIT_EXCEEDED, OfferErrorCodes.OFFER_ANNUAL_LIMIT_EXCEEDED, OfferErrorCodes.OFFER_TOTAL_LIMIT_EXCEEDED); 
					
		} else if(unit.equalsIgnoreCase(OfferConstants.ACCOUNT_OFFER_COUNTER.get())) {
			
			errorList = Arrays.asList(OfferErrorCodes.ACCOUNT_OFFER_DAILY_LIMIT_EXCEEDED, OfferErrorCodes.ACCOUNT_OFFER_WEEKLY_LIMIT_EXCEEDED,
					OfferErrorCodes.ACCOUNT_OFFER_MONTHLY_LIMIT_EXCEEDED, OfferErrorCodes.ACCOUNT_OFFER_ANNUAL_LIMIT_EXCEEDED, OfferErrorCodes.ACCOUNT_OFFER_TOTAL_LIMIT_EXCEEDED); 
					
		} else if(unit.equalsIgnoreCase(OfferConstants.MEMBER_OFFER_COUNTER.get())) {
			
			errorList = Arrays.asList(OfferErrorCodes.MEMBER_OFFER_DAILY_LIMIT_EXCEEDED, OfferErrorCodes.MEMBER_OFFER_WEEKLY_LIMIT_EXCEEDED,
					OfferErrorCodes.MEMBER_OFFER_MONTHLY_LIMIT_EXCEEDED, OfferErrorCodes.MEMBER_OFFER_ANNUAL_LIMIT_EXCEEDED, OfferErrorCodes.MEMBER_OFFER_TOTAL_LIMIT_EXCEEDED); 
					
		} else if(unit.equalsIgnoreCase(OfferConstants.OFFER_DENOMINATION.get())) {
			
			errorList = Arrays.asList(OfferErrorCodes.OFFER_DENOMINATION_DAILY_LIMIT_EXCEEDED, OfferErrorCodes.OFFER_DENOMINATION_WEEKLY_LIMIT_EXCEEDED,
					OfferErrorCodes.OFFER_DENOMINATION_MONTHLY_LIMIT_EXCEEDED, OfferErrorCodes.OFFER_DENOMINATION_ANNUAL_LIMIT_EXCEEDED, OfferErrorCodes.OFFER_DENOMINATION_TOTAL_LIMIT_EXCEEDED); 
					
		} else if(unit.equalsIgnoreCase(OfferConstants.ACCOUNT_OFFER_DENOMINATION.get())) {
			
			errorList = Arrays.asList(OfferErrorCodes.ACCOUNT_OFFER_DENOMINATION_DAILY_LIMIT_EXCEEDED, OfferErrorCodes.ACCOUNT_OFFER_DENOMINATION_WEEKLY_LIMIT_EXCEEDED,
					OfferErrorCodes.ACCOUNT_OFFER_DENOMINATION_MONTHLY_LIMIT_EXCEEDED, OfferErrorCodes.ACCOUNT_OFFER_DENOMINATION_ANNUAL_LIMIT_EXCEEDED, OfferErrorCodes.ACCOUNT_OFFER_DENOMINATION_TOTAL_LIMIT_EXCEEDED); 
					
		} else if(unit.equalsIgnoreCase(OfferConstants.MEMBER_OFFER_DENOMINATION.get())) {
			
			errorList = Arrays.asList(OfferErrorCodes.MEMBER_OFFER_DENOMINATION_DAILY_LIMIT_EXCEEDED, OfferErrorCodes.MEMBER_OFFER_DENOMINATION_WEEKLY_LIMIT_EXCEEDED,
					OfferErrorCodes.MEMBER_OFFER_DENOMINATION_MONTHLY_LIMIT_EXCEEDED, OfferErrorCodes.MEMBER_OFFER_DENOMINATION_ANNUAL_LIMIT_EXCEEDED, OfferErrorCodes.MEMBER_OFFER_DENOMINATION_TOTAL_LIMIT_EXCEEDED); 
					
		}
		
		return errorList;
		   
	}
    
    /**
     * 
     * @param paymentMethod
     * @param isLess
     * @return error codes for amount check
     */
    public static OfferErrorCodes getAmountCheckErrorMessage(String paymentMethod, boolean isLess) {
		
		OfferErrorCodes errorCode = null;
		
		if(Checks.checkIsCreditCardPaymentMethod(paymentMethod)) {
			
			errorCode = isLess
					? OfferErrorCodes.LESS_CREDIT_CARD_AMOUNT_PAID
					: OfferErrorCodes.MORE_CREDIT_CARD_AMOUNT_PAID;
			
		} else if(Checks.checkIsFullPointsPaymentMethod(paymentMethod)) {
			
			errorCode = isLess
					? OfferErrorCodes.LESS_SMILES_POINTS_AMOUNT_DISCREPANCY
					: OfferErrorCodes.MORE_SMILES_POINTS_AMOUNT_DISCREPANCY;
			
		} else if(Checks.checkIsPartialPointsPaymentMethod(paymentMethod)) {
			
			errorCode = isLess
					? OfferErrorCodes.LESS_CARD_POINTS_AMOUNT_DISCREPANCY
					: OfferErrorCodes.MORE_CARD_POINTS_AMOUNT_DISCREPANCY;
			
			
		} else if(Checks.checkIsAddToBillPaymentMethod(paymentMethod)) {
			
			errorCode = isLess
					? OfferErrorCodes.LESS_ADD_TO_BILL_AMOUNT_DISCREPANCY
					: OfferErrorCodes.MORE_ADD_TO_BILL_AMOUNT_DISCREPANCY;
			
		} else if(Checks.checkIsDeductFromBalancePaymentMethod(paymentMethod)) {
			
			errorCode = isLess
					? OfferErrorCodes.LESS_DEDUCT_FROM_BALANCE_AMOUNT_DISCREPANCY
					: OfferErrorCodes.MORE_DEDUCT_FROM_BALANCE_AMOUNT_DISCREPANCY;
			
		} else if(Checks.checkIsApplePayPaymentMethod(paymentMethod)) {
			
			errorCode = isLess
					? OfferErrorCodes.LESS_APPLE_PAY_AMOUNT_PAID
					: OfferErrorCodes.MORE_APPLE_PAY_AMOUNT_PAID;
		} else if(Checks.checkIsSamsungPayPaymentMethod(paymentMethod)) {
			
			errorCode = isLess
					? OfferErrorCodes.LESS_SAMSUNG_PAY_AMOUNT_PAID
					: OfferErrorCodes.MORE_SAMSUNG_PAY_AMOUNT_PAID;
		}
		
		return errorCode;
	}

    /**
     * 
     * @param cobrandedCardDetails
     * @return list of cobranded cards
     */
	public static List<String> getCoBrandedCards(List<CobrandedCardDto> cobrandedCardDetails) {
		
		return null!=cobrandedCardDetails && !cobrandedCardDetails.isEmpty()
				? cobrandedCardDetails.stream().map(CobrandedCardDto::getPartnerCode).collect(Collectors.toList())
				: null;
	}
	
	/**
	 * 
	 * @param purchaseHistoryList
	 * @param item
	 * @param conversionRateList 
	 * @param resultResponse 
	 * @return bill payment/ recharge amount in configured duration
	 */
    public static Double getBillPaymentRechargeAmount(List<PurchaseHistory> purchaseHistoryList, String item) {
		
		Double billRechargeAmount = OffersConfigurationConstants.ZERO_DOUBLE;
		
		if(CollectionUtils.isNotEmpty(purchaseHistoryList)) {
		   
		   List<PurchaseHistory> records = FilterValues.filterPurchaseRecords(purchaseHistoryList, Predicates.samePurchaseItemAndSuccessStatus(item));
		   		   
		   if(CollectionUtils.isNotEmpty(records)) {
			   
			   for(PurchaseHistory record : records) {
				   
				   billRechargeAmount = billRechargeAmount + Math.abs(record.getPurchaseAmount());
				   
			   }
			   
		   }
			
		}
		
		return billRechargeAmount;
	}
    
//    /**
//     * 
//     * @param paymentMethod
//     * @param purchaseAmount
//     * @param partnerCode 
//     * @param conversionRateList
//     * @param resultResponse 
//     * @return purchase amount/ converted purchase amount for the transaction
//     */
//    private static double getPurchaseAmount(PurchaseHistory record, List<ConversionRate> conversionRateList, ResultResponse resultResponse) {
//		
//    	Double billRechargeAmount = OffersConfigurationConstants.ZERO_DOUBLE;
//    	
//    	if(!ObjectUtils.isEmpty(record)) {
//    		
//    		billRechargeAmount = !Checks.checkPaymentMethodFullPoints(record.getPaymentMethod())
//    				? Utilities.getDoubleValue(record.getSpentAmount())
//    				: getEquivalentAmount(ProcessValues.filterConversionRateListForPartner(conversionRateList, record.getPartnerCode()), Utilities.getPositiveIntegerValue(record.getSpentPoints()), resultResponse); 
//    		
//    	}
//    	
//		return  billRechargeAmount;
//	}

	/**
     * 
     * @param paymentResponse
     * @return transaction number for current transaction
     */
    public static String getTransactionNumber(PaymentResponse paymentResponse) {
		
		return !ObjectUtils.isEmpty(paymentResponse) && !ObjectUtils.isEmpty(paymentResponse.getTransactionNo())
			? paymentResponse.getTransactionNo()
			: null;
	}

    /**
     * 
     * @param paymentResponse
     * @return external reference number for current transaction
     */
	public static String getExtRefNo(PaymentResponse paymentResponse) {
		
		return !ObjectUtils.isEmpty(paymentResponse) && !ObjectUtils.isEmpty(paymentResponse.getExtRefNo())
				? paymentResponse.getExtRefNo()
				: null;
	}
	
	/**
	 * 
	 * @param purchaseExtraAttributes
	 * @return Subscription segment for the transaction
	 */
	public static String getSubscriptionSegment(PurchaseExtraAttributes purchaseExtraAttributes) {
		
		return !ObjectUtils.isEmpty(purchaseExtraAttributes) && !StringUtils.isEmpty(purchaseExtraAttributes.getSubscriptionSegment())
				? purchaseExtraAttributes.getSubscriptionSegment()
				: null;
	}

	/**
	 * 
	 * @param paymentResponse
	 * @return epgTransactionId for current transaction
	 */
	public static String getEpgTransactionId(PaymentResponse paymentResponse) {
		
		return !ObjectUtils.isEmpty(paymentResponse) && !ObjectUtils.isEmpty(paymentResponse.getEpgTransactionId())
				? paymentResponse.getEpgTransactionId()
				: null;
	}

	/**
	 * 
	 * @param paymentResponse
	 * @return list of voucher codes from paymentResponse
	 */
	public static List<String> getVoucherCodes(PaymentResponse paymentResponse) {
		
		return !ObjectUtils.isEmpty(paymentResponse) && !ObjectUtils.isEmpty(paymentResponse.getVoucherCode())
				? paymentResponse.getVoucherCode()
				: null;
	}

	/**
	 * 
	 * @param paymentResponse
	 * @return status of transaction from paymentResponse
	 */
	public static String getStatus(PaymentResponse paymentResponse) {
		
		return !ObjectUtils.isEmpty(paymentResponse) && !ObjectUtils.isEmpty(paymentResponse.getPaymentStatus())
				? paymentResponse.getPaymentStatus()
				: OfferConstants.FAILED.get();
	}

	/**
	 * 
	 * @param paymentResponse
	 * @return failure reason of transaction from paymentResponse
	 */
	public static String getStatusReason(PaymentResponse paymentResponse) {
		
		String statusReason = null;
		
		if(!ObjectUtils.isEmpty(paymentResponse) && !ObjectUtils.isEmpty(paymentResponse.getPaymentStatus())) {
			
			statusReason = !StringUtils.equalsIgnoreCase(paymentResponse.getPaymentStatus(), OfferConstants.SUCCESS.get())	
					? paymentResponse.getFailedreason()
					: null;
			
		} else {
			
			statusReason = OfferConstants.ERROR_OCCURED.get();
			
		}
		
		return statusReason;
		
	}

	/**
	 * 
	 * @param savedPurchaseHistory
	 * @param headers
	 * @return created user for purchase history
	 */
	public static String getCreatedUser(PurchaseHistory savedPurchaseHistory, Headers headers) {
		
		return !ObjectUtils.isEmpty(savedPurchaseHistory)
			? savedPurchaseHistory.getCreatedUser()
			: headers.getUserName();
	}
	
	/**
	 * 
	 * @param savedPurchaseHistory
	 * @return created date for purchase history
	 */
	public static Date getCreatedDate(PurchaseHistory savedPurchaseHistory) {
		
		return !ObjectUtils.isEmpty(savedPurchaseHistory)
			? savedPurchaseHistory.getCreatedDate()
			: new Date();
	}
	
	/**
     * 
     * @param item
     * @return equivalent product type value for input item
     */
    public static String getProductTypeValue(String item) {
		
		String productTypeValue = null;
		
		if(Checks.checkIsCashVoucher(item)) {
			
			productTypeValue = OfferConstants.CASH_VOUCHER_PRODUCT.get();
			
		} else if(Checks.checkIsDiscountVoucher(item)){
			
			productTypeValue = OfferConstants.DISCOUNT_COUPON_PRODUCT.get();
			
		} else if(Checks.checkIsEtisalatAddon(item)){
			
			productTypeValue = OfferConstants.ETISLAT_ADDON_PRODUCT.get();
			
		} else if(Checks.checkIsDealVoucher(item)){
			
			productTypeValue = OfferConstants.DEAL_VOUCHER_PRODUCT.get();
			
		} else if(Checks.checkIsSubscription(item)) {
			
			productTypeValue = OfferConstants.SUBSCRIPTION_PRODUCT.get();
			
		} else if(Checks.checkIsBillPayment(item)) {
			
			productTypeValue = OfferConstants.BILL_PAYMENT_PRODUCT.get();
			
		} else if(Checks.checkIsRecharge(item)) {
			
			productTypeValue = OfferConstants.RECHARGE_PRODUCT.get();
			
		} else if(Utilities.isEqual(OffersConfigurationConstants.POINT_GIFTING_PRODUCT_ITEM, item)) {
			
			productTypeValue = OffersConfigurationConstants.POINT_GIFTING_PRODUCT_ITEM;
		
		} else if(Checks.checkIsGoldCertificate(item)) {
			
			productTypeValue = OfferConstants.GOLD_CERTIFICATE_PRODUCT.get();
		}
			
		return productTypeValue;
	}
    
    /**
	 * 
	 * @param customerTypeDetails
	 * @param currentTypes
	 * @return list of parent type for the current list
	 */
	public static List<String> getAllParentTypes(List<ParentChlidCustomer> customerTypeDetails,
			List<String> currentTypes) {
		
		List<String> typeList = null;
		
		if(CollectionUtils.isNotEmpty(currentTypes) && CollectionUtils.isNotEmpty(customerTypeDetails)) {
			
			typeList = new ArrayList<>(currentTypes.size());
			
			for(String type : currentTypes) {
				
				typeList.add(type);
			    List<String> parentList = getParentList(type, customerTypeDetails);    
				
			    if(CollectionUtils.isNotEmpty(parentList)) {
			    	
			    	typeList.addAll(parentList);
			    	
			    }
				
			}
		
		}
		
		return typeList;
	}
	
	 /**
     * 
     * @param type
     * @param customerTypeDetails
     * @return parent list for current type
     */
	private static List<String> getParentList(String type, List<ParentChlidCustomer> customerTypeDetails) {
    	
    	List<String> parentList = null;
     	
    	if(!ObjectUtils.isEmpty(type) && CollectionUtils.isNotEmpty(customerTypeDetails)) {
    		
    		ParentChlidCustomer currentType = customerTypeDetails.stream().filter(p->StringUtils.equalsIgnoreCase(type, p.getChild()))
    	            .findAny().orElse(null);
        	
         	if(!ObjectUtils.isEmpty(currentType)) {
        		
         		parentList = new ArrayList<>(1);
         		
         		while(!ObjectUtils.isEmpty(currentType) 
        		   && !ObjectUtils.isEmpty(currentType.getParent())) {
        			
        			parentList.add(currentType.getParent());
        			String typeName = currentType.getParent();
        	   		currentType = customerTypeDetails.stream().filter(p->StringUtils.equalsIgnoreCase(typeName, p.getChild()))
        		            .findAny().orElse(null);
        			
        		}
        		
        	}
    		
    	}
    	
    	return parentList;
    	
    }

    /**
     * 
     * @param customerTypeDetails
     * @param currentTypes
     * @return child types of parent customer type
     */
	public static List<String> getAllChildTypes(List<ParentChlidCustomer> customerTypeDetails,
			List<String> currentTypes) {
		
		List<String> typeList = null;
		
		if(CollectionUtils.isNotEmpty(currentTypes) && CollectionUtils.isNotEmpty(customerTypeDetails)) {
			
			typeList = new ArrayList<>(currentTypes.size());
			
			for(String type : currentTypes) {
				
				typeList.add(type);
			    List<String> childList = getChildList(type, customerTypeDetails);    
				
			    if(CollectionUtils.isNotEmpty(childList)) {
			    	
			    	typeList.addAll(childList);
			    	
			    }
				
			}
		
		}
		
		return typeList;
	}
	
	/**
	 * 
	 * @param type
	 * @param customerTypeDetails
	 * @return list of child types for the input parent customer type
	 */
    private static List<String> getChildList(String type, List<ParentChlidCustomer> customerTypeDetails) {
    	
    	List<String> childList = null;
     	
    	if(!ObjectUtils.isEmpty(type) && CollectionUtils.isNotEmpty(customerTypeDetails)) {
    		
    		ParentChlidCustomer currentType = customerTypeDetails.stream().filter(p->StringUtils.equalsIgnoreCase(type, p.getParent()))
    	            .findAny().orElse(null);
        	
         	if(!ObjectUtils.isEmpty(currentType)) {
        		
        		childList = new ArrayList<>(1);
        		
        		while(!ObjectUtils.isEmpty(currentType) 
        		   && !ObjectUtils.isEmpty(currentType.getChild())) {
        			
        			childList.add(currentType.getChild());
        			String typeName = currentType.getChild();
        	   		currentType = customerTypeDetails.stream().filter(p->StringUtils.equalsIgnoreCase(typeName, p.getParent()))
        		            .findAny().orElse(null);
        			
        		}
        		
        	}
    		
    	}
    	
    	return childList;
    	
    }
    
    /**
     * 
     * @param giftInfo
     * @return entity object for giftInfo
     */
    public static GiftInfo getGiftInfoEntity(GiftInfoDomain giftInfo) {
		
		return !ObjectUtils.isEmpty(giftInfo) 
				? new GiftInfo(Utilities.getStringValueOrNull(giftInfo.getIsGift()), Utilities.getListOrNull(giftInfo.getGiftChannels()), Utilities.getListOrNull(giftInfo.getGiftSubCardTypes())) 
				: null;
	}
    
    /**
     * 
     * @param conversionRateList
     * @param originalvalue
     * @param resultResponse
     * @return equivalent amount for points
     */
    public static Double getEquivalentAmount(List<ConversionRate> conversionRateList, Integer points, ResultResponse resultResponse) {
    	
    	Double value = (double) points;
    	Double equivalentAmount = null;
    	List<ConversionRate> configuredRates = FilterValues.filterConversionRateList(conversionRateList, Predicates.forPointValueInRange(Double.valueOf(points))); 
    	configuredRates = getFilteredRatesForEquivalentAmount(configuredRates);
		
    	Responses.setResponseAfterConditionCheck(CollectionUtils.isNotEmpty(configuredRates), OfferErrorCodes.RATE_NOT_DEFINED_FOR_THIS_RANGE, resultResponse);					
		
		if(Checks.checkNoErrors(resultResponse)) {
			
			equivalentAmount = getEquivalentValueForPoints(configuredRates, value, resultResponse);
			
		}
		return equivalentAmount;				
    	
	}
    
    /**
     * 
     * @param configuredRates
     * @return filtered list of conversion rate for equivalent amount conversion based on partner code and channel id
     */
    public static List<ConversionRate> getFilteredRatesForEquivalentAmount(List<ConversionRate> configuredRates) {
		
    	List<ConversionRate> rateList =  null;
    	
    	if(!CollectionUtils.isEmpty(configuredRates)) {
    		
    		rateList = FilterValues.filterConversionRateList(configuredRates, Predicates.nonSmilesWithChannelId());
					
    		if(CollectionUtils.isEmpty(rateList)) {
    			
    			rateList = FilterValues.filterConversionRateList(configuredRates, Predicates.nonSmilesWithEmptyChannelId());
    		}
    		
    		if(CollectionUtils.isEmpty(rateList)) {
    			
    			rateList = FilterValues.filterConversionRateList(configuredRates, Predicates.smilesWithChannelId());
    		}

			if(CollectionUtils.isEmpty(rateList)) {
				
				rateList = FilterValues.filterConversionRateList(configuredRates, Predicates.smilesWithEmptyChannelId());
			}
    		
    	}
    	
		return rateList;
	}
    
    /**
     * 
     * @param configuredRates
     * @param value
     * @param resultResponse 
     * @return equivalent amount for the input value
     */
    private static Double getEquivalentValueForPoints(List<ConversionRate> configuredRates, Double value, ResultResponse resultResponse) {
		
    	Double equivalentAmount = null;
    	String productItem = null;
		
		Double coefficientA = 0.0;
		double firstAedStart = 444.44;
		double secondAedStart = 900;
		
		List<Double> pointStart = new ArrayList<Double>();
		pointStart.add(55556d);
		pointStart.add(100000d);
		
		List<Double> pointEnd = new ArrayList<Double>();
		pointEnd.add(62499d);
		pointEnd.add(111110d);
		
		List<Double> aedStart = new ArrayList<Double>();
		aedStart.add(firstAedStart);
		aedStart.add(secondAedStart);
		
		List<Double> aedEnd = new ArrayList<Double>();
		aedEnd.add(562.49);
		aedEnd.add(1111.1);
		
		List<Double> coEffecientA = new ArrayList<Double>();
		coEffecientA.add(0.017002737);
		coEffecientA.add(0.0190009);
		
		List<Double> coEffecientB = new ArrayList<Double>();
		coEffecientB.add(-500.1640328);
		coEffecientB.add(-1000.090009);
		
		Double coefficientB = 0.0;
    	
    	if(CollectionUtils.isNotEmpty(configuredRates)) {	
	    	
    		if(configuredRates.size()>1) {				
				/*
				 * Double cofficientA = getCofficientA(configuredRates); Double cofficientB =
				 * getCofficientB(configuredRates);
				 */
				for(int i=0; i<configuredRates.size(); i++) {
					if(value >= pointStart.get(i) && value <= pointEnd.get(i)  ) {
						coefficientB = coEffecientB.get(i);
						coefficientA = coEffecientA.get(i);
						break;
					}
				}
				
				if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(coEffecientA) && !ObjectUtils.isEmpty(coEffecientB),
				  OfferErrorCodes.RATE_NOT_DEFINED_FOR_THIS_RANGE, resultResponse)) {
					
					equivalentAmount = (value*coefficientA) + coefficientB;
				}
				
			} else {				
				equivalentAmount = value*configuredRates.get(0).getValuePerPoint();
				productItem = configuredRates.get(0).getProductItem();		
				
			}
	    	
	    	if(!ObjectUtils.isEmpty(equivalentAmount)) {
				
				equivalentAmount = null != productItem && (productItem.equalsIgnoreCase(OfferConstants.CASH_VOUCHER_PRODUCT.get()) || productItem.equalsIgnoreCase(OfferConstants.DEAL_VOUCHER_PRODUCT.get()))
						? Math.round(equivalentAmount * 100.0)/100.0
						: Math.floor(equivalentAmount*100)/100;
				
			}
    	}
		return equivalentAmount;
	}
    
//    /**
//     * 
//     * @param configuredRates
//     * @return cofficientA for overlapping conversion rate
//     */
//    private static Double getCofficientA(List<ConversionRate> configuredRates) {
//		
//		Double cofficientA = null;
//		
//		if(CollectionUtils.isNotEmpty(configuredRates)) {
//			
//			cofficientA = 0.0;
//			
//			for(ConversionRate rate : configuredRates) {
//				
//				cofficientA = cofficientA + rate.getValuePerPoint();
//				
//			}
//			
//		}
//		
//		return cofficientA;
//	}
//    
//    /**
//     * 
//     * @param configuredRates
//     * @return cofficientB for overlapping conversion rate
//     */
//    private static Double getCofficientB(List<ConversionRate> configuredRates) {
//		
//    	return !CollectionUtils.isEmpty(configuredRates)
//    		 ?	configuredRates.stream().sorted((c1, c2)->c1.getPointStart().compareTo(c2.getPointStart()))
//					 .collect(Collectors.toList()).get(0).getCoefficientB()
//    		 :	OffersConfigurationConstants.ZERO_DOUBLE;
//	}
	
    /**
     * 
     * @param item
     * @param channel
     * @param action
     * @return success code for create/update offer
     */
    public static OfferSuccessCodes getCreateUpdateOfferSuccessCodeValue(String item, String channel, String action) {
		
		OfferSuccessCodes successResult = null;
		
		if(Checks.checkIsCashVoucher(item)) {
			
			successResult = action.equalsIgnoreCase(OfferConstants.INSERT_ACTION.get())
					? OfferSuccessCodes.CASH_VOUCHER_CREATED
					: OfferSuccessCodes.CASH_VOUCHER_UPDATED;
			
		} else if(Checks.checkIsDiscountVoucher(item)){
			
			successResult = action.equalsIgnoreCase(OfferConstants.INSERT_ACTION.get())
					? OfferSuccessCodes.DISCOUNT_OFFER_CREATED
					: OfferSuccessCodes.DISCOUNT_OFFER_UPDATED;
			
		} else if(Checks.checkIsEtisalatAddon(item) ){
			
			successResult = getProvisioningChannelSuccessCodes(channel, action);
			
			
		} else if(Checks.checkIsDealVoucher(item)){
			
			successResult = action.equalsIgnoreCase(OfferConstants.INSERT_ACTION.get())
					? OfferSuccessCodes.DEAL_VOUCHER_CREATED
					: OfferSuccessCodes.DEAL_VOUCHER_UPDATED;
			
		} else if(Checks.checkIsGoldCertificate(item)){
			
			successResult = action.equalsIgnoreCase(OfferConstants.INSERT_ACTION.get())
					? OfferSuccessCodes.GOLD_CERTIFICATE_CREATED
					: OfferSuccessCodes.GOLD_CERTIFICATE_UPDATED;
			
		} 
		
		return successResult;
	} 

    /**
     * 
     * @param channel
     * @param action
     * @return success codes for etisalat add on offers
     */
	private static OfferSuccessCodes getProvisioningChannelSuccessCodes(String channel, String action) {
	
		OfferSuccessCodes successResult = null;
		
		if(Checks.checkIsComsProvisioningChannel(channel)) {
			
			successResult = action.equalsIgnoreCase(OfferConstants.INSERT_ACTION.get())
					? OfferSuccessCodes.ETISALAT_COMS_CREATED
					: OfferSuccessCodes.ETISALAT_COMS_UPDATED;
			
		} else if(Checks.checkIsRtfProvisioningChannel(channel)) {
			
			successResult = action.equalsIgnoreCase(OfferConstants.INSERT_ACTION.get())
					? OfferSuccessCodes.ETISALAT_RTF_CREATED
					: OfferSuccessCodes.ETISALAT_RTF_UPDATED;
			
		} else if(Checks.checkIsEmcaisProvisioningChannel(channel)) {
			
			successResult = action.equalsIgnoreCase(OfferConstants.INSERT_ACTION.get())
					? OfferSuccessCodes.ETISALAT_EMCAIS_CREATED
					: OfferSuccessCodes.ETISALAT_EMCAIS_UPDATED;
			
		} else if(Checks.checkIsPhoneyTunesProvisioningChannel(channel)) {
			
			successResult = action.equalsIgnoreCase(OfferConstants.INSERT_ACTION.get())
					? OfferSuccessCodes.ETISALAT_PHONEY_TUNES_CREATED
					: OfferSuccessCodes.ETISALAT_PHONEY_TUNES_UPDATED;
			
		} else if(Checks.checkIsRbtProvisioningChannel(channel)) {
			
			successResult = action.equalsIgnoreCase(OfferConstants.INSERT_ACTION.get())
					? OfferSuccessCodes.ETISALAT_RBT_CREATED
					: OfferSuccessCodes.ETISALAT_RBT_UPDATED;
			
		}
		
		return successResult;
		
	}

	/**
	 * Sets eligibilityInfo fields for validating offer for listing offers
	 * @param eligibilityInfo
	 * @param headers
	 * @param fetchedOffers
	 * @param isMember
	 */
	public static void setEligibilityInfoForListingOffer(EligibilityInfo eligibilityInfo, Headers headers, List<OfferCatalog> fetchedOffers,
			boolean isMember) {
		
		eligibilityInfo.setHeaders(headers);
		eligibilityInfo.setOfferList(fetchedOffers);
		eligibilityInfo.setMember(isMember);
		  
	}
	
	/**
	 * 
	 * @param item
	 * @return error codes for offer purchase
	 */
	public static OfferErrorCodes getPurchaseErrorCode(String item) {
		
		OfferErrorCodes errorCode = null; 
		
		if(Checks.checkIsDiscountVoucher(item)) {
			
			errorCode = OfferErrorCodes.OFFER_PURCHASE_FAILED;
		
		} else if(Checks.checkIsCashVoucher(item)) {
			
			errorCode = OfferErrorCodes.OFFER_PURCHASE_FAILED;
			
		} else if(Checks.checkIsDealVoucher(item)) {
			
			errorCode = OfferErrorCodes.OFFER_PURCHASE_FAILED;
			
		} else if(Checks.checkIsEtisalatAddon(item)) {
			
			errorCode = OfferErrorCodes.OFFER_PURCHASE_FAILED;
			
		} else if(Checks.checkIsBillPayment(item)) {
			
			errorCode = OfferErrorCodes.BILLING_FAILED;
			
		} else if(Checks.checkIsRecharge(item)) {
			
			errorCode = OfferErrorCodes.RECHARGE_FAILED;
			
		} else if(Checks.checkIsGoldCertificate(item)) {
			
			errorCode = OfferErrorCodes.GOLD_CERTIFICATE_PURCHASE_FAILED;
			
		} 
		
		return errorCode;
	}

	/**
	 * Sets fields in eligibilityInfo object for purchase validations
	 * @param eligibilityInfo
	 * @param purchaseRequest
	 * @param offerDetails
	 * @param purchasePaymentMethod
	 * @param headers
	 */
	public static void setEligibilityInfoForPurchase(EligibilityInfo eligibilityInfo, PurchaseRequestDto purchaseRequest,
			OfferCatalog offerDetails, PurchasePaymentMethod purchasePaymentMethod, Headers headers) {
		
		if(!ObjectUtils.isEmpty(offerDetails)) {
			
			purchaseRequest.setOfferId(offerDetails.getOfferId());
			
		}
		
		Denomination denomination = Checks.checkIsDenomination(purchaseRequest.getVoucherDenomination())
	            && !ObjectUtils.isEmpty(offerDetails)
	            ? FilterValues.findAnyDenominationInDenominationList(offerDetails.getDenominations(), Predicates.sameDirhamValueForDenomination(purchaseRequest.getVoucherDenomination()))
	            : null;		 
		
	    SubOffer subOffer = Checks.checkIsDealVoucher(purchaseRequest.getSelectedPaymentItem())
				? FilterValues.findAnySubOfferInList(offerDetails.getSubOffer(), Predicates.sameSubOfferId(purchaseRequest.getSubOfferId()))
				: null;
		
		eligibilityInfo.setMember(true);
		eligibilityInfo.setOffer(offerDetails);
		eligibilityInfo.setPurchasepaymentMethod(purchasePaymentMethod);
		eligibilityInfo.setHeaders(headers);
		eligibilityInfo.setDenomination(denomination);
		eligibilityInfo.setSubOffer(subOffer);
		eligibilityInfo.setAccountNumber(purchaseRequest.getAccountNumber());
		
		AdditionalDetails additionalDetails = new AdditionalDetails();
		additionalDetails.setDod(Checks.checkFlagSet(eligibilityInfo.getOffer().getIsDod()));
		additionalDetails.setFeatured(Checks.checkFlagSet(eligibilityInfo.getOffer().getIsDod()));
		additionalDetails.setBirthdayOffer(Checks.checkFlagSet(eligibilityInfo.getOffer().getIsBirthdayGift()));
		additionalDetails.setGift(Checks.checkIsGiftValue(offerDetails.getGiftInfo()));
		additionalDetails.setPromotionApplied(!StringUtils.isEmpty(purchaseRequest.getPromoCode()));
		additionalDetails.setSubscribed(false);
		// need to check whether the offer is mamba or not
		additionalDetails.setMamba(Checks.checkIsMamba(offerDetails.getFreeOffers()));
		additionalDetails.setPromotionalGift(Checks.checkIsPromotionalGift(eligibilityInfo.getOffer().getFreeOffers()));
		additionalDetails.setPointsRedemptionGift(Checks.checkIsPointsRedemptionGift(eligibilityInfo.getOffer().getFreeOffers()));
		eligibilityInfo.setAdditionalDetails(additionalDetails);
		eligibilityInfo.getAdditionalDetails().setFree(ProcessValues.getIsFree(eligibilityInfo, purchaseRequest));
		
	}
	
	/**
	 * 
	 * @param eligibilityInfo
	 * @param purchaseRequest
	 * @return status to indicate if offer is free or not
	 */
    public static boolean getIsFree(EligibilityInfo eligibilityInfo, PurchaseRequestDto purchaseRequest) {
		
		return !Utilities.presentInList(OffersListConstants.ELIGIBLE_TELECOM_SPEND_ITEMS, purchaseRequest.getSelectedPaymentItem())
			&& (eligibilityInfo.getAdditionalDetails().isSubscriptionBenefit()
			|| eligibilityInfo.getAdditionalDetails().isGift()
			|| eligibilityInfo.getAdditionalDetails().isMamba()
			|| eligibilityInfo.getAdditionalDetails().isPromotionalGift()
			|| eligibilityInfo.getAdditionalDetails().isPointsRedemptionGift()	
			|| (!ObjectUtils.isEmpty(eligibilityInfo.getAmountInfo())  
			&& eligibilityInfo.getAmountInfo().getOfferCost().equals(OffersConfigurationConstants.ZERO_DOUBLE)
			&& eligibilityInfo.getAmountInfo().getOfferPoints().equals(OffersConfigurationConstants.ZERO_INTEGER))
			|| Checks.checkIsVideoOnDemandOffer(eligibilityInfo.getOffer().getOfferType().getOfferTypeId(), eligibilityInfo.getOffer().getProvisioningChannel()));
    }
	
    /**
     * 
     * @param item
     * @return success codes for offer purchase
     */
    public static OfferSuccessCodes getPurchaseSuccessCode(String item) {
		
		OfferSuccessCodes successCode = null; 
		
		if(Checks.checkIsDiscountVoucher(item)) {
			
			successCode = OfferSuccessCodes.OFFER_PURCHASED_SUCCESSFULLY;
		
		} else if(Checks.checkIsCashVoucher(item)) {
			
			successCode = OfferSuccessCodes.OFFER_PURCHASED_SUCCESSFULLY;
			
		} else if(Checks.checkIsDealVoucher(item)) {
			
			successCode = OfferSuccessCodes.OFFER_PURCHASED_SUCCESSFULLY;
			
		} else if(Checks.checkIsEtisalatAddon(item)) {
			
			successCode = OfferSuccessCodes.OFFER_PURCHASED_SUCCESSFULLY;
			
		} else if(Checks.checkIsBillPayment(item)) {
			
			successCode = OfferSuccessCodes.BILL_PAYMENT_COMPLETED_SUCCESSFULLY;
			
		} else if(Checks.checkIsRecharge(item)) {
			
			successCode = OfferSuccessCodes.RECHARGE_COMPLETED_SUCCESSFULLY;
			
		} else if(Checks.checkIsGoldCertificate(item)) {
			
			successCode = OfferSuccessCodes.GOLD_CERTIFICATE_PURCHASED_SUCCESSFULLY;
			
		} 
		
		return successCode;
	}
    
    /**
     * Sets correct transaction type for transaction request
     * @param transactionRequest
     */
    public static void setTransactionTypeInTransactionRequest(TransactionRequestDto transactionRequest) {
		
		if(!ObjectUtils.isEmpty(transactionRequest) 
		&& (StringUtils.equalsIgnoreCase(transactionRequest.getTransactionType(), OfferConstants.ALL.get())
		|| StringUtils.equalsIgnoreCase(transactionRequest.getTransactionType(), OfferConstants.REDEMPTION.get()))) {
			
			transactionRequest.setTransactionType(null);
		}
		
	}
    
   /*
    * Sets correct offer type for transaction request
    * @param transactionRequest
    */
   public static void setOfferTypeInTransactionRequest(TransactionRequestDto transactionRequest) {
		
		if(!ObjectUtils.isEmpty(transactionRequest) 
		&& (StringUtils.equalsIgnoreCase(transactionRequest.getOfferType(), OfferConstants.ALL.get()))) {
			
			transactionRequest.setOfferType(null);
		}
		
	}
    
    /**
     * 
     * @param offerTypeId
     * @return equivalent purchase item for offer type
     */
    public static String getPurchaseItemFromOfferType(String offerTypeId) {
	
        String purchaseItem = null;
        
        if(Checks.checkIsDiscountVoucher(offerTypeId)) {
        	
        	purchaseItem = OffersConfigurationConstants.discountVoucherItem;
        	
        } else if(Checks.checkIsCashVoucher(offerTypeId)) {
        	
        	purchaseItem = OffersConfigurationConstants.cashVoucherItem;
        	
        } else if(Checks.checkIsDealVoucher(offerTypeId)) {
        	
        	purchaseItem = OffersConfigurationConstants.dealVoucherItem;
        	
        } else if(Checks.checkIsEtisalatAddon(offerTypeId)) {
        	
        	purchaseItem = OffersConfigurationConstants.addOnItem;
        	
        } else if(Checks.checkIsGoldCertificate(offerTypeId)) {
        	
        	purchaseItem = OffersConfigurationConstants.goldCertificateItem;
        	
        } else if(Checks.checkIsTelcomType(offerTypeId)) {
        	
        	purchaseItem = OffersConfigurationConstants.billPaymentItem;
        	
        } else if(Checks.checkIsOtherOfferType(offerTypeId)) {
        	
        	purchaseItem = OfferConstants.OTHER_PURCHASE_ITEM.get();
        	
        } else if(Checks.checkIsWelcomeGiftOfferType(offerTypeId)) {
        	
        	purchaseItem = OfferConstants.WELCOME_GIFT_ITEM.get();
        	
        }  else if(Checks.checkIsLifestyleOfferType(offerTypeId)) {
        	
        	purchaseItem = OfferConstants.LIFESTYLE_OFFER_ITEM.get();
        }  
        
        return purchaseItem;
        
	}
    
    /**
     * 
     * @param purchasePaymentMethod
     * @param list 
     * @return payment methods for a purchase item and offer
     */
    public static List<PaymentMethod> getPaymentMethodsForPurchases(PurchasePaymentMethod purchasePaymentMethod, List<PaymentMethod> offerPaymentList, boolean includeOtherPaymentMethods) {
		
		List<PaymentMethod> paymentMethodList = !ObjectUtils.isEmpty(purchasePaymentMethod) 
			&& CollectionUtils.isNotEmpty(purchasePaymentMethod.getPaymentMethods())
			  ? purchasePaymentMethod.getPaymentMethods()
			  : null;
		LOG.info("paymentMethodList : {}", !CollectionUtils.isEmpty(paymentMethodList)
				? paymentMethodList.stream().map(PaymentMethod::getDescription).collect(Collectors.toList())
				: null);
		LOG.info("offerPaymentList : {}", offerPaymentList);
		
		if(!includeOtherPaymentMethods) {
			
			paymentMethodList = offerPaymentList;
		}
		
		return !CollectionUtils.isEmpty(paymentMethodList)
			&& !CollectionUtils.isEmpty(offerPaymentList)
			  ? paymentMethodList.stream().filter(Predicates.samePaymentMethodIdInList(offerPaymentList)).collect(Collectors.toList())
			  : paymentMethodList ;
	}
    
    /**
     * Sets unmapped values in offer response
     * @param offerCatalogDto
     * @param offerCatalog
     * @param paymentMethods
     * @param conversionRateList
     * @param imageList
     */
    public static void setUnmappedOfferValues(OfferCatalogResultResponseDto offerCatalogDto, OfferCatalog offerCatalog,
			List<PaymentMethod> paymentMethods, List<ConversionRate> conversionRateList, List<MarketplaceImage> imageList) {
		
		if(!ObjectUtils.isEmpty(offerCatalog) && !ObjectUtils.isEmpty(offerCatalogDto)) {
			
			setOfferCommonValues(offerCatalogDto, offerCatalog, conversionRateList);
			setDealVoucherValues(offerCatalogDto, offerCatalog, conversionRateList);
			setEtisalatAddOnValues(offerCatalogDto, offerCatalog);
			setOfferReferenceValues(offerCatalogDto, offerCatalog, paymentMethods, conversionRateList, imageList);
			setDiscountVoucherValues(offerCatalog, offerCatalogDto);
			
		}
		
	}

    /**
     * Setting common values in offer response for all offer types 
     * @param offerCatalogDto
     * @param offerCatalog
     * @param conversionRateList
     */
	private static void setOfferCommonValues(OfferCatalogResultResponseDto offerCatalogDto, OfferCatalog offerCatalog, List<ConversionRate> conversionRateList) {
		
		if(!ObjectUtils.isEmpty(offerCatalog.getOfferValues())) {
			
			offerCatalogDto.setCost(offerCatalog.getOfferValues().getCost());
        	offerCatalogDto.setPointsValue(!ObjectUtils.isEmpty(offerCatalog.getOfferValues().getPointsValue())
        			? offerCatalog.getOfferValues().getPointsValue()
        			: ProcessValues.getEquivalentPoints(conversionRateList, offerCatalog.getOfferValues().getCost()));
            
        }
        
        if(CollectionUtils.isNotEmpty(offerCatalog.getLimit())
        && CollectionUtils.isNotEmpty(offerCatalogDto.getOfferLimit())) {
        	
        	for(LimitResponseDto limitDto : offerCatalogDto.getOfferLimit()) {
        		
        		limitDto.setCouponQuantity(Utilities.getIntegerValueOrNull(ProcessValues.getCouponQuantity(limitDto, offerCatalog.getLimit())));		
        		
        	}
        	
        }
        
        if(!ObjectUtils.isEmpty(offerCatalog.getOfferRating())) {
			   
			offerCatalogDto.setOfferRating(offerCatalog.getOfferRating().getId());
		}
        
        if(ObjectUtils.isEmpty(offerCatalog.getStaticRating())
        || (!ObjectUtils.isEmpty(offerCatalogDto.getStaticRating())
        && offerCatalogDto.getStaticRating()==0)) {
        	
        	offerCatalogDto.setStaticRating(null);
        }
        
        if(!ObjectUtils.isEmpty(offerCatalog.getEarnMultiplier())) {
			   
			offerCatalogDto.setEarnMultiplier(offerCatalog.getEarnMultiplier());
		}
        
        if(!ObjectUtils.isEmpty(offerCatalog.getAccrualDetails())
        && !ObjectUtils.isEmpty(offerCatalog.getAccrualDetails().getPointsEarnMultiplier())) {
			   
			offerCatalogDto.setPointsEarnMultiplier(offerCatalog.getAccrualDetails().getPointsEarnMultiplier());
		}
        
        if(!ObjectUtils.isEmpty(offerCatalog.getGiftInfo())) {
        	
        	offerCatalogDto.setIsGift(offerCatalog.getGiftInfo().getIsGift());
        	offerCatalogDto.setGiftChannels(offerCatalog.getGiftInfo().getGiftChannels());
        	offerCatalogDto.setGiftSubCardTypes(offerCatalog.getGiftInfo().getGiftSubCardTypes());
        }
        
        if(!ObjectUtils.isEmpty(offerCatalog.getFreeOffers())) {
        	offerCatalogDto.setMamba(offerCatalog.getFreeOffers().isMamba());
        	offerCatalogDto.setPromotionalGift(offerCatalog.getFreeOffers().isPromotionalGift());
        	offerCatalogDto.setPointsRedemptionGift(offerCatalog.getFreeOffers().isPointsRedemptionGift());
        }
        
        offerCatalogDto.setEligibility(new Eligibility(true, null));
        setVoucherInfoDetails(offerCatalog, offerCatalogDto);
        setActivityCodeDetails(offerCatalog, offerCatalogDto);
        
        if(!ObjectUtils.isEmpty(offerCatalog.getRestaurant())) {
        	offerCatalogDto.getRestaurant().setRestaurantNameEng(offerCatalog.getRestaurant().getRestaurantNameEn());
        }
    
	}
	
	/**
	 * Sets voucher info details in offer response
	 * @param offerCatalog
	 * @param offerCatalogDto
	 */
	private static void setVoucherInfoDetails(OfferCatalog offerCatalog,
			OfferCatalogResultResponseDto offerCatalogDto) {
		
		if(!ObjectUtils.isEmpty(offerCatalog.getVoucherInfo())) {
        	
        	offerCatalogDto.setVoucherExpiryDate(offerCatalog.getVoucherInfo().getVoucherExpiryDate());
        	offerCatalogDto.setPartnerRedeemURL(offerCatalog.getVoucherInfo().getPartnerRedeemURL());
        	
        	if(!ObjectUtils.isEmpty(offerCatalog.getVoucherInfo().getInstructionsToRedeemTitle())) {
        		
        		offerCatalogDto.setInstructionsToRedeemTitleEn(offerCatalog.getVoucherInfo().getInstructionsToRedeemTitle().getInstructionsToRedeemTitleEn());
        		offerCatalogDto.setInstructionsToRedeemTitleAr(offerCatalog.getVoucherInfo().getInstructionsToRedeemTitle().getInstructionsToRedeemTitleAr());
        	}
        	
        	if(!ObjectUtils.isEmpty(offerCatalog.getVoucherInfo().getInstructionsToRedeem())) {
        		
        		offerCatalogDto.setInstructionsToRedeemEn(offerCatalog.getVoucherInfo().getInstructionsToRedeem().getInstructionsToRedeemEn());
        		offerCatalogDto.setInstructionsToRedeemAr(offerCatalog.getVoucherInfo().getInstructionsToRedeem().getInstructionsToRedeemAr());
        	}
        }
		
	}

	/**
	 * Setting activity code details in offer response
	 * @param offerCatalog
	 * @param offerCatalogDto
	 */
	private static void setActivityCodeDetails(OfferCatalog offerCatalog,
			OfferCatalogResultResponseDto offerCatalogDto) {
		
		if(!ObjectUtils.isEmpty(offerCatalog.getActivityCode())) {
        	
        	if(!ObjectUtils.isEmpty(offerCatalog.getActivityCode().getRedemptionActivityCode())) {
        		
        		offerCatalogDto.setRedActivityCd(offerCatalog.getActivityCode().getRedemptionActivityCode().getActivityCode());
        	    offerCatalogDto.setRedActivityId(offerCatalog.getActivityCode().getRedemptionActivityCode().getActivityId());
        		
                if(!ObjectUtils.isEmpty(offerCatalog.getActivityCode().getRedemptionActivityCode().getActivityCodeDescription())) {
                	
                	offerCatalogDto.setRedActivityCodeDescriptionEn(offerCatalog.getActivityCode().getRedemptionActivityCode().getActivityCodeDescription().getActivityCodeDescriptionEn());
            		offerCatalogDto.setRedActivityCodeDescriptionAr(offerCatalog.getActivityCode().getRedemptionActivityCode().getActivityCodeDescription().getActivityCodeDescriptionAr());
                	
                }  
        	    
        	}
        	
        	if(!ObjectUtils.isEmpty(offerCatalog.getActivityCode().getAccrualActivityCode())) {
        	
        		offerCatalogDto.setAccActivityCd(offerCatalog.getActivityCode().getAccrualActivityCode().getActivityCode());
        		offerCatalogDto.setAccActivityId(offerCatalog.getActivityCode().getAccrualActivityCode().getActivityId());
        		
                if(!ObjectUtils.isEmpty(offerCatalog.getActivityCode().getAccrualActivityCode().getActivityCodeDescription())) {
                	
                	offerCatalogDto.setAccActivityCodeDescriptionEn(offerCatalog.getActivityCode().getAccrualActivityCode().getActivityCodeDescription().getActivityCodeDescriptionEn());
            		offerCatalogDto.setAccActivityCodeDescriptionAr(offerCatalog.getActivityCode().getAccrualActivityCode().getActivityCodeDescription().getActivityCodeDescriptionAr());   
                	
                }
        		
        		
        	}
        	
        	if(!ObjectUtils.isEmpty(offerCatalog.getActivityCode().getPointsAccrualActivityCode())) {
            	
        		offerCatalogDto.setPointsAcrActivityCode(offerCatalog.getActivityCode().getPointsAccrualActivityCode().getActivityCode());
        		offerCatalogDto.setPointsAcrId(offerCatalog.getActivityCode().getPointsAccrualActivityCode().getActivityId());
        		
                if(!ObjectUtils.isEmpty(offerCatalog.getActivityCode().getPointsAccrualActivityCode().getActivityCodeDescription())) {
                	
                	offerCatalogDto.setPointsAcrCodeDescriptionEn(offerCatalog.getActivityCode().getPointsAccrualActivityCode().getActivityCodeDescription().getActivityCodeDescriptionEn());
            		offerCatalogDto.setPointsAcrCodeDescriptionAr(offerCatalog.getActivityCode().getPointsAccrualActivityCode().getActivityCodeDescription().getActivityCodeDescriptionAr());   
                	
                }
        		
        	}
        	
        }
		
	}

	/**
	 * Setting unmapped values for deal voucher in offer response
	 * @param offerCatalogDto
	 * @param offerCatalog
	 * @param conversionRateList
	 */
	private static void setDealVoucherValues(OfferCatalogResultResponseDto offerCatalogDto, OfferCatalog offerCatalog, List<ConversionRate> conversionRateList) {
		
		if(Checks.checkIsDealVoucher(offerCatalog.getOfferType().getOfferTypeId())
		&& CollectionUtils.isNotEmpty(offerCatalog.getSubOffer())) {
				
				List<SubOfferDto> subOfferDtoList = new ArrayList<>(offerCatalog.getSubOffer().size());
 				
				for(SubOffer subOffer : offerCatalog.getSubOffer()) {
					
 					SubOfferDto subOfferDto = new SubOfferDto();
 					setSubOfferDto(subOffer, subOfferDto, conversionRateList);
 					subOfferDtoList.add(subOfferDto);
 					
				}
 				
 				offerCatalogDto.setSubOffer(subOfferDtoList);
				
		}
			
	}
	
	private static void setDiscountVoucherValues(OfferCatalog offerCatalog,
			OfferCatalogResultResponseDto offerCatalogDto) {
		
		if(!ObjectUtils.isEmpty(offerCatalog) && !ObjectUtils.isEmpty(offerCatalog.getSubscriptionDetails())) {
			offerCatalogDto.setSubscPromo(offerCatalog.getSubscriptionDetails().getSubscPromo());
			offerCatalogDto.setSubscriptionCatalogId(offerCatalog.getSubscriptionDetails().getSubscriptionCatalogId());
		}
	}

	/**
	 * Setting subOffer values for deal voucher in offer response
	 * @param subOffer
	 * @param subOfferDto
	 * @param conversionRateList
	 */
	private static void setSubOfferDto(SubOffer subOffer, SubOfferDto subOfferDto, List<ConversionRate> conversionRateList) {
		
		   subOfferDto.setSubOfferId(subOffer.getSubOfferId());
			
			if(!ObjectUtils.isEmpty(subOffer.getSubOfferTitle())) {
				subOfferDto.setSubOfferTitleEn(subOffer.getSubOfferTitle().getSubOfferTitleEn());
				subOfferDto.setSubOfferTitleAr(subOffer.getSubOfferTitle().getSubOfferTitleAr());
			}
			
			if(!ObjectUtils.isEmpty(subOffer.getSubOfferDesc())) {
				subOfferDto.setSubOfferDescEn(subOffer.getSubOfferDesc().getSubOfferDescEn());
				subOfferDto.setSubOfferDescAr(subOffer.getSubOfferDesc().getSubOfferDescAr());
			}
			
			if(!ObjectUtils.isEmpty(subOffer.getSubOfferValue())) {
				
				subOfferDto.setOldCost( subOffer.getSubOfferValue().getOldCost());
				subOfferDto.setNewCost(subOffer.getSubOfferValue().getNewCost());
				subOfferDto.setOldPointValue( !ObjectUtils.isEmpty(subOffer.getSubOfferValue().getOldPointValue())
						? subOffer.getSubOfferValue().getOldPointValue()
						: ProcessValues.getEquivalentPoints(conversionRateList, subOffer.getSubOfferValue().getOldCost()));
				subOfferDto.setNewPointValue(!ObjectUtils.isEmpty(subOffer.getSubOfferValue().getNewPointValue())
						? subOffer.getSubOfferValue().getNewPointValue()
						: ProcessValues.getEquivalentPoints(conversionRateList, subOffer.getSubOfferValue().getNewCost()));
			}
			
	}
	
	/**
	 * Setting unmapped values for etisalat add on in offer response
	 * @param offerCatalogDto
	 * @param offerCatalog
	 */
	private static void setEtisalatAddOnValues(OfferCatalogResultResponseDto offerCatalogDto,
			OfferCatalog offerCatalog) {
		
		if(!ObjectUtils.isEmpty(offerCatalog.getProvisioningAttributes())){
			
            offerCatalogDto.setRatePlanCode(offerCatalog.getProvisioningAttributes().getRatePlanCode());
            offerCatalogDto.setRtfProductCode(offerCatalog.getProvisioningAttributes().getRtfProductCode());
            offerCatalogDto.setRtfProductType(offerCatalog.getProvisioningAttributes().getRtfProductType());
            offerCatalogDto.setVasCode(offerCatalog.getProvisioningAttributes().getVasCode());
            offerCatalogDto.setVasActionId(offerCatalog.getProvisioningAttributes().getVasActionId());
            offerCatalogDto.setPromotionalPeriod(offerCatalog.getProvisioningAttributes().getPromotionalPeriod());
            offerCatalogDto.setFeature(offerCatalog.getProvisioningAttributes().getFeature());
            offerCatalogDto.setServiceId(offerCatalog.getProvisioningAttributes().getServiceId());
            offerCatalogDto.setActivityIdRbt(offerCatalog.getProvisioningAttributes().getActivityId());
            offerCatalogDto.setPackName(offerCatalog.getProvisioningAttributes().getPackName());
            
        }
		
	}
	
	/**
	 * Setting all dbRef values in offer response
	 * @param offerCatalogDto
	 * @param offerCatalog
	 * @param paymentMethods
	 * @param conversionRateList
	 * @param imageList
	 */
	private static void setOfferReferenceValues(OfferCatalogResultResponseDto offerCatalogDto,
			OfferCatalog offerCatalog, List<PaymentMethod> paymentMethods, List<ConversionRate> conversionRateList, List<MarketplaceImage> imageList) {
		
		setPaymentMethods(offerCatalogDto, paymentMethods);
		setStoreDetails(offerCatalogDto, offerCatalog);
		setCategoryDetails(offerCatalogDto, offerCatalog);
		setImageUrls(offerCatalogDto, imageList);
		setRatings(offerCatalog, offerCatalogDto);
		setDenominationDetails(offerCatalogDto, offerCatalog, conversionRateList);
	}

	/**
	 * Set payment methods in offer response
	 * @param offerCatalogDto
	 * @param offerPaymentMethods
	 */
	private static void setPaymentMethods(OfferCatalogResultResponseDto offerCatalogDto,
			List<PaymentMethod> offerPaymentMethods) {
		
		if(CollectionUtils.isNotEmpty(offerPaymentMethods)) {
			
			List<PaymentMethodDto> paymentMethods = null;
            
            if(CollectionUtils.isNotEmpty(offerPaymentMethods)) {
                
                paymentMethods = new ArrayList<>(offerPaymentMethods.size());
                
                for(PaymentMethod paymentMethod : offerPaymentMethods) {
                    
                    PaymentMethodDto paymentMethodDto = new PaymentMethodDto();
                    paymentMethodDto.setPaymentMethodId(paymentMethod.getPaymentMethodId());
                    paymentMethodDto.setDescription(paymentMethod.getDescription());
                    paymentMethods.add(paymentMethodDto);
                    
                }
                
                offerCatalogDto.setPaymentMethods(paymentMethods);
                
            }
			
		}
		
	}
	
	/**
	 * Set store details in offer response
	 * @param offerCatalogDto
	 * @param offerCatalog
	 */
	private static void setStoreDetails(OfferCatalogResultResponseDto offerCatalogDto, OfferCatalog offerCatalog) {
		
		if(CollectionUtils.isNotEmpty(offerCatalogDto.getOfferStores())) {
			
			for(StoreDto storeDto : offerCatalogDto.getOfferStores()){
				
	            Store store = FilterValues
	            		.findAnyStoreInList(offerCatalog.getOfferStores(), Predicates.sameStoreCode(storeDto.getStoreCode()));
	            
	            if(!ObjectUtils.isEmpty(store)) {
	                  
	            	List<String> mobileNumbers = null;
	                
	                if(CollectionUtils.isNotEmpty(store.getContactPersons())) {
	                	
	                	mobileNumbers = MapValues.mapAllStoreContactNumbers(store, Predicates.nonEmptyMobileNumber());
	                	storeDto.setMobileNumber(mobileNumbers);
	                	
	                }
	                
	                List<String> storeCoordinates = null;
	                if(!StringUtils.isEmpty(store.getLatitude()) 
	                || !StringUtils.isEmpty(store.getLongitude())) {
	                	
	                	storeCoordinates = new ArrayList<>(2);
	                	storeCoordinates.add(store.getLatitude());
	                    storeCoordinates.add(store.getLongitude());
	                    storeDto.setStoreCoordinates(storeCoordinates);
	                    
	                }
	            	
	            }
	            
	        }
			
		}
		
	}
	
	/**
	 * Set denomination details in offer response
	 * @param offerCatalogDto
	 * @param offerCatalog
	 * @param conversionRateList
	 */
	private static void setDenominationDetails(OfferCatalogResultResponseDto offerCatalogDto, OfferCatalog offerCatalog,
			List<ConversionRate> conversionRateList) {
		
		if(CollectionUtils.isNotEmpty(offerCatalog.getDenominations()) 
		&& CollectionUtils.isNotEmpty(offerCatalogDto.getDenominations())) {
			
	       for(DenominationDto denomination : offerCatalogDto.getDenominations()) {
	    	   
	    	   if(ObjectUtils.isEmpty(denomination.getPointValue())) {
	    		   
	    		   denomination.setPointValue(getEquivalentPoints(conversionRateList, (double) denomination.getDirhamValue()));
	    		   
	    	   }
	    	   
	       }		
			
		}
		
	}
	
	/**
	 * Set category details in offer response
	 * @param offerCatalogDto
	 * @param offerCatalog
	 */
    private static void setCategoryDetails(OfferCatalogResultResponseDto offerCatalogDto, OfferCatalog offerCatalog) {
		
		if(!ObjectUtils.isEmpty(offerCatalog.getCategory())) {
	        
        	offerCatalogDto.setCategoryId(offerCatalog.getCategory().getCategoryId());
        	
        }

        if(!ObjectUtils.isEmpty(offerCatalog.getSubCategory())) {
        	
        	offerCatalogDto.setSubCategoryId(offerCatalog.getSubCategory().getCategoryId());
        	
        }
		
	}
	
    /**
     * Set offer rating details in offer details
     * @param offerCatalog
     * @param offerCatalogDto
     */
	private static void setRatings(OfferCatalog offerCatalog, OfferCatalogResultResponseDto offerCatalogDto) {
		
		OfferRating offerRating = offerCatalog.getOfferRating();
		offerCatalogDto.setAverageRating(OffersConfigurationConstants.ZERO_DOUBLE);
		
		if(!ObjectUtils.isEmpty(offerRating)) {
			
			offerCatalogDto.setAverageRating(offerRating.getAverageRating());
			List<MemberRatingDto> memberRatingList = null;
			
			if(CollectionUtils.isNotEmpty(offerRating.getMemberRatings())) {
			
				memberRatingList = new ArrayList<>(offerRating.getMemberRatings().size());
				
				for(MemberRating rating : offerRating.getMemberRatings()) {
					
					MemberRatingDto memberRating = new MemberRatingDto();
					memberRating.setAccountNumber(rating.getAccountNumber());
					memberRating.setMembershipCode(rating.getMembershipCode());
					memberRating.setFirstName(rating.getFirstName());
					memberRating.setLastName(rating.getLastName());
					List<MemberCommentDto> memberCommentList = null;
				
					if(CollectionUtils.isNotEmpty(rating.getComments())) {
						
						memberCommentList = new ArrayList<>(rating.getComments().size());
												
						for(MemberComment comment : rating.getComments()) {
							
							MemberCommentDto memberComment = new MemberCommentDto();
							memberComment.setRating(comment.getRating());
							memberComment.setComment(comment.getComment());
							memberComment.setReviewDate(comment.getReviewDate());
							memberCommentList.add(memberComment);	
							
						}
						
					}
					
					memberRating.setComments(memberCommentList);
					memberRatingList.add(memberRating);
					
				}
				
				offerCatalogDto.setMemberRatings(memberRatingList);
			}
			
		}
		
	}
	
	/**
	 * Set image urls in offer response
	 * @param offerCatalogDto
	 * @param imageList
	 */
	private static void setImageUrls(OfferCatalogResultResponseDto offerCatalogDto,
			List<MarketplaceImage> imageList) {
		
		if(!CollectionUtils.isEmpty(imageList)) {
			
			List<ImageUrlDto> imageUrlList = new ArrayList<>(imageList.size());
			for(MarketplaceImage image : imageList) {
				
	           	ImageUrlDto imageUrlDto = new ImageUrlDto();
	           	imageUrlDto.setImageUrl(image.getImageUrl());
	        	imageUrlDto.setImageUrlDr(image.getImageUrlDr());
	           	imageUrlDto.setImageUrlProd(image.getImageUrlProd());
	           	
	           	if(!ObjectUtils.isEmpty(image.getMerchantOfferImage())){
	           		
	           		imageUrlDto.setAvailableInChannel(image.getMerchantOfferImage().getAvailableInChannel());
		           	imageUrlDto.setImageType(image.getMerchantOfferImage().getImageType());
	           		
	           	}
	           		
	           	imageUrlList.add(imageUrlDto);
	           	
				
			}
			offerCatalogDto.setImageUrlList(imageUrlList);
			
		}
		
	}

	/**
	 * Set average rating in offer response
	 * @param offerCatalogDto
	 * @param offerCatalog
	 */
	public static void setAverageRating(OfferCatalogResultResponseDto offerCatalogDto, OfferCatalog offerCatalog) {
		
		if(ObjectUtils.isEmpty(offerCatalog.getStaticRating()) 
		&& ObjectUtils.isEmpty(offerCatalog.getOfferRating())) {
			
			offerCatalogDto.setAverageRating(OffersConfigurationConstants.ZERO_DOUBLE);
			
		} else {
		
			Double staticRating = !ObjectUtils.isEmpty(offerCatalog.getStaticRating())
					?(double) offerCatalog.getStaticRating() 
					:OffersConfigurationConstants.ZERO_DOUBLE;
					
			Double avgRating = !ObjectUtils.isEmpty(offerCatalog.getOfferRating()) 
					&& !ObjectUtils.isEmpty(offerCatalog.getOfferRating().getAverageRating()) 
					? offerCatalog.getOfferRating().getAverageRating()
					:OffersConfigurationConstants.ZERO_DOUBLE;		
			
			offerCatalogDto.setAverageRating(
					!ObjectUtils.isEmpty(offerCatalog.getStaticRating()) 
					&& offerCatalog.getStaticRating().equals(OffersConfigurationConstants.ZERO_INTEGER)  
					? staticRating
					: avgRating);
		}
		
	}
	
	/**
	 * Set average rating in offer response
	 * @param offerCatalogDto
	 * @param offerCatalog
	 */
	public static void setAverageRatingForEligibleOffer(OfferCatalogResultResponseDto offerCatalogDto, EligibleOffers offerCatalog) {
		
		if(ObjectUtils.isEmpty(offerCatalog.getStaticRating()) 
		&& ObjectUtils.isEmpty(offerCatalog.getOfferRating())) {
			
			offerCatalogDto.setAverageRating(OffersConfigurationConstants.ZERO_DOUBLE);
			
		} else {
		
			Double staticRating = !ObjectUtils.isEmpty(offerCatalog.getStaticRating())
					?(double) offerCatalog.getStaticRating() 
					:OffersConfigurationConstants.ZERO_DOUBLE;
					
			Double avgRating = !ObjectUtils.isEmpty(offerCatalog.getOfferRating()) 
					&& !ObjectUtils.isEmpty(offerCatalog.getOfferRating().getAverageRating()) 
					? offerCatalog.getOfferRating().getAverageRating()
					:OffersConfigurationConstants.ZERO_DOUBLE;		
			
			offerCatalogDto.setAverageRating(
					!ObjectUtils.isEmpty(offerCatalog.getStaticRating()) 
					&& offerCatalog.getStaticRating().equals(OffersConfigurationConstants.ZERO_INTEGER)  
					? staticRating
					: avgRating);
		}
		
	}
	
	/**
	 * 
	 * @param limitDto
	 * @param offerLimitList
	 * @return appropriate coupon quantity from the limit list
	 */
    public static Integer getCouponQuantity(LimitResponseDto limitDto, List<OfferLimit> offerLimitList) {
		
		OfferLimit limit = FilterValues.findAnyLimitWithinLimitList(offerLimitList, Predicates.sameCustomerSegmentForLimit(limitDto.getCustomerSegment()));
		return !ObjectUtils.isEmpty(limit)? limit.getCouponQuantity(): null;
	}
    
    /**
     * 
     * @param purchaseRequest
     * @param eligibilityInfo
     * @param applicableRateList
     * @param points
     * @return amountInfo object for validating amount paid for purchase
     */
    public static AmountInfo setAmountInfoValues(PurchaseRequestDto purchaseRequest, EligibilityInfo eligibilityInfo,
			List<ConversionRate> applicableRateList, Integer points, ResultResponse resultResponse) {
		
    	boolean pointMethod = Checks.checkPointsMethod(purchaseRequest.getSelectedOption());
		boolean fullPointsMethod = Checks.checkIsFullPointsPaymentMethod(purchaseRequest.getSelectedOption());
		boolean goldCertificate = Checks.checkIsGoldCertificate(purchaseRequest.getSelectedPaymentItem());
		
		AmountInfo amountInfo = new AmountInfo();
		amountInfo.setOfferCost(goldCertificate 
				              ? purchaseRequest.getVoucherDenomination()/OffersConfigurationConstants.BALANCE_TO_AMOUNT_RATE
				              : getAmountValue(purchaseRequest, eligibilityInfo.getOffer(), eligibilityInfo.getSubOffer()));
		
	    Integer pointsValue = pointMethod && ObjectUtils.isEmpty(points) 
	            ? getEquivalentPoints(applicableRateList, amountInfo.getOfferCost())		
	            : points; 
	                    
		amountInfo.setOfferPoints(!ObjectUtils.isEmpty(pointsValue) 
					    		? pointsValue 
					    		: OffersConfigurationConstants.ZERO_INTEGER);
		amountInfo.setSpentAmount(purchaseRequest.getSpentAmount());
		amountInfo.setSpentPoints(purchaseRequest.getSpentPoints());
		amountInfo.setCost(amountInfo.getOfferCost());
		
		amountInfo.setVatAmount(!eligibilityInfo.getAdditionalDetails().isFree() && !ObjectUtils.isEmpty(eligibilityInfo.getOffer().getVatPercentage())
				?  getVatAmount(fullPointsMethod, purchaseRequest.getCouponQuantity(), eligibilityInfo.getOffer().getVatPercentage(), amountInfo)
				: OffersConfigurationConstants.ZERO_DOUBLE);
	    amountInfo.setPurchaseAmount(getPurchaseAmount(amountInfo, Checks.checkPaymentMethodFullPoints(purchaseRequest.getSelectedOption()), 
	    		purchaseRequest.getCouponQuantity())); 
	    
	    if(amountInfo.getSpentPoints()>0) {
	    	
	    	amountInfo.setConvertedSpentPointsToAmount(Checks.checkIsPartialPointsPaymentMethod(purchaseRequest.getSelectedOption())
					? getEquivalentAmount(applicableRateList, purchaseRequest.getSpentPoints(), resultResponse)
					: null);
	    	
	    }
		
		return getPurchaseAmountForTelecomSpend(purchaseRequest, amountInfo);
	}
	
	/**
     * 
     * @param purchaseRequest
     * @param amountInfo
     * @return amount info with purchase amount set for telecom spend
     */
    private static AmountInfo getPurchaseAmountForTelecomSpend(PurchaseRequestDto purchaseRequest, AmountInfo amountInfo) {
		
    	if(Checks.checkIsTelcomType(purchaseRequest.getSelectedPaymentItem())) {
    		
    		if(Checks.checkIsFullPointsPaymentMethod(purchaseRequest.getSelectedOption())) {
    			
    			amountInfo.setPurchaseAmount((double) purchaseRequest.getSpentPoints());
    			
    		} else if(Checks.checkIsPartialPointsPaymentMethod(purchaseRequest.getSelectedOption())) {
    			
    			amountInfo.setPurchaseAmount(purchaseRequest.getSpentAmount() + amountInfo.getConvertedSpentPointsToAmount());
    			
    		} else {
    			
    			amountInfo.setPurchaseAmount(purchaseRequest.getSpentAmount());
    		}
    		
    	}
		
    	return amountInfo;
	}
    
    /**
     * 
     * @param amountInfo
     * @param cardMethod
     * @param couponQuantity
     * @return total purchase amount for transaction
     */
    private static Double getPurchaseAmount(AmountInfo amountInfo, boolean fullPointsMethod, Integer couponQuantity) {
		
		return !fullPointsMethod 
		     ? couponQuantity * getCalculatedPurchaseAmount(amountInfo.getOfferCost(), amountInfo.getVatAmount())
		     : couponQuantity * getCalculatedPurchaseAmount((double) amountInfo.getOfferPoints(), amountInfo.getVatAmount());
	}
    
    /**
     * 
     * @param cost
     * @param vat
     * @return calculated purchase amount for purchase
     */
    public static Double getCalculatedPurchaseAmount(Double cost, Double vat) {
		
		return cost + vat;
		
	}
    
    /**
     * 
     * @param cardMethod
     * @param couponQuantity
     * @param vatPercentage
     * @param amountInfo
     * @return vat amount for the transaction
     */
    private static Double getVatAmount(boolean fullPointsMethod, Integer couponQuantity, Double vatPercentage, AmountInfo amountInfo) {
		
		return !fullPointsMethod 
	        ? getCalculatedVatAmount(amountInfo.getCost(), Utilities.getDoubleValue(vatPercentage))
	        : Math.ceil(getCalculatedVatAmount((double) amountInfo.getOfferPoints(), Utilities.getDoubleValue(vatPercentage)));
			
	}
    
    /**
     * 
     * @param cost
     * @param vatPercentage
     * @return calculated vat amount for the transaction
     */
    public static Double getCalculatedVatAmount(Double cost, Double vatPercentage) {
		
		return null!=vatPercentage ? vatPercentage*cost : OffersConfigurationConstants.ZERO_DOUBLE;
	}
    
    /**
     * 
     * @param amount
     * @param quantity
     * @return total offer amount for the purchase
     */
    public static Double getOfferAmount(Double amount, Integer quantity) {
		
		return amount*quantity;
		
	}
    
    /**
     * 
     * @param purchaseRequest
     * @param offerCatalog
     * @param subOffer
     * @param denomination
     * @return points value for the offer
     */
    public static Integer getOfferPointValue(PurchaseRequestDto purchaseRequest, OfferCatalog offerCatalog,
			SubOffer subOffer, Denomination denomination) {
		
		Integer pointsValue = null;
		boolean isDealVoucher = Checks.checkIsDealVoucher(purchaseRequest.getSelectedPaymentItem()); 
		boolean isDenomination = Checks.checkIsDenomination(purchaseRequest.getVoucherDenomination());
		boolean isGoldCertificate = Checks.checkIsGoldCertificate(purchaseRequest.getSelectedPaymentItem());
	    
		if(!isGoldCertificate) {
			
			Integer dealVoucherPoints = isDealVoucher && !ObjectUtils.isEmpty(subOffer) && !ObjectUtils.isEmpty(subOffer.getSubOfferValue()) && !ObjectUtils.isEmpty(subOffer.getSubOfferValue().getNewCost())
					? subOffer.getSubOfferValue().getNewPointValue() 
					: null;
			Integer otherVoucherPoints = null;
			
			if(!isDealVoucher) {
				
				otherVoucherPoints = isDenomination 
								   ? getDenominationPoints(isDenomination, purchaseRequest, offerCatalog, denomination) 
								   : offerCatalog.getOfferValues().getPointsValue();
			}
			
			pointsValue = isDealVoucher ? dealVoucherPoints : otherVoucherPoints;
			
		} 
		
		return pointsValue;
	}
    
    /**
     * 
     * @param isDenomination
     * @param purchaseRequest
     * @param offerCatalog
     * @param denomination
     * @return point value for the denomination in purchase request
     */
    private static Integer getDenominationPoints(boolean isDenomination, PurchaseRequestDto purchaseRequest, OfferCatalog offerCatalog,
			Denomination denomination) {
		
    	Integer denominationPoints = null;
    	
    	if(isDenomination 
		&& !(Checks.checkIsCashVoucher(purchaseRequest.getSelectedPaymentItem()) 
	    && Checks.checkFlagSet(offerCatalog.getDynamicDenomination()))) {
					
			denominationPoints = denomination.getDenominationValue().getPointValue();
		}
    	
		return denominationPoints;
	}

	/**
     * 
     * @param points
     * @param cost
     * @param conversionRateList
     * @return points value or equivalent point value for the offer
     */
    public static Integer getPointsOrConvertedValue( Integer points, Double cost, List<ConversionRate> conversionRateList) {
		
    	return ObjectUtils.isEmpty(points) 
    		? getEquivalentPoints(conversionRateList, cost)
    		: points;
	}
    
    /**
     * 
     * @param conversionRateList
     * @param partnerCode
     * @return filtered applicable conversion rate for current partner
     */
    public static List<ConversionRate> filterApplicableConversionRateList(List<ConversionRate> conversionRateList, String partnerCode){
		
		List<ConversionRate> rateList = filterConversionRateListForPartner(conversionRateList, partnerCode); 
		
		if(CollectionUtils.isEmpty(rateList)) {
			
			rateList = filterConversionRateListForPartner(conversionRateList, OfferConstants.SMILES.get()); 
		}
		
		return rateList;
	}
    
    /**
     * 
     * @param conversionRateList
     * @param partnerCode
     * @return filtered conversion rate list for partner
     */
    public static List<ConversionRate> filterConversionRateListForPartner(List<ConversionRate> conversionRateList, String partnerCode){
		
		return !StringUtils.isEmpty(partnerCode) && CollectionUtils.isNotEmpty(conversionRateList)
			 ? conversionRateList.stream().filter(c->Utilities.isEqual(partnerCode, c.getPartnerCode()))
					.collect(Collectors.toList())
			 : null;
		
	}
    
    /**
     * Sets amountInfo values after promo code is applied
     * @param promoCodeApply
     * @param eligibilityInfo
     * @param purchaseRequest
     */
    public static void setAmountInfoAfterApplyingPromoCode(PromoCodeApply promoCodeApply,
			EligibilityInfo eligibilityInfo, PurchaseRequestDto purchaseRequest) {
		
		boolean fullPointsMethod = Checks.checkPaymentMethodFullPoints(purchaseRequest.getSelectedOption());
		eligibilityInfo.getAmountInfo().setOfferCost(promoCodeApply.getCost());
		eligibilityInfo.getAmountInfo().setOfferPoints(promoCodeApply.getPointsValue());
		eligibilityInfo.getAmountInfo().setVatAmount(!eligibilityInfo.getAdditionalDetails().isFree() && !ObjectUtils.isEmpty(eligibilityInfo.getOffer().getVatPercentage())
				?  getVatAmount(fullPointsMethod, purchaseRequest.getCouponQuantity(), eligibilityInfo.getOffer().getVatPercentage(), eligibilityInfo.getAmountInfo())
				: OffersConfigurationConstants.ZERO_DOUBLE);
		eligibilityInfo.getAmountInfo().setPurchaseAmount(getPurchaseAmount(eligibilityInfo.getAmountInfo(), fullPointsMethod, purchaseRequest.getCouponQuantity())); 
	}
    
    /**
     * 
     * @param item
     * @param earnMultiplier
     * @return earnMultiplier for purchase response
     */
    public static Double getEarnMultiplierForPurchaseResponse(String item, Double earnMultiplier) {
		
		return Checks.checkIsCashVoucher(item) ? Utilities.getDoubleValueOrNull(earnMultiplier) : null;
	}
    
    /**
     * 
     * @param paymentResponse
     * @param purchaseDetails
     * @param earnMultiplier
     * @param purchaseRequest
     * @param resultResponse
     * @return response for purchase transaction
     */
    public static PurchaseResponseDto getPurchaseResponse(PaymentResponse paymentResponse, PurchaseHistory purchaseDetails, Double earnMultiplier, PurchaseRequestDto purchaseRequest, ResultResponse resultResponse) {
		
		PurchaseResponseDto purchaseResponseDto = null;
		
		if(!ObjectUtils.isEmpty(paymentResponse)  
		&& !ObjectUtils.isEmpty(paymentResponse.getPaymentStatus())) {
			
		   if(StringUtils.equalsIgnoreCase(paymentResponse.getPaymentStatus(), OfferConstants.SUCCESS.get())) {
				purchaseResponseDto = new PurchaseResponseDto();
				purchaseResponseDto.setPaymentStatus(paymentResponse.getPaymentStatus());
				purchaseResponseDto.setTransactionNo(purchaseDetails.getId());
				purchaseResponseDto.setActivityCode(purchaseDetails.getPartnerActivity().getActivityCode());
				purchaseResponseDto.setPartnerCode(purchaseDetails.getPartnerCode());
				purchaseResponseDto.setEpgTransactionId(Utilities.getStringValueOrNull(purchaseDetails.getEpgTransactionId()));
				purchaseResponseDto.setVoucherCodes(Utilities.getListOrNull(purchaseDetails.getVoucherCode()));
				purchaseResponseDto.setAdditionalParams(Utilities.getStringValueOrNull(purchaseRequest.getAdditionalParams()));
		   
				if(!ObjectUtils.isEmpty(earnMultiplier)) {
					purchaseResponseDto.setEarnPointsRate(earnMultiplier);
				}
				
		   } else {
			   
			   resultResponse.addErrorAPIResponse(paymentResponse.getErrorCode(), paymentResponse.getFailedreason());
		   }
		   
		} else {
			
			Responses.addErrorWithMessage(resultResponse, OfferErrorCodes.PAYMENT_FAILED, OfferConstants.ERROR_OCCURED.get());
			
		}
		
		return purchaseResponseDto;
	}

    /**
     * 
     * @param existingRecord
     * @return action for create/update
     */
	public static String getAction(Object existingRecord) {
		
		return ObjectUtils.isEmpty(existingRecord)?OfferConstants.INSERT_ACTION.get():OfferConstants.UPDATE_ACTION.get();
	}
	
	/**
	 * 
	 * @param purchaseRequest
	 * @param eligibilityInfo
	 * @param purchaseDetails
	 * @return Dto for gold certificate
	 */
    public static GoldCertificateDto getGoldCertificateDto(PurchaseRequestDto purchaseRequest, EligibilityInfo eligibilityInfo, PurchaseHistory purchaseDetails) {
		
		GoldCertificateDto goldCertificateDto = new GoldCertificateDto();
		goldCertificateDto.setCertificateId(OfferConstants.CERTIFICATE_PREFIX.get()+purchaseDetails.getId());
		goldCertificateDto.setTransactionId(purchaseDetails.getId());
		goldCertificateDto.setTransactionType(OfferConstants.PURCHASE.get());
		goldCertificateDto.setAccountNumber(purchaseRequest.getAccountNumber());
		goldCertificateDto.setMembershipCode(purchaseRequest.getMembershipCode());
		goldCertificateDto.setBalance((double) purchaseRequest.getVoucherDenomination());
		goldCertificateDto.setAedAmount(eligibilityInfo.getAmountInfo().getOfferCost());
		goldCertificateDto.setPointsValue(eligibilityInfo.getAmountInfo().getOfferPoints());
		goldCertificateDto.setPartnerCode(eligibilityInfo.getOffer().getPartnerCode());
		goldCertificateDto.setMerchantCode(eligibilityInfo.getOffer().getMerchant().getMerchantCode());
		goldCertificateDto.setMerchantNameEn(eligibilityInfo.getOffer().getMerchant().getMerchantName().getMerchantNameEn());
		goldCertificateDto.setMerchantNameAr(eligibilityInfo.getOffer().getMerchant().getMerchantName().getMerchantNameAr());
		goldCertificateDto.setStartDate(new Date());
		return goldCertificateDto;
	}
    
    /**
     * 
     * @param timelimits
     * @return counter after resetting
     */
    public static TimeLimits resetCounter(TimeLimits timelimits) {
		 Calendar currentCalendar = Calendar.getInstance();
		 
		 if (null!=timelimits.getDailyCount()
		  && timelimits.getDailyCount() != 0 
		  && !Checks.checkIsDateInCurrentDay(timelimits.getLastPurchased(),currentCalendar)) {
			 //reset dayCount to 0
			 timelimits.setDailyCount(0);
		 }
		 
		 if (null!=timelimits.getWeeklyCount()
         && timelimits.getWeeklyCount() != 0 
		  && !Checks.checkIsDateInCurrentWeek(timelimits.getLastPurchased(),currentCalendar)) {
			 //reset weekCount to 0
			 timelimits.setWeeklyCount(0);
		 }
		 
		 if (null!=timelimits.getMonthlyCount()
		  && timelimits.getMonthlyCount() != 0 
		  && !Checks.checkIsDateInCurrentMonth(timelimits.getLastPurchased(),currentCalendar)) {
			 //reset monthCount to 0
			 timelimits.setMonthlyCount(0);
		 }
		 
		 if (null!=timelimits.getAnnualCount()
		  && timelimits.getAnnualCount() != 0 
		  && !Checks.checkIsDateInCurrentYear(timelimits.getLastPurchased(),currentCalendar)) {
			 //reset yearCount to 0
			 timelimits.setAnnualCount(0);
		 }
		 return timelimits;
		 
	}

    /**
     * 
     * @param paymentResponse
	 * @param purchaseRequest 
     * @return pointsTransactionId for purchase
     */
	public static String getPointsTransactionId(PaymentResponse paymentResponse, PurchaseRequestDto purchaseRequest) {
		
		String pointsTransactionId = null;
		
		if(Checks.checkIsPartialPointsPaymentMethod(purchaseRequest.getSelectedOption())) {
			
			pointsTransactionId = purchaseRequest.getPartialTransactionId();
			
		} else { 
		
			pointsTransactionId =  !ObjectUtils.isEmpty(paymentResponse) && !ObjectUtils.isEmpty(paymentResponse.getExtRefNo())
				? paymentResponse.getExtRefNo()
				: null;
		}
		
		return pointsTransactionId;
	}
	
	/**
     * 
     * @param eligibilityInfo
	 * @param purchaseRequest 
	 * @return customerType for purchase
     */
	public static String getCustomerType(EligibilityInfo eligibilityInfo, PurchaseRequestDto purchaseRequest) {
		
		String customerType = null;
		if(!ObjectUtils.isEmpty(eligibilityInfo) && !ObjectUtils.isEmpty(eligibilityInfo.getMemberDetails())
				&& !CollectionUtils.isEmpty(eligibilityInfo.getMemberDetails().getCustomerType())) {
			customerType = eligibilityInfo.getMemberDetails().getCustomerType().get(0);
		} else {
			customerType = purchaseRequest.getAdditionalParams();
		}
		
		return customerType;
	}

	/**
	 * 
	 * @param offerTypeList
	 * @return list of product types
	 */
	public static List<String> getProductTypeList(List<String> offerTypeList) {
		
		List<String> productTypeList = null; 
		
		if(CollectionUtils.isNotEmpty(offerTypeList)) {
			
			productTypeList = new ArrayList<>(offerTypeList.size());
			
			for(String offerType : offerTypeList) {
				
				productTypeList.add(getProductTypeValue(offerType));
				
			}
			
		}
		
		return productTypeList;
	}
	
	/**
	 * 
	 * @param offerTypeList
	 * @return list of equivalent purchase item from offer type list
	 */
	public static List<String> getPurchaseItemListFromOfferTypeList(List<String> offerTypeList) {
		
        List<String> purchaseItemList = null; 
		
		if(CollectionUtils.isNotEmpty(offerTypeList)) {
			
			purchaseItemList = new ArrayList<>(offerTypeList.size());
			
			for(String offerType : offerTypeList) {
				
				purchaseItemList.add(getPurchaseItemFromOfferType(offerType));
				
			}
			
		}
		
		return purchaseItemList;
	}
	
	/**
	 * 
	 * @param eligibilityInfo
	 * @param offerList
	 * @param couponQuantity
	 * @param denomination
	 * @param resultResponse
	 * @return list of eligible offers after download limit check
	 */
	public static List<OfferCatalog> getEligibileOffersAfterDownoadLimitCheck(EligibilityInfo eligibilityInfo,
			List<OfferCatalog> offerList, Integer couponQuantity, Integer denomination, OfferCatalogResultResponse resultResponse) {
		
		List<OfferCatalog> eligibleOffers = null;
		if(CollectionUtils.isNotEmpty(offerList)) {
			
			eligibleOffers = new ArrayList<>(offerList.size());
			
			for(OfferCatalog offerDetails : offerList) {
				
				OfferCounter offerCounter = FilterValues.findAnyOfferCounterinCounterList(eligibilityInfo.getOfferCounterList(), Predicates.sameOfferIdForLimit(offerDetails.getOfferId()));
	        	List<String> customerSegmentNames = Utilities.intersection(eligibilityInfo.getMemberDetails().getCustomerSegment(),
						MapValues.mapCustomerSegmentInOfferLimits(offerDetails.getLimit(), Predicates.isCustomerSegmentInLimits()));
	        	Checks.checkDownloadLimitLeft(offerCounter, offerDetails, couponQuantity, eligibilityInfo, denomination, resultResponse, customerSegmentNames);	
				
	        	if(Checks.checkNoErrors(resultResponse)) {
	        		
	        		eligibleOffers.add(offerDetails);
	        		
	        	} else {
	        	
	        		Responses.removeLastError(resultResponse);
	        	}
	        	
	        }
			
		}
		
		return eligibleOffers;
	}
	
	/**
	 * Filters all active offers based on customer type, customer segment, download limit to get eligible 
	 * offers for account
	 * @param eligibilityInfo
	 * @param activeOffers
	 * @param resultResponse
	 */
	public static void filterEligibleOffers(EligibilityInfo eligibilityInfo, List<OfferCatalog> fetchedOffers, ResultResponse resultResponse) {
		
		PurchaseCount purchaseCount = getValuesForPurchaseCount(eligibilityInfo.getOfferCountersList(), eligibilityInfo.getMemberOfferCounterList(), eligibilityInfo.getAccountOfferCounterList());
		eligibilityInfo.setOfferList(fetchedOffers);
		eligibilityInfo.setOfferList(FilterValues.filterOfferList(eligibilityInfo.getOfferList(), Predicates.validCustomerType(eligibilityInfo.getMemberDetails().getCustomerType())));
		eligibilityInfo.setOfferList(FilterValues.filterOfferList(eligibilityInfo.getOfferList(), Predicates.validCustomerSegment(eligibilityInfo.getMemberDetails().getCustomerSegment(), eligibilityInfo.getRuleResult())));
		eligibilityInfo.setOfferList(FilterValues.filterOfferList(eligibilityInfo.getOfferList(), Predicates.downloadLimitLeft(null, 0, eligibilityInfo, purchaseCount, resultResponse)));
		removeLastErrorIfPresent(resultResponse);
	}
    
	/**
	 * Filters all active offers based on customer type, customer segment, download limit to get eligible 
	 * offers for account
	 * @param eligibilityInfo
	 * @param activeOffers
	 * @param resultResponse
	 */
	public static List<OfferCatalog> filterEligibleOffersForMember(EligibilityInfo eligibilityInfo, List<OfferCatalog> fetchedOffers, ResultResponse resultResponse) {
		
		fetchedOffers = FilterValues.filterOfferList(fetchedOffers, Predicates.validCustomerType(eligibilityInfo.getMemberDetails().getCustomerType()));
		fetchedOffers = FilterValues.filterOfferList(fetchedOffers, Predicates.validCustomerSegment(eligibilityInfo.getMemberDetails().getCustomerSegment(), eligibilityInfo.getRuleResult()));
		fetchedOffers = FilterValues.filterOfferList(fetchedOffers, Predicates.downloadLimitLeft(eligibilityInfo.getOfferCounterList(), null, 0, resultResponse, eligibilityInfo));
		removeLastErrorIfPresent(resultResponse);
		return fetchedOffers;

	}
	
	/**
	 * Removes last error from the response
	 * @param resultResponse
	 */
	private static void removeLastErrorIfPresent(ResultResponse resultResponse) {
		
		if(Checks.checkErrorsPresent(resultResponse)) {
			
			Responses.removeLastError(resultResponse);
		}
		
	}
	
	/**
	 * Sets extra information for offer information
	 * @param offerCatalog
	 * @param offerCatalogDto
	 * @param eligibilityInfo
	 * @param isEligibilityRequired
	 * @param weeklyPurchaseCount 
	 * @param resultResponse
	 */
	public static void setExtraInformation(OfferCatalog offerCatalog, OfferCatalogResultResponseDto offerCatalogDto,
			EligibilityInfo eligibilityInfo, boolean isEligibilityRequired, PurchaseCount purchaseCount, ResultResponse resultResponse) {
		
		if(isEligibilityRequired) {
			  
			  offerCatalogDto.setEligibility(Responses.getMemberEligibility(purchaseCount, eligibilityInfo, resultResponse));
			  getDenominationLimitFailureResult(offerCatalogDto, offerCatalog.getDenominations());	  
			  
		} else if(eligibilityInfo.isBirthdayInfoRequired()) {
			 
			 setBirthdayAvailedStatus(eligibilityInfo, offerCatalogDto); 
			  
		}
		
	}
	
	/**
	 * Sets birthday offer availed status in offer response
	 * @param eligibilityInfo
	 * @param offerCatalogDto
	 */
	public static void setBirthdayAvailedStatus(EligibilityInfo eligibilityInfo,
			OfferCatalogResultResponseDto offerCatalogDto) {
		
	  PurchaseHistory purchaseRecord = FilterValues.findAnyPurchaseRecordinRecordList(eligibilityInfo.getPurchaseHistoryList(), Predicates.sameOfferIdInPurchaseRecord(offerCatalogDto.getOfferId()));
	  offerCatalogDto.setBirthdayGiftAvailed(!ObjectUtils.isEmpty(purchaseRecord)
			  ? OfferConstants.FLAG_SET.get()
			  : OfferConstants.FLAG_NOT_SET.get());
		
	}

	/**
	 * Sets fields specific to account number in the offer response
	 * @param offerCatalogDto
	 * @param offerCatalog
	 * @param eligibilityInfo
	 */
	public static void setMemberAttributesInOffer(OfferCatalogResultResponseDto offerCatalogDto, OfferCatalog offerCatalog,
			EligibilityInfo eligibilityInfo) {
		
		offerCatalogDto.setPaymentMethods(FilterValues.filterPaymenMethodDtoList(offerCatalogDto.getPaymentMethods(), Predicates.paymentMethodInEligiblePaymentMethodsForMember(MapValues.mapMemberPaymentMethods(eligibilityInfo.getMemberDetails().getEligiblePaymentMethod()))));
		offerCatalogDto.setStaticRating(null);
		setAverageRating(offerCatalogDto, offerCatalog);
		setActiveStores(offerCatalogDto, offerCatalog);
		setValidDenominations(offerCatalog, offerCatalogDto, eligibilityInfo);
		offerCatalogDto.setSoldCount(!ObjectUtils.isEmpty(FilterValues.findAnyOfferCounterinCounterList(eligibilityInfo.getOfferCounterList(), Predicates.sameOfferIdForLimit(offerCatalog.getOfferId()))) 
				? FilterValues.findAnyOfferCounterinCounterList(eligibilityInfo.getOfferCounterList(), Predicates.sameOfferIdForLimit(offerCatalog.getOfferId())).getTotalCount() 
				: 0);
		setQuantityLeft(offerCatalogDto, eligibilityInfo);
		
	}
	
	/**
	 * Sets fields specific to account number in the eligible offer response
	 * @param offerCatalogDto
	 * @param offerCatalog
	 * @param eligibilityInfo
	 * @param conversionRateList 
	 */
	public static void setMemberAttributesInEligibleOffer(OfferCatalogResultResponseDto offerCatalogDto, EligibleOffers offerCatalog,
			EligibleOfferHelperDto eligibilityInfo) {
		
		offerCatalogDto.setPaymentMethods(FilterValues.filterPaymenMethodDtoList(offerCatalogDto.getPaymentMethods(), Predicates.paymentMethodInEligiblePaymentMethodsForMember(MapValues.mapMemberPaymentMethods(eligibilityInfo.getMemberDetails().getEligiblePaymentMethod()))));
		offerCatalogDto.setStaticRating(null);
		setAverageRatingForEligibleOffer(offerCatalogDto, offerCatalog);
		setActiveStoresForEligibleOffer(offerCatalogDto, offerCatalog);
		setValidDenominationsForEligibleOffer(offerCatalog, offerCatalogDto, eligibilityInfo);
		offerCatalogDto.setSoldCount(!ObjectUtils.isEmpty(FilterValues.findAnyOfferCounterinCounterList(eligibilityInfo.getOfferCounterList(), Predicates.sameOfferIdForLimit(offerCatalog.getOfferId()))) 
				? FilterValues.findAnyOfferCounterinCounterList(eligibilityInfo.getOfferCounterList(), Predicates.sameOfferIdForLimit(offerCatalog.getOfferId())).getTotalCount() 
				: 0);
	}

	/**
	 * Sets active stores in offer response
	 * @param offerCatalogDto
	 * @param offerCatalog
	 */
	private static void setActiveStores(OfferCatalogResultResponseDto offerCatalogDto, OfferCatalog offerCatalog) {
		
		if(CollectionUtils.isNotEmpty(offerCatalog.getOfferStores())){
			
			List<String> storeIdList = MapValues.mapStoreCodesFromStoreList(offerCatalog.getOfferStores(), Predicates.activeStores());
			if(!CollectionUtils.isEmpty(storeIdList)) {
				
				offerCatalogDto.setOfferStores(FilterValues.filterStoreDtoList(offerCatalogDto.getOfferStores(), Predicates.storeCodeInList(storeIdList)));
			}
		}
		
	}
	
	/**
	 * Sets active stores in eligible offer response
	 * @param offerCatalogDto
	 * @param offerCatalog
	 */
	private static void setActiveStoresForEligibleOffer(OfferCatalogResultResponseDto offerCatalogDto, EligibleOffers offerCatalog) {
		
		if(CollectionUtils.isNotEmpty(offerCatalog.getOfferStores())){
			
			List<String> storeIdList = MapValues.mapEligibleOfferStoreCodesFromStoreList(offerCatalog.getOfferStores(), Predicates.activeEligibleOfferStores());
			if(!CollectionUtils.isEmpty(storeIdList)) {
				
				offerCatalogDto.setOfferStores(FilterValues.filterStoreDtoList(offerCatalogDto.getOfferStores(), Predicates.storeCodeInList(storeIdList)));
			}
		}
		
	}
	
	/**
	 * Set valid denominations in eligible offer response
	 * @param offerCatalog
	 * @param offerCatalogDto
	 * @param eligibilityInfo
	 */
	private static void setValidDenominations(OfferCatalog offerCatalog, OfferCatalogResultResponseDto offerCatalogDto,
			EligibilityInfo eligibilityInfo) {
	
	   if(CollectionUtils.isNotEmpty(offerCatalog.getDenominations())
	   && CollectionUtils.isNotEmpty(offerCatalogDto.getDenominations())		   
       && !ObjectUtils.isEmpty(eligibilityInfo.getOffer().getLimit())		   
       && !ObjectUtils.isEmpty(eligibilityInfo.getOfferCounters())){
		   
    	   List<DenominationDto> validDenominations = new ArrayList<>(1);
    	   List<Integer> limitDenominations = MapValues.mapLimitDenominationFromOfferLimit(offerCatalog.getLimit());
    	   List<Integer> configuredDenominations = MapValues.mapDenominationDirhamValues(offerCatalog.getDenominations());
    	   List<Integer> denominationList = Utilities.intersectionInteger(limitDenominations , configuredDenominations);
    	   List<DenominationLimitCounter> limitList = getDenominationLimit(offerCatalog.getLimit(), eligibilityInfo.getCommonSegmentNames(), eligibilityInfo.isCustomerSegmentCheckRequired(), denominationList);
    	   List<DenominationLimitCounter> counterList = getDenominationCounter(eligibilityInfo.getOfferCounters(), denominationList, eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberDetails().getMembershipCode());
    	   
    	   if(!CollectionUtils.isEmpty(denominationList)
    	   && !CollectionUtils.isEmpty(limitList)
    	   && !CollectionUtils.isEmpty(counterList)){
    		   
    		   getAllValidDenominationAfterDownloadLimitCheck(offerCatalog, offerCatalogDto, limitList, counterList, validDenominations, denominationList); 
    		    
    	   } else {
 			  
 			  validDenominations.addAll(offerCatalogDto.getDenominations());
 		  }
    	   
    	   offerCatalogDto.setDenominations(validDenominations);
       }
		
	}
	
	/**
	 * Set valid denominations in eligible offer response
	 * @param offerCatalog
	 * @param offerCatalogDto
	 * @param eligibilityInfo
	 */
	private static void setValidDenominationsForEligibleOffer(EligibleOffers offerCatalog, OfferCatalogResultResponseDto offerCatalogDto,
			EligibleOfferHelperDto eligibilityInfo) {
	
	   if(CollectionUtils.isNotEmpty(offerCatalog.getDenominations())
	   && CollectionUtils.isNotEmpty(offerCatalogDto.getDenominations())		   
       && !ObjectUtils.isEmpty(eligibilityInfo.getOffer().getLimit())		   
       && !ObjectUtils.isEmpty(eligibilityInfo.getOfferCounters())){
		   
    	   List<DenominationDto> validDenominations = new ArrayList<>(1);
    	   List<Integer> limitDenominations = MapValues.mapLimitDenominationFromOfferLimit(offerCatalog.getLimit());
    	   List<Integer> configuredDenominations = MapValues.mapEligibleOfferDenominationDirhamValues(offerCatalog.getDenominations());
    	   List<Integer> denominationList = Utilities.intersectionInteger(limitDenominations , configuredDenominations);
    	   List<DenominationLimitCounter> limitList = getDenominationLimit(offerCatalog.getLimit(), eligibilityInfo.getCommonSegmentNames(), eligibilityInfo.isCustomerSegmentCheckRequired(), denominationList);
    	   List<DenominationLimitCounter> counterList = getDenominationCounter(eligibilityInfo.getOfferCounters(), denominationList, eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberDetails().getMembershipCode());
    	   
    	   if(!CollectionUtils.isEmpty(denominationList)
    	   && !CollectionUtils.isEmpty(limitList)
    	   && !CollectionUtils.isEmpty(counterList)){
    		   
    		   getAllValidDenominationAfterDownloadLimitCheckForEligibleOffer(offerCatalog, offerCatalogDto, limitList, counterList, validDenominations, denominationList); 
    		    
    	   } else {
 			  
 			  validDenominations.addAll(offerCatalogDto.getDenominations());
 		  }
    	   
    	   offerCatalogDto.setDenominations(validDenominations);
       }
		
	}

	/**
	 * Filters all valid denominations after denomination download limit check
	 * @param offerCatalog
	 * @param offerCatalogDto
	 * @param limitList
	 * @param counterList
	 * @param validDenominations
	 * @param denominationList
	 */
	private static void getAllValidDenominationAfterDownloadLimitCheck(OfferCatalog offerCatalog,
			OfferCatalogResultResponseDto offerCatalogDto, List<DenominationLimitCounter> limitList,
			List<DenominationLimitCounter> counterList, List<DenominationDto> validDenominations, List<Integer> denominationList) {
		
		for(DenominationDto denominationDto : offerCatalogDto.getDenominations()) {
			   
			   Integer denomination = findDenominationDirhamValue(offerCatalog.getDenominations(), denominationDto.getDenominationId());
			   DenominationLimitCounter limit = null;
			   DenominationLimitCounter counter = null;
			   
			   if(Utilities.presentInIntegerList(denominationList, denomination)) {
				     
				   limit = FilterValues.findDenominationDtoInDenominationDtoList(limitList, Predicates.sameDenominationDirhamValue(denomination)); 
				   counter = FilterValues.findDenominationDtoInDenominationDtoList(counterList, Predicates.sameDenominationDirhamValue(denomination));
 		       } 
			   
			   if(Checks.checkDenominationDownloadLimitLeft(limit, counter)) {
				   validDenominations.add(denominationDto);
			   }
			   
		   } 
		
	}
	
	/**
	 * Filters all valid denominations after denomination download limit check
	 * @param offerCatalog
	 * @param offerCatalogDto
	 * @param limitList
	 * @param counterList
	 * @param validDenominations
	 * @param denominationList
	 */
	private static void getAllValidDenominationAfterDownloadLimitCheckForEligibleOffer(EligibleOffers offerCatalog,
			OfferCatalogResultResponseDto offerCatalogDto, List<DenominationLimitCounter> limitList,
			List<DenominationLimitCounter> counterList, List<DenominationDto> validDenominations, List<Integer> denominationList) {
		
		for(DenominationDto denominationDto : offerCatalogDto.getDenominations()) {
			   
			   Integer denomination = findEligibleOfferDenominationDirhamValue(offerCatalog.getDenominations(), denominationDto.getDenominationId());
			   DenominationLimitCounter limit = null;
			   DenominationLimitCounter counter = null;
			   
			   if(Utilities.presentInIntegerList(denominationList, denomination)) {
				     
				   limit = FilterValues.findDenominationDtoInDenominationDtoList(limitList, Predicates.sameDenominationDirhamValue(denomination)); 
				   counter = FilterValues.findDenominationDtoInDenominationDtoList(counterList, Predicates.sameDenominationDirhamValue(denomination));
 		       } 
			   
			   if(Checks.checkDenominationDownloadLimitLeft(limit, counter)) {
				   validDenominations.add(denominationDto);
			   }
			   
		   } 
		
	}
     
	/**
	 * 
	 * @param denominationList
	 * @param denominationId
	 * @return Dieham value for the denomination
	 */
	private static Integer findEligibleOfferDenominationDirhamValue(List<EligibleOfferDenomination> denominationList, String denominationId) {
		
		EligibleOfferDenomination denomination = FilterValues.findAnyEligibleOfferDenominationInDenominationList(denominationList, Predicates.sameEligibleOfferDenominationIdForDenomination(denominationId));
		return !ObjectUtils.isEmpty(denomination) && !ObjectUtils.isEmpty(denomination.getDenominationValue()) 
			 ? denomination.getDenominationValue().getDirhamValue()
			 : null;
	}
	
	
	/**
	 * 
	 * @param denominationList
	 * @param denominationId
	 * @return Dieham value for the denomination
	 */
	private static Integer findDenominationDirhamValue(List<Denomination> denominationList, String denominationId) {
		
		Denomination denomination = FilterValues.findAnyDenominationInDenominationList(denominationList, Predicates.sameDenominationIdForDenomination(denominationId));
		return !ObjectUtils.isEmpty(denomination) && !ObjectUtils.isEmpty(denomination.getDenominationValue()) 
			 ? denomination.getDenominationValue().getDirhamValue()
			 : null;
	}

	/**
	 * 
	 * @param limitList
	 * @param customerSegmentList
	 * @param isCustomerSegment
	 * @param denominationList
	 * @return list of denomination limit for customer segment
	 */
	private static List<DenominationLimitCounter> getDenominationLimit(List<OfferLimit> limitList,
			List<String> customerSegmentList, boolean isCustomerSegment, List<Integer> denominationList) {
		
		List<DenominationLimitCounter> denominationLimitList = null;
		
		if(!CollectionUtils.isEmpty(denominationList)) {
			
			denominationLimitList = new ArrayList<>(denominationList.size());
			
			for(Integer denomination : denominationList) {
				
				DenominationLimitCounter denominationLimitCounter = new DenominationLimitCounter();
				denominationLimitCounter.setDenomination(denomination);
				
				if(isCustomerSegment 
				&& !CollectionUtils.isEmpty(customerSegmentList)) {
					
					getDenominationLimitWithCustomerSegment(limitList, denomination, denominationLimitCounter,customerSegmentList);		
					
				} else {
					
			        getDenominationLimitWithoutCustomerSegment(limitList, denomination, denominationLimitCounter);		
					
				}
				
				denominationLimitList.add(denominationLimitCounter);
			}
			
		}
		
		return denominationLimitList;
	}
	
	/**
	 * Sets denomination limit for customer segment
	 * @param limitList
	 * @param denomination
	 * @param denominationLimitCounter
	 * @param customerSegmentList
	 */
	private static void getDenominationLimitWithCustomerSegment(List<OfferLimit> limitList, Integer denomination,
			DenominationLimitCounter denominationLimitCounter, List<String> customerSegmentList) {
		
		List<OfferLimit> offerLimitList = FilterValues.filterOfferLimits(limitList, Predicates.customerSegmentInListForLimit(customerSegmentList));
	
		if(!CollectionUtils.isEmpty(customerSegmentList)) {
		    
			List<DenominationLimit> offerDenominationLimitList = MapValues.mapAllSameDenominationLimitForoffer(offerLimitList, Predicates.sameDenominationForOfferLimit(denomination));
			setOfferDenominationLimit(true, denominationLimitCounter, denomination, offerDenominationLimitList);
			List<DenominationLimit> accountDenominationLimitList = MapValues.mapAllSameDenominationLimitForAccount(offerLimitList, Predicates.sameDenominationForOfferLimit(denomination));
			setAccountDenominationLimit(true, denominationLimitCounter, denomination, accountDenominationLimitList);
			List<DenominationLimit> memberDenominationLimitList = MapValues.mapAllSameDenominationLimitForMember(offerLimitList, Predicates.sameDenominationForOfferLimit(denomination));
			setMemberDenominationLimit(true, denominationLimitCounter, denomination, memberDenominationLimitList);
		} 	
		
	}

	/**
	 * 
	 * @param denominationLimitList
	 * @return get maximum denomination limit from limit list 
	 */
	private static DenominationLimit getMaximumDenominationLimitFromDenominationList(
			List<DenominationLimit> denominationLimitList) {
		
		DenominationLimit denominationLimit = null;
		
		if(CollectionUtils.isNotEmpty(denominationLimitList)) {
			
			denominationLimit = new DenominationLimit();
			List<Integer> dailyLimitValues = MapValues.getDailyLimitFromDenominationLimits(denominationLimitList, Predicates.denominationDailyLimitNotNull());
			denominationLimit.setDailyLimit(!CollectionUtils.isEmpty(dailyLimitValues)? Collections.max(dailyLimitValues) : null);
			List<Integer> weeklyLimitValues = MapValues.getWeeklyLimitFromDenominationLimits(denominationLimitList, Predicates.denominationWeeklyLimitNotNull());
			denominationLimit.setWeeklyLimit(!CollectionUtils.isEmpty(weeklyLimitValues)? Collections.max(weeklyLimitValues) : null);
			List<Integer> monthlyLimitValues = MapValues.getMonthlyLimitFromDenominationLimits(denominationLimitList, Predicates.denominationMonthlyLimitNotNull());
			denominationLimit.setMonthlyLimit(!CollectionUtils.isEmpty(monthlyLimitValues)? Collections.max(monthlyLimitValues): null);
			List<Integer> annualLimitValues = MapValues.getAnnualLimitFromDenominationLimits(denominationLimitList, Predicates.denominationAnnualLimitNotNull());
			denominationLimit.setAnnualLimit(!CollectionUtils.isEmpty(annualLimitValues)? Collections.max(annualLimitValues): null);
			List<Integer> totalLimitValues = MapValues.getTotalLimitFromDenominationLimits(denominationLimitList, Predicates.denominationTotalLimitNotNull());
			denominationLimit.setTotalLimit(!CollectionUtils.isEmpty(totalLimitValues)? Collections.max(totalLimitValues): null);
		} 
		
		return denominationLimit;
	}
	
	/**
	 * Set default denomination limit
	 * @param limitList
	 * @param denomination
	 * @param denominationLimitCounter
	 */
	private static void getDenominationLimitWithoutCustomerSegment(List<OfferLimit> limitList, Integer denomination, DenominationLimitCounter denominationLimitCounter) {
		
		OfferLimit offerLimit = FilterValues.findAnyLimitWithinLimitList(limitList, Predicates.noCustomerSegmentInLimits());
		
		if(!ObjectUtils.isEmpty(offerLimit)) {
			
			setOfferDenominationLimit(false, denominationLimitCounter, denomination, offerLimit.getDenominationLimit());
			setAccountDenominationLimit(false, denominationLimitCounter, denomination, offerLimit.getAccountDenominationLimit());
			setMemberDenominationLimit(false, denominationLimitCounter, denomination,offerLimit.getMemberDenominationLimit());
			
		} 
		
	}

	/**
	 * Set denomination limit for offer
	 * @param isCustomerSegment
	 * @param denominationLimitCounter
	 * @param denomination
	 * @param denominationList
	 */
	private static void setOfferDenominationLimit(boolean isCustomerSegment, DenominationLimitCounter denominationLimitCounter, 
			Integer denomination, List<DenominationLimit> denominationList) {
		
		DenominationLimit denominationLimit = isCustomerSegment 
				? getMaximumDenominationLimitFromDenominationList(denominationList)
				: FilterValues.findAnyDenominationLimitInDenominationLimitList(denominationList, Predicates.sameDenominationForOfferLimit(denomination));
		denominationLimitCounter.setOfferDaily(!ObjectUtils.isEmpty(denominationLimit)?denominationLimit.getDailyLimit():null);
		denominationLimitCounter.setOfferWeekly(!ObjectUtils.isEmpty(denominationLimit)?denominationLimit.getWeeklyLimit():null);
		denominationLimitCounter.setOfferMonthly(!ObjectUtils.isEmpty(denominationLimit)?denominationLimit.getMonthlyLimit():null);
		denominationLimitCounter.setOfferAnnual(!ObjectUtils.isEmpty(denominationLimit)?denominationLimit.getAnnualLimit():null);
		denominationLimitCounter.setOfferTotal(!ObjectUtils.isEmpty(denominationLimit)?denominationLimit.getTotalLimit():null);
		
	}

	/**
	 * Set denomination limit for account
	 * @param isCustomerSegment
	 * @param denominationLimitCounter
	 * @param denomination
	 * @param denominationList
	 */
	private static void setAccountDenominationLimit(boolean isCustomerSegment, 
			DenominationLimitCounter denominationLimitCounter, Integer denomination, List<DenominationLimit> denominationList) {
		
		DenominationLimit accountDenominationLimit = isCustomerSegment 
				? getMaximumDenominationLimitFromDenominationList(denominationList)
				: FilterValues.findAnyDenominationLimitInDenominationLimitList(denominationList, Predicates.sameDenominationForOfferLimit(denomination));
	    denominationLimitCounter.setAccountDaily(!ObjectUtils.isEmpty(accountDenominationLimit)?accountDenominationLimit.getDailyLimit(): null);
		denominationLimitCounter.setAccountWeekly(!ObjectUtils.isEmpty(accountDenominationLimit)?accountDenominationLimit.getWeeklyLimit():null);
		denominationLimitCounter.setAccountMonthly(!ObjectUtils.isEmpty(accountDenominationLimit)?accountDenominationLimit.getMonthlyLimit():null);
		denominationLimitCounter.setAccountAnnual(!ObjectUtils.isEmpty(accountDenominationLimit)?accountDenominationLimit.getAnnualLimit():null);
		denominationLimitCounter.setAccountTotal(!ObjectUtils.isEmpty(accountDenominationLimit)?accountDenominationLimit.getTotalLimit():null);
		
	}

	/**
	 * Set denomination limit for member
	 * @param isCustomerSegment
	 * @param denominationLimitCounter
	 * @param denomination
	 * @param denominationList
	 */
	private static void setMemberDenominationLimit(boolean isCustomerSegment, 
			DenominationLimitCounter denominationLimitCounter, Integer denomination, List<DenominationLimit> denominationList) {
		
		DenominationLimit memberDenominationLimit = isCustomerSegment 
				? getMaximumDenominationLimitFromDenominationList(denominationList)
				: FilterValues.findAnyDenominationLimitInDenominationLimitList(denominationList, Predicates.sameDenominationForOfferLimit(denomination));
		denominationLimitCounter.setMemberDaily(!ObjectUtils.isEmpty(memberDenominationLimit)?memberDenominationLimit.getDailyLimit():null);
		denominationLimitCounter.setMemberWeekly(!ObjectUtils.isEmpty(memberDenominationLimit)?memberDenominationLimit.getWeeklyLimit():null);
		denominationLimitCounter.setMemberMonthly(!ObjectUtils.isEmpty(memberDenominationLimit)?memberDenominationLimit.getMonthlyLimit():null);
		denominationLimitCounter.setMemberAnnual(!ObjectUtils.isEmpty(memberDenominationLimit)?memberDenominationLimit.getAnnualLimit():null);
		denominationLimitCounter.setMemberTotal(!ObjectUtils.isEmpty(memberDenominationLimit)?memberDenominationLimit.getTotalLimit():null);
		
	}

	/**
	 * 
	 * @param offerCounter
	 * @param denominationList
	 * @param accountNumber
	 * @param membershipCode
	 * @return denomination counter
	 */
	private static List<DenominationLimitCounter> getDenominationCounter(OfferCounter offerCounter,
			List<Integer> denominationList, String accountNumber, String membershipCode) {
		
		List<DenominationLimitCounter> denominationCounterList = null;
		if(!CollectionUtils.isEmpty(denominationList)) {
			
			denominationCounterList = new ArrayList<>(denominationList.size());
			
			for(Integer denomination : denominationList) {
				
				DenominationLimitCounter denominationLimitCounter = new DenominationLimitCounter();
				denominationLimitCounter.setDenomination(denomination);
				setOfferDenominationCount(denominationLimitCounter, denomination, offerCounter);
				setAccountDenominationCount(denominationLimitCounter, denomination, offerCounter, accountNumber, membershipCode);
				setMemberDenominationCount(denominationLimitCounter, denomination, offerCounter, membershipCode);
				denominationCounterList.add(denominationLimitCounter);
			}
			
		}
		
		return denominationCounterList;
	}

	/**
	 * Sets denomination counter for offer
	 * @param denominationLimitCounter
	 * @param denomination
	 * @param offerCounter
	 */
	private static void setOfferDenominationCount(DenominationLimitCounter denominationLimitCounter,
			Integer denomination, OfferCounter offerCounter) {
		
		DenominationCount denominationCount = FilterValues.findAnyDenominationCounterInDenominationCountList(offerCounter.getDenominationCount(), Predicates.sameDenominationForOfferCounter(denomination));	
		denominationLimitCounter.setOfferDaily(!ObjectUtils.isEmpty(denominationCount)?denominationCount.getDailyCount():0);
		denominationLimitCounter.setOfferWeekly(!ObjectUtils.isEmpty(denominationCount)?denominationCount.getWeeklyCount():0);
		denominationLimitCounter.setOfferMonthly(!ObjectUtils.isEmpty(denominationCount)?denominationCount.getMonthlyCount():0);
		denominationLimitCounter.setOfferAnnual(!ObjectUtils.isEmpty(denominationCount)?denominationCount.getAnnualCount():0);
		denominationLimitCounter.setOfferTotal(!ObjectUtils.isEmpty(denominationCount)?denominationCount.getTotalCount():0);
		
	}
	
	/**
	 * Sets denomination counter for account
	 * @param denominationLimitCounter
	 * @param denomination
	 * @param offerCounter
	 * @param accountNumber
	 * @param membershipCode
	 */
	private static void setAccountDenominationCount(DenominationLimitCounter denominationLimitCounter,
			Integer denomination, OfferCounter offerCounter, String accountNumber, String membershipCode) {
		
		AccountOfferCount accountOfferCount = FilterValues.findAccountOfferCountInOfferAccountCounterList(offerCounter.getAccountOfferCount(), Predicates.sameAccountNumberAndMembershipCodeForCounter(accountNumber, membershipCode));
		DenominationCount accountDenominationCount = !ObjectUtils.isEmpty(accountOfferCount)
				? FilterValues.findAnyDenominationCounterInDenominationCountList(accountOfferCount.getDenominationCount(), Predicates.sameDenominationForOfferCounter(denomination))
				: null;
		denominationLimitCounter.setAccountDaily(!ObjectUtils.isEmpty(accountDenominationCount)?accountDenominationCount.getDailyCount():0);
		denominationLimitCounter.setAccountWeekly(!ObjectUtils.isEmpty(accountDenominationCount)?accountDenominationCount.getWeeklyCount():0);
		denominationLimitCounter.setAccountMonthly(!ObjectUtils.isEmpty(accountDenominationCount)?accountDenominationCount.getMonthlyCount():0);
		denominationLimitCounter.setAccountAnnual(!ObjectUtils.isEmpty(accountDenominationCount)?accountDenominationCount.getAnnualCount():0);
		denominationLimitCounter.setAccountTotal(!ObjectUtils.isEmpty(accountDenominationCount)?accountDenominationCount.getTotalCount():0);
		
	}
	
	/**
	 * Sets denomination counter for member
	 * @param denominationLimitCounter
	 * @param denomination
	 * @param offerCounter
	 * @param membershipCode
	 */
	private static void setMemberDenominationCount(DenominationLimitCounter denominationLimitCounter,
			Integer denomination, OfferCounter offerCounter, String membershipCode) {
		
		MemberOfferCount memberOfferCount = FilterValues.findMemberOfferCountInOfferMemberCounterList(offerCounter.getMemberOfferCount(), Predicates.sameMembershipCodeForCounter(membershipCode));
		DenominationCount memberDenominationCount = !ObjectUtils.isEmpty(memberOfferCount)
				? FilterValues.findAnyDenominationCounterInDenominationCountList(memberOfferCount.getDenominationCount(), Predicates.sameDenominationForOfferCounter(denomination))
				: null;
		denominationLimitCounter.setMemberDaily(!ObjectUtils.isEmpty(memberDenominationCount)?memberDenominationCount.getDailyCount():0);
		denominationLimitCounter.setMemberWeekly(!ObjectUtils.isEmpty(memberDenominationCount)?memberDenominationCount.getWeeklyCount():0);
		denominationLimitCounter.setMemberMonthly(!ObjectUtils.isEmpty(memberDenominationCount)?memberDenominationCount.getMonthlyCount():0);
		denominationLimitCounter.setMemberAnnual(!ObjectUtils.isEmpty(memberDenominationCount)?memberDenominationCount.getAnnualCount():0);
		denominationLimitCounter.setMemberTotal(!ObjectUtils.isEmpty(memberDenominationCount)?memberDenominationCount.getTotalCount():0);
		
	}

	/**
	 * Sets failure result if no valid denominations
	 * @param offerCatalogDto
	 * @param denominationList
	 */
	public static void getDenominationLimitFailureResult(OfferCatalogResultResponseDto offerCatalogDto, List<Denomination> denominationList) {
		
		if(CollectionUtils.isNotEmpty(denominationList) && CollectionUtils.isEmpty(offerCatalogDto.getDenominations())) {
			
			List<RuleFailure> failureStatus = CollectionUtils.isNotEmpty(offerCatalogDto.getEligibility().getFailureStatus())
					? offerCatalogDto.getEligibility().getFailureStatus()
					: new ArrayList<>(1); 
					
			if(CollectionUtils.isNotEmpty(offerCatalogDto.getEligibility().getFailureStatus())) {
				
				failureStatus.addAll(offerCatalogDto.getEligibility().getFailureStatus());
			}
			
			failureStatus.add(new RuleFailure(OfferConstants.DOWNLOAD_LIMIT.get(), OfferConstants.DOWNLOAD_LIMIT_FAILURE.get()));
			
			
			offerCatalogDto.getEligibility().setFailureStatus(failureStatus);
			offerCatalogDto.getEligibility().setStatus(false);
			
		}
		
	}
	
	/**
	 * Sets failure result if no valid denominations
	 * @param offerCatalogDto
	 * @param denominationList
	 */
	public static void getEligibleOfferDenominationLimitFailureResult(OfferCatalogResultResponseDto offerCatalogDto, List<EligibleOfferDenomination> denominationList) {
		
		if(CollectionUtils.isNotEmpty(denominationList) && CollectionUtils.isEmpty(offerCatalogDto.getDenominations())) {
			
			List<RuleFailure> failureStatus = CollectionUtils.isNotEmpty(offerCatalogDto.getEligibility().getFailureStatus())
					? offerCatalogDto.getEligibility().getFailureStatus()
					: new ArrayList<>(1); 
					
			if(CollectionUtils.isNotEmpty(offerCatalogDto.getEligibility().getFailureStatus())) {
				
				failureStatus.addAll(offerCatalogDto.getEligibility().getFailureStatus());
			}
			
			failureStatus.add(new RuleFailure(OfferConstants.DOWNLOAD_LIMIT.get(), OfferConstants.DOWNLOAD_LIMIT_FAILURE.get()));
			
			
			offerCatalogDto.getEligibility().setFailureStatus(failureStatus);
			offerCatalogDto.getEligibility().setStatus(false);
			
		}
		
	}
	
	/**
	 * Set quantity left for offer in offer response
	 * @param offerCatalogDto
	 * @param eligibilityInfo
	 */
	private static void setQuantityLeft(OfferCatalogResultResponseDto offerCatalogDto,
			EligibilityInfo eligibilityInfo) {
		
		if(Checks.checkConditionForQuantityLeft(offerCatalogDto)
		&& !ObjectUtils.isEmpty(eligibilityInfo)) {
			
			LimitCounter limit = FilterValues.findAnyLimitCounterWithinList(eligibilityInfo.getOfferLimitCounterLimitList(), Predicates.sameOfferIdForLimitCounter(offerCatalogDto.getOfferId()));
			LimitCounter counter = FilterValues.findAnyLimitCounterWithinList(eligibilityInfo.getOfferLimitCounterCounterList(), Predicates.sameOfferIdForLimitCounter(offerCatalogDto.getOfferId()));
			
			getQuantityLeftWithNonEmptyCounter(offerCatalogDto, limit, counter);
			getQuantityLeftWithEmptyCounter(offerCatalogDto, limit, counter);
				
		}
		
	}
	
	/**
	 * Set quantity left for offer in offer response
	 * @param offerCatalogDto
	 * @param eligibilityInfo
	 * @param counter 
	 * @param limit 
	 */
	public static void setQuantityLeftForEligibleOffer(OfferCatalogResultResponseDto offerCatalogDto,
			EligibleOfferHelperDto eligibilityInfo, LimitCounter limit, LimitCounter counter) {
		
		if(Checks.checkConditionForQuantityLeft(offerCatalogDto)
		&& !ObjectUtils.isEmpty(eligibilityInfo)) {
			
			getQuantityLeftWithNonEmptyCounter(offerCatalogDto, limit, counter);
			getQuantityLeftWithEmptyCounter(offerCatalogDto, limit, counter);
				
		}
		
	}

	/**
	 * Set quantity left with counter present
	 * @param offerCatalogDto
	 * @param limit
	 * @param counter
	 */
	private static void getQuantityLeftWithNonEmptyCounter(OfferCatalogResultResponseDto offerCatalogDto,
			LimitCounter limit, LimitCounter counter) {
		
		if(!ObjectUtils.isEmpty(limit)
		&& !ObjectUtils.isEmpty(counter)) {
			
			if(!ObjectUtils.isEmpty(limit.getAccountOfferDaily())) {
				
				offerCatalogDto.setQuantityLeft(limit.getAccountOfferDaily() - Utilities.getIntegerValue(counter.getAccountOfferDaily()));
				
			} else if(!ObjectUtils.isEmpty(limit.getAccountOfferWeekly())) {
				
				offerCatalogDto.setQuantityLeft(limit.getAccountOfferWeekly() - Utilities.getIntegerValue(counter.getAccountOfferWeekly()));
				
			} else if(!ObjectUtils.isEmpty(limit.getAccountOfferMonthly())) {
				
				offerCatalogDto.setQuantityLeft(limit.getAccountOfferMonthly() - Utilities.getIntegerValue(counter.getAccountOfferMonthly()));
				
			} else if(!ObjectUtils.isEmpty(limit.getAccountOfferAnnual())) {
				
				offerCatalogDto.setQuantityLeft(limit.getAccountOfferAnnual() - Utilities.getIntegerValue(counter.getAccountOfferAnnual()));
				
			} else if(!ObjectUtils.isEmpty(limit.getAccountOfferTotal())) {
				
				offerCatalogDto.setQuantityLeft(limit.getAccountOfferTotal() - Utilities.getIntegerValue(counter.getAccountOfferTotal()));
				
			}
			
		}
		
	}
	
	/**
	 * Set quantity left with counter not present
	 * @param offerCatalogDto
	 * @param limit
	 * @param counter
	 */
	private static void getQuantityLeftWithEmptyCounter(OfferCatalogResultResponseDto offerCatalogDto,
			LimitCounter limit, LimitCounter counter) {
		
		if(!ObjectUtils.isEmpty(limit)
		&& ObjectUtils.isEmpty(counter)) {
			
			if(!ObjectUtils.isEmpty(limit.getAccountOfferDaily())) {
				
				offerCatalogDto.setQuantityLeft(limit.getAccountOfferDaily());
				
			} else if(!ObjectUtils.isEmpty(limit.getAccountOfferWeekly())) {
				
				offerCatalogDto.setQuantityLeft(limit.getAccountOfferWeekly());
				
			} else if(!ObjectUtils.isEmpty(limit.getAccountOfferMonthly())) {
				
				offerCatalogDto.setQuantityLeft(limit.getAccountOfferMonthly());
				
			} else if(!ObjectUtils.isEmpty(limit.getAccountOfferAnnual())) {
				
				offerCatalogDto.setQuantityLeft(limit.getAccountOfferAnnual());
				
			} else if(!ObjectUtils.isEmpty(limit.getAccountOfferTotal())) {
				
				offerCatalogDto.setQuantityLeft(limit.getAccountOfferTotal());
				
			}
			
		}
		
	}

	/**
	 * 
	 * @param field
	 * @param valueList
	 * @param startEndRequired
	 * @return list of regular expression
	 */
	public static Criteria[] getRegexList(String field, List<String> valueList, boolean startEndRequired) {
		
		Criteria[] regExList = null;
		
		if(CollectionUtils.isNotEmpty(valueList)) {
			
			regExList = new Criteria[valueList.size()];   
			
			int seq =0;
			for(String value : valueList) {
				
				regExList[seq++] = Criteria.where(field)
						.regex( startEndRequired ? getRegexStartEndExpression(value) : getRegexPatternMatch(value) ,
								OfferConstants.CASE_INSENSITIVE.get());
				
			}
			
		}
		
		return regExList;
	}

	/**
	 * 
	 * @param value
	 * @return regular expression for pattern match
	 */
	private static String getRegexPatternMatch(String value) {
		
		return OfferConstants.REGEX_STAR.get()+ value + OfferConstants.REGEX_STAR.get();
	}

	/**
	 * 
	 * @param term
	 * @return regular expression for exact match
	 */
	public static String getRegexStartEndExpression(String term) {
		
		return OfferConstants.REGEX_START.get()+ term+ OfferConstants.REGEX_END.get();
	}

	/**
	 * 
	 * @param offerTypePreference
	 * @return sequence of offer types for displaying eligible offers
	 */
	public static List<String> getOfferTypeSequence(String offerTypePreference) {
		
	   List<String> defaultSequence = Arrays.asList(OffersConfigurationConstants.discountVoucherItem,
			   OffersConfigurationConstants.cashVoucherItem, OffersConfigurationConstants.dealVoucherItem, 
			   OffersConfigurationConstants.addOnItem, OffersConfigurationConstants.goldCertificateItem,
			   OfferConstants.OTHER_PURCHASE_ITEM.get(), OffersConfigurationConstants.billPaymentItem,
			   OfferConstants.WELCOME_GIFT_ITEM.get(), OfferConstants.LIFESTYLE_OFFER_ITEM.get());
	   List<String> newSequence = !StringUtils.isEmpty(offerTypePreference)
			   ? new ArrayList<>(defaultSequence.size())
			   : null;
		
	   if(null!=newSequence) {
		   
		   newSequence.add(offerTypePreference);
		   
		   for(String seq : defaultSequence) {
			   
			   if(!StringUtils.equalsIgnoreCase(offerTypePreference, seq)) {
				   
				   newSequence.add(seq);
			   }
			   
		   }
		   
	   }
		
	   return !StringUtils.isEmpty(offerTypePreference)   
			  ? newSequence
			  : defaultSequence;
   }
	
	/**
	 * 
	 * @param item
	 * @return equivalent offer type id from purchase item
	 */
    public static List<String> getOfferTypeIdFromOfferTypeItem(String item) {
	   
	   List<String> typeList = null;
	   
	   switch(item) {
	   
	   		case OffersConfigurationConstants.DISCOUNT_VOUCHER :  	typeList = OffersListConstants.DISCOUNT_OFFER_ID_LIST;
	   																break;
	   		case OffersConfigurationConstants.CASH_VOUCHER : 		typeList = OffersListConstants.CASH_OFFER_ID_LIST;
																	break;
	   		case OffersConfigurationConstants.DEAL_VOUCHER : 		typeList = OffersListConstants.DEAL_OFFER_ID_LIST;
																	break;
	   		case OffersConfigurationConstants.ETISALAT_ADD_ON : 	typeList = OffersListConstants.ETISALAT_BUNDLE_ID_LIST;
																	break;
	   		case OffersConfigurationConstants.GOLD_CERTIFICATE : 	typeList = OffersListConstants.GOLD_CERTIFICATE_ID_LIST;
																	break;
	   		default : 
	   		
	   }
	   
	   return typeList;
   }

    /**
     * 
     * @param priceOrder
     * @return the default price order or received price order
     */
    public static Integer getPriceOrder(Integer priceOrder) {
	
	 return !ObjectUtils.isEmpty(priceOrder)
		   ? priceOrder
		   : 1;
   }

    /**
     * 
     * @param offerCatalogList
     * @param latitude
     * @param longitude
     * @return list of distance for offer stores from input location
     */
	public static List<OfferStoreDistanceDto> getOfferStoreDistanceList(
			List<OfferCatalogResultResponseDto> offerCatalogList, String latitude, String longitude) {
		
		List<OfferCatalogResultResponseDto> applicableOfferList = FilterValues.filterOfferResponseList(offerCatalogList, Predicates.storeWithLocationInOffer());
		
		List<OfferStoreDistanceDto> offerStoreDistanceList = null;
		
		if(!CollectionUtils.isEmpty(applicableOfferList)) {
			
			offerStoreDistanceList = new ArrayList<>(applicableOfferList.size());
			
			for(OfferCatalogResultResponseDto offer : applicableOfferList) {
				
				offerStoreDistanceList.add(new OfferStoreDistanceDto(offer.getOfferId(), getDistanceFromCurrentLocation(latitude, longitude, offer.getOfferStores())));
			}
		}
		
		return offerStoreDistanceList;
	}

	/**
	 * 
	 * @param latitude
	 * @param longitude
	 * @param offerStores
	 * @return minimum distance of input location from store location
	 */
	private static Double getDistanceFromCurrentLocation(String latitude, String longitude,
			List<StoreDto> offerStores) {
		
		List<Double> distanceList = new ArrayList<>(offerStores.size());
		
		for(StoreDto store : offerStores) {
			
			if(!ObjectUtils.isEmpty(store.getStoreCoordinates())
			&& !StringUtils.isEmpty(store.getStoreCoordinates().get(0))
			&& !StringUtils.isEmpty(store.getStoreCoordinates().get(1)))
			
				distanceList.add(calculateDistance(Double.parseDouble(latitude), Double.parseDouble(longitude), 
					Double.parseDouble(store.getStoreCoordinates().get(0)), Double.parseDouble(store.getStoreCoordinates().get(1))));
		}
		
		return Collections.min(distanceList);
	}

	/**
	 * 
	 * @param latitude
	 * @param longitude
	 * @param secondLatitude
	 * @param secondLongitude
	 * @return Calculated distance between two locations
	 */
	private static Double calculateDistance(Double latitude, Double longitude,
			Double secondLatitude, Double secondLongitude) {
		
		// The math module contains a function 
        // named toRadians which converts from 
        // degrees to radians. 
		longitude = Math.toRadians(longitude); 
		secondLongitude = Math.toRadians(secondLongitude); 
		latitude = Math.toRadians(latitude); 
		secondLatitude = Math.toRadians(secondLatitude); 
  
        // Haversine formula  
        double dlon = secondLongitude - longitude;  
        double dlat = secondLatitude - latitude; 
        double a = Math.pow(Math.sin(dlat / 2), 2) 
                 + Math.cos(secondLatitude) * Math.cos(latitude) 
                 * Math.pow(Math.sin(dlon / 2),2); 
              
        double c = 2 * Math.asin(Math.sqrt(a)); 
  
        // Radius of earth in kilometers. Use 3956  
        // for miles 
        double r = 6371; 
  
        // calculate the result 
        return(c * r); 

	}	
	
	/**
	 * 
	 * @param offerTypePreference
	 * @return index of offer type in the sequence
	 */
	private static Integer getOfferTypeIndex(List<String> sequenceList, String offerTypeItem) {
		
	   return !CollectionUtils.isEmpty(sequenceList)
			   ? IntStream.range(0, sequenceList.size())
					    .filter(i-> StringUtils.equalsIgnoreCase(sequenceList.get(i), offerTypeItem))
					    .findFirst()
					    .getAsInt()
			  : null ;
		
   }
	
	/**
	 * 
	 * @param offerStores
	 * @param eligibleOffersRequest
	 * @return minimum distance of store configured for offers for current location
	 */
	private static Double getMinimumStoreDistance(List<StoreDto> offerStores,
			EligibleOffersFiltersRequest eligibleOffersRequest) {
		
		Double distance = Double.POSITIVE_INFINITY;
		
		if(!CollectionUtils.isEmpty(offerStores)) {
			
			for(StoreDto store : offerStores) {
				
				if(!CollectionUtils.isEmpty(store.getStoreCoordinates())) {
					
					  Double currentDistance = calculateDistance(Double.valueOf(eligibleOffersRequest.getLatitude()), Double.valueOf(eligibleOffersRequest.getLongitude()), 
							  Double.valueOf(store.getStoreCoordinates().get(0)), Double.valueOf(store.getStoreCoordinates().get(1)));
			          
					  if(distance>currentDistance) {
			        	  
			        	  distance = currentDistance;  
			          }		
					
				}
				
			}
			
		}
		
		return distance;
		
	}
	
	/**
	 * 
	 * @param offerCatalogList
	 * @param eligibleOffersRequest
	 * @return sorted offer list response
	 */
	public static void sortEligibleOffers(OfferCatalogResultResponse offerCatalogResultResponse,
			EligibleOffersFiltersRequest eligibleOffersRequest) {
		
		if(!StringUtils.isEmpty(eligibleOffersRequest.getLatitude()) && !StringUtils.isEmpty(eligibleOffersRequest.getLongitude())) {
    		
			offerCatalogResultResponse.setOfferCatalogs(offerCatalogResultResponse.getOfferCatalogs().stream().sorted((o1, o2)->getMinimumStoreDistance(o1.getOfferStores(), eligibleOffersRequest)
    				.compareTo(getMinimumStoreDistance(o2.getOfferStores(), eligibleOffersRequest))).collect(Collectors.toList()));
			
		}
    	
		offerCatalogResultResponse.setOfferCatalogs(offerCatalogResultResponse.getOfferCatalogs().stream().sorted((o1, o2)->o2.getIsFeatured().compareTo(o1.getIsFeatured()))
					.collect(Collectors.toList()));
		
    	if(!ObjectUtils.isEmpty(eligibleOffersRequest.getPriceOrder())
    	&& eligibleOffersRequest.getPriceOrder().equals(-1)) {
			
        	offerCatalogResultResponse.setOfferCatalogs(offerCatalogResultResponse.getOfferCatalogs().stream().sorted((o1, o2)->o2.getCost().compareTo(o1.getCost()))
					 .collect(Collectors.toList()));
        	
		} else {
			
			offerCatalogResultResponse.setOfferCatalogs(offerCatalogResultResponse.getOfferCatalogs().stream().sorted((o1, o2)->o1.getCost().compareTo(o2.getCost()))
					 .collect(Collectors.toList()));
			
		}
    	
        List<String> offerTypeSequence = ProcessValues.getOfferTypeSequence(eligibleOffersRequest.getOfferTypePreference());
                
        offerCatalogResultResponse.setOfferCatalogs(
        		offerCatalogResultResponse.getOfferCatalogs().stream().sorted((o1, o2)->getOfferTypeIndex(offerTypeSequence, getPurchaseItemFromOfferType(o1.getOfferTypeId()))
            			.compareTo(getOfferTypeIndex(offerTypeSequence, getPurchaseItemFromOfferType(o2.getOfferTypeId())))).collect(Collectors.toList()));
    	
	}

	/**
	 * 
	 * @param offerCatalogList
	 * @param eligibleOffersRequest
	 * @return sorted offer list
	 */
	public static List<OfferCatalogResultResponseDto> sortOffers(List<OfferCatalogResultResponseDto> offerCatalogList,
			EligibleOffersFiltersRequest eligibleOffersRequest) {
		
		List<OfferCatalogResultResponseDto> sortedOfferList = new ArrayList<>(offerCatalogList.size());
		
    	if(!StringUtils.isEmpty(eligibleOffersRequest.getLatitude()) && !StringUtils.isEmpty(eligibleOffersRequest.getLongitude())) {
    		
    		offerCatalogList = sortOffersByDistance(offerCatalogList, eligibleOffersRequest.getLatitude(), eligibleOffersRequest.getLongitude());
    		
    	}
    	
		sortedOfferList.addAll(FilterValues.filterOfferResponseList(offerCatalogList, Predicates.featuredOffers()));
		sortedOfferList.addAll(FilterValues.filterOfferResponseList(offerCatalogList, Predicates.nonFeaturedOffers()));
		return sortOffersByOfferType(sortedOfferList, ProcessValues.getOfferTypeSequence(eligibleOffersRequest.getOfferTypePreference()),
				ProcessValues.getPriceOrder(eligibleOffersRequest.getPriceOrder()), eligibleOffersRequest.isGrouped());
		
	}
    	
	/**
	 * 
	 * @param offerCatalogList
	 * @param latitude
	 * @param longitude
	 * @return offers sorted by distance of input location from stores
	 */
    private static List<OfferCatalogResultResponseDto> sortOffersByDistance(List<OfferCatalogResultResponseDto> offerCatalogList, 
			String latitude, String longitude) {
		
		List<OfferCatalogResultResponseDto> sortedOfferList = new ArrayList<>(offerCatalogList.size());
		List<String> nonDistanceStoreOfferList = MapValues.mapOfferIdFromOfferResponseList(offerCatalogList, Predicates.noStoresWithLocationInOffer());
		List<OfferStoreDistanceDto> offerStoreDistanceList = getOfferStoreDistanceList(offerCatalogList, latitude, longitude);
		
		if(CollectionUtils.isNotEmpty(offerStoreDistanceList)) {
			
			offerStoreDistanceList = offerStoreDistanceList.stream().sorted((o1, o2)->o1.getDistance().compareTo(o2.getDistance()))
					 .collect(Collectors.toList());
			offerStoreDistanceList.forEach(o->sortedOfferList.add(FilterValues.findAnyOfferResponseWithinOfferResponseList(offerCatalogList, Predicates.sameOfferIdForOfferResponse(o.getOfferId()))));
			
		}
		
		if(CollectionUtils.isNotEmpty(nonDistanceStoreOfferList)) {
			
			nonDistanceStoreOfferList.forEach(n->sortedOfferList.add(FilterValues.findAnyOfferResponseWithinOfferResponseList(offerCatalogList, Predicates.sameOfferIdForOfferResponse(n))));
			
		}
		
		return sortedOfferList;

	}

    /**
     * 
     * @param offerCatalogList
     * @param offerTypeSequence
     * @param priceOrder
     * @param isGrouped
     * @return offers sorted by offer type
     */
	private static List<OfferCatalogResultResponseDto> sortOffersByOfferType(List<OfferCatalogResultResponseDto> offerCatalogList,
			List<String> offerTypeSequence, Integer priceOrder, boolean isGrouped) {
		
		List<OfferCatalogResultResponseDto> sortedOfferList = new ArrayList<>(offerCatalogList.size());
		
		for(String type : offerTypeSequence) {
			
			List<OfferCatalogResultResponseDto> filteredOfferList = FilterValues.filterOfferResponseList(offerCatalogList, Predicates.sameOfferTypeId(ProcessValues.getOfferTypeIdFromOfferTypeItem(type))); 
		    
			if(Checks.checkIsDiscountVoucher(type) && isGrouped) {
				
				sortGroupedOffersByPartnerCode(filteredOfferList);
				
			}
			
			sortedOfferList.addAll(sortOffersByPrice(filteredOfferList, priceOrder));
			
		}
		
		return sortedOfferList;
	}

	/**
	 * 
	 * @param offerResponseList
	 * @return grouped offers sorted by partner code
	 */
	private static List<OfferCatalogResultResponseDto> sortGroupedOffersByPartnerCode(
			List<OfferCatalogResultResponseDto> offerResponseList) {
		
		Map<String, List<OfferCatalogResultResponseDto>> groupOfferByPartnerCode = 
				offerResponseList.stream().collect(Collectors.groupingBy(OfferCatalogResultResponseDto::getPartnerCode));
		groupOfferByPartnerCode.entrySet().forEach(g->offerResponseList.addAll(g.getValue()));
		return offerResponseList;
	}

	/**
	 * 
	 * @param offerCatalogList
	 * @param priceOrder
	 * @return offers sorted on the basis of price(cost)
	 */
	private static List<OfferCatalogResultResponseDto> sortOffersByPrice(List<OfferCatalogResultResponseDto> offerCatalogList, Integer priceOrder) {
		
		List<OfferCatalogResultResponseDto> sortedOffers = null;
		if(priceOrder.equals(-1)) {
			
			sortedOffers = offerCatalogList.stream().sorted((o1, o2)->o2.getCost().compareTo(o1.getCost()))
					 .collect(Collectors.toList());
			
		} else {
			
			sortedOffers = offerCatalogList.stream().sorted((o1, o2)->o1.getCost().compareTo(o2.getCost()))
					 .collect(Collectors.toList());
			
		}
		
		return sortedOffers;
		
	}
	
	/**
	 * Filters offers with download limit remaining
	 * @param eligibilityInfo
	 * @param resultResponse
	 */
	public static void filterOffersWithDownloadLimitLeft(EligibilityInfo eligibilityInfo, OfferCatalogResultResponse resultResponse) {
		
		FilterValues.filterOfferList(eligibilityInfo.getOfferList(), Predicates.downloadLimitLeft(eligibilityInfo.getOfferCounterList(), null, 0, resultResponse, eligibilityInfo));
		ProcessValues.removeLastErrorIfPresent(resultResponse);
		
	}

	/**
	 * Sets limit counter list to eligibilityInfo for validations
	 * @param eligibilityInfo
	 * @param counter
	 * @param limit
	 */
	public static void setLimitCounterToList(EligibilityInfo eligibilityInfo, LimitCounter counter,
			LimitCounter limit) {
		
		if(!ObjectUtils.isEmpty(eligibilityInfo)) {
			
			if(!ObjectUtils.isEmpty(limit)) {
				
				List<LimitCounter> limitList = !CollectionUtils.isEmpty(eligibilityInfo.getOfferLimitCounterLimitList())
						? eligibilityInfo.getOfferLimitCounterLimitList()
						: new ArrayList<>(1);
				limitList.add(limit);	
				eligibilityInfo.setOfferLimitCounterLimitList(limitList);
				
			}

			if(!ObjectUtils.isEmpty(counter)) {
				
				List<LimitCounter> counterList = !CollectionUtils.isEmpty(eligibilityInfo.getOfferLimitCounterCounterList())
						? eligibilityInfo.getOfferLimitCounterCounterList()
						: new ArrayList<>(1);
				counterList.add(counter);	
				eligibilityInfo.setOfferLimitCounterCounterList(counterList);
				
			}
		}
		
	}
	
	/**
	 * 
	 * @param eligibilityInfo
	 * @param selectedPaymentItem
	 * @return partner code for purchase transaction
	 */
	public static String getPartnerCode(EligibilityInfo eligibilityInfo, String item) {
		
		return Checks.checkIsSubscription(item)
			? OfferConstants.PURCHASE_PARTNER_CODE.get()
			: eligibilityInfo.getOffer().getPartnerCode();
	}

	/**
	 * 
	 * @param value
	 * @return program activity for the purchase item
	 */
	public static String getProgramActivity(String value) {
		
		return Utilities.presentInList(OffersListConstants.ELIGIBLE_TELECOM_SPEND_ITEMS, value)
				? OfferConstants.TELECOM_SPEND_PROGRAM_ACTIVITY.get()
				: OfferConstants.MARKETPLACE_PROGRAM_ACTIVITY.get();
	}

	/**
	 * 
	 * @param spentPoints
	 * @return negative value if input is positive
	 */
	public static Integer getNegativeValuesForPositiveValues(Integer value) {
		
		if(!ObjectUtils.isEmpty(value) && value>0.0) {
			
			value = Utilities.getNegativeIntegerValue(value);
		}
		
		return value;
	}

	/**
	 * 
	 * @param birthdayInfo
	 * @param date 
	 * @return the duration for birthday week
	 */
	public static BirthdayDurationInfoDto getBirthdayDurationInfo(BirthdayInfo birthdayInfo, Date birthDate) {

		Date dob = Utilities.setCurrentYear(birthDate);
		Integer minusDuration = !ObjectUtils.isEmpty(birthdayInfo)? Utilities.getNegativeIntegerValue(birthdayInfo.getThresholdMinusValue()) :0;
		Integer plusDuration = !ObjectUtils.isEmpty(birthdayInfo)? birthdayInfo.getThresholdPlusValue(): 0;
		
	    Date startDate = Utilities.getDateFromSpecificDate(minusDuration, dob); 
		startDate = Utilities.setTimeInDate(startDate, OfferConstants.FROM_DATE_TIME.get());
	    Date endDate = Utilities.getDateFromSpecificDate(plusDuration, dob); 
	    endDate = Utilities.setTimeInDate(endDate, OfferConstants.FROM_DATE_TIME.get());
	    
	    BirthdayDurationInfoDto birthdayDurationInfoDto = new BirthdayDurationInfoDto();
	    birthdayDurationInfoDto.setStartDate(startDate);
		birthdayDurationInfoDto.setEndDate(endDate);
		
		return birthdayDurationInfoDto;
	}
	
	/**
	 * 
	 * @param accountNumber
	 * @param membershipCode
	 * @param offerId
	 * @param couponQuantity
	 * @param denomination
	 * @param rules 
	 * @return  error record to be saved for offer counter
	 */
	public static ErrorRecords getErrorRecord(String accountNumber, String membershipCode, String offerId,
			Integer couponQuantity, Integer denomination, List<String> rules) {
		
		ErrorRecords errorRecord = new ErrorRecords();
		errorRecord.setOfferId(offerId);
		errorRecord.setRules(rules);
		errorRecord.setAccountNumber(accountNumber);
		errorRecord.setMembershipCode(membershipCode);	
		errorRecord.setCouponQuantity(couponQuantity);
		errorRecord.setDenomination(denomination);	 
		errorRecord.setCreatedDate(new Date());	
		errorRecord.setStatus(OfferConstants.NEW_STATUS.get());
		return errorRecord;
		
	}

	/**
	 * Resets the offer counter as per the error records
	 * @param erroRecordList
	 * @param offerCounter
	 */
	public static void checkErrorRecordAndSetCounter(List<ErrorRecords> errorRecordList, OfferCounter offerCounter) {
		
		if(!CollectionUtils.isEmpty(errorRecordList) && !ObjectUtils.isEmpty(offerCounter)) {
			
			for(ErrorRecords errorRecord : errorRecordList) {
				
				offerCounter.setDailyCount(Utilities.getIntegerValue(offerCounter.getDailyCount()) + Utilities.getIntegerValue(errorRecord.getCouponQuantity()));
				offerCounter.setWeeklyCount(Utilities.getIntegerValue(offerCounter.getWeeklyCount()) + Utilities.getIntegerValue(errorRecord.getCouponQuantity()));
				offerCounter.setMonthlyCount(Utilities.getIntegerValue(offerCounter.getMonthlyCount()) + Utilities.getIntegerValue(errorRecord.getCouponQuantity()));
				offerCounter.setAnnualCount(Utilities.getIntegerValue(offerCounter.getAnnualCount()) + Utilities.getIntegerValue(errorRecord.getCouponQuantity()));
				offerCounter.setTotalCount(Utilities.getIntegerValue(offerCounter.getTotalCount()) + Utilities.getIntegerValue(errorRecord.getCouponQuantity()));
				setErrorRecordValueForDenominationCount(errorRecord, offerCounter.getDenominationCount());
				setErrorRecordValueForMembership(errorRecord, offerCounter.getMemberOfferCount());
				setErrorRecordValueForAccount(errorRecord, offerCounter.getAccountOfferCount());
				
			}
			
		}
		
	}

	/**
	 * Set specific member offer counter with error record value
	 * @param errorRecord
	 * @param memberOfferCount
	 */
	private static void setErrorRecordValueForMembership(ErrorRecords errorRecord,
			List<MemberOfferCount> memberOfferCounterList) {
		
		if(!CollectionUtils.isEmpty(memberOfferCounterList)) {
			
			for(MemberOfferCount count : memberOfferCounterList) {
				
				if(StringUtils.equals(count.getMembershipCode(), errorRecord.getMembershipCode())) {
					
					count.setDailyCount(Utilities.getIntegerValue(count.getDailyCount()) + Utilities.getIntegerValue(errorRecord.getCouponQuantity()));
					count.setWeeklyCount(Utilities.getIntegerValue(count.getWeeklyCount()) + Utilities.getIntegerValue(errorRecord.getCouponQuantity()));
					count.setMonthlyCount(Utilities.getIntegerValue(count.getMonthlyCount()) + Utilities.getIntegerValue(errorRecord.getCouponQuantity()));
					count.setAnnualCount(Utilities.getIntegerValue(count.getAnnualCount()) + Utilities.getIntegerValue(errorRecord.getCouponQuantity()));
					count.setTotalCount(Utilities.getIntegerValue(count.getTotalCount()) + Utilities.getIntegerValue(errorRecord.getCouponQuantity()));
					setErrorRecordValueForDenominationCount(errorRecord, count.getDenominationCount());
				}
				
			}
			
		}
		
	}

	/**
	 * Set specific account offer counter with error record value
	 * @param errorRecord
	 * @param accountOfferCount
	 */
	private static void setErrorRecordValueForAccount(ErrorRecords errorRecord,
			List<AccountOfferCount> accountOfferCounterList) {
		
		if(!CollectionUtils.isEmpty(accountOfferCounterList)) {
			
			for(AccountOfferCount count : accountOfferCounterList) {
				
				if(StringUtils.equals(count.getAccountNumber(), errorRecord.getAccountNumber())
				&& StringUtils.equals(count.getMembershipCode(), errorRecord.getMembershipCode())) {
					
					count.setDailyCount(Utilities.getIntegerValue(count.getDailyCount()) + Utilities.getIntegerValue(errorRecord.getCouponQuantity()));
					count.setWeeklyCount(Utilities.getIntegerValue(count.getWeeklyCount()) + Utilities.getIntegerValue(errorRecord.getCouponQuantity()));
					count.setMonthlyCount(Utilities.getIntegerValue(count.getMonthlyCount()) + Utilities.getIntegerValue(errorRecord.getCouponQuantity()));
					count.setAnnualCount(Utilities.getIntegerValue(count.getAnnualCount()) + Utilities.getIntegerValue(errorRecord.getCouponQuantity()));
					count.setTotalCount(Utilities.getIntegerValue(count.getTotalCount()) + Utilities.getIntegerValue(errorRecord.getCouponQuantity()));
					setErrorRecordValueForDenominationCount(errorRecord, count.getDenominationCount());
				}
				
			}
			
		}
		
	}

	/**
	 * Set specific denomination counter with error record value
	 * @param errorRecord
	 * @param denominationCount
	 */
	private static void setErrorRecordValueForDenominationCount(ErrorRecords errorRecord,
			List<DenominationCount> denominationCounterList) {
		
		if(!CollectionUtils.isEmpty(denominationCounterList)) {
			
			for(DenominationCount count : denominationCounterList) {
				
				if(count.getDenomination().equals(errorRecord.getDenomination())) {
					
					count.setDailyCount(Utilities.getIntegerValue(count.getDailyCount()) + Utilities.getIntegerValue(errorRecord.getCouponQuantity()));
					count.setWeeklyCount(Utilities.getIntegerValue(count.getWeeklyCount()) + Utilities.getIntegerValue(errorRecord.getCouponQuantity()));
					count.setMonthlyCount(Utilities.getIntegerValue(count.getMonthlyCount()) + Utilities.getIntegerValue(errorRecord.getCouponQuantity()));
					count.setAnnualCount(Utilities.getIntegerValue(count.getAnnualCount()) + Utilities.getIntegerValue(errorRecord.getCouponQuantity()));
					count.setTotalCount(Utilities.getIntegerValue(count.getTotalCount()) + Utilities.getIntegerValue(errorRecord.getCouponQuantity()));
					
				}
				
			}
			
		}
		
	}

	/**
	 * 
	 * @param purchaseRequest
	 * @param headers
	 * @param offerDetails
	 * @param conversionRateList
	 * @return EligibilityInfo object for welcome gift
	 */
	public static EligibilityInfo getEligibilityInfoForWelcomeGift(PurchaseRequestDto purchaseRequest, Headers headers,
			OfferCatalog offerDetails, List<ConversionRate> conversionRateList, ResultResponse resultResponse) {
		
		Denomination denomination = Checks.checkIsDenomination(purchaseRequest.getVoucherDenomination())
                && !ObjectUtils.isEmpty(offerDetails)
                ? FilterValues.findAnyDenominationInDenominationList(offerDetails.getDenominations(), Predicates.sameDirhamValueForDenomination(purchaseRequest.getVoucherDenomination()))
                : null;		 
		SubOffer subOffer = Checks.checkIsDealVoucher(purchaseRequest.getSelectedPaymentItem())
				? FilterValues.findAnySubOfferInList(offerDetails.getSubOffer(), Predicates.sameSubOfferId(purchaseRequest.getSubOfferId()))
				: null;
		
		AdditionalDetails additionalDetails = new AdditionalDetails(false, false, false, false, true, Checks.checkIsOfferWelcomeGift(offerDetails.getGiftInfo()), false, false, false, false, null, false);
		EligibilityInfo eligibilityInfo = new EligibilityInfo();
		eligibilityInfo.setHeaders(headers);
		eligibilityInfo.setMember(true);
		eligibilityInfo.setAccountNumber(purchaseRequest.getAccountNumber());
		eligibilityInfo.setDenomination(denomination);
		eligibilityInfo.setSubOffer(subOffer);
		eligibilityInfo.setAdditionalDetails(additionalDetails);
		eligibilityInfo.setConversionRateList(conversionRateList);
		eligibilityInfo.setOffer(offerDetails);
		eligibilityInfo.setOfferList(Arrays.asList(offerDetails));
		
		Integer points = ProcessValues.getOfferPointValue(purchaseRequest, offerDetails, eligibilityInfo.getSubOffer(), eligibilityInfo.getDenomination());
		AmountInfo amountInfo = ProcessValues.setAmountInfoValues(purchaseRequest, eligibilityInfo, conversionRateList, points, resultResponse);
		eligibilityInfo.setAmountInfo(amountInfo);
				
		return eligibilityInfo;
	}
	
	/**
	 * 
	 * @param purchaseRequest
	 * @param headers
	 * @param offerDetails
	 * @param conversionRateList
	 * @return EligibilityInfo object for free voucher generation
	 */
	public static EligibilityInfo getEligibilityInfoForFreeVoucherGeneration(PurchaseRequestDto purchaseRequest, Headers headers,
			OfferCatalog offerDetails, List<ConversionRate> conversionRateList, Denomination denomination) {
		
		SubOffer subOffer = Checks.checkIsDealVoucher(purchaseRequest.getSelectedPaymentItem())
				? FilterValues.findAnySubOfferInList(offerDetails.getSubOffer(), Predicates.sameSubOfferId(purchaseRequest.getSubOfferId()))
				: null;
		
		AdditionalDetails additionalDetails = new AdditionalDetails(false, false, false, false, true, Checks.checkIsOfferWelcomeGift(offerDetails.getGiftInfo()), false, false, false, false, null, false);
		EligibilityInfo eligibilityInfo = new EligibilityInfo();
		eligibilityInfo.setHeaders(headers);
		eligibilityInfo.setMember(true);
		eligibilityInfo.setAccountNumber(purchaseRequest.getAccountNumber());
		eligibilityInfo.setDenomination(denomination);
		eligibilityInfo.setSubOffer(subOffer);
		eligibilityInfo.setAdditionalDetails(additionalDetails);
		eligibilityInfo.setConversionRateList(conversionRateList);
		eligibilityInfo.setOffer(offerDetails);
		eligibilityInfo.setOfferList(Arrays.asList(offerDetails));
		eligibilityInfo.setAmountInfo(new AmountInfo(0.0,0, 0.0, 0, 0.0, 0.0, 0.0, 0.0));
				
		return eligibilityInfo;
	}
	
	/**
	 * 
	 * @param birthdayInfo
	 * @param dob
	 * @return birthdayDurationInfoDto object
	 */
	public static BirthdayDurationInfoDto getBirthdayDurationInfoDto(BirthdayInfo birthdayInfo, GetMemberResponse memberDetails) {
		
		BirthdayDurationInfoDto birthdayDurationInfoDto = new BirthdayDurationInfoDto();
		
		if(!ObjectUtils.isEmpty(memberDetails)
		&& !ObjectUtils.isEmpty(memberDetails.getDob())) {
			
			birthdayDurationInfoDto = new BirthdayDurationInfoDto();
			
			Date dob = Utilities.setCurrentYear(memberDetails.getDob());		
			birthdayDurationInfoDto.setDob(dob);
			
			Integer minusDuration = !ObjectUtils.isEmpty(birthdayInfo)? Utilities.getNegativeIntegerValue(birthdayInfo.getThresholdMinusValue()) : 0;
			Integer plusDuration = !ObjectUtils.isEmpty(birthdayInfo)? birthdayInfo.getThresholdPlusValue(): 0;
			
		    Date startDate = Utilities.getDateFromSpecificDate(minusDuration, dob); 
			startDate = Utilities.setTimeInDate(startDate, OfferConstants.FROM_DATE_TIME.get());
			birthdayDurationInfoDto.setStartDate(startDate);
			
		    Date endDate = Utilities.getDateFromSpecificDate(plusDuration, dob); 
		    endDate = Utilities.setTimeInDate(endDate, OfferConstants.END_DATE_TIME.get());
		    birthdayDurationInfoDto.setEndDate(endDate);
		    
		    Date lastYearBirthday = Utilities.getDateFromSpecificDateDurationYear(-1, dob);
			birthdayDurationInfoDto.setLastYearDob(lastYearBirthday);
			
			birthdayDurationInfoDto.setPurchaseLimit(!ObjectUtils.isEmpty(birthdayInfo)? birthdayInfo.getPurchaseLimit() : null);
			 
		}
	    
	    return birthdayDurationInfoDto;
		
	}

	/**
	 * 
	 * @param birthdayInfo
	 * @return birthdayDurationInfoDto object specific to birthday alerts
	 */
	public static BirthdayDurationInfoDto getBirthdayDurationInfoForAlerts(BirthdayInfo birthdayInfo) {
		
		BirthdayDurationInfoDto birthdayDurationInfoDto = new BirthdayDurationInfoDto();
		Integer minusDuration = !ObjectUtils.isEmpty(birthdayInfo)? birthdayInfo.getThresholdMinusValue() : 0;
		Integer plusDuration = !ObjectUtils.isEmpty(birthdayInfo)? birthdayInfo.getThresholdPlusValue(): 0;
		birthdayDurationInfoDto.setStartDate(Utilities.setTimeInDate(new Date(), OfferConstants.FROM_DATE_TIME.get()));
		Date endDate = Utilities.getDateFromSpecificDate(minusDuration + plusDuration, birthdayDurationInfoDto.getStartDate());
		birthdayDurationInfoDto.setEndDate(Utilities.setTimeInDate(endDate, OfferConstants.END_DATE_TIME.get()));
		birthdayDurationInfoDto.setPurchaseLimit(!ObjectUtils.isEmpty(birthdayInfo)? birthdayInfo.getPurchaseLimit() : null);
		return birthdayDurationInfoDto;
	}

	/**
	 * 
	 * @param uiLanguage
	 * @return language of notification to be sent
	 */
	public static String getNotificationLanguage(String uiLanguage) {
		
		return StringUtils.equalsIgnoreCase(uiLanguage, OfferConstants.ENGLISH.get())? OffersConfigurationConstants.LANGUAGE_EN : OffersConfigurationConstants.LANGUAGE_AR;
	}

	
	/**
	 * 
	 * @param eligibilityInfo
	 * @return weekly purchase count for cinema offers
	 */
	public static PurchaseCount getPurchaseCountForCinemaOffers(EligibilityInfo eligibilityInfo) {
		
		PurchaseCount purchaseCount = new PurchaseCount(0,0,0);
		
		if(!ObjectUtils.isEmpty(eligibilityInfo)) {
			
			List<String> cinemaOfferIdList = MapValues.mapOfferIdList(eligibilityInfo.getOfferList(), Predicates.isCinemaOffer());
			List<OfferCounter> offerCounterList = FilterValues.filterOfferCounters(eligibilityInfo.getOfferCounterList(), Predicates.offerIdInlistOrCinemaOfferCounter(cinemaOfferIdList));
			
			if(!CollectionUtils.isEmpty(cinemaOfferIdList) && !CollectionUtils.isEmpty(offerCounterList)) {
			
				for(OfferCounter counter : offerCounterList) {
					
					if(!ObjectUtils.isEmpty(counter.getWeeklyCount())) {
						
						purchaseCount.setGlobalWeekly(purchaseCount.getGlobalWeekly() + counter.getWeeklyCount());
					}
					
					setAccountAndMemberLevelCinemaOfferCount(counter, purchaseCount, eligibilityInfo.getMemberDetails());
					
				}
				
			}
		}
		
		String log = Logs.logForVariable(OfferConstants.PURCHASE_COUNT_VARIABLE.get(), purchaseCount);
	    LOG.info(log);
		return purchaseCount;
		  
	}
	
	/**
	 * 
	 * @param eligibilityInfo
	 * @return weekly purchase count for cinema offers in eligible offers
	 */
	public static PurchaseCount getPurchaseCountForCinemaOffersInEligibleOffers(EligibleOfferHelperDto eligibilityInfo) {
		
		PurchaseCount purchaseCount = new PurchaseCount(0,0,0);
		
		if(!ObjectUtils.isEmpty(eligibilityInfo)) {
			
			List<String> cinemaOfferIdList = MapValues.mapEligibleOfferIdList(eligibilityInfo.getOfferList(), Predicates.isCinemaOfferEligibleOffer());
			List<OfferCounter> offerCounterList = FilterValues.filterOfferCounters(eligibilityInfo.getOfferCounterList(), Predicates.offerIdInlistOrCinemaOfferCounter(cinemaOfferIdList));
			
			if(!CollectionUtils.isEmpty(cinemaOfferIdList) && !CollectionUtils.isEmpty(offerCounterList)) {
			
				for(OfferCounter counter : offerCounterList) {
					
					if(!ObjectUtils.isEmpty(counter.getWeeklyCount())) {
						
						purchaseCount.setGlobalWeekly(purchaseCount.getGlobalWeekly() + counter.getWeeklyCount());
					}
					
					setAccountAndMemberLevelCinemaOfferCount(counter, purchaseCount, eligibilityInfo.getMemberDetails());
					
				}
				
			}
		}
		
		String log = Logs.logForVariable(OfferConstants.PURCHASE_COUNT_VARIABLE.get(), purchaseCount);
	    LOG.info(log);
		return purchaseCount;
		  
	}

	/**
	 * Sets account and membership level purchase count for cinema offers 
	 * @param counter
	 * @param purchaseCount
	 * @param memberDetails
	 */
	private static void setAccountAndMemberLevelCinemaOfferCount(OfferCounter counter, PurchaseCount purchaseCount,
			GetMemberResponse memberDetails) {
		
		if(!ObjectUtils.isEmpty(memberDetails) 
		&& !ObjectUtils.isEmpty(counter)
		&& !ObjectUtils.isEmpty(purchaseCount)) {
			
			AccountOfferCount accountOfferCount = FilterValues.findAccountOfferCountInOfferAccountCounterList(counter.getAccountOfferCount(), Predicates.sameAccountNumberAndMembershipCodeForCounter(memberDetails.getAccountNumber(), memberDetails.getMembershipCode()));
			
			if(!ObjectUtils.isEmpty(accountOfferCount) 
			&& !ObjectUtils.isEmpty(accountOfferCount.getWeeklyCount())) {
				
				purchaseCount.setAccountWeekly(purchaseCount.getAccountWeekly() + accountOfferCount.getWeeklyCount());
			}
			
			MemberOfferCount memberOfferCount = FilterValues.findMemberOfferCountInOfferMemberCounterList(counter.getMemberOfferCount(), Predicates.sameMembershipCodeForCounter(memberDetails.getMembershipCode()));
			
			if(!ObjectUtils.isEmpty(memberOfferCount) 
			&& !ObjectUtils.isEmpty(memberOfferCount.getWeeklyCount())) {
				
				purchaseCount.setMemberWeekly(purchaseCount.getMemberWeekly() + memberOfferCount.getWeeklyCount());
			}
			
		}
		
	}

	/**
	 * 
	 * @param originalDate
	 * @return date with UTC time and current year set
	 * @throws ParseException 
	 */
	public static Date setUtcTimeandCurrentYear(Date originalDate) throws ParseException {
		
		Date convertedDate = null;
		
		if(!ObjectUtils.isEmpty(originalDate)) {
			
			convertedDate = Utilities.setUtcTimeZone(originalDate,OfferConstants.TRANSACTIONS_DATE_FORMAT.get());
			convertedDate = Utilities.setCurrentYear(convertedDate);
		}
		
		return convertedDate;
	}

	/**
	 * Sets th eoffer references for updating offer
	 * @param offerCatalogRequest 
	 * @param offerReference
	 * @param resultResponse 
	 * @param fetchedOffer
	 */
	public static void setOfferReferencesForUpdate(OfferCatalogDto offerCatalogRequest, OfferReferences offerReference, OfferCatalog existingOffer, ResultResponse resultResponse) {
		
		if(Utilities.isEqual(offerCatalogRequest.getCategoryId(), existingOffer.getCategory().getCategoryId())) {
			
			offerReference.setCategory(existingOffer.getCategory());
		}
		
		if(Utilities.isEqual(offerCatalogRequest.getSubCategoryId(), existingOffer.getSubCategory().getCategoryId())) {
			
			offerReference.setSubCategory(existingOffer.getSubCategory());
		}
		
		if(Utilities.isEqual(offerCatalogRequest.getMerchantCode(), existingOffer.getMerchant().getMerchantCode())) {
			
			OfferValidator.validateMerchant(offerReference, existingOffer.getMerchant(), offerCatalogRequest, resultResponse);
			
		}
				
		if(Checks.checkNoDifferentValues(MapValues.mapAllStoreCodes(existingOffer.getOfferStores()), offerCatalogRequest.getStoreCodes())) {
			
			 offerReference.setStore(existingOffer.getOfferStores());
		}
		
		if(Checks.checkNoDifferentValues(MapValues.mapAllDirhamValues(existingOffer.getDenominations()), offerCatalogRequest.getDenominations())) {
			
			 offerReference.setDenominations(existingOffer.getDenominations());
		}
		
		if(Checks.checkNoDifferentValues(getCombinedOfferPaymentIds(existingOffer), getPaymentMethodList(offerCatalogRequest.getPaymentMethods(), offerCatalogRequest.getAccrualPaymentMethods()))) {
			
			 offerReference.setPaymentMethods(getCombinedPaymentMethodsFromOffer(existingOffer));
		}
		
	}
	
	/**
	 * 
	 * @param existingOffer
	 * @return
	 */
	public static List<String> getCombinedOfferPaymentIds(OfferCatalog existingOffer) {
		
		List<String> combinedPaymentMethods = (!ObjectUtils.isEmpty(existingOffer.getAccrualDetails())
				&& !CollectionUtils.isEmpty(existingOffer.getAccrualDetails().getAccrualPaymentMethods()))
				|| !CollectionUtils.isEmpty(existingOffer.getPaymentMethods())
				 ? new ArrayList<>(1)
				 : null;
		
		if(!ObjectUtils.isEmpty(existingOffer.getAccrualDetails())
		&& !CollectionUtils.isEmpty(existingOffer.getAccrualDetails().getAccrualPaymentMethods()))	{
			
			combinedPaymentMethods.addAll(MapValues.mapAllPaymentMethodIds(existingOffer.getAccrualDetails().getAccrualPaymentMethods()));
		}	
		
		if(!CollectionUtils.isEmpty(existingOffer.getPaymentMethods())) {
			
			combinedPaymentMethods.addAll(MapValues.mapAllPaymentMethodIds(existingOffer.getPaymentMethods()));
			
		}
		
		return combinedPaymentMethods;
	}

	/**
	 * 
	 * @param existingOffer
	 * @return
	 */
	public static List<PaymentMethod> getCombinedPaymentMethodsFromOffer(OfferCatalog existingOffer) {
		
		List<PaymentMethod> combinedPaymentMethods = (!ObjectUtils.isEmpty(existingOffer.getAccrualDetails())
				&& !CollectionUtils.isEmpty(existingOffer.getAccrualDetails().getAccrualPaymentMethods()))
				|| !CollectionUtils.isEmpty(existingOffer.getPaymentMethods())
				 ? new ArrayList<>(1)
				 : null;
		
		if(!ObjectUtils.isEmpty(existingOffer.getAccrualDetails())
		&& !CollectionUtils.isEmpty(existingOffer.getAccrualDetails().getAccrualPaymentMethods()))	{
			
			combinedPaymentMethods.addAll(existingOffer.getAccrualDetails().getAccrualPaymentMethods());
		}	
		
		if(!CollectionUtils.isEmpty(existingOffer.getPaymentMethods())) {
			
			combinedPaymentMethods.addAll(existingOffer.getPaymentMethods());
			
		}
		
		return combinedPaymentMethods;
	}


	/**
	 * Sets existing offer values to update offer request
	 * @param fetchedOffer
	 * @param offerCatalogRequest
	 */
	public static void setUpdateParameters(OfferCatalog fetchedOffer, OfferCatalogDto offerCatalogRequest) {
		
		 offerCatalogRequest.setStatus(fetchedOffer.getStatus());
		 
		 if(!ObjectUtils.isEmpty(fetchedOffer.getActivityCode())) {
			 
			 if(!ObjectUtils.isEmpty(fetchedOffer.getActivityCode().getRedemptionActivityCode())) {
				 
				 offerCatalogRequest.setRedemptionId(fetchedOffer.getActivityCode().getRedemptionActivityCode().getActivityId());
				 offerCatalogRequest.setRedemptionActivityCode(fetchedOffer.getActivityCode().getRedemptionActivityCode().getActivityCode());

				 if(!ObjectUtils.isEmpty(fetchedOffer.getActivityCode().getRedemptionActivityCode().getActivityCodeDescription())) {
					 
					 offerCatalogRequest.setRedemptionCodeDescriptionEn(fetchedOffer.getActivityCode().getRedemptionActivityCode().getActivityCodeDescription().getActivityCodeDescriptionEn());
					 offerCatalogRequest.setRedemptionCodeDescriptionAr(fetchedOffer.getActivityCode().getRedemptionActivityCode().getActivityCodeDescription().getActivityCodeDescriptionAr());
					 
				 }
				 
			 }
			 
			 if(!ObjectUtils.isEmpty(fetchedOffer.getActivityCode().getAccrualActivityCode())) {
				 
				 offerCatalogRequest.setAccrualId(fetchedOffer.getActivityCode().getAccrualActivityCode().getActivityId());
				 offerCatalogRequest.setAccrualActivityCode(fetchedOffer.getActivityCode().getAccrualActivityCode().getActivityCode());
				
				 if(!ObjectUtils.isEmpty(fetchedOffer.getActivityCode().getAccrualActivityCode().getActivityCodeDescription())) {
					 
					 offerCatalogRequest.setAccrualCodeDescriptionEn(fetchedOffer.getActivityCode().getAccrualActivityCode().getActivityCodeDescription().getActivityCodeDescriptionEn());
					 offerCatalogRequest.setAccrualCodeDescriptionAr(fetchedOffer.getActivityCode().getAccrualActivityCode().getActivityCodeDescription().getActivityCodeDescriptionAr());
					 
				 }
				 
			 }
			 
			 if(!ObjectUtils.isEmpty(fetchedOffer.getActivityCode().getPointsAccrualActivityCode())) {
				 
				 offerCatalogRequest.setPointsAccrualId(fetchedOffer.getActivityCode().getPointsAccrualActivityCode().getActivityId());
				 offerCatalogRequest.setPointsAccrualActivityCode(fetchedOffer.getActivityCode().getPointsAccrualActivityCode().getActivityCode());
				
				 if(!ObjectUtils.isEmpty(fetchedOffer.getActivityCode().getPointsAccrualActivityCode().getActivityCodeDescription())) {
					 
					 offerCatalogRequest.setPointsAccrualCodeDescriptionEn(fetchedOffer.getActivityCode().getPointsAccrualActivityCode().getActivityCodeDescription().getActivityCodeDescriptionEn());
					 offerCatalogRequest.setPointsAccrualCodeDescriptionAr(fetchedOffer.getActivityCode().getPointsAccrualActivityCode().getActivityCodeDescription().getActivityCodeDescriptionAr());
					 
				 }
				 
			}
			 
		 }
		 
	}

	/**
	 * 
	 * @param eligibilityInfo
	 * @param eligibleOffersRequest 
	 * @param offerCatalogResultResponse 
	 * @return filtered eligible offers for members
	 */
	public static boolean filterEligibleOffers(EligibilityInfo eligibilityInfo, EligibleOffersFiltersRequest eligibleOffersRequest, OfferCatalogResultResponse offerCatalogResultResponse) {
		
		 if(!ObjectUtils.isEmpty(eligibilityInfo) && !CollectionUtils.isEmpty(eligibilityInfo.getOfferList())) {
			 
			  eligibilityInfo.setOfferList(FilterValues.filterOfferList(eligibilityInfo.getOfferList(), Predicates.activeMerchantAndStore()));
			  
			  if(eligibilityInfo.isMember()) {
				  
				  eligibilityInfo.setOfferList(FilterValues.filterOfferList(eligibilityInfo.getOfferList(), Predicates.validCustomerType(eligibilityInfo.getMemberDetails().getCustomerType())));
				  eligibilityInfo.setOfferList(FilterValues.filterOfferList(eligibilityInfo.getOfferList(), Predicates.validCustomerSegment(eligibilityInfo.getMemberDetails().getCustomerSegment(), eligibilityInfo.getRuleResult())));
			      
			  }
			  
			  if(!StringUtils.isEmpty(eligibleOffersRequest.getMerchantCode())) {
				  
				  eligibilityInfo.setOfferList(FilterValues.filterOfferList(eligibilityInfo.getOfferList(), Predicates.sameMerchantForOffer(eligibleOffersRequest.getMerchantCode())));
				  
			  }
			  
			  if(!StringUtils.isEmpty(eligibleOffersRequest.getCategoryId())) {
				  
				  eligibilityInfo.setOfferList(FilterValues.filterOfferList(eligibilityInfo.getOfferList(), Predicates.sameCategoryForOffer(eligibleOffersRequest.getCategoryId())));
				  
			  }
			  
			  if(!CollectionUtils.isEmpty(eligibleOffersRequest.getSubCategoryList())) {
				  
				  eligibilityInfo.setOfferList(FilterValues.filterOfferList(eligibilityInfo.getOfferList(), Predicates.sameSubCategoryForOffer(eligibleOffersRequest.getSubCategoryList())));
				  
			  }
			  
			  applyInputFiltersForEligibleOffers(eligibilityInfo, eligibleOffersRequest);
			    
		}
		 
		 return Responses.setResponseAfterConditionCheck(CollectionUtils.isNotEmpty(eligibilityInfo.getOfferList()), OfferErrorCodes.NO_OFFERS_FOR_MEMBER_TO_DISPLAY, offerCatalogResultResponse);
	}

	/**
	 * Filters eligible offers as per the input request
	 * @param eligibilityInfo
	 * @param eligibleOffersRequest
	 */
	private static void applyInputFiltersForEligibleOffers(EligibilityInfo eligibilityInfo,
			EligibleOffersFiltersRequest eligibleOffersRequest) {
		
		if(!ObjectUtils.isEmpty(eligibilityInfo) && !CollectionUtils.isEmpty(eligibilityInfo.getOfferList())) {
			
			if(!ObjectUtils.isEmpty(eligibleOffersRequest.getKeywords())) {
				  
				  eligibilityInfo.setOfferList(FilterValues.filterOfferList(eligibilityInfo.getOfferList(), Predicates.anyValuePresentInTags(Arrays.asList(eligibleOffersRequest.getKeywords()))));
				  
			  }
			  
			  if(!ObjectUtils.isEmpty(eligibleOffersRequest.getEmirate())) {
				  
				  eligibilityInfo.setOfferList(FilterValues.filterOfferList(eligibilityInfo.getOfferList(), Predicates.anyValuePresentInTags(Arrays.asList(eligibleOffersRequest.getEmirate()))));
				  
			  }
			  
			  if(!CollectionUtils.isEmpty(eligibleOffersRequest.getAreas())) {
				  
				  eligibilityInfo.setOfferList(FilterValues.filterOfferList(eligibilityInfo.getOfferList(), Predicates.anyValuePresentInTags(eligibleOffersRequest.getAreas())));
				  
			  }

			  if(eligibleOffersRequest.isNewOffer()) {
				  
				  eligibilityInfo.setOfferList(FilterValues.filterOfferList(eligibilityInfo.getOfferList(), Predicates.isNewOffer()));
				  
			  }
			  
			  if(eligibleOffersRequest.isGrouped()) {
				  
				  eligibilityInfo.setOfferList(FilterValues.filterOfferList(eligibilityInfo.getOfferList(), Predicates.groupedFlagSet()));
				  
			  }

			
			
		}
		
	}

	/**
	 * Apply pagination to eligible offers request
	 * @param eligibleOffersRequest
	 * @param offerCatalogResultResponse
	 */
	public static void applyPagination(EligibleOffersFiltersRequest eligibleOffersRequest,
			OfferCatalogResultResponse offerCatalogResultResponse) {
		
		if(!ObjectUtils.isEmpty(offerCatalogResultResponse) 
		&& !CollectionUtils.isEmpty(offerCatalogResultResponse.getOfferCatalogs())
		&& !ObjectUtils.isEmpty(eligibleOffersRequest.getPage()) 
		&& !ObjectUtils.isEmpty(eligibleOffersRequest.getPageLimit())) {
			
			offerCatalogResultResponse.setOfferCatalogs(offerCatalogResultResponse.getOfferCatalogs().stream()
					.skip((long)(eligibleOffersRequest.getPage()*eligibleOffersRequest.getPageLimit()))
					.limit(eligibleOffersRequest.getPageLimit())
					.collect(Collectors.toList()));
		}
		
	}
	
	
	/**
	 * 
	 * @param eligibilityInfo
	 * @param eligibleOffersRequest 
	 * @param offerCatalogResultResponse 
	 * @return filtered eligible offers for members
	 */
	public static boolean filterAllEligibleOffers(EligibleOfferHelperDto eligibilityInfo, EligibleOffersFiltersRequest eligibleOffersRequest, OfferCatalogResultResponse offerCatalogResultResponse) {
		
		 if(!ObjectUtils.isEmpty(eligibilityInfo) && !CollectionUtils.isEmpty(eligibilityInfo.getOfferList())) {
			 
			 eligibilityInfo.setOfferList(FilterValues.filterEligibleOfferList(eligibilityInfo.getOfferList(), Predicates.availableInChannelId(eligibilityInfo.getHeaders().getChannelId())));
			 
			  if(eligibilityInfo.isMember()) {
				  
				  eligibilityInfo.setOfferList(FilterValues.filterEligibleOfferList(eligibilityInfo.getOfferList(), Predicates.validCustomerTypeForEligibleOffers (eligibilityInfo.getMemberDetails().getCustomerType())));
				  eligibilityInfo.setOfferList(FilterValues.filterEligibleOfferList(eligibilityInfo.getOfferList(), Predicates.validCustomerSegmentForEligibleOffers(eligibilityInfo.getMemberDetails().getCustomerSegment(), eligibilityInfo.getRuleResult())));
			      
			  }
			  
			  if(!StringUtils.isEmpty(eligibleOffersRequest.getMerchantCode())) {
				  
				  eligibilityInfo.setOfferList(FilterValues.filterEligibleOfferList(eligibilityInfo.getOfferList(), Predicates.sameMerchantForEligibleOffers(eligibleOffersRequest.getMerchantCode())));
				  
			  }
			  
			  if(!StringUtils.isEmpty(eligibleOffersRequest.getCategoryId())) {
				  
				  eligibilityInfo.setOfferList(FilterValues.filterEligibleOfferList(eligibilityInfo.getOfferList(), Predicates.sameCategoryForEligibleOffers(eligibleOffersRequest.getCategoryId())));
				  
			  }
			  
			  if(!CollectionUtils.isEmpty(eligibleOffersRequest.getSubCategoryList())) {
				  
				  eligibilityInfo.setOfferList(FilterValues.filterEligibleOfferList(eligibilityInfo.getOfferList(), Predicates.sameSubCategoryForEligibleOffers(eligibleOffersRequest.getSubCategoryList())));
				  
			  }
			  
			  applyInputFiltersForAllEligibleOffers(eligibilityInfo, eligibleOffersRequest);
			    
		}
		 
		 return Responses.setResponseAfterConditionCheck(CollectionUtils.isNotEmpty(eligibilityInfo.getOfferList()), OfferErrorCodes.NO_OFFERS_FOR_MEMBER_TO_DISPLAY, offerCatalogResultResponse);
	}
	
	/**
	 * Filters eligible offers as per the input request
	 * @param eligibilityInfo
	 * @param eligibleOffersRequest
	 */
	private static void applyInputFiltersForAllEligibleOffers(EligibleOfferHelperDto eligibilityInfo,
			EligibleOffersFiltersRequest eligibleOffersRequest) {
		
		if(!ObjectUtils.isEmpty(eligibilityInfo) && !CollectionUtils.isEmpty(eligibilityInfo.getOfferList())) {
			
			 if(!StringUtils.isEmpty(eligibleOffersRequest.getOfferType())) {
				 
				 eligibilityInfo.setOfferList(FilterValues.filterEligibleOfferList(eligibilityInfo.getOfferList(), Predicates.sameOfferTypeInEligibleOffer(eligibleOffersRequest.getOfferType())));
			 }
			
			 if(!StringUtils.isEmpty(eligibleOffersRequest.getKeywords())) {
				  
				  eligibilityInfo.setOfferList(FilterValues.filterEligibleOfferList(eligibilityInfo.getOfferList(), Predicates.anyValuePresentInTagsForEligibleOffers(Arrays.asList(eligibleOffersRequest.getKeywords()))));
				  
			  }
			  
			  if(!StringUtils.isEmpty(eligibleOffersRequest.getEmirate())) {
				  
				  eligibilityInfo.setOfferList(FilterValues.filterEligibleOfferList(eligibilityInfo.getOfferList(), Predicates.anyValuePresentInTagsForEligibleOffers(Arrays.asList(eligibleOffersRequest.getEmirate()))));
				  
			  }
			  
			  if(!CollectionUtils.isEmpty(eligibleOffersRequest.getAreas())) {
				  
				  eligibilityInfo.setOfferList(FilterValues.filterEligibleOfferList(eligibilityInfo.getOfferList(), Predicates.anyValuePresentInTagsForEligibleOffers(eligibleOffersRequest.getAreas())));
				  
			  }

			  if(eligibleOffersRequest.isNewOffer()) {
				  
				  eligibilityInfo.setOfferList(FilterValues.filterEligibleOfferList(eligibilityInfo.getOfferList(), Predicates.isNewOfferForEligibleOffers()));
				  
			  }
			  
			  if(eligibleOffersRequest.isGrouped()) {
				  
				  eligibilityInfo.setOfferList(FilterValues.filterEligibleOfferList(eligibilityInfo.getOfferList(), Predicates.groupedFlagSetForEligibleOffers()));
				  
			  }

		}
		
	}
	
	
	
	/**
     * Sets unmapped values in offer response
     * @param offerCatalogDto
     * @param offerCatalog
     * @param paymentMethods
     * @param conversionRateList
     * @param imageList
     */
    public static void setUnmappedValuesForEligibleOffers(OfferCatalogResultResponseDto offerCatalogDto, EligibleOffers offerCatalog,
			List<ConversionRate> conversionRateList) {
		
    	setOfferCommonValuesForEligibleOffer(offerCatalogDto, offerCatalog, conversionRateList);
		setDealVoucherValuesForEligibleOffer(offerCatalogDto, offerCatalog, conversionRateList);
		setEtisalatAddOnValuesForEligibleOffer(offerCatalogDto, offerCatalog);
		setOfferReferenceValuesForEligibleOffer(offerCatalogDto, offerCatalog, conversionRateList);
			
	}
    
    /**
     * Setting common values in offer response for all offer types 
     * @param offerCatalogDto
     * @param offerCatalog
     * @param conversionRateList
     */
	private static void setOfferCommonValuesForEligibleOffer(OfferCatalogResultResponseDto offerCatalogDto, EligibleOffers offerCatalog, List<ConversionRate> conversionRateList) {
		
       if(!ObjectUtils.isEmpty(offerCatalog.getOfferType())) {
			
			offerCatalogDto.setOfferTypeId(offerCatalog.getOfferType().getOfferTypeId());
        	
			if(!ObjectUtils.isEmpty(offerCatalog.getOfferType().getOfferDescription())) {
				
				offerCatalogDto.setTypeDescriptionEn(offerCatalog.getOfferType().getOfferDescription().getTypeDescriptionEn());
				offerCatalogDto.setTypeDescriptionAr(offerCatalog.getOfferType().getOfferDescription().getTypeDescriptionAr());
			}
            
        }
		
		if(!ObjectUtils.isEmpty(offerCatalog.getOfferValues())) {
			
			offerCatalogDto.setCost(offerCatalog.getOfferValues().getCost());
        	offerCatalogDto.setPointsValue(!ObjectUtils.isEmpty(offerCatalog.getOfferValues().getPointsValue())
        			? offerCatalog.getOfferValues().getPointsValue()
        			: ProcessValues.getEquivalentPoints(conversionRateList, offerCatalog.getOfferValues().getCost()));
            
        }
        
        if(CollectionUtils.isNotEmpty(offerCatalog.getLimit())
        && CollectionUtils.isNotEmpty(offerCatalogDto.getOfferLimit())) {
        	
        	for(LimitResponseDto limitDto : offerCatalogDto.getOfferLimit()) {
        		
        		limitDto.setCouponQuantity(Utilities.getIntegerValueOrNull(ProcessValues.getCouponQuantity(limitDto, offerCatalog.getLimit())));		
        		
        	}
        	
        }
        
        if(!ObjectUtils.isEmpty(offerCatalog.getOfferRating())) {
			   
			offerCatalogDto.setOfferRating(offerCatalog.getOfferRating().getId());
		}
        
        if(!ObjectUtils.isEmpty(offerCatalog.getEarnMultiplier())) {
			   
			offerCatalogDto.setEarnMultiplier(offerCatalog.getEarnMultiplier());
		}
        
        if(!ObjectUtils.isEmpty(offerCatalog.getAccrualDetails())
        && !ObjectUtils.isEmpty(offerCatalog.getAccrualDetails().getPointsEarnMultiplier())) {
			   
			offerCatalogDto.setPointsEarnMultiplier(offerCatalog.getAccrualDetails().getPointsEarnMultiplier());
		}
        
        if(!ObjectUtils.isEmpty(offerCatalog.getGiftInfo())) {
        	
        	offerCatalogDto.setIsGift(offerCatalog.getGiftInfo().getIsGift());
        	offerCatalogDto.setGiftChannels(offerCatalog.getGiftInfo().getGiftChannels());
        	offerCatalogDto.setGiftSubCardTypes(offerCatalog.getGiftInfo().getGiftSubCardTypes());
        }
        
        if(!ObjectUtils.isEmpty(offerCatalog.getFreeOffers())) {
        	offerCatalogDto.setMamba(offerCatalog.getFreeOffers().isMamba());
			offerCatalogDto.setPromotionalGift(offerCatalog.getFreeOffers().isPromotionalGift());
			offerCatalogDto.setPointsRedemptionGift(offerCatalog.getFreeOffers().isPointsRedemptionGift());
        }
        
        offerCatalogDto.setEligibility(new Eligibility(true, null));
        setVoucherInfoDetailsInEligibleOfferResponse(offerCatalog, offerCatalogDto);
        setActivityCodeDetailsForEligibleOffer(offerCatalog, offerCatalogDto);
        
	}
	
	/**
	 * Sets voucher info details in eligible offer response
	 * @param offerCatalog
	 * @param offerCatalogDto
	 */
	private static void setVoucherInfoDetailsInEligibleOfferResponse(EligibleOffers offerCatalog,
			OfferCatalogResultResponseDto offerCatalogDto) {
		
		if(!ObjectUtils.isEmpty(offerCatalog.getVoucherInfo())) {
        	
        	offerCatalogDto.setVoucherExpiryDate(offerCatalog.getVoucherInfo().getVoucherExpiryDate());
        	offerCatalogDto.setPartnerRedeemURL(offerCatalog.getVoucherInfo().getPartnerRedeemURL());
        	
        	if(!ObjectUtils.isEmpty(offerCatalog.getVoucherInfo().getInstructionsToRedeemTitle())) {
        		
        		offerCatalogDto.setInstructionsToRedeemTitleEn(offerCatalog.getVoucherInfo().getInstructionsToRedeemTitle().getInstructionsToRedeemTitleEn());
        		offerCatalogDto.setInstructionsToRedeemTitleAr(offerCatalog.getVoucherInfo().getInstructionsToRedeemTitle().getInstructionsToRedeemTitleAr());
        	}
        	
        	if(!ObjectUtils.isEmpty(offerCatalog.getVoucherInfo().getInstructionsToRedeem())) {
        		
        		offerCatalogDto.setInstructionsToRedeemEn(offerCatalog.getVoucherInfo().getInstructionsToRedeem().getInstructionsToRedeemEn());
        		offerCatalogDto.setInstructionsToRedeemAr(offerCatalog.getVoucherInfo().getInstructionsToRedeem().getInstructionsToRedeemAr());
        	}
        }
		
	}
	
	/**
	 * Setting activity code details in offer response
	 * @param offerCatalog
	 * @param offerCatalogDto
	 */
	private static void setActivityCodeDetailsForEligibleOffer(EligibleOffers offerCatalog,
			OfferCatalogResultResponseDto offerCatalogDto) {
		
		if(!ObjectUtils.isEmpty(offerCatalog.getActivityCode())) {
        	
        	if(!ObjectUtils.isEmpty(offerCatalog.getActivityCode().getRedemptionActivityCode())) {
        		
        		offerCatalogDto.setRedActivityCd(offerCatalog.getActivityCode().getRedemptionActivityCode().getActivityCode());
        	    offerCatalogDto.setRedActivityId(offerCatalog.getActivityCode().getRedemptionActivityCode().getActivityId());
        		
                if(!ObjectUtils.isEmpty(offerCatalog.getActivityCode().getRedemptionActivityCode().getActivityCodeDescription())) {
                	
                	offerCatalogDto.setRedActivityCodeDescriptionEn(offerCatalog.getActivityCode().getRedemptionActivityCode().getActivityCodeDescription().getActivityCodeDescriptionEn());
            		offerCatalogDto.setRedActivityCodeDescriptionAr(offerCatalog.getActivityCode().getRedemptionActivityCode().getActivityCodeDescription().getActivityCodeDescriptionAr());
                	
                }  
        	    
        	}
        	
        	if(!ObjectUtils.isEmpty(offerCatalog.getActivityCode().getAccrualActivityCode())) {
        	
        		offerCatalogDto.setAccActivityCd(offerCatalog.getActivityCode().getAccrualActivityCode().getActivityCode());
        		offerCatalogDto.setAccActivityId(offerCatalog.getActivityCode().getAccrualActivityCode().getActivityId());
        		
                if(!ObjectUtils.isEmpty(offerCatalog.getActivityCode().getAccrualActivityCode().getActivityCodeDescription())) {
                	
                	offerCatalogDto.setAccActivityCodeDescriptionEn(offerCatalog.getActivityCode().getAccrualActivityCode().getActivityCodeDescription().getActivityCodeDescriptionEn());
            		offerCatalogDto.setAccActivityCodeDescriptionAr(offerCatalog.getActivityCode().getAccrualActivityCode().getActivityCodeDescription().getActivityCodeDescriptionAr());   
                	
                }
        		
        		
        	}
        	
        	if(!ObjectUtils.isEmpty(offerCatalog.getActivityCode().getPointsAccrualActivityCode())) {
            	
        		offerCatalogDto.setPointsAcrActivityCode(offerCatalog.getActivityCode().getPointsAccrualActivityCode().getActivityCode());
        		offerCatalogDto.setPointsAcrId(offerCatalog.getActivityCode().getPointsAccrualActivityCode().getActivityId());
        		
                if(!ObjectUtils.isEmpty(offerCatalog.getActivityCode().getPointsAccrualActivityCode().getActivityCodeDescription())) {
                	
                	offerCatalogDto.setPointsAcrCodeDescriptionEn(offerCatalog.getActivityCode().getPointsAccrualActivityCode().getActivityCodeDescription().getActivityCodeDescriptionEn());
            		offerCatalogDto.setPointsAcrCodeDescriptionAr(offerCatalog.getActivityCode().getPointsAccrualActivityCode().getActivityCodeDescription().getActivityCodeDescriptionAr());   
                	
                }
        		
        	}
        	
        }
		
	}

	/**
	 * Setting unmapped values for deal voucher in offer response
	 * @param offerCatalogDto
	 * @param offerCatalog
	 * @param conversionRateList
	 */
	private static void setDealVoucherValuesForEligibleOffer(OfferCatalogResultResponseDto offerCatalogDto, EligibleOffers offerCatalog, List<ConversionRate> conversionRateList) {
		
		if(Checks.checkIsDealVoucher(offerCatalog.getOfferType().getOfferTypeId())
		&& CollectionUtils.isNotEmpty(offerCatalog.getSubOffer())) {
				
				List<SubOfferDto> subOfferDtoList = new ArrayList<>(offerCatalog.getSubOffer().size());
 				
				for(SubOffer subOffer : offerCatalog.getSubOffer()) {
					
 					SubOfferDto subOfferDto = new SubOfferDto();
 					setSubOfferDto(subOffer, subOfferDto, conversionRateList);
 					subOfferDtoList.add(subOfferDto);
 					
				}
 				
 				offerCatalogDto.setSubOffer(subOfferDtoList);
				
		}
			
	}

	/**
	 * Setting unmapped values for etisalat add on in offer response
	 * @param offerCatalogDto
	 * @param offerCatalog
	 */
	private static void setEtisalatAddOnValuesForEligibleOffer(OfferCatalogResultResponseDto offerCatalogDto,
			EligibleOffers offerCatalog) {
		
		if(!ObjectUtils.isEmpty(offerCatalog.getProvisioningAttributes())){
			
            offerCatalogDto.setRatePlanCode(offerCatalog.getProvisioningAttributes().getRatePlanCode());
            offerCatalogDto.setRtfProductCode(offerCatalog.getProvisioningAttributes().getRtfProductCode());
            offerCatalogDto.setRtfProductType(offerCatalog.getProvisioningAttributes().getRtfProductType());
            offerCatalogDto.setVasCode(offerCatalog.getProvisioningAttributes().getVasCode());
            offerCatalogDto.setVasActionId(offerCatalog.getProvisioningAttributes().getVasActionId());
            offerCatalogDto.setPromotionalPeriod(offerCatalog.getProvisioningAttributes().getPromotionalPeriod());
            offerCatalogDto.setFeature(offerCatalog.getProvisioningAttributes().getFeature());
            offerCatalogDto.setServiceId(offerCatalog.getProvisioningAttributes().getServiceId());
            offerCatalogDto.setActivityIdRbt(offerCatalog.getProvisioningAttributes().getActivityId());
            offerCatalogDto.setPackName(offerCatalog.getProvisioningAttributes().getPackName());
            
        }
		
	}
	
	/**
	 * Setting all dbRef values in offer response
	 * @param offerCatalogDto
	 * @param offerCatalog
	 * @param paymentMethods
	 * @param conversionRateList
	 * @param imageList
	 */
	private static void setOfferReferenceValuesForEligibleOffer(OfferCatalogResultResponseDto offerCatalogDto,
			EligibleOffers offerCatalog, List<ConversionRate> conversionRateList) {
		
		setStoreDetailsForEligibleOffer(offerCatalogDto, offerCatalog);
		setCategoryDetailsForEligibleOffer(offerCatalogDto, offerCatalog);
		setRatingsForEligibleOffer(offerCatalog, offerCatalogDto);
		setDenominationDetailsForEligibleOffer(offerCatalogDto, offerCatalog, conversionRateList);
	}
	
	/**
	 * Set store details in offer response
	 * @param offerCatalogDto
	 * @param offerCatalog
	 */
	private static void setStoreDetailsForEligibleOffer(OfferCatalogResultResponseDto offerCatalogDto, EligibleOffers offerCatalog) {
		
		if(CollectionUtils.isNotEmpty(offerCatalogDto.getOfferStores())) {
			
			for(StoreDto storeDto : offerCatalogDto.getOfferStores()){
				
	            EligibleOfferStore store = FilterValues
	            		.findAnyEligibleOfferStoreInList(offerCatalog.getOfferStores(), Predicates.sameEligibleOfferStoreCode(storeDto.getStoreCode()));
	            
	            if(!ObjectUtils.isEmpty(store)) {
	                  
	            	List<String> mobileNumbers = null;
	                
	                if(CollectionUtils.isNotEmpty(store.getContactPersons())) {
	                	
	                	mobileNumbers = MapValues.mapAllEligibleOfferStoreContactNumbers(store, Predicates.nonEmptyMobileNumber());
	                	storeDto.setMobileNumber(mobileNumbers);
	                	
	                }
	                
	                List<String> storeCoordinates = null;
	                if(!StringUtils.isEmpty(store.getLatitude()) 
	                || !StringUtils.isEmpty(store.getLongitude())) {
	                	
	                	storeCoordinates = new ArrayList<>(2);
	                	storeCoordinates.add(store.getLatitude());
	                    storeCoordinates.add(store.getLongitude());
	                    storeDto.setStoreCoordinates(storeCoordinates);
	                    
	                }
	            	
	            }
	            
	        }
			
		}
		
	}
	
	/**
	 * Set denomination details in offer response
	 * @param offerCatalogDto
	 * @param offerCatalog
	 * @param conversionRateList
	 */
	private static void setDenominationDetailsForEligibleOffer(OfferCatalogResultResponseDto offerCatalogDto, EligibleOffers offerCatalog,
			List<ConversionRate> conversionRateList) {
		
		if(CollectionUtils.isNotEmpty(offerCatalog.getDenominations()) 
		&& CollectionUtils.isNotEmpty(offerCatalogDto.getDenominations())) {
			
	       for(DenominationDto denomination : offerCatalogDto.getDenominations()) {
	    	   
	    	   EligibleOfferDenomination  denom = FilterValues.findAnyEligibleOfferDenominationInDenominationList(offerCatalog.getDenominations(), Predicates.sameEligibleOfferDenominationIdForDenomination(denomination.getDenominationId()));
	    	   setEligibleOfferDenominationValues(denom, denomination, conversionRateList);
	    	   
	       }		
			
		}
		
	}
	
	/**
	 * Map denomination values in response
	 * @param offerCatalogDto
	 * @param denom
	 * @param denomination
	 * @param conversionRateList 
	 */
	private static void setEligibleOfferDenominationValues(EligibleOfferDenomination denom, DenominationDto denomination, List<ConversionRate> conversionRateList) {
		
		if(!ObjectUtils.isEmpty(denom)) {
 		   
 		   if(!ObjectUtils.isEmpty(denom.getDenominationValue())) {
	    		   
 			   denomination.setDirhamValue(denom.getDenominationValue().getDirhamValue());
	    		 
 			   if(ObjectUtils.isEmpty(denomination.getPointValue())) {
		    		   
		    		   denomination.setPointValue(getEquivalentPoints(conversionRateList, (double) denomination.getDirhamValue()));
		    		   
	    	   }
		   
    	   }
	    	   
 		   if(!ObjectUtils.isEmpty(denom.getDenominationDescription())) {
	    		   
 			   denomination.setDenominationDescriptionEn(denom.getDenominationDescription().getDenominationDescriptionEn());
	    	   denomination.setDenominationDescriptionAr(denom.getDenominationDescription().getDenominationDescriptionAr());
 			   
	      }
	    	   
 	   }
		
	}
	
	/**
	 * Set category details in offer response
	 * @param offerCatalogDto
	 * @param offerCatalog
	 */
    private static void setCategoryDetailsForEligibleOffer(OfferCatalogResultResponseDto offerCatalogDto, EligibleOffers offerCatalog) {
		
		if(!ObjectUtils.isEmpty(offerCatalog.getCategory())) {
	        
        	offerCatalogDto.setCategoryId(offerCatalog.getCategory().getCategoryId());
        	
        }

        if(!ObjectUtils.isEmpty(offerCatalog.getSubCategory())) {
        	
        	offerCatalogDto.setSubCategoryId(offerCatalog.getSubCategory().getCategoryId());
        	
        }
		
	}
	
    /**
     * Set offer rating details in offer details
     * @param offerCatalog
     * @param offerCatalogDto
     */
	private static void setRatingsForEligibleOffer(EligibleOffers offerCatalog, OfferCatalogResultResponseDto offerCatalogDto) {
		
		EligibleOfferRating offerRating = offerCatalog.getOfferRating();
		offerCatalogDto.setAverageRating(OffersConfigurationConstants.ZERO_DOUBLE);
		
		if(!ObjectUtils.isEmpty(offerRating)) {
			
			offerCatalogDto.setAverageRating(offerRating.getAverageRating());
			List<MemberRatingDto> memberRatingList = null;
			
			if(CollectionUtils.isNotEmpty(offerRating.getMemberRatings())) {
			
				memberRatingList = new ArrayList<>(offerRating.getMemberRatings().size());
				
				for(MemberRating rating : offerRating.getMemberRatings()) {
					
					MemberRatingDto memberRating = new MemberRatingDto();
					memberRating.setAccountNumber(rating.getAccountNumber());
					memberRating.setMembershipCode(rating.getMembershipCode());
					memberRating.setFirstName(rating.getFirstName());
					memberRating.setLastName(rating.getLastName());
					List<MemberCommentDto> memberCommentList = null;
				
					if(CollectionUtils.isNotEmpty(rating.getComments())) {
						
						memberCommentList = new ArrayList<>(rating.getComments().size());
												
						for(MemberComment comment : rating.getComments()) {
							
							MemberCommentDto memberComment = new MemberCommentDto();
							memberComment.setRating(comment.getRating());
							memberComment.setComment(comment.getComment());
							memberComment.setReviewDate(comment.getReviewDate());
							memberCommentList.add(memberComment);	
							
						}
						
					}
					
					memberRating.setComments(memberCommentList);
					memberRatingList.add(memberRating);
					
				}
				
				offerCatalogDto.setMemberRatings(memberRatingList);
			}
			
		}
		
	}
	
	/**
	 * 
	 * @param weeklyPurchaseCount 
	 * @param referenceDetails
	 * @param offerId
	 * @return eligibility status for offer response for eligible offer
	 */
	public static void getMemberEligibilityForEligibleOffer(PurchaseCount purchaseCount, 
			EligibleOfferHelperDto eligibilityInfo, ResultResponse resultResponse, 
			OfferCatalogResultResponseDto offerCatalogDto) {
		
	    if(!ObjectUtils.isEmpty(eligibilityInfo)) {
			
	    	if(ObjectUtils.isEmpty(offerCatalogDto.getEligibility())) {
	    		
	    		offerCatalogDto.setEligibility(new Eligibility());
	    		
	    	}
			
			if(Checks.checkCinemaOffer(eligibilityInfo.getOffer().getRules())) {
				
				offerCatalogDto.getEligibility().setFailureStatus(Checks.checkCustomerSegmentForCinemaOffers(eligibilityInfo.getRuleResult(),
						eligibilityInfo.getMemberDetails(), offerCatalogDto.getEligibility().getFailureStatus(), offerCatalogDto.getEligibility(),
						eligibilityInfo.getOffer().getCustomerSegments(), eligibilityInfo.getOffer().getRules(), resultResponse));
				Checks.checkCinemaOfferDownloadLimit(purchaseCount, eligibilityInfo.getMemberDetails().getCustomerSegment(), resultResponse);	
					
			} else {
				
				List<String> customerSegmentNames = Utilities.intersection(eligibilityInfo.getMemberDetails().getCustomerSegment(),
						MapValues.mapCustomerSegmentInOfferLimits(eligibilityInfo.getOffer().getLimit(), Predicates.isCustomerSegmentInLimits()));
				Checks.checkDownloadLimitLeftForEligibleOffer(eligibilityInfo.getOfferCounters(), 0, eligibilityInfo, 
						null, resultResponse, customerSegmentNames, offerCatalogDto);
				
			}
			
			offerCatalogDto.getEligibility().setFailureStatus(Responses.setErrorMessage(OfferConstants.DOWNLOAD_LIMIT.get(), 
					offerCatalogDto.getEligibility(), offerCatalogDto.getEligibility().getFailureStatus(), resultResponse));
			offerCatalogDto.getEligibility().setStatus(CollectionUtils.isEmpty(offerCatalogDto.getEligibility().getFailureStatus()));
			
		}
		        	
	}

	/**
	 * Sets payment method for for eligible offer
	 * @param offer
	 * @param purchasePaymentMethodList
	 */
	public static void setPaymentMethodsForEligibleOffer(EligibleOffers offer,
			List<PurchasePaymentMethod> purchasePaymentMethodList) {
		
		if(!ObjectUtils.isEmpty(offer) && !CollectionUtils.isEmpty(purchasePaymentMethodList)) {
			
			PurchasePaymentMethod purchasePaymentMethod = FilterValues.findAnyPurchasePaymentMethodWithinList(purchasePaymentMethodList, 
      						 Predicates.matchesPurchaseItem(ProcessValues.getPurchaseItemFromOfferType(offer.getOfferType().getOfferTypeId())));
      		//true for including offer payments as well for eligible payment methods
			offer.setPaymentMethods(ProcessValues.getPaymentMethodsForPurchases(purchasePaymentMethod, offer.getPaymentMethods(), true));
				
		}
		
	}

	/**
	 * Sets list of images for the offer
	 * @param offer
	 * @param imageList
	 */
	public static void setImageListForEligibleOffer(EligibleOffers offer, List<MarketplaceImage> imageList) {
		
		if(!ObjectUtils.isEmpty(offer) && !CollectionUtils.isEmpty(imageList)) {
			
			List<MarketplaceImage> filteredImageList = FilterValues.filterImageList(imageList, Predicates.sameDomainId(offer.getOfferId()));
	 		
			if(!CollectionUtils.isEmpty(filteredImageList)) {
		       
				offer.setImageUrlList(new ArrayList<>(filteredImageList.size()));
				
				for(MarketplaceImage image : filteredImageList) {
					
					MarketplaceImageUrl imageUrl = new MarketplaceImageUrl();
					imageUrl.setImageUrl(image.getImageUrl());
					imageUrl.setImageUrlDr(image.getImageUrlDr());
		           	imageUrl.setImageUrlProd(image.getImageUrlProd());
		           	
		           	if(!ObjectUtils.isEmpty(image.getMerchantOfferImage())){
		           		
		           		imageUrl.setAvailableInChannel(image.getMerchantOfferImage().getAvailableInChannel());
		           		imageUrl.setImageType(image.getMerchantOfferImage().getImageType());
		           		
		           	}
		           	
					offer.getImageUrlList().add(imageUrl);
					
				}
				
				
			}		
					
		}
		
	}

	/**
	 * 
	 * @param purchaseItem
	 * @return type for sending lifetime savings request to points bank
	 */
	public static String getLifetimeSavingsType(String item) {
		
		String type = null;
		
		if(Checks.checkIsCashVoucher(item)) {
			
			type = OfferConstants.CASH_VOUCHER_LIFETIME_SAVINGS.get(); 
			
		} else if(Checks.checkIsDiscountVoucher(item)){
			
			type = OfferConstants.DISCOUNT_VOUCHER_LIFETIME_SAVINGS.get();
			
		} else if(Checks.checkIsEtisalatAddon(item)
			|| Checks.checkIsBillPayment(item)
			|| Checks.checkIsRecharge(item)){
			
			type = OfferConstants.ETISALAT_SERVICES_LIFETIME_SAVINGS.get();
			
		} 
		
		return type;
	}

	/**
	 * 
	 * @param purchaseRequest
	 * @return subscription id for purchase domain
	 */
	public static String getSubscriptionId(PurchaseRequestDto purchaseRequest) {
		
		return Checks.checkIsSubscription(purchaseRequest.getSelectedPaymentItem())
				? Utilities.getStringValueOrNull(purchaseRequest.getSubscriptionCatalogId())
				: null;
	}

	/***
	 * 
	 * @param amountInfo
	 * @param selectedPaymentItem
	 * @param selectedOption
	 * @return the spent amount for a transaction
	 */
	public static Double getSpentAmount(AmountInfo amountInfo, Double vat, String purchaseItem, String paymentMethod, Integer couponQuantity) {
		
		Double amount = null;
		
		if(!Checks.checkIsTelcomType(purchaseItem)) {
			
			if(ObjectUtils.isEmpty(vat)) {
				
				vat=0.0;
			}
			
			amount = Checks.checkPaymentMethodFullPoints(paymentMethod)
					? (amountInfo.getCost()*couponQuantity) + (vat * amountInfo.getCost()*couponQuantity)
					: amountInfo.getPurchaseAmount();
			
		}
		
		return amount;
	}

	/**
	 * Sets free amount and points in response for free offers
	 * @param subscribed
	 * @param offerCatalog
	 * @param offerCatalogDto
	 * @param conversionRateList
	 */
	public static void setFreeOfferResponse(OfferCatalogResultResponseDto offerCatalogDto, EligibilityInfo eligibilityInfo, boolean subscriptionCheckRequired, List<ConversionRate> conversionRateList) {
		
		if(Checks.checkOfferIsFree(offerCatalogDto, eligibilityInfo.getMemberDetails(), subscriptionCheckRequired)){
			
			if(!ObjectUtils.isEmpty(eligibilityInfo.getOffer().getOfferValues())) {
				
				offerCatalogDto.setOriginalCost(eligibilityInfo.getOffer().getOfferValues().getCost());
				offerCatalogDto.setOriginalPointsValue(!ObjectUtils.isEmpty(eligibilityInfo.getOffer().getOfferValues().getPointsValue())
						? eligibilityInfo.getOffer().getOfferValues().getPointsValue()
						: getEquivalentPoints(conversionRateList, eligibilityInfo.getOffer().getOfferValues().getCost()));
				
			}
			
		    offerCatalogDto.setCost(OffersConfigurationConstants.ZERO_DOUBLE);
		    offerCatalogDto.setPointsValue(OffersConfigurationConstants.ZERO_INTEGER);
			
			if(!CollectionUtils.isEmpty(offerCatalogDto.getDenominations())) {
				
				for(DenominationDto denomination : offerCatalogDto.getDenominations()) {
					
					denomination.setDirhamValue(OffersConfigurationConstants.ZERO_INTEGER);
					denomination.setPointValue(OffersConfigurationConstants.ZERO_INTEGER);
				}
				
			}
			
		}
		
	}

	/**
	 * Sets free amount and points in eligible offer response for free offers
	 * @param eligibilityInfo
	 * @param offer
	 * @param offerCatalogDto
	 * @param conversionRateList
	 */
	public static void setFreeOfferResponseInEligibleOffer(EligibleOfferHelperDto eligibilityInfo,
			OfferCatalogResultResponseDto offerCatalogDto, List<ConversionRate> conversionRateList, boolean subscriptionCheckRequired) {
		
		if(Checks.checkOfferIsFree(offerCatalogDto, eligibilityInfo.getMemberDetails(), subscriptionCheckRequired)){
			
			if(!ObjectUtils.isEmpty(eligibilityInfo.getOffer().getOfferValues())) {
				
				offerCatalogDto.setOriginalCost(eligibilityInfo.getOffer().getOfferValues().getCost());
				offerCatalogDto.setOriginalPointsValue(!ObjectUtils.isEmpty(eligibilityInfo.getOffer().getOfferValues().getPointsValue())
						? eligibilityInfo.getOffer().getOfferValues().getPointsValue()
						: getEquivalentPoints(conversionRateList, eligibilityInfo.getOffer().getOfferValues().getCost()));
				
			}
			
		    offerCatalogDto.setCost(OffersConfigurationConstants.ZERO_DOUBLE);
		    offerCatalogDto.setPointsValue(OffersConfigurationConstants.ZERO_INTEGER);
			
			if(!CollectionUtils.isEmpty(offerCatalogDto.getDenominations())) {
				
				for(DenominationDto denomination : offerCatalogDto.getDenominations()) {
					
					denomination.setDirhamValue(OffersConfigurationConstants.ZERO_INTEGER);
					denomination.setPointValue(OffersConfigurationConstants.ZERO_INTEGER);
				}
				
			}
			
		}
		
	}
	
	/**
	 * Sets the values for purchase count for cinema offers at offer, account and member level
	 * @param offerCounterList
	 * @param memberOfferCounterList
	 * @param accountOfferCounterList
	 * @return
	 */
	public static PurchaseCount getValuesForPurchaseCount(List<OfferCounters> offerCounterList, List<MemberOfferCounts> memberOfferCounterList,
			           List<AccountOfferCounts> accountOfferCounterList) {
		
		PurchaseCount purchaseCount = new PurchaseCount(0,0,0);
		
		if(!CollectionUtils.isEmpty(offerCounterList)) {
		
			List<String> cinemaOfferIdList = MapValues.mapOfferIdFromOfferCounterList(offerCounterList, Predicates.isOfferCounterForCinemaOffer()); 
					
			setGlobalWeeklyValueInCinemaPurchaseCount(offerCounterList, cinemaOfferIdList, purchaseCount);
			setMemberWeeklyValueInCinemaPurchaseCount(memberOfferCounterList, cinemaOfferIdList, purchaseCount);
			setAccountWeeklyValueInCinemaPurchaseCount(accountOfferCounterList, cinemaOfferIdList, purchaseCount);
		}
		
		String log = Logs.logForVariable(OfferConstants.PURCHASE_COUNT_VARIABLE.get(), purchaseCount);
	    LOG.info(log);
		return purchaseCount;
		  
	}
	
	/**
	 * Sets value of cinema offer weekly limit at global level
	 * @param offerCounterList
	 * @param cinemaOfferIdList
	 * @param purchaseCount
	 */
	private static void setGlobalWeeklyValueInCinemaPurchaseCount(List<OfferCounters> offerCounterList,
			List<String> cinemaOfferIdList, PurchaseCount purchaseCount) {
		
		if(!CollectionUtils.isEmpty(cinemaOfferIdList)
		&& !CollectionUtils.isEmpty(offerCounterList)
		&& !ObjectUtils.isEmpty(purchaseCount)) {
			
			for(OfferCounters counter : offerCounterList) {
				
				if(CollectionUtils.containsAny(cinemaOfferIdList, Arrays.asList(counter.getOfferId()))
				&& !ObjectUtils.isEmpty(counter.getWeeklyCount())) {
					
					purchaseCount.setGlobalWeekly(purchaseCount.getGlobalWeekly() + counter.getWeeklyCount());
					
				}
				
			}
			
		}
		
	}

	/**
	 * Sets value of cinema offer weekly limit for membership
	 * @param memberOfferCounterList
	 * @param cinemaOfferIdList
	 * @param purchaseCount
	 */
	private static void setMemberWeeklyValueInCinemaPurchaseCount(List<MemberOfferCounts> memberOfferCounterList,
			List<String> cinemaOfferIdList, PurchaseCount purchaseCount) {
		
		if(!CollectionUtils.isEmpty(cinemaOfferIdList)
		&& !CollectionUtils.isEmpty(memberOfferCounterList)
		&& !ObjectUtils.isEmpty(purchaseCount)) {
			
			for(MemberOfferCounts counter : memberOfferCounterList) {
				
				if(CollectionUtils.containsAny(cinemaOfferIdList, Arrays.asList(counter.getOfferId()))
				&& !ObjectUtils.isEmpty(counter.getWeeklyCount())) {
					
					purchaseCount.setMemberWeekly(purchaseCount.getMemberWeekly() + counter.getWeeklyCount());
					
				}
				
			}
			
		}
		
	}

	/**
	 * Sets value of cinema offer weekly count for account
	 * @param accountOfferCounterList
	 * @param cinemaOfferIdList
	 * @param purchaseCount
	 */
	private static void setAccountWeeklyValueInCinemaPurchaseCount(List<AccountOfferCounts> accountOfferCounterList,
			List<String> cinemaOfferIdList, PurchaseCount purchaseCount) {
		
		if(!CollectionUtils.isEmpty(cinemaOfferIdList)
	    && !CollectionUtils.isEmpty(accountOfferCounterList)
		&& !ObjectUtils.isEmpty(purchaseCount)) {
			
			for(AccountOfferCounts counter : accountOfferCounterList) {
				
				if(CollectionUtils.containsAny(cinemaOfferIdList, Arrays.asList(counter.getOfferId()))
				&& !ObjectUtils.isEmpty(counter.getWeeklyCount())) {
					
					purchaseCount.setAccountWeekly(purchaseCount.getAccountWeekly() + counter.getWeeklyCount());
					
				}
				
			}
			
		}
		
	}
	
	/**
     * 
     * @param eligibilityInfo
     * @param purchaseRequest
     * @return counter for current offer
     */
    public static LimitCounter getOfferCounterForCurrentOffer(EligibilityInfo eligibilityInfo, Integer denomination, Integer couponQuantity) {
    	
    	LimitCounter counter = new LimitCounter();
		boolean isDenomination = !ObjectUtils.isEmpty(denomination);
		counter.setCouponQuantity(couponQuantity);
		counter.setOfferId(eligibilityInfo.getOffer().getOfferId());
		setAccountOfferCounterValues(counter, eligibilityInfo.getAccountOfferCounter(), denomination, couponQuantity, isDenomination);
		setOfferCounterValues(counter, eligibilityInfo.getOfferCounter(), denomination, couponQuantity, isDenomination);
		setMemberOfferCounterValues(counter, eligibilityInfo.getMemberOfferCounter(), denomination, couponQuantity, isDenomination);
		return counter;
    }
    
    /**
     * Sets the value of account offer counter to the full counter
     * @param counter
     * @param eligibilityInfo
     * @param denomination
     * @param couponQuantity
     * @param isDenomination
     */
    private static void setAccountOfferCounterValues(LimitCounter counter, AccountOfferCounts accountOfferCounter, Integer denomination,
			Integer couponQuantity, boolean isDenomination) {
		
    	if(!ObjectUtils.isEmpty(accountOfferCounter)
    	&& !ObjectUtils.isEmpty(counter)) {
			
    		DenominationCount denominationCount = isDenomination
					? FilterValues.findAnyDenominationCounterInDenominationCountList(accountOfferCounter.getDenominationCount(), Predicates.sameDenominationForOfferCounter(denomination))
			        : null;
			boolean isDenominationCounter = null!=denominationCount;		
			counter.setAccountOfferDaily(Utilities.addTwoIntegers(accountOfferCounter.getDailyCount(), couponQuantity));
			counter.setAccountOfferWeekly(Utilities.addTwoIntegers(accountOfferCounter.getWeeklyCount(), couponQuantity));
			counter.setAccountOfferMonthly(Utilities.addTwoIntegers(accountOfferCounter.getMonthlyCount(), couponQuantity));
			counter.setAccountOfferAnnual(Utilities.addTwoIntegers(accountOfferCounter.getAnnualCount(), couponQuantity));
			counter.setAccountOfferTotal(Utilities.addTwoIntegers(accountOfferCounter.getTotalCount(), couponQuantity));
			counter.setAccountDenominationDaily(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getDailyCount() : null, couponQuantity));
			counter.setAccountDenominationWeekly(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getWeeklyCount() : null, couponQuantity));
			counter.setAccountDenominationMonthly(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getMonthlyCount() : null, couponQuantity));
			counter.setAccountDenominationAnnual(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getAnnualCount() : null, couponQuantity));
			counter.setAccountDenominationTotal(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getTotalCount() : null, couponQuantity));
		}
		
	}

	/**
     * Sets the value of offer counter to the full counter
     * @param counter
     * @param eligibilityInfo
     * @param denomination
     * @param couponQuantity
     * @param isDenomination
     */
    private static void setOfferCounterValues(LimitCounter counter, OfferCounters offerCounter,
			Integer denomination, Integer couponQuantity, boolean isDenomination) {
		
    	if(!ObjectUtils.isEmpty(offerCounter)
    	&& !ObjectUtils.isEmpty(counter)) {
			
    		DenominationCount denominationCount = isDenomination
					? FilterValues.findAnyDenominationCounterInDenominationCountList(offerCounter.getDenominationCount(), Predicates.sameDenominationForOfferCounter(denomination))
			        : null;
			boolean isDenominationCounter = null!=denominationCount;
			counter.setOfferDaily(Utilities.addTwoIntegers(offerCounter.getDailyCount(), couponQuantity));
			counter.setOfferWeekly(Utilities.addTwoIntegers(offerCounter.getWeeklyCount(), couponQuantity));
			counter.setOfferMonthly(Utilities.addTwoIntegers(offerCounter.getMonthlyCount(), couponQuantity));
			counter.setOfferAnnual(Utilities.addTwoIntegers(offerCounter.getAnnualCount(), couponQuantity));
			counter.setOfferTotal(Utilities.addTwoIntegers(offerCounter.getTotalCount(), couponQuantity));
			counter.setOfferDenominationDaily(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getDailyCount() : null, couponQuantity));
			counter.setOfferDenominationWeekly(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getWeeklyCount() : null, couponQuantity));
			counter.setOfferDenominationMonthly(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getMonthlyCount() : null, couponQuantity));
			counter.setOfferDenominationAnnual(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getAnnualCount() : null, couponQuantity));
			counter.setOfferDenominationTotal(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getTotalCount() : null, couponQuantity));
		}
		
	}

	/**
     * Sets the value of member offer counter to the full counter
	 * @param counter 
     * @param eligibilityInfo
     * @param denomination
     * @param couponQuantity
     * @param isDenomination
     */
    private static void setMemberOfferCounterValues(LimitCounter counter, MemberOfferCounts memberOfferCounter, Integer denomination,
			Integer couponQuantity, boolean isDenomination) {
		
    	if(!ObjectUtils.isEmpty(memberOfferCounter)
    	&& !ObjectUtils.isEmpty(counter)) {
			
    		DenominationCount denominationCount = isDenomination
					? FilterValues.findAnyDenominationCounterInDenominationCountList(memberOfferCounter.getDenominationCount(), Predicates.sameDenominationForOfferCounter(denomination))
			        : null;
			boolean isDenominationCounter = null!=denominationCount;
			counter.setMemberOfferDaily(Utilities.addTwoIntegers(memberOfferCounter.getDailyCount(), couponQuantity));
			counter.setMemberOfferWeekly(Utilities.addTwoIntegers(memberOfferCounter.getWeeklyCount(), couponQuantity));
			counter.setMemberOfferMonthly(Utilities.addTwoIntegers(memberOfferCounter.getMonthlyCount(), couponQuantity));
			counter.setMemberOfferAnnual(Utilities.addTwoIntegers(memberOfferCounter.getAnnualCount(), couponQuantity));
			counter.setMemberOfferTotal(Utilities.addTwoIntegers(memberOfferCounter.getTotalCount(), couponQuantity));
			counter.setMemberDenominationDaily(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getDailyCount() : null, couponQuantity));
			counter.setMemberDenominationWeekly(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getWeeklyCount() : null, couponQuantity));
			counter.setMemberDenominationMonthly(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getMonthlyCount() : null, couponQuantity));
			counter.setMemberDenominationAnnual(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getAnnualCount() : null, couponQuantity));
			counter.setMemberDenominationTotal(getCounterValueForDenomination(isDenomination, isDenominationCounter, isDenominationCounter ? denominationCount.getTotalCount() : null, couponQuantity));
		}
		
	}
    
    /**
	 * 
	 * @param offerId
	 * @param rules
	 * @param denomination
	 * @param couponQuantity
	 * @return new created offer counter for input
	 */
	public static OfferCounters getNewOfferCounter(String offerId, List<String> rules, Integer denomination, Integer couponQuantity) {
		
		OfferCounters counter = new OfferCounters();
		counter.setOfferId(offerId);
		counter.setRules(rules);
		counter.setDailyCount(couponQuantity);
		counter.setWeeklyCount(couponQuantity);
		counter.setMonthlyCount(couponQuantity);
		counter.setAnnualCount(couponQuantity);
		counter.setTotalCount(couponQuantity);
		counter.setLastPurchased(new Date());
		counter.setDenominationCount(!ObjectUtils.isEmpty(denomination) 
				? Arrays.asList(getNewDenominationCount(denomination, couponQuantity))
				: new ArrayList<>(1));
		return counter;
	}
	
	/**
	 * 
	 * @param offerId
	 * @param membershipCode
	 * @param denomination
	 * @param couponQuantity
	 * @return new created member offer counter for input
	 */
	public static MemberOfferCounts getNewMemberOfferCounter(String offerId, String membershipCode, Integer denomination, Integer couponQuantity) {
		
		MemberOfferCounts counter = new MemberOfferCounts();
		counter.setOfferId(offerId);
		counter.setMembershipCode(membershipCode);
		counter.setDailyCount(couponQuantity);
		counter.setWeeklyCount(couponQuantity);
		counter.setMonthlyCount(couponQuantity);
		counter.setAnnualCount(couponQuantity);
		counter.setTotalCount(couponQuantity);
		counter.setLastPurchased(new Date());
		counter.setDenominationCount(!ObjectUtils.isEmpty(denomination) 
				? Arrays.asList(getNewDenominationCount(denomination, couponQuantity))
				: new ArrayList<>(1));
		return counter;
	}
	
	/**
	 * 
	 * @param offerId
	 * @param membershipCode
	 * @param denomination
	 * @param couponQuantity
	 * @return new created member offer counter for input
	 */
	public static AccountOfferCounts getNewAccountOfferCounter(String offerId, String accountNumber, String membershipCode, Integer denomination, Integer couponQuantity) {
		
		AccountOfferCounts counter = new AccountOfferCounts();
		counter.setOfferId(offerId);
		counter.setAccountNumber(accountNumber);
		counter.setMembershipCode(membershipCode);
		counter.setDailyCount(couponQuantity);
		counter.setWeeklyCount(couponQuantity);
		counter.setMonthlyCount(couponQuantity);
		counter.setAnnualCount(couponQuantity);
		counter.setTotalCount(couponQuantity);
		counter.setLastPurchased(new Date());
		counter.setDenominationCount(!ObjectUtils.isEmpty(denomination) 
				? Arrays.asList(getNewDenominationCount(denomination, couponQuantity))
				: new ArrayList<>(1));
		return counter;
	}
	
	/**
	 * 
	 * @param denomination
	 * @param couponQuantity
	 * @return denomination counter created for input denomination
	 */
	public static DenominationCount getNewDenominationCount(Integer denomination, Integer couponQuantity) {
		
		DenominationCount denominationCount = new DenominationCount();
		denominationCount.setDenomination(denomination);
		denominationCount.setDailyCount(couponQuantity);
    	denominationCount.setWeeklyCount(couponQuantity);
    	denominationCount.setMonthlyCount(couponQuantity);
    	denominationCount.setAnnualCount(couponQuantity);
    	denominationCount.setTotalCount(couponQuantity);
    	denominationCount.setLastPurchased(new Date());
		return denominationCount;
	}
	
	/**
	 * Sets the value to be synced in counters
	 * @param eligibilityInfo
	 */
	public static void setErrorCodeValuesAndResetInCounter(EligibilityInfo eligibilityInfo) {
		
		if(!ObjectUtils.isEmpty(eligibilityInfo) && !CollectionUtils.isEmpty(eligibilityInfo.getErrorRecordsList())) {
		
			List<OfferCounters> offerCounterList = new ArrayList<>(1);
			List<MemberOfferCounts> memberCounterList = new ArrayList<>(1);
			List<AccountOfferCounts> accountCounterList = new ArrayList<>(1);
			
			for(ErrorRecords errorRecord : eligibilityInfo.getErrorRecordsList()) {
				
				resetOfferCounterFromErrorRecords(errorRecord, offerCounterList, eligibilityInfo);
				resetMemberOfferCounterFromErrorRecords(errorRecord, memberCounterList, eligibilityInfo);
				resetAccountOfferCounterFromErrorRecords(errorRecord, accountCounterList, eligibilityInfo);
				
			}
			
			eligibilityInfo.setOfferCountersList(offerCounterList);
			eligibilityInfo.setMemberOfferCounterList(memberCounterList);
			eligibilityInfo.setAccountOfferCounterList(accountCounterList);
			updateLastUpdatedDateForCounters(eligibilityInfo);
			
		} else {
			
			resetAllRequiredCounters(eligibilityInfo);
			
		} 
			
	}
	
	/**
	 * Resets list of offer counters from error record values
	 * @param errorRecord
	 * @param offerCounterList
	 * @param eligibilityInfo 
	 */
	private static void resetOfferCounterFromErrorRecords(ErrorRecords errorRecord, List<OfferCounters> offerCounterList, EligibilityInfo eligibilityInfo) {
		
		OfferCounters offerCounter = FilterValues.findAnyOfferCounterInList(offerCounterList, Predicates.isCounterWithOfferId(errorRecord.getOfferId()));
		
		if(ObjectUtils.isEmpty(offerCounter)) {
			
			offerCounter = FilterValues.findAnyOfferCounterInList(eligibilityInfo.getOfferCountersList(), Predicates.isCounterWithOfferId(errorRecord.getOfferId()));
			
			if(ObjectUtils.isEmpty(offerCounter)) {
				
				offerCounter = getNewOfferCounter(errorRecord.getOfferId(), errorRecord.getRules(), errorRecord.getDenomination(), 0);
			}
			
			offerCounterList.add(modifyOfferCounterFromErrorRecords(errorRecord, offerCounter));
			
		} else {
			
			for(OfferCounters counter : offerCounterList) {
				
				if(StringUtils.equals(counter.getOfferId(), errorRecord.getOfferId())) {
					
					modifyOfferCounterFromErrorRecords(errorRecord, counter);
					
				}
				
			}
		}
		
	}

	/**
	 * Resets list of member offer counters from error record values
	 * @param errorRecord
	 * @param memberCounterList
	 * @param eligibilityInfo
	 */
	private static void resetMemberOfferCounterFromErrorRecords(ErrorRecords errorRecord,
			List<MemberOfferCounts> memberCounterList, EligibilityInfo eligibilityInfo) {
		
		MemberOfferCounts memberOfferCounter = FilterValues.findAnyMemberOfferCounterInList(memberCounterList, Predicates.isCounterWithMembershipCodeAndOfferId(errorRecord.getMembershipCode(), errorRecord.getOfferId()));
		
		if(ObjectUtils.isEmpty(memberOfferCounter)) {
			
			memberOfferCounter = FilterValues.findAnyMemberOfferCounterInList(eligibilityInfo.getMemberOfferCounterList(), Predicates.isCounterWithMembershipCodeAndOfferId(errorRecord.getMembershipCode(), errorRecord.getOfferId()));
			
			if(ObjectUtils.isEmpty(memberOfferCounter)) {
				
				memberOfferCounter = getNewMemberOfferCounter(errorRecord.getOfferId(), errorRecord.getMembershipCode(), errorRecord.getDenomination(), 0);
						
			}
			memberCounterList.add(modifyMemberOfferCounterFromErrorRecords(errorRecord, memberOfferCounter));
			
			
		} else {
			
			for(MemberOfferCounts counter : memberCounterList) {
				
				if(StringUtils.equals(counter.getOfferId(), errorRecord.getOfferId())
				&& StringUtils.equals(counter.getMembershipCode(), errorRecord.getMembershipCode())) {
					
					modifyMemberOfferCounterFromErrorRecords(errorRecord, counter);
					
				}
				
			}
			
		}
		
	}
	
	/**
	 * Resets list of account offer counters from error record values
	 * @param errorRecord
	 * @param accountCounterList
	 * @param eligibilityInfo
	 */
	private static void resetAccountOfferCounterFromErrorRecords(ErrorRecords errorRecord,
			List<AccountOfferCounts> accountCounterList, EligibilityInfo eligibilityInfo) {
		
		AccountOfferCounts accountOfferCounter = FilterValues.findAnyAccountOfferCounterInList(accountCounterList, Predicates.isCounterWithAccountNumberAndOfferId(errorRecord.getAccountNumber(), errorRecord.getMembershipCode(), errorRecord.getOfferId()));
		
		if(ObjectUtils.isEmpty(accountOfferCounter)) {
			
			accountOfferCounter = FilterValues.findAnyAccountOfferCounterInList(eligibilityInfo.getAccountOfferCounterList(), Predicates.isCounterWithAccountNumberAndOfferId(errorRecord.getAccountNumber(), errorRecord.getMembershipCode(), errorRecord.getOfferId()));
			
			if(ObjectUtils.isEmpty(accountOfferCounter)) {
				accountOfferCounter =  getNewAccountOfferCounter(errorRecord.getOfferId(), errorRecord.getAccountNumber(), errorRecord.getMembershipCode(), errorRecord.getDenomination(), 0);
			}
			
			accountCounterList.add(modifyAccountOfferCounterFromErrorRecords(errorRecord, accountOfferCounter));
		
		} else {
			
			for(AccountOfferCounts counter : accountCounterList) {
				
				if(StringUtils.equals(counter.getOfferId(), errorRecord.getOfferId())
				&& StringUtils.equals(counter.getMembershipCode(), errorRecord.getMembershipCode())
				&& StringUtils.equals(counter.getAccountNumber(), errorRecord.getAccountNumber())) {
					
					modifyAccountOfferCounterFromErrorRecords(errorRecord, counter);
				
				}
				
			}
			
		}
		
	}
	
	/**
	 * Updates the last updated date for counters after resetting from error records
	 * @param eligibilityInfo
	 */
	private static void updateLastUpdatedDateForCounters(EligibilityInfo eligibilityInfo) {
		
		updateLastUpdatedDateForOfferCounters(eligibilityInfo);
		updateLastUpdatedDateForMemberOfferCounters(eligibilityInfo);
		updateLastUpdatedDateForAccountOfferCounters(eligibilityInfo);
		
	}

	/**
	 * Updates the last purchased date for offer counters
	 * @param eligibilityInfo
	 */
	private static void updateLastUpdatedDateForOfferCounters(EligibilityInfo eligibilityInfo) {
		
		if(!ObjectUtils.isEmpty(eligibilityInfo)
		&& !CollectionUtils.isEmpty(eligibilityInfo.getOfferCountersList())){
			
			for(OfferCounters counter : eligibilityInfo.getOfferCountersList()) {
				
				counter.setLastPurchased(new Date());
				
				if(!CollectionUtils.isEmpty(counter.getDenominationCount())){
					
					counter.getDenominationCount().forEach(d->d.setLastPurchased(new Date()));
					
				}	
				
			}
			
		}
		
	}

	/**
	 * Updates the last purchased date for membership offer counters
	 * @param eligibilityInfo
	 */
	private static void updateLastUpdatedDateForMemberOfferCounters(EligibilityInfo eligibilityInfo) {
		
		if(!ObjectUtils.isEmpty(eligibilityInfo)
		&& !CollectionUtils.isEmpty(eligibilityInfo.getMemberOfferCounterList())){
			
			for(MemberOfferCounts counter : eligibilityInfo.getMemberOfferCounterList()) {
				
				counter.setLastPurchased(new Date());
				
				if(!CollectionUtils.isEmpty(counter.getDenominationCount())){
					
					counter.getDenominationCount().forEach(d->d.setLastPurchased(new Date()));
					
				}	
				
			}
			
		}
		
	}

	/**
	 * Updates the last purchased date for account offer counters
	 * @param eligibilityInfo
	 */
	private static void updateLastUpdatedDateForAccountOfferCounters(EligibilityInfo eligibilityInfo) {
		
		if(!ObjectUtils.isEmpty(eligibilityInfo)
		&& !CollectionUtils.isEmpty(eligibilityInfo.getAccountOfferCounterList())){
			
			for(AccountOfferCounts counter : eligibilityInfo.getAccountOfferCounterList()) {
				
				counter.setLastPurchased(new Date());
				
				if(!CollectionUtils.isEmpty(counter.getDenominationCount())){
					
					counter.getDenominationCount().forEach(d->d.setLastPurchased(new Date()));
					
				}	
				
			}
			
		}
		
	}


	/***
	 * Resets required counters before purchase
	 * @param eligibilityInfo
	 */
	private static void resetAllRequiredCounters(EligibilityInfo eligibilityInfo) {
		
		if(!ObjectUtils.isEmpty(eligibilityInfo)) {
			
			if(!CollectionUtils.isEmpty(eligibilityInfo.getOfferCountersList())) {
				
				eligibilityInfo.setOfferCountersList(getResetOfferCounterList(eligibilityInfo.getOfferCountersList()));
				
			}
			
			if(!CollectionUtils.isEmpty(eligibilityInfo.getMemberOfferCounterList())) {
				
				eligibilityInfo.setMemberOfferCounterList(getResetMemberOfferCountList(eligibilityInfo.getMemberOfferCounterList()));
				
			}
			
			if(!CollectionUtils.isEmpty(eligibilityInfo.getAccountOfferCounterList())) {
				
				eligibilityInfo.setAccountOfferCounterList(getResetAccountOfferCountList(eligibilityInfo.getAccountOfferCounterList()));
			}
			
		}
		
	}
	
	/***
	 * Modifies the value in offer counter from error record list
	 * @param errorRecordList
	 * @param counter
	 * @param updateDate 
	 */
	private static OfferCounters modifyOfferCounterFromErrorRecords(ErrorRecords errorRecord, OfferCounters counter) {
		
		if(!ObjectUtils.isEmpty(errorRecord) && !ObjectUtils.isEmpty(counter)) {
			
			TimeLimits timeLimit = ObjectMapperUtils.map(counter, TimeLimits.class);
			setErrorRecordDataInCounter(timeLimit, errorRecord.getCouponQuantity(), errorRecord.getCreatedDate());
			counter.setDailyCount(timeLimit.getDailyCount());
			counter.setWeeklyCount(timeLimit.getWeeklyCount());
			counter.setMonthlyCount(timeLimit.getMonthlyCount());
			counter.setAnnualCount(timeLimit.getAnnualCount());
			counter.setTotalCount(counter.getTotalCount()+errorRecord.getCouponQuantity());
			modifyOfferCounterDenomination(counter, errorRecord);
	        
		} 
		
		return counter;
					
	}
	
	/**
	 * Modifies value of denomination counter in offer counter list from error records
	 * @param counter
	 * @param errorRecord
	 */
	private static void modifyOfferCounterDenomination(OfferCounters counter, ErrorRecords errorRecord) {
		
		TimeLimits timeLimit = null;
		if(!ObjectUtils.isEmpty(errorRecord.getDenomination())) {
        	
			if(!CollectionUtils.isEmpty(counter.getDenominationCount())) {
                			        	
        		resetExistingDenominationCountersForOfferCounterList(counter, errorRecord);
        		
        	} else {
	        	
        		DenominationCount dCount = getNewDenominationCount(errorRecord.getDenomination(), 0);
    			timeLimit = ObjectMapperUtils.map(dCount, TimeLimits.class);
    			setErrorRecordDataInCounter(timeLimit, errorRecord.getCouponQuantity(), errorRecord.getCreatedDate());
				dCount.setDailyCount(timeLimit.getDailyCount());
				dCount.setWeeklyCount(timeLimit.getWeeklyCount());
				dCount.setMonthlyCount(timeLimit.getMonthlyCount());
				dCount.setAnnualCount(timeLimit.getAnnualCount());
				dCount.setTotalCount(dCount.getTotalCount()+errorRecord.getCouponQuantity());
				counter.setDenominationCount(new ArrayList<>(1));
				counter.getDenominationCount().add(dCount);
	        	
	        }
        	
        }
		
	}

	/**
	 * Modifies value of existing denomination counter in offer counter list from error records
	 * @param counter
	 * @param errorRecord
	 */
	private static void resetExistingDenominationCountersForOfferCounterList(OfferCounters counter, ErrorRecords errorRecord) {
		
		TimeLimits timeLimit = null;
		if(Checks.checkDenominationCountPresent(counter.getDenominationCount(), errorRecord.getDenomination())) {
			
			for(DenominationCount denominationCount : counter.getDenominationCount()) {
        		
				if(denominationCount.getDenomination().equals(errorRecord.getDenomination())) {
					
					timeLimit = ObjectMapperUtils.map(denominationCount, TimeLimits.class);
					setErrorRecordDataInCounter(timeLimit, errorRecord.getCouponQuantity(), errorRecord.getCreatedDate());
					denominationCount.setDailyCount(timeLimit.getDailyCount());
					denominationCount.setWeeklyCount(timeLimit.getWeeklyCount());
					denominationCount.setMonthlyCount(timeLimit.getMonthlyCount());
					denominationCount.setAnnualCount(timeLimit.getAnnualCount());
					denominationCount.setTotalCount(denominationCount.getTotalCount()+errorRecord.getCouponQuantity());
					
				}
				
    		}
			
	    } else {
		
			DenominationCount dCount = getNewDenominationCount(errorRecord.getDenomination(), 0);
			timeLimit = ObjectMapperUtils.map(dCount, TimeLimits.class);
			setErrorRecordDataInCounter(timeLimit, errorRecord.getCouponQuantity(), errorRecord.getCreatedDate());
			dCount.setDailyCount(timeLimit.getDailyCount());
			dCount.setWeeklyCount(timeLimit.getWeeklyCount());
			dCount.setMonthlyCount(timeLimit.getMonthlyCount());
			dCount.setAnnualCount(timeLimit.getAnnualCount());
			dCount.setTotalCount(dCount.getTotalCount()+errorRecord.getCouponQuantity());
			
			
			if(CollectionUtils.isEmpty(counter.getDenominationCount())) {
				
				counter.setDenominationCount(new ArrayList<>(1));
				
			} 
			
			counter.getDenominationCount().add(dCount);
			
		}
		
	}

	/***
	 * Modifies the value in member offer counter from error record list
	 * @param errorRecordList
	 * @param counter
	 */
	private static MemberOfferCounts modifyMemberOfferCounterFromErrorRecords(ErrorRecords errorRecord, MemberOfferCounts counter) {
		
		if(!ObjectUtils.isEmpty(errorRecord) && !ObjectUtils.isEmpty(counter)) {
			
			TimeLimits timeLimit = ObjectMapperUtils.map(counter, TimeLimits.class);
			setErrorRecordDataInCounter(timeLimit, errorRecord.getCouponQuantity(), errorRecord.getCreatedDate());
			counter.setDailyCount(timeLimit.getDailyCount());
			counter.setWeeklyCount(timeLimit.getWeeklyCount());
			counter.setMonthlyCount(timeLimit.getMonthlyCount());
			counter.setAnnualCount(timeLimit.getAnnualCount());
			counter.setTotalCount(counter.getTotalCount()+errorRecord.getCouponQuantity());
			resetMemberOfferDenominationCounterFromErrorRecords(errorRecord, counter);
	        
		}
		
		return counter;

	}
	
	/**
	 * Modifies value of denomination counter in member offer counter list from error records
	 * @param errorRecord
	 * @param counter
	 */
	private static void resetMemberOfferDenominationCounterFromErrorRecords(ErrorRecords errorRecord,
			MemberOfferCounts counter) {
		
		TimeLimits timeLimit = null;
		if(!ObjectUtils.isEmpty(errorRecord.getDenomination())) {
        	
        	if(!CollectionUtils.isEmpty(counter.getDenominationCount())) {
                			        	
        		resetExistingDenominationCountersForMemberOfferCounterList(errorRecord, counter);
        		
        	} else {
	        	
        		DenominationCount dCount = getNewDenominationCount(errorRecord.getDenomination(), 0);
    			timeLimit = ObjectMapperUtils.map(dCount, TimeLimits.class);
    			setErrorRecordDataInCounter(timeLimit, errorRecord.getCouponQuantity(), errorRecord.getCreatedDate());
				dCount.setDailyCount(timeLimit.getDailyCount());
				dCount.setWeeklyCount(timeLimit.getWeeklyCount());
				dCount.setMonthlyCount(timeLimit.getMonthlyCount());
				dCount.setAnnualCount(timeLimit.getAnnualCount());
				dCount.setTotalCount(dCount.getTotalCount()+errorRecord.getCouponQuantity());
				counter.setDenominationCount(new ArrayList<>(1));
				counter.getDenominationCount().add(dCount);
	        	
	        }
        	
        }
		
	}

	/**
	 * Modifies value of existing denomination counter in member offer counter list from error records
	 * @param errorRecord
	 * @param counter
	 */
	private static void resetExistingDenominationCountersForMemberOfferCounterList(ErrorRecords errorRecord,
			MemberOfferCounts counter) {
		
		TimeLimits timeLimit = null;
		if(Checks.checkDenominationCountPresent(counter.getDenominationCount(), errorRecord.getDenomination())) {
			
			for(DenominationCount denominationCount : counter.getDenominationCount()) {
	        		
					if(denominationCount.getDenomination().equals(errorRecord.getDenomination())) {
    					
    					timeLimit = ObjectMapperUtils.map(denominationCount, TimeLimits.class);
    					setErrorRecordDataInCounter(timeLimit, errorRecord.getCouponQuantity(), errorRecord.getCreatedDate());
    					denominationCount.setDailyCount(timeLimit.getDailyCount());
    					denominationCount.setWeeklyCount(timeLimit.getWeeklyCount());
    					denominationCount.setMonthlyCount(timeLimit.getMonthlyCount());
    					denominationCount.setAnnualCount(timeLimit.getAnnualCount());
    					denominationCount.setTotalCount(denominationCount.getTotalCount()+errorRecord.getCouponQuantity());
    					
    				}
    				
        		}
				
		} else {
			
			DenominationCount dCount = getNewDenominationCount(errorRecord.getDenomination(), 0);
			timeLimit = ObjectMapperUtils.map(dCount, TimeLimits.class);
			setErrorRecordDataInCounter(timeLimit, errorRecord.getCouponQuantity(), errorRecord.getCreatedDate());
			dCount.setDailyCount(timeLimit.getDailyCount());
			dCount.setWeeklyCount(timeLimit.getWeeklyCount());
			dCount.setMonthlyCount(timeLimit.getMonthlyCount());
			dCount.setAnnualCount(timeLimit.getAnnualCount());
			dCount.setTotalCount(dCount.getTotalCount()+errorRecord.getCouponQuantity());
			
			if(CollectionUtils.isEmpty(counter.getDenominationCount())) {
				
				counter.setDenominationCount(new ArrayList<>(1));
				
			} 
			
			counter.getDenominationCount().add(dCount);
			
		}
		
	}

	/***
	 * Modifies the value in account offer counter from error record list
	 * @param errorRecordList
	 * @param counter
	 */
	private static AccountOfferCounts modifyAccountOfferCounterFromErrorRecords(ErrorRecords errorRecord, AccountOfferCounts counter) {
		
		if(!ObjectUtils.isEmpty(errorRecord) && !ObjectUtils.isEmpty(counter)) {
			
			TimeLimits timeLimit = ObjectMapperUtils.map(counter, TimeLimits.class);
			setErrorRecordDataInCounter(timeLimit, errorRecord.getCouponQuantity(), errorRecord.getCreatedDate());
			counter.setDailyCount(timeLimit.getDailyCount());
			counter.setWeeklyCount(timeLimit.getWeeklyCount());
			counter.setMonthlyCount(timeLimit.getMonthlyCount());
			counter.setAnnualCount(timeLimit.getAnnualCount());
			counter.setTotalCount(counter.getTotalCount()+errorRecord.getCouponQuantity());
			resetAccountOfferCounterDenominationListFromErrorRecords(errorRecord, counter);
	        
		}
		return counter;
	}

	/**
	 * Modifies value of denomination counter in account offer counter list from error records
	 * @param errorRecord
	 * @param counter
	 */
	private static void resetAccountOfferCounterDenominationListFromErrorRecords(ErrorRecords errorRecord,
			AccountOfferCounts counter) {
		
		TimeLimits timeLimit = null;
		if(!ObjectUtils.isEmpty(errorRecord.getDenomination())) {
        	
        	if(!CollectionUtils.isEmpty(counter.getDenominationCount())) {
                			        	
        		resetExistingDenominationCountersForAccountOfferCounterList(errorRecord, counter);
        		
        	} else {
	        	
        		DenominationCount dCount = getNewDenominationCount(errorRecord.getDenomination(), 0);
    			timeLimit = ObjectMapperUtils.map(dCount, TimeLimits.class);
    			setErrorRecordDataInCounter(timeLimit, errorRecord.getCouponQuantity(), errorRecord.getCreatedDate());
				dCount.setDailyCount(timeLimit.getDailyCount());
				dCount.setWeeklyCount(timeLimit.getWeeklyCount());
				dCount.setMonthlyCount(timeLimit.getMonthlyCount());
				dCount.setAnnualCount(timeLimit.getAnnualCount());
				dCount.setTotalCount(dCount.getTotalCount()+errorRecord.getCouponQuantity());
				counter.setDenominationCount(new ArrayList<>(1));
				counter.getDenominationCount().add(dCount);
	        	
	        }
        	
        }
		
	}

	/**
	 * Modifies value of existing denomination counter in account offer counter list from error records
	 * @param errorRecord
	 * @param counter
	 */
	private static void resetExistingDenominationCountersForAccountOfferCounterList(ErrorRecords errorRecord,
			AccountOfferCounts counter) {
		
		TimeLimits timeLimit = null;
		if(Checks.checkDenominationCountPresent(counter.getDenominationCount(), errorRecord.getDenomination())) {
			
			for(DenominationCount denominationCount : counter.getDenominationCount()) {
        		
				if(denominationCount.getDenomination().equals(errorRecord.getDenomination())) {
					
					timeLimit = ObjectMapperUtils.map(denominationCount, TimeLimits.class);
					setErrorRecordDataInCounter(timeLimit, errorRecord.getCouponQuantity(), errorRecord.getCreatedDate());
					denominationCount.setDailyCount(timeLimit.getDailyCount());
					denominationCount.setWeeklyCount(timeLimit.getWeeklyCount());
					denominationCount.setMonthlyCount(timeLimit.getMonthlyCount());
					denominationCount.setAnnualCount(timeLimit.getAnnualCount());
					denominationCount.setTotalCount(denominationCount.getTotalCount()+errorRecord.getCouponQuantity());
					
				}
				
    		}
				
		} else {
			
			DenominationCount dCount = getNewDenominationCount(errorRecord.getDenomination(), 0);
			timeLimit = ObjectMapperUtils.map(dCount, TimeLimits.class);
			setErrorRecordDataInCounter(timeLimit, errorRecord.getCouponQuantity(), errorRecord.getCreatedDate());
			dCount.setDailyCount(timeLimit.getDailyCount());
			dCount.setWeeklyCount(timeLimit.getWeeklyCount());
			dCount.setMonthlyCount(timeLimit.getMonthlyCount());
			dCount.setAnnualCount(timeLimit.getAnnualCount());
			dCount.setTotalCount(dCount.getTotalCount()+errorRecord.getCouponQuantity());
			
			
			if(CollectionUtils.isEmpty(counter.getDenominationCount())) {
				
				counter.setDenominationCount(new ArrayList<>(1));
								
			}
			counter.getDenominationCount().add(dCount);
			
		}
		
	}
	
	/**
     * 
     * @param timelimits
     * @return counter after setting values as per time
     */
    public static TimeLimits setErrorRecordDataInCounter(TimeLimits timelimits, Integer couponQuantity, Date date) {
		 Calendar currentCalendar = Calendar.getInstance();
		 
		 if(Checks.checkDateFirstGreaterThanSecond(new Date(), date)) {
			 
			 setErrorRecordFirstResetLater(timelimits, couponQuantity, date, currentCalendar);
			 
		} else {
			
			 resetFirstSetErrorRecordLater(timelimits, couponQuantity, date, currentCalendar);
			 
		 }
		 
		return timelimits;
		 
	}
    
    /**
     * Resets daily count of counter
     * @param timelimits
     * @param currentCalendar
     */
    private static void resetDailyCount(TimeLimits timelimits, Calendar currentCalendar) {
		
		if(!Checks.checkIsDateInCurrentDay(timelimits.getLastPurchased(), currentCalendar)) {
			 
			 timelimits.setDailyCount(0);
		 }
		
	}
    
    /**
     * Resets weekly count of counter
     * @param timelimits
     * @param currentCalendar
     */
    private static void resetWeeklyCount(TimeLimits timelimits, Calendar currentCalendar) {
		
    	if(!Checks.checkIsDateInCurrentWeek(timelimits.getLastPurchased(), currentCalendar)) {
			 
			 timelimits.setWeeklyCount(0);
		 }
		
	}
    
    /**
     * Resets monthly count of counter
     * @param timelimits
     * @param currentCalendar
     */
    private static void resetMonthlyCount(TimeLimits timelimits, Calendar currentCalendar) {
		
    	if(!Checks.checkIsDateInCurrentMonth(timelimits.getLastPurchased(), currentCalendar)) {
			 
			 timelimits.setMonthlyCount(0);
		 }
		
	}
    
    /**
     * Resets annual count of counter
     * @param timelimits
     * @param currentCalendar
     */
    private static void resetAnnualCount(TimeLimits timelimits, Calendar currentCalendar) {
		
    	if(!Checks.checkIsDateInCurrentYear(timelimits.getLastPurchased(), currentCalendar)) {
			 
			 timelimits.setAnnualCount(0);
		 }
		
	}
    
    /**
     * Modify daily count from Error Records
     * @param timelimits
     * @param couponQuantity
     * @param date
     * @param currentCalendar
     */
    private static void modifyDailyCountFromErrorRecords(TimeLimits timelimits, Integer couponQuantity, Date date,
			Calendar currentCalendar) {
		
    	if (Checks.checkIsDateInCurrentDay(date,currentCalendar)) {
			 //set error record daily count
			 timelimits.setDailyCount(null!=timelimits.getDailyCount()
					 ? timelimits.getDailyCount()+couponQuantity
					 : couponQuantity);
		 }
		
	}
    
    /**
     * Modify weekly count from Error Records
     * @param timelimits
     * @param couponQuantity
     * @param date
     * @param currentCalendar
     */
    private static void modifyWeeklyCountFromErrorRecords(TimeLimits timelimits, Integer couponQuantity, Date date,
			Calendar currentCalendar) {
		
    	if (Checks.checkIsDateInCurrentWeek(date,currentCalendar)) {
			//set error record weekly count
			 timelimits.setWeeklyCount(null!=timelimits.getWeeklyCount()
					 ? timelimits.getWeeklyCount()+couponQuantity
					 : couponQuantity);
			 
    	}
		
	}
    
    /**
     * Modify monthly count from Error Records
     * @param timelimits
     * @param couponQuantity
     * @param date
     * @param currentCalendar
     */
    private static void modifyMonthlyCountFromErrorRecords(TimeLimits timelimits, Integer couponQuantity, Date date,
			Calendar currentCalendar) {
		
    	if (Checks.checkIsDateInCurrentMonth(date,currentCalendar)) {
			//set error record monthly count
			 timelimits.setMonthlyCount(null!=timelimits.getMonthlyCount()
					 ? timelimits.getMonthlyCount()+couponQuantity
					 : couponQuantity);
				 
		 } 
		
	}
    
    /**
     * Modify annual count from Error Records
     * @param timelimits
     * @param couponQuantity
     * @param date
     * @param currentCalendar
     */
    private static void modifyAnnualCountFromErrorRecords(TimeLimits timelimits, Integer couponQuantity, Date date,
			Calendar currentCalendar) {
		
    	if (Checks.checkIsDateInCurrentYear(date,currentCalendar)) {
			//set error record annual count
			 timelimits.setAnnualCount(null!=timelimits.getAnnualCount()
					 ? timelimits.getAnnualCount()+couponQuantity
					 : couponQuantity);
			 
		 } 
		
	}
    
    /**
     * 
     * @param timelimits
     * @param couponQuantity
     * @param date
     * @param currentCalendar
     */
    private static void resetFirstSetErrorRecordLater(TimeLimits timelimits, Integer couponQuantity, Date date,
			Calendar currentCalendar) {
		
    	resetDailyCount(timelimits, currentCalendar);
    	modifyDailyCountFromErrorRecords(timelimits, couponQuantity, date, currentCalendar);
    	resetWeeklyCount(timelimits, currentCalendar);
    	modifyWeeklyCountFromErrorRecords(timelimits, couponQuantity, date, currentCalendar);
    	resetMonthlyCount(timelimits, currentCalendar);
    	modifyMonthlyCountFromErrorRecords(timelimits, couponQuantity, date, currentCalendar);
    	resetAnnualCount(timelimits, currentCalendar);
    	modifyAnnualCountFromErrorRecords(timelimits, couponQuantity, date, currentCalendar);
		 
	}

	

	/**
     * 
     * @param timelimits
     * @param couponQuantity
     * @param date
     * @param currentCalendar
     */
    private static void setErrorRecordFirstResetLater(TimeLimits timelimits, Integer couponQuantity, Date date, Calendar currentCalendar) {
		
    	modifyDailyCountFromErrorRecords(timelimits, couponQuantity, date, currentCalendar);
    	resetDailyCount(timelimits, currentCalendar);
    	modifyWeeklyCountFromErrorRecords(timelimits, couponQuantity, date, currentCalendar);
    	resetWeeklyCount(timelimits, currentCalendar);
    	modifyMonthlyCountFromErrorRecords(timelimits, couponQuantity, date, currentCalendar);
    	resetMonthlyCount(timelimits, currentCalendar);
    	modifyAnnualCountFromErrorRecords(timelimits, couponQuantity, date, currentCalendar);
    	resetAnnualCount(timelimits, currentCalendar);
	}

    /**
 	 *  
 	 * @param offerCounterList
 	 * @return list of offer counters after modifying limits
 	 */
	public static List<OfferCounters> getResetOfferCounterList(List<OfferCounters> offerCounterList) {
		
		List<OfferCounters> offerCounterListModified =  new ArrayList<>(offerCounterList.size());
		if(CollectionUtils.isNotEmpty(offerCounterList)) {
			
			for(OfferCounters offerCounter : offerCounterList) {
			
				TimeLimits timeLimit = ObjectMapperUtils.map(offerCounter, TimeLimits.class);
				ProcessValues.resetCounter(timeLimit);
				offerCounter.setId(offerCounter.getId());
				offerCounter.setRules(offerCounter.getRules());
				offerCounter.setDailyCount(timeLimit.getDailyCount());
				offerCounter.setWeeklyCount(timeLimit.getWeeklyCount());
				offerCounter.setMonthlyCount(timeLimit.getMonthlyCount());
				offerCounter.setAnnualCount(timeLimit.getAnnualCount());
				offerCounter.setLastPurchased(new Date());
				 
				if(!CollectionUtils.isEmpty(offerCounter.getDenominationCount())) {
					
					offerCounter.setDenominationCount(getResetDenominationCounterList(offerCounter.getDenominationCount()));
				}
				
				offerCounterListModified.add(offerCounter);
			}
		
		}
		
		return offerCounterListModified;
	}
	
	/**
	 * 
	 * @param accountOfferCountList
	 * @return list of account offer counts after modifying limits
	 */
	public static List<AccountOfferCounts> getResetAccountOfferCountList(List<AccountOfferCounts> accountOfferCountList) {
		
		List<AccountOfferCounts> accountOfferCountListModified =  null;
		if(!CollectionUtils.isEmpty(accountOfferCountList)) {
			
			accountOfferCountListModified =  new ArrayList<>(accountOfferCountList.size());
			
			for(AccountOfferCounts accountOffer : accountOfferCountList) {
			
				TimeLimits timeLimit = ObjectMapperUtils.map(accountOffer, TimeLimits.class);
				ProcessValues.resetCounter(timeLimit);
				accountOffer.setDailyCount(timeLimit.getDailyCount());
				accountOffer.setWeeklyCount(timeLimit.getWeeklyCount());
				accountOffer.setMonthlyCount(timeLimit.getMonthlyCount());
				accountOffer.setAnnualCount(timeLimit.getAnnualCount());
				accountOffer.setLastPurchased(new Date());
				
				if(!CollectionUtils.isEmpty(accountOffer.getDenominationCount())) {
					
					accountOffer.setDenominationCount(getResetDenominationCounterList(accountOffer.getDenominationCount()));
				}
				accountOfferCountListModified.add(accountOffer);
			}
		
		}
		
		return accountOfferCountListModified;
		
	}
	
	/**
	 * 
	 * @param memberOfferCountList
	 * @return list of member offer counts after modifying limits
	 */
	public static List<MemberOfferCounts> getResetMemberOfferCountList(List<MemberOfferCounts> memberOfferCountList) {
		
		List<MemberOfferCounts> memberOfferCountListModified =  null;

		if(!CollectionUtils.isEmpty(memberOfferCountList)) {
			
			memberOfferCountListModified =  new ArrayList<>(memberOfferCountList.size());
			
			for(MemberOfferCounts memberOffer : memberOfferCountList) {
			
				TimeLimits timeLimit = ObjectMapperUtils.map(memberOffer, TimeLimits.class);
				ProcessValues.resetCounter(timeLimit);
				memberOffer.setDailyCount(timeLimit.getDailyCount());
				memberOffer.setWeeklyCount(timeLimit.getWeeklyCount());
				memberOffer.setMonthlyCount(timeLimit.getMonthlyCount());
				memberOffer.setAnnualCount(timeLimit.getAnnualCount());
				memberOffer.setLastPurchased(new Date());
			
				if(!CollectionUtils.isEmpty(memberOffer.getDenominationCount())) {
					
					memberOffer.setDenominationCount(getResetDenominationCounterList(memberOffer.getDenominationCount()));
				}
				memberOfferCountListModified.add(memberOffer);
			}
		
		}
		
		return memberOfferCountListModified;
		
	}
	
	/**
	 * 
	 * @param denominationCountList
	 * @return list of denomination count after modifying limits
	 */
	private static List<DenominationCount> getResetDenominationCounterList(List<DenominationCount> denominationCountList){
		
		List<DenominationCount> denominationCountListModified = null;
		
		if(!CollectionUtils.isEmpty(denominationCountList)) {
			
			denominationCountListModified = new ArrayList<>(denominationCountList.size());
			
			for(DenominationCount denominationCount : denominationCountList) {
				
				TimeLimits timeLimitDenom = ObjectMapperUtils.map(denominationCount, TimeLimits.class);
				ProcessValues.resetCounter(timeLimitDenom);
				denominationCount.setDailyCount(timeLimitDenom.getDailyCount());
				denominationCount.setWeeklyCount(timeLimitDenom.getWeeklyCount());
				denominationCount.setMonthlyCount(timeLimitDenom.getMonthlyCount());
				denominationCount.setAnnualCount(timeLimitDenom.getAnnualCount());
				denominationCount.setLastPurchased(new Date());
				denominationCountListModified.add(denominationCount);
	       }
			
		}
		
		return denominationCountListModified;
	}

	/**
	 * Sets fields specific to account number in the eligible offer response
	 * @param offerCatalogDto
	 * @param offerCatalog
	 * @param eligibilityInfo
	 * @param conversionRateList 
	 */
	public static void setMemberAttributesInEligibleOffer(OfferCatalogResultResponseDto offerCatalogDto, EligibleOffers offerCatalog,
			EligibleOfferHelperDto eligibilityInfo, List<ConversionRate> conversionRateList) {
		
		//setEligibleOfferCostValuesForSubscribedUser(offerCatalogDto, offerCatalog, eligibilityInfo, conversionRateList);
		offerCatalogDto.setPaymentMethods(FilterValues.filterPaymenMethodDtoList(offerCatalogDto.getPaymentMethods(), Predicates.paymentMethodInEligiblePaymentMethodsForMember(MapValues.mapMemberPaymentMethods(eligibilityInfo.getMemberDetails().getEligiblePaymentMethod()))));
		offerCatalogDto.setStaticRating(null);
		setAverageRatingForEligibleOffer(offerCatalogDto, offerCatalog);
		setActiveStoresForEligibleOffer(offerCatalogDto, offerCatalog);
		setValidDenominationsForEligibleOffer(offerCatalog, offerCatalogDto, eligibilityInfo);
		offerCatalogDto.setSoldCount(!ObjectUtils.isEmpty(eligibilityInfo.getOfferCounter())
				? eligibilityInfo.getOfferCounter().getTotalCount() 
				: 0);
	}
	
	/**
	 * 
	 * @param weeklyPurchaseCount 
	 * @param referenceDetails
	 * @param offerId
	 * @return eligibility status for offer response for eligible offer
	 */
	public static void getEligibilityForEligibleOffer(PurchaseCount purchaseCount, 
			EligibleOfferHelperDto eligibilityInfo, ResultResponse resultResponse, 
			OfferCatalogResultResponseDto offerCatalogDto) {
		
	    if(!ObjectUtils.isEmpty(eligibilityInfo)) {
			
	    	if(ObjectUtils.isEmpty(offerCatalogDto.getEligibility())) {
	    		
	    		offerCatalogDto.setEligibility(new Eligibility());
	    		
	    	}
			
			if(Checks.checkCinemaOffer(eligibilityInfo.getOffer().getRules())) {
				
				offerCatalogDto.getEligibility().setFailureStatus(Checks.checkCustomerSegmentForCinemaOffers(eligibilityInfo.getRuleResult(),
						eligibilityInfo.getMemberDetails(), offerCatalogDto.getEligibility().getFailureStatus(), offerCatalogDto.getEligibility(),
						eligibilityInfo.getOffer().getCustomerSegments(), eligibilityInfo.getOffer().getRules(), resultResponse));
				Checks.checkCinemaOfferDownloadLimit(purchaseCount, eligibilityInfo.getMemberDetails().getCustomerSegment(), resultResponse);	
					
			} else {
				
				List<String> customerSegmentNames = Utilities.intersection(eligibilityInfo.getMemberDetails().getCustomerSegment(),
						MapValues.mapCustomerSegmentInOfferLimits(eligibilityInfo.getOffer().getLimit(), Predicates.isCustomerSegmentInLimits()));
				Checks.checkDownloadLimitLeftForEligibleOffer(purchaseCount, 0, eligibilityInfo, 
						null, resultResponse, customerSegmentNames, offerCatalogDto);
				
			}
			
			offerCatalogDto.getEligibility().setFailureStatus(Responses.setErrorMessage(OfferConstants.DOWNLOAD_LIMIT.get(), 
					offerCatalogDto.getEligibility(), offerCatalogDto.getEligibility().getFailureStatus(), resultResponse));
			offerCatalogDto.getEligibility().setStatus(CollectionUtils.isEmpty(offerCatalogDto.getEligibility().getFailureStatus()));
			
		}
		        	
	}

	/**
     * 
     * @param eligibilityInfo
     * @param purchaseRequest
     * @return counter for current offer
     */
    public static LimitCounter getOfferCounterForCurrentEligibleOffer(EligibleOfferHelperDto eligibilityInfo, Integer denomination, Integer couponQuantity) {
    	
    	LimitCounter counter = new LimitCounter();
		boolean isDenomination = !ObjectUtils.isEmpty(denomination);
		counter.setCouponQuantity(couponQuantity);
		counter.setOfferId(eligibilityInfo.getOffer().getOfferId());
		setAccountOfferCounterValues(counter, eligibilityInfo.getAccountOfferCounter(), denomination, couponQuantity, isDenomination);
		setOfferCounterValues(counter, eligibilityInfo.getOfferCounter(), denomination, couponQuantity, isDenomination);
		setMemberOfferCounterValues(counter, eligibilityInfo.getMemberOfferCounter(), denomination, couponQuantity, isDenomination);
		return counter;
    }
    
    /**
	 * Sets fields specific to account number in the offer response
	 * @param offerCatalogDto
	 * @param offerCatalog
	 * @param eligibilityInfo
	 */
	public static void setMemberAttributesInOffer(OfferCatalogResultResponseDto offerCatalogDto, OfferCatalog offerCatalog,
			EligibilityInfo eligibilityInfo, List<ConversionRate> conversionRateList) {
		
		//setCostValueForSubscribedUser(offerCatalog, offerCatalogDto, eligibilityInfo, conversionRateList);
		offerCatalogDto.setPaymentMethods(FilterValues.filterPaymenMethodDtoList(offerCatalogDto.getPaymentMethods(), Predicates.paymentMethodInEligiblePaymentMethodsForMember(MapValues.mapMemberPaymentMethods(eligibilityInfo.getMemberDetails().getEligiblePaymentMethod()))));
		offerCatalogDto.setStaticRating(null);
		setAverageRating(offerCatalogDto, offerCatalog);
		setActiveStores(offerCatalogDto, offerCatalog);
		setValidDenominations(offerCatalog, offerCatalogDto, eligibilityInfo);
		offerCatalogDto.setSoldCount(!ObjectUtils.isEmpty(eligibilityInfo.getOfferCounter())
				? eligibilityInfo.getOfferCounter().getTotalCount() 
				: 0);
		setQuantityLeft(offerCatalogDto, eligibilityInfo);
		
	}
	
		/**
	    * Adds error message after payment in the response 
	    * @param paymentResponse
	    * @param resultResponse
	    */
	   public static void addErrorMessageForPayment(PaymentResponse paymentResponse, ResultResponse resultResponse) {
	      
	       if(!ObjectUtils.isEmpty(paymentResponse)
	       && !ObjectUtils.isEmpty(paymentResponse.getPaymentStatus())) {
	          
	           if(!StringUtils.equalsIgnoreCase(paymentResponse.getPaymentStatus(), OfferConstants.SUCCESS.get())) {
	              
	               resultResponse.addErrorAPIResponse(paymentResponse.getErrorCode(), paymentResponse.getFailedreason());
	          
	           }
	          
	       } else {
	          
	           Responses.addErrorWithMessage(resultResponse, OfferErrorCodes.PAYMENT_FAILED, OfferConstants.ERROR_OCCURED.get());
	       }
	      
	   }

	   /**
	    * 
	    * @param promotionalGiftRequest
	    * @param promotionalGiftResult 
	    * @param headers
	    * @return purchase request for promotional gift offer
	    */
		public static PurchaseRequestDto getPurchaseRequestForPromotionalGiftOffer(
				PromotionalGiftRequestDto promotionalGiftRequest, OfferGiftValues offerGiftValues, Headers headers) {
			
			PurchaseRequestDto purchaseRequestDto = new PurchaseRequestDto();
			purchaseRequestDto.setUiLanguage(OffersConfigurationConstants.LANGUAGE_EN);
			purchaseRequestDto.setCouponQuantity(offerGiftValues.getCouponQuantity());
			purchaseRequestDto.setOfferId(offerGiftValues.getOfferId());
			purchaseRequestDto.setAccountNumber(promotionalGiftRequest.getAccountNumber());
			purchaseRequestDto.setSelectedOption(OffersConfigurationConstants.fullPoints);
			purchaseRequestDto.setPaymentType(OffersConfigurationConstants.fullPoints);
			purchaseRequestDto.setSpentAmount(OffersConfigurationConstants.ZERO_DOUBLE);
			purchaseRequestDto.setSpentPoints(OffersConfigurationConstants.ZERO_INTEGER);
			purchaseRequestDto.setExtTransactionId(headers.getExternalTransactionId());
			if(Checks.checkIsCashVoucher(offerGiftValues.getOfferType())) {
				purchaseRequestDto.setVoucherDenomination(offerGiftValues.getDenomination());
			}
			if(Checks.checkIsDealVoucher(offerGiftValues.getOfferType())) {
				purchaseRequestDto.setSubOfferId(offerGiftValues.getSubOfferId());	
			}
			purchaseRequestDto.setSelectedPaymentItem(ProcessValues.getPurchaseItemFromOfferType(offerGiftValues.getOfferType()));
			return purchaseRequestDto;
		}
		
		
		/**
		 * 
		 * @param promotionalGiftRequest
		 * @param promotionalGiftResult
		 * @param subscriptionValues 
		 * @param headers
		 * @return purchase request for promotional gift subscription
		 */
		public static PurchaseRequestDto getPurchaseRequestForPromotionalGiftSubscription(
				PromotionalGiftRequestDto promotionalGiftRequest, String subscriptionCatalogId, SubscriptionValues subscriptionValues, Headers headers) {
			
			PurchaseRequestDto purchaseRequestDto = new PurchaseRequestDto();
			purchaseRequestDto.setUiLanguage(OffersConfigurationConstants.LANGUAGE_EN);
			purchaseRequestDto.setAccountNumber(promotionalGiftRequest.getAccountNumber());
			purchaseRequestDto.setSelectedPaymentItem(OffersConfigurationConstants.subscriptionItem);
			purchaseRequestDto.setSelectedOption(OffersConfigurationConstants.fullPoints);
			purchaseRequestDto.setPaymentType(OffersConfigurationConstants.fullPoints);
			purchaseRequestDto.setSpentAmount(OffersConfigurationConstants.ZERO_DOUBLE);
			purchaseRequestDto.setSpentPoints(subscriptionValues.getPointsValue());
			purchaseRequestDto.setCouponQuantity(1);
			purchaseRequestDto.setExtTransactionId(headers.getExternalTransactionId());
			purchaseRequestDto.setSubscriptionCatalogId(subscriptionCatalogId);
			return purchaseRequestDto;
		}

		/**
		 * Mock method
		 * @param purchaseRequestDto 
		 * @param promotionalGiftRequest
		 * @param promotionalGiftResult
		 * @param purchaseResultResponse
		 * @param headers
		 * @return
		 */
		public static PurchaseResultResponse getSubscriptionData(PurchaseRequestDto purchaseRequestDto, PromotionalGiftRequestDto promotionalGiftRequest,
				PromotionalGiftResult promotionalGiftResult, PurchaseResultResponse purchaseResultResponse,
				Headers headers) {
			
			PurchaseResponseDto purchaseResponseDto = new PurchaseResponseDto();
			purchaseResponseDto.setActivityCode("activityCode");
			purchaseResponseDto.setPartnerCode("ES");
			purchaseResponseDto.setTransactionNo("transactionNo");
			purchaseResultResponse.setPurchaseResponseDto(purchaseResponseDto);
			return purchaseResultResponse;
		}

		/***
		 * 
		 * @param subscriptionGifted
		 * @param voucherGifted
		 * @param isOffer 
		 * @param isSubscription 
		 * @param isSubscribed 
		 * @return error code for promotional gift
		 */
		public static OfferErrorCodes getErrorCodeForPromotionalGift(PromotionalGiftHelper promotionalGiftHelper) {
			
			OfferErrorCodes errorCode = OfferErrorCodes.PROMOTIONAL_GIFT_FAILED;
			
			if(promotionalGiftHelper.isSubscription() && promotionalGiftHelper.isOffer()) {
				
				errorCode = OfferErrorCodes.PROMOTIONAL_BOTH_GIFT_FAILED;
					
			} else if (promotionalGiftHelper.isOffer()) {
				
				errorCode = OfferErrorCodes.PROMOTIONAL_VOUCHER_GIFT_FAILED;
			
			} else if(promotionalGiftHelper.isSubscription()) {
			
				errorCode = OfferErrorCodes.PROMOTIONAL_GIFT_SUBSCRIPTION_FAILED;
			}
			
			return errorCode;
		}

		/**
		 * 
		 * @param subscriptionGifted
		 * @param voucherGifted
		 * @param isSubscription
		 * @param isOffer
		 * @param isSubscribed 
		 * @param promotionalGiftResultResponse
		 * @return success code for promotional gift
		 */
		public static OfferSuccessCodes getSuccessCodeForPromotionalGift(PromotionalGiftHelper promotionalGiftHelper) {
			
			OfferSuccessCodes successCode = OfferSuccessCodes.PROMOTIONAL_GIFT_SUCCESS;
			
			if(promotionalGiftHelper.isSubscription() && promotionalGiftHelper.isOffer()) {
				
				successCode = getSuccessCodeForPromoWithBothSubscriptionAndVoucher(promotionalGiftHelper); 
				
			} else if(promotionalGiftHelper.isSubscription() && promotionalGiftHelper.isSubscriptionGifted()) {
				
				successCode = OfferSuccessCodes.PROMOTIONAL_SUBSCRIPTION_SUCCESS;
				
			} else if(promotionalGiftHelper.isOffer()) {
				
				successCode = OfferSuccessCodes.PROMOTIONAL_VOUCHER_SUCCESS;
						
			} 
			
			return successCode;
		}

		/**
		 * 
		 * @param subscriptionGifted
		 * @param voucherGifted
		 * @param isOffer
		 * @param isSubscribed 
		 * @param promotionalGiftResultResponse
		 */
		private static OfferSuccessCodes getSuccessCodeForPromoWithBothSubscriptionAndVoucher(PromotionalGiftHelper promotionalGiftHelper) {
			
			OfferSuccessCodes successCode = null;
			
			if(promotionalGiftHelper.isVoucherGifted() && promotionalGiftHelper.isSubscriptionGifted()) {
				
				successCode = OfferSuccessCodes.PROMOTIONAL_BOTH_GIFT_SUCCESS;
				
			} else if(promotionalGiftHelper.isSubscriptionGifted()) {
				
				successCode = OfferSuccessCodes.PROMOTIONAL_VOUCHER_GIFT_FAILED_SUBSCRIPTION_GIFTED;
				
			} else if(promotionalGiftHelper.isVoucherGifted()) { 
				
				successCode = OfferSuccessCodes.PROMOTIONAL_GIFT_SUBSCRIPTION_FAILED_VOUCHER_GIFTED;
				
			}
			
			return successCode;
		}
		
		/**
		 * Sets final values for promotional gift response
		 * @param subscriptionResponse
		 * @param voucherResponse
		 * @param promotionalGiftResultResponse
		 */
		public static void setPromotionalGiftResponse(PromotionalGiftHelper promotionalGiftHelper, PromotionalGiftResultResponse promotionalGiftResultResponse) {
			
			if(!ObjectUtils.isEmpty(promotionalGiftResultResponse)) {
				
				if(ObjectUtils.isEmpty(promotionalGiftResultResponse.getPromotionalGiftResponseDto())) {
					
					promotionalGiftResultResponse.setPromotionalGiftResponseDto(new PromotionalGiftResponseDto());
				}
				
				if(!ObjectUtils.isEmpty(promotionalGiftHelper.getVoucherResponse()) && !ObjectUtils.isEmpty(promotionalGiftHelper.getVoucherResponse().getPurchaseResponseDto())) {
					
					promotionalGiftResultResponse.getPromotionalGiftResponseDto().setVoucherActivityCode(promotionalGiftHelper.getVoucherResponse().getPurchaseResponseDto().getActivityCode());
					promotionalGiftResultResponse.getPromotionalGiftResponseDto().setVoucherPartnerCode(promotionalGiftHelper.getVoucherResponse().getPurchaseResponseDto().getPartnerCode());
					promotionalGiftResultResponse.getPromotionalGiftResponseDto().setVoucherCodes(promotionalGiftHelper.getVoucherResponse().getPurchaseResponseDto().getVoucherCodes());
					promotionalGiftResultResponse.getPromotionalGiftResponseDto().setVoucherEarnPointsRate(promotionalGiftHelper.getVoucherResponse().getPurchaseResponseDto().getEarnPointsRate());
					promotionalGiftResultResponse.getPromotionalGiftResponseDto().setVoucherTransactionNo(promotionalGiftHelper.getVoucherResponse().getPurchaseResponseDto().getTransactionNo());
					promotionalGiftResultResponse.getPromotionalGiftResponseDto().setVoucherStatus(promotionalGiftHelper.getVoucherResponse().getPurchaseResponseDto().getPaymentStatus());
					promotionalGiftResultResponse.getPromotionalGiftResponseDto().setVoucherEpgTransactionId(promotionalGiftHelper.getVoucherResponse().getPurchaseResponseDto().getEpgTransactionId());
					promotionalGiftResultResponse.getPromotionalGiftResponseDto().setVoucherAdditionalParams(promotionalGiftHelper.getVoucherResponse().getPurchaseResponseDto().getAdditionalParams());
				}
				
				if(!ObjectUtils.isEmpty(promotionalGiftHelper.getSubscriptionResponse()) && !ObjectUtils.isEmpty(promotionalGiftHelper.getSubscriptionResponse().getPurchaseResponseDto())) {
					
					promotionalGiftResultResponse.getPromotionalGiftResponseDto().setSubscriptionStatus(promotionalGiftHelper.getSubscriptionResponse().getPurchaseResponseDto().getPaymentStatus());
					promotionalGiftResultResponse.getPromotionalGiftResponseDto().setSubscriptionTransactionNo(promotionalGiftHelper.getSubscriptionResponse().getPurchaseResponseDto().getTransactionNo());
					promotionalGiftResultResponse.getPromotionalGiftResponseDto().setSubscriptionEndDate(promotionalGiftHelper.getSubscriptionResponse().getPurchaseResponseDto().getSubscriptionEndDate());
					promotionalGiftResultResponse.getPromotionalGiftResponseDto().setSubscriptionId(promotionalGiftHelper.getSubscriptionResponse().getPurchaseResponseDto().getSubscriptionId());
					promotionalGiftResultResponse.getPromotionalGiftResponseDto().setSubscriptionAdditionalParams(promotionalGiftHelper.getSubscriptionResponse().getPurchaseResponseDto().getAdditionalParams());
				}
				
			}
			
			
		}

		/**
		 * 
		 * @param subscriptionGifted
		 * @param voucherGifted
		 * @param isSubscription
		 * @param isOffer
		 * @param isSubscribed 
		 * @param promotionalGiftResultResponse
		 * @param subscriptionResponse
		 * @param voucherResponse
		 */
		public static void addErrorsForPromotionalGift(PromotionalGiftHelper promotionalGiftHelper, PromotionalGiftResultResponse promotionalGiftResultResponse) {
			
			if(!ObjectUtils.isEmpty(promotionalGiftResultResponse)) {
			
				boolean failedGift = promotionalGiftHelper.isSubscription() && promotionalGiftHelper.isOffer() 
						&& !promotionalGiftHelper.isSubscriptionGifted() 
						&& !promotionalGiftHelper.isVoucherGifted();
				boolean failedOffer = !promotionalGiftHelper.isSubscription() && promotionalGiftHelper.isOffer()  
						&& !promotionalGiftHelper.isVoucherGifted();
				boolean failedSubscription = !promotionalGiftHelper.isOffer() && promotionalGiftHelper.isSubscription() 
						&& !promotionalGiftHelper.isSubscriptionGifted();
			    addErrorResponseForPromotionalSubscription(failedGift, failedSubscription, promotionalGiftHelper.getSubscriptionResponse(), promotionalGiftResultResponse);
				addErrorResponseForPromotionalVoucher(failedGift, failedOffer, promotionalGiftHelper.getVoucherResponse(), promotionalGiftResultResponse);
				
			}
			
		}

		/**
		 * Sets error response for promotional subscription
		 * @param failedGift
		 * @param failedSubscription
		 * @param alreadySubscribed 
		 * @param subscriptionResponse
		 * @param promotionalGiftResultResponse 
		 */
		private static void addErrorResponseForPromotionalSubscription(boolean failedGift, boolean failedSubscription,
				PurchaseResultResponse subscriptionResponse, PromotionalGiftResultResponse promotionalGiftResultResponse) {
			
			if(failedGift || failedSubscription) {
				
				if(Checks.checkErrorsPresent(subscriptionResponse)) {
					
					subscriptionResponse.getApiStatus().getErrors().forEach(e->promotionalGiftResultResponse.addErrorAPIResponse(e.getCode(), e.getMessage()));
					
				} else if(ObjectUtils.isEmpty(subscriptionResponse.getPurchaseResponseDto())) {
					
					promotionalGiftResultResponse.addErrorAPIResponse(OfferErrorCodes.PROPER_RESPONSE_NOT_RECIEVED_AFTER_SUBSCRUPTION_GIFTING.getIntId(), OfferErrorCodes.PROPER_RESPONSE_NOT_RECIEVED_AFTER_SUBSCRUPTION_GIFTING.getMsg());
				}
				
			} 
			
		}
		
		/**
		 * Sets error response for promotional voucher
		 * @param failedGift
		 * @param failedOffer
		 * @param voucherResponse
		 * @param promotionalGiftResultResponse
		 */
		private static void addErrorResponseForPromotionalVoucher(boolean failedGift, boolean failedOffer,
				PurchaseResultResponse voucherResponse, PromotionalGiftResultResponse promotionalGiftResultResponse) {
			
			if(failedGift || failedOffer) {
				
				if(Checks.checkErrorsPresent(voucherResponse)) {
					
					voucherResponse.getApiStatus().getErrors().forEach(e->promotionalGiftResultResponse.addErrorAPIResponse(e.getCode(), e.getMessage()));
					
				} else if(ObjectUtils.isEmpty(voucherResponse.getPurchaseResponseDto())) {
					
					promotionalGiftResultResponse.addErrorAPIResponse(OfferErrorCodes.PROPER_RESPONSE_NOT_RECIEVED_AFTER_VOUCHER_GIFTING.getIntId(), OfferErrorCodes.PROPER_RESPONSE_NOT_RECIEVED_AFTER_VOUCHER_GIFTING.getMsg());
				}
				
			}
			
		}
		
	   /**
	    * Set equivalent aed values for full points method
	    * @param purchaseRequest
	    * @param eligibilityInfo
	    * @param purchaseResultResponse 
	    */
	   public static void setValuesForFullPointsMethod(PurchaseRequestDto purchaseRequest, EligibilityInfo eligibilityInfo, PurchaseResultResponse purchaseResultResponse) {
			
		   if(Checks.checkIsFullPointsPaymentMethod(purchaseRequest.getSelectedOption())
			&& !ObjectUtils.isEmpty(eligibilityInfo)
			&& !ObjectUtils.isEmpty(eligibilityInfo.getAmountInfo())) {
				
			   eligibilityInfo.getAmountInfo().setPurchaseAmount(getEquivalentAmount(eligibilityInfo.getConversionRateList(), (int) Math.round(eligibilityInfo.getAmountInfo().getPurchaseAmount()), purchaseResultResponse));
			   eligibilityInfo.getAmountInfo().setVatAmount(getEquivalentAmount(eligibilityInfo.getConversionRateList(), (int) Math.round(eligibilityInfo.getAmountInfo().getVatAmount()), purchaseResultResponse));
			
			}
			
	   }
	   
	   /***
	    * 
	    * @param customerSegmentNames 
		* @param isCustomerSegment 
		* @param limit
		* @return
		*/
		public static LimitCounterWithDenominationList getLimitForOfferEligibility(String offerId, List<OfferLimit> limitList, boolean isCustomerSegment, List<String> customerSegmentNames, List<Integer> denominations) {
			
		   LimitCounterWithDenominationList limit = null;
		   
		   if(!CollectionUtils.isEmpty(limitList))
			   
			   limit = new LimitCounterWithDenominationList();
		       OfferLimit offerLimit = null; 
		   	   if(!isCustomerSegment) {
					
		   			offerLimit = FilterValues.findAnyLimitWithinLimitList(limitList, Predicates.noCustomerSegmentInLimits());
					
			   } else if(CollectionUtils.isNotEmpty(customerSegmentNames)){
					
					List<OfferLimit> offerLimits = FilterValues.filterOfferLimits(limitList, Predicates.customerSegmentInListForLimit(customerSegmentNames));
					offerLimit = creatOfferLimitForCustomerSegment(offerLimits, customerSegmentNames, denominations); 
			   }
			   
			   if(!ObjectUtils.isEmpty(offerLimit)) {
					
					limit.setOfferId(offerId);	
					setOfferLimitForEligibility(limit, offerLimit);
					setAccountOfferLimitForEligibility(limit, offerLimit);
					setMemberOfferLimitForEligibility(limit, offerLimit);
					
					if(!CollectionUtils.isEmpty(denominations)) {
						setDenominationLimitForEligibility(limit, offerLimit, denominations);
					}
					
			   }
			   
	       return limit;
		}
	    
		/***
		 * 
		 * @param offerLimits
		 * @param customerSegmentNames 
		 * @param denominations 
		 * @return
		 */
		private static OfferLimit creatOfferLimitForCustomerSegment(List<OfferLimit> offerLimits, List<String> customerSegmentNames, List<Integer> denominations) {
		
			OfferLimit offerLimit = null;
			
			if(!CollectionUtils.isEmpty(offerLimits)) {
				
				offerLimit = new OfferLimit();
				offerLimit.setDailyLimit(getOfferLimitsForCustomerSegment(offerLimits,customerSegmentNames, OfferConstants.OFFER_COUNTER.get(), OfferConstants.DAILY_LIMIT.get()));
				offerLimit.setWeeklyLimit(getOfferLimitsForCustomerSegment(offerLimits, customerSegmentNames, OfferConstants.OFFER_COUNTER.get(), OfferConstants.WEEKLY_LIMIT.get()));
				offerLimit.setMonthlyLimit(getOfferLimitsForCustomerSegment(offerLimits, customerSegmentNames, OfferConstants.OFFER_COUNTER.get(), OfferConstants.MONTHLY_LIMIT.get()));
				offerLimit.setAnnualLimit(getOfferLimitsForCustomerSegment(offerLimits, customerSegmentNames, OfferConstants.OFFER_COUNTER.get(), OfferConstants.ANNUAL_LIMIT.get()));
				offerLimit.setDownloadLimit(getOfferLimitsForCustomerSegment(offerLimits, customerSegmentNames, OfferConstants.OFFER_COUNTER.get(), OfferConstants.TOTAL_LIMIT.get()));
				offerLimit.setAccountDailyLimit(getOfferLimitsForCustomerSegment(offerLimits, customerSegmentNames, OfferConstants.ACCOUNT_OFFER_COUNTER.get(), OfferConstants.DAILY_LIMIT.get()));
				offerLimit.setAccountWeeklyLimit(getOfferLimitsForCustomerSegment(offerLimits, customerSegmentNames, OfferConstants.ACCOUNT_OFFER_COUNTER.get(), OfferConstants.WEEKLY_LIMIT.get()));
				offerLimit.setAccountMonthlyLimit(getOfferLimitsForCustomerSegment(offerLimits, customerSegmentNames, OfferConstants.ACCOUNT_OFFER_COUNTER.get(), OfferConstants.MONTHLY_LIMIT.get()));
				offerLimit.setAccountAnnualLimit(getOfferLimitsForCustomerSegment(offerLimits, customerSegmentNames, OfferConstants.ACCOUNT_OFFER_COUNTER.get(), OfferConstants.ANNUAL_LIMIT.get()));
				offerLimit.setAccountTotalLimit(getOfferLimitsForCustomerSegment(offerLimits, customerSegmentNames, OfferConstants.ACCOUNT_OFFER_COUNTER.get(), OfferConstants.TOTAL_LIMIT.get()));
				offerLimit.setMemberDailyLimit(getOfferLimitsForCustomerSegment(offerLimits, customerSegmentNames, OfferConstants.MEMBER_OFFER_COUNTER.get(), OfferConstants.DAILY_LIMIT.get()));
				offerLimit.setMemberWeeklyLimit(getOfferLimitsForCustomerSegment(offerLimits, customerSegmentNames, OfferConstants.MEMBER_OFFER_COUNTER.get(), OfferConstants.WEEKLY_LIMIT.get()));
				offerLimit.setMemberMonthlyLimit(getOfferLimitsForCustomerSegment(offerLimits, customerSegmentNames, OfferConstants.MEMBER_OFFER_COUNTER.get(), OfferConstants.MONTHLY_LIMIT.get()));
				offerLimit.setMemberAnnualLimit(getOfferLimitsForCustomerSegment(offerLimits, customerSegmentNames, OfferConstants.MEMBER_OFFER_COUNTER.get(), OfferConstants.ANNUAL_LIMIT.get()));
				offerLimit.setMemberTotalLimit(getOfferLimitsForCustomerSegment(offerLimits, customerSegmentNames, OfferConstants.MEMBER_OFFER_COUNTER.get(), OfferConstants.TOTAL_LIMIT.get()));
		       	setDenominationLimitValuesForEligibility(offerLimit, offerLimits, denominations);	
				
			}
			
			return offerLimit;
		}
		
		/***
		 * 
		 * @param offerLimit
		 * @param offerLimits
		 * @param denominations
		 */
		private static void setDenominationLimitValuesForEligibility(OfferLimit offerLimit, List<OfferLimit> offerLimits, List<Integer> denominations) {
			
			if(!CollectionUtils.isEmpty(denominations)) {
				
				for(Integer denomination : denominations) {
					
					DenominationLimit offerDenominationLimit = getDenominationLimit(filterOfferDenominationLimitFromSegmentOfferLimit(offerLimits, denomination));
					
					if(!ObjectUtils.isEmpty(offerDenominationLimit)) {
						
						if(ObjectUtils.isEmpty(offerLimit.getDenominationLimit())) {
							
							offerLimit.setDenominationLimit(new ArrayList<>(1));
						}
						offerLimit.getDenominationLimit().add(offerDenominationLimit);
						
					}
					
					DenominationLimit accountDenominationLimit = getDenominationLimit(filterAccountOfferDenominationLimitFromSegmentOfferLimit(offerLimits, denomination));
					
					if(!ObjectUtils.isEmpty(accountDenominationLimit)) {
						
						if(ObjectUtils.isEmpty(offerLimit.getAccountDenominationLimit())) {
							
							offerLimit.setDenominationLimit(new ArrayList<>(1));
						}
						offerLimit.getAccountDenominationLimit().add(accountDenominationLimit);
						
					}
					
					DenominationLimit memberDenominationLimit = getDenominationLimit(filterMemberOfferDenominationLimitFromSegmentOfferLimit(offerLimits, denomination));
				
					if(!ObjectUtils.isEmpty(memberDenominationLimit)) {
						
						if(ObjectUtils.isEmpty(offerLimit.getMemberDenominationLimit())) {
							
							offerLimit.setDenominationLimit(new ArrayList<>(1));
						}
						offerLimit.getMemberDenominationLimit().add(memberDenominationLimit);
						
					}
				
				
				}
				
			}
			
		}

		/**
		 * 
		 * @param denominationLimitList
		 * @return
		 */
		private static DenominationLimit getDenominationLimit(List<DenominationLimit> denominationLimitList) {
			
			DenominationLimit denominationLimit = new DenominationLimit();
			denominationLimit.setDailyLimit(MapValues.mapDailyLimitForDenomination(denominationLimitList));
			denominationLimit.setWeeklyLimit(MapValues.mapWeeklyLimitForDenomination(denominationLimitList));
			denominationLimit.setMonthlyLimit(MapValues.mapMonthlyLimitForDenomination(denominationLimitList));
			denominationLimit.setAnnualLimit(MapValues.mapAnnualLimitForDenomination(denominationLimitList));
			denominationLimit.setTotalLimit(MapValues.mapTotalLimitForDenomination(denominationLimitList));
			return denominationLimit;
		}

		/**
		 * Sets default offer limit
		 * @param limit
		 * @param offerLimit
		 */
		private static void setOfferLimitForEligibility(LimitCounterWithDenominationList limit, OfferLimit offerLimit) {
			
			if(!ObjectUtils.isEmpty(limit) && !ObjectUtils.isEmpty(offerLimit)) {
				
				if(!ObjectUtils.isEmpty(offerLimit.getDailyLimit())) {
					
					limit.setOfferDaily(offerLimit.getDailyLimit());
				}
				
				if(!ObjectUtils.isEmpty(offerLimit.getWeeklyLimit())) {
					
					limit.setOfferWeekly(offerLimit.getWeeklyLimit());
				}
		
				if(!ObjectUtils.isEmpty(offerLimit.getMonthlyLimit())) {
					
					limit.setOfferMonthly(offerLimit.getMonthlyLimit());
				}
				
				if(!ObjectUtils.isEmpty(offerLimit.getAnnualLimit())) {
					
					limit.setOfferAnnual(offerLimit.getAnnualLimit());
				}
				
				if(!ObjectUtils.isEmpty(offerLimit.getDownloadLimit())) {
					
					limit.setOfferTotal(offerLimit.getDownloadLimit());
				}
				
			}
			
		}
			
		/**
		 * Sets default account offer limit
		 * @param limit
		 * @param offerLimit
		 */
		private static void setAccountOfferLimitForEligibility(LimitCounterWithDenominationList limit, OfferLimit offerLimit) {
			
			if(!ObjectUtils.isEmpty(limit) && !ObjectUtils.isEmpty(offerLimit)) {
				
				if(!ObjectUtils.isEmpty(offerLimit.getAccountDailyLimit())) {
					
					limit.setAccountOfferDaily(offerLimit.getAccountDailyLimit());
				}
				
				if(!ObjectUtils.isEmpty(offerLimit.getAccountWeeklyLimit())) {
					
					limit.setAccountOfferWeekly(offerLimit.getAccountWeeklyLimit());
				}
		
				if(!ObjectUtils.isEmpty(offerLimit.getAccountMonthlyLimit())) {
					
					limit.setAccountOfferMonthly(offerLimit.getAccountMonthlyLimit());
				}
				
				if(!ObjectUtils.isEmpty(offerLimit.getAccountAnnualLimit())) {
					
					limit.setAccountOfferAnnual(offerLimit.getAccountAnnualLimit());
				}
				
				if(!ObjectUtils.isEmpty(offerLimit.getAccountTotalLimit())) {
					
					limit.setAccountOfferTotal(offerLimit.getAccountTotalLimit());
				}
		
			}
			
		}

		/**
		 * Sets default member offer limit
		 * @param limit
		 * @param offerLimit
		 */
		private static void setMemberOfferLimitForEligibility(LimitCounterWithDenominationList limit, OfferLimit offerLimit) {
			
			if(!ObjectUtils.isEmpty(limit) && !ObjectUtils.isEmpty(offerLimit)) {
				
				if(!ObjectUtils.isEmpty(offerLimit.getMemberDailyLimit())) {
					
					limit.setMemberOfferDaily(offerLimit.getMemberDailyLimit());
				}
				
				if(!ObjectUtils.isEmpty(offerLimit.getMemberWeeklyLimit())) {
					
					limit.setMemberOfferWeekly(offerLimit.getMemberWeeklyLimit());
				}
		
				if(!ObjectUtils.isEmpty(offerLimit.getMemberMonthlyLimit())) {
					
					limit.setMemberOfferMonthly(offerLimit.getMemberMonthlyLimit());
				}
				
				if(!ObjectUtils.isEmpty(offerLimit.getMemberAnnualLimit())) {
					
					limit.setMemberOfferAnnual(offerLimit.getMemberAnnualLimit());
				}
				
				if(!ObjectUtils.isEmpty(offerLimit.getMemberTotalLimit())) {
					
					limit.setMemberOfferTotal(offerLimit.getMemberTotalLimit());
				}
				
			}
			
		}

		/**
		 * Sets default offer denomination limit
		 * @param isDenomination
		 * @param limit
		 * @param offerLimit
		 * @param denominations 
		 * @param denomination
		 */
		private static void setDenominationLimitForEligibility(LimitCounterWithDenominationList limit,
				OfferLimit offerLimit, List<Integer> denominations) {
			
			if(!CollectionUtils.isEmpty(denominations)
			&& !ObjectUtils.isEmpty(offerLimit)
			&& (!CollectionUtils.isEmpty(offerLimit.getDenominationLimit())
			 || !CollectionUtils.isEmpty(offerLimit.getAccountDenominationLimit())
			 || !CollectionUtils.isEmpty(offerLimit.getMemberDenominationLimit()))) {
				
				if(CollectionUtils.isEmpty(limit.getDenominationList())) {
					
					limit.setDenominationList(new ArrayList<>(1));
				}
				
				for(Integer denomination : denominations) {
					
					DenominationLimit offerDenominationLimit = getDenominationLimit(offerLimit.getDenominationLimit(), denomination);
					DenominationLimit accountDenominationLimit = getDenominationLimit(offerLimit.getAccountDenominationLimit(), denomination);
					DenominationLimit memberDenominationLimit = getDenominationLimit(offerLimit.getMemberDenominationLimit(), denomination);
					
					if(!ObjectUtils.isEmpty(offerDenominationLimit)) {
						
						DenominationLimitCounter denominationLimitCounter = new DenominationLimitCounter();
						denominationLimitCounter.setDenomination(denomination);
						setDenominationLimitForOfferEligibility(denominationLimitCounter, offerDenominationLimit);
						setDenominationLimitForAccountEligibility(denominationLimitCounter, accountDenominationLimit);
						setDenominationLimitForMemberEligibility(denominationLimitCounter, memberDenominationLimit);
						limit.getDenominationList().add(denominationLimitCounter);
					}
					
				}
			}
		}
		
		/**
		 * 
		 * @param denominationLimitList
		 * @param denomination
		 * @return
		 */
		private static DenominationLimit getDenominationLimit(List<DenominationLimit> denominationLimitList,
			Integer denomination) {
		
			return !CollectionUtils.isEmpty(denominationLimitList)
				? denominationLimitList.stream().filter(d->d.getDenomination().equals(denomination))
						 .findAny().orElse(null)
				: null;
		}

		/***
		 * 
		 * @param denominationLimitCounter
		 * @param offerDenominationLimit
		 */
		private static void setDenominationLimitForOfferEligibility(
				DenominationLimitCounter denominationLimitCounter, DenominationLimit denominationLimit) {
			
			if(!ObjectUtils.isEmpty(denominationLimit.getDailyLimit())) {
				denominationLimitCounter.setOfferDaily(denominationLimit.getDailyLimit());
			}
			
			if(!ObjectUtils.isEmpty(denominationLimit.getWeeklyLimit())) {
				
				denominationLimitCounter.setOfferWeekly(denominationLimit.getWeeklyLimit());
			}

			if(!ObjectUtils.isEmpty(denominationLimit.getMonthlyLimit())) {
				
				denominationLimitCounter.setOfferMonthly(denominationLimit.getMonthlyLimit());
			}
			
			if(!ObjectUtils.isEmpty(denominationLimit.getAnnualLimit())) {
				
				denominationLimitCounter.setOfferAnnual(denominationLimit.getAnnualLimit());
			}
			
			if(!ObjectUtils.isEmpty(denominationLimit.getTotalLimit())) {
				
				denominationLimitCounter.setOfferTotal(denominationLimit.getTotalLimit());
			}
			
		}

		/**
		 * 
		 * @param denominationLimitCounter
		 * @param accountDenominationLimit
		 */
		private static void setDenominationLimitForAccountEligibility(
			DenominationLimitCounter denominationLimitCounter, DenominationLimit denominationLimit) {
			
			if(!ObjectUtils.isEmpty(denominationLimit.getDailyLimit())) {
				denominationLimitCounter.setAccountDaily(denominationLimit.getDailyLimit());
			}
			
			if(!ObjectUtils.isEmpty(denominationLimit.getWeeklyLimit())) {
				
				denominationLimitCounter.setAccountWeekly(denominationLimit.getWeeklyLimit());
			}

			if(!ObjectUtils.isEmpty(denominationLimit.getMonthlyLimit())) {
				
				denominationLimitCounter.setAccountMonthly(denominationLimit.getMonthlyLimit());
			}
			
			if(!ObjectUtils.isEmpty(denominationLimit.getAnnualLimit())) {
				
				denominationLimitCounter.setAccountAnnual(denominationLimit.getAnnualLimit());
			}
			
			if(!ObjectUtils.isEmpty(denominationLimit.getTotalLimit())) {
				
				denominationLimitCounter.setAccountTotal(denominationLimit.getTotalLimit());
			}
		
		}

		/**
		 * 
		 * @param denominationLimitCounter
		 * @param memberDenominationLimit
		 */
		private static void setDenominationLimitForMemberEligibility(
			DenominationLimitCounter denominationLimitCounter, DenominationLimit denominationLimit) {
			
			if(!ObjectUtils.isEmpty(denominationLimit.getDailyLimit())) {
				denominationLimitCounter.setMemberDaily(denominationLimit.getDailyLimit());
			}
			
			if(!ObjectUtils.isEmpty(denominationLimit.getWeeklyLimit())) {
				
				denominationLimitCounter.setMemberWeekly(denominationLimit.getWeeklyLimit());
			}

			if(!ObjectUtils.isEmpty(denominationLimit.getMonthlyLimit())) {
				
				denominationLimitCounter.setMemberMonthly(denominationLimit.getMonthlyLimit());
			}
			
			if(!ObjectUtils.isEmpty(denominationLimit.getAnnualLimit())) {
				
				denominationLimitCounter.setMemberAnnual(denominationLimit.getAnnualLimit());
			}
			
			if(!ObjectUtils.isEmpty(denominationLimit.getTotalLimit())) {
				
				denominationLimitCounter.setOfferTotal(denominationLimit.getTotalLimit());
			}
		
		}

		/***
		 * 
		 * @param offerId
		 * @param offerCounter
		 * @param isCustomerSegment
		 * @param customerSegmentNames
		 * @param denominations
		 * @return
		 */
		public static LimitCounterWithDenominationList getCounterForOfferEligibility(String offerId,
				AccountOfferCounts accountOfferCounter, List<Integer> denominations) {
			
			LimitCounterWithDenominationList counter = new LimitCounterWithDenominationList();
			OfferCounters offerCounter = !ObjectUtils.isEmpty(accountOfferCounter)
					? accountOfferCounter.getOfferCounter()
					: null;
			MemberOfferCounts memberOfferCounter = !ObjectUtils.isEmpty(accountOfferCounter)
					? accountOfferCounter.getMemberOfferCounter()
					: null;
			counter.setOfferId(offerId);
			
			if(ObjectUtils.isEmpty(accountOfferCounter)) {
				
				accountOfferCounter = new AccountOfferCounts(null, null, null, null, 0, 0, 0, 0, 0, null, null, null, null);
				
			} 
			
			if(ObjectUtils.isEmpty(offerCounter)) {
				
				offerCounter = new OfferCounters(null, null, null, 0, 0, 0, 0, 0, null, null);
				
			} 

			if(ObjectUtils.isEmpty(memberOfferCounter)) {
				
				memberOfferCounter = new MemberOfferCounts(null, null, null, 0, 0, 0, 0, 0, null, null);
				
			} 
			
			counter.setAccountOfferDaily(accountOfferCounter.getDailyCount());
			counter.setAccountOfferWeekly(accountOfferCounter.getWeeklyCount());
			counter.setAccountOfferMonthly(accountOfferCounter.getMonthlyCount());
			counter.setAccountOfferAnnual(accountOfferCounter.getAnnualCount());
			counter.setAccountOfferTotal(accountOfferCounter.getTotalCount());
			counter.setOfferDaily(offerCounter.getDailyCount());
			counter.setOfferWeekly(offerCounter.getWeeklyCount());
			counter.setOfferMonthly(offerCounter.getMonthlyCount());
			counter.setOfferAnnual(offerCounter.getAnnualCount());
			counter.setOfferTotal(offerCounter.getTotalCount());
			counter.setMemberOfferDaily(memberOfferCounter.getDailyCount());
			counter.setMemberOfferWeekly(memberOfferCounter.getWeeklyCount());
			counter.setMemberOfferMonthly(memberOfferCounter.getMonthlyCount());
			counter.setMemberOfferAnnual(memberOfferCounter.getAnnualCount());
			counter.setMemberOfferTotal(memberOfferCounter.getTotalCount());
			
			if(!CollectionUtils.isEmpty(denominations)) {
				
			   counter.setDenominationList(new ArrayList<>(1));	
			   for(Integer denomination : denominations) {
				   
				   counter.getDenominationList().add(setDenominationValueInCounterForEligibility(counter, denomination, accountOfferCounter.getDenominationCount(), 
						   memberOfferCounter.getDenominationCount(), offerCounter.getDenominationCount()));
				   
				   
			   }	
				
			}
			
			return counter;
		}
		
		/***
		 * 
		 * @param counter
		 * @param denomination
		 * @param denominationCount
		 * @param denominationCount2
		 * @param denominationCount3
		 */
		private static DenominationLimitCounter setDenominationValueInCounterForEligibility(LimitCounterWithDenominationList counter,
				Integer denomination, List<DenominationCount> accountDenominationCounterList, List<DenominationCount> memberDenominationCounterList,
				List<DenominationCount> offerDenominationCounterList) {
			
			DenominationLimitCounter denominationCounter = new DenominationLimitCounter();
			denominationCounter.setDenomination(denomination);
			DenominationCount offerDenominationCount = getDenominationCount(offerDenominationCounterList, denomination);
			DenominationCount accountDenominationCount = getDenominationCount(accountDenominationCounterList, denomination);
			DenominationCount memberDenominationCount = getDenominationCount(memberDenominationCounterList, denomination);
			
			if(ObjectUtils.isEmpty(offerDenominationCount)) {
				
				offerDenominationCount = getNewDenominationCount(denomination, 0);
			}
			
			if(ObjectUtils.isEmpty(accountDenominationCount)) {
				
				accountDenominationCount = getNewDenominationCount(denomination, 0);
			}

			if(ObjectUtils.isEmpty(memberDenominationCount)) {
				
				memberDenominationCount = getNewDenominationCount(denomination, 0);
			}
			
			denominationCounter.setOfferDaily(offerDenominationCount.getDailyCount());
			denominationCounter.setOfferWeekly(offerDenominationCount.getWeeklyCount());
			denominationCounter.setOfferMonthly(offerDenominationCount.getMonthlyCount());
			denominationCounter.setOfferAnnual(offerDenominationCount.getAnnualCount());
			denominationCounter.setOfferTotal(offerDenominationCount.getTotalCount());
			denominationCounter.setAccountDaily(accountDenominationCount.getDailyCount());
			denominationCounter.setAccountWeekly(accountDenominationCount.getWeeklyCount());
			denominationCounter.setAccountMonthly(accountDenominationCount.getMonthlyCount());
			denominationCounter.setAccountAnnual(accountDenominationCount.getAnnualCount());
			denominationCounter.setAccountTotal(accountDenominationCount.getTotalCount());
			denominationCounter.setMemberDaily(memberDenominationCount.getDailyCount());
			denominationCounter.setMemberWeekly(memberDenominationCount.getWeeklyCount());
			denominationCounter.setMemberMonthly(memberDenominationCount.getMonthlyCount());
			denominationCounter.setMemberAnnual(memberDenominationCount.getAnnualCount());
			denominationCounter.setMemberTotal(memberDenominationCount.getTotalCount());
			
			return denominationCounter;
			
		}

		/**
		 * 
		 * @param denominationCounterList
		 * @param denomination
		 * @return
		 */
		private static DenominationCount getDenominationCount(List<DenominationCount> denominationCounterList,
				Integer denomination) {
			
				return !CollectionUtils.isEmpty(denominationCounterList)
					? denominationCounterList.stream().filter(d->d.getDenomination().equals(denomination))
							 .findAny().orElse(null)
					: null;
		}

		/**
		 * 
		 * @param totalCount
		 * @param updateResult
		 * @return
		 */
		public static Integer getRemainingCount(Integer totalCount, UpdateResult updateResult) {
			
			LOG.info("Update Result : {}", updateResult);
			Integer remainingCount = null;
			
			if(Checks.checkCountRemaining(totalCount, updateResult)) {
				
				remainingCount = totalCount-(int) updateResult.getModifiedCount();
			}
			
			return remainingCount;
		}
		
		/**
		 * 
		 * @param unit
		 * @param field
		 * @return
		 */
		public static boolean updateField(String unit, String field) {
			
			boolean update = false;
			
			switch (unit) {
			
			case OffersConfigurationConstants.ANNUAL :  update = Utilities.presentInList(OffersListConstants.ANNUAL_COUNTER_RESET, field);
														break;
			case OffersConfigurationConstants.MONTHLY : update = Utilities.presentInList(OffersListConstants.MONTHLY_COUNTER_RESET, field);
														break;
			case OffersConfigurationConstants.WEEKLY :  update = Utilities.presentInList(OffersListConstants.WEEKLY_COUNTER_RESET, field);
														break;
			case OffersConfigurationConstants.DAILY :   update = Utilities.presentInList(OffersListConstants.DAILY_COUNTER_RESET, field);
														break;
			default : update = false;
			}
			
			return update;
			
		}
		
		/**
		 * 
		 * @param unit
		 * @return
		 */
		public static Date getDateForCounterUpdateCheck(String unit) {
			
			Date date = null;
			
			switch (unit) {
			
				case OffersConfigurationConstants.ANNUAL :  date = Utilities.getFirstDayOfCurrentYear();
															break;
				case OffersConfigurationConstants.MONTHLY : date = Utilities.getFirstDayOfCurrentMonth();
															break;
				case OffersConfigurationConstants.WEEKLY :  date = Utilities.getFirstDayOfCurrentWeek();
															break;
				case OffersConfigurationConstants.DAILY :   date = Utilities.getCurrentDateWithoutTimeStamp();
															break;
				default : date = new Date();
			}
			
			return date;
		}
		
		/***
		 * 
		 * @param type
		 * @param customerTypeList
		 * @param paymentMethods
		 * @return
		 */
		public static List<PaymentMethod> getCommonPaymentMethods(String type, List<CustomerTypeEntity> customerTypeList,
				List<PaymentMethod> paymentMethods) {
			
			CustomerTypeEntity customerType = !CollectionUtils.isEmpty(customerTypeList)
					? customerTypeList.stream().filter(c->c.getCustomerType().equalsIgnoreCase(type)).findAny().orElse(null)
					: null;
			List<String> customerPaymentMethodList = !ObjectUtils.isEmpty(customerType)
					&& !ObjectUtils.isEmpty(customerType.getEligiblityMatrix())
					&& !CollectionUtils.isEmpty(customerType.getEligiblityMatrix().getPaymentMethod())
					? customerType.getEligiblityMatrix().getPaymentMethod().stream().map(PaymentMethods::getDescription).collect(Collectors.toList())
					: null;		
			return !ObjectUtils.isEmpty(customerPaymentMethodList)
				&& !CollectionUtils.isEmpty(paymentMethods)
				?  paymentMethods.stream().filter(p->customerPaymentMethodList.contains(p.getDescription())).collect(Collectors.toList())
				: null;
		}





		/**
		 * 
		 * @param paymentMethods
		 * @param accrualPaymentMethods
		 * @return merged payment methods
		 */
		public static List<String> getPaymentMethodList(List<String> paymentMethods,
				List<String> accrualPaymentMethods) {
			
			List<String> paymentMethodList =!CollectionUtils.isEmpty(paymentMethods)
					|| 	!CollectionUtils.isEmpty(accrualPaymentMethods)  
					? new ArrayList<>(1) 
					: null;
			
			if(!CollectionUtils.isEmpty(paymentMethods)) {
				
				paymentMethodList.addAll(paymentMethods);
				
			}
			
			if(!CollectionUtils.isEmpty(accrualPaymentMethods)) {
				
				paymentMethodList.addAll(accrualPaymentMethods);
			
			}
			
			return Utilities.convertListToDistinctValueList(paymentMethodList);
		}

		/**
		 * 
		 * @param eligibilityInfo
		 * @param savingsAmount 
		 * @param pointsTransactionId 
		 * @return LifetimeSavingsHelperDto
		 */
		public static LifetimeSavingsHelperDto createLifetimeSavingsHelperDto(boolean isDiscountCoupon, EligibilityInfo eligibilityInfo, Double savingsAmount, String pointsTransactionId) {
			
			LifetimeSavingsHelperDto lifetimeSavingsHelperDto = null;
			
			if(!ObjectUtils.isEmpty(eligibilityInfo)) {
				
				lifetimeSavingsHelperDto = new LifetimeSavingsHelperDto();
				lifetimeSavingsHelperDto.setSavings(!isDiscountCoupon
						|| lifetimeSavingsHelperDto.isSubscriptionStatus()
						? savingsAmount
						: savingsAmount-eligibilityInfo.getOffer().getOfferValues().getCost());
				lifetimeSavingsHelperDto.setMerchantName(eligibilityInfo.getOffer().getMerchant().getMerchantName().getMerchantNameEn());
				lifetimeSavingsHelperDto.setSubscriptionStatus(eligibilityInfo.getAdditionalDetails().isSubscribed());
				lifetimeSavingsHelperDto.setPointsTransactionId(pointsTransactionId);
				
				if(lifetimeSavingsHelperDto.isSubscriptionStatus()) {
					
					lifetimeSavingsHelperDto.setSubscriptionWaiveOff(eligibilityInfo.getOffer().getOfferValues().getCost());
				
				} 				
			}
			
			return lifetimeSavingsHelperDto;
		}
		
		public static void setOfferCountFlag(OfferCatalogDto offerCatalogRequest, OfferCatalog existingOffer, OfferCountDto offerCount) {
			if((!Utilities.isEqual(offerCatalogRequest.getMerchantCode(), existingOffer.getMerchant().getMerchantCode()))
					&& (existingOffer.getStatus().equalsIgnoreCase("Active") || existingOffer.getStatus().equalsIgnoreCase("ACTIVE"))) {
				offerCount.setExistingMerchantCode(existingOffer.getMerchant().getMerchantCode());
				offerCount.setUpdatedMerchantCode(offerCatalogRequest.getMerchantCode());
				offerCount.setMerchantCodeNotEqual(true);
			}
		}
    	
}

