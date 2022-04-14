package com.loyalty.marketplace.offers.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import com.loyalty.marketplace.constants.MarketPlaceCode;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OfferErrorCodes;
import com.loyalty.marketplace.offers.constants.OffersConfigurationConstants;
import com.loyalty.marketplace.offers.constants.OffersListConstants;
import com.loyalty.marketplace.offers.decision.manager.outbound.dto.RuleResult;
import com.loyalty.marketplace.offers.helper.dto.AmountInfo;
import com.loyalty.marketplace.offers.helper.dto.BirthdayDurationInfoDto;
import com.loyalty.marketplace.offers.helper.dto.DenominationLimitCounter;
import com.loyalty.marketplace.offers.helper.dto.EligibilityInfo;
import com.loyalty.marketplace.offers.helper.dto.EligibleOfferHelperDto;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.helper.dto.LimitCounter;
import com.loyalty.marketplace.offers.helper.dto.LimitCounterWithDenominationList;
import com.loyalty.marketplace.offers.helper.dto.PurchaseCount;
import com.loyalty.marketplace.offers.inbound.dto.DenominationLimitDto;
import com.loyalty.marketplace.offers.inbound.dto.LimitDto;
import com.loyalty.marketplace.offers.inbound.dto.OfferCatalogDto;
import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
import com.loyalty.marketplace.offers.inbound.dto.SubOfferDto;
import com.loyalty.marketplace.offers.inbound.dto.TransactionRequestDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.ApiStatus;
import com.loyalty.marketplace.offers.member.management.outbound.dto.CatSubCat;
import com.loyalty.marketplace.offers.member.management.outbound.dto.CobrandedCardDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.member.management.outbound.dto.OfferBenefit;
import com.loyalty.marketplace.offers.member.management.outbound.dto.ParentChlidCustomer;
import com.loyalty.marketplace.offers.outbound.database.entity.AccountOfferCounts;
import com.loyalty.marketplace.offers.outbound.database.entity.ActivityCode;
import com.loyalty.marketplace.offers.outbound.database.entity.BirthdayGiftTracker;
import com.loyalty.marketplace.offers.outbound.database.entity.DenominationCount;
import com.loyalty.marketplace.offers.outbound.database.entity.EligibleOffers;
import com.loyalty.marketplace.offers.outbound.database.entity.FreeOffers;
import com.loyalty.marketplace.offers.outbound.database.entity.GiftInfo;
import com.loyalty.marketplace.offers.outbound.database.entity.ListValues;
import com.loyalty.marketplace.offers.outbound.database.entity.MemberRating;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCounter;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferRating;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchaseHistory;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchasePaymentMethod;
import com.loyalty.marketplace.offers.outbound.database.entity.SubOffer;
import com.loyalty.marketplace.offers.outbound.database.entity.Tags;
import com.loyalty.marketplace.offers.outbound.dto.DenominationDto;
import com.loyalty.marketplace.offers.outbound.dto.Eligibility;
import com.loyalty.marketplace.offers.outbound.dto.OfferCatalogResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.OfferCatalogResultResponseDto;
import com.loyalty.marketplace.offers.outbound.dto.PurchaseResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.RuleFailure;
import com.loyalty.marketplace.offers.stores.outbound.database.entity.Store;
import com.loyalty.marketplace.outbound.database.entity.Denomination;
import com.loyalty.marketplace.outbound.database.entity.DenominationValue;
import com.loyalty.marketplace.outbound.database.entity.PaymentMethod;
import com.loyalty.marketplace.outbound.dto.Errors;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.utils.Logs;
import com.loyalty.marketplace.utils.Utils;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherListResult;
import com.mongodb.client.result.UpdateResult;

/**
 * 
 * @author jaya.shukla
 *
 */
public class Checks{
	
	private static final Logger LOG = LoggerFactory.getLogger(Checks.class);
	
	Checks(){}
	
	/**
	 * 
	 * @param resultResponse
	 * @return if no errors in response object
	 */
	public static boolean checkNoErrors(ResultResponse resultResponse) {
		
		return (null != resultResponse && null != resultResponse.getApiStatus()) && CollectionUtils.isEmpty(resultResponse.getApiStatus().getErrors());
		
	}
	
	/**
	 * 
	 * @param resultResponse
	 * @return if errors present in response object
	 */
	public static boolean checkErrorsPresent(ResultResponse resultResponse) {
		
		return !CollectionUtils.isEmpty(resultResponse.getApiStatus().getErrors());
		
	}

	/**
	 * 
	 * @param item
	 * @return if it a cash voucher
	 */
	public static boolean checkIsCashVoucher(String item) {
		
		return Utilities.presentInList(
				Stream.of(OffersListConstants.CASH_OFFER_ID_LIST, 
						  Arrays.asList(OffersConfigurationConstants.cashVoucherItem))
	            .flatMap(Collection::stream)
	            .collect(Collectors.toList()), item);
	}
	
	/**
	 * 
	 * @param item
	 * @return if it a cash voucher
	 */
	public static boolean checkIsDiscountVoucher(String item) {
		
		return Utilities.presentInList(
				Stream.of(OffersListConstants.DISCOUNT_OFFER_ID_LIST, 
						  Arrays.asList(OffersConfigurationConstants.discountVoucherItem))
	            .flatMap(Collection::stream)
	            .collect(Collectors.toList()), item);
	}
	
	/**
	 * 
	 * @param item
	 * @return if it a deal voucher
	 */
    public static boolean checkIsDealVoucher(String item) {
		
    	return Utilities.presentInList(
				Stream.of(OffersListConstants.DEAL_OFFER_ID_LIST, 
						  Arrays.asList(OffersConfigurationConstants.dealVoucherItem))
	            .flatMap(Collection::stream)
	            .collect(Collectors.toList()), item);
	}
    
    /**
     * 
     * @param item
     * @return if it a gold certificate
     */
    public static boolean checkIsGoldCertificate(String item) {
    	
    	return Utilities.presentInList(
				Stream.of(OffersListConstants.GOLD_CERTIFICATE_ID_LIST, 
						  Arrays.asList(OffersConfigurationConstants.goldCertificateItem))
	            .flatMap(Collection::stream)
	            .collect(Collectors.toList()), item);
    }
    
    /**
     * 
     * @param item
     * @return if it a bill payment or recharge
     */
    public static boolean checkIsTelcomType(String item) {
    	
    	return Utilities.presentInList(
				Stream.of(OffersListConstants.TELECOM_OFFER_ID_LIST, 
						  Arrays.asList(OffersConfigurationConstants.billPaymentItem, OffersConfigurationConstants.rechargeItem))
	            .flatMap(Collection::stream)
	            .collect(Collectors.toList()), item);
    	
    }

    /**
     * 
     * @param item
     * @return if it is an etisalat add on offer
     */
    public static boolean checkIsEtisalatAddon(String item) {
	
    	return Utilities.presentInList(
				Stream.of(OffersListConstants.ETISALAT_BUNDLE_ID_LIST, 
						  Arrays.asList(OffersConfigurationConstants.addOnItem))
	            .flatMap(Collection::stream)
	            .collect(Collectors.toList()), item);
    }
    
    /**
     * 
     * @param item
     * @return if it is a welcome offer type
     */
    public static boolean checkIsWelcomeGiftOfferType(String item) {
    	
    	return Utilities.presentInList(
				OffersListConstants.WELCOME_GIFT_ID_LIST, item);
    }
    
    /**
     * 
     * @param item
     * @return if it is a lifestyle offer type
     */
    public static boolean checkIsLifestyleOfferType(String item) {
    	
    	return Utilities.presentInList(
				OffersListConstants.LIFESTYLE_OFFER_ID_LIST, item);
    }
    
    /**
     * 
     * @param item
     * @return if it is an other offer type
     */
    public static boolean checkIsOtherOfferType(String item) {
    	
    	return Utilities.presentInList(
				OffersListConstants.OTHER_OFFER_ID_LIST, item);
    }
    
    /**
     * 
     * @param item
     * @return if the provisioning channel is COMS
     */
    public static boolean checkIsComsProvisioningChannel(String item) {
		
		return StringUtils.equalsIgnoreCase(item, OffersConfigurationConstants.coms);
	}

    /**
     * 
     * @param item
     * @return if the provisioning channel is RTF
     */
    public static boolean checkIsRtfProvisioningChannel(String item) {
		
    	return StringUtils.equalsIgnoreCase(item, OffersConfigurationConstants.rtf);
	}

    /**
     * 
     * @param item
     * @return if the provisioning channel is EMCAIS
     */
	public static boolean checkIsEmcaisProvisioningChannel(String item) {
		
		return StringUtils.equalsIgnoreCase(item, OffersConfigurationConstants.emcais);
		
	}
	
	/**
	 * 
	 * @param item
	 * @return if the provisioning channel is PHONYTUNES
	 */
    public static boolean checkIsPhoneyTunesProvisioningChannel(String item) {
		
		return StringUtils.equalsIgnoreCase(item, OffersConfigurationConstants.phonyTunes);
	
	}
	
    /**
     * 
     * @param item
     * @return if the provisioning channel is RBT
     */
    public static boolean checkIsRbtProvisioningChannel(String item) {
		
		return StringUtils.equalsIgnoreCase(item, OffersConfigurationConstants.rbt);
	
	}
    
    /**
     * 
     * @param item
     * @return if the purchase item is Bill Payment
     */
    public static boolean checkIsBillPayment(String item) {
    	
    	return StringUtils.equalsIgnoreCase(item, OffersConfigurationConstants.billPaymentItem);
    	
    }

    /**
     * 
     * @param item
     * @return if the purchase item is Recharge
     */
    public static boolean checkIsRecharge(String item) {
	
    	return StringUtils.equalsIgnoreCase(item, OffersConfigurationConstants.rechargeItem);
    
    }
    
    /**
     * 
     * @param method
     * @return if the payment method is a point method
     */
    public static boolean checkPointsMethod(String method) {
    	
    	return Utilities.presentInList(OffersListConstants.POINTS_TYPES, method);
    }
    
    /**
     * 
     * @param method
     * @return if the payment method is a non-point payment method
     */
    public static boolean checkAmountMethod(String method) {
    	
    	return Utilities.presentInList(OffersListConstants.AMOUNT_TYPES, method);
    	
    }
    
    /**
     * 
     * @param method
     * @return if the payment method is a card method
     */
    public static boolean checkCardMethod(String method) {
    	
    	return Utilities.presentInList(OffersListConstants.CARD_TYPES, method);
    	
    }
    
    /**
     * 
     * @param method
     * @return if the payment method is full points method
     */
    public static boolean checkIsFullPointsPaymentMethod(String method) {
		
		return StringUtils.equalsIgnoreCase(method, OffersConfigurationConstants.fullPoints);
	}
    
    /**
     * 
     * @param method
     * @return if the payment method is partial points method
     */
    public static boolean checkIsPartialPointsPaymentMethod(String method) {
		
		return StringUtils.equalsIgnoreCase(method, OffersConfigurationConstants.partialCardPoints);
	}
    
    /**
     * 
     * @param item
     * @return if the purchase item is subscription
     */
    public static boolean checkIsSubscription(String item) {
    	
    	return StringUtils.equalsIgnoreCase(item, OffersConfigurationConstants.subscriptionItem);
    
    }
    
    /**
     * 
     * @param offerTypeId
     * @param provisioningChannel
     * @return if the offer is a video on demand offer
     */
    public static boolean checkIsVideoOnDemandOffer(String offerTypeId, String provisioningChannel) {
		
		return checkIsEtisalatAddon(offerTypeId) && checkIsPhoneyTunesProvisioningChannel(provisioningChannel);
	}
    
    /**
     * 
     * @param method
     * @return if the payment method is a credit card payment method
     */
    public static boolean checkIsCreditCardPaymentMethod(String method) {
		
		return StringUtils.equalsIgnoreCase(method, OffersConfigurationConstants.fullCreditCard);
	}
    
    /**
     * 
     * @param method
     * @return if the payment method is a deduct from balance payment method
     */
    public static boolean checkIsDeductFromBalancePaymentMethod(String method) {
		
		return StringUtils.equalsIgnoreCase(method, OffersConfigurationConstants.deductFromBalance);
	}

    /**
     * 
     * @param method
     * @return if the payment method is an add to bill payment method
     */
    public static boolean checkIsAddToBillPaymentMethod(String method) {
		
		return StringUtils.equalsIgnoreCase(method, OffersConfigurationConstants.addToBill);
	}
    
    /**
     * 
     * @param method
     * @return if the payment method is an apple pay payment method
     */
    public static boolean checkIsApplePayPaymentMethod(String method) {
    	
    	return StringUtils.equalsIgnoreCase(method, OffersConfigurationConstants.applePay);
    }
    
    /**
     * 
     * @param method
     * @return if the payment method is an samsung pay payment method
     */
    public static boolean checkIsSamsungPayPaymentMethod(String method) {
    	return StringUtils.equalsIgnoreCase(method, OffersConfigurationConstants.samsungPay);
    }
    /**
     * 
     * @param redeemType
     * @return status to indicate the voucher redeem type is default
     */
    public static boolean checkDefaultRedeemType(String redeemType) {
    	
    	return !StringUtils.isEmpty(redeemType)
    		&& StringUtils.equalsIgnoreCase(redeemType, OffersConfigurationConstants.DEFAULT_REDEEM_TYPE);
    }
    
    /**
     * 
     * @param redeemType
     * @return status to indicate the voucher redeem type is non pin
     */
    public static boolean checkNonPinRedeemType(String redeemType) {
    	
    	return !StringUtils.isEmpty(redeemType)
    		&& StringUtils.equalsIgnoreCase(redeemType, OffersConfigurationConstants.NON_PIN_REDEEM_TYPE);
     }
    
    /**
     * 
     * @param redeemType
     * @return status to indicate the voucher redeem type is online
     */
    public static boolean checkOnlineRedeemType(String redeemType) {
    	
    	return !StringUtils.isEmpty(redeemType)
    		&& StringUtils.equalsIgnoreCase(redeemType, OffersConfigurationConstants.ONLINE_REDEEM_TYPE);
     }
    
    /**
     * 
     * @param redeemType
     * @return status to indicate the voucher redeem type is partnerPin
     */
    public static boolean checkPartnerPinRedeemType(String redeemType) {
    	
    	return !StringUtils.isEmpty(redeemType)
    		&& StringUtils.equalsIgnoreCase(redeemType, OffersConfigurationConstants.PARTNERPIN_REDEEM_TYPE);
     }
    
    /**
     * 
     * @param offerRating
     * @param accountNumber
     * @param membershipCode
     * @return if comment exists for account
     */
    public static boolean checkCommentsForAccountExists(OfferRating offerRating, 
			String accountNumber, String membershipCode) {
		
		MemberRating ratingExists = offerRating.getMemberRatings().stream()
				.filter(a->a.getMembershipCode().equals(membershipCode) 
						&& a.getAccountNumber().equals(accountNumber))
				.findAny().orElse(null);
		
		return null != ratingExists;
		
	}
    
    /**
     * 
     * @param purchaseRequest
     * @param purchasePaymentMethod
     * @param resultResponse
     * @return if purchase request is valid
     */
    public static boolean checkPurchaseRequestValid(PurchaseRequestDto purchaseRequest, PurchasePaymentMethod purchasePaymentMethod, ResultResponse resultResponse) {
		
		return  Responses.setResponseAfterConditionCheck(Checks.checkValidPaymentItem(purchasePaymentMethod), 
			    OfferErrorCodes.PURCHASE_ITEM_INVALID, resultResponse)
			 && Responses.setResponseAfterConditionCheck(checkStringItemPresent(purchaseRequest.getSelectedPaymentItem(), purchaseRequest.getOfferId(), OffersListConstants.OFFER_ID_PURCHASE_LIST), 
			    OfferErrorCodes.OFFERID_CANNOT_BE_EMPTY, resultResponse)
			 && Responses.setResponseAfterConditionCheck(!Utilities.presentInList(OffersListConstants.SUB_OFFER_ID_PURCHASE_LIST, purchaseRequest.getSelectedPaymentItem()) || !StringUtils.isEmpty(purchaseRequest.getSubOfferId()), 
				OfferErrorCodes.SUB_OFFERID_CANNOT_BE_EMPTY, resultResponse)
			 && Responses.setResponseAfterConditionCheck(checkNumericalItemPresent(purchaseRequest.getSelectedPaymentItem(), purchaseRequest.getVoucherDenomination(), OffersListConstants.OFFER_DENOMINATION_PURCHASE_LIST), 
				OfferErrorCodes.DENOMINATION_CANNOT_BE_EMPTY, resultResponse)
			 && Responses.setResponseAfterConditionCheck(checkPaymentMethodPresent(purchasePaymentMethod.getPaymentMethods()), 
				OfferErrorCodes.PURCHASE_ITEM_PAYMENT_METHOD_NOT_SET, resultResponse)
			 && Responses.setResponseAfterConditionCheck(checkNumericalItemPresent(purchaseRequest.getSelectedPaymentItem(), purchaseRequest.getCouponQuantity(), OffersListConstants.OFFER_COUPON_QUANTITY_PURCHASE_LIST), 
				OfferErrorCodes.COUPON_QUANTITY_EMPTY, resultResponse)
		     && Responses.setResponseAfterConditionCheck(checkNumericalItemPresent(purchaseRequest.getSelectedPaymentItem(), purchaseRequest.getSpentPoints(), OffersListConstants.POINTS_TYPES), 
			    OfferErrorCodes.POINTS_TO_REDEEM_EMPTY, resultResponse)
		     && Responses.setResponseAfterConditionCheck(checkNumericalItemPresent(purchaseRequest.getSelectedPaymentItem(), purchaseRequest.getSpentAmount(), OffersListConstants.CARD_TYPES), 
				OfferErrorCodes.COST_EMPTY, resultResponse)
		     && Responses.setResponseAfterConditionCheck(checkCardValuesPresent(purchaseRequest),
		    	OfferErrorCodes.CARD_DETAILS_EMPTY, resultResponse);
		
	}
    
