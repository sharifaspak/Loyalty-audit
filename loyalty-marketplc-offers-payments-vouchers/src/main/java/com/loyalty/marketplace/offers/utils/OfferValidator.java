package com.loyalty.marketplace.offers.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import com.loyalty.marketplace.constants.MarketPlaceCode;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OfferErrorCodes;
import com.loyalty.marketplace.offers.constants.OffersListConstants;
import com.loyalty.marketplace.offers.helper.dto.OfferReferences;
import com.loyalty.marketplace.offers.inbound.dto.EligibleOffersFiltersRequest;
import com.loyalty.marketplace.offers.inbound.dto.LimitDto;
import com.loyalty.marketplace.offers.inbound.dto.ListValuesDto;
import com.loyalty.marketplace.offers.inbound.dto.OfferCatalogDto;
import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
import com.loyalty.marketplace.offers.inbound.dto.SubOfferDto;
import com.loyalty.marketplace.offers.inbound.dto.TransactionRequestDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.ParentChlidCustomer;
import com.loyalty.marketplace.offers.merchants.outbound.database.entity.Merchant;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferType;
import com.loyalty.marketplace.offers.outbound.dto.OfferCatalogResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.PurchaseResultResponse;
import com.loyalty.marketplace.offers.stores.outbound.database.entity.Store;
import com.loyalty.marketplace.outbound.database.entity.Category;
import com.loyalty.marketplace.outbound.database.entity.Denomination;
import com.loyalty.marketplace.outbound.database.entity.PaymentMethod;
import com.loyalty.marketplace.outbound.dto.Errors;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.utils.MarketplaceValidator;
import com.loyalty.marketplace.utils.Utils;
import com.loyalty.marketplace.voucher.outbound.database.entity.VoucherAction;

/**
 * 
 * @author jaya.shukla
 *
 */
public class OfferValidator extends MarketplaceValidator {
	
	/**
	 * 
	 * @param marketPlaceDto
	 * @param validator
	 * @param resultResponse
	 * @return value to indicate validation of do as per annotations
	 */
	public static boolean validateDto(Object marketPlaceDto, Validator validator,
			ResultResponse resultResponse) {
		Set<ConstraintViolation<Object>> violations = validator.validate(marketPlaceDto);
		if (violations.isEmpty()) {
			return true;
		} else {
			List<Errors> errorList=new ArrayList<>();
			
			for (ConstraintViolation<Object> violation : violations) {
				 Errors error=new Errors();
				 if (violation.getMessage()!=null) {
					error.setCode(Integer.parseInt(
							(violation.getMessage().split(MarketPlaceCode.VALIDATOR_DELIMITOR.getConstant(), 2)[0])));
					error.setMessage((violation.getMessage().split(MarketPlaceCode.VALIDATOR_DELIMITOR.getConstant(),2)[1]));
		         errorList.add(error);	
				}else {
					 error.setCode(MarketPlaceCode.INVALID_PARAMETER.getIntId());
			         error.setMessage(MarketPlaceCode.INVALID_PARAMETER.getMsg());
				}
			}
		resultResponse.setBulkErrorAPIResponse(errorList);
		return false;
		}
	}
	
	/**
	 * @param action : parameter to be validated
	 * @param resultResponse : to set corresponding error if encountered
	 * @return boolean value indicating whether action is valid or not
	 * @description Validate the input action
	 * Valid update actions are UPDATE, ACTIVATE, DEACTIVATE
	 */
	public static boolean validateOfferUpdateAction(String action, ResultResponse resultResponse) {
		
		Responses.setResponseAfterConditionCheck(!StringUtils.isEmpty(action), OfferErrorCodes.INVALID_ACTION, resultResponse);
		Responses.setResponseAfterConditionCheck(Utilities.presentInList(OffersListConstants.UPDATE_ACTION_LIST, action), 
				OfferErrorCodes.INVALID_UPDATE_ACTION, resultResponse);
		
		return Checks.checkNoErrors(resultResponse);
	}
	
	/**
	 * @param offerCatalogRequest : request object to be validated
	 * @param resultResponse : to set corresponding error if encountered
	 * @return boolean value indicating whether reuest object is valid or not
	 * @throws ParseException 
	 * @description : Validate the request object as pe following conditions : 
	 * a)OfferCode must not be empty
	 * b)OfferLabel - always in pair(English and Arabic), any one cannot be present alone
	 * c)Dates(OfferStartDate, OfferEndDate, VoucherExpiryDate must be in prper format - )
	 * d)Voucher Action must be in LOV "1", "2", "3"
	 * e)Validations corresponding to offer type(checked in validateOfferTypeAttributes)
	 */
	public static boolean validateOfferCatalog(OfferCatalogDto offerCatalogRequest, 
			ResultResponse resultResponse) throws ParseException{

		   return checkValidFields(offerCatalogRequest, resultResponse)
	       && checkBlankValues(offerCatalogRequest, resultResponse)
		   && checkValidValuesInList(offerCatalogRequest, resultResponse)
		   && validateOfferTypeAttributes(offerCatalogRequest, resultResponse)
		   && validateMandatoryDenominations(offerCatalogRequest, resultResponse)
		   && validateCustomerSegmentAndLimits(MapValues.mapCustomerSegmentNamesFromLimit(offerCatalogRequest.getLimit(), Predicates.limitWithCustomerSegmentPresent()), 
				offerCatalogRequest.getCustomerSegments(), resultResponse);	
		  
	}
	
	/**
	 * 
	 * @param offerCatalogRequest
	 * @param resultResponse
	 * @return
	 */
	private static boolean validateMandatoryDenominations(OfferCatalogDto offerCatalogRequest,
			ResultResponse resultResponse) {
		
		if(Utilities.presentInList(OffersListConstants.REQUIRED_DENOMINATIONS_TYPES, offerCatalogRequest.getOfferTypeId())
		&& CollectionUtils.isEmpty(offerCatalogRequest.getDenominations())){
			
			Responses.addError(resultResponse, OfferErrorCodes.DENOMINATION_NOT_PRESENT);
			
		}
		
		return Checks.checkNoErrors(resultResponse);
	}

