package com.loyalty.marketplace.gifting.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.drools.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import com.loyalty.marketplace.constants.MarketPlaceCode;
import com.loyalty.marketplace.gifting.constants.GiftingCodes;
import com.loyalty.marketplace.gifting.helper.dto.GiftingExceptionInfo;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OffersConfigurationConstants;
import com.loyalty.marketplace.offers.helper.dto.EligibilityInfo;
import com.loyalty.marketplace.offers.helper.dto.PurchaseCount;
import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.ApiError;
import com.loyalty.marketplace.offers.member.management.outbound.dto.ApiStatus;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;
import com.loyalty.marketplace.offers.outbound.dto.Eligibility;
import com.loyalty.marketplace.offers.outbound.dto.OfferCatalogResultResponseDto;
import com.loyalty.marketplace.offers.outbound.dto.RuleFailure;
import com.loyalty.marketplace.offers.utils.Checks;
import com.loyalty.marketplace.offers.utils.MapValues;
import com.loyalty.marketplace.offers.utils.Predicates;
import com.loyalty.marketplace.offers.utils.Utilities;
import com.loyalty.marketplace.outbound.dto.Errors;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.utils.MarketplaceException;

/**
 * 
 * @author jaya.shukla
 *
 */
public class GiftingResponses {
	
	private static final Logger LOG = LoggerFactory.getLogger(GiftingResponses.class);
	
	GiftingResponses(){
		
	}
	
	/***
	 * Sets error/success response as per presence/absence of errors
	 * @param resultResponse(Response object)
	 * @param errorResult(Required error response)
	 * @param successResult(Required success response)
	 * 
	 * 
	 */
	public static void setResponse(ResultResponse resultResponse, GiftingCodes errorResult,
			GiftingCodes successResult) {
		
		
		if(Checks.checkNoErrors(resultResponse)) {
			
			setSuccessResponse(resultResponse, successResult);
			
		} else {
			
			setErrorResponse(resultResponse, errorResult);
		}
		
	}
	
	/**
	 * 
	 * @param resultResponse
	 * @param succesResult
	 * @param errorResult
	 * @param successMessage
	 */
	public static void setResultWithMessage(ResultResponse resultResponse, GiftingCodes succesResult, GiftingCodes errorResult, String successMessage, String errorMessage) {
		
		if(CollectionUtils.isEmpty(resultResponse.getApiStatus().getErrors())) {
			
			resultResponse.setResult(succesResult.getId(), 
					!ObjectUtils.isEmpty(successMessage)
					? succesResult.getMsg()+successMessage
					: succesResult.getMsg());
			
		} else {
			
			resultResponse.setResult(errorResult.getId(), !ObjectUtils.isEmpty(errorMessage)
					? errorResult.getMsg()+errorMessage
					: errorResult.getMsg());
		}
	}
	
	/**
	 * Sets error response to result
	 * @param resultResponse
	 * @param errorResult
	 * 
	 */
	public static void setErrorResponse(ResultResponse resultResponse, GiftingCodes errorResult) {
		
		resultResponse.setResult(errorResult.getId(), errorResult.getMsg());
		
	}
	
	/**
	 * Sets success response to result
	 * @param resultResponse
	 * @param successResult
	 */
	public static void setSuccessResponse(ResultResponse resultResponse, GiftingCodes successResult) {
		
		resultResponse.setResult(successResult.getId(), successResult.getMsg());
		
	}
	
	/**
	 * Adds error to result
	 * @param resultResponse
	 * @param error
	 */
	public static void addError(ResultResponse resultResponse, GiftingCodes error) {
		
		resultResponse.addErrorAPIResponse(error.getIntId(), error.getMsg());
		
	}
	
	/**
	 * Add error with some message in result
	 * @param resultResponse
	 * @param error
	 * @param message
	 */
    public static void addErrorWithMessage(ResultResponse resultResponse, GiftingCodes error, String message) {
		
		resultResponse.addErrorAPIResponse(error.getIntId(), error.getMsg() + OfferConstants.MESSAGE_SEPARATOR.get() + message);
		
	}
	
    /**
     * Add exception message to result
     * @param resultResponse
     * @param exception
     * @param message 
     */
	public static void addExceptionError(ResultResponse resultResponse, GiftingCodes exception, String logId) {
		
		if(!StringUtils.isEmpty(logId)){
			
			resultResponse.addErrorAPIResponse(exception.getIntId(), exception.getMsg()+ OfferConstants.REFER_EXCEPTION_LOGS.get()+logId);
		
		} else {
			resultResponse.addErrorAPIResponse(exception.getIntId(), exception.getMsg()+ OfferConstants.REFER_LOGS.get());
		}
		
	}
	
	/**
	 * Add exception message with additional message to result
	 * @param resultResponse
	 * @param error
	 * @param message
	 */
	public static void addExceptionWithMessage(ResultResponse resultResponse, GiftingCodes error, String message) {
		
		resultResponse.addErrorAPIResponse(error.getIntId(), error.getMsg() + OfferConstants.MESSAGE_SEPARATOR.get() + message);
		
	}
	