    /**
     * 
     * @param item
     * @return status to check if it is a valid purchase item for offers
     */
    public static boolean checkValidOfferItem(String item) {
    	
    	return StringUtils.isEmpty(item)
    		|| Utilities.presentInList(OffersListConstants.ELIGIBLE_OFFER_PURCHASE_ITEMS, item);
    }
    
    /**
     * 
     * @param purchasePaymentMethod
     * @return if payment item is valid
     */
    public static boolean checkValidPaymentItem(PurchasePaymentMethod purchasePaymentMethod) {
		
		return null!=purchasePaymentMethod;
	}
   
    /**
     * 
     * @param paymentMethods
     * @return if payment method list is non-empty 
     */
    public static boolean checkPaymentMethodPresent(Collection<?> paymentMethods) {
		
		return !CollectionUtils.isEmpty(paymentMethods); 
	}
   
    /**
     * 
     * @param purchaseRequest
     * @return if all card details are present
     */
    public static boolean checkCardValuesPresent(PurchaseRequestDto purchaseRequest) {
    	LOG.info("Inside checkCardValuesPresent method");
    	LOG.info("cardNumber : {}",purchaseRequest.getCardNumber());   	
    	LOG.info("cardType : {}",purchaseRequest.getCardType());
    	LOG.info("cardSubType : {}",purchaseRequest.getCardSubType());
    	LOG.info("cardToken : {}", purchaseRequest.getCardToken());
    	LOG.info("authorizationCode : {}",purchaseRequest.getAuthorizationCode());
    	LOG.info("cardTypes : {}",OffersListConstants.CARD_TYPES);
    	LOG.info("amountTypes : {}",OffersListConstants.AMOUNT_TYPES);
    	
	   	boolean cardType = Utilities.presentInList(OffersListConstants.CARD_TYPES, purchaseRequest.getSelectedOption());
	   	LOG.info("cardType value : {}", cardType);
	   	boolean valuesPresent = true;
	   	LOG.info("valuesPresent value before calling checkListItemsPresent method : {}", valuesPresent);
	   	if(cardType) {
	   		
	   	   valuesPresent = checkListItemsPresent(Arrays.asList(purchaseRequest.getCardNumber(), purchaseRequest.getCardType(),
			    		 purchaseRequest.getCardSubType(), purchaseRequest.getCardToken(), purchaseRequest.getAuthorizationCode()));
	   		
	   	}
	   	LOG.info("valuesPresent value after calling checkListItemsPresent method : {}", valuesPresent);
	   	LOG.info("{}",!cardType||valuesPresent);
	   	return !cardType || valuesPresent;
    }
   
    /**
     * 
     * @param valueList
     * @return if all list items are present
     */
    private static boolean checkListItemsPresent(List<String> valueList) {
		
    	boolean itemsPresent = true;
   	
		if(!CollectionUtils.isEmpty(valueList)) {
			
			for(String value : valueList) {
				
				if(StringUtils.isEmpty(value)) {
					
					itemsPresent = false;
					break;
				}
				
			}
			
		}
		
		return itemsPresent;
	}
   
    /**
     * 
     * @param item
     * @param list
     * @return if input offer type is present in input list
     */
    public static boolean checkOfferTypeInList(String item, List<String> list) {
		
		return Utilities.presentInList(list, item);
	}
   
    /**
     * 
     * @param item
     * @param value
     * @param list
     * @return if item is present in list
     */
    public static boolean checkStringItemPresent(String item, String value, List<String> list) {
		
		return !checkOfferTypeInList(item, list) || !StringUtils.isEmpty(value);
		
    }
   
    /**
     * 
     * @param item
     * @param value
     * @param list
     * @return if numerical item present in list
     */
    public static boolean checkNumericalItemPresent(String item, Object value, List<String> list) {
		
		return !checkOfferTypeInList(item, list) || !ObjectUtils.isEmpty(value);
		
    }
    
    /**
     * 
     * @param offerCounter
     * @param offer
     * @param couponQuantity
     * @param eligibilityInfo
     * @param denomination
     * @param resultResponse
     * @param customerSegmentNames
     * @return if download limit is left for the offer 
     */
    public static boolean checkDownloadLimitLeftForEligibleOffer(OfferCounter offerCounter, Integer couponQuantity,
			EligibleOfferHelperDto eligibilityInfo, Integer denomination, ResultResponse resultResponse, List<String> customerSegmentNames,
			OfferCatalogResultResponseDto offerCatalogDto) {
		
    	if(Checks.checkCinemaOffer(eligibilityInfo.getOffer().getRules())) {
	    	
	    	PurchaseCount purchaseCount = ProcessValues.getPurchaseCountForCinemaOffersInEligibleOffers(eligibilityInfo);
	    	Checks.checkCinemaOfferDownloadLimit(purchaseCount, eligibilityInfo.getMemberDetails().getCustomerSegment(), resultResponse);	
	    	
	    } else {
	    
	    	LimitCounter limit = ProcessValues.getOfferLimitForCurrentOffer(eligibilityInfo.getOffer().getOfferId(), eligibilityInfo.getOffer().getLimit(), denomination, !CollectionUtils.isEmpty(customerSegmentNames), customerSegmentNames);
			LimitCounter counter = ProcessValues.getCurrentOfferCounter(eligibilityInfo.getOffer().getOfferId(), offerCounter, denomination, couponQuantity, eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberDetails().getMembershipCode());
	    	Checks.checkDownloadLimitForOffer(counter, limit, resultResponse);
	    	ProcessValues.setQuantityLeftForEligibleOffer(offerCatalogDto, eligibilityInfo, limit, counter);
	    }
	    
	    return Checks.checkNoErrors(resultResponse);
	}
   
    /**
     * 
     * @param offerCounter
     * @param offer
     * @param couponQuantity
     * @param eligibilityInfo
     * @param denomination
     * @param resultResponse
     * @param customerSegmentNames
     * @return if download limit is left for the offer 
     */
    public static boolean checkDownloadLimitLeft(OfferCounter offerCounter, OfferCatalog offer, Integer couponQuantity,
			EligibilityInfo eligibilityInfo, Integer denomination, ResultResponse resultResponse, List<String> customerSegmentNames) {
		
	    if(Checks.checkCinemaOffer(offer.getRules())) {
	    	
	    	PurchaseCount purchaseCount = ProcessValues.getPurchaseCountForCinemaOffers(eligibilityInfo);
	    	Checks.checkCinemaOfferDownloadLimit(purchaseCount, eligibilityInfo.getMemberDetails().getCustomerSegment(), resultResponse);	
	    	
	    } else {
	    	
	    	LimitCounter limit = ProcessValues.getOfferLimitForCurrentOffer(offer.getOfferId(), offer.getLimit(), denomination, !CollectionUtils.isEmpty(customerSegmentNames), customerSegmentNames);
			LimitCounter counter = ProcessValues.getCurrentOfferCounter(offer.getOfferId(), offerCounter, denomination, couponQuantity, eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberDetails().getMembershipCode());
			ProcessValues.setLimitCounterToList(eligibilityInfo, counter, limit);
			Checks.checkDownloadLimitForOffer(counter, limit, resultResponse);
	    	
	    }
	    
	    return Checks.checkNoErrors(resultResponse);
	}
    
