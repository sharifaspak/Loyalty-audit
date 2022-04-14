package com.loyalty.marketplace.offers.utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import com.loyalty.marketplace.domain.model.CategoryDomain;
import com.loyalty.marketplace.domain.model.DenominationDomain;
import com.loyalty.marketplace.domain.model.PaymentMethodDomain;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OfferErrorCodes;
import com.loyalty.marketplace.offers.constants.OffersConfigurationConstants;
import com.loyalty.marketplace.offers.domain.model.AccountOfferCountDomain;
import com.loyalty.marketplace.offers.domain.model.AccountOfferCountersDomain;
import com.loyalty.marketplace.offers.domain.model.AccrualDetailsDomain;
import com.loyalty.marketplace.offers.domain.model.ActivityCodeDescriptionDomain;
import com.loyalty.marketplace.offers.domain.model.ActivityCodeDomain;
import com.loyalty.marketplace.offers.domain.model.AddTAndCDomain;
import com.loyalty.marketplace.offers.domain.model.AdditionalDetailsDomain;
import com.loyalty.marketplace.offers.domain.model.BirthdayDescriptionDomain;
import com.loyalty.marketplace.offers.domain.model.BirthdayGiftTrackerDomain;
import com.loyalty.marketplace.offers.domain.model.BirthdayIconTextDomain;
import com.loyalty.marketplace.offers.domain.model.BirthdayInfoDomain;
import com.loyalty.marketplace.offers.domain.model.BirthdaySubTitleDomain;
import com.loyalty.marketplace.offers.domain.model.BirthdayTitleDomain;
import com.loyalty.marketplace.offers.domain.model.BirthdayWeekIconDomain;
import com.loyalty.marketplace.offers.domain.model.BrandDescriptionDomain;
import com.loyalty.marketplace.offers.domain.model.DenominationCountDomain;
import com.loyalty.marketplace.offers.domain.model.DenominationLimitDomain;
import com.loyalty.marketplace.offers.domain.model.DynamicDenominationValueDomain;
import com.loyalty.marketplace.offers.domain.model.FreeOfferDomain;
import com.loyalty.marketplace.offers.domain.model.GiftInfoDomain;
import com.loyalty.marketplace.offers.domain.model.ListValuesDomain;
import com.loyalty.marketplace.offers.domain.model.MarketplaceActivityDomain;
import com.loyalty.marketplace.offers.domain.model.MemberCommentDomain;
import com.loyalty.marketplace.offers.domain.model.MemberOfferCountDomain;
import com.loyalty.marketplace.offers.domain.model.MemberOfferCountersDomain;
import com.loyalty.marketplace.offers.domain.model.MemberRatingDomain;
import com.loyalty.marketplace.offers.domain.model.OfferCatalogDomain;
import com.loyalty.marketplace.offers.domain.model.OfferCounterDomain;
import com.loyalty.marketplace.offers.domain.model.OfferCountersDomain;
import com.loyalty.marketplace.offers.domain.model.OfferDateDomain;
import com.loyalty.marketplace.offers.domain.model.OfferDetailMobileDomain;
import com.loyalty.marketplace.offers.domain.model.OfferDetailWebDomain;
import com.loyalty.marketplace.offers.domain.model.OfferDetailsDomain;
import com.loyalty.marketplace.offers.domain.model.OfferLabelDomain;
import com.loyalty.marketplace.offers.domain.model.OfferLimitDomain;
import com.loyalty.marketplace.offers.domain.model.OfferRatingDomain;
import com.loyalty.marketplace.offers.domain.model.OfferTitleDescriptionDomain;
import com.loyalty.marketplace.offers.domain.model.OfferTitleDomain;
import com.loyalty.marketplace.offers.domain.model.OfferTypeDomain;
import com.loyalty.marketplace.offers.domain.model.OfferValuesDomain;
import com.loyalty.marketplace.offers.domain.model.ProvisioningAttributesDomain;
import com.loyalty.marketplace.offers.domain.model.PurchaseDomain;
import com.loyalty.marketplace.offers.domain.model.RedeemInstructionsDomain;
import com.loyalty.marketplace.offers.domain.model.RedeemTitleInstructionsDomain;
import com.loyalty.marketplace.offers.domain.model.RestaurantDomain;
import com.loyalty.marketplace.offers.domain.model.SubOfferDescDomain;
import com.loyalty.marketplace.offers.domain.model.SubOfferDomain;
import com.loyalty.marketplace.offers.domain.model.SubOfferTitleDomain;
import com.loyalty.marketplace.offers.domain.model.SubOfferValueDomain;
import com.loyalty.marketplace.offers.domain.model.SubscriptionDetailsDomain;
import com.loyalty.marketplace.offers.domain.model.TAndCDomain;
import com.loyalty.marketplace.offers.domain.model.TagsDomain;
import com.loyalty.marketplace.offers.domain.model.TermsConditionsDomain;
import com.loyalty.marketplace.offers.domain.model.VoucherInfoDomain;
import com.loyalty.marketplace.offers.domain.model.WhatYouGetDomain;
import com.loyalty.marketplace.offers.domain.model.WishlistDomain;
import com.loyalty.marketplace.offers.helper.dto.EligibilityInfo;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.helper.dto.OfferReferences;
import com.loyalty.marketplace.offers.helper.dto.PurchaseExtraAttributes;
import com.loyalty.marketplace.offers.inbound.dto.BirthdayInfoRequestDto;
import com.loyalty.marketplace.offers.inbound.dto.DenominationLimitDto;
import com.loyalty.marketplace.offers.inbound.dto.LimitDto;
import com.loyalty.marketplace.offers.inbound.dto.ListValuesDto;
import com.loyalty.marketplace.offers.inbound.dto.OfferCatalogDto;
import com.loyalty.marketplace.offers.inbound.dto.OfferRatingDto;
import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
import com.loyalty.marketplace.offers.inbound.dto.SubOfferDto;
import com.loyalty.marketplace.offers.inbound.dto.WishlistRequestDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.merchants.domain.model.MerchantDomain;
import com.loyalty.marketplace.offers.outbound.database.entity.AccountOfferCount;
import com.loyalty.marketplace.offers.outbound.database.entity.AccountOfferCounts;
import com.loyalty.marketplace.offers.outbound.database.entity.ActivityCodeDescription;
import com.loyalty.marketplace.offers.outbound.database.entity.BirthdayGiftTracker;
import com.loyalty.marketplace.offers.outbound.database.entity.BirthdayInfo;
import com.loyalty.marketplace.offers.outbound.database.entity.DenominationCount;
import com.loyalty.marketplace.offers.outbound.database.entity.MarketplaceActivity;
import com.loyalty.marketplace.offers.outbound.database.entity.MemberComment;
import com.loyalty.marketplace.offers.outbound.database.entity.MemberOfferCount;
import com.loyalty.marketplace.offers.outbound.database.entity.MemberOfferCounts;
import com.loyalty.marketplace.offers.outbound.database.entity.MemberRating;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCounter;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCounters;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferRating;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchaseHistory;
import com.loyalty.marketplace.offers.outbound.database.entity.WishlistEntity;
import com.loyalty.marketplace.offers.outbound.dto.RestaurantDto;
import com.loyalty.marketplace.offers.stores.domain.model.StoreDomain;
import com.loyalty.marketplace.offers.stores.outbound.database.entity.Store;
import com.loyalty.marketplace.outbound.database.entity.Denomination;
import com.loyalty.marketplace.outbound.database.entity.PaymentMethod;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.payment.constants.MarketplaceConstants;
import com.loyalty.marketplace.payment.outbound.dto.PaymentResponse;
import com.loyalty.marketplace.subscription.constants.SubscriptionManagementConstants;
import com.loyalty.marketplace.subscription.phoneyTunes.inbound.dto.PhoneyTunesResponse;

/**
 * 
 * @author jaya.shukla
 *
 */
public class DomainConfiguration {
	
	DomainConfiguration(){}
	
	/**
	 * Creates domain object for creating/updating offer
	 * @param header
	 * @param existingOffer
	 * @param offerCatalog
	 * @param offerReference
	 * @return domain object for offer
	 * @throws ParseException
	 */
	public static OfferCatalogDomain getOfferDomain(Headers header, OfferCatalog existingOffer,
			OfferCatalogDto offerCatalog, OfferReferences offerReference) throws ParseException {

		boolean isInsert = offerCatalog.getAction().equalsIgnoreCase(OfferConstants.INSERT_ACTION.get());
		OfferCatalogDomain offerCatalogDomain = null;
		
		if(Checks.checkIsDiscountVoucher(offerCatalog.getOfferTypeId())) {
			
			offerCatalogDomain = getDiscountVoucherDomain(header, existingOffer, offerCatalog, isInsert, offerReference);
		
		} else if(Checks.checkIsCashVoucher(offerCatalog.getOfferTypeId())) { 
			
			offerCatalogDomain = getCashVoucherDomain(header, existingOffer, offerCatalog, isInsert, offerReference);
		
		} else if(Checks.checkIsDealVoucher(offerCatalog.getOfferTypeId())) { 
			
			offerCatalogDomain = getDealVoucherDomain(header, existingOffer, offerCatalog, isInsert, offerReference);
		
		} else if(Checks.checkIsEtisalatAddon(offerCatalog.getOfferTypeId())) { 
			
			offerCatalogDomain = getEtisalatAddOnDomain(header, existingOffer, offerCatalog, isInsert, offerReference);
		
		} else if(Checks.checkIsGoldCertificate(offerCatalog.getOfferTypeId())) { 
			
			offerCatalogDomain = getGoldCertificateDomain(header, existingOffer, offerCatalog, isInsert, offerReference);
		
		}
		
		return offerCatalogDomain;
	
	}
	
	/**
	 * Creates domain object for discount offer
	 * @param header
	 * @param existingOffer
	 * @param offerCatalog
	 * @param isInsert
	 * @param offerReference
	 * @return domain object for discount offer
	 * @throws ParseException
	 */
	private static OfferCatalogDomain getDiscountVoucherDomain(Headers header, OfferCatalog existingOffer, OfferCatalogDto offerCatalog, boolean isInsert, OfferReferences offerReference) throws ParseException {
		
		return new OfferCatalogDomain.OfferCatalogBuilder(offerCatalog.getOfferCode(),
				new OfferTypeDomain.OfferTypeBuilder(offerCatalog.getOfferTypeId()).build(),
				new CategoryDomain.CategoryBuilder(offerCatalog.getCategoryId()).build(),
				new CategoryDomain.CategoryBuilder(offerCatalog.getSubCategoryId()).build(), 
					offerReference.getMerchant().getPartner(),
				new MerchantDomain.MerchantBuilder(offerReference.getMerchant().getId()).build(),
				getStoreDomain(offerCatalog.getStoreCodes(), offerReference))
			.id(isInsert ? null : existingOffer.getId())
			.programCode(header.getProgram())
			.offerId(getOfferId(isInsert, existingOffer, offerCatalog.getOfferCode(), offerReference))
			.brandDescription(new BrandDescriptionDomain(offerCatalog.getBrandDescriptionEn(),
								offerCatalog.getBrandDescriptionAr()))
			.whatYouGet(new WhatYouGetDomain(offerCatalog.getWhatYouGetEn(), offerCatalog.getWhatYouGetAr()))
			.denominations(getDenominationDomainByDirhamValue(
								Utilities.convertFromStringToIntegerList(offerCatalog.getDenominations()), offerReference))
			.offer(getOfferInfoDomain(offerCatalog)).tAndC(getTermsAndConditionsDomain(offerCatalog))
			.tags(new TagsDomain(offerCatalog.getTagsEn(), offerCatalog.getTagsAr()))
			.offerDates(getOfferDateDomain(offerCatalog))
			.trendingRank(offerCatalog.getTrendingRank())
			.status(isInsert? OfferConstants.OFFER_DEFAULT_STATUS.get() : existingOffer.getStatus())
			.newOffer(Utilities.getFlag(offerCatalog.getNewOffer()))
			.giftInfo(getGiftDomain(offerCatalog.getIsGift(), offerCatalog.getGiftChannels(), offerCatalog.getGiftSubCardTypes()))
			.isDod(Utilities.getFlag(offerCatalog.getIsDod()))
			.isFeatured(Utilities.getFlag(offerCatalog.getIsFeatured()))
			.availableInPortals(offerCatalog.getAvailableInPortals())
			.offerValues(new OfferValuesDomain(offerCatalog.getPointsValue(), offerCatalog.getCost()))
			.discountPerc(offerCatalog.getDiscountPerc())
			.estSavings(offerCatalog.getEstSavings())
			.limit(getOfferLimitDomainList(offerCatalog.getLimit()))
			.sharing(Utilities.getFlag(offerCatalog.getSharing()))
			.sharingBonus(offerCatalog.getSharingBonus())
			.vatPercentage(offerCatalog.getVatPercentage())
			.groupedFlag(getGroupedFlagValue(offerCatalog.getGroupedFlag(), true))
			.rules(null != offerCatalog.getRules() && !offerCatalog.getRules().isEmpty()
								? offerCatalog.getRules()
								: null)
			.customerTypes(getListTypeDomain(offerCatalog.getCustomerTypes()))
			.customerSegments(getListTypeDomain(offerCatalog.getCustomerSegments()))
			.activityCode(getActivityCodeDomain(false, offerCatalog))
			.dynamicDenomination(OfferConstants.FLAG_NOT_SET.get())
			.isBirthdayGift(Utilities.getFlag(offerCatalog.getIsBirthdayGift()))
			.staticRating(null!=offerCatalog.getStaticRating()?offerCatalog.getStaticRating():OffersConfigurationConstants.ZERO_INTEGER)
			.offerRating(new OfferRatingDomain.OfferRatingBuilder(offerCatalog.getOfferRating()).build())
			.voucherInfo(getVoucherDomain(offerCatalog))
			.freeOffers(getFreeOfferDomain(offerCatalog))
			.subscriptionDetails(new SubscriptionDetailsDomain(offerCatalog.getSubscPromo(), offerCatalog.getSubscriptionCatalogId()))
			.paymentMethods(getPaymentDomain(offerCatalog.getPaymentMethods(), offerReference))
			.restaurants(getRestaurantDomain(offerCatalog.getRestaurant()))
			.createdDate(isInsert ? new Date() : existingOffer.getCreatedDate())
			.createdUser(isInsert ? header.getUserName() : existingOffer.getCreatedUser())
			.updatedDate(new Date())
			.updatedUser(header.getUserName())
			.build();
		
	}
	