	/**
	 * 
	 * @param offerCatalogRequest
	 * @param resultResponse
	 * @return value to indicate if sent fields are in valid format
	 * @throws ParseException
	 */
	private static boolean checkValidFields(OfferCatalogDto offerCatalogRequest, ResultResponse resultResponse) throws ParseException {
		
		String offerCodeRemovedSpecialChar = Utils.removeSpecialChars(offerCatalogRequest.getOfferCode());

		Responses.setResponseAfterConditionCheck(!StringUtils.isEmpty(offerCodeRemovedSpecialChar), OfferErrorCodes.INVALID_OFFER_CODE, resultResponse);
		Responses.setResponseAfterConditionCheck(!StringUtils.isEmpty(offerCodeRemovedSpecialChar) && offerCodeRemovedSpecialChar.length()<=20,
				OfferErrorCodes.INVALID_OFFER_CODE_SIZE, resultResponse);
		Responses.setResponseAfterConditionCheck(Checks.checkOnePresentWhenOtherPresent(offerCatalogRequest.getOfferLabelEn(), offerCatalogRequest.getOfferLabelAr()),
				OfferErrorCodes.INVALID_OFFER_LABEL_AR, resultResponse);
		Responses.setResponseAfterConditionCheck(Checks.checkOnePresentWhenOtherPresent(offerCatalogRequest.getOfferLabelAr(), offerCatalogRequest.getOfferLabelEn()),
				OfferErrorCodes.INVALID_OFFER_LABEL_EN, resultResponse);
		validateDates(offerCatalogRequest, resultResponse);
		validateDoubleValues(offerCatalogRequest, resultResponse);
		
		return Checks.checkNoErrors(resultResponse);
	}
	
	/**
	 * 
	 * @param offerCatalogRequest
	 * @param resultResponse
	 * @description checks all double values are greater than 0.0
	 */
	private static void validateDoubleValues(OfferCatalogDto offerCatalogRequest, ResultResponse resultResponse) {
		
		Responses.setResponseAfterConditionCheck(Checks.checkValidDoubleMoreThanEqualToZero(offerCatalogRequest.getCost()), 
				OfferErrorCodes.INVALID_COST_VALUE, resultResponse);
		Responses.setResponseAfterConditionCheck(Checks.checkValidDoubleMoreThanZero(offerCatalogRequest.getEstSavings()), 
				OfferErrorCodes.INVALID_ESTIMATED_SAVINGS_VALUE, resultResponse);
		Responses.setResponseAfterConditionCheck(Checks.checkValidDoubleMoreThanEqualToZero(offerCatalogRequest.getVatPercentage()), 
				OfferErrorCodes.INVALID_VAT_PERCENTAGE_VALUE, resultResponse);
		Responses.setResponseAfterConditionCheck(Checks.checkValidDoubleMoreThanEqualToZero(offerCatalogRequest.getEarnMultiplier()), 
				OfferErrorCodes.INVALID_EARN_MULTIPLIER_VALUE, resultResponse);
		Responses.setResponseAfterConditionCheck(Checks.checkValidDoubleMoreThanEqualToZero(offerCatalogRequest.getVoucherAmount()), 
				OfferErrorCodes.INVALID_VOUCHER_AMOUNT, resultResponse);
		
		validateSubOfferDoubleValues(offerCatalogRequest,resultResponse);
		
	}

	/**
	 * 
	 * @param offerCatalogRequest
	 * @param resultResponse
	 */
	private static void validateSubOfferDoubleValues(OfferCatalogDto offerCatalogRequest,
			ResultResponse resultResponse) {
		
		if(!CollectionUtils.isEmpty(offerCatalogRequest.getSubOffer())) {
			
			for(SubOfferDto subOffer:offerCatalogRequest.getSubOffer()) {
				
				Responses.setResponseWithMessageAfterConditionCheck(Checks.checkValidDoubleMoreThanEqualToZero(subOffer.getOldCost()),
						OfferErrorCodes.INVALID_SUBOFFER_OLD_COST, subOffer.getSubOfferId(), resultResponse);
				Responses.setResponseWithMessageAfterConditionCheck(Checks.checkValidDoubleMoreThanEqualToZero(subOffer.getNewCost()),
						OfferErrorCodes.INVALID_SUBOFFER_NEW_COST, subOffer.getSubOfferId(), resultResponse);
				
			}
			
		}
		
	}

	/***
	 * 
	 * @param offerCatalogRequest
	 * @param resultResponse
	 * @throws ParseException 
	 * @description checks if all the dates are valid 
	 */
	private static void validateDates(OfferCatalogDto offerCatalogRequest, ResultResponse resultResponse) throws ParseException {
		
		checkDateFormat(offerCatalogRequest, resultResponse);
		String format = OfferConstants.DATE_FORMAT.get();
		Date startDate = Utilities.changeStringToDate(offerCatalogRequest.getOfferStartDate(), format);
		Date endDate = (offerCatalogRequest.getOfferEndDate()!=null)
				? Utilities.changeStringToDate(offerCatalogRequest.getOfferEndDate(), format)
				: null;
		Responses.setResponseAfterConditionCheck(!Checks.checkDateFirstGreaterThanSecond(startDate, endDate), 
						OfferErrorCodes.START_DATE_GREATER_THAN_END_DATE, resultResponse);		
				
	}
	
	/**
	 * 
	 * @param offerCatalogRequest
	 * @param resultResponse
	 * @throws ParseException
	 */
    private static void checkDateFormat(OfferCatalogDto offerCatalogRequest, ResultResponse resultResponse) throws ParseException {
		
		Responses.setResponseAfterConditionCheck(ObjectUtils.isEmpty(offerCatalogRequest.getOfferStartDate()) || validateDate(offerCatalogRequest.getOfferStartDate()),
				OfferErrorCodes.INVALID_OFFER_STARTDATE, resultResponse);
		Responses.setResponseAfterConditionCheck(ObjectUtils.isEmpty(offerCatalogRequest.getOfferEndDate()) || validateDate(offerCatalogRequest.getOfferEndDate()),
				OfferErrorCodes.INVALID_OFFER_ENDDATE, resultResponse);
		Responses.setResponseAfterConditionCheck(ObjectUtils.isEmpty(offerCatalogRequest.getVoucherExpiryDate()) || validateDate(offerCatalogRequest.getVoucherExpiryDate()),
				OfferErrorCodes.INVALID_VOUCHER_EXPIRYDATE, resultResponse);
		
	}
    