    /**
     * 
     * @param offerCounter
     * @param offer
     * @param couponQuantity
     * @param eligibilityInfo
     * @param denomination
     * @param resultResponse
     * @param customerSegmentNames
     * @return if download limit is left for the offer 
     */
    public static boolean checkAllDownloadLimitForOffer(EligibilityInfo eligibilityInfo, PurchaseRequestDto purchaseRequest, PurchaseCount purchaseCount , List<String> customerSegmentNames, ResultResponse resultResponse) {
		
	    if(Checks.checkCinemaOffer(eligibilityInfo.getOffer().getRules())) {
	    	
	    	if(ObjectUtils.isEmpty(purchaseCount)) {
	    		
	    		purchaseCount = ProcessValues.getValuesForPurchaseCount(eligibilityInfo.getOfferCountersList(), 
	    				eligibilityInfo.getMemberOfferCounterList(), eligibilityInfo.getAccountOfferCounterList());
	    		
	    	}
	    	
	    	Checks.checkCinemaOfferDownloadLimit(purchaseCount, eligibilityInfo.getMemberDetails().getCustomerSegment(), resultResponse);	
	    	
	    } else {
	    	
	    	LimitCounter limit = ProcessValues.getOfferLimitForCurrentOffer(eligibilityInfo.getOffer().getOfferId(), eligibilityInfo.getOffer().getLimit(), purchaseRequest.getVoucherDenomination(), !CollectionUtils.isEmpty(customerSegmentNames), customerSegmentNames);
	    	LimitCounter counter = ProcessValues.getOfferCounterForCurrentOffer(eligibilityInfo, purchaseRequest.getVoucherDenomination(), purchaseRequest.getCouponQuantity());
			ProcessValues.setLimitCounterToList(eligibilityInfo, counter, limit);
			Checks.checkDownloadLimitForOffer(counter, limit, resultResponse);
	    	
	    }
	    
	    return Checks.checkNoErrors(resultResponse);
	}

    
    /**
     * 
     * @param counter
     * @param limit
     * @param resultResponse
     * @return if download limit is left for the offer
     */
    public static boolean checkDownloadLimitForOffer(LimitCounter counter, LimitCounter limit, ResultResponse resultResponse) {

    	   List<OfferErrorCodes> offerErrorCodes = ProcessValues.getCounterCheckErrorCodes(OfferConstants.OFFER_COUNTER.get());
		   List<OfferErrorCodes> offerDenominationErrorCodes = ProcessValues.getCounterCheckErrorCodes(OfferConstants.OFFER_DENOMINATION.get());
		   List<OfferErrorCodes> accountOfferErrorCodes = ProcessValues.getCounterCheckErrorCodes(OfferConstants.ACCOUNT_OFFER_COUNTER.get());
		   List<OfferErrorCodes> accountDenominationErrorCodes = ProcessValues.getCounterCheckErrorCodes(OfferConstants.ACCOUNT_OFFER_DENOMINATION.get());
		   List<OfferErrorCodes> memberOfferErrorCodes = ProcessValues.getCounterCheckErrorCodes(OfferConstants.MEMBER_OFFER_COUNTER.get());
		   List<OfferErrorCodes> memberDenominationErrorCodes = ProcessValues.getCounterCheckErrorCodes(OfferConstants.MEMBER_OFFER_DENOMINATION.get());
		   
		   return ObjectUtils.isEmpty(limit)
			 || ObjectUtils.isEmpty(counter)
			 || (Responses.setResponseAfterConditionCheck(checkLimit(counter.getCouponQuantity(), limit.getCouponQuantity()), OfferErrorCodes.OFFER_COUPON_LIMIT_EXCEEDED ,resultResponse)
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getOfferDaily(), limit.getOfferDaily()), offerErrorCodes.get(0), resultResponse)
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getOfferWeekly(), limit.getOfferWeekly()), offerErrorCodes.get(1), resultResponse)
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getOfferMonthly(), limit.getOfferMonthly()), offerErrorCodes.get(2), resultResponse)
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getOfferAnnual(), limit.getOfferAnnual()), offerErrorCodes.get(3), resultResponse)
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getOfferTotal(), limit.getOfferTotal()), offerErrorCodes.get(4), resultResponse)
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getOfferDenominationDaily(), limit.getOfferDenominationDaily()), offerDenominationErrorCodes.get(0), resultResponse)
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getOfferDenominationWeekly(), limit.getOfferDenominationWeekly()), offerDenominationErrorCodes.get(1), resultResponse)
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getOfferDenominationMonthly(), limit.getOfferDenominationMonthly()), offerDenominationErrorCodes.get(2), resultResponse)
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getOfferDenominationAnnual(), limit.getOfferDenominationAnnual()), offerDenominationErrorCodes.get(3), resultResponse)
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getOfferDenominationTotal(), limit.getOfferDenominationTotal()), offerDenominationErrorCodes.get(4), resultResponse)
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getAccountOfferDaily(), limit.getAccountOfferDaily()), accountOfferErrorCodes.get(0), resultResponse)
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getAccountOfferWeekly(), limit.getAccountOfferWeekly()), accountOfferErrorCodes.get(1), resultResponse)
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getAccountOfferMonthly(), limit.getAccountOfferMonthly()), accountOfferErrorCodes.get(2), resultResponse)
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getAccountOfferAnnual(), limit.getAccountOfferAnnual()), accountOfferErrorCodes.get(3), resultResponse)
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getAccountOfferTotal(), limit.getAccountOfferTotal()), accountOfferErrorCodes.get(4), resultResponse)	 
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getAccountDenominationDaily(), limit.getAccountDenominationDaily()), accountDenominationErrorCodes.get(0), resultResponse)
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getAccountDenominationWeekly(), limit.getAccountDenominationWeekly()), accountDenominationErrorCodes.get(1), resultResponse)
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getAccountDenominationMonthly(), limit.getAccountDenominationMonthly()), accountDenominationErrorCodes.get(2), resultResponse)
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getAccountDenominationAnnual(), limit.getAccountDenominationAnnual()), accountDenominationErrorCodes.get(3), resultResponse)
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getAccountDenominationTotal(), limit.getAccountDenominationTotal()), accountDenominationErrorCodes.get(4), resultResponse)
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getMemberOfferDaily(), limit.getMemberOfferDaily()), memberOfferErrorCodes.get(0), resultResponse)
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getMemberOfferWeekly(), limit.getMemberOfferWeekly()), memberOfferErrorCodes.get(1), resultResponse)
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getMemberOfferMonthly(), limit.getMemberOfferMonthly()), memberOfferErrorCodes.get(2), resultResponse)
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getMemberOfferAnnual(), limit.getMemberOfferAnnual()), memberOfferErrorCodes.get(3), resultResponse)
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getMemberOfferTotal(), limit.getMemberOfferTotal()), memberOfferErrorCodes.get(4), resultResponse)		 
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getMemberDenominationDaily(), limit.getMemberDenominationDaily()), memberDenominationErrorCodes.get(0), resultResponse)
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getMemberDenominationWeekly(), limit.getMemberDenominationWeekly()), memberDenominationErrorCodes.get(1), resultResponse)
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getMemberDenominationMonthly(), limit.getMemberDenominationMonthly()), memberDenominationErrorCodes.get(2), resultResponse)
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getMemberDenominationAnnual(), limit.getMemberDenominationAnnual()), memberDenominationErrorCodes.get(3), resultResponse)
			 &&  Responses.setResponseAfterConditionCheck(checkLimit(counter.getMemberDenominationTotal(), limit.getMemberDenominationTotal()), memberDenominationErrorCodes.get(4), resultResponse)
		  );	
			
		}
    
    /**
     * 
     * @param counter
     * @param limit
     * @return status to indicate limit is not crossed
     */
    public static boolean checkLimit(Integer counter, Integer limit) {
		
		return ObjectUtils.isEmpty(limit) 
			|| Utilities.isNullOrZero(counter)
			|| counter<=limit;
		
    }
    
    /**
     * 
     * @param counter
     * @param limit
     * @return status to indicate limit is not crossed
     */
    public static boolean checkLimitForEligibility(Integer counter, Integer limit) {
		
		return ObjectUtils.isEmpty(limit) 
			|| Utilities.isNullOrZero(counter)
			|| counter<limit;
		
    }

    /**
     * 
     * @param eligibilityInfo
     * @param purchaseRequest
     * @param purchaseResultResponse
     * @return status to indicate offer details are valid
     */
    public static boolean checkOfferDetails(EligibilityInfo eligibilityInfo, PurchaseRequestDto purchaseRequest,
			ResultResponse purchaseResultResponse) {
		
		return checkActiveOfferDetails(eligibilityInfo.getOffer(), eligibilityInfo.getHeaders().getChannelId(), purchaseResultResponse, false)
		   &&  checkOtherOfferDetailsPresent(purchaseRequest, eligibilityInfo, purchaseResultResponse);
    }
    
    /**
     * 
     * @param offer
     * @param channelId
     * @param resultResponse
     * @param portalCheck 
     * @return status to indicate offer is active
     */
    public static boolean checkActiveOfferDetails(OfferCatalog offer, String channelId, ResultResponse resultResponse, boolean portalCheck){
		
		return  Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(offer), 
			    OfferErrorCodes.OFFER_NOT_AVAILABLE, resultResponse)
			 && Responses.setResponseAfterConditionCheck(checkStatus(offer.getStatus()), 
			    OfferErrorCodes.OFFER_INACTIVE, resultResponse)
			 && Responses.setResponseAfterConditionCheck(checkOfferExpiry(offer.getOfferDates().getOfferEndDate()), 
			    OfferErrorCodes.OFFER_EXPIRED, resultResponse)
			 && Responses.setResponseAfterConditionCheck(checkStatus(offer.getMerchant().getStatus()), 
				OfferErrorCodes.MERCHANT_INACTIVE, resultResponse)
			 && Responses.setResponseAfterConditionCheck(checkStoreLinked(offer.getOfferStores()), 
				OfferErrorCodes.STORE_INACTIVE, resultResponse)
			 && (!portalCheck
			 || Responses.setResponseAfterConditionCheck(checkAvailableInPortals(offer.getAvailableInPortals(), channelId), 
				OfferErrorCodes.NOT_AVAILABLE_IN_PORTAL, resultResponse));
		
	}
    
    /**
     * 
     * @param purchaseRequest
     * @param eligibilityInfo
     * @param resultResponse
     * @return status to indicate associated offer values are present
     */
    public static boolean checkOtherOfferDetailsPresent(PurchaseRequestDto purchaseRequest, EligibilityInfo eligibilityInfo, ResultResponse resultResponse) {
		
		return checkOfferType(eligibilityInfo.getOffer().getOfferType().getOfferTypeId(), purchaseRequest.getSelectedPaymentItem(), resultResponse)
			&& Responses.setResponseAfterConditionCheck(checkActivityCode(eligibilityInfo.getOffer().getActivityCode(), purchaseRequest.getSelectedOption(), purchaseRequest.getSelectedPaymentItem(), eligibilityInfo.getOffer().getEarnMultiplier()), 
					OfferErrorCodes.ACTIVITY_CODE_NOT_PRESENT, resultResponse)
			&& Responses.setResponseAfterConditionCheck(checkDenominationPresent(purchaseRequest.getSelectedPaymentItem(), purchaseRequest.getVoucherDenomination(), eligibilityInfo.getDenomination(), eligibilityInfo.getOffer()), 
					OfferErrorCodes.DENOMINATION_NOT_EXISTING_FOR_OFFER, resultResponse)
			&& Responses.setResponseAfterConditionCheck(checkSubOfferPresent(purchaseRequest.getSelectedPaymentItem(), eligibilityInfo.getSubOffer()), 
					OfferErrorCodes.SUB_OFFER_NOT_EXISTING_FOR_OFFER, resultResponse);
		
    }
   
    /**
     * 
     * @param offerType
     * @param purchaseItem
     * @param resultResponse
     * @return status to indicate the purchaseItem is of valid offerType
     */
    private static boolean checkOfferType(String offerType, String purchaseItem, ResultResponse resultResponse) {
		
   		return Responses.setResponseAfterConditionCheck(!checkIsCashVoucher(purchaseItem) || checkIsCashVoucher(offerType), 
			   OfferErrorCodes.INAVLID_CASH_VOUCHER_ID, resultResponse)
			&& Responses.setResponseAfterConditionCheck(!checkIsDiscountVoucher(purchaseItem) || checkIsDiscountVoucher(offerType), 
			   OfferErrorCodes.INAVLID_DISCOUNT_VOUCHER_ID, resultResponse)  
			&& Responses.setResponseAfterConditionCheck(!checkIsDealVoucher(purchaseItem) || checkIsDealVoucher(offerType), 
			   OfferErrorCodes.INAVLID_DEAL_VOUCHER_ID, resultResponse)
			&& Responses.setResponseAfterConditionCheck(!checkIsEtisalatAddon(purchaseItem) || checkIsEtisalatAddon(offerType), 
			   OfferErrorCodes.INAVLID_ETISALAT_ADDON_ID, resultResponse)
			&& Responses.setResponseAfterConditionCheck(!checkIsGoldCertificate(purchaseItem) || checkIsGoldCertificate(offerType), 
			   OfferErrorCodes.INAVLID_GOLD_CERTIFICATE_ID, resultResponse);
	 }
   
    /**
     * 
     * @param activityCode
     * @param selectedOption
     * @param selectedPaymentItem
     * @return status to indicate activity code is present and valid
     */
    private static boolean checkActivityCode(ActivityCode activityCode, String selectedOption,
			String selectedPaymentItem, Double earnMultiplier) {
		
		return !ObjectUtils.isEmpty(activityCode) 
		    && !ObjectUtils.isEmpty(activityCode.getRedemptionActivityCode())
			&& !ObjectUtils.isEmpty(activityCode.getRedemptionActivityCode().getActivityCode())
			&& (!checkIsCashVoucher(selectedPaymentItem)
			|| !checkPointsMethod(selectedOption)
			|| ObjectUtils.isEmpty(earnMultiplier) 
			|| earnMultiplier<=0.0
			|| (!ObjectUtils.isEmpty(activityCode.getAccrualActivityCode())
			&& !ObjectUtils.isEmpty(activityCode.getAccrualActivityCode().getActivityCode())
			));
    }	
    
    /**
     * 
     * @param status
     * @return status to check active status
     */
    public static boolean checkStatus(String status) {
		
		return StringUtils.equalsIgnoreCase(status, OfferConstants.ACTIVE_STATUS.get());
	}

    /**
     * 
     * @param portalList
     * @param channelId
     * @return  status to channelId is present in portal list
     */
	public static boolean checkAvailableInPortals(List<String> portalList, String channelId) {
		
		return Utilities.presentInList(portalList, channelId);
		
	}
	
	/**
	 * 
	 * @param offerStores
	 * @return  status to indicate an active store is linked to the offer
	 */
    public static boolean checkStoreLinked(List<Store> offerStores) {
		
		boolean linked = !CollectionUtils.isEmpty(offerStores);
		boolean active = false;
		
		if(linked)
		{
			for(Store store : offerStores)
			{
			  if(checkStatus(store.getStatus()))
			  {
				  active = true;
				  break;
			  } 
			  
			}
		
		} 
		
		return linked && active;
	}

    /**
     * 
     * @param endDate
     * @return status to indicate offer is not expired
     */
    public static boolean checkOfferExpiry(Date endDate){
		
		return ObjectUtils.isEmpty(endDate) || checkDateFirstLessThanSecond(new Date(), endDate);
	}
   
    /**
     * 
     * @param firstDate
     * @param secondDate
     * @return status to indicate first date is less than second date
     */
    public static boolean checkDateFirstLessThanSecond(Date firstDate, Date secondDate){
		
		return !ObjectUtils.isEmpty(firstDate)
			&& !ObjectUtils.isEmpty(secondDate)	
			&& Utilities.compareDates(firstDate,secondDate)<0 ;
		
    }
   
    /**
     * 
     * @param value
     * @return status to indicate first date is less than second date
     */
    public static boolean checkFlagSet(String value) {
		
		return !ObjectUtils.isEmpty(value) && StringUtils.equalsIgnoreCase(value, OfferConstants.FLAG_SET.get());
	}
   
    /**
     * 
     * @param purchaseItem
     * @param voucherDenomination
     * @param denomination
     * @param offer
     * @return status to indicate denomination is present for cash voucher/ gold certificate
     */
    public static boolean checkDenominationPresent(String purchaseItem, Integer voucherDenomination, Denomination denomination, OfferCatalog offer) {
		
	   	boolean checkStatus = true;
	   	
	   	if(!ObjectUtils.isEmpty(voucherDenomination)) {
	            
	   		checkStatus = !ObjectUtils.isEmpty(denomination);
	   		
	   		if(!checkStatus && checkIsCashVoucher(purchaseItem) && checkFlagSet(offer.getDynamicDenomination())) {
	       		
	   			checkStatus = checkIntegerBetweenInclusive(offer.getDynamicDenominationValue().getMaxDenomination(), offer.getDynamicDenominationValue().getMinDenomination(), 
	   					voucherDenomination) && !ObjectUtils.isEmpty(offer.getIncrementalValue());
	   			
	   			if(checkStatus) {
	   				
	   				Double difference = (double) (voucherDenomination - offer.getDynamicDenominationValue().getMinDenomination());
		   			Double incrementValue = difference/offer.getIncrementalValue();
		   			Double newValue = offer.getDynamicDenominationValue().getMinDenomination() + (incrementValue*offer.getIncrementalValue());
		   			checkStatus = Utilities.isEqual(Double.valueOf(voucherDenomination), newValue);
	   				
	   			}
	   			
	       	} 
	   		
	   	}
	   	
	   	return checkStatus;
    }
    
    /**
     * 
     * @param highValue
     * @param lowValue
     * @param value
     * @return status to indicate integer lies between the two input values(inclusive)
     */
    public static boolean checkIntegerBetweenInclusive(Integer highValue, Integer lowValue, Integer value) {
		
		return !ObjectUtils.isEmpty(highValue) 
			&& !ObjectUtils.isEmpty(lowValue) 
			&& !ObjectUtils.isEmpty(value) 
			&& value>=lowValue 
			&& value<=highValue;
	}
   
    /**
     *  
     * @param purchaseItem
     * @param subOffer
     * @return status to indicate suboffer is present for deal voucher
     */
   private static boolean checkSubOfferPresent(String purchaseItem, SubOffer subOffer) {
		
		return !checkIsDealVoucher(purchaseItem) || !ObjectUtils.isEmpty(subOffer);
   }
   
   /**
    * 
    * @param amountInfo
    * @return status to indicate if offer is free
    */
   public static boolean checkOfferFree(AmountInfo amountInfo) {
		
		return !ObjectUtils.isEmpty(amountInfo)
			&& Utilities.isEqual(amountInfo.getOfferCost(), OffersConfigurationConstants.ZERO_DOUBLE)
			&&(ObjectUtils.isEmpty(amountInfo.getOfferPoints())
			|| Utilities.isEqual(amountInfo.getOfferPoints(), OffersConfigurationConstants.ZERO_INTEGER));
	}
   
  /**
   * 
   * @param freeOffer
   * @param purchaseRequest
   * @param amountInfo
   * @param resultResponse
   * @return status to indicate that amount/ points paid is valid
   */
   public static boolean checkValidAmount(boolean freeOffer, PurchaseRequestDto purchaseRequest, AmountInfo amountInfo, ResultResponse resultResponse) {
	  
	    String log = Logs.logForVariable(OfferConstants.AMOUNT_INFO_VARIABLE.get(), amountInfo);
	    LOG.info(log);
	   
	    return (freeOffer && checkNoAmountPaid(purchaseRequest, resultResponse))  
			|| (!freeOffer
			&& (!checkAmountMethod(purchaseRequest.getSelectedOption()) || checkValidValue(purchaseRequest.getSelectedOption(), amountInfo.getPurchaseAmount(), amountInfo.getSpentAmount(), resultResponse))
			&& (!checkIsFullPointsPaymentMethod(purchaseRequest.getSelectedOption()) || checkValidValue(purchaseRequest.getSelectedOption(), amountInfo.getPurchaseAmount(), (double) amountInfo.getSpentPoints(), resultResponse))
			&& (!checkIsPartialPointsPaymentMethod(purchaseRequest.getSelectedOption()) || checkValidValue(purchaseRequest.getSelectedOption(), amountInfo.getPurchaseAmount(), amountInfo.getSpentAmount() + amountInfo.getConvertedSpentPointsToAmount(), resultResponse)));
		
   }
   
   /**
    * 
    * @param purchaseRequest
    * @param resultResponse
    * @return status to indicate no payment is made for free offer
    */
   private static boolean checkNoAmountPaid(PurchaseRequestDto purchaseRequest, ResultResponse resultResponse) {
	
	   boolean noAmountPaid = purchaseRequest.getSpentAmount().equals(OffersConfigurationConstants.ZERO_DOUBLE)
			   			   && purchaseRequest.getSpentPoints().equals(OffersConfigurationConstants.ZERO_INTEGER);
	   return Responses.setResponseAfterConditionCheck(noAmountPaid, OfferErrorCodes.NO_PAYMENT_NEEDED_FOR_FREE_OFFER, resultResponse);
   }

   /**
    * 
    * @param paymentMethod
    * @param purchaseAmount
    * @param spentAmount
    * @param resultResponse
    * @return status to indicate payment is not less/more than required amount
    */
   public static boolean checkValidValue(String paymentMethod, Double purchaseAmount, Double spentAmount, ResultResponse resultResponse) {
		
		return Responses.setResponseAfterConditionCheck(!Utilities.firstLessThanSecond(purchaseAmount, spentAmount), ProcessValues.getAmountCheckErrorMessage(paymentMethod, false), resultResponse)
			&& Responses.setResponseAfterConditionCheck(!Utilities.firstMoreThanSecond(purchaseAmount, spentAmount), ProcessValues.getAmountCheckErrorMessage(paymentMethod, true), resultResponse);
		
   }
   
   /**
    * 
    * @param purchaseItem
    * @return status to indicate that lifetime savings is required
    */
   public static boolean checkIsLifetimeSavingsRequired(String purchaseItem) {
	   
	   return Utilities.presentInList(OffersListConstants.LIFETIME_SAVINGS_PURCHASE_ITEMS, purchaseItem);
   }
   
   /**
    * 
    * @param voucherDenomination
    * @return status to indicate that denomination is present
    */
   public static boolean checkIsDenomination(Integer voucherDenomination) {
	   
	   return !ObjectUtils.isEmpty(voucherDenomination);
   }
   
   /**
    * 
    * @param originalValueList
    * @param currentValueList
    * @return status to indicate value is present in eligible list/ absent in exclusion list
    */
   public static boolean checkListValuesInList(ListValues originalValueList, List<String> currentValueList) {
		
   	     return ObjectUtils.isEmpty(originalValueList)
			|| (Utilities.matchAnyValuePresentInListIfPresent(originalValueList.getEligibleTypes(), currentValueList)
			&&  Utilities.matchNoValuePresentInListIfPresent(originalValueList.getExclusionTypes(), currentValueList));
	}
   
   /**
    * 
    * @param offerList
    * @return status to indicate customer segment check is required in DM
    */
   public static boolean checkIsCustomerSegmentCheckRequired(List<OfferCatalog> offerList) {
		
	    return !ObjectUtils.isEmpty(FilterValues.findAnyOfferWithinOfferList(offerList, Predicates.isCustomerSegmentPresent()))
			|| !ObjectUtils.isEmpty(FilterValues.findAnyOfferWithinOfferList(offerList, Predicates.isCustomerSegmentsInOfferLimits()));
   }
   
   /**
    * 
    * @param offerList
    * @return status to indicate customer segment check is required in DM
    */
   public static boolean checkIsCustomerSegmentCheckRequiredForEligibleOffers(List<EligibleOffers> offerList) {
		
	    return !ObjectUtils.isEmpty(FilterValues.findAnyEligibleOfferWithinOfferList(offerList, Predicates.isCustomerSegmentPresentInEligibleOffer()))
			|| !ObjectUtils.isEmpty(FilterValues.findAnyEligibleOfferWithinOfferList(offerList, Predicates.isCustomerSegmentsInEligibleOfferLimits()));
	}
   
   /**
    * 
    * @param offerList
    * @return status to indicate subscription check is required or not
    */
   public static boolean checkIsSubscriptionCheckRequiredForDiscountVoucher(List<OfferCatalog> offerList) {
		
	    return !ObjectUtils.isEmpty(FilterValues.findAnyOfferWithinOfferList(offerList, Predicates.isDiscountVoucher()));
   }
   
   /**
    * 
    * @param offerList
    * @return status to indicate subscription check is required or not
    */
   public static boolean checkIsSubscriptionCheckRequiredForEligibleDiscountVoucher(List<EligibleOffers> offerList) {
		
	    return !ObjectUtils.isEmpty(FilterValues.findAnyEligibleOfferWithinOfferList(offerList, Predicates.isEligibleOfferDiscountVoucher()));
   }
   
   /**
    * 
    * @param cobrandedCardDetails
    * @return status to indicate cobranded card is active for member
    */
   public static boolean checkActiveCobranded(List<CobrandedCardDto> cobrandedCardDetails) {
		
		boolean activeCoBranded = false; 
		
		if(!CollectionUtils.isEmpty(cobrandedCardDetails)) {
			
			CobrandedCardDto activeCoBrand = cobrandedCardDetails.stream().filter(c->StringUtils.equalsIgnoreCase(c.getStatus(), OfferConstants.COBRANDED_ACTIVE_STATUS.get())).findAny().orElse(null);
			activeCoBranded = !ObjectUtils.isEmpty(activeCoBrand);
		}
		
		return  activeCoBranded; 
	}
   
   /**
    * 
    * @param value
    * @return status to indicate the double value is greater than 0.0
    */
   public static boolean checkValidDoubleMoreThanZero(Double value) {
		
		return ObjectUtils.isEmpty(value)
			|| value>0.0;
	}
   
   /**
    * 
    * @param value
    * @return status to indicate the double value is greater than  or equal to 0.0
    */
   public static boolean checkValidDoubleMoreThanEqualToZero(Double value) {
		
		return ObjectUtils.isEmpty(value)
			|| value>=0.0;
	}
   
   /**
    * 
    * @param firstDate
    * @param secondDate
    * @return status to indicate first date is greater than the second date
    */
   public static boolean checkDateFirstGreaterThanSecond(Date firstDate, Date secondDate){
		
		return !ObjectUtils.isEmpty(firstDate)
			&& !ObjectUtils.isEmpty(secondDate)	
			&& Utilities.compareDates(firstDate,secondDate)>0 ;
		
   }
   
   /**
    * 
    * @param firstDate
    * @param secondDate
    * @returnstatus to indicate first date is equal to the second date
    */
   public static boolean checkDateFirstEqualToSecond(Date firstDate, Date secondDate){
		
		return !ObjectUtils.isEmpty(firstDate)
			&& !ObjectUtils.isEmpty(secondDate)			
			&& Utilities.compareDates(firstDate,secondDate)==0 ;
		
	}
   
   /**
    * 
    * @param transactionRequest
    * @return status to indicate membershipCode is present when accountNumber is present
    */
   public static boolean checkMembershipCodePresentWhenAccountNumberPresent(TransactionRequestDto transactionRequest) {
		
		return StringUtils.isEmpty(transactionRequest.getAccountNumber())
			|| !StringUtils.isEmpty(transactionRequest.getMembershipCode());
	}
   
   /**
    * 
    * @param limit
    * @return status to indicate denomination limit is configured
    */
   public static boolean checkDenominationLimitConfigured(List<LimitDto> limit) {
		
		boolean configured = false;
		
		if(!ObjectUtils.isEmpty(limit)) {
			
			for(LimitDto limitDto : limit) {
				
				configured = !CollectionUtils.isEmpty(limitDto.getDenominationLimit())
						  || !CollectionUtils.isEmpty(limitDto.getAccountDenominationLimit())
						  || !CollectionUtils.isEmpty(limitDto.getMemberDenominationLimit());
				
				if(configured) {
					break;
				}
				
			}
			
		}
		
		return configured;
	}
   
   /**
    * 
    * @param valueMatchList
    * @param valueList
    * @param errorMessage
    * @param resultResponse
    * @return status to indicate the list of integer values is valid
    */
   public static boolean checkValidIntegerListValues(List<Integer> valueMatchList, List<Integer> valueList,
			OfferErrorCodes errorMessage, ResultResponse resultResponse) {
		
		if(!CollectionUtils.isEmpty(valueMatchList) && !CollectionUtils.isEmpty(valueList)) {
			
			for(Integer value :  valueList) {
				
				Responses.setResponseWithMessageAfterConditionCheck(Utilities.presentInIntegerList(valueMatchList, value), errorMessage, String.valueOf(value), resultResponse);
				
			}
			
		}
		
		return checkNoErrors(resultResponse);
	}
   
   /**
    * 
    * @param value1
    * @param value2
    * @return status to indicate the boolean values are valid
    */
   public static boolean checkNotBooleanValue(boolean value1, boolean value2) {
		
		return !value1 || !value2;
		
	}
   
   /**
    * 
    * @param customerTypeDetails
    * @param customerType
    * @return status to indicate customer type is valid(present in eligible and not present in exclusion)
    */
   public static boolean checkValidCustomerType(List<ParentChlidCustomer> customerTypeDetails, 
			String customerType) {
	
		List<String> types = MapValues.mapCustomerTypeValues(customerTypeDetails);
		return Utilities.presentInList(types, customerType);
		
	}
   
   /**
    * Check download limit values for denomination limit
    * @param filteredDenominationValueList
    * @param configuredDenominationList
    * @param filteredLimitDenominations
    * @param resultResponse
    */
   public static void checkDenominationLimitValues(List<Integer> filteredDenominationValueList,
			List<Integer> configuredDenominationList, List<Integer> filteredLimitDenominations, ResultResponse resultResponse) {
		
		Checks.checkValidIntegerListValues(filteredDenominationValueList, filteredLimitDenominations, OfferErrorCodes.DENOMINATION_NOT_EXISTING, resultResponse);
		
		if(!CollectionUtils.isEmpty(configuredDenominationList)) {
			
			Checks.checkValidIntegerListValues(filteredLimitDenominations, configuredDenominationList, OfferErrorCodes.DENOMINATION_LIMIT_NOT_SET, resultResponse);
			Checks.checkValidIntegerListValues(configuredDenominationList, filteredLimitDenominations, OfferErrorCodes.DENOMINATION_NOT_CONFIGURED, resultResponse);

		}
		
	}
   
   /**
    * 
    * @param valueMatchList
    * @param valueList
    * @param errorMessage
    * @param resultResponse
    * @return status to indicate list values in input is valid(present in eligile list/ absent in exclusion list)
    */
   public static boolean checkValidListValues(List<String> valueMatchList, List<String> valueList,
			OfferErrorCodes errorMessage, ResultResponse resultResponse) {
		
		if(!CollectionUtils.isEmpty(valueMatchList) && !CollectionUtils.isEmpty(valueList)) {
			
			for(String value :  valueList) {
				
				Responses.setResponseWithMessageAfterConditionCheck(Utilities.presentInList(valueMatchList, value), errorMessage, value, resultResponse);
				
			}
			
		}
		
		return checkNoErrors(resultResponse);
	}
   
   /**
    * 
    * @param customerTypeDetails
    * @param customerType
    * @param customerTypeList
    * @return if any value in customerTypeList is a valid child of customerType
    */
   public static boolean checkValidChildInList(List<ParentChlidCustomer> customerTypeDetails, 
			String customerType, List<String> customerTypeList) {
		
		boolean isValidChild = false;
		
		ParentChlidCustomer customerTypeDetail = FilterValues
				.findAnyParentChildCustomerTupeInList(customerTypeDetails, Predicates.sameCustomerTpe(customerType));
		
		if(customerTypeDetail.getParent()==null) {
		
			return false;
			 
		}
		
		do {
			
			isValidChild = Utilities.presentInList(customerTypeList, customerType);
			
			customerType = customerTypeDetail.getParent();
			customerTypeDetail = FilterValues
					.findAnyParentChildCustomerTupeInList(customerTypeDetails, Predicates.sameCustomerTpe(customerType));
			
		 } while(!isValidChild && customerTypeDetail.getParent()!=null);
		
		return  Utilities.presentInList(customerTypeList, customerType);
		
	}
   
   /**
    * 
    * @param giftInfo
    * @param channelId
    * @param subCardType
    * @return if the offer is a welcome gift or not
    */
   public static boolean checkIsGiftOffer(GiftInfo giftInfo, String channelId, String subCardType) {
		
	   return StringUtils.equalsIgnoreCase(giftInfo.getIsGift(), OfferConstants.FLAG_SET.get())
		   && Utilities.presentInList(giftInfo.getGiftChannels(), channelId)
		   && (StringUtils.isEmpty(subCardType)
		   || Utilities.presentInList(giftInfo.getGiftSubCardTypes(), subCardType));
   }
   
   /**
    * 
    * @param giftInfo
    * @return if it is a welcome gift
    */
   public static boolean checkIsGiftValue(GiftInfo giftInfo) {
		
		return !ObjectUtils.isEmpty(giftInfo) 
			&& StringUtils.isNotEmpty(giftInfo.getIsGift()) 
			&& Checks.checkFlagSet(giftInfo.getIsGift());
	}
   
   /**
    * 
    * @param date
    * @param currentCalendar
    * @return if date is the current day
    */
   public static boolean checkIsDateInCurrentDay(Date date, Calendar currentCalendar) {
		  Calendar targetCalendar = Calendar.getInstance();
		  targetCalendar.setTime(date);
		  return currentCalendar.get(Calendar.DAY_OF_YEAR) == targetCalendar.get(Calendar.DAY_OF_YEAR) && 
				  currentCalendar.get(Calendar.YEAR) == targetCalendar.get(Calendar.YEAR);
	}
	
	/**
	 * 
	 * @param date
	 * @param currentCalendar
	 * @return if date is in current week
	 */
	public static boolean checkIsDateInCurrentWeek(Date date, Calendar currentCalendar) {
		  Calendar targetCalendar = Calendar.getInstance();
		  targetCalendar.setTime(date);
		  return currentCalendar.get(Calendar.WEEK_OF_YEAR) == targetCalendar.get(Calendar.WEEK_OF_YEAR) && 
				  currentCalendar.get(Calendar.YEAR) == targetCalendar.get(Calendar.YEAR);
	}
	
	/**
	 * 
	 * @param date
	 * @param currentCalendar
	 * @return if date is in current month
	 * 
	 */
	public static boolean checkIsDateInCurrentMonth(Date date, Calendar currentCalendar) {
		  Calendar targetCalendar = Calendar.getInstance();
		  targetCalendar.setTime(date);
		  return currentCalendar.get(Calendar.MONTH) == targetCalendar.get(Calendar.MONTH) && 
				  currentCalendar.get(Calendar.YEAR) == targetCalendar.get(Calendar.YEAR);
	}
	
	/**
	 * 
	 * @param date
	 * @param currentCalendar
	 * @return if date is in current year
	 * 
	 */
	public static boolean checkIsDateInCurrentYear(Date date, Calendar currentCalendar) {
		  Calendar targetCalendar = Calendar.getInstance();
		  targetCalendar.setTime(date);
		  return currentCalendar.get(Calendar.YEAR) == targetCalendar.get(Calendar.YEAR);
	}
	
	/**
	 *  
	 * @param highValue
	 * @param lowValue
	 * @param value
	 * @return if a given value falls in a specific range inclusive of the boundary values
	 */
    public static boolean checkDoubleBetweenInclusive(Double highValue, Double lowValue, Double value) {
		
		return !ObjectUtils.isEmpty(highValue) 
			&& !ObjectUtils.isEmpty(lowValue) 
			&& !ObjectUtils.isEmpty(value) 
			&& value>=lowValue 
			&& value<=highValue;
	}
    
    /**
     * 
     * @param highValue
     * @param lowValue
     * @param value
     * @return if a given value falls in a specific range exclusive of the boundary values
     */
    public static boolean checkDoubleBetweenExclusive(Double highValue, Double lowValue, Double value) {
		
		return !ObjectUtils.isEmpty(highValue) 
			&& !ObjectUtils.isEmpty(lowValue) 
			&& !ObjectUtils.isEmpty(value) 
			&& value>lowValue 
			&& value<highValue;
	}
    
    /**
     * Checks if the action is Insert
     * @param action
     * @return check status
     */
    public static boolean checkIsActionInsert(String action) {
		
		return StringUtils.equalsIgnoreCase(action, OfferConstants.INSERT_ACTION.get());
	}
    
    /**
     * Checks if the action is Update
     * @param action
     * @return check status
     */
    public static boolean checkIsActionUpdate(String action) {
		
		return StringUtils.equalsIgnoreCase(action, OfferConstants.UPDATE_ACTION.get());
	}

    /**
     * Checks if the payment method is fullPoints
     * @param action
     * @return check status
     */
	public static boolean checkPaymentMethodFullPoints(String method) {
		
		return StringUtils.equalsIgnoreCase(method, OffersConfigurationConstants.fullPoints);
	}
	
	/**
	 * 
	 * @param method
	 * @return if payment method is partial credit card and points
	 */
    public static boolean checkPartialPaymentPaymentMethod(String method) {
		
		return StringUtils.equalsIgnoreCase(method, OffersConfigurationConstants.partialCardPoints);
	}
    
    /**
     * Checks if a given date falls in a specific range inclusive of the boundary dates
     * @param highValue
     * @param lowValue
     * @param currentValue
     * @return check status
     */
    public static boolean checkDateInRangeInclusive(Date highValue, Date lowValue, Date currentValue){
		
		return !ObjectUtils.isEmpty(highValue)
			&& !ObjectUtils.isEmpty(lowValue)
			&& !ObjectUtils.isEmpty(currentValue)
			&& Utilities.compareDates(lowValue,currentValue)<=0
			&& Utilities.compareDates(highValue,currentValue)>=0;
		
	}

    /**
     * Checks if a given date falls in a specific range exclusive of the boundary dates
     * @param highValue
     * @param lowValue
     * @param currentValue
     * @return check status
     */
    public static boolean checkDateInRangeExclusive(Date highValue, Date lowValue, Date currentValue){
		
		return !ObjectUtils.isEmpty(highValue)
			&& !ObjectUtils.isEmpty(lowValue)
			&& !ObjectUtils.isEmpty(currentValue)
			&& Utilities.compareDates(lowValue,currentValue)<0
			&& Utilities.compareDates(highValue,currentValue)>0;
		
	}

    /**
     * Checks if necessary conditions associated wih member for purchasing
     * @param purchaseRequest
     * @param memberDetails
     * @param purchasePaymentMethod
     * @param offerPaymentList 
     * @param offerDetails
     * @param resultResponse
     * @return check status
     */
    public static boolean checkMemberDetails(PurchaseRequestDto purchaseRequest, GetMemberResponse memberDetails, PurchasePaymentMethod purchasePaymentMethod,
			List<PaymentMethod> offerPaymentList, ResultResponse resultResponse) {
		
	 return Checks.checkNoErrors(resultResponse)
		&& Responses.setResponseAfterConditionCheck(checkMembershipCode(purchaseRequest.getMembershipCode(), memberDetails.getMembershipCode()), 
				OfferErrorCodes.MEMBERSHIPCODE_DOES_NOT_MATCH, resultResponse)
		&& Responses.setResponseAfterConditionCheck(memberDetails.isAgeEligibleFlag(), 
				OfferErrorCodes.NOT_IN_AGE_LIMIT, resultResponse)
		&& Responses.setResponseAfterConditionCheck(checkPaymentMethodPresent(memberDetails.getEligiblePaymentMethod()), 
				OfferErrorCodes.CUSTOMER_PAYMENT_METHOD_NOT_SET, resultResponse)
		&& checkEligiblePaymentMethods(MapValues.mapEligiblePaymentMethods(ProcessValues.getPaymentMethodsForPurchases(purchasePaymentMethod, offerPaymentList, true), Predicates.presentInMemberPaymentMethod(memberDetails.getEligiblePaymentMethod())), 
				purchaseRequest.getSelectedOption(), resultResponse);
    }
    
    /**
     * Checks if payment method is a part of eligible payment methods for purchase item and member
     * @param eligiblePaymentMethods
     * @param paymentMethod
     * @param resultResponse
     * @return check status
     */
    private static boolean checkEligiblePaymentMethods(List<String> eligiblePaymentMethods, String paymentMethod, ResultResponse resultResponse) {
		
    	LOG.info("Eligible Payment Methods for Purchase : {}", eligiblePaymentMethods);
    	LOG.info("paymentMethod for Purchase : {}", paymentMethod);
    	
    	return Responses.setResponseAfterConditionCheck(checkPaymentMethodPresent(eligiblePaymentMethods), 
				OfferErrorCodes.NO_PAYMENT_METHODS_TO_DISPLAY, resultResponse)
    		&& Responses.setResponseAfterConditionCheck(CollectionUtils.containsAny(eligiblePaymentMethods, Arrays.asList(paymentMethod)), 
    				OfferErrorCodes.PAYMENT_METHOD_NOT_ELIGIBLE, resultResponse);
	}
    
    /**
     * 
     * @param offer
     * @param memberDetails
     * @param resultResponse
     * @return if it is an eligible customer type
     */
	public static boolean checkCustomerTypeEligibility(OfferCatalog offer, GetMemberResponse memberDetails, 
			ResultResponse resultResponse) {
		
		return Responses.setResponseAfterConditionCheck(checkEligibleCustomerType(offer.getCustomerTypes(), memberDetails.getCustomerType()), 
				   OfferErrorCodes.CUSTOMER_TYPE_INELIGIBLE, resultResponse);
		
	}
    
	/**
	 * 
	 * @param customerTypeList
	 * @param customerType
	 * @return if the customer type is present in eligible list and not present in the exclusion list
	 */
    public static boolean checkEligibleCustomerType(ListValues customerTypeList,
			List<String> customerType) {
		
		return !CollectionUtils.isEmpty(customerType) 
			&& !ObjectUtils.isEmpty(customerTypeList)	
			&& !CollectionUtils.isEmpty(customerTypeList.getEligibleTypes()) 
			&& checkInEligibleList(customerTypeList.getEligibleTypes(), customerType)
			&& (CollectionUtils.isEmpty(customerTypeList.getExclusionTypes())
			|| checkNotInExclusionList(customerTypeList.getExclusionTypes(), customerType));
		
	}
    
    /**
     * 
     * @param eligibleList
     * @param customerType
     * @return if customer type is present in the eligible list
     */
    public static boolean checkInEligibleList(List<String> eligibleList, List<String> customerType) {
		
		boolean isEligible = false;
		
		for(String type : customerType) {
			
			isEligible = Utilities.presentInList(eligibleList, type);
			
			if(isEligible) {
				break;
			}
			
		}
		
		return isEligible;
		
	}
	
	/**
	 * 
	 * @param eligibleList
	 * @param customerType
	 * @return value to indicate customer type is not in exclusion list
	 * 
	 */
	public static boolean checkNotInExclusionList(List<String> exclusionList, List<String> customerType) {
		
		boolean isNotExcluded = false;
		
		for(String type : customerType) {
			
			isNotExcluded = !Utilities.presentInList(exclusionList, type);
			
			if(isNotExcluded) {
				break;
			}
			
		}
		
		return isNotExcluded;
		
	}
    
	/**
	 * Checks whether membership code is correct when passed in request
	 * @param requestMembershipCode
	 * @param actualMembershipCode
	 * @return check status
	 */
    private static boolean checkMembershipCode(String requestMembershipCode, String actualMembershipCode) {
		
		return StringUtils.isEmpty(requestMembershipCode) 
			|| StringUtils.equals(requestMembershipCode, actualMembershipCode);
	}

    /**
     * Checks whether user is in valid customer segment or not
     * @param ruleResult
     * @param customerSegments
     * @param customerSegment
     * @param resultResponse
     * @return check status
     */
	public static boolean checkCustomerSegment(RuleResult ruleResult, ListValues customerSegments, List<String> customerSegment, ResultResponse resultResponse) {
		
		if(!ObjectUtils.isEmpty(ruleResult)){
			
			if(!ruleResult.isEligibility()) {
				 
				Responses.addErrorWithMessage(resultResponse, OfferErrorCodes.MEMBER_NOT_ELIGIBLE_ON_DAY, ruleResult.getReason());
			}
			 
			if(Checks.checkNoErrors(resultResponse)) {
				
				Responses.setResponseAfterConditionCheck(checkListValuesInList(customerSegments, customerSegment), OfferErrorCodes.NOT_IN_ELIGIBLE_CUSTOMER_SEGMENT, resultResponse);
				
			}
			
		}
		
		return Checks.checkNoErrors(resultResponse);
	}
    
	/**
	 * Checks whether the amount/points paid by user are sufficient or not
	 * @param purchaseRequest
	 * @param eligibilityInfo
	 * @param purchaseResultResponse
	 * @return check status
	 */
	public static boolean checkValidAmountPaid(PurchaseRequestDto purchaseRequest, EligibilityInfo eligibilityInfo,
			ResultResponse purchaseResultResponse) {
		
		return checkIsBillPayment(purchaseRequest.getSelectedPaymentItem()) 
		|| checkIsRecharge(purchaseRequest.getSelectedPaymentItem())
		|| checkValidAmount(eligibilityInfo.getAdditionalDetails().isFree(), purchaseRequest, eligibilityInfo.getAmountInfo(), purchaseResultResponse);
		
		
	}
	
	/**
	 * 
	 * @param valueList
	 * @param errorResult
	 * @param resultResponse
	 * @return if any empty value is oresent in the list
	 */
	public static boolean checkEmptyValuePresent(List<String> valueList, OfferErrorCodes errorResult, ResultResponse resultResponse) {
		
		boolean emptyValueNotPresent =true;
		
		if(CollectionUtils.isNotEmpty(valueList)) {
			
			for(String value : valueList) {
				
				emptyValueNotPresent = Utils.removeSpecialChars(value).length()>0
						&& !StringUtils.isEmpty(value);
				
				if(!emptyValueNotPresent) {
					
					break;
				}
				
			}
			
		}		
		
		return Responses.setResponseAfterConditionCheck(emptyValueNotPresent, errorResult, resultResponse);
		
	}
	
	/**
	 * 
	 * @param value
	 * @param errorResult
	 * @param resultResponse
	 * @return if the flag value is in eligible values for flag list
	 */
	public static boolean checkFlagValueValid(String value, OfferErrorCodes errorResult, ResultResponse resultResponse) {
	
		return Responses.setResponseAfterConditionCheck(StringUtils.isEmpty(value) ||
				Utilities.presentInList(OffersListConstants.ELIGIBLE_FLAG_VALUES, value),
				errorResult, resultResponse);
		
	}
	
	public static boolean checkGroupFlagValueValid(String value, OfferErrorCodes errorResult, ResultResponse resultResponse) {
		
		return Responses.setResponseAfterConditionCheck(StringUtils.isEmpty(value) ||
				Utilities.presentInList(OffersListConstants.ELIGIBLE_GROUP_FLAG_VALUES, value),
				errorResult, resultResponse);
		
	}

	/**
	 * 
	 * @param valueList
	 * @param currentList
	 * @param errorResult
	 * @param resultResponse
	 * @return if any invalid value is present in the list
	 */
	public static boolean checkInvalidValuePresentInList(List<String> valueList, List<String> currentList,
			OfferErrorCodes errorResult, ResultResponse resultResponse) {
		
	   if(CollectionUtils.isNotEmpty(valueList) && CollectionUtils.isNotEmpty(currentList)) {
		   
		   for(String value : currentList) {
				
				Responses.setResponseWithMessageAfterConditionCheck(Utilities.presentInList(valueList, value), errorResult, value, resultResponse);
				
				if(checkErrorsPresent(resultResponse)) {
					
					break;
				}	
				
			}
		   
	   }
		
	   return checkNoErrors(resultResponse); 
	}
	
	/**
	 * 
	 * @param value1
	 * @param value2
	 * @return status that first integer is less than second integer
	 */
    public static boolean checkFirstIntegerLessThanSecond(Integer value1, Integer value2) {
		
		return !ObjectUtils.isEmpty(value1) 
			&& !ObjectUtils.isEmpty(value2) 
			&& value1<value2;
	}
    
    /**
     * 
     * @param value1
     * @param value2
     * @return status that first integer is less than equal to second integer
     */
    public static boolean checkFirstIntegerLessThanEqualToSecond(Integer value1, Integer value2) {
		
		return !ObjectUtils.isEmpty(value1) 
			&& !ObjectUtils.isEmpty(value2) 
			&& value1<=value2;
	}

    /**
     * 
     * @param firstValue
     * @param secondValue
     * @return if both values are present/absent together
     */
	public static boolean checkOnePresentWhenOtherPresent(String firstValue, String secondValue) {
		
		return StringUtils.isEmpty(firstValue)
			|| !StringUtils.isEmpty(secondValue);
	}

	/**
	 * 
	 * @param page
	 * @param pageLimit
	 * @return if both page/pageLimit are present/absent together
	 */
	public static boolean checkPageNumberAndPageLimitCombination(Integer page, Integer pageLimit) {
		
		return (!ObjectUtils.isEmpty(page) 
			 && !ObjectUtils.isEmpty(pageLimit))
			|| (ObjectUtils.isEmpty(page) && ObjectUtils.isEmpty(pageLimit));
	}
	
	/**
	 * 
	 * @param latitude
	 * @param longitude
	 * @return if both latitude/longitude are present/absent together
	 */
	public static boolean checkLatitudeAndLongitudeCombination(String latitude, String longitude) {
		
		return (!StringUtils.isEmpty(latitude) && !StringUtils.isEmpty(longitude))
			|| (StringUtils.isEmpty(latitude) && StringUtils.isEmpty(longitude));
	}

	/**
	 * 
	 * @param denominations
	 * @param offerTypeId
	 * @return if denomination is present for required offer type
	 */
	public static boolean checkDenominationsPresentForRequiredType(List<DenominationDto> denominations,
			String offerTypeId) {
		
		return !Utilities.presentInList(OffersListConstants.REQUIRED_DENOMINATIONS_TYPES, offerTypeId)
			|| CollectionUtils.isNotEmpty(denominations);
	}

	/**
	 * 
	 * @param limit
	 * @param counter
	 * @return if download limit is left for the denomination
	 */
	public static boolean checkDenominationDownloadLimitLeft(DenominationLimitCounter limit,
			DenominationLimitCounter counter) {
		
		return ObjectUtils.isEmpty(limit)
			|| ObjectUtils.isEmpty(counter)
			|| (checkLimit(counter.getOfferDaily(), limit.getOfferDaily())
			&& checkLimit(counter.getOfferWeekly(), limit.getOfferWeekly())
			&& checkLimit(counter.getOfferMonthly(), limit.getOfferMonthly())
			&& checkLimit(counter.getOfferAnnual(), limit.getOfferAnnual())
			&& checkLimit(counter.getOfferTotal(), limit.getOfferTotal())
			&& checkLimit(counter.getAccountDaily(), limit.getAccountDaily())
			&& checkLimit(counter.getAccountWeekly(), limit.getAccountWeekly())
			&& checkLimit(counter.getAccountMonthly(), limit.getAccountMonthly())
			&& checkLimit(counter.getAccountTotal(), limit.getAccountTotal())
			&& checkLimit(counter.getAccountAnnual(), limit.getAccountAnnual())
			&& checkLimit(counter.getMemberDaily(), limit.getMemberDaily())
			&& checkLimit(counter.getMemberWeekly(), limit.getMemberWeekly())
			&& checkLimit(counter.getMemberMonthly(), limit.getMemberMonthly())
			&& checkLimit(counter.getMemberAnnual(), limit.getMemberAnnual())
			&& checkLimit(counter.getMemberTotal(), limit.getMemberTotal()));
	}

	/**
	 * Check if eligible offers are filtered or not
	 * @param offerCatalogList
	 * @param offerCatalogResultResponse
	 */
	public static void checkEligibleOffersFiltered(List<OfferCatalogResultResponseDto> offerCatalogList,
			OfferCatalogResultResponse offerCatalogResultResponse) {
		
		if(Checks.checkNoErrors(offerCatalogResultResponse)) {
			  
			  Responses.setResponseAfterConditionCheck(!CollectionUtils.isEmpty(offerCatalogList), OfferErrorCodes.NO_OFFERS_FOR_MEMBER_TO_DISPLAY, offerCatalogResultResponse);
		 }
		
	}

	/**
	 * 
	 * @param resultResponse
	 * @param apiStatus
	 * @return if the response is valid without any errors
	 */
	public static boolean checkValidResponseWithNoErrors(ResultResponse resultResponse, ApiStatus apiStatus) {
			
		return Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(apiStatus), OfferErrorCodes.MEMBER_MANAGEMENT_RESPONSE_NOT_RECEIVED, resultResponse)
			&& Responses.addServiceCallErrors(resultResponse, apiStatus)
			&& apiStatus.getStatusCode() == 0;
	}

	/**
	 * 
	 * @param apiStatus
	 * @param resultResponse
	 * @return if partner activity does not exist
	 */
	public static boolean checkPartnerActivityNotExisting(ApiStatus apiStatus, ResultResponse resultResponse) {
		
		if(StringUtils.equalsIgnoreCase(apiStatus.getMessage(), OfferConstants.PARTNER_EXISTS_MESSAGE.get())) {
			
			resultResponse.addErrorAPIResponse(apiStatus.getStatusCode(), apiStatus.getMessage());
			
		}
		
		return Checks.checkNoErrors(resultResponse);
	}

	/**
	 * 
	 * @param offerCatalogDto
	 * @return if condition is met for purchase quantity left for member
	 */
	public static boolean checkConditionForQuantityLeft(OfferCatalogResultResponseDto offerCatalogDto) {
		
		return !ObjectUtils.isEmpty(offerCatalogDto)
			&& !ObjectUtils.isEmpty(offerCatalogDto.getEligibility())
			&& offerCatalogDto.getEligibility().isStatus()
			&& !CollectionUtils.isEmpty(offerCatalogDto.getOfferLimit());
	}

	/**
	 * 
	 * @param birthdayDurationInfoDto
	 * @param birthdayGiftTracker
	 * @param resultResponse
	 * @param purchaseRecords
	 * @param membershipCode 
	 * @param accountNumber 
	 * @return status of birthday eligibility check
	 */
	public static boolean checkBirthdayEligibility(BirthdayDurationInfoDto birthdayDurationInfoDto, BirthdayGiftTracker birthdayGiftTracker, ResultResponse resultResponse, List<PurchaseHistory> purchaseRecords, String accountNumber, String membershipCode) {
		   
		return !ObjectUtils.isEmpty(birthdayDurationInfoDto)
			&& checkInBirthdayPeriod(birthdayDurationInfoDto, resultResponse)
			&& checkBirthdayGiftAvailed(birthdayDurationInfoDto, birthdayGiftTracker, resultResponse)
			&& checkBirthdayPurchaseLimitCrossed(purchaseRecords, birthdayDurationInfoDto, accountNumber, membershipCode, resultResponse);  
	}
	
	/**
	 * 
	 * @param birthdayDurationInfoDto
	 * @param birthdayGiftTracker
	 * @param resultResponse
	 * @param purchaseRecords
	 * @param accountNumber
	 * @param membershipCode
	 * @return status of birthday eligibility check for sending birthday gift alert
 	 */
	public static boolean checkBirthdayEligibilityForAlerts(BirthdayDurationInfoDto birthdayDurationInfoDto, BirthdayGiftTracker birthdayGiftTracker, ResultResponse resultResponse, List<PurchaseHistory> purchaseRecords, String accountNumber, String membershipCode) {
		   
		return !ObjectUtils.isEmpty(birthdayDurationInfoDto)
			&& checkInBirthdayPeriodForAlerts(birthdayDurationInfoDto, resultResponse)
			&& checkBirthdayGiftAvailed(birthdayDurationInfoDto, birthdayGiftTracker, resultResponse)
			&& checkBirthdayPurchaseLimitCrossed(purchaseRecords, birthdayDurationInfoDto, accountNumber, membershipCode, resultResponse);  
	}
	
	/**
	 * 
	 * @param birthdayDurationInfoDto
	 * @param resultResponse
	 * @return status whether today's date is in configured birthday period for member
	 */
	public static boolean checkInBirthdayPeriod(BirthdayDurationInfoDto birthdayDurationInfoDto, ResultResponse resultResponse) {
		
		return Responses.setResponseAfterConditionCheck(Checks.checkDateInRangeInclusive(birthdayDurationInfoDto.getEndDate(), birthdayDurationInfoDto.getStartDate(), new Date()), OfferErrorCodes.BIRTHDAY_CRITERIA_CHECKED_FAILED, resultResponse);
		
	}
	
	/**
	 * 
	 * @param birthdayDurationInfoDto
	 * @param resultResponse
	 * @return status whether today's date is in configured birthday period for member
	 */
	public static boolean checkInBirthdayPeriodForAlerts(BirthdayDurationInfoDto birthdayDurationInfoDto, ResultResponse resultResponse) {
		
		return Responses.setResponseAfterConditionCheck(Checks.checkDateInRangeInclusive(birthdayDurationInfoDto.getEndDate(), birthdayDurationInfoDto.getStartDate(), birthdayDurationInfoDto.getDob()), OfferErrorCodes.BIRTHDAY_CRITERIA_CHECKED_FAILED, resultResponse);
		
	}
	
	/**
	 * Checks whether the birthday gift has already been viewed
	 * @param dob
	 * @param startDate
	 * @param birthdayGiftTracker
	 * @param resultResponse
	 */
	public static boolean checkBirthdayGiftAvailed(BirthdayDurationInfoDto birthdayDurationInfoDto, BirthdayGiftTracker birthdayGiftTracker,
			ResultResponse resultResponse) {
		
		if(Checks.checkNoErrors(resultResponse)
		&& !ObjectUtils.isEmpty(birthdayGiftTracker) 
		&& !ObjectUtils.isEmpty(birthdayGiftTracker.getLastViewedDate())) {
			
			Responses.setResponseAfterConditionCheck(!Checks.checkDateInRangeInclusive(birthdayDurationInfoDto.getStartDate(), birthdayDurationInfoDto.getLastYearDob(), birthdayGiftTracker.getLastViewedDate()), OfferErrorCodes.BIRTHDAY_GIFT_AVAILED, resultResponse); 	
					
		}
		
		return Checks.checkNoErrors(resultResponse);
	}

	/**
	 * Checks the limit for purchase of birthday offers has not been increased
	 * @param purchaseRecords
	 * @param membershipCode 
	 * @param accountNumber 
	 * @param birthdayInfo
	 * @param endDate 
	 * @param startDate 
	 * @param resultResponse
	 */
	public static boolean checkBirthdayPurchaseLimitCrossed(List<PurchaseHistory> purchaseRecords, BirthdayDurationInfoDto birthdayDurationInfoDto, String accountNumber, String membershipCode, ResultResponse resultResponse) {
		
		if(Checks.checkNoErrors(resultResponse)
		&& !ObjectUtils.isEmpty(birthdayDurationInfoDto)
		&& !ObjectUtils.isEmpty(birthdayDurationInfoDto.getPurchaseLimit())
		&& !CollectionUtils.isEmpty(purchaseRecords)) {
			
			List<PurchaseHistory> birthdayRecords = FilterValues.filterPurchaseRecords(purchaseRecords, Predicates.isBirthdayOfferPurchase(birthdayDurationInfoDto.getStartDate(), birthdayDurationInfoDto.getEndDate(), accountNumber, membershipCode));
			int size = !CollectionUtils.isEmpty(birthdayRecords) 
					? birthdayRecords.size() 
					: OffersConfigurationConstants.ZERO_INTEGER;
					
			Responses.setResponseAfterConditionCheck(size<birthdayDurationInfoDto.getPurchaseLimit(), OfferErrorCodes.BIRTHDAY_OFFER_PURCHASE_LIMIT_CROSSED, resultResponse); 	
			
		}
		
		return Checks.checkNoErrors(resultResponse);
	}

	/**
	 * 
	 * @param priceOrder
	 * @return status whether price order in eligible offer request is valid or not
	 */
	public static boolean checkValidPriceOrder(Integer priceOrder) {
		
		return ObjectUtils.isEmpty(priceOrder) || Utilities.presentInIntegerList(Arrays.asList(1, -1), priceOrder);
	}

	/**
	 * 
	 * @param offerTypePreference
	 * @return status whether offerTypePreference in eligible offer request is valid or not
	 */
	public static boolean checkValidOfferTypePreference(String offerTypePreference) {
		
		return StringUtils.isEmpty(offerTypePreference) || Utilities.presentInList(OffersListConstants.OFFER_TYPE_LIST, offerTypePreference);
	}
	
    /**
     * 
     * @param page
     * @param pageLimit
     * @param resultResponse
     * @return status after checking page number and page limit are valid
     */
	public static boolean checkPageNumberAndPageLimitValid(Integer page, Integer pageLimit,
			ResultResponse resultResponse) {
		
		return (ObjectUtils.isEmpty(page) && ObjectUtils.isEmpty(pageLimit))
			|| (Responses.setResponseAfterConditionCheck(checkFirstIntegerLessThanEqualToSecond(0, page), OfferErrorCodes.INVALID_PAGE_NUMBER, resultResponse)
		    && Responses.setResponseAfterConditionCheck(checkFirstIntegerLessThanEqualToSecond(1, pageLimit), OfferErrorCodes.INVALID_PAGE_LIMIT, resultResponse));
	}
	
	/**
	 * 
	 * @param rules
	 * @return status of whether the offer is a cinema offer or not
	 */
	public static boolean checkCinemaOffer(List<String> rules) {
		
		return !CollectionUtils.isEmpty(rules)
			&& CollectionUtils.containsAny(rules, Arrays.asList(OffersConfigurationConstants.cinemaRule));
		
	}

	/**
	 * Checks download limit for cinema offers
	 * @param weeklyPurchaseCount
	 * @param customerSegment
	 * @param resultResponse
	 */
	public static void checkCinemaOfferDownloadLimit(PurchaseCount purchaseCount, List<String> customerSegmentList,
			ResultResponse resultResponse) {
		
		if(Utilities.presentInList(customerSegmentList, OffersConfigurationConstants.standardCustomerSegment)) {
			
			Responses.setResponseAfterConditionCheck(checkFirstIntegerLessThanSecond(purchaseCount.getGlobalWeekly(), OffersConfigurationConstants.standardGlobalLimit), OfferErrorCodes.STANDARD_GLOBAL_LIMIT_EXCEEDED, resultResponse);
			Responses.setResponseAfterConditionCheck(checkFirstIntegerLessThanSecond(purchaseCount.getMemberWeekly(), OffersConfigurationConstants.standardMemberLimit), OfferErrorCodes.STANDARD_MEMBER_LIMIT_EXCEEDED, resultResponse);
			Responses.setResponseAfterConditionCheck(checkFirstIntegerLessThanSecond(purchaseCount.getAccountWeekly(), OffersConfigurationConstants.standardAccountLimit), OfferErrorCodes.STANDARD_ACCOUNT_LIMIT_EXCEEDED, resultResponse);
			
		} else if(Utilities.presentInList(customerSegmentList, OffersConfigurationConstants.subscriberCustomerSegment)) {
			
			Responses.setResponseAfterConditionCheck(checkFirstIntegerLessThanSecond(purchaseCount.getGlobalWeekly(), OffersConfigurationConstants.subscriberGlobalLimit), OfferErrorCodes.SUBSCRIBER_GLOBAL_LIMIT_EXCEEDED, resultResponse);
			Responses.setResponseAfterConditionCheck(checkFirstIntegerLessThanSecond(purchaseCount.getMemberWeekly(), OffersConfigurationConstants.subscriberMemberLimit), OfferErrorCodes.SUBSCRIBER_MEMBER_LIMIT_EXCEEDED, resultResponse);
			Responses.setResponseAfterConditionCheck(checkFirstIntegerLessThanSecond(purchaseCount.getAccountWeekly(), OffersConfigurationConstants.subscriberAccountLimit), OfferErrorCodes.SUBSCRIBER_ACCOUNT_LIMIT_EXCEEDED, resultResponse);
		
		} else if(Utilities.presentInList(customerSegmentList, OffersConfigurationConstants.specialCustomerSegment)) {
			
			Responses.setResponseAfterConditionCheck(checkFirstIntegerLessThanSecond(purchaseCount.getGlobalWeekly(), OffersConfigurationConstants.specialGlobalLimit), OfferErrorCodes.SPECIAL_GLOBAL_LIMIT_EXCEEDED, resultResponse);
			Responses.setResponseAfterConditionCheck(checkFirstIntegerLessThanSecond(purchaseCount.getMemberWeekly(), OffersConfigurationConstants.specialMemberLimit), OfferErrorCodes.SPECIAL_MEMBER_LIMIT_EXCEEDED, resultResponse);
			Responses.setResponseAfterConditionCheck(checkFirstIntegerLessThanSecond(purchaseCount.getAccountWeekly(), OffersConfigurationConstants.specialAccountLimit), OfferErrorCodes.SPECIAL_ACCOUNT_LIMIT_EXCEEDED, resultResponse);
		}
			
	}

	/**
	 * 
	 * @param status
	 * @return status to indicate input status is valid
	 */
	public static boolean checkValidOfferStatus(String status) {
		
		return StringUtils.isEmpty(status)
			|| Utilities.presentInList(OffersListConstants.OFFER_STATUS_LIST, status);
	}

	/**
	 * 
	 * @param offerTypeId
	 * @return status to indicate it is valid offer type to generate voucher
	 */
	public static boolean checkVoucherActionOfferType(String offerTypeId) {
		
		return CollectionUtils.containsAny(OffersListConstants.VOUCHER_ACTION_TYPES, Arrays.asList(offerTypeId));
	}

	/**
	 * 
	 * @param existingOffer
	 * @return check if accrual code exists for the offer
	 */
	public static boolean checkAccrualCodeExists(OfferCatalog existingOffer) {
		
		return !ObjectUtils.isEmpty(existingOffer)
			&& !ObjectUtils.isEmpty(existingOffer.getActivityCode())
			&& !ObjectUtils.isEmpty(existingOffer.getActivityCode().getAccrualActivityCode());
	}
	
	/**
	 * 
	 * @param storeCodes
	 * @param allStoreCodes
	 * @return  status to indicate all values in list are same
	 */
	public static boolean checkNoDifferentValues(List<String> originalList, List<String> currentList) {
		
		return (CollectionUtils.isEmpty(currentList) 
				&& CollectionUtils.isEmpty(originalList))
				|| (!CollectionUtils.isEmpty(currentList) 
			    && !CollectionUtils.isEmpty(originalList)
			    && originalList.containsAll(currentList));
		
	}
	

	/**
	 * 
	 * @param areas
	 * @param tags
	 * @return  status to indicate if area is present in tags
	 */
	public static boolean checkAreaPresentInTags(List<String> values, Tags tags) {
		
		for(String value : values) {
			
			Pattern pattern = Pattern.compile(OfferConstants.STAR_REGEX.get()+value+OfferConstants.STAR_REGEX.get());
	        Matcher matcherEn = pattern.matcher(tags.getTagsEn());
	        Matcher matcherAr = pattern.matcher(tags.getTagsAr());
	        if (matcherEn.find() || matcherAr.find()){
	            
	        	return true;
	        }
	        
		}
		
		return false;
		
	}

	/**
	 * 
	 * @param eligibilityInfo
	 * @param failureStatus
	 * @param eligibility
	 * @param offer
	 * @param resultResponse
	 * @return failure status after customer segment check for cinema offer
	 */
	public static List<RuleFailure> checkCustomerSegmentForCinemaOffers(RuleResult ruleResult,
			GetMemberResponse memberDetails, List<RuleFailure> failureStatus, 
			Eligibility eligibility, ListValues customerSegments,
			List<String> rules, ResultResponse resultResponse) {
		
		if(!ObjectUtils.isEmpty(customerSegments)) {
			
			if(!ruleResult.isEligibility()) {
			       
				failureStatus = !CollectionUtils.isEmpty(eligibility.getFailureStatus())
						      ? eligibility.getFailureStatus()
						      : new ArrayList<>(1);
				
				failureStatus.add(new RuleFailure(OfferConstants.DAY_ELIGIBILITY.get(), ruleResult.getReason()));
				
			} else if(Checks.checkCinemaOffer(rules)){
				
				Responses.setResponseAfterConditionCheck(Checks.checkListValuesInList(customerSegments, memberDetails.getCustomerSegment()), OfferErrorCodes.NOT_IN_ELIGIBLE_CUSTOMER_SEGMENT, resultResponse);
				failureStatus = Responses.setErrorMessage(OfferConstants.CUSTOMER_SEGMENT_ELIGIBILITY_CHECK.get(), eligibility, failureStatus, resultResponse);
			}
			
		}
		
		return failureStatus;
	}

	/**
	 * 
	 * @param subOfferId
	 * @param subOfferList
	 * @return status to indicate subOfferId is unique in the list
	 */
	public static boolean checkUniqueSubOfferId(String subOfferId, List<SubOfferDto> subOfferList) {
		
		boolean status = true;
		
		if(!CollectionUtils.isEmpty(subOfferList) && !StringUtils.isEmpty(subOfferId)) {
			
			List<String> idList = MapValues.mapSubOfferIdList(subOfferList, Predicates.sameInputSubOfferId(subOfferId)); 
			status = CollectionUtils.isEmpty(idList) || idList.size()==1;
			
		}
		
		return status;
	}

	/**
	 * 
	 * @param resultResponse
	 * @return status to indicate that it is a success response
	 */
	public static boolean checkSuccessResponse(ResultResponse resultResponse) {
		
		return !ObjectUtils.isEmpty(resultResponse) 
			&& !ObjectUtils.isEmpty(resultResponse.getApiStatus())
			&& !ObjectUtils.isEmpty(resultResponse.getApiStatus().getStatusCode())
			&& resultResponse.getApiStatus().getStatusCode().equals(MarketPlaceCode.STATUS_SUCCESS.getIntId());
	}
	
	/**
	 * 
	 * @param voucherDetails
	 * @return status to indicate that rollback has happened for transaction
	 */
	public static boolean checkRollBackStatusForTransaction(List<VoucherListResult> voucherDetails) {
		
		return !CollectionUtils.isEmpty(FilterValues.filterVoucherDetailsList(voucherDetails, Predicates.isVoucherCancelled()));
	}
	
	/**
	 * 
	 * @param limitDto
	 * @return status to indicate limitDto is not empty
	 */
	public static boolean checkNotEmptyLimit(LimitDto limitDto) {
		
		return !ObjectUtils.isEmpty(limitDto)
			&& (!ObjectUtils.isEmpty(limitDto.getCouponQuantity())
			||	!ObjectUtils.isEmpty(limitDto.getDailyLimit())
			||	!ObjectUtils.isEmpty(limitDto.getWeeklyLimit())
			||	!ObjectUtils.isEmpty(limitDto.getMonthlyLimit())
			||	!ObjectUtils.isEmpty(limitDto.getAnnualLimit())
			||	!ObjectUtils.isEmpty(limitDto.getDownloadLimit())
			||  !CollectionUtils.isEmpty(limitDto.getDenominationLimit())
			||	!ObjectUtils.isEmpty(limitDto.getAccountDailyLimit())
			||	!ObjectUtils.isEmpty(limitDto.getAccountWeeklyLimit())
			||	!ObjectUtils.isEmpty(limitDto.getAccountMonthlyLimit())
			||	!ObjectUtils.isEmpty(limitDto.getAccountAnnualLimit())
			||	!ObjectUtils.isEmpty(limitDto.getAccountTotalLimit())
			||  !CollectionUtils.isEmpty(limitDto.getAccountDenominationLimit())
			||	!ObjectUtils.isEmpty(limitDto.getMemberDailyLimit())
			||	!ObjectUtils.isEmpty(limitDto.getMemberWeeklyLimit())
			||	!ObjectUtils.isEmpty(limitDto.getMemberMonthlyLimit())
			||	!ObjectUtils.isEmpty(limitDto.getMemberAnnualLimit())
			||	!ObjectUtils.isEmpty(limitDto.getMemberTotalLimit())
			||  !CollectionUtils.isEmpty(limitDto.getMemberDenominationLimit()));
	}

	/**
	 * 
	 * @param denominationLimitDto
	 * @return  status to indicate denominationLimitDto is not empty
	 */
	public static boolean checkNotEmptyDenominationLimit(DenominationLimitDto denominationLimitDto) {
		
		return !ObjectUtils.isEmpty(denominationLimitDto)
		&& (!ObjectUtils.isEmpty(denominationLimitDto.getDailyLimit())
		||	!ObjectUtils.isEmpty(denominationLimitDto.getWeeklyLimit())
		||	!ObjectUtils.isEmpty(denominationLimitDto.getMonthlyLimit())
		||	!ObjectUtils.isEmpty(denominationLimitDto.getAnnualLimit())
		||	!ObjectUtils.isEmpty(denominationLimitDto.getTotalLimit()));
	}
	
	/**
	 * 
	 * @param offerCatalogRequest
	 * @param existingOffer
	 * @return status to indicate customer types are same in existing offer and update request 
	 */
	public static boolean checkSameCustomerTypesInRequest(OfferCatalogDto offerCatalogRequest,
			OfferCatalog existingOffer) {
		
		return !ObjectUtils.isEmpty(offerCatalogRequest)
			&& !ObjectUtils.isEmpty(offerCatalogRequest.getCustomerTypes())
			&& !ObjectUtils.isEmpty(existingOffer)
			&& !ObjectUtils.isEmpty(existingOffer.getCustomerTypes())
			&& checkNoDifferentValues(existingOffer.getCustomerTypes().getEligibleTypes(), offerCatalogRequest.getCustomerTypes().getEligibleTypes())
			&& checkNoDifferentValues(existingOffer.getCustomerTypes().getExclusionTypes(), offerCatalogRequest.getCustomerTypes().getExclusionTypes());
	}
	
	/**
	 * 
	 * @param offerCatalogRequest
	 * @param existingOffer
	 * @return status to indicate customer types are same in existing offer and update request 
	 */
	public static boolean checkExactlySameEligibleCustomerTypesInRequest(OfferCatalogDto offerCatalogRequest,
			OfferCatalog existingOffer) {
		
		return !ObjectUtils.isEmpty(offerCatalogRequest)
			&& !ObjectUtils.isEmpty(offerCatalogRequest.getCustomerTypes())
			&& !ObjectUtils.isEmpty(existingOffer)
			&& !ObjectUtils.isEmpty(existingOffer.getCustomerTypes())
			&& checkNoDifferentValues(existingOffer.getCustomerTypes().getEligibleTypes(), offerCatalogRequest.getCustomerTypes().getEligibleTypes())
			&& checkNoDifferentValues(offerCatalogRequest.getCustomerTypes().getEligibleTypes(), existingOffer.getCustomerTypes().getEligibleTypes());
	}

	/**
	 * 
	 * @param item
	 * @return status to indicate it is valid offer type for creation
	 */
	public static boolean checkValidOfferTypeForCreation(String item) {
		
		return Utilities.presentInList(OffersListConstants.CREATE_OFFER_TYPE_LIST, item);
	}
	
	/**
	 * 
	 * @param voucherRedeemType
	 * @return status to indicate voucher redeem type is valid
	 */
	public static boolean checkValidVoucherRedeemType(String voucherRedeemType) {
		
		return !StringUtils.isEmpty(voucherRedeemType)
			&& CollectionUtils.containsAny(OffersListConstants.VOUCHER_REDEEM_TYPES, Arrays.asList(voucherRedeemType));
	}

	/**
	 * Checks all request fields are valid for default redeem type attributes
	 * @param offerCatalogRequest
	 * @param resultResponse
	 */
	public static void checkValidDefaultRedeemTypeAttributes(OfferCatalogDto offerCatalogRequest,
			ResultResponse resultResponse) {
		
		if(checkDefaultRedeemType(offerCatalogRequest.getVoucherRedeemType())) {
			
			Responses.setResponseAfterConditionCheck(StringUtils.isEmpty(offerCatalogRequest.getPartnerRedeemURL()), OfferErrorCodes.NON_EMPTY_PARTNER_REDEEM_URL, resultResponse);
			Responses.setResponseAfterConditionCheck(StringUtils.isEmpty(offerCatalogRequest.getInstructionsToRedeemTitleEn()), OfferErrorCodes.NON_EMPTY_TITLE_REDEEM_INSTRUCTION_EN, resultResponse);
			Responses.setResponseAfterConditionCheck(StringUtils.isEmpty(offerCatalogRequest.getInstructionsToRedeemTitleAr()), OfferErrorCodes.NON_EMPTY_TITLE_REDEEM_INSTRUCTION_AR, resultResponse);
			Responses.setResponseAfterConditionCheck(StringUtils.isEmpty(offerCatalogRequest.getInstructionsToRedeemEn()), OfferErrorCodes.NON_EMPTY_REDEEM_INSTRUCTION_EN, resultResponse);
			Responses.setResponseAfterConditionCheck(StringUtils.isEmpty(offerCatalogRequest.getInstructionsToRedeemAr()), OfferErrorCodes.NON_EMPTY_REDEEM_INSTRUCTION_AR, resultResponse);
			
		}
		
	}

	/**
	 * Checks all request fields are valid for non pin redeem type attributes
	 * @param offerCatalogRequest
	 * @param resultResponse
	 */
	public static void checkValidNonPinRedeemTypeAttributes(OfferCatalogDto offerCatalogRequest,
			ResultResponse resultResponse) {
		
		if(checkNonPinRedeemType(offerCatalogRequest.getVoucherRedeemType())) {
			
			Responses.setResponseAfterConditionCheck(!StringUtils.isEmpty(offerCatalogRequest.getInstructionsToRedeemTitleEn()), OfferErrorCodes.EMPTY_TITLE_REDEEM_INSTRUCTION_EN, resultResponse);
			Responses.setResponseAfterConditionCheck(!StringUtils.isEmpty(offerCatalogRequest.getInstructionsToRedeemTitleAr()), OfferErrorCodes.EMPTY_TITLE_REDEEM_INSTRUCTION_AR, resultResponse);
			Responses.setResponseAfterConditionCheck(!StringUtils.isEmpty(offerCatalogRequest.getInstructionsToRedeemEn()), OfferErrorCodes.EMPTY_REDEEM_INSTRUCTION_EN, resultResponse);
			Responses.setResponseAfterConditionCheck(!StringUtils.isEmpty(offerCatalogRequest.getInstructionsToRedeemAr()), OfferErrorCodes.EMPTY_REDEEM_INSTRUCTION_AR, resultResponse);
			
		}
		
	}

	/**
	 * Checks all request fields are valid for online redeem type attributes 
	 * @param offerCatalogRequest
	 * @param resultResponse
	 */
	public static void checkValidOnlineRedeemTypeAttributes(OfferCatalogDto offerCatalogRequest,
			ResultResponse resultResponse) {
		
		if(checkOnlineRedeemType(offerCatalogRequest.getVoucherRedeemType())) {
			
			Responses.setResponseAfterConditionCheck(!StringUtils.isEmpty(offerCatalogRequest.getPartnerRedeemURL()), OfferErrorCodes.EMPTY_PARTNER_REDEEM_URL, resultResponse);
			Responses.setResponseAfterConditionCheck(!StringUtils.isEmpty(offerCatalogRequest.getInstructionsToRedeemTitleEn()), OfferErrorCodes.EMPTY_TITLE_REDEEM_INSTRUCTION_EN, resultResponse);
			Responses.setResponseAfterConditionCheck(!StringUtils.isEmpty(offerCatalogRequest.getInstructionsToRedeemTitleAr()), OfferErrorCodes.EMPTY_TITLE_REDEEM_INSTRUCTION_AR, resultResponse);
			Responses.setResponseAfterConditionCheck(!StringUtils.isEmpty(offerCatalogRequest.getInstructionsToRedeemEn()), OfferErrorCodes.EMPTY_REDEEM_INSTRUCTION_EN, resultResponse);
			Responses.setResponseAfterConditionCheck(!StringUtils.isEmpty(offerCatalogRequest.getInstructionsToRedeemAr()), OfferErrorCodes.EMPTY_REDEEM_INSTRUCTION_AR, resultResponse);
		
		}
	}
	
	/**
	 * Checks all request fields are valid for partnerPin redeem type attributes 
	 * @param offerCatalogRequest
	 * @param resultResponse
	 */
	public static void checkValidPartnerPinRedeemTypeAttributes(OfferCatalogDto offerCatalogRequest,
			ResultResponse resultResponse) {
		
		if(checkPartnerPinRedeemType(offerCatalogRequest.getVoucherRedeemType())) {
			
			Responses.setResponseAfterConditionCheck(!StringUtils.isEmpty(offerCatalogRequest.getPartnerRedeemURL()), OfferErrorCodes.EMPTY_PARTNER_REDEEM_URL, resultResponse);
			Responses.setResponseAfterConditionCheck(!StringUtils.isEmpty(offerCatalogRequest.getInstructionsToRedeemTitleEn()), OfferErrorCodes.EMPTY_TITLE_REDEEM_INSTRUCTION_EN, resultResponse);
			Responses.setResponseAfterConditionCheck(!StringUtils.isEmpty(offerCatalogRequest.getInstructionsToRedeemTitleAr()), OfferErrorCodes.EMPTY_TITLE_REDEEM_INSTRUCTION_AR, resultResponse);
			Responses.setResponseAfterConditionCheck(!StringUtils.isEmpty(offerCatalogRequest.getInstructionsToRedeemEn()), OfferErrorCodes.EMPTY_REDEEM_INSTRUCTION_EN, resultResponse);
			Responses.setResponseAfterConditionCheck(!StringUtils.isEmpty(offerCatalogRequest.getInstructionsToRedeemAr()), OfferErrorCodes.EMPTY_REDEEM_INSTRUCTION_AR, resultResponse);
		
		}
	}

	/**
	 * 
	 * @param subscribed
	 * @param offerCatalogDto 
	 * @return status to indicate offer is free
	 */
	public static boolean checkOfferIsFree(OfferCatalogResultResponseDto offerCatalogDto,
			GetMemberResponse memberDetails, boolean checkSubscription) {
		
		return checkSubscriptionBenefitsApplicable(offerCatalogDto, memberDetails, checkSubscription)
			|| (!ObjectUtils.isEmpty(offerCatalogDto) 
			&& (checkIsVideoOnDemandOffer(offerCatalogDto.getOfferTypeId(), offerCatalogDto.getProvisioningChannel())
			||  checkIsWelcomeGift(offerCatalogDto.getIsGift()))
			|| offerCatalogDto.isPromotionalGift()
			|| offerCatalogDto.isMamba())
			|| offerCatalogDto.isPointsRedemptionGift();
	}

	/**
	 * 
	 * @param offerCatalogDto
	 * @param memberDetails
	 * @param checkSubscription
	 * @return status to indicate user eligible for benefits
	 */
	private static boolean checkSubscriptionBenefitsApplicable(OfferCatalogResultResponseDto
			offerCatalogDto, GetMemberResponse memberDetails, 
			boolean checkSubscription) {
		
		return checkSubscription
			&& checkOfferEligibleForBenefits(offerCatalogDto.getOfferTypeId(),
						offerCatalogDto.getCategoryId(),
						offerCatalogDto.getSubCategoryId(),
						!ObjectUtils.isEmpty(memberDetails)
						?memberDetails.getBenefits()
						:null);
	}
	
	/**
	    * 
	    * @param offerTypeId
	    * @param categoryId
	    * @param subCategoryId
	    * @param benefits
	    * @return status to indicate offer is part of benefit or not
	    */
	public static boolean checkOfferEligibleForBenefits(String offerTypeId, String categoryId, String subCategoryId,
			List<OfferBenefit> benefits) {
		
		   boolean eligible = false;
		   if(!CollectionUtils.isEmpty(benefits)) {
			   
			   for(OfferBenefit benefit : benefits) {
				   
				   eligible = !StringUtils.isEmpty(offerTypeId)
							  && StringUtils.equalsIgnoreCase(offerTypeId, benefit.getOfferType())
							  && (CollectionUtils.isEmpty(benefit.getCatSubCat())
							  || checkOfferInConfiguredCatgeory(categoryId, subCategoryId, benefit.getCatSubCat())); 
				   
				   if(eligible) {break;}
			   }
			   
		  }
		   return eligible;
	   }

	   /**
	    * 
	    * @param categoryId
	    * @param subCategoryId
	    * @param catSubCategoryList
	    * @return status to indicate offer is part of configured category/subcategory in subscription benefits or not
	    */
	   private static boolean checkOfferInConfiguredCatgeory(String categoryId, String subCategoryId,
				List<CatSubCat> catSubCategoryList) {
	       
	       boolean eligible = false;
	 	   if(!CollectionUtils.isEmpty(catSubCategoryList)) {
	 		   
	 		   for(CatSubCat catSubCat : catSubCategoryList) {
	 			   
	 			   eligible = !StringUtils.isEmpty(categoryId)
	 					  &&  (StringUtils.isEmpty(catSubCat.getCategory())
	 					  ||  StringUtils.equalsIgnoreCase(categoryId, catSubCat.getCategory()))
	 					  &&  !StringUtils.isEmpty(subCategoryId)
	 					  &&  (CollectionUtils.isEmpty(catSubCat.getSubCategory())
	 					  ||  Utilities.presentInList(catSubCat.getSubCategory(), subCategoryId));
	 			 
	 			   if(eligible) {break;}
	 		   } 
	 		   
	 	   }
	 	   return eligible;
		}
	
	/**
	 * 
	 * @param isGift
	 * @return status to indicate offer is a welcome gift
	 */
	private static boolean  checkIsWelcomeGift(String isGift) {
		
		return !StringUtils.isEmpty(isGift)
			&& StringUtils.equalsIgnoreCase(isGift, OfferConstants.FLAG_SET.get());
	}
	
	/**
	 * 
	 * @param denominationCount
	 * @param denomination
	 * @return check status is denomination count is present for denomination
	 */
	public static boolean checkDenominationCountPresent(List<DenominationCount> denominationCountList,
			Integer denomination) {
		
		return !ObjectUtils.isEmpty(FilterValues
				.findAnyDenominationCounterInDenominationCountList(denominationCountList, 
						Predicates.sameDenominationForOfferCounter(denomination)));
	}
	
	/**
	 * 
	 * @param list 
	 * @param accountNumber
	 * @param membershipCode
	 * @param offerId
	 * @return status to indicate account counter present in list 
	 */
	public static boolean checkAccountCounterPresent(List<AccountOfferCounts> list, String accountNumber, String membershipCode, String offerId) {
		
		return !ObjectUtils.isEmpty(FilterValues.findAccountOfferCounterInOfferAccountCounterList(list, Predicates.isCounterWithAccountNumberAndOfferId(accountNumber, membershipCode, offerId)));
	}

	/**
	 * 
	 * @param offerList
	 * @return check offerlist has all cinema offers
	 */
	public static boolean checkAllCinemaOffer(List<OfferCatalog> offerList) {
		
		return !CollectionUtils.isEmpty(offerList)
			&& ObjectUtils.isEmpty(FilterValues.findAnyOfferWithinOfferList(offerList, Predicates.isNotCinemaOffer()));
	}

	/**
	 * 
	 * @param offerList
	 * @return check offer list has both cinema and non cinema offers
	 */
	public static boolean checkCinemaAndNonCinemaOffer(List<OfferCatalog> offerList) {
		
		return !CollectionUtils.isEmpty(offerList)
			&& !ObjectUtils.isEmpty(FilterValues.findAnyOfferWithinOfferList(offerList, Predicates.isCinemaOffer()))
			&& !ObjectUtils.isEmpty(FilterValues.findAnyOfferWithinOfferList(offerList, Predicates.isNotCinemaOffer()));
	}

	/**
	 * 
	 * @param offerList
	 * @return check eligible offer list has all cinema offers
	 */
	public static boolean checkAllCinemaOfferInEligibleOffers(List<EligibleOffers> offerList) {
		
		return !CollectionUtils.isEmpty(offerList)
			&& ObjectUtils.isEmpty(FilterValues.findAnyEligibleOfferWithinOfferList(offerList, Predicates.isNotCinemaOfferEligibleOffer()));
	}
	
	/**
	 * 
	 * @param offerList
	 * @return check eligible offer list has both cinema and non cinema offers
	 */
	public static boolean checkCinemaAndNonCinemaOfferInEligibleOffers(List<EligibleOffers> offerList) {
		
		return !CollectionUtils.isEmpty(offerList)
			&& !ObjectUtils.isEmpty(FilterValues.findAnyEligibleOfferWithinOfferList(offerList, Predicates.isCinemaOfferEligibleOffer()))
			&& !ObjectUtils.isEmpty(FilterValues.findAnyEligibleOfferWithinOfferList(offerList, Predicates.isNotCinemaOfferEligibleOffer()));
	}
	
	/**
     * 
     * @param offerCounter
     * @param offer
     * @param couponQuantity
     * @param eligibilityInfo
     * @param denomination
     * @param resultResponse
     * @param customerSegmentNames
     * @return if download limit is left for the offer 
     */
    public static boolean checkDownloadLimitLeftForEligibleOffer(PurchaseCount purchaseCount, Integer couponQuantity,
			EligibleOfferHelperDto eligibilityInfo, Integer denomination, ResultResponse resultResponse, List<String> customerSegmentNames,
			OfferCatalogResultResponseDto offerCatalogDto) {
		
    	if(Checks.checkCinemaOffer(eligibilityInfo.getOffer().getRules())) {
	    	
	    	if(ObjectUtils.isEmpty(purchaseCount)) {
	    		
	    		purchaseCount = ProcessValues.getValuesForPurchaseCount(eligibilityInfo.getOfferCountersList(), 
	    				eligibilityInfo.getMemberOfferCounterList(), eligibilityInfo.getAccountOfferCounterList());
	    		
	    	}
    		
	    	Checks.checkCinemaOfferDownloadLimit(purchaseCount, eligibilityInfo.getMemberDetails().getCustomerSegment(), resultResponse);	
	    	
	    } else {
	    
	    	LimitCounter limit = ProcessValues.getOfferLimitForCurrentOffer(eligibilityInfo.getOffer().getOfferId(), eligibilityInfo.getOffer().getLimit(), denomination, !CollectionUtils.isEmpty(customerSegmentNames), customerSegmentNames);
	    	LimitCounter counter = ProcessValues.getOfferCounterForCurrentEligibleOffer(eligibilityInfo, denomination, couponQuantity);
	    	Checks.checkDownloadLimitForOffer(counter, limit, resultResponse);
	    	ProcessValues.setQuantityLeftForEligibleOffer(offerCatalogDto, eligibilityInfo, limit, counter);
	    }
	    
	    return Checks.checkNoErrors(resultResponse);
	}
    
    /**
     * 
     * @param offerCounter
     * @param offer
     * @param couponQuantity
     * @param eligibilityInfo
     * @param denomination
     * @param resultResponse
     * @param customerSegmentNames
     * @return if download limit is left for the offer 
     */
    public static boolean checkOfferDownloadLimitLeft(PurchaseCount purchaseCount, OfferCatalog offer, Integer couponQuantity,
			EligibilityInfo eligibilityInfo, Integer denomination, ResultResponse resultResponse, List<String> customerSegmentNames) {
		
	    if(Checks.checkCinemaOffer(offer.getRules())) {
	    	
	    	if(ObjectUtils.isEmpty(purchaseCount)) {
	    		
	    		purchaseCount = ProcessValues.getValuesForPurchaseCount(eligibilityInfo.getOfferCountersList(), 
	    				eligibilityInfo.getMemberOfferCounterList(), eligibilityInfo.getAccountOfferCounterList());
	    		
	    	}
	    	
	    	Checks.checkCinemaOfferDownloadLimit(purchaseCount, eligibilityInfo.getMemberDetails().getCustomerSegment(), resultResponse);	
	    	
	    } else {
	    	
	    	LimitCounter limit = ProcessValues.getOfferLimitForCurrentOffer(offer.getOfferId(), offer.getLimit(), denomination, !CollectionUtils.isEmpty(customerSegmentNames), customerSegmentNames);
			LimitCounter counter = ProcessValues.getOfferCounterForCurrentOffer(eligibilityInfo, denomination, couponQuantity);
			ProcessValues.setLimitCounterToList(eligibilityInfo, counter, limit);
			Checks.checkDownloadLimitForOffer(counter, limit, resultResponse);
	    	
	    }
	    
	    return Checks.checkNoErrors(resultResponse);
	}
    
    /**
	 * 
	 * @param offer
	 * @param denomination
	 * @param couponQuantity
	 * @param purchaseCount
	 * @param eligibilityInfo
	 * @param resultResponse
	 * @return check status if download limit is left for current offer
 	 */
	public static boolean checkDownloadLimitEligibility(OfferCatalog offer, Integer denomination, Integer couponQuantity,
			PurchaseCount purchaseCount, EligibilityInfo eligibilityInfo, ResultResponse resultResponse) {
		
		eligibilityInfo.setOffer(offer);
		eligibilityInfo.setAccountOfferCounter(FilterValues.findAnyAccountOfferCounterInList(eligibilityInfo.getAccountOfferCounterList(), Predicates.isCounterWithAccountNumberAndOfferId(eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberDetails().getMembershipCode(), eligibilityInfo.getOffer().getOfferId())));
		eligibilityInfo.setMemberOfferCounter(!ObjectUtils.isEmpty(eligibilityInfo.getAccountOfferCounter())
				? eligibilityInfo.getAccountOfferCounter().getMemberOfferCounter()
				: FilterValues.findAnyMemberOfferCounterInList(eligibilityInfo.getMemberOfferCounterList(), Predicates.isCounterWithMembershipCodeAndOfferId(eligibilityInfo.getMemberDetails().getMembershipCode(), eligibilityInfo.getOffer().getOfferId())));
		eligibilityInfo.setOfferCounter(!ObjectUtils.isEmpty(eligibilityInfo.getAccountOfferCounter())
				? eligibilityInfo.getAccountOfferCounter().getOfferCounter()
				: FilterValues.findAnyOfferCounterInList(eligibilityInfo.getOfferCountersList(), Predicates.isCounterWithOfferId(eligibilityInfo.getOffer().getOfferId())));
		
		return Checks.checkOfferDownloadLimitLeft(
				purchaseCount, offer, couponQuantity, eligibilityInfo, denomination, resultResponse,
				Utilities.intersection(eligibilityInfo.getMemberDetails().getCustomerSegment(),
						MapValues.mapCustomerSegmentInOfferLimits(offer.getLimit(), Predicates.isCustomerSegmentInLimits())));
		
		
	}

	/**
    *
    * @param giftInfo
    * @return status to indicate offer is a welcome gift
    */
   public static boolean  checkIsOfferWelcomeGift(GiftInfo giftInfo) {
      
       return !ObjectUtils.isEmpty(giftInfo)
           && checkIsWelcomeGift(giftInfo.getIsGift());
   }
   
   /**
    * 
    * @param freeOffers
    * @return status to indicate offer is promotional gift
    */
  	public static boolean  checkIsPromotionalGift(FreeOffers freeOffers) {
     
      return !ObjectUtils.isEmpty(freeOffers)
          && freeOffers.isPromotionalGift();
  	}
	
	/**
    * 
    * @param freeOffers
    * @return status to indicate offer is a mamba offer
    */
   public static boolean checkIsMamba(FreeOffers freeOffers) {
	   
	   return !ObjectUtils.isEmpty(freeOffers) 
			   && freeOffers.isMamba();
	   
   }
   
   /**
    * 
    * @param freeOffers
    * @return status to indicate offer is pointsRedemption gift
    */
  	public static boolean  checkIsPointsRedemptionGift(FreeOffers freeOffers) {
     
      return !ObjectUtils.isEmpty(freeOffers)
          && freeOffers.isPointsRedemptionGift();
  	}
   
   /***
    * 
    * @param eligibilityInfo
    * @param customerSegmentNames 
    * @param offerCatalogResultResponse
    */
   public static boolean checkDownloadLimitLeftForOfferEligibility(EligibilityInfo eligibilityInfo,
			List<String> customerSegmentNames, ResultResponse offerCatalogResultResponse) {
		
	   LOG.info("EligibilityInfo : {}", eligibilityInfo);
	   if(Checks.checkCinemaOffer(eligibilityInfo.getOffer().getRules())) {
	    	
	    	PurchaseCount purchaseCount = ProcessValues.getPurchaseCountForCinemaOffers(eligibilityInfo);
	    	checkCinemaOfferDownloadLimit(purchaseCount, eligibilityInfo.getMemberDetails().getCustomerSegment(), offerCatalogResultResponse);	
	    	
	    } else {
	    	
	    	List<Integer> denominations = !CollectionUtils.isEmpty(eligibilityInfo.getOffer().getDenominations())
	    			? eligibilityInfo.getOffer().getDenominations().stream()
	    					.map(Denomination::getDenominationValue).map(DenominationValue::getDirhamValue)
	    					.collect(Collectors.toList())
	    			: null;
	    	LimitCounterWithDenominationList limit = ProcessValues.getLimitForOfferEligibility(eligibilityInfo.getOffer().getOfferId(), eligibilityInfo.getOffer().getLimit(), !CollectionUtils.isEmpty(customerSegmentNames), customerSegmentNames, denominations);
	    	LOG.info("limit : {}", limit);
	    	LimitCounterWithDenominationList counter = ProcessValues.getCounterForOfferEligibility(eligibilityInfo.getOffer().getOfferId(), eligibilityInfo.getAccountOfferCounter(), denominations);		
	    	LOG.info("counter : {}", counter);
	    	
	    	checkLimitValues(limit, counter, offerCatalogResultResponse);
	    
	    	LOG.info("resultResponse.errors : {}", offerCatalogResultResponse.getApiStatus().getErrors());
	    }
	    
	    return checkNoErrors(offerCatalogResultResponse);
	   
   }   

   /**
    * 
    * @param limit
    * @param counter
    * @param offerCatalogResultResponse
    */
   private static boolean checkLimitValues(LimitCounterWithDenominationList limit, LimitCounterWithDenominationList counter,
		ResultResponse resultResponse) {
	
	   List<OfferErrorCodes> offerErrorCodes = ProcessValues.getCounterCheckErrorCodes(OfferConstants.OFFER_COUNTER.get());
	   List<OfferErrorCodes> offerDenominationErrorCodes = ProcessValues.getCounterCheckErrorCodes(OfferConstants.OFFER_DENOMINATION.get());
	   List<OfferErrorCodes> accountOfferErrorCodes = ProcessValues.getCounterCheckErrorCodes(OfferConstants.ACCOUNT_OFFER_COUNTER.get());
	   List<OfferErrorCodes> accountDenominationErrorCodes = ProcessValues.getCounterCheckErrorCodes(OfferConstants.ACCOUNT_OFFER_DENOMINATION.get());
	   List<OfferErrorCodes> memberOfferErrorCodes = ProcessValues.getCounterCheckErrorCodes(OfferConstants.MEMBER_OFFER_COUNTER.get());
	   List<OfferErrorCodes> memberDenominationErrorCodes = ProcessValues.getCounterCheckErrorCodes(OfferConstants.MEMBER_OFFER_DENOMINATION.get());
	   
	   return ObjectUtils.isEmpty(limit)
		 || ObjectUtils.isEmpty(counter)
		 || (Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counter.getOfferDaily(), limit.getOfferDaily()), offerErrorCodes.get(0), resultResponse)
		 &&  Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counter.getOfferWeekly(), limit.getOfferWeekly()), offerErrorCodes.get(1), resultResponse)
		 &&  Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counter.getOfferMonthly(), limit.getOfferMonthly()), offerErrorCodes.get(2), resultResponse)
		 &&  Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counter.getOfferAnnual(), limit.getOfferAnnual()), offerErrorCodes.get(3), resultResponse)
		 &&  Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counter.getOfferTotal(), limit.getOfferTotal()), offerErrorCodes.get(4), resultResponse)
		 &&  Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counter.getAccountOfferDaily(), limit.getAccountOfferDaily()), accountOfferErrorCodes.get(0), resultResponse)
		 &&  Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counter.getAccountOfferWeekly(), limit.getAccountOfferWeekly()), accountOfferErrorCodes.get(1), resultResponse)
		 &&  Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counter.getAccountOfferMonthly(), limit.getAccountOfferMonthly()), accountOfferErrorCodes.get(2), resultResponse)
		 &&  Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counter.getAccountOfferAnnual(), limit.getAccountOfferAnnual()), accountOfferErrorCodes.get(3), resultResponse)
		 &&  Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counter.getAccountOfferTotal(), limit.getAccountOfferTotal()), accountOfferErrorCodes.get(4), resultResponse)	 
		 &&  Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counter.getMemberOfferDaily(), limit.getMemberOfferDaily()), memberOfferErrorCodes.get(0), resultResponse)
		 &&  Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counter.getMemberOfferWeekly(), limit.getMemberOfferWeekly()), memberOfferErrorCodes.get(1), resultResponse)
		 &&  Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counter.getMemberOfferMonthly(), limit.getMemberOfferMonthly()), memberOfferErrorCodes.get(2), resultResponse)
		 &&  Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counter.getMemberOfferAnnual(), limit.getMemberOfferAnnual()), memberOfferErrorCodes.get(3), resultResponse)
		 &&  Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counter.getMemberOfferTotal(), limit.getMemberOfferTotal()), memberOfferErrorCodes.get(4), resultResponse)		 
		 && checkDenominationLimitForOfferEligibility(limit, counter, offerDenominationErrorCodes, accountDenominationErrorCodes, memberDenominationErrorCodes, resultResponse));
	
   }

   /**
    * 
    * @param limit
    * @param counter
    * @param offerDenominationErrorCodes
    * @param accountDenominationErrorCodes
    * @param memberDenominationErrorCodes
    * @param resultResponse
    * @return
    */
   private static boolean checkDenominationLimitForOfferEligibility(LimitCounterWithDenominationList limit,
		LimitCounterWithDenominationList counter, List<OfferErrorCodes> offerDenominationErrorCodes,
		List<OfferErrorCodes> accountDenominationErrorCodes, List<OfferErrorCodes> memberDenominationErrorCodes,
		ResultResponse resultResponse) {
	    
	    boolean check = ObjectUtils.isEmpty(limit)
		    	 || CollectionUtils.isEmpty(limit.getDenominationList())	
		   		 || ObjectUtils.isEmpty(counter)
				 || CollectionUtils.isEmpty(counter.getDenominationList());
	    
	    if(!check) {
	    
	    	List<Integer> denominationList = limit.getDenominationList().stream()
	    			.map(DenominationLimitCounter::getDenomination).collect(Collectors.toList());
	    	
	    	if(!CollectionUtils.isEmpty(denominationList)) {
	    		
	    		for(Integer denomination : denominationList) {
		    		
	    			DenominationLimitCounter limitDenomination = limit.getDenominationList().stream()
	    	    			.filter(d->d.getDenomination().equals(denomination)).findAny().orElse(null);
	    			DenominationLimitCounter counterDenomination = counter.getDenominationList().stream()
	    	    			.filter(d->d.getDenomination().equals(denomination)).findAny().orElse(null); 		
	    			
	    			if(!ObjectUtils.isEmpty(limitDenomination)
	    			&& !ObjectUtils.isEmpty(counterDenomination)) {
	    				
	    				check = Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counterDenomination.getOfferDaily(), limitDenomination.getOfferDaily()), offerDenominationErrorCodes.get(0), resultResponse)
	    				 &&  Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counterDenomination.getOfferWeekly(), limitDenomination.getOfferWeekly()), offerDenominationErrorCodes.get(1), resultResponse)
	    				 &&  Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counterDenomination.getOfferMonthly(), limitDenomination.getOfferMonthly()), offerDenominationErrorCodes.get(2), resultResponse)
	    				 &&  Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counterDenomination.getOfferAnnual(), limitDenomination.getOfferAnnual()), offerDenominationErrorCodes.get(3), resultResponse)
	    				 &&  Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counterDenomination.getOfferTotal(), limitDenomination.getOfferTotal()), offerDenominationErrorCodes.get(4), resultResponse)
	    				 &&  Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counterDenomination.getAccountDaily(), limitDenomination.getAccountDaily()), accountDenominationErrorCodes.get(0), resultResponse)
	    				 &&  Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counterDenomination.getAccountWeekly(), limitDenomination.getAccountWeekly()), accountDenominationErrorCodes.get(1), resultResponse)
	    				 &&  Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counterDenomination.getAccountMonthly(), limitDenomination.getAccountMonthly()), accountDenominationErrorCodes.get(2), resultResponse)
	    				 &&  Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counterDenomination.getAccountAnnual(), limitDenomination.getAccountAnnual()), accountDenominationErrorCodes.get(3), resultResponse)
	    				 &&  Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counterDenomination.getAccountTotal(), limitDenomination.getAccountTotal()), accountDenominationErrorCodes.get(4), resultResponse)
	    				 &&  Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counterDenomination.getMemberDaily(), limitDenomination.getMemberDaily()), memberDenominationErrorCodes.get(0), resultResponse)
	    				 &&  Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counterDenomination.getMemberWeekly(), limitDenomination.getMemberWeekly()), memberDenominationErrorCodes.get(1), resultResponse)
	    				 &&  Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counterDenomination.getMemberMonthly(), limitDenomination.getMemberMonthly()), memberDenominationErrorCodes.get(2), resultResponse)
	    				 &&  Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counterDenomination.getMemberAnnual(), limitDenomination.getMemberAnnual()), memberDenominationErrorCodes.get(3), resultResponse)
	    				 &&  Responses.setResponseAfterConditionCheck(checkLimitForEligibility(counterDenomination.getMemberTotal(), limitDenomination.getMemberTotal()), memberDenominationErrorCodes.get(4), resultResponse);
	    				
	    			}
	    			
	    			if(!check) {
	    			    break;	
	    			}
	    			
	    		}
	    		
	    	}		
	    	
	    }
	    
	    return check;
   }

   /**
    * 
    * @param totalCount
    * @param updateResult
    * @return
    */
	public static boolean checkCountRemaining(Integer totalCount, UpdateResult updateResult) {
		
		return !ObjectUtils.isEmpty(updateResult)
		&& !ObjectUtils.isEmpty(updateResult.getModifiedCount())
		&& !ObjectUtils.isEmpty(totalCount)
		&& (totalCount-(int) updateResult.getModifiedCount())>0;
	}
	
	/***
	 * 
	 * @param headers
	 * @return
	 */
    public static boolean checkSappChannel(Headers headers) {
	
    	return !ObjectUtils.isEmpty(headers)
    			&& !StringUtils.isEmpty(headers.getChannelId())
    			&& headers.getChannelId().equalsIgnoreCase(OfferConstants.SAPP.get());
    }
	
	 /***
     * 
     * @param purchaseResultResponse
     * @return status to indicate purchaseresponse is present
     */
	public static boolean checkPurchaseResponsePresent(PurchaseResultResponse purchaseResultResponse) {
		
		return !ObjectUtils.isEmpty(purchaseResultResponse)
			&& !ObjectUtils.isEmpty(purchaseResultResponse.getPurchaseResponseDto());
	}
	
	/***
	 * 
	 * @param method
	 * @return status to indicate payment method is eligible for reversal
	 */
	public static boolean checkReversalPaymentMethod(String method) {
    	
    	return Utilities.presentInList(OffersListConstants.REVERSAL_PAYMENT_TYPES, method);
    	
    }

	/***
	 * 
	 * @param purchaseRequestDto
	 * @param headers
	 * @param purchaseResultResponse 
	 * @return status to indicate transaction is eligible for refund
	 */
	public static boolean checkEligibleForRefund(PurchaseRequestDto purchaseRequestDto, Headers headers, PurchaseResultResponse purchaseResultResponse) {
		
		return checkSappChannel(headers)
			&& checkErrorsPresent(purchaseResultResponse)
			&& checkReversalPaymentMethod(purchaseRequestDto.getSelectedOption());
	}
	
	/**
	 * 
	 * @param existingOffer
	 * @return check if points accrual code exists for the offer
	 */
	public static boolean checkPointsAccrualCodeExists(OfferCatalog existingOffer) {
		
		return !ObjectUtils.isEmpty(existingOffer)
			&& !ObjectUtils.isEmpty(existingOffer.getActivityCode())
			&& !ObjectUtils.isEmpty(existingOffer.getActivityCode().getPointsAccrualActivityCode());
	}
	
	/**
	 * 
	 * @param resultResponse
	 * @param apiStatus
	 * @return
	 */
	public static boolean checkValidResponseWithNoErrorsForActivityCode(ResultResponse resultResponse, ApiStatus apiStatus) {
		
		return Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(apiStatus), OfferErrorCodes.MEMBER_MANAGEMENT_RESPONSE_NOT_RECEIVED, resultResponse)
			&& (Responses.addServiceCallErrors(resultResponse, apiStatus)
			|| checkActivityCodeExists(resultResponse));
	}

	/**
	 * 
	 * @param resultResponse
	 * @return
	 */
	public static boolean checkActivityCodeExists(ResultResponse resultResponse) {
	
		return Responses.getErrorSize(resultResponse) == 1
			&& checkSpecificErrorCodePresent(resultResponse, OfferConstants.PARTNER_EXISTS_CODE.get());	
	}

	/**
	 * 
	 * @param resultResponse
	 * @param string
	 * @return
	 */
	private static boolean checkSpecificErrorCodePresent(ResultResponse resultResponse, String code) {
		
		boolean status = false;
		
		for(Errors error :  resultResponse.getApiStatus().getErrors()) {
			status = String.valueOf(error.getCode()).equals(code);
			LOG.info("status : {}", status);
			if(status) break;
		}
		return status;
	}

	/***
	 * 
	 * @param subscriptionResponse
	 * @return status to indicate subscription was successful
	 */
	public static boolean checkSuccessfulSubscription(PurchaseResultResponse subscriptionResponse) {
		
		return checkNoErrors(subscriptionResponse)
			|| (checkPurchaseResponsePresent(subscriptionResponse)
			&& StringUtils.equalsIgnoreCase(subscriptionResponse.getPurchaseResponseDto().getPaymentStatus(), OfferConstants.SUCCESS.get())
		    && !StringUtils.isEmpty(subscriptionResponse.getPurchaseResponseDto().getSubscriptionId()));
		
	}
	
}