	/**
	 * Creates domain object for cash voucher 
	 * @param header
	 * @param existingOffer
	 * @param offerCatalog
	 * @param isInsert
	 * @param offerReference
	 * @return domain object for cash voucher 
	 * @throws ParseException
	 */
	private static OfferCatalogDomain getCashVoucherDomain(Headers header, OfferCatalog existingOffer, OfferCatalogDto offerCatalog, boolean isInsert, OfferReferences offerReference) throws ParseException {
		
		return new OfferCatalogDomain.OfferCatalogBuilder(offerCatalog.getOfferCode(),
				new OfferTypeDomain.OfferTypeBuilder(offerCatalog.getOfferTypeId()).build(),
				new CategoryDomain.CategoryBuilder(offerCatalog.getCategoryId()).build(),
				new CategoryDomain.CategoryBuilder(offerCatalog.getSubCategoryId()).build(), 
					offerReference.getMerchant().getPartner(),
				new MerchantDomain.MerchantBuilder(offerReference.getMerchant().getId()).build(),
				getStoreDomain(offerCatalog.getStoreCodes(), offerReference))
			.id(isInsert ? null : existingOffer.getId())
			.programCode(header.getProgram())
			.offerId(getOfferId(isInsert, existingOffer, offerCatalog.getOfferCode(), offerReference))
			.brandDescription(new BrandDescriptionDomain(offerCatalog.getBrandDescriptionEn(),
								offerCatalog.getBrandDescriptionAr()))
			.whatYouGet(new WhatYouGetDomain(offerCatalog.getWhatYouGetEn(), offerCatalog.getWhatYouGetAr()))
			.denominations(getDenominationDomainByDirhamValue(
								Utilities.convertFromStringToIntegerList(offerCatalog.getDenominations()), offerReference))
			.offer(getOfferInfoDomain(offerCatalog)).tAndC(getTermsAndConditionsDomain(offerCatalog))
			.tags(new TagsDomain(offerCatalog.getTagsEn(), offerCatalog.getTagsAr()))
			.offerDates(getOfferDateDomain(offerCatalog))
			.trendingRank(offerCatalog.getTrendingRank())
			.status(isInsert? OfferConstants.OFFER_DEFAULT_STATUS.get() : existingOffer.getStatus())
			.newOffer(Utilities.getFlag(offerCatalog.getNewOffer()))
			.giftInfo(getGiftDomain(offerCatalog.getIsGift(), offerCatalog.getGiftChannels(), offerCatalog.getGiftSubCardTypes()))
			.isDod(Utilities.getFlag(offerCatalog.getIsDod()))
			.isFeatured(Utilities.getFlag(offerCatalog.getIsFeatured()))
			.availableInPortals(offerCatalog.getAvailableInPortals())
			.offerValues(new OfferValuesDomain(offerCatalog.getPointsValue(), offerCatalog.getCost()))
			.discountPerc(offerCatalog.getDiscountPerc())
			.estSavings(offerCatalog.getEstSavings())
			.limit(getOfferLimitDomainList(offerCatalog.getLimit()))
			.sharing(Utilities.getFlag(offerCatalog.getSharing()))
			.sharingBonus(offerCatalog.getSharingBonus())
			.vatPercentage(offerCatalog.getVatPercentage())
			.groupedFlag(getGroupedFlagValue(offerCatalog.getGroupedFlag(), false))
			.rules(null != offerCatalog.getRules() && !offerCatalog.getRules().isEmpty()
								? offerCatalog.getRules()
								: null)
			.customerTypes(getListTypeDomain(offerCatalog.getCustomerTypes()))
			.customerSegments(getListTypeDomain(offerCatalog.getCustomerSegments()))
			.activityCode(getActivityCodeDomain(true, offerCatalog))
			.dynamicDenomination(Utilities.getFlag(offerCatalog.getDynamicDenomination()))
			.dynamicDenominationValue(new DynamicDenominationValueDomain(offerCatalog.getMinDenomination(),
									offerCatalog.getMaxDenomination()))
			.incrementalValue(offerCatalog.getIncrementalValue())
			.isBirthdayGift(Utilities.getFlag(offerCatalog.getIsBirthdayGift()))
			.staticRating(null!=offerCatalog.getStaticRating()?offerCatalog.getStaticRating():OffersConfigurationConstants.ZERO_INTEGER)
			.earnMultiplier(offerCatalog.getEarnMultiplier())
			.accrualDetails(getAccrualDetailsDomain(offerCatalog, offerReference))
			.offerRating(new OfferRatingDomain.OfferRatingBuilder(offerCatalog.getOfferRating()).build())
			.voucherInfo(getVoucherDomain(offerCatalog))
			.freeOffers(getFreeOfferDomain(offerCatalog))
			.paymentMethods(getPaymentDomain(offerCatalog.getPaymentMethods(), offerReference))
			.restaurants(getRestaurantDomain(offerCatalog.getRestaurant()))
			.createdDate(isInsert ? new Date() : existingOffer.getCreatedDate())
			.createdUser(isInsert ? header.getUserName() : existingOffer.getCreatedUser())
			.updatedDate(new Date())
			.updatedUser(header.getUserName())
			.build();
		
	}
	
	/**
	 * 
	 * @param restaurant
	 * @return restaurant domain
	 */
	private static RestaurantDomain getRestaurantDomain(RestaurantDto restaurant) {
		
		return !ObjectUtils.isEmpty(restaurant)
			? new RestaurantDomain(restaurant.getRestaurantNameEng(), restaurant.getRestaurantNameAr(), restaurant.getType())
			: null;
	}

	/* 
	 * @param offerCatalog
	 * @param offerReference 
	 * @return accrualDetailsDomain object
	 */
	private static AccrualDetailsDomain getAccrualDetailsDomain(OfferCatalogDto offerCatalog, OfferReferences offerReference) {
		
		AccrualDetailsDomain accrualDetailsDomain = !ObjectUtils.isEmpty(offerCatalog.getPointsEarnMultiplier()) 
				 || !CollectionUtils.isEmpty(offerCatalog.getAccrualPaymentMethods()) 
				 ? new AccrualDetailsDomain()
				 : null;
		if(!ObjectUtils.isEmpty(accrualDetailsDomain)) {
			
			accrualDetailsDomain.setAccrualPaymentMethods(getPaymentDomain(offerCatalog.getAccrualPaymentMethods(), offerReference));
			accrualDetailsDomain.setPointsEarnMultiplier(offerCatalog.getPointsEarnMultiplier());
		}
		
		return accrualDetailsDomain;
	}
	
	/**
	 * Creates domain object for deal voucher
	 * @param header
	 * @param existingOffer
	 * @param offerCatalog
	 * @param isInsert
	 * @param offerReference
	 * @return
	 * @throws ParseException
	 */
	private static OfferCatalogDomain getDealVoucherDomain(Headers header, OfferCatalog existingOffer, OfferCatalogDto offerCatalog, boolean isInsert, OfferReferences offerReference) throws ParseException {
		
		return new OfferCatalogDomain.OfferCatalogBuilder(offerCatalog.getOfferCode(),
				new OfferTypeDomain.OfferTypeBuilder(offerCatalog.getOfferTypeId()).build(),
				new CategoryDomain.CategoryBuilder(offerCatalog.getCategoryId()).build(),
				new CategoryDomain.CategoryBuilder(offerCatalog.getSubCategoryId()).build(), 
					offerReference.getMerchant().getPartner(),
				new MerchantDomain.MerchantBuilder(offerReference.getMerchant().getId()).build(),
				getStoreDomain(offerCatalog.getStoreCodes(), offerReference))
			.id(isInsert ? null : existingOffer.getId())
			.programCode(header.getProgram())
			.offerId(getOfferId(isInsert, existingOffer, offerCatalog.getOfferCode(), offerReference))
			.brandDescription(new BrandDescriptionDomain(offerCatalog.getBrandDescriptionEn(),
								offerCatalog.getBrandDescriptionAr()))
			.whatYouGet(new WhatYouGetDomain(offerCatalog.getWhatYouGetEn(), offerCatalog.getWhatYouGetAr()))
			.denominations(getDenominationDomainByDirhamValue(
								Utilities.convertFromStringToIntegerList(offerCatalog.getDenominations()), offerReference))
			.offer(getOfferInfoDomain(offerCatalog)).tAndC(getTermsAndConditionsDomain(offerCatalog))
			.tags(new TagsDomain(offerCatalog.getTagsEn(), offerCatalog.getTagsAr()))
			.offerDates(getOfferDateDomain(offerCatalog))
			.trendingRank(offerCatalog.getTrendingRank())
			.status(isInsert? OfferConstants.OFFER_DEFAULT_STATUS.get() : existingOffer.getStatus())
			.newOffer(Utilities.getFlag(offerCatalog.getNewOffer()))
			.giftInfo(getGiftDomain(offerCatalog.getIsGift(), offerCatalog.getGiftChannels(), offerCatalog.getGiftSubCardTypes()))
			.isDod(Utilities.getFlag(offerCatalog.getIsDod()))
			.isFeatured(Utilities.getFlag(offerCatalog.getIsFeatured()))
			.availableInPortals(offerCatalog.getAvailableInPortals())
			.offerValues(new OfferValuesDomain(offerCatalog.getPointsValue(), offerCatalog.getCost()))
			.discountPerc(offerCatalog.getDiscountPerc())
			.estSavings(offerCatalog.getEstSavings())
			.limit(getOfferLimitDomainList(offerCatalog.getLimit()))
			.sharing(Utilities.getFlag(offerCatalog.getSharing()))
			.sharingBonus(offerCatalog.getSharingBonus())
			.vatPercentage(offerCatalog.getVatPercentage())
			.groupedFlag(getGroupedFlagValue(offerCatalog.getGroupedFlag(), false))
			.rules(null != offerCatalog.getRules() && !offerCatalog.getRules().isEmpty()
								? offerCatalog.getRules()
								: null)
			.customerTypes(getListTypeDomain(offerCatalog.getCustomerTypes()))
			.customerSegments(getListTypeDomain(offerCatalog.getCustomerSegments()))
			.activityCode(getActivityCodeDomain(false, offerCatalog))
			.dynamicDenomination(OfferConstants.FLAG_NOT_SET.get())
			.isBirthdayGift(Utilities.getFlag(offerCatalog.getIsBirthdayGift()))
			.staticRating(null!=offerCatalog.getStaticRating()?offerCatalog.getStaticRating():OffersConfigurationConstants.ZERO_INTEGER)
			.subOffer(getSubOfferDomain(offerCatalog))
			.offerRating(new OfferRatingDomain.OfferRatingBuilder(offerCatalog.getOfferRating()).build())
			.voucherInfo(getVoucherDomain(offerCatalog))
			.freeOffers(getFreeOfferDomain(offerCatalog))
			.paymentMethods(getPaymentDomain(offerCatalog.getPaymentMethods(), offerReference))
			.restaurants(getRestaurantDomain(offerCatalog.getRestaurant()))
			.createdDate(isInsert ? new Date() : existingOffer.getCreatedDate())
			.createdUser(isInsert ? header.getUserName() : existingOffer.getCreatedUser())
			.updatedDate(new Date())
			.updatedUser(header.getUserName())
			.build();
		
	}
	