	/**
	 * 
	 * @param offerCatalogRequest
	 * @param resultResponse
	 * @return value to indicate if no values are blank
	 */
    private static boolean checkBlankValues(OfferCatalogDto offerCatalogRequest, ResultResponse resultResponse) {
		
    	Checks.checkEmptyValuePresent(offerCatalogRequest.getAvailableInPortals(), OfferErrorCodes.PORTAL_VALUE_BLANK, resultResponse);
    	Checks.checkEmptyValuePresent(offerCatalogRequest.getRules(), OfferErrorCodes.RULE_VALUE_BLANK, resultResponse);
    	Checks.checkEmptyValuePresent(offerCatalogRequest.getStoreCodes(), OfferErrorCodes.STORE_VALUE_BLANK, resultResponse);
    	
    	if(!ObjectUtils.isEmpty(offerCatalogRequest.getCustomerTypes())) {
    		
    		Checks.checkEmptyValuePresent(offerCatalogRequest.getCustomerTypes().getEligibleTypes(), OfferErrorCodes.ELIGIBLE_VALUE_BLANK, resultResponse);
    		Checks.checkEmptyValuePresent(offerCatalogRequest.getCustomerTypes().getExclusionTypes(), OfferErrorCodes.EXCLUSION_VALUE_BLANK, resultResponse);
    		
    	}
    	
    	if(!ObjectUtils.isEmpty(offerCatalogRequest.getCustomerSegments())) {
    		
    		Checks.checkEmptyValuePresent(offerCatalogRequest.getCustomerSegments().getEligibleTypes(), OfferErrorCodes.ELIGIBLE_SEGMENT_BLANK, resultResponse);
    		Checks.checkEmptyValuePresent(offerCatalogRequest.getCustomerSegments().getExclusionTypes(), OfferErrorCodes.EXCLUSION_SEGMENT_BLANK, resultResponse);
    	}
    	
    	return Checks.checkNoErrors(resultResponse);
		
	}

    /**
     * 
     * @param offerCatalogRequest
     * @param resultResponse
     * @return value to indicate all values in list are valid
     */
	private static boolean checkValidValuesInList(OfferCatalogDto offerCatalogRequest, 
			ResultResponse resultResponse) {
		
		checkFlagValidValues(offerCatalogRequest, resultResponse);
		checkListValidValues(offerCatalogRequest, resultResponse);
		
		return Checks.checkNoErrors(resultResponse);
	}
	
	/**
	 * 
	 * @param offerCatalogRequest
	 * @param resultResponse
	 */
	private static void checkFlagValidValues(OfferCatalogDto offerCatalogRequest, ResultResponse resultResponse) {
		
		Checks.checkFlagValueValid(offerCatalogRequest.getIsDod(), OfferErrorCodes.INVALID_IS_DOD, resultResponse);
		Checks.checkFlagValueValid(offerCatalogRequest.getIsFeatured(), OfferErrorCodes.INVALID_IS_FEATURED, resultResponse);
		Checks.checkFlagValueValid(offerCatalogRequest.getDynamicDenomination(), OfferErrorCodes.INVALID_IS_DOD, resultResponse);
		Checks.checkFlagValueValid(offerCatalogRequest.getIsBirthdayGift(), OfferErrorCodes.INVALID_IS_DOD, resultResponse);
		Checks.checkFlagValueValid(offerCatalogRequest.getIsGift(), OfferErrorCodes.INVALID_IS_GIFT, resultResponse);
		Checks.checkFlagValueValid(offerCatalogRequest.getSharing(), OfferErrorCodes.INVALID_SHARING, resultResponse);
		Checks.checkFlagValueValid(offerCatalogRequest.getNewOffer(), OfferErrorCodes.INVALID_NEW_OFFER, resultResponse);
		Checks.checkGroupFlagValueValid(offerCatalogRequest.getGroupedFlag(), OfferErrorCodes.INVALID_GROUPED_FLAG, resultResponse);
		
	}
	
	/**
	 * 
	 * @param offerCatalogRequest
	 * @param resultResponse
	 */
    private static void checkListValidValues(OfferCatalogDto offerCatalogRequest, ResultResponse resultResponse) {
		
		Checks.checkInvalidValuePresentInList(OffersListConstants.CONFIGURED_RULES_LIST, offerCatalogRequest.getRules(),
    			OfferErrorCodes.RULE_NOT_ALLOWED, resultResponse);
		
	}

	/**
     * 
     * @param offerCatalogRequest
     * @param resultResponse
     * @return value to indicate all offer type specific parameters are valid
     */
	private static boolean validateOfferTypeAttributes(OfferCatalogDto offerCatalogRequest,
			ResultResponse resultResponse) {
		
		String item = offerCatalogRequest.getOfferTypeId();
		
		return Responses.setResponseAfterConditionCheck(Checks.checkValidOfferTypeForCreation(item), OfferErrorCodes.CANNOT_CREATE_OFFER_WITH_OFFER_TYPE, resultResponse)
			&& (Checks.checkIsGoldCertificate(item)
			|| (Checks.checkIsDiscountVoucher(item) && checkValidDiscountVoucher(offerCatalogRequest,resultResponse))	
			|| (Checks.checkIsCashVoucher(item) && checkValidCashVoucher(offerCatalogRequest,resultResponse))
			|| (Checks.checkIsDealVoucher(item) && checkValidDealVoucher(offerCatalogRequest,resultResponse))
			|| (Checks.checkIsEtisalatAddon(item) && checkValidEtisalatOffer(offerCatalogRequest,resultResponse)));
			
	}