	/**
	 * Set error response if errors present
	 * @param conditionCheckPass
	 * @param errorResult
	 * @param resultResponse
	 * @return error not present status
	 */
	public static boolean setResponseAfterConditionCheck(boolean conditionCheckPass, GiftingCodes errorResult, ResultResponse resultResponse) {
		
		if(!conditionCheckPass) {
			addError(resultResponse, errorResult);	
		}
		
		if(null != resultResponse) {
			return Checks.checkNoErrors(resultResponse);
		}
		
		return true;
	}
	
	/**
	 * Set error response with message if errors present
	 * @param conditionCheckPass
	 * @param errorResult
	 * @param message
	 * @param resultResponse
	 * @return error not present status
	 */
	public static boolean setResponseWithMessageAfterConditionCheck(boolean conditionCheckPass, GiftingCodes errorResult, String message, ResultResponse resultResponse) {
		
		if(!conditionCheckPass) {
			
			addErrorWithMessage(resultResponse, errorResult, message);
			
		}
		
		return Checks.checkNoErrors(resultResponse);
		
	}
	
	/**
	 * Set exception message when exception occurs
	 * @param resultResponse
	 * @param exceptionInfo
	 */
	public static void setResponseAfterException(ResultResponse resultResponse, GiftingExceptionInfo exceptionInfo) {
		
		String logMessage = null;
		
		if(!ObjectUtils.isEmpty(exceptionInfo.getMarketplaceException())) {
			
			logMessage = exceptionInfo.getMarketplaceException().getClassName()
	                + OfferConstants.COMMA_OPERATOR.get()
	                + exceptionInfo.getMarketplaceException().getMethodName()
	                + OfferConstants.MESSAGE_SEPARATOR.get()
	                + exceptionInfo.getMarketplaceException().getDetailMessage();
			
			if(!StringUtils.isEmpty(exceptionInfo.getExceptionLogId())) {
				
				addErrorWithMessage(resultResponse, exceptionInfo.getMarketplaceError(), exceptionInfo.getExceptionLogId());
			} else {
				
				addError(resultResponse, exceptionInfo.getMarketplaceError());
			}
			
		} else if(!ObjectUtils.isEmpty(exceptionInfo.getException())){
			
			logMessage = exceptionInfo.getException().getClass() + exceptionInfo.getException().getMessage();
		
			addExceptionError(resultResponse, exceptionInfo.getExceptionError(), exceptionInfo.getExceptionLogId());
		}
		
		String log = new MarketplaceException(exceptionInfo.getCurrentClassName(), 
				exceptionInfo.getCurrentMethodName(), logMessage, 
                exceptionInfo.getErrorResult())
				.printMessage(); 
		
		LOG.error(OfferConstants.SINGLE_MESSAGE.get(), log);
		
	}
	
	/**
	 * Set response after rest template call
	 * @param check
	 * @param apiStatus
	 * @param resultResponse
	 * @return  errors not present status
	 */
	public static boolean setResponseAfterServiceResponseCheck(boolean check, ApiStatus apiStatus,
			ResultResponse resultResponse) {
		
		if(!check) {
			
			resultResponse.setErrorAPIResponse(apiStatus.getStatusCode(), apiStatus.getMessage());
			
		}
		
		return Checks.checkNoErrors(resultResponse);
	}

	/**
	 * 
	 * @param resultResponse
	 * @return last error present in response object
	 */
	public static Errors getLastError(ResultResponse resultResponse) {
		
		return Checks.checkErrorsPresent(resultResponse)
			 ? resultResponse.getApiStatus().getErrors().get(resultResponse.getApiStatus().getErrors().size()-1)
			 : null;
	}