	/**
	 * Creates domain object for etisalat add on offer
	 * @param header
	 * @param existingOffer
	 * @param offerCatalog
	 * @param isInsert
	 * @param offerReference
	 * @return domain object for etisalat add on offer
	 * @throws ParseException
	 */
	private static OfferCatalogDomain getEtisalatAddOnDomain(Headers header, OfferCatalog existingOffer, OfferCatalogDto offerCatalog, boolean isInsert, OfferReferences offerReference) throws ParseException {
		
		return new OfferCatalogDomain.OfferCatalogBuilder(offerCatalog.getOfferCode(),
				new OfferTypeDomain.OfferTypeBuilder(offerCatalog.getOfferTypeId()).build(),
				new CategoryDomain.CategoryBuilder(offerCatalog.getCategoryId()).build(),
				new CategoryDomain.CategoryBuilder(offerCatalog.getSubCategoryId()).build(), 
					offerReference.getMerchant().getPartner(),
				new MerchantDomain.MerchantBuilder(offerReference.getMerchant().getId()).build(),
				getStoreDomain(offerCatalog.getStoreCodes(), offerReference))
			.id(isInsert ? null : existingOffer.getId())
			.programCode(header.getProgram())
			.offerId(getOfferId(isInsert, existingOffer, offerCatalog.getOfferCode(), offerReference))
			.brandDescription(new BrandDescriptionDomain(offerCatalog.getBrandDescriptionEn(),
								offerCatalog.getBrandDescriptionAr()))
			.whatYouGet(new WhatYouGetDomain(offerCatalog.getWhatYouGetEn(), offerCatalog.getWhatYouGetAr()))
			.denominations(getDenominationDomainByDirhamValue(
								Utilities.convertFromStringToIntegerList(offerCatalog.getDenominations()), offerReference))
			.offer(getOfferInfoDomain(offerCatalog)).tAndC(getTermsAndConditionsDomain(offerCatalog))
			.tags(new TagsDomain(offerCatalog.getTagsEn(), offerCatalog.getTagsAr()))
			.offerDates(getOfferDateDomain(offerCatalog))
			.trendingRank(offerCatalog.getTrendingRank())
			.status(isInsert? OfferConstants.OFFER_DEFAULT_STATUS.get() : existingOffer.getStatus())
			.newOffer(Utilities.getFlag(offerCatalog.getNewOffer()))
			.giftInfo(getGiftDomain(offerCatalog.getIsGift(), offerCatalog.getGiftChannels(), offerCatalog.getGiftSubCardTypes()))
			.isDod(Utilities.getFlag(offerCatalog.getIsDod()))
			.isFeatured(Utilities.getFlag(offerCatalog.getIsFeatured()))
			.availableInPortals(offerCatalog.getAvailableInPortals())
			.offerValues(new OfferValuesDomain(offerCatalog.getPointsValue(), offerCatalog.getCost()))
			.discountPerc(offerCatalog.getDiscountPerc())
			.estSavings(offerCatalog.getEstSavings())
			.limit(getOfferLimitDomainList(offerCatalog.getLimit()))
			.sharing(Utilities.getFlag(offerCatalog.getSharing()))
			.sharingBonus(offerCatalog.getSharingBonus())
			.vatPercentage(offerCatalog.getVatPercentage())
			.groupedFlag(getGroupedFlagValue(offerCatalog.getGroupedFlag(), true))
			.rules(null != offerCatalog.getRules() && !offerCatalog.getRules().isEmpty()
								? offerCatalog.getRules()
								: null)
			.customerTypes(getListTypeDomain(offerCatalog.getCustomerTypes()))
			.customerSegments(getListTypeDomain(offerCatalog.getCustomerSegments()))
			.activityCode(getActivityCodeDomain(false, offerCatalog))
			.dynamicDenomination(OfferConstants.FLAG_NOT_SET.get())
			.isBirthdayGift(Utilities.getFlag(offerCatalog.getIsBirthdayGift()))
			.staticRating(null!=offerCatalog.getStaticRating()?offerCatalog.getStaticRating():OffersConfigurationConstants.ZERO_INTEGER)
			.offerRating(new OfferRatingDomain.OfferRatingBuilder(offerCatalog.getOfferRating()).build())
			.provisioningChannel(offerCatalog.getProvisioningChannel())
			.proratedBundle(offerCatalog.isProratedBundle())
			.provisioningAttributes(getProvisioningAttributesDomain(offerCatalog))
			.freeOffers(getFreeOfferDomain(offerCatalog))
			.paymentMethods(getPaymentDomain(offerCatalog.getPaymentMethods(), offerReference))
			.restaurants(getRestaurantDomain(offerCatalog.getRestaurant()))
			.createdDate(isInsert ? new Date() : existingOffer.getCreatedDate())
			.createdUser(isInsert ? header.getUserName() : existingOffer.getCreatedUser())
			.updatedDate(new Date())
			.updatedUser(header.getUserName())
			.build();
		
	}
	
	/**
	 * Creates domain object for gold certificate
	 * @param header
	 * @param existingOffer
	 * @param offerCatalog
	 * @param isInsert
	 * @param offerReference
	 * @return domain object for gold certificate
	 * @throws ParseException
	 */
	private static OfferCatalogDomain getGoldCertificateDomain(Headers header, OfferCatalog existingOffer, OfferCatalogDto offerCatalog, boolean isInsert, OfferReferences offerReference) throws ParseException {
		
		return new OfferCatalogDomain.OfferCatalogBuilder(offerCatalog.getOfferCode(),
				new OfferTypeDomain.OfferTypeBuilder(offerCatalog.getOfferTypeId()).build(),
				new CategoryDomain.CategoryBuilder(offerCatalog.getCategoryId()).build(),
				new CategoryDomain.CategoryBuilder(offerCatalog.getSubCategoryId()).build(), 
					offerReference.getMerchant().getPartner(),
				new MerchantDomain.MerchantBuilder(offerReference.getMerchant().getId()).build(),
				getStoreDomain(offerCatalog.getStoreCodes(), offerReference))
			.id(isInsert ? null : existingOffer.getId())
			.programCode(header.getProgram())
			.offerId(getOfferId(isInsert, existingOffer, offerCatalog.getOfferCode(), offerReference))
			.brandDescription(new BrandDescriptionDomain(offerCatalog.getBrandDescriptionEn(),
								offerCatalog.getBrandDescriptionAr()))
			.whatYouGet(new WhatYouGetDomain(offerCatalog.getWhatYouGetEn(), offerCatalog.getWhatYouGetAr()))
			.denominations(getDenominationDomainByDirhamValue(
								Utilities.convertFromStringToIntegerList(offerCatalog.getDenominations()), offerReference))
			.offer(getOfferInfoDomain(offerCatalog)).tAndC(getTermsAndConditionsDomain(offerCatalog))
			.tags(new TagsDomain(offerCatalog.getTagsEn(), offerCatalog.getTagsAr()))
			.offerDates(getOfferDateDomain(offerCatalog))
			.trendingRank(offerCatalog.getTrendingRank())
			.status(isInsert? OfferConstants.OFFER_DEFAULT_STATUS.get() : existingOffer.getStatus())
			.newOffer(Utilities.getFlag(offerCatalog.getNewOffer()))
			.giftInfo(getGiftDomain(offerCatalog.getIsGift(), offerCatalog.getGiftChannels(), offerCatalog.getGiftSubCardTypes()))
			.isDod(Utilities.getFlag(offerCatalog.getIsDod()))
			.isFeatured(Utilities.getFlag(offerCatalog.getIsFeatured()))
			.availableInPortals(offerCatalog.getAvailableInPortals())
			.offerValues(new OfferValuesDomain(offerCatalog.getPointsValue(), offerCatalog.getCost()))
			.discountPerc(offerCatalog.getDiscountPerc())
			.estSavings(offerCatalog.getEstSavings())
			.limit(getOfferLimitDomainList(offerCatalog.getLimit()))
			.sharing(Utilities.getFlag(offerCatalog.getSharing()))
			.sharingBonus(offerCatalog.getSharingBonus())
			.vatPercentage(offerCatalog.getVatPercentage())
			.groupedFlag(getGroupedFlagValue(offerCatalog.getGroupedFlag(), false))
			.rules(null != offerCatalog.getRules() && !offerCatalog.getRules().isEmpty()
								? offerCatalog.getRules()
								: null)
			.customerTypes(getListTypeDomain(offerCatalog.getCustomerTypes()))
			.customerSegments(getListTypeDomain(offerCatalog.getCustomerSegments()))
			.activityCode(getActivityCodeDomain(false, offerCatalog))
			.dynamicDenomination(OfferConstants.FLAG_NOT_SET.get())
			.isBirthdayGift(Utilities.getFlag(offerCatalog.getIsBirthdayGift()))
			.staticRating(null!=offerCatalog.getStaticRating()?offerCatalog.getStaticRating():OffersConfigurationConstants.ZERO_INTEGER)
			.offerRating(new OfferRatingDomain.OfferRatingBuilder(offerCatalog.getOfferRating()).build())
			.freeOffers(getFreeOfferDomain(offerCatalog))
			.paymentMethods(getPaymentDomain(offerCatalog.getPaymentMethods(), offerReference))
			.restaurants(getRestaurantDomain(offerCatalog.getRestaurant()))
			.createdDate(isInsert ? new Date() : existingOffer.getCreatedDate())
			.createdUser(isInsert ? header.getUserName() : existingOffer.getCreatedUser())
			.updatedDate(new Date())
			.updatedUser(header.getUserName())
			.build();
		
	}
	
	/**
	 * Checks whether flag is present in input and sets value for grouped flag
	 * @param groupedFlag
	 * @param isDiscountVoucher
	 * @return value of grouped flag
	 */
	private static String getGroupedFlagValue(String groupedFlag, boolean isDiscountVoucher) {
		
		return isDiscountVoucher 
			&& !StringUtils.isEmpty(groupedFlag) 
			? groupedFlag 
			: OfferConstants.FLAG_NOT_SET.get();
	}

	/**
	 * Creates domain object for OfferDate object of offer catalog
	 * @param offerCatalog
	 * @return domain object for OfferDate
	 * @throws ParseException
	 */
    private static OfferDateDomain getOfferDateDomain(OfferCatalogDto offerCatalog) throws ParseException {
		
		String format = OfferConstants.DATE_FORMAT.get();
		Date offerStartDate = Utilities.changeStringToDate(offerCatalog.getOfferStartDate(), format);
		Date offerEndDate = null!=offerCatalog.getOfferEndDate() && !StringUtils.isEmpty(offerCatalog.getOfferEndDate()) 
				? Utilities.changeStringToDate(offerCatalog.getOfferEndDate(), format)
				: null;
		return new OfferDateDomain(offerStartDate, offerEndDate);
		
	}
    
    /***
     * Creates domain object for VoucherInfo object of offer catalog
     * @param offerCatalog
     * @return domain object for voucher info
     * @throws ParseException 
     */
private static VoucherInfoDomain getVoucherDomain(OfferCatalogDto offerCatalog) throws ParseException {
		
    	VoucherInfoDomain voucherInfo = null;
    	
    	if(!StringUtils.isEmpty(offerCatalog.getVoucherAction())
    	|| !ObjectUtils.isEmpty(offerCatalog.getVoucherAmount())
    	|| !ObjectUtils.isEmpty(offerCatalog.getVoucherExpiryPeriod())
    	|| !StringUtils.isEmpty(offerCatalog.getVoucherExpiryDate())
    	|| !ObjectUtils.isEmpty(offerCatalog.getVoucherRedeemType())
    	|| !ObjectUtils.isEmpty(offerCatalog.getPartnerRedeemURL())
    	|| !ObjectUtils.isEmpty(offerCatalog.getInstructionsToRedeemTitleEn())
    	|| !ObjectUtils.isEmpty(offerCatalog.getInstructionsToRedeemTitleAr())
    	|| !ObjectUtils.isEmpty(offerCatalog.getInstructionsToRedeemEn())
    	|| !ObjectUtils.isEmpty(offerCatalog.getInstructionsToRedeemAr())) {
    		
    		String format = OfferConstants.DATE_FORMAT.get();
    		Date voucherExpiryDate = !StringUtils.isEmpty(offerCatalog.getVoucherExpiryDate())
    				? Utilities.changeStringToDate(offerCatalog.getVoucherExpiryDate(), format)
    				: null;
    		
    		RedeemTitleInstructionsDomain redeemTitleInstructionsDomain = 
    				   !ObjectUtils.isEmpty(offerCatalog.getInstructionsToRedeemTitleEn())
    		    	|| !ObjectUtils.isEmpty(offerCatalog.getInstructionsToRedeemTitleAr())
    				? new RedeemTitleInstructionsDomain(offerCatalog.getInstructionsToRedeemTitleEn(), 
    						offerCatalog.getInstructionsToRedeemTitleAr())
    				: null;		
			RedeemInstructionsDomain redeemInstructionsDomain = 
    				   !ObjectUtils.isEmpty(offerCatalog.getInstructionsToRedeemEn())
    		    	|| !ObjectUtils.isEmpty(offerCatalog.getInstructionsToRedeemAr())
    				? new RedeemInstructionsDomain(offerCatalog.getInstructionsToRedeemEn(), 
    											   offerCatalog.getInstructionsToRedeemAr())
    				: null;	
    	    				
    		voucherInfo = new VoucherInfoDomain(offerCatalog.getVoucherExpiryPeriod(),
    				offerCatalog.getVoucherAmount(), 
    				offerCatalog.getVoucherAction(),
    				voucherExpiryDate, 
    				offerCatalog.getVoucherRedeemType(),
    				offerCatalog.getPartnerRedeemURL(),
    				redeemTitleInstructionsDomain,
    				redeemInstructionsDomain);
    		
       }
    	
	   return voucherInfo;
	
    }

	/**
	 * 
	 * @param offerCatalog
	 * @return domain object for free offer
	 */
	private static FreeOfferDomain getFreeOfferDomain(OfferCatalogDto offerCatalog) {
		
		return Checks.checkIsCashVoucher(offerCatalog.getOfferTypeId())
			? new FreeOfferDomain(offerCatalog.isMamba(), offerCatalog.isPromotionalGift(), offerCatalog.isPointsRedemptionGift())
			: new FreeOfferDomain(false, offerCatalog.isPromotionalGift(), false);
	}
	