	/**
	 * 
	 * @param offerCatalogRequest
	 * @param resultResponse
	 * @return value to indicate discount offer parameters are valid
	 */
	private static boolean checkValidDiscountVoucher(OfferCatalogDto offerCatalogRequest,
			ResultResponse resultResponse) {
	
		Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(offerCatalogRequest.getEstSavings()),  OfferErrorCodes.ESTIMATED_SAVINGS_REQUIRED,resultResponse);
		validateVoucherInfo(offerCatalogRequest, resultResponse);
		return Checks.checkNoErrors(resultResponse);
	}
	
	/**
	 * 
	 * @param offerCatalogRequest
	 * @param resultResponse
	 */
	private static void validateVoucherInfo(OfferCatalogDto offerCatalogRequest, ResultResponse resultResponse) {
		
		Responses.setResponseAfterConditionCheck(!StringUtils.isEmpty(offerCatalogRequest.getVoucherRedeemType()), OfferErrorCodes.BLANK_VOUCHER_REDEEM_TYPE, resultResponse);
		Responses.setResponseAfterConditionCheck(Checks.checkValidVoucherRedeemType(offerCatalogRequest.getVoucherRedeemType()), OfferErrorCodes.INVALID_VOUCHER_REDEEM_TYPE, resultResponse);
		Checks.checkValidDefaultRedeemTypeAttributes(offerCatalogRequest, resultResponse);
		Checks.checkValidNonPinRedeemTypeAttributes(offerCatalogRequest, resultResponse);
		Checks.checkValidOnlineRedeemTypeAttributes(offerCatalogRequest, resultResponse);
		Checks.checkValidPartnerPinRedeemTypeAttributes(offerCatalogRequest, resultResponse);
		
	}

	/**
	 * 
	 * @param offerCatalogRequest
	 * @param resultResponse
	 * @return value to indicate etisalat offer parameters are valid
	 * 
	 */
	private static boolean checkValidEtisalatOffer(OfferCatalogDto offerCatalogRequest,
			ResultResponse resultResponse) {
		
		Responses.setResponseAfterConditionCheck(!StringUtils.isEmpty(offerCatalogRequest.getProvisioningChannel()),
				OfferErrorCodes.INVALID_PROVISIONING_CHANNEL, resultResponse);
		if(Checks.checkNoErrors(resultResponse)
		&& Checks.checkInvalidValuePresentInList(OffersListConstants.PROVISIONING_CHANNEL_LIST, Arrays.asList(offerCatalogRequest.getProvisioningChannel()), OfferErrorCodes.PROVISIONING_CHANNEL_NOT_ALLOWED, resultResponse)) {
					
			Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(offerCatalogRequest.getVatPercentage()), 
					OfferErrorCodes.NULL_VAT_PERCENTAGE, resultResponse);
			
			checkValidProvisioningAttributes(offerCatalogRequest, resultResponse);		

		}
		
		return Checks.checkNoErrors(resultResponse);
	}	

	/**
	 * 
	 * @param offerCatalogRequest
	 * @param resultResponse
	 * @description value to indicate provisioning attributes are are valid
	 * 
	 */
	private static boolean checkValidProvisioningAttributes(OfferCatalogDto offerCatalogRequest,
			ResultResponse resultResponse) {
		
		String provisioningChannel = Utils.removeSpecialChars(offerCatalogRequest.getProvisioningChannel());
		
		return (Checks.checkIsComsProvisioningChannel(provisioningChannel) && validateComsAttributes(offerCatalogRequest, resultResponse))
			|| (Checks.checkIsRtfProvisioningChannel(provisioningChannel) && validateRtfAttributes(offerCatalogRequest, resultResponse))
			|| (Checks.checkIsEmcaisProvisioningChannel(provisioningChannel) && validateEmcaisAttributes(offerCatalogRequest, resultResponse))
			|| (Checks.checkIsPhoneyTunesProvisioningChannel(provisioningChannel) && validatePhoneyTunesAttributes(offerCatalogRequest, resultResponse))
			|| (Checks.checkIsRbtProvisioningChannel(provisioningChannel) && validateRbtAttributes(offerCatalogRequest, resultResponse));
		
	}
	
	/**
	 * 
	 * @param offerCatalogRequest
	 * @param resultResponse
	 * @return
	 */
	private static boolean validateComsAttributes(OfferCatalogDto offerCatalogRequest, ResultResponse resultResponse) {
		
		Responses.setResponseAfterConditionCheck(!StringUtils.isEmpty(offerCatalogRequest.getRatePlanCode()), 
				OfferErrorCodes.INVALID_RATE_PLAN_CODE, resultResponse);
		
		return Checks.checkNoErrors(resultResponse);
		
	}
	
	/**
	 * 
	 * @param offerCatalogRequest
	 * @param resultResponse
	 * @return
	 */
	private static boolean validateRtfAttributes(OfferCatalogDto offerCatalogRequest, ResultResponse resultResponse) {
		
		
//		Responses.setResponseAfterConditionCheck(!StringUtils.isEmpty(offerCatalogRequest.getRtfProductType()), 
//				OfferErrorCodes.INVALID_PRODUCT_TYPE, resultResponse);
		Responses.setResponseAfterConditionCheck(!StringUtils.isEmpty(offerCatalogRequest.getRtfProductCode()), 
				OfferErrorCodes.INVALID_PRODUCT_CODE, resultResponse);
		
		return Checks.checkNoErrors(resultResponse);
		
	}
	
	/**
	 * 
	 * @param offerCatalogRequest
	 * @param resultResponse
	 * @return
	 */
	private static boolean validateEmcaisAttributes(OfferCatalogDto offerCatalogRequest, ResultResponse resultResponse) {
		
		
		Responses.setResponseAfterConditionCheck(!StringUtils.isEmpty(offerCatalogRequest.getVasCode()), 
				OfferErrorCodes.INVALID_VASCODE, resultResponse);
		Responses.setResponseAfterConditionCheck(!StringUtils.isEmpty(offerCatalogRequest.getVasActionId()), 
				OfferErrorCodes.INVALID_VASACTION, resultResponse);
		
		return Checks.checkNoErrors(resultResponse);
		
	}
	
	/**
	 * 
	 * @param offerCatalogRequest
	 * @param resultResponse
	 * @return
	 */
	private static boolean validatePhoneyTunesAttributes(OfferCatalogDto offerCatalogRequest,
			ResultResponse resultResponse) {
		
		Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(offerCatalogRequest.getPromotionalPeriod()),
				OfferErrorCodes.INVALID_PROMOTIONAL_PERIOD, resultResponse);
		return Checks.checkNoErrors(resultResponse);
		
	}
	
	/**
	 * 
	 * @param offerCatalogRequest
	 * @param resultResponse
	 * @return
	 */
	private static boolean validateRbtAttributes(OfferCatalogDto offerCatalogRequest, ResultResponse resultResponse) {

		Responses.setResponseAfterConditionCheck(!StringUtils.isEmpty(offerCatalogRequest.getFeature()), 
				OfferErrorCodes.INVALID_FEATURE, resultResponse);
		Responses.setResponseAfterConditionCheck(!StringUtils.isEmpty(offerCatalogRequest.getServiceId()),
				OfferErrorCodes.INVALID_SERVICE_ID, resultResponse);
		Responses.setResponseAfterConditionCheck(!StringUtils.isEmpty(offerCatalogRequest.getActivityId()),
				OfferErrorCodes.INVALID_ACTIVITY_ID, resultResponse);
		Responses.setResponseAfterConditionCheck(!StringUtils.isEmpty(offerCatalogRequest.getPackName()), 
				OfferErrorCodes.INVALID_PACK_NAME, resultResponse);
		return Checks.checkNoErrors(resultResponse);
		
	}

	/**
	 * 
	 * @param offerCatalogRequest
	 * @param resultResponse
	 * @description value to indicate cash offer parameters are valid
	 * 
	 */
	private static boolean checkValidCashVoucher(OfferCatalogDto offerCatalogRequest,
			ResultResponse resultResponse) {
		
		if(!StringUtils.isEmpty(offerCatalogRequest.getDynamicDenomination()) 
		&& StringUtils.equalsIgnoreCase(OfferConstants.FLAG_SET.get(), offerCatalogRequest.getDynamicDenomination())){
			
			Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(offerCatalogRequest.getMinDenomination()), 
					OfferErrorCodes.BLANK_MIN_DYNAMIC_DENOMINATION, resultResponse);
			Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(offerCatalogRequest.getMaxDenomination()), 
					OfferErrorCodes.BLANK_MAX_DYNAMIC_DENOMINATION, resultResponse);
			Responses.setResponseAfterConditionCheck(Checks.checkFirstIntegerLessThanSecond(offerCatalogRequest.getMinDenomination(), offerCatalogRequest.getMaxDenomination()), 
					OfferErrorCodes.MIN_GREATER_THAN_MAX, resultResponse);
			Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(offerCatalogRequest.getIncrementalValue()), 
					OfferErrorCodes.BLANK_INCREMENTAL_VALUE, resultResponse);
			
		}
		
		validateVoucherInfo(offerCatalogRequest, resultResponse);
		return Checks.checkNoErrors(resultResponse);
		
	}
	
	/**
	 * 
	 * @param offerCatalogRequest
	 * @param resultResponse
	 * @return value to indicate deal offer parameters are valid
	 * 
	 */
	private static boolean checkValidDealVoucher(OfferCatalogDto offerCatalogRequest,
			ResultResponse resultResponse) {

		Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(offerCatalogRequest.getVatPercentage()), 
				OfferErrorCodes.INVALID_VAT_PERCENATEG, resultResponse);
		
		if(CollectionUtils.isNotEmpty(offerCatalogRequest.getSubOffer())) {
			
			for(SubOfferDto subOfferDto : offerCatalogRequest.getSubOffer()) {
				
				Responses.setResponseWithMessageAfterConditionCheck(Checks.checkUniqueSubOfferId(subOfferDto.getSubOfferId(), offerCatalogRequest.getSubOffer()), 
						OfferErrorCodes.SUBOFFER_ID_NOT_UNIQUE, subOfferDto.getSubOfferId(), resultResponse);
				Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(subOfferDto.getNewCost()), 
						OfferErrorCodes.INVALID_SUBOFFER_NEWDIRHAM_VALUE, resultResponse);
				Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(subOfferDto.getOldCost()), 
						OfferErrorCodes.INVALID_SUBOFFER_OLDDIRHAM_VALUE, resultResponse);
				Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(subOfferDto.getSubOfferTitleEn()), 
						OfferErrorCodes.INVALID_SUBOFFER_TITLEEN, resultResponse);
			}

		}
		
		validateVoucherInfo(offerCatalogRequest, resultResponse);
		return Checks.checkNoErrors(resultResponse);
		
	}
	
	/**
	 * 
	 * @param date
	 * @return value to indicate date is in valid format
	 * @throws ParseException
	 */
	private static boolean validateDate(String date) throws ParseException {
		
		DateFormat sdf = new SimpleDateFormat(OfferConstants.DATE_FORMAT.get());
        sdf.setLenient(false);
        
        try {
        	
        	if(!StringUtils.isEmpty(date)) {
        		sdf.parse(date);
    		}
        	
        } catch (ParseException e) {
			return false;
		} 
		return true;
	}
	
	/**
	 * 
	 * @param offerCatalogRequest
	 * @param offer
	 * @param resultResponse
	 * @return value to indicate if update request is valid
	 */
	public static boolean validateUpdateRequest(OfferCatalogDto offerCatalogRequest, OfferCatalog offer,
			ResultResponse resultResponse) {
		
		if (!StringUtils.equals(offerCatalogRequest.getOfferCode(), offer.getOfferCode())) {
			
			resultResponse.addErrorAPIResponse(OfferErrorCodes.OFFER_CODE_CANNOT_BE_CHANGED.getIntId(),
					OfferErrorCodes.OFFER_CODE_CANNOT_BE_CHANGED.getMsg());
		
		} else if (!StringUtils.equals(offerCatalogRequest.getOfferTypeId(), offer.getOfferType().getOfferTypeId())) {
			
			resultResponse.addErrorAPIResponse(OfferErrorCodes.OFFER_TYPE_CANNOT_BE_CHANGED.getIntId(),
					OfferErrorCodes.OFFER_TYPE_CANNOT_BE_CHANGED.getMsg());
			
		} 
		
		return Checks.checkNoErrors(resultResponse);
	}
	
	/**
	 * 
	 * @param purchaseRequest
	 * @param purchaseResultResponse
	 * @return value to indicate if purchase request is valid 
	 */
	public static boolean validateOfferPurchaseRequest(PurchaseRequestDto purchaseRequest, PurchaseResultResponse purchaseResultResponse) {
	
		if(purchaseRequest.getCouponQuantity()==null) {
			purchaseResultResponse.addErrorAPIResponse(OfferErrorCodes.COUPON_QUANTITY_EMPTY.getIntId(),
					OfferErrorCodes.COUPON_QUANTITY_EMPTY.getMsg());
		} 
		
		return null==purchaseResultResponse.getApiStatus().getErrors() 
				|| purchaseResultResponse.getApiStatus().getErrors().isEmpty();
		
	}

	public static boolean validateTransactionRequest(TransactionRequestDto transactionRequest,
			ResultResponse resultResponse) {
		
		return Responses.setResponseAfterConditionCheck(Checks.checkMembershipCodePresentWhenAccountNumberPresent(transactionRequest), OfferErrorCodes.MEMBERSHIP_CODE_WITH_ACCOUNT_NUMBER, resultResponse);
	}
	
	/**
	 * 
	 * @param offerReference
	 * @param offerType
	 * @param resultResponse
	 * @return
	 */
	public static boolean validateOfferType(OfferReferences offerReference, OfferType offerType, ResultResponse resultResponse) {
		
		if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(offerType), OfferErrorCodes.OFFERTYPE_NOT_EXISTING, resultResponse)){
			
			offerReference.setOfferType(offerType);
		}
		
		return Checks.checkNoErrors(resultResponse);
	}
	
	/**
     * 
     * @param category
     * @param subCategory
     * @param resultResponse
     * @return
     */
    public static boolean validateCategoryAndSubCategory(OfferReferences offerReference, List<Category> categoryList, String categoryId, String subCategoryId, ResultResponse resultResponse) {
		
    	Category category = FilterValues.findCategoryInCategoryList(categoryList, Predicates.sameCategoryId(categoryId));
    	Category subCategory = FilterValues.findCategoryInCategoryList(categoryList, Predicates.sameCategoryId(subCategoryId));
    	
		if(!ObjectUtils.isEmpty(category)
		&& Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(subCategory), OfferErrorCodes.SUBCATEGORY_NOT_EXISTING, resultResponse)
		&& Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(subCategory.getParentCategory()), OfferErrorCodes.SUBCATEGORY_NOT_VALID, resultResponse)
		&& Responses.setResponseAfterConditionCheck(StringUtils.equals(subCategory.getParentCategory().getCategoryId(), category.getCategoryId()), OfferErrorCodes.SUBCATEGORY_NOT_UNDER_CATEGORY, resultResponse)) {
			
			offerReference.setCategory(category);
			offerReference.setSubCategory(subCategory);
			
		}
			
		return Checks.checkNoErrors(resultResponse);	
	}
    
    /**
     * 
     * @param merchant
     * @param offerCatalogRequest
     * @param resultResponse
     * @return
     */
    public static boolean validateMerchant(OfferReferences offerReferences, Merchant merchant, OfferCatalogDto offerCatalogRequest, ResultResponse resultResponse) {
		
	     if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(merchant), OfferErrorCodes.INVALID_MERCHANT_CODE, resultResponse)) {
        	
	    	offerReferences.setMerchant(merchant); 
            offerCatalogRequest.setPartnerCode(merchant.getPartner());
			
			if(!ObjectUtils.isEmpty(merchant.getMerchantName())) {
				
				offerCatalogRequest.setRedemptionCodeDescriptionEn(merchant.getMerchantName().getMerchantNameEn() + OfferConstants.VOUCHER_EN.get());
				offerCatalogRequest.setAccrualCodeDescriptionEn(merchant.getMerchantName().getMerchantNameEn() + OfferConstants.VOUCHER_EN.get());
				offerCatalogRequest.setPointsAccrualCodeDescriptionEn(merchant.getMerchantName().getMerchantNameEn() + OfferConstants.VOUCHER_EN.get());
				offerCatalogRequest.setRedemptionCodeDescriptionAr(merchant.getMerchantName().getMerchantNameAr() + OfferConstants.VOUCHER_AR.get());
				offerCatalogRequest.setAccrualCodeDescriptionAr(merchant.getMerchantName().getMerchantNameAr() + OfferConstants.VOUCHER_AR.get());
				offerCatalogRequest.setPointsAccrualCodeDescriptionAr(merchant.getMerchantName().getMerchantNameAr() + OfferConstants.VOUCHER_AR.get());
			}
        	
        	
        }
        
        return Checks.checkNoErrors(resultResponse);
    
    }
    
    /**
     * 
     * @param offerReferences
     * @param storeCodes
     * @param stores
     * @param resultResponse
     * @return
     */
    public static boolean validateStores(OfferReferences offerReferences, List<String> storeCodes, List<Store> stores, ResultResponse resultResponse) {
		
		if(Responses.setResponseAfterConditionCheck(CollectionUtils.isNotEmpty(stores), OfferErrorCodes.CONFIGURED_STORES_NOT_FOUND, resultResponse)
		&& CollectionUtils.isNotEmpty(storeCodes)) {
			
			storeCodes.forEach(s->Responses.setResponseWithMessageAfterConditionCheck(!ObjectUtils
					.isEmpty(FilterValues.findAnyStoreInList(stores, Predicates.sameStoreCode(s))), 
					OfferErrorCodes.INVALID_STORE_CODE, s, resultResponse));
		    
			if(Checks.checkNoErrors(resultResponse)) {
				
				offerReferences.setStore(stores);
			}
			
		}
    	
    	return Checks.checkNoErrors(resultResponse);
	}
    
    /**
     * 
     * @param offerReferences
     * @param fetchedDenominationList
     * @param configuredDenominationList
     * @param limit
     * @param dynamicDenominationForCashVoucher
     * @param resultResponse
     * @return
     */
    public static boolean validateDenominationsAndDenominationLimit(OfferReferences offerReferences, List<Denomination> fetchedDenominationList, List<Integer> configuredDenominationList, List<LimitDto> limit, boolean dynamicDenominationForCashVoucher, ResultResponse resultResponse) {
		
    	boolean denominationLimitConfigured = Checks.checkDenominationLimitConfigured(limit);
    	offerReferences.setDenominations(fetchedDenominationList);
		
    	if(denominationLimitConfigured) {
			
			if(!CollectionUtils.isEmpty(configuredDenominationList)) {
				
				Responses.setResponseAfterConditionCheck(!CollectionUtils.isEmpty(fetchedDenominationList), OfferErrorCodes.NO_DENOMINATIONS_PRESENT, resultResponse);
				checkDenominationLimit(limit, configuredDenominationList, fetchedDenominationList, dynamicDenominationForCashVoucher, denominationLimitConfigured, resultResponse);
				
			} else {
				
				Responses.setResponseAfterConditionCheck(denominationLimitConfigured, OfferErrorCodes.NO_DENOMINATION_CONFIGURED, resultResponse);
				
			}
			
		}
			
		return Checks.checkNoErrors(resultResponse);
		
	}
    
    /**
     * Checks the denomination limits are valid
     * @param limit
     * @param configuredDenominationList 
     * @param fetchedDenominationList
     * @param dynamicDenominationForCashVoucher
     * @param denominationLimitConfigured 
     * @param resultResponse
     */
    private static void checkDenominationLimit(List<LimitDto> limit, List<Integer> configuredDenominationList, List<Denomination> fetchedDenominationList,
			boolean dynamicDenominationForCashVoucher, boolean denominationLimitConfigured, ResultResponse resultResponse) {
		
    	if(Checks.checkNoErrors(resultResponse)) {
			
			List<Integer> filteredDenominationValueList = MapValues.mapDenominationDirhamValues(fetchedDenominationList);
			Checks.checkValidIntegerListValues(filteredDenominationValueList, configuredDenominationList, OfferErrorCodes.DENOMINATION_NOT_EXISTING, resultResponse);
			Responses.setResponseAfterConditionCheck(Checks.checkNotBooleanValue(dynamicDenominationForCashVoucher, denominationLimitConfigured), OfferErrorCodes.DYNAMIC_DENOMINATION_CONFIGURED, resultResponse);
			
			if(Checks.checkNoErrors(resultResponse) && !CollectionUtils.isEmpty(limit)) {
					
				for(LimitDto currentLimit : limit) {
					
					List<Integer> filteredOfferLimitDenominations = MapValues.mapDenominationValuesFromLimit(currentLimit.getDenominationLimit());
					Checks.checkDenominationLimitValues(filteredDenominationValueList, configuredDenominationList, filteredOfferLimitDenominations, resultResponse);
					List<Integer> filteredMemberLimitDenominations = MapValues.mapDenominationValuesFromLimit(currentLimit.getMemberDenominationLimit());
					Checks.checkDenominationLimitValues(filteredDenominationValueList, configuredDenominationList, filteredMemberLimitDenominations, resultResponse);
					List<Integer> filteredAccountLimitDenominations = MapValues.mapDenominationValuesFromLimit(currentLimit.getAccountDenominationLimit());
					Checks.checkDenominationLimitValues(filteredDenominationValueList, configuredDenominationList, filteredAccountLimitDenominations, resultResponse);
				}
					
			}
		
		 }
		
	}

	/**
     * 
     * @param customerSegmentsForLimit
     * @param customerSegmentValues
     * @param resultResponse
     * @return
     */
    public static boolean validateCustomerSegmentAndLimits(List<String> customerSegmentsForLimit, ListValuesDto customerSegmentValues, ResultResponse resultResponse) {
		
    	if(!ObjectUtils.isEmpty(customerSegmentValues)) {
			
			Checks.checkValidListValues(OffersListConstants.ELIGIBLE_CUSTOMER_SEGMENTS, customerSegmentValues.getEligibleTypes(), OfferErrorCodes.INVALID_CUSTOMER_SEGMENT, resultResponse);
			Checks.checkValidListValues(OffersListConstants.ELIGIBLE_CUSTOMER_SEGMENTS, customerSegmentValues.getExclusionTypes(), OfferErrorCodes.INVALID_CUSTOMER_SEGMENT, resultResponse);
				
		}
		
		if(Checks.checkNoErrors(resultResponse)  
		&& CollectionUtils.isNotEmpty(customerSegmentsForLimit)) {
			
			Checks.checkValidListValues(OffersListConstants.ELIGIBLE_CUSTOMER_SEGMENTS, customerSegmentsForLimit, OfferErrorCodes.INVALID_CUSTOMER_SEGMENT_IN_LIMIT, resultResponse);
			
			if(!ObjectUtils.isEmpty(customerSegmentValues)) {
				
				Checks.checkValidListValues(customerSegmentsForLimit, customerSegmentValues.getEligibleTypes(), OfferErrorCodes.CUSTOMER_SEGMENT_LIMIT_NOT_DEFINED, resultResponse);
			}
			
		}
		
		return Checks.checkNoErrors(resultResponse);
		
	}
    
    /**
     * 
     * @param customerTypeDetails
     * @param offerCatalogRequest
     * @param resultResponse
     * @return
     */
	public static boolean validateCustomerTypes(List<ParentChlidCustomer> customerTypeDetails,
			OfferCatalogDto offerCatalogRequest, ResultResponse resultResponse) {
		
        if(!ObjectUtils.isEmpty(offerCatalogRequest) 
        && !ObjectUtils.isEmpty(offerCatalogRequest.getCustomerTypes())
        && CollectionUtils.isNotEmpty(customerTypeDetails)) {
        		
    		validateEligibleCustomerType(offerCatalogRequest.getCustomerTypes().getEligibleTypes(), 
					customerTypeDetails, resultResponse);
			validateExclusionCustomerType(offerCatalogRequest.getCustomerTypes().getExclusionTypes(), 
					offerCatalogRequest.getCustomerTypes().getEligibleTypes(),
					customerTypeDetails, resultResponse);
			
		}					

		return Checks.checkNoErrors(resultResponse);	
		
	}
	
	/**
	 * 
	 * @param offerCatalogRequest
	 * @param customerTypeDetails
	 * @param resultResponse
	 * @description checks if eligible customer type list has valid values
	 */
	public static boolean validateEligibleCustomerType(List<String> eligibleCustomerTypes,
			List<ParentChlidCustomer> customerTypeDetails,
			ResultResponse resultResponse) {
		
		if(!CollectionUtils.isEmpty(eligibleCustomerTypes)) {
			
			for(String customerType : eligibleCustomerTypes) {
				
				Responses.setResponseWithMessageAfterConditionCheck(Checks.checkValidCustomerType(customerTypeDetails, customerType), OfferErrorCodes.INVALID_CUSTOMER_TYPE, customerType, resultResponse);
				
			}
			
		}
		
		return Checks.checkNoErrors(resultResponse);
		
	}

	/**
	 * 
	 * @param offerCatalogRequest
	 * @param customerTypeDetails
	 * @param resultResponse
	 * @description checks if exclusion customer type list have valid values
	 */
	public static boolean validateExclusionCustomerType(List<String> exclusionCustomerTypes, 
			List<String> eligibleCustomerTypes,
			List<ParentChlidCustomer> customerTypeDetails, ResultResponse resultResponse) {
		
		if(!CollectionUtils.isEmpty(exclusionCustomerTypes)) {
			
			for(String customerType : exclusionCustomerTypes) {
				
				Responses.setResponseWithMessageAfterConditionCheck(Checks.checkValidCustomerType(customerTypeDetails, customerType), OfferErrorCodes.INVALID_CUSTOMER_TYPE, customerType, resultResponse);
				Responses.setResponseWithMessageAfterConditionCheck(Checks.checkValidChildInList(customerTypeDetails, customerType, eligibleCustomerTypes), OfferErrorCodes.INVALID_EXCLUSION_TYPE, customerType, resultResponse);
				
			}
			
		}
		
		return Checks.checkNoErrors(resultResponse);
		
	}

	/**
	 * 
	 * @param eligibleOffersRequest
	 * @param offerCatalogResultResponse
	 * @return status to indicate eligible offer request is valid
	 */
	public static boolean validateEligibleOfferFilterRequest(EligibleOffersFiltersRequest eligibleOffersRequest,
			OfferCatalogResultResponse offerCatalogResultResponse) {
		
		return Responses.setResponseAfterConditionCheck(Checks.checkPageNumberAndPageLimitCombination(eligibleOffersRequest.getPage(), eligibleOffersRequest.getPageLimit()), OfferErrorCodes.PAGE_LIMIT_NUMBER_TOGETHER, offerCatalogResultResponse)
		    && Responses.setResponseAfterConditionCheck(Checks.checkLatitudeAndLongitudeCombination(eligibleOffersRequest.getLatitude(), eligibleOffersRequest.getLongitude()), OfferErrorCodes.LATITUDE_LONGITUDE_TOGETHER, offerCatalogResultResponse)
		    && Responses.setResponseAfterConditionCheck(Checks.checkValidPriceOrder(eligibleOffersRequest.getPriceOrder()), OfferErrorCodes.NOT_A_VALID_ORDER, offerCatalogResultResponse)
		    && Responses.setResponseAfterConditionCheck(Checks.checkValidOfferTypePreference(eligibleOffersRequest.getOfferTypePreference()), OfferErrorCodes.NOT_A_VALID_OFFER_TYPE_FOR_PRFERENCE, offerCatalogResultResponse)
		    && Responses.setResponseAfterConditionCheck(Checks.checkValidOfferItem(eligibleOffersRequest.getOfferType()), OfferErrorCodes.NOT_A_VALID_OFFER_TYPE_ITEM, offerCatalogResultResponse);
	}

	/**
	 * 
	 * @param voucherActionById
	 * @param offerTypeId 
	 * @param resultResponse
	 * @return status to indicate voucher action is valid
	 */
	public static boolean validateVoucherAction(VoucherAction voucherAction, ResultResponse resultResponse) {
		
		return Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(voucherAction), OfferErrorCodes.VOUCHER_ACTION_NOT_ALLOWED, resultResponse);
	}
	
    /**
     * 
     * @param offerReferences
     * @param paymentMethods
     * @param availablePaymentMethods
     * @param resultResponse
     * @return
     */
    public static boolean validatePaymentMethods(OfferReferences offerReferences, List<String> paymentMethods, List<PaymentMethod> availablePaymentMethods, ResultResponse resultResponse) {
		
		if(Responses.setResponseAfterConditionCheck(CollectionUtils.isNotEmpty(availablePaymentMethods), OfferErrorCodes.CONFIGURED_PAYMENT_METHODS_NOT_FOUND, resultResponse)
		&& CollectionUtils.isNotEmpty(paymentMethods)) {
			
			paymentMethods.forEach(s->Responses.setResponseWithMessageAfterConditionCheck(!ObjectUtils
					.isEmpty(FilterValues.findAnyPaymentMethodInList(availablePaymentMethods, Predicates.samePaymentMethodId(s))), 
					OfferErrorCodes.PAYMENT_METHOD_NOT_EXISTING, s, resultResponse));
		    
			if(Checks.checkNoErrors(resultResponse)) {
				
				offerReferences.setPaymentMethods(availablePaymentMethods);
			}
			
		}
    	
    	return Checks.checkNoErrors(resultResponse);
	}
}