	/**
	 * Removes last error from response object
	 * @param resultResponse
	 */
	public static void removeLastError(ResultResponse resultResponse) {
		
	   if(Checks.checkErrorsPresent(resultResponse)) {
		   
		   resultResponse.getApiStatus().getErrors().remove( resultResponse.getApiStatus().getErrors().size() - 1 );
		   
		   if(Checks.checkNoErrors(resultResponse)) {
			   
			   resultResponse.getApiStatus().setStatusCode(MarketPlaceCode.STATUS_SUCCESS.getIntId());
			   resultResponse.getApiStatus().setOverallStatus(MarketPlaceCode.STATUS_SUCCESS.getMsg());
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
	public static Eligibility getMemberEligibility(PurchaseCount purchaseCount, EligibilityInfo eligibilityInfo, ResultResponse resultResponse) {
		
		Eligibility eligibility = null;
		
		if(!ObjectUtils.isEmpty(eligibilityInfo)) {
			
			eligibility = new Eligibility(); 
			
			OfferCatalog offer = eligibilityInfo.getOffer();
			List<RuleFailure> failureStatus = null;
			
			if(Checks.checkCinemaOffer(offer.getRules())) {
				
				failureStatus = Checks.checkCustomerSegmentForCinemaOffers(eligibilityInfo.getRuleResult(),
						eligibilityInfo.getMemberDetails(), failureStatus, eligibility,
						offer.getCustomerSegments(), offer.getRules(), resultResponse);
				
				Checks.checkCinemaOfferDownloadLimit(purchaseCount, eligibilityInfo.getMemberDetails().getCustomerSegment(), resultResponse);	
					
			} else {
				
				List<String> customerSegmentNames = Utilities.intersection(eligibilityInfo.getMemberDetails().getCustomerSegment(),
						MapValues.mapCustomerSegmentInOfferLimits(offer.getLimit(), Predicates.isCustomerSegmentInLimits()));
				Checks.checkDownloadLimitLeft(eligibilityInfo.getOfferCounters(), offer, 0, eligibilityInfo, 
						null, resultResponse, customerSegmentNames);
				
			}
			
			failureStatus = setErrorMessage(OfferConstants.DOWNLOAD_LIMIT.get(), eligibility, failureStatus, resultResponse);
			eligibility.setFailureStatus(failureStatus);
			eligibility.setStatus(CollectionUtils.isEmpty(failureStatus));

		}
		        	
		return eligibility;
		
	}
	
	/**
	 * Sets error message in eligibility
	 * @param ruleName
	 * @param eligibility
	 * @param failureStatus
	 * @param resultResponse
	 * @return
	 */
	public static List<RuleFailure> setErrorMessage(String ruleName, Eligibility eligibility, List<RuleFailure> failureStatus, ResultResponse resultResponse) {
		
		if(Checks.checkErrorsPresent(resultResponse)) {
			
			failureStatus = !CollectionUtils.isEmpty(eligibility.getFailureStatus())
				      ? eligibility.getFailureStatus()
				      : new ArrayList<>(1);
			Errors errorResult = getLastError(resultResponse);
			String message = !ObjectUtils.isEmpty(errorResult)
					? errorResult.getMessage()
					: null;
			failureStatus.add(new RuleFailure(ruleName, message));
			removeAllErrors(resultResponse);
			
		}
		
		return failureStatus;
	}

	/**
	 * Sets spent amount and points to zero if not present in purchase request
	 * @param purchaseRequest
	 */
	public static void setDefaultPurchasesValues(PurchaseRequestDto purchaseRequest) {
		
		if(ObjectUtils.isEmpty(purchaseRequest.getSpentPoints())) {
			purchaseRequest.setSpentPoints(OffersConfigurationConstants.ZERO_INTEGER);
		}
		if(ObjectUtils.isEmpty(purchaseRequest.getSpentAmount())) {
			purchaseRequest.setSpentAmount(OffersConfigurationConstants.ZERO_DOUBLE);
		}
		
	}

	/**
	 * Removes all errors present in response object
	 * @param resultResponse
	 */
	public static void removeAllErrors(ResultResponse resultResponse) {
		
		if(Checks.checkErrorsPresent(resultResponse)){
			
			while(Checks.checkErrorsPresent(resultResponse)) {
				
				removeLastError(resultResponse);
				
			}
			
		}
		
	}
	
	/**
	 * Add all the errors after the service rest call
	 * @param resultResponse
	 * @param apiStatus
	 * @return
	 */
	public static boolean addServiceCallErrors(ResultResponse resultResponse, ApiStatus apiStatus) {
		
		if(!ObjectUtils.isEmpty(apiStatus) && !CollectionUtils.isEmpty(apiStatus.getErrors())) {
			
			for(ApiError apiError : apiStatus.getErrors())
			{
				resultResponse.addErrorAPIResponse( apiError.getCode(), apiError.getMessage());
			}
			
		}
		return Checks.checkNoErrors(resultResponse);
		
	}
	
	/**
	 * Sets status in response
	 * @param resultResponse
	 * @param status
	 */
	public static void setResultStatus(ResultResponse resultResponse, Object status) {
		
		if(!ObjectUtils.isEmpty(resultResponse)) {
			
			resultResponse.setResultStatus(status);
		}
		
	}
	
	/**
	 * 
	 * @param weeklyPurchaseCount 
	 * @param referenceDetails
	 * @param offerId
	 * @return eligibility status for offer response for eligible offer
	 */
	public static void getMemberEligibility(PurchaseCount purchaseCount, OfferCatalogResultResponseDto offerCatalogDto, EligibilityInfo eligibilityInfo, ResultResponse resultResponse) {
		
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
				Checks.checkDownloadLimitLeftForOfferEligibility(eligibilityInfo, customerSegmentNames, resultResponse);
			}
			
			offerCatalogDto.getEligibility().setFailureStatus(GiftingResponses.setErrorMessage(OfferConstants.DOWNLOAD_LIMIT.get(), 
					offerCatalogDto.getEligibility(), offerCatalogDto.getEligibility().getFailureStatus(), resultResponse));
			offerCatalogDto.getEligibility().setStatus(CollectionUtils.isEmpty(offerCatalogDto.getEligibility().getFailureStatus()));
		  
		}	
	}

}