    /**
     * Creates domain object for TermsConditions object of offer catalog
     * @param offerCatalog
     * @return domain object for TermsConditions
     */
	private static TermsConditionsDomain getTermsAndConditionsDomain(OfferCatalogDto offerCatalog) {
		
		TAndCDomain tAndC = new TAndCDomain(offerCatalog.getTermsAndConditionsEn(),
				offerCatalog.getTermsAndConditionsAr());
		AddTAndCDomain addTAndC = new AddTAndCDomain(offerCatalog.getAdditionalTermsAndConditionsEn(),
				offerCatalog.getAdditionalTermsAndConditionsAr());
		return new TermsConditionsDomain(tAndC, addTAndC);
		
	}

	/**
	 * Creates domain object for OfferDetails object of offer catalog
	 * @param offerCatalog
	 * @return domain object for OfferDetails
	 */
	private static OfferDetailsDomain getOfferInfoDomain(OfferCatalogDto offerCatalog) {
		
		OfferLabelDomain offerLabel = new OfferLabelDomain(offerCatalog.getOfferLabelEn(),
				offerCatalog.getOfferLabelAr());
		OfferTitleDomain offerTitle = new OfferTitleDomain(offerCatalog.getOfferTitleEn(),
				offerCatalog.getOfferTitleAr());
		OfferTitleDescriptionDomain offerTitleDescription = new OfferTitleDescriptionDomain(
				offerCatalog.getOfferTitleDescriptionEn(), offerCatalog.getOfferTitleDescriptionAr());
		OfferDetailMobileDomain offerMobile = new OfferDetailMobileDomain(offerCatalog.getOfferDetailMobileEn(),
				offerCatalog.getOfferDetailMobileAr());
		OfferDetailWebDomain offerWeb = new OfferDetailWebDomain(offerCatalog.getOfferDetailWebEn(),
				offerCatalog.getOfferDetailWebAr());
		return new OfferDetailsDomain(offerLabel, offerTitle, offerTitleDescription, offerMobile, offerWeb);
		
    }

	/**
	 * 
	 * @param isInsert
	 * @param existingOffer
	 * @param offerCode
	 * @param offerReferences
	 * @return offerId for current domain object 
	 */
	private static String getOfferId(boolean isInsert, OfferCatalog existingOffer, String offerCode, OfferReferences offerReferences) {
		
		String sequence = isInsert ? String.valueOf(offerReferences.getSize() + 1) : null;
		return isInsert ? OfferConstants.OFFER_CODE_PREF.get() + offerCode 
		   + OfferConstants.UNDERSCORE_SEPARATOR.get() + sequence 
		 : existingOffer.getOfferId();
	}
	
	/**
	 * 
	 * @param listValues
	 * @return ListValuesDomain object for OfferCatalogDomain
	 */
	private static ListValuesDomain getListTypeDomain(ListValuesDto listValues) {
		
		ListValuesDomain listValuesDomain = null;
		
		if(!ObjectUtils.isEmpty(listValues) 
		&& (!CollectionUtils.isEmpty(listValues.getEligibleTypes()) 
		 || !CollectionUtils.isEmpty(listValues.getExclusionTypes()))) {
			
			List<String> exclusionTypes = CollectionUtils.isNotEmpty(listValues.getExclusionTypes())
					? listValues.getExclusionTypes()
					: null;
			List<String> eligibleTypes = CollectionUtils.isNotEmpty(listValues.getEligibleTypes())
					? listValues.getEligibleTypes()
					: null;
			
			listValuesDomain = new ListValuesDomain(eligibleTypes, exclusionTypes);
		}
		
				
		return listValuesDomain;
		
	}

	/**
	 * 
	 * @param isCashVoucher
	 * @param offerCatalog
	 * @return  domain object for activity code
	 */
	private static ActivityCodeDomain getActivityCodeDomain(boolean isCashVoucher, OfferCatalogDto offerCatalog) {

		MarketplaceActivityDomain redemptionActivityCode = getMarketplaceActivityDomain(offerCatalog.getRedemptionId(),
				offerCatalog.getRedemptionActivityCode(), offerCatalog.getRedemptionCodeDescriptionEn(), offerCatalog.getRedemptionCodeDescriptionAr());

		MarketplaceActivityDomain accrualActivityCode = isCashVoucher && null != offerCatalog.getEarnMultiplier()
				&& null != offerCatalog.getAccrualActivityCode() && !offerCatalog.getAccrualActivityCode().isEmpty()
						? getMarketplaceActivityDomain(offerCatalog.getAccrualId(), offerCatalog.getAccrualActivityCode(), offerCatalog.getAccrualCodeDescriptionEn(), offerCatalog.getAccrualCodeDescriptionAr())
						: null;
		MarketplaceActivityDomain pointsAccrualActivityCode = isCashVoucher && null != offerCatalog.getPointsEarnMultiplier()
				&& null != offerCatalog.getPointsAccrualActivityCode() && !offerCatalog.getPointsAccrualActivityCode().isEmpty()
						? getMarketplaceActivityDomain(offerCatalog.getPointsAccrualId(), offerCatalog.getPointsAccrualActivityCode(), offerCatalog.getPointsAccrualCodeDescriptionEn(), offerCatalog.getPointsAccrualCodeDescriptionAr())
						: null;

		return new ActivityCodeDomain(accrualActivityCode, redemptionActivityCode, pointsAccrualActivityCode);

	}
	
	/**
	 * 
	 * @param activityId
	 * @param activityCode
	 * @param descriptionEn
	 * @param descriptionAr
	 * @return domain object for marketplace activity
	 */
	private static MarketplaceActivityDomain getMarketplaceActivityDomain(String activityId, String activityCode, String descriptionEn, String descriptionAr) {
		
		ActivityCodeDescriptionDomain activityCodeDescription = new ActivityCodeDescriptionDomain(descriptionEn, descriptionAr); 
		return new MarketplaceActivityDomain(activityId, activityCode, activityCodeDescription);
	}

	/**
	 * 
	 * @param offerCatalog
	 * @return suboffer domain list for deal offers
	 */
	private static List<SubOfferDomain> getSubOfferDomain(OfferCatalogDto offerCatalog) {
		
		List<SubOfferDomain> subOfferList = null; 
				
		if(!CollectionUtils.isEmpty(offerCatalog.getSubOffer())) {
			
			Random rand = new Random(); 
			subOfferList = new ArrayList<>(offerCatalog.getSubOffer().size());
			
			String subOfferPrefix = OfferConstants.SUBOFFER_CODE_PREF.get()+offerCatalog.getOfferCode();
			Integer count = 0;
			
			String subOfferId = null;
			SubOfferTitleDomain subOfferTitle = null;
			SubOfferDescDomain subOfferDesc = null;
			SubOfferValueDomain subOfferValue = null;
			SubOfferDomain subOfferDomain = null;
			
			for (SubOfferDto subOffer : offerCatalog.getSubOffer()) {
				
				count = count+1;
				subOfferId = StringUtils.isEmpty(subOffer.getSubOfferId())
						? subOfferPrefix+OfferConstants.UNDERSCORE_SEPARATOR.get()+count
						: subOffer.getSubOfferId();
				
				subOffer.setSubOfferId(subOfferId);
				while(!Checks.checkUniqueSubOfferId(subOfferId, offerCatalog.getSubOffer())) {
					
					subOfferId = subOfferPrefix+OfferConstants.UNDERSCORE_SEPARATOR.get()+rand.nextInt(10000);
					subOffer.setSubOfferId(subOfferId);
				}
				
				subOfferTitle = new SubOfferTitleDomain(subOffer.getSubOfferTitleEn(),
						subOffer.getSubOfferTitleAr());
				subOfferDesc = new SubOfferDescDomain(subOffer.getSubOfferDescEn(),
						subOffer.getSubOfferDescAr());
				subOfferValue = new SubOfferValueDomain(subOffer.getOldCost(),
						subOffer.getOldPointValue(), subOffer.getNewCost(), subOffer.getNewPointValue());
				subOfferDomain = new SubOfferDomain(subOfferId, subOfferTitle, subOfferDesc, subOfferValue);
				subOfferList.add(subOfferDomain);
			}
			
		}		
				
		
		return subOfferList;
	}

	/**
	 * 
	 * @param offerCatalog
	 * @return provisioning attributes domain object as per the provisioning channel
	 * 
	 */
	private static ProvisioningAttributesDomain getProvisioningAttributesDomain(OfferCatalogDto offerCatalog) {
		
		String provisioningChannel = offerCatalog.getProvisioningChannel();
		String ratePlanCode = null;
		String rtfProductCode = null;
		String rtfProductType = null;
		String vasCode = null;
		String vasActionId = null;
		Integer promotionalPeriod = null;
		String feature = null;
		String serviceId = null;
		String activityId = null;
		String packName = null; 

		if (Checks.checkIsComsProvisioningChannel(provisioningChannel)) {
			
			ratePlanCode = offerCatalog.getRatePlanCode();

		} else if (Checks.checkIsRtfProvisioningChannel(provisioningChannel)) {
			
			rtfProductCode = offerCatalog.getRtfProductCode();
			rtfProductType = offerCatalog.getRtfProductType();

		} else if (Checks.checkIsEmcaisProvisioningChannel(provisioningChannel)) {
			
			vasCode = offerCatalog.getVasCode();
			vasActionId = offerCatalog.getVasActionId();
			
		} else if(Checks.checkIsPhoneyTunesProvisioningChannel(provisioningChannel)) {
			
			promotionalPeriod = offerCatalog.getPromotionalPeriod();
			
		} else if (Checks.checkIsRbtProvisioningChannel(provisioningChannel)) {
			
			feature = offerCatalog.getFeature();
			serviceId = offerCatalog.getServiceId();
			activityId = offerCatalog.getActivityId();
			packName = offerCatalog.getPackName(); 
			
		} 

		return new ProvisioningAttributesDomain(ratePlanCode, rtfProductCode, rtfProductType, vasCode, vasActionId, promotionalPeriod,
				feature, serviceId, activityId, packName);
	}

	/**
	 * 
	 * @param offerLimit
	 * @return list of domain object for offer limit
	 */
	private static List<OfferLimitDomain> getOfferLimitDomainList(List<LimitDto> offerLimit) {
		
	     List<OfferLimitDomain> offerLimitList = null;
   	
	     if(CollectionUtils.isNotEmpty(offerLimit)) {
	    	 
	    	offerLimitList = new ArrayList<>(offerLimit.size()); 
	    	OfferLimitDomain limitDomain = null;
	          	
	        	for(LimitDto limitDto : offerLimit) {
	        		
	        		if(Checks.checkNotEmptyLimit(limitDto)) {
	        			
	        			limitDomain = new OfferLimitDomain(
		        				limitDto.getCustomerSegment(),
		        				limitDto.getCouponQuantity(),
		        				limitDto.getDownloadLimit(), 
		        				limitDto.getDailyLimit(), 
		        				limitDto.getWeeklyLimit(), 
		        				limitDto.getMonthlyLimit(), 
		        				limitDto.getAnnualLimit(), 
			    				getDenominationLimitDomainList(limitDto.getDenominationLimit()), 
			    				limitDto.getAccountDailyLimit(), 
			    				limitDto.getAccountWeeklyLimit(), 
			    				limitDto.getAccountMonthlyLimit(), 
			    				limitDto.getAccountAnnualLimit(),
			    				limitDto.getAccountTotalLimit(), 
			    				getDenominationLimitDomainList(limitDto.getAccountDenominationLimit()),
			    				limitDto.getMemberDailyLimit(), 
			    				limitDto.getMemberWeeklyLimit(), 
			    				limitDto.getMemberMonthlyLimit(), 
			    				limitDto.getMemberAnnualLimit(),
			    				limitDto.getMemberTotalLimit(), 
			    				getDenominationLimitDomainList(limitDto.getMemberDenominationLimit()));
			        	
	        			offerLimitList.add(limitDomain);
	        			
	        		}    		
	        	}
	        	
	     }
	     
	     return !CollectionUtils.isEmpty(offerLimitList) ? offerLimitList : null;
	}
	
	/***
	 * 
	 * @param denominationLimitList
	 * @return list of denomination limit domain object
	 * 
	 */
    private static List<DenominationLimitDomain> getDenominationLimitDomainList(List<DenominationLimitDto> denominationLimitList) {
		
		List <DenominationLimitDomain> denominationLimitDomainList = (denominationLimitList!=null) 
				? new ArrayList<>(denominationLimitList.size()) : null;
		
		if(denominationLimitList!=null) {
			
			denominationLimitList.forEach(
					d->{
							if(Checks.checkNotEmptyDenominationLimit(d)) {
								denominationLimitDomainList
								.add(new DenominationLimitDomain(d.getDenomination(), d.getDailyLimit(),
										d.getWeeklyLimit(), d.getAnnualLimit(),
										d.getMonthlyLimit(), d.getTotalLimit()));
							
							}
						});
		}
		
		return denominationLimitDomainList;
		
	}

	/**
	 * 
	 * @param denominationList
	 * @param offerReferences
	 * @return domain object for associated denominations for the offer
	 */
	private static List<DenominationDomain> getDenominationDomainByDirhamValue(List<Integer> denominationList, OfferReferences offerReferences) {
		
		List<DenominationDomain> denominations = null;
		
		if(CollectionUtils.isNotEmpty(denominationList)) {
			
			denominations = new ArrayList<>(denominationList.size());
			
			if(CollectionUtils.isNotEmpty(offerReferences.getDenominations())) {
				
				Denomination currentDenomination = null;
				DenominationDomain denominationDomain = null; 
				for (Integer denom : denominationList) {
					
					currentDenomination = FilterValues
							.findAnyDenominationInDenominationList(offerReferences.getDenominations(), Predicates.sameDirhamValueForDenomination(denom));
					
					denominationDomain = new DenominationDomain.DenominationBuilder(currentDenomination.getDenominationId()).build();
					denominations.add(denominationDomain);
					
				}
				
				
			}
			
		}
		
		return denominations;
	}

