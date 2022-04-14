//package com.loyalty.marketplace.customer.segmentation.utils;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.util.ObjectUtils;
//
//import com.loyalty.marketplace.customer.segmentation.constants.CustomerSegmentErrorCodes;
//import com.loyalty.marketplace.customer.segmentation.constants.CustomerSegmentExceptionCodes;
//import com.loyalty.marketplace.customer.segmentation.constants.CustomerSegmentSuccessCodes;
//import com.loyalty.marketplace.customer.segmentation.helper.dto.CustomerSegmentExceptionInfo;
//import com.loyalty.marketplace.offers.constants.OfferConstants;
//import com.loyalty.marketplace.outbound.dto.ResultResponse;
//import com.loyalty.marketplace.utils.MarketplaceException;
//
//public class CustomerSegmentResponses {
//	
//	private static final Logger LOG = LoggerFactory.getLogger(CustomerSegmentResponses.class);
//	
//	CustomerSegmentResponses(){
//		
//	}
//	
//	/***
//	 * 
//	 * @param resultResponse(Response object)
//	 * @param errorResult(Required error response)
//	 * @param successResult(Required success response)
//	 * Sets error/success response as per presence/absence of errors
//	 * 
//	 */
//	public static void setResponse(ResultResponse resultResponse, CustomerSegmentErrorCodes errorResult,
//			CustomerSegmentSuccessCodes successResult) {
//		
//		
//		if(CustomerSegmentChecks.checkNoErrors(resultResponse)) {
//			
//			setSuccessResponse(resultResponse, successResult);
//			
//		} else {
//			
//			setErrorResponse(resultResponse, errorResult);
//		}
//		
//	}
//	
//	/**
//	 * 
//	 * @param resultResponse
//	 * @param errorResult
//	 * Sets error response
//	 * 
//	 */
//	public static void setErrorResponse(ResultResponse resultResponse, CustomerSegmentErrorCodes errorResult) {
//		
//		resultResponse.setResult(errorResult.getId(), errorResult.getMsg());
//		
//	}
//	
//	public static void setSuccessResponse(ResultResponse resultResponse, CustomerSegmentSuccessCodes successResult) {
//		
//		resultResponse.setResult(successResult.getId(), successResult.getMsg());
//		
//	}
//	
//	/**
//	 * 
//	 * @param resultResponse
//	 * @param error
//	 */
//	public static void addError(ResultResponse resultResponse, CustomerSegmentErrorCodes error) {
//		
//		resultResponse.addErrorAPIResponse(error.getIntId(), error.getMsg());
//		
//	}
//	
//	/**
//	 * 
//	 * @param resultResponse
//	 * @param error
//	 * @param message
//	 */
//    public static void addErrorWithMessage(ResultResponse resultResponse, CustomerSegmentErrorCodes error, String message) {
//		
//		resultResponse.addErrorAPIResponse(error.getIntId(), error.getMsg() + OfferConstants.MESSAGE_SEPARATOR.get() + message);
//		
//	}
//	
//    /**
//     * 
//     * @param resultResponse
//     * @param exception
//     */
//	public static void addExceptionError(ResultResponse resultResponse, CustomerSegmentExceptionCodes exception) {
//		
//		resultResponse.addErrorAPIResponse(exception.getIntId(), exception.getMsg()+ OfferConstants.REFER_LOGS.get());
//		
//	}
//	
//	/**
//	 * 
//	 * @param resultResponse
//	 * @param error
//	 * @param message
//	 */
//	public static void addExceptionWithMessage(ResultResponse resultResponse, CustomerSegmentExceptionCodes error, String message) {
//		
//		resultResponse.addErrorAPIResponse(error.getIntId(), error.getMsg() + OfferConstants.MESSAGE_SEPARATOR.get() + message);
//		
//	}
//	
//	/**
//	 * 
//	 * @param conditionCheckPass
//	 * @param errorResult
//	 * @param resultResponse
//	 * @return
//	 */
//	public static boolean setResponseAfterConditionCheck(boolean conditionCheckPass, CustomerSegmentErrorCodes errorResult, ResultResponse resultResponse) {
//		
//		if(!conditionCheckPass) {
//			
//			addError(resultResponse, errorResult);
//			
//		}
//		
//		return CustomerSegmentChecks.checkNoErrors(resultResponse);
//		
//	}
//	
//	/**
//	 * 
//	 * @param conditionCheckPass
//	 * @param errorResult
//	 * @param message
//	 * @param resultResponse
//	 * @return
//	 */
//	public static boolean setResponseWithMessageAfterConditionCheck(boolean conditionCheckPass, CustomerSegmentErrorCodes errorResult, String message, ResultResponse resultResponse) {
//		
//		if(!conditionCheckPass) {
//			
//			addErrorWithMessage(resultResponse, errorResult, message);
//			
//		}
//		
//		return CustomerSegmentChecks.checkNoErrors(resultResponse);
//		
//	}
//	
//	/**
//	 * 
//	 * @param resultResponse
//	 * @param exceptionInfo
//	 */
//	public static void setResponseAfterException(ResultResponse resultResponse, CustomerSegmentExceptionInfo exceptionInfo) {
//		
//		String logMessage = null;
//		
//		if(!ObjectUtils.isEmpty(exceptionInfo.getMarketplaceException())) {
//			
//			logMessage = exceptionInfo.getMarketplaceException().getClassName()
//	                + OfferConstants.COMMA_OPERATOR.get()
//	                + exceptionInfo.getMarketplaceException().getMethodName()
//	                + OfferConstants.MESSAGE_SEPARATOR.get()
//	                + exceptionInfo.getMarketplaceException().getDetailMessage();
//			
//			addError(resultResponse, exceptionInfo.getMarketplaceError());
//			
//		} else if(!ObjectUtils.isEmpty(exceptionInfo.getException())){
//			
//			logMessage = exceptionInfo.getException().getClass() + exceptionInfo.getException().getMessage();
//		
//			addExceptionError(resultResponse, exceptionInfo.getExceptionError());
//		}
//		
//		String log = new MarketplaceException(exceptionInfo.getCurrentClassName(), 
//				exceptionInfo.getCurrentMethodName(), logMessage, 
//                exceptionInfo.getErrorResult())
//				.printMessage(); 
//		
//		LOG.error(OfferConstants.SINGLE_MESSAGE.get(), log);
//		
//	}
//	
//}