	/**
	 * 
	 * @param storeCodes
	 * @param offerReference
	 * @return domain object for associated stores for the offer
	 */
	private static List<StoreDomain> getStoreDomain(List<String> storeCodes, OfferReferences offerReference) {
		
		List<StoreDomain> stores = new ArrayList<>(storeCodes.size());
		for (String code : storeCodes) {
			
			Store sto = FilterValues.findAnyStoreInList(offerReference.getStore(), Predicates.sameStoreCode(code));
			StoreDomain store = new StoreDomain.StoreBuilder(sto.getId()).build();
			stores.add(store);
		}
		return stores;	
	}
	
	/**
	 * 
	 * @param partnerActivity
	 * @return  domain object for marketplace activity
	 */
	public static MarketplaceActivityDomain getPartnerActivityDomain(MarketplaceActivity partnerActivity) {
		
		ActivityCodeDescriptionDomain activityCodeDescription = !ObjectUtils.isEmpty(partnerActivity) && !ObjectUtils.isEmpty(partnerActivity.getActivityCodeDescription())
				? new ActivityCodeDescriptionDomain(Utilities.getStringValueOrNull(partnerActivity.getActivityCodeDescription().getActivityCodeDescriptionEn()), Utilities.getStringValueOrNull(partnerActivity.getActivityCodeDescription().getActivityCodeDescriptionAr()))
				: null;
		
		 return !ObjectUtils.isEmpty(partnerActivity) 
			  ? new MarketplaceActivityDomain(Utilities.getStringValueOrNull(partnerActivity.getActivityId()), Utilities.getStringValueOrNull(partnerActivity.getActivityCode()), activityCodeDescription)
			  : null;
		
	 }
	
	/**
	 * 
	 * @param eligibilityInfo
	 * @param purchaseRequest
	 * @param paymentResponse
	 * @param savedPurchaseHistory
	 * @param headers
	 * @param purchaseExtraAttributes
	 * @return domain object for purchase history
	 */
	public static PurchaseDomain getPurchaseDomain(EligibilityInfo eligibilityInfo, PurchaseRequestDto purchaseRequest, 
			PaymentResponse paymentResponse, PurchaseHistory savedPurchaseHistory, Headers headers, PurchaseExtraAttributes purchaseExtraAttributes) {
		
		return new PurchaseDomain
				.PurchaseDomainBuilder(purchaseRequest.getSelectedPaymentItem(), 
									   purchaseRequest.getMembershipCode(),
									   purchaseRequest.getAccountNumber(),
									   purchaseRequest.getSelectedOption(),
						               ProcessValues.getPartnerCode(eligibilityInfo, purchaseRequest.getSelectedPaymentItem()))
				.id(!ObjectUtils.isEmpty(savedPurchaseHistory)?savedPurchaseHistory.getId():null)
				.programCode(headers.getProgram())
				.merchantCode(!ObjectUtils.isEmpty(eligibilityInfo)?eligibilityInfo.getOffer().getMerchant().getMerchantCode():null)
				.merchantName(!ObjectUtils.isEmpty(eligibilityInfo)?eligibilityInfo.getOffer().getMerchant().getMerchantName().getMerchantNameEn():null)
				.offerId(Utilities.getStringValueOrNull(purchaseRequest.getOfferId()))
				.offerType(!ObjectUtils.isEmpty(eligibilityInfo)?eligibilityInfo.getOffer().getOfferType().getOfferDescription().getTypeDescriptionEn():null)
				.subOfferId(Utilities.getStringValueOrNull(purchaseRequest.getSubOfferId()))
				.promoCode(Utilities.getStringValueOrNull(purchaseRequest.getPromoCode()))
				.transactionType(ProcessValues.getTransactionType(purchaseRequest.getSelectedOption()))
				.transactionNo(ProcessValues.getTransactionNumber(paymentResponse))
				.extRefNo(headers.getExternalTransactionId())
				.epgTransactionId(ProcessValues.getEpgTransactionId(paymentResponse))
				.couponQuantity(Utilities.getIntegerValueOrNull(purchaseRequest.getCouponQuantity()))
				.voucherCode(ProcessValues.getVoucherCodes(paymentResponse))
				.subscriptionId(ProcessValues.getSubscriptionId(purchaseRequest))
				.spentAmount(Utilities.getDoubleValueOrNull(purchaseRequest.getSpentAmount()))
				.spentPoints(Utilities.getNegativeIntegerValue(Utilities.getIntegerValueOrNull(purchaseRequest.getSpentPoints())))
				.partnerActivity(getPartnerActivityDomain(eligibilityInfo))
				.cost(!ObjectUtils.isEmpty(eligibilityInfo) ? eligibilityInfo.getAmountInfo().getCost() : null)
				.vatAmount(!ObjectUtils.isEmpty(eligibilityInfo) ? eligibilityInfo.getAmountInfo().getVatAmount() : null)
				.purchaseAmount(!ObjectUtils.isEmpty(eligibilityInfo) ? eligibilityInfo.getAmountInfo().getPurchaseAmount() : null)
				.status(ProcessValues.getStatus(paymentResponse))
				.statusReason(ProcessValues.getStatusReason(paymentResponse))
				.language(Utilities.getStringValueOrNull(purchaseRequest.getUiLanguage()))
				.channelId(headers.getChannelId())
				.additionalDetails(getAdditionalDetailsDomain(eligibilityInfo))
				.referralAccountNumber(getReferralAccountNumber(eligibilityInfo))
				.referralBonusCode(getReferralBonusCode(eligibilityInfo))
				.pointsTransactionId(ProcessValues.getPointsTransactionId(paymentResponse, purchaseRequest))
				.customerType(ProcessValues.getCustomerType(eligibilityInfo, purchaseRequest))
				.subscriptionSegment(ProcessValues.getSubscriptionSegment(purchaseExtraAttributes))
				.createdUser(ProcessValues.getCreatedUser(savedPurchaseHistory, headers))
				.createdDate(ProcessValues.getCreatedDate(savedPurchaseHistory))
				.updatedUser(headers.getUserName())
				.updatedDate(new Date())
				.preferredNumber(purchaseRequest.getPreferredNumber())
				.build();
	}
	
	/**
	 * 
	 * @param eligibilityInfo
	 * @param purchaseRequest
	 * @param phoneyTunesResponse
	 * @param savedPurchaseHistory
	 * @param headers
	 * @param purchaseExtraAttributes
	 * @return domain object for purchase history
	 */
	public static PurchaseDomain getPurchaseDomainForPhoneyTunes(EligibilityInfo eligibilityInfo, PurchaseRequestDto purchaseRequest, 
			PhoneyTunesResponse phoneyTunesResponse, PurchaseHistory savedPurchaseHistory, Headers headers, PurchaseExtraAttributes purchaseExtraAttributes) {
		
		return new PurchaseDomain
				.PurchaseDomainBuilder(purchaseRequest.getSelectedPaymentItem(), 
									   purchaseRequest.getMembershipCode(),
									   purchaseRequest.getAccountNumber(),
									   purchaseRequest.getSelectedOption(),
						               ProcessValues.getPartnerCode(eligibilityInfo, purchaseRequest.getSelectedPaymentItem()))
				.id(!ObjectUtils.isEmpty(savedPurchaseHistory)?savedPurchaseHistory.getId():null)
				.programCode(headers.getProgram())
				.merchantCode(!ObjectUtils.isEmpty(eligibilityInfo)?eligibilityInfo.getOffer().getMerchant().getMerchantCode():null)
				.merchantName(!ObjectUtils.isEmpty(eligibilityInfo)?eligibilityInfo.getOffer().getMerchant().getMerchantName().getMerchantNameEn():null)
				.offerId(Utilities.getStringValueOrNull(purchaseRequest.getOfferId()))
				.offerType(!ObjectUtils.isEmpty(eligibilityInfo)?eligibilityInfo.getOffer().getOfferType().getOfferDescription().getTypeDescriptionEn():null)
				.subOfferId(Utilities.getStringValueOrNull(purchaseRequest.getSubOfferId()))
				.promoCode(Utilities.getStringValueOrNull(purchaseRequest.getPromoCode()))
				.transactionType(ProcessValues.getTransactionType(purchaseRequest.getSelectedOption()))
				.transactionNo(phoneyTunesResponse.getTransactionID())
				.extRefNo(headers.getExternalTransactionId())
				.epgTransactionId(null)
				.couponQuantity(Utilities.getIntegerValueOrNull(purchaseRequest.getCouponQuantity()))
				.voucherCode(null)
				.subscriptionId(Utilities.getStringValueOrNull(purchaseRequest.getSubscriptionCatalogId()))
				.spentAmount(Utilities.getDoubleValueOrNull(purchaseRequest.getSpentAmount()))
				.spentPoints(Utilities.getNegativeIntegerValue(Utilities.getIntegerValueOrNull(purchaseRequest.getSpentPoints())))
				.partnerActivity(getPartnerActivityDomain(eligibilityInfo))
				.cost(!ObjectUtils.isEmpty(eligibilityInfo) ? eligibilityInfo.getAmountInfo().getCost() : null)
				.vatAmount(!ObjectUtils.isEmpty(eligibilityInfo) ? eligibilityInfo.getAmountInfo().getVatAmount() : null)
				.purchaseAmount(!ObjectUtils.isEmpty(eligibilityInfo) ? eligibilityInfo.getAmountInfo().getPurchaseAmount() : null)
				//.status(phoneyTunesResponse.getAckMessage().getStatus())
				.status(phoneyTunesResponse.getResponse().toUpperCase().contains(MarketplaceConstants.STATUS_SUCCESS.getConstant()) ?
						phoneyTunesResponse.getAckMessage().getStatus() : MarketplaceConstants.STATUS_FAILURE.getConstant())
				.statusReason(null != phoneyTunesResponse.getResponse() ? phoneyTunesResponse.getResponse() : phoneyTunesResponse.getAckMessage().getErrorDescription())
				.language(Utilities.getStringValueOrNull(purchaseRequest.getUiLanguage()))
				.channelId(headers.getChannelId())
				.additionalDetails(getAdditionalDetailsDomain(eligibilityInfo))
				.referralAccountNumber(getReferralAccountNumber(eligibilityInfo))
				.referralBonusCode(getReferralBonusCode(eligibilityInfo))
				.pointsTransactionId(null)
				.subscriptionSegment(ProcessValues.getSubscriptionSegment(purchaseExtraAttributes))
				.createdUser(ProcessValues.getCreatedUser(savedPurchaseHistory, headers))
				.createdDate(ProcessValues.getCreatedDate(savedPurchaseHistory))
				.updatedUser(headers.getUserName())
				.updatedDate(new Date())
				.build();
	}
	
	/**
	 * 
	 * @param purchaseRequest
	 * @param headers
	 * @param error
	 * @return purchase domain for PurchaseHistory with error 
	 */
	public static PurchaseDomain getPurchaseDomainForError(PurchaseRequestDto purchaseRequest, 
			Headers headers, String error) {
		
		return new PurchaseDomain
				.PurchaseDomainBuilder(purchaseRequest.getSelectedPaymentItem(), 
									   purchaseRequest.getMembershipCode(),
									   purchaseRequest.getAccountNumber(),
									   purchaseRequest.getSelectedOption(),
						               null)
				.programCode(headers.getProgram())
				.offerId(Utilities.getStringValueOrNull(purchaseRequest.getOfferId()))
				.subOfferId(Utilities.getStringValueOrNull(purchaseRequest.getSubOfferId()))
				.promoCode(Utilities.getStringValueOrNull(purchaseRequest.getPromoCode()))
				.transactionType(ProcessValues.getTransactionType(purchaseRequest.getSelectedOption()))
				.extRefNo(headers.getExternalTransactionId())
				.couponQuantity(Utilities.getIntegerValueOrNull(purchaseRequest.getCouponQuantity()))
				.subscriptionId(ProcessValues.getSubscriptionId(purchaseRequest))
				.spentAmount(Utilities.getDoubleValueOrNull(purchaseRequest.getSpentAmount()))
				.spentPoints(Utilities.getNegativeIntegerValue(Utilities.getIntegerValueOrNull(purchaseRequest.getSpentPoints())))
				.status(OfferConstants.FAILED.get())
				.statusReason(error)
				.language(Utilities.getStringValueOrNull(purchaseRequest.getUiLanguage()))
				.channelId(headers.getChannelId())
				.createdUser(headers.getUserName())
				.createdDate(new Date())
				.updatedUser(headers.getUserName())
				.updatedDate(new Date())
				.preferredNumber(purchaseRequest.getPreferredNumber())
				.build();
	}
	
	/**
	 * 
	 * @param partnerActivity
	 * @return  domain object for partnerActivity
	 */
	public static MarketplaceActivityDomain getPartnerActivityDomain(EligibilityInfo eligibilityInfo) {
		
		MarketplaceActivity partnerActivity = !ObjectUtils.isEmpty(eligibilityInfo)
				? eligibilityInfo.getOffer().getActivityCode().getRedemptionActivityCode()
				: getSubscriptionActivityCode();
		ActivityCodeDescriptionDomain activityCodeDescription = !ObjectUtils.isEmpty(partnerActivity) && !ObjectUtils.isEmpty(partnerActivity.getActivityCodeDescription())
				? new ActivityCodeDescriptionDomain(Utilities.getStringValueOrNull(partnerActivity.getActivityCodeDescription().getActivityCodeDescriptionEn()), Utilities.getStringValueOrNull(partnerActivity.getActivityCodeDescription().getActivityCodeDescriptionAr()))
				: null;
		return !ObjectUtils.isEmpty(partnerActivity)
			 ? new MarketplaceActivityDomain(Utilities.getStringValueOrNull(partnerActivity.getActivityId()), Utilities.getStringValueOrNull(partnerActivity.getActivityCode()), activityCodeDescription)
			 : null;
	}
	
	/**
	 * 
	 * @return activity code for subscription
	 */
	private static MarketplaceActivity getSubscriptionActivityCode() {

		MarketplaceActivity subscriptionActivityCode = new MarketplaceActivity();
		subscriptionActivityCode.setActivityId(SubscriptionManagementConstants.SUBSCRIPTION_ACTIVITY_ID.get());
		subscriptionActivityCode.setActivityCode(SubscriptionManagementConstants.SUBSCRIPTION_ACTIVITY_CODE.get());
		ActivityCodeDescription activityCodeDescription = new ActivityCodeDescription();
		activityCodeDescription.setActivityCodeDescriptionEn(SubscriptionManagementConstants.SUBSCRIPTION_ACTIVITY_CODE_DESCRIPTION_EN.get());
		activityCodeDescription.setActivityCodeDescriptionAr(SubscriptionManagementConstants.SUBSCRIPTION_ACTIVITY_CODE_DESCRIPTION_AR.get());
		subscriptionActivityCode.setActivityCodeDescription(activityCodeDescription);
		return subscriptionActivityCode;
	}

	/**
	 * 
	 * @param additionalDetails
	 * @return domain object for additional details
	 */
    public static AdditionalDetailsDomain getAdditionalDetailsDomain(EligibilityInfo eligibilityInfo) {
		
		return  !ObjectUtils.isEmpty(eligibilityInfo)
			&&	!ObjectUtils.isEmpty(eligibilityInfo.getAdditionalDetails())
				? new AdditionalDetailsDomain(eligibilityInfo.getAdditionalDetails().isBirthdayOffer(), eligibilityInfo.getAdditionalDetails().isSubscribed(), 
						eligibilityInfo.getAdditionalDetails().isPromotionApplied(), eligibilityInfo.getAdditionalDetails().isDod(),
						eligibilityInfo.getAdditionalDetails().isFree(), eligibilityInfo.getAdditionalDetails().isGift(), 
						eligibilityInfo.getAdditionalDetails().isFeatured(), eligibilityInfo.getAdditionalDetails().isMamba(),
						eligibilityInfo.getAdditionalDetails().isPromotionalGift(), eligibilityInfo.getAdditionalDetails().isSubscriptionBenefit(), 
						eligibilityInfo.getAdditionalDetails().getPromoGiftId(), eligibilityInfo.getAdditionalDetails().isPointsRedemptionGift())
	            : null;
	}

	
	/**
	 * 
	 * @param eligibilityInfo
	 * @return value of referral bonus account number for purchase domain object
	 */
	private static String getReferralAccountNumber(EligibilityInfo eligibilityInfo) {
		
		return !ObjectUtils.isEmpty(eligibilityInfo)
			&& !ObjectUtils.isEmpty(eligibilityInfo.getMemberDetails())
			&& !StringUtils.isEmpty(eligibilityInfo.getMemberDetails().getReferralAccountNumber())
			? eligibilityInfo.getMemberDetails().getReferralAccountNumber()
			: null;
	}
	
	/**
	 * 
	 * @param eligibilityInfo
	 * @return value of referral bonus code for purchase domain object
	 */
	private static String getReferralBonusCode(EligibilityInfo eligibilityInfo) {
		
		return !ObjectUtils.isEmpty(eligibilityInfo)
			&& !ObjectUtils.isEmpty(eligibilityInfo.getMemberDetails())
			&& !StringUtils.isEmpty(eligibilityInfo.getMemberDetails().getReferralBonusCode())
			? eligibilityInfo.getMemberDetails().getReferralBonusCode()
			: null;
	}

	/**
	 * 
	 * @param offerCounter
	 * @param couponQuantity
	 * @param rules
	 * @return domain object for offer counter
	 */
	public static OfferCounterDomain getOfferCounterDomain(OfferCounter offerCounter, Integer couponQuantity, String offerId, String accountNumber, String membershipCode, Integer denomination, List<String> rules) {
		
		OfferCounterDomain offerCounterDomain = null;
		
		List<DenominationCountDomain> denominationCountDomainList = null;
		List<MemberOfferCountDomain> memberOfferCountDomainList = null;
		List<AccountOfferCountDomain> accountOfferCountDomainList = null;
		
		if(!ObjectUtils.isEmpty(offerCounter)) {
			
			denominationCountDomainList = !ObjectUtils.isEmpty(denomination)
						? getDenominationCountDomainList(offerCounter.getDenominationCount(),
								denomination, couponQuantity)
						: null;		
					
			memberOfferCountDomainList = 
					getMemberOfferCountDomainList(offerCounter.getMemberOfferCount(), 
					membershipCode, couponQuantity, denomination);
			
			accountOfferCountDomainList = 
					getAccountOfferCountDomainList(offerCounter.getAccountOfferCount(),
					accountNumber, membershipCode, couponQuantity, denomination);
				
			offerCounterDomain = new OfferCounterDomain
					.OfferCounterBuilder(offerCounter.getId())
					.offerId(offerCounter.getOfferId())
					.rules(rules)
					.dailyCount(offerCounter.getDailyCount()+couponQuantity)
					.weeklyCount(offerCounter.getWeeklyCount()+couponQuantity)
					.monthlyCount(offerCounter.getMonthlyCount()+couponQuantity)
					.annualCount(offerCounter.getAnnualCount()+couponQuantity)
					.totalCount(offerCounter.getTotalCount()+couponQuantity)
					.denominationCount(denominationCountDomainList)
					.accountOfferCount(accountOfferCountDomainList)
					.memberOfferCount(memberOfferCountDomainList)
					.lastPurchased(new Date())
					.build();
			
		} else {
			
			denominationCountDomainList = getDenominationCountDomainList(null, denomination, couponQuantity);
			memberOfferCountDomainList = getMemberOfferCountDomainList(null, membershipCode, couponQuantity, denomination);
			accountOfferCountDomainList = getAccountOfferCountDomainList(null, accountNumber, membershipCode, couponQuantity, denomination);
			
			offerCounterDomain = new OfferCounterDomain.OfferCounterBuilder(offerId, couponQuantity, couponQuantity,
									 couponQuantity, couponQuantity, couponQuantity)
									.rules(rules)
									.denominationCount(denominationCountDomainList)
									.accountOfferCount(accountOfferCountDomainList)
									.memberOfferCount(memberOfferCountDomainList)
									.lastPurchased(new Date())
									.build();
		}
		
		return offerCounterDomain;
	}
	
	/**
	 * 
	 * @param denominationCountList
	 * @param denomination
	 * @param couponQuantity
	 * @return list of denomination count domain
	 */
	private static List<AccountOfferCountDomain> getAccountOfferCountDomainList(List<AccountOfferCount> accountOfferCountList, String accountNumber, String membershipCode, Integer couponQuantity, Integer denomination){
		
		AccountOfferCount existingAccOfferCount = null;
		
		if(!ObjectUtils.isEmpty(accountOfferCountList)) {
			
			existingAccOfferCount = accountOfferCountList.stream()
								.filter(a->a.getAccountNumber().equals(accountNumber))
								.findAny().orElse(null);
			
		}
		
		int increment = !ObjectUtils.isEmpty(existingAccOfferCount) ? 0 : 1;
		int size = CollectionUtils.isNotEmpty(accountOfferCountList)
				? accountOfferCountList.size()+increment
				: increment;
		
		List<AccountOfferCountDomain> accountOfferCountDomainList = new ArrayList<>(size);
		
		AccountOfferCountDomain accountOfferDomain = null;
		
		if(CollectionUtils.isNotEmpty(accountOfferCountList)) {
			
			for(AccountOfferCount accountOffer : accountOfferCountList){
				
				if(!StringUtils.equals(accountNumber, accountOffer.getAccountNumber())) {
					
					List<DenominationCountDomain> denominationCountDomainList = 
							getDenominationCountDomainListForNonCurrent(accountOffer.getDenominationCount());
					accountOfferDomain = new AccountOfferCountDomain(accountOffer.getMembershipCode(),
							accountOffer.getAccountNumber(), 
							accountOffer.getDailyCount(), 
							accountOffer.getWeeklyCount(), 
							accountOffer.getMonthlyCount(), 
							accountOffer.getAnnualCount(), 
							accountOffer.getTotalCount(), 
							accountOffer.getLastPurchased(), 
							denominationCountDomainList);
					accountOfferCountDomainList.add(accountOfferDomain);
					
				}
				
			}
			
		}
		
		List<DenominationCountDomain> denominationCountDomainList 
	        = ObjectUtils.isEmpty(existingAccOfferCount)
	        ?getDenominationCountDomainList(null, denomination, couponQuantity)
	        :getDenominationCountDomainList(existingAccOfferCount.getDenominationCount(), denomination, couponQuantity) ;
	    
        accountOfferDomain = ObjectUtils.isEmpty(existingAccOfferCount)
			? new AccountOfferCountDomain(membershipCode, accountNumber, 
					couponQuantity, couponQuantity, couponQuantity, 
					couponQuantity, couponQuantity, new Date(), 
					denominationCountDomainList)
	        : new AccountOfferCountDomain(membershipCode, accountNumber,  
        		existingAccOfferCount.getDailyCount() + couponQuantity,
        		existingAccOfferCount.getWeeklyCount() + couponQuantity, 
        		existingAccOfferCount.getMonthlyCount() + couponQuantity, 
        		existingAccOfferCount.getAnnualCount() + couponQuantity, 
        		existingAccOfferCount.getTotalCount() + couponQuantity, 
        		new Date(), 
        		denominationCountDomainList);
	
		accountOfferCountDomainList.add(accountOfferDomain);
		return accountOfferCountDomainList;
		
	}
	
	/**
	 * 
	 * @param denominationCountList
	 * @param denomination
	 * @param couponQuantity
	 * @return list of denomination count domain
	 */
	private static List<MemberOfferCountDomain> getMemberOfferCountDomainList(List<MemberOfferCount> memberOfferCountList, String membershipCode, Integer couponQuantity, Integer denomination){
		
		MemberOfferCount existingMemOfferCount = null;
		MemberOfferCountDomain memberOfferDomain = null;
		
		if(CollectionUtils.isNotEmpty(memberOfferCountList)) {
			
			existingMemOfferCount = memberOfferCountList.stream()
								.filter(m->m.getMembershipCode().equals(membershipCode))
								.findAny().orElse(null);
			
		}
		
		int increment = !ObjectUtils.isEmpty(existingMemOfferCount) ? 0 : 1;
		int size = CollectionUtils.isNotEmpty(memberOfferCountList)
				 ?(memberOfferCountList.size()+increment)
				 :(increment);
		List<MemberOfferCountDomain> memberOfferCountDomainList = new ArrayList<>(size);
		
		if(CollectionUtils.isNotEmpty(memberOfferCountList)) {
			
			for(MemberOfferCount memberOffer : memberOfferCountList){
				if(!StringUtils.equals(memberOffer.getMembershipCode(), membershipCode)) {
					
					List<DenominationCountDomain> denominationCountDomainList = 
							getDenominationCountDomainListForNonCurrent(memberOffer.getDenominationCount());
					memberOfferDomain = new MemberOfferCountDomain(memberOffer.getMembershipCode(), 
							memberOffer.getDailyCount(), 
							memberOffer.getWeeklyCount(), 
							memberOffer.getMonthlyCount(), 
							memberOffer.getAnnualCount(), 
							memberOffer.getTotalCount(), 
							new Date(), 
							denominationCountDomainList);
					memberOfferCountDomainList.add(memberOfferDomain);
					
					
				}
			}
			
		} 
		
		List<DenominationCountDomain> denominationCountDomainList 
		       = ObjectUtils.isEmpty(existingMemOfferCount)
               ?getDenominationCountDomainList(null, denomination, couponQuantity)
               :getDenominationCountDomainList(existingMemOfferCount.getDenominationCount(), denomination, couponQuantity) ;
		memberOfferDomain 
		        = ObjectUtils.isEmpty(existingMemOfferCount)
				? new MemberOfferCountDomain(membershipCode, 
						couponQuantity, 
						couponQuantity, 
						couponQuantity, 
						couponQuantity, 
						couponQuantity, 
						new Date(), denominationCountDomainList)
		        : new MemberOfferCountDomain(membershipCode, 
		        	existingMemOfferCount.getDailyCount() + couponQuantity,
		        	existingMemOfferCount.getWeeklyCount() + couponQuantity, 
		        	existingMemOfferCount.getMonthlyCount() + couponQuantity, 
		        	existingMemOfferCount.getAnnualCount() + couponQuantity, 
		        	existingMemOfferCount.getTotalCount() + couponQuantity, 
		        	new Date(), denominationCountDomainList);
		memberOfferCountDomainList.add(memberOfferDomain);
		
		return memberOfferCountDomainList;
	}
		
	/**
	 * 
	 * @param denominationCountList
	 * @param denomination
	 * @param couponQuantity
	 * @return list of denomination count domain
	 */
	private static List<DenominationCountDomain> getDenominationCountDomainList(List<DenominationCount> denominationCountList, Integer denomination, Integer couponQuantity){
		
		int size = CollectionUtils.isNotEmpty(denominationCountList)
				? denominationCountList.size()+1
				: 1;
		
		List<DenominationCountDomain> denominationCountDomainList = new ArrayList<>(size);
		
		if(null!=denomination) {
		
			DenominationCountDomain denomCountDomain = null;
			DenominationCount existingDenominationCount = null;
			
			if(CollectionUtils.isNotEmpty(denominationCountList)){	
				
				existingDenominationCount = denominationCountList.stream().filter(d->d.getDenomination().equals(denomination))
						.findAny().orElse(null);
				
				for(DenominationCount denomCount : denominationCountList) {
					
					if(!denomCount.getDenomination().equals(denomination)) {
						
						denomCountDomain = new DenominationCountDomain(denomCount.getDenomination(), 
								denomCount.getDailyCount(), 
								denomCount.getWeeklyCount(), 
								denomCount.getMonthlyCount(), 
								denomCount.getAnnualCount(), 
								denomCount.getTotalCount(), 
								denomCount.getLastPurchased());
						denominationCountDomainList.add(denomCountDomain);	
					}
					
				} 
				
			}
			
			denomCountDomain = ObjectUtils.isEmpty(existingDenominationCount)
					?   new DenominationCountDomain(denomination,
							couponQuantity,
							couponQuantity,
							couponQuantity,
							couponQuantity,
							couponQuantity,
							new Date())
					:   new DenominationCountDomain(existingDenominationCount.getDenomination(),
							existingDenominationCount.getDailyCount()+couponQuantity,
							existingDenominationCount.getWeeklyCount()+couponQuantity,
							existingDenominationCount.getMonthlyCount()+couponQuantity,
							existingDenominationCount.getAnnualCount()+couponQuantity,
							existingDenominationCount.getTotalCount()+couponQuantity,
							new Date());		
			
			denominationCountDomainList.add(denomCountDomain);
		
		}
		
		return denominationCountDomainList;
		
	}
	
	/**
	 * 
	 * @param denominationCountList
	 * @param denomination
	 * @param couponQuantity
	 * @return list of DenominationCountDomain for account/membership not involved in current purchase 
	 */
	private static List<DenominationCountDomain> getDenominationCountDomainListForNonCurrent(List<DenominationCount> denominationCountList){
		
		List<DenominationCountDomain> denominationCountDomainList = new ArrayList<>(1);
		
		if(CollectionUtils.isNotEmpty(denominationCountList)) {
		
			denominationCountDomainList = new ArrayList<>(denominationCountList.size());
			DenominationCountDomain denomCountDomain = null;
			
			for(DenominationCount denomCount : denominationCountList) {
				
				denomCountDomain = new DenominationCountDomain(denomCount.getDenomination(), 
						denomCount.getDailyCount(), 
						denomCount.getWeeklyCount(), 
						denomCount.getMonthlyCount(), 
						denomCount.getAnnualCount(), 
						denomCount.getTotalCount(), 
						denomCount.getLastPurchased());
				denominationCountDomainList.add(denomCountDomain);	
				
			} 
				
		}
		
		return denominationCountDomainList;
		
	}

	/**
     * 
     * @param offerRatingExists
     * @param offerRatingRequest
     * @param getMemberResponse
     * @param userName
     * @param isNewAccount
     * @return  domain object for member rating and comments
     */
	public static OfferRatingDomain createOfferRatingDomainForAccount(OfferRating offerRatingExists, OfferRatingDto offerRatingRequest,
			GetMemberResponse getMemberResponse, String userName, String isNewAccount) {
		
		Integer updatedRateCount = 0;
		List<MemberCommentDomain> memberCommentsList;
		List<MemberRatingDomain> memberRatingList = new ArrayList<>();
		
		for(MemberRating memberRating : offerRatingExists.getMemberRatings()) {
			memberCommentsList = new ArrayList<>(offerRatingExists.getMemberRatings().size()+1);
			for(MemberComment memberComment : memberRating.getComments()) {
				MemberCommentDomain memberCommentDomain = new MemberCommentDomain.MemberCommentBuilder(
						memberComment.getReviewDate())
						.comment(memberComment.getComment())
						.rating(memberComment.getRating())
						.build();
				
				memberCommentsList.add(memberCommentDomain);
			}
			
			if (memberRating.getAccountNumber().equals(offerRatingRequest.getAccountNumber())
					&& memberRating.getMembershipCode().equals(getMemberResponse.getMembershipCode())) {
				MemberCommentDomain newRatingComment = new MemberCommentDomain.MemberCommentBuilder(
						new Date())
						.comment(offerRatingRequest.getComment())
						.rating(offerRatingRequest.getRating())
						.build();
				memberCommentsList.add(newRatingComment);
			}
			
			MemberRatingDomain memberRatingDomain = new MemberRatingDomain.MemberRatingBuilder(
					memberRating.getAccountNumber(), 
					memberRating.getMembershipCode(),
					memberRating.getFirstName(), 
					memberRating.getLastName(),
					memberCommentsList)
					.build();
			memberRatingList.add(memberRatingDomain);
		}
		
		if(OfferConstants.OFFER_RATING_FOR_NEW_ACCOUNT.get().equalsIgnoreCase(isNewAccount)) {
			MemberCommentDomain memberCommentDomain = new MemberCommentDomain.MemberCommentBuilder(
					new Date())
					.comment(offerRatingRequest.getComment())
					.rating(offerRatingRequest.getRating())
					.build();

			List<MemberCommentDomain> newMemberCommentList = new ArrayList<>();
			newMemberCommentList.add(memberCommentDomain);

			MemberRatingDomain newMemberRatingDomain = new MemberRatingDomain.MemberRatingBuilder(
					offerRatingRequest.getAccountNumber(), 
					getMemberResponse.getMembershipCode(),
					getMemberResponse.getFirstName(), 
					getMemberResponse.getLastName(),
					newMemberCommentList)
					.build();
			memberRatingList.add(newMemberRatingDomain);
		}
	
		Double totalRating = offerRatingExists.getAverageRating() * offerRatingExists.getRatingCount();
		updatedRateCount = offerRatingExists.getRatingCount() + 1;
		
		return new OfferRatingDomain.OfferRatingBuilder(
				offerRatingExists.getOfferId(), 
				memberRatingList, 
				(totalRating + offerRatingRequest.getRating())/updatedRateCount)
				.id(offerRatingExists.getId())
				.commentCount(null != offerRatingRequest.getComment()
								? offerRatingExists.getCommentCount() + 1
								: offerRatingExists.getCommentCount())
				.ratingCount(null != offerRatingRequest.getRating()
						? updatedRateCount
						: offerRatingExists.getRatingCount())
				.programCode(offerRatingExists.getProgramCode())
				.createdDate(offerRatingExists.getCreatedDate())
				.createdUser(offerRatingExists.getCreatedUser())
				.updatedDate(new Date())
				.updatedUser(userName)
				.build();
		
	}

	/**
	 * 
	 * @param offerRatingRequest
	 * @param getMemberResponse
	 * @param userName
	 * @param program
	 * @return  domain object for offer rating
	 */
	public static OfferRatingDomain createOfferRatingDomainNewOffer(OfferRatingDto offerRatingRequest,
			GetMemberResponse getMemberResponse, String userName, String program) {
		
		List<MemberCommentDomain> memberCommentsList = new ArrayList<>();
		List<MemberRatingDomain> memberRatingList = new ArrayList<>();
		MemberCommentDomain memberCommentDomain = new MemberCommentDomain.MemberCommentBuilder(
				new Date())
				.comment(offerRatingRequest.getComment())
				.rating(offerRatingRequest.getRating())
				.build();
		memberCommentsList.add(memberCommentDomain);

		MemberRatingDomain memberRatingDomain = new MemberRatingDomain.MemberRatingBuilder(
				offerRatingRequest.getAccountNumber(), getMemberResponse.getMembershipCode(),
				getMemberResponse.getFirstName(), getMemberResponse.getLastName(),
				memberCommentsList).build();
		memberRatingList.add(memberRatingDomain);

		return new OfferRatingDomain.OfferRatingBuilder(
				offerRatingRequest.getOfferId(), memberRatingList, Double.valueOf(offerRatingRequest.getRating()))
				.commentCount(null != offerRatingRequest.getComment() ? 1 : 0)
				.ratingCount(1)
				.programCode(program)
				.createdDate(new Date())
				.createdUser(userName)
				.updatedDate(new Date())
				.updatedUser(userName)
				.build();
		
	}
	
	/**
	 * 
	 * @param memberDetails
	 * @param wishlist
	 * @param program
	 * @param accountNumber
	 * @param wishlistRequest
	 * @param userName
	 * @param resultResponse
	 * @return domain object for wishlist to add offer
	 */
	public static WishlistDomain getWishListDomainForAddingOffer(GetMemberResponse memberDetails, WishlistEntity wishlist, String program, String accountNumber, 
			WishlistRequestDto wishlistRequest, String userName, ResultResponse resultResponse) {
		
		WishlistDomain wishlistOfferDomain = null;
		
		if (ObjectUtils.isEmpty(wishlist)) {
	
			wishlistOfferDomain = new WishlistDomain.WishlistBuilder(program, accountNumber, Arrays.asList(wishlistRequest.getOfferId()))
					.dtCreated(new Date()).dtUpdated(new Date())
					.usrCreated(userName).usrUpdated(userName)
					.membershipCode(memberDetails.getMembershipCode())
					.build();
			
		} else {
			
			
			List<String> offerDomainList = getOfferDomainList(wishlist, wishlistRequest, resultResponse);
			
			if(null!=resultResponse.getApiStatus().getErrors() || resultResponse.getApiStatus().getErrors().isEmpty()) {
				
				wishlistOfferDomain = new WishlistDomain.WishlistBuilder(wishlist.getId(), wishlist.getProgram(), accountNumber, offerDomainList)
						.membershipCode(memberDetails.getMembershipCode())
						.dtCreated(wishlist.getDtCreated()).dtUpdated(new Date())
						.usrCreated(wishlist.getUsrCreated()).usrUpdated(userName).build();

				
			}		
		
		}
		
		return wishlistOfferDomain;
	}

    /**
     * 
     * @param wishlist
     * @param wishlistRequest
     * @param resultResponse
     * @return domain object for offer list in wishlist
     */
	private static List<String> getOfferDomainList(WishlistEntity wishlist, WishlistRequestDto wishlistRequest, ResultResponse resultResponse) {
		
		boolean offersAlreadyPresent = null!=wishlist.getOffers() && !wishlist.getOffers().isEmpty();
		
		List<String> offerDomainList  = offersAlreadyPresent
				? new ArrayList<>(wishlist.getOffers().size()+1)
				: new ArrayList<>(1);
		
		if(offersAlreadyPresent) {
			
			if(wishlist.getOffers().contains(wishlistRequest.getOfferId())) {
				
				resultResponse.addErrorAPIResponse(OfferErrorCodes.OFFER_ALREADY_IN_WISHLIST.getIntId(),
						OfferErrorCodes.OFFER_ALREADY_IN_WISHLIST.getMsg());
				
			} else {
				
				for(String offerId : wishlist.getOffers()) {
					
					offerDomainList.add(offerId);
					
				}
				
			}
			
		}
		
		offerDomainList.add(wishlistRequest.getOfferId());
		return offerDomainList;
	}

	/**
	 * 
	 * @param header
	 * @param birthdayInfo
	 * @param birthayInfoRequest
	 * @return  domain object for birthday info
	 */
	public static BirthdayInfoDomain getBirthdayInfoDomain(Headers header, BirthdayInfo birthdayInfo,
			BirthdayInfoRequestDto birthayInfoRequest) {
		
		boolean isInsert = ObjectUtils.isEmpty(birthdayInfo);
				  
		return new BirthdayInfoDomain.BirthdayInfoBuilder(
				        Utilities.getIntegerValue(birthayInfoRequest.getPurchaseLimit()),
				        Utilities.getIntegerValue(birthayInfoRequest.getThresholdPlusValue()),
				        Utilities.getIntegerValue(birthayInfoRequest.getThresholdMinusValue()),
				        Utilities.getIntegerValue(birthayInfoRequest.getDisplayLimit()))
				   .programCode(isInsert ? header.getProgram() : birthdayInfo.getProgramCode())
				   .id(isInsert ? null : birthdayInfo.getId())
				   .title(new BirthdayTitleDomain(birthayInfoRequest.getTitleEn(), birthayInfoRequest.getTitleAr()))
				   .subTitle(new BirthdaySubTitleDomain(birthayInfoRequest.getSubTitleEn(), birthayInfoRequest.getSubTitleAr()))
				   .description(new BirthdayDescriptionDomain(birthayInfoRequest.getDescriptionEn(), birthayInfoRequest.getDescriptionAr()))
				   .iconText(new BirthdayIconTextDomain(birthayInfoRequest.getIconTextEn(), birthayInfoRequest.getIconTextAr()))
				   .weekIcon(new BirthdayWeekIconDomain(birthayInfoRequest.getWeekIconEn(), birthayInfoRequest.getWeekIconAr()))
				   .createdDate(isInsert?new Date(): birthdayInfo.getCreatedDate())
				   .createdUser(isInsert ? header.getUserName() : birthdayInfo.getCreatedUser())
				   .updatedDate(new Date())
				   .updatedUser(header.getUserName())
				   .build();
	}

	/**
	 * 
	 * @param wishlist
	 * @param accountNumber
	 * @param offerDomainList
	 * @param headers
	 * @return domain object for wishlist
	 */
	public static WishlistDomain getWishlistDomainForRemovingOffer(WishlistEntity wishlist,
			String accountNumber, List<String> offerDomainList, Headers headers) {
		
		return new WishlistDomain.WishlistBuilder(wishlist.getId(), wishlist.getProgram(), accountNumber, offerDomainList)
			    .membershipCode(wishlist.getMembershipCode())
				.dtCreated(wishlist.getDtCreated()).dtUpdated(new Date())
				.usrCreated(wishlist.getUsrCreated()).usrUpdated(headers.getUserName())
				.build();
	}

	/**
	 * 
	 * @param birthdayGiftTracker
	 * @param memberDetails
	 * @param headers
	 * @return  domain object for birthday gift tracker
	 */
	public static BirthdayGiftTrackerDomain getBirthdayConfigurationDomain(BirthdayGiftTracker birthdayGiftTracker,
			GetMemberResponse memberDetails, Headers headers) {
		
		boolean isInsert = ObjectUtils.isEmpty(birthdayGiftTracker);
		return new BirthdayGiftTrackerDomain
			.BirthdayGiftTrackerDomainBuilder(memberDetails.getMembershipCode(),
					memberDetails.getAccountNumber(),
					memberDetails.getDob(),
					new Date())
			.programCode(isInsert ? headers.getProgram() : birthdayGiftTracker.getProgramCode())
			.id(isInsert?null:birthdayGiftTracker.getId())
			.createdAt(isInsert ? new Date() : birthdayGiftTracker.getCreatedAt())
			.createdBy(isInsert ? headers.getUserName() : birthdayGiftTracker.getCreatedBy())
			.updatedAt(new Date())
			.updatedBy(headers.getUserName())
			.build();
			
	}
	
	/**
     * 
     * @param isGift
     * @param giftChannels
     * @param giftSubCardTypes
     * @return domain object for GiftInfo
     * 
     */
    public static GiftInfoDomain getGiftDomain(String isGift, List<String> giftChannels, List<String> giftSubCardTypes) {
		
		return new GiftInfoDomain(Utilities.getFlag(isGift), Utilities.getListOrNull(giftChannels), Utilities.getListOrNull(giftSubCardTypes));
	}
    
    /**
     * 
     * @param offerCounter
     * @param couponQuantity
     * @param offerId
     * @param denomination
     * @param rules
     * @return Domain object for offer counter domain
     */
	public static OfferCountersDomain getOfferCountersDomain(OfferCounters offerCounter, Integer couponQuantity,
			String offerId, Integer denomination, List<String> rules) {
		
		OfferCountersDomain offerCounterDomain = null;
		List<DenominationCountDomain> denominationCountDomainList = null;
		
		if(!ObjectUtils.isEmpty(offerCounter)) {
			
			if(!StringUtils.isEmpty(offerCounter.getId()) && !ObjectUtils.isEmpty(couponQuantity)) {
				
				denominationCountDomainList = !ObjectUtils.isEmpty(denomination)
						? getDenominationCountDomainList(offerCounter.getDenominationCount(),
								denomination, couponQuantity)
						: new ArrayList<>(1);		
					
				offerCounterDomain = new OfferCountersDomain
						.OfferCounterBuilder(offerCounter.getId())
						.offerId(offerCounter.getOfferId())
						.rules(rules)
						.dailyCount(offerCounter.getDailyCount()+couponQuantity)
						.weeklyCount(offerCounter.getWeeklyCount()+couponQuantity)
						.monthlyCount(offerCounter.getMonthlyCount()+couponQuantity)
						.annualCount(offerCounter.getAnnualCount()+couponQuantity)
						.totalCount(offerCounter.getTotalCount()+couponQuantity)
						.denominationCount(denominationCountDomainList)
						.lastPurchased(new Date())
						.build();
			} else {
				
				denominationCountDomainList = getDenominationCountDomainList(offerCounter.getDenominationCount(), denomination, couponQuantity);
				
				offerCounterDomain = new OfferCountersDomain.OfferCounterBuilder(offerCounter.getOfferId(), offerCounter.getDailyCount(), 
										 offerCounter.getWeeklyCount(), offerCounter.getMonthlyCount(), 
										 offerCounter.getAnnualCount(), offerCounter.getTotalCount())
										.id(offerCounter.getId())
						                .rules(offerCounter.getRules())
										.denominationCount(denominationCountDomainList)
										.lastPurchased(new Date())
										.build();
				
				
			}
			
		} else {
			
			denominationCountDomainList = getDenominationCountDomainList(null, denomination, couponQuantity);
			
			offerCounterDomain = new OfferCountersDomain.OfferCounterBuilder(offerId, couponQuantity, couponQuantity,
									 couponQuantity, couponQuantity, couponQuantity)
									.rules(rules)
									.denominationCount(denominationCountDomainList)
									.lastPurchased(new Date())
									.build();
		}
		
		return offerCounterDomain;
	}

	/**
	 * 
	 * @param memberOfferCounter
	 * @param couponQuantity
	 * @param offerId
	 * @param membershipCode
	 * @param denomination
	 * @return Domain object for member offer counter domain
	 */
	public static MemberOfferCountersDomain getMemberOfferCountersDomain(MemberOfferCounts memberOfferCounter,
			Integer couponQuantity, String offerId, String membershipCode, Integer denomination) {
		
		MemberOfferCountersDomain offerCounterDomain = null;
		List<DenominationCountDomain> denominationCountDomainList = null;
		
		if(!ObjectUtils.isEmpty(memberOfferCounter)) {
			
			if(!StringUtils.isEmpty(memberOfferCounter.getId()) && !ObjectUtils.isEmpty(couponQuantity)) {
				
				denominationCountDomainList = !ObjectUtils.isEmpty(denomination)
						? getDenominationCountDomainList(memberOfferCounter.getDenominationCount(),
								denomination, couponQuantity)
						: new ArrayList<>(1);		
					
				offerCounterDomain = new MemberOfferCountersDomain
					.MemberOfferCountersBuilder(memberOfferCounter.getId())
					.offerId(memberOfferCounter.getOfferId())
					.membershipCode(memberOfferCounter.getMembershipCode())
					.dailyCount(memberOfferCounter.getDailyCount()+couponQuantity)
					.weeklyCount(memberOfferCounter.getWeeklyCount()+couponQuantity)
					.monthlyCount(memberOfferCounter.getMonthlyCount()+couponQuantity)
					.annualCount(memberOfferCounter.getAnnualCount()+couponQuantity)
					.totalCount(memberOfferCounter.getTotalCount()+couponQuantity)
					.denominationCount(denominationCountDomainList)
					.lastPurchased(new Date())
					.build();
				
			} else {
				
				denominationCountDomainList = getDenominationCountDomainList(memberOfferCounter.getDenominationCount(), denomination, couponQuantity);
				
				offerCounterDomain = new MemberOfferCountersDomain
						.MemberOfferCountersBuilder(memberOfferCounter.getMembershipCode(), memberOfferCounter.getOfferId(),
										 memberOfferCounter.getDailyCount(), memberOfferCounter.getWeeklyCount(),
										 memberOfferCounter.getMonthlyCount(), memberOfferCounter.getAnnualCount(), 
										 memberOfferCounter.getTotalCount())
										.id(memberOfferCounter.getId())
										.denominationCount(denominationCountDomainList)
										.lastPurchased(new Date())
										.build();
				
			}
			
		} else {
			
			denominationCountDomainList = getDenominationCountDomainList(null, denomination, couponQuantity);
			
			offerCounterDomain = new MemberOfferCountersDomain
					.MemberOfferCountersBuilder(membershipCode, offerId, couponQuantity, couponQuantity,
									 couponQuantity, couponQuantity, couponQuantity)
									.denominationCount(denominationCountDomainList)
									.lastPurchased(new Date())
									.build();
		}
		
		return offerCounterDomain;
	}

	/**
	 * 
	 * @param accountOfferCounter
	 * @param couponQuantity
	 * @param savedOfferCounter
	 * @param accountNumber
	 * @param savedMemberOfferCounter
	 * @param denomination
	 * @return Domain object for account offer counters domain
	 */
	public static AccountOfferCountersDomain getAccountOfferCountersDomain(AccountOfferCounts accountOfferCounter,
			Integer couponQuantity, OfferCounters savedOfferCounter, String accountNumber, MemberOfferCounts savedMemberOfferCounter, Integer denomination) {
		
		AccountOfferCountersDomain offerCounterDomain = null;
		List<DenominationCountDomain> denominationCountDomainList = null;
		
		if(!ObjectUtils.isEmpty(accountOfferCounter)) {
			
			if(!StringUtils.isEmpty(accountOfferCounter.getId()) && !ObjectUtils.isEmpty(couponQuantity)) {
				
				denominationCountDomainList = !ObjectUtils.isEmpty(denomination)
						? getDenominationCountDomainList(accountOfferCounter.getDenominationCount(),
								denomination, couponQuantity)
						: new ArrayList<>(1);		
					
				offerCounterDomain = new AccountOfferCountersDomain
					.AccountOfferCountersBuilder(accountOfferCounter.getId())
					.offerId(accountOfferCounter.getOfferId())
					.accountNumber(accountOfferCounter.getAccountNumber())
					.membershipCode(accountOfferCounter.getMembershipCode())
					.dailyCount(accountOfferCounter.getDailyCount()+couponQuantity)
					.weeklyCount(accountOfferCounter.getWeeklyCount()+couponQuantity)
					.monthlyCount(accountOfferCounter.getMonthlyCount()+couponQuantity)
					.annualCount(accountOfferCounter.getAnnualCount()+couponQuantity)
					.totalCount(accountOfferCounter.getTotalCount()+couponQuantity)
					.denominationCount(denominationCountDomainList)
					.offerCounter(new OfferCountersDomain.OfferCounterBuilder(savedOfferCounter.getId()).build())
					.memberOfferCounter(new MemberOfferCountersDomain.MemberOfferCountersBuilder(savedMemberOfferCounter.getId()).build())
					.lastPurchased(new Date())
					.build();
			
			} else {
				
				denominationCountDomainList = getDenominationCountDomainList(accountOfferCounter.getDenominationCount(), denomination, couponQuantity);
				
				offerCounterDomain = new AccountOfferCountersDomain
						.AccountOfferCountersBuilder(accountOfferCounter.getAccountNumber(), savedOfferCounter.getOfferId(), 
										 accountOfferCounter.getDailyCount(), accountOfferCounter.getWeeklyCount(),
										 accountOfferCounter.getMonthlyCount(), accountOfferCounter.getAnnualCount(), 
										 accountOfferCounter.getTotalCount())
						                .membershipCode(savedMemberOfferCounter.getMembershipCode())
						                .id(accountOfferCounter.getId())
						                .denominationCount(denominationCountDomainList)
										.offerCounter(new OfferCountersDomain.OfferCounterBuilder(savedOfferCounter.getId()).build())
										.memberOfferCounter(new MemberOfferCountersDomain.MemberOfferCountersBuilder(savedMemberOfferCounter.getId()).build())
										.lastPurchased(new Date())
										.build();
			}
			
			
		} else {
			
			denominationCountDomainList = getDenominationCountDomainList(null, denomination, couponQuantity);
			
			offerCounterDomain = new AccountOfferCountersDomain
					.AccountOfferCountersBuilder(accountNumber, savedOfferCounter.getOfferId(), couponQuantity, couponQuantity,
									 couponQuantity, couponQuantity, couponQuantity)
					                .membershipCode(savedMemberOfferCounter.getMembershipCode())
									.denominationCount(denominationCountDomainList)
									.offerCounter(new OfferCountersDomain.OfferCounterBuilder(savedOfferCounter.getId()).build())
									.memberOfferCounter(new MemberOfferCountersDomain.MemberOfferCountersBuilder(savedMemberOfferCounter.getId()).build())
									.lastPurchased(new Date())
									.build();
		}
		
		return offerCounterDomain;
	}		

	/**
	 * 
	 * @param PaymentMethodsIds
	 * @param offerReference
	 * @return domain object for associated PaymentMethods for the offer
	 */
	private static List<PaymentMethodDomain> getPaymentDomain(List<String> paymentMethodsIds, OfferReferences offerReference) {
		
		List<PaymentMethodDomain> selectedPaymentMethods = null;
		
		if(!CollectionUtils.isEmpty(paymentMethodsIds)) {
		
			selectedPaymentMethods = new ArrayList<>(paymentMethodsIds.size());
			for (String code : paymentMethodsIds) {
				
				PaymentMethod paymethod = FilterValues.findAnyPaymentMethodInList(offerReference.getPaymentMethods(), Predicates.samePaymentMethodId(code));
				PaymentMethodDomain paymentMethod = new PaymentMethodDomain.PaymentMethodDomainBuilder(paymethod.getPaymentMethodId(), paymethod.getDescription()).build();
				selectedPaymentMethods.add(paymentMethod);
			}
			
		}
 		
		return selectedPaymentMethods;	
	}
}
