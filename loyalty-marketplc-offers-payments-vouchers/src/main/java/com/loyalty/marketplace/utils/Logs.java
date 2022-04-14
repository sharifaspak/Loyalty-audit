package com.loyalty.marketplace.utils;

import java.util.Date;

import org.springframework.http.HttpEntity;

import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OfferExceptionCodes;
import com.loyalty.marketplace.utils.MarketplaceException;

/**
 * 
 * @author jaya.shukla
 *
 */
public class Logs {
	
	Logs(){
		
	}
	
	/**
	 * 
	 * @param className
	 * @param methodName
	 * @param exceptionClass
	 * @param exceptionMessage
	 * @param customException
	 * @return log statement for an exception
	 */
	public static String logForException(String className, String methodName, String exceptionClass, String exceptionMessage,
			OfferExceptionCodes customException) {
		
		return new MarketplaceException(className, methodName, exceptionClass + exceptionMessage,
				customException).printMessage();
	}
	
	/**
	 * 
	 * @param requestObject
	 * @return log statement for request
	 */
	public static String logForRequest(Object requestObject) {
		
		return OfferConstants.REQUEST_PARAMS.get() + requestObject;
	}
	
	/**
	 * 
	 * @param responseObject
	 * @return log statement for response
	 */
    public static String logForResponse(Object responseObject) {
		
		return OfferConstants.RESPONSE_PARAMS.get() + responseObject;
	}

    /**
     * 
     * @param variable
     * @param value
     * @return log statement for variable
     */
	public static String logForVariable(String variable, Object value) {
		
		return variable + OfferConstants.MESSAGE_SEPARATOR.get() + value;
	}

	/**
     * 
     * @param variable
     * @param value
     * @return log statement for entering controller
     */
	public static String logForEnteringControllerMethod(String className, String methodName) {
		
		return OfferConstants.DOUBLE_LINE.get() + OfferConstants.ENTERING.get()+ className 
			 + OfferConstants.DOUBLE_COLON.get() + methodName + OfferConstants.DOUBLE_COLON.get()
			 + new Date() + OfferConstants.DOUBLE_LINE.get();
	}
	
	/**
     * 
     * @param variable
     * @param value
     * @return log statement for leaving controller
     */
	public static String logForLeavingControllerMethod(String className, String methodName) {
		
		return OfferConstants.DOUBLE_LINE.get() + OfferConstants.EXITING.get()+ className 
			 + OfferConstants.DOUBLE_COLON.get() + methodName + OfferConstants.DOUBLE_COLON.get()
			 + new Date()  + OfferConstants.DOUBLE_LINE.get();
	}
	
	/**
	 * 
	 * @param className
	 * @param methodName
	 * @return log statement for entering any method
	 */
	public static String logForEnteringOtherMethod(String className, String methodName) {
		
		return OfferConstants.SINGLE_LINE.get() + OfferConstants.INSIDE.get()+ className 
			 + OfferConstants.MESSAGE_SEPARATOR.get() + methodName + OfferConstants.SINGLE_LINE.get();
	}
	
	/**
	 * 
	 * @param className
	 * @param methodName
	 * @return log statement for leaving any method
	 */
	public static String logForLeavingOtherMethod(String className, String methodName) {
		
		return OfferConstants.SINGLE_LINE.get() + OfferConstants.LEAVING.get()+ className 
			 + OfferConstants.MESSAGE_SEPARATOR.get() + methodName + OfferConstants.SINGLE_LINE.get();
	}
	
	/**
	 * 
	 * @param className
	 * @param methodName
	 * @return log statement for entering service method
	 */
	public static String logsForEnteringServiceMethod(String className, String methodName) {
		
		return OfferConstants.OTHER_LINE.get() + OfferConstants.INSIDE.get()+ className 
				 + OfferConstants.MESSAGE_SEPARATOR.get() + methodName + OfferConstants.OTHER_LINE.get();
	}
	
	/**
	 * 
	 * @param className
	 * @param methodName
	 * @return log statement for leaving service method
	 */
	public static String logsForLeavingServiceMethod(String className, String methodName) {
		
		return OfferConstants.OTHER_LINE.get() + OfferConstants.LEAVING.get()+ className 
				 + OfferConstants.MESSAGE_SEPARATOR.get() + methodName + OfferConstants.OTHER_LINE.get();
	}

	/**
	 * 
	 * @param url
	 * @return log statement for service url
	 */
	public static String logForServiceUrl(String url) {
		
		return OfferConstants.HITTING_URL.get()+ OfferConstants.MESSAGE_SEPARATOR.get() + url;
	}

	/**
	 * 
	 * @param requestObject
	 * @return log statement for service request
	 */
	public static String logForServiceRequest(Object requestObject) {
		
		return OfferConstants.SERVICE_REQUEST_PARAMS.get() + requestObject;
	}
	
	/**
	 * 
	 * @param requestObject
	 * @return log statement for service response
	 */
    public static String logForServiceResponse(Object responseObject) {
		
		return OfferConstants.SERVICE_RESPONSE_PARAMS.get() + responseObject;
	}

    /**
     * 
     * @param entity
     * @return log statement for service headers
     */
	public static String logForServiceHeaders(HttpEntity<?> entity) {
		
		return OfferConstants.SERVICE_HEADER_PARAMS.get() + entity;
	}

	/**
	 * 
	 * @param db
	 * @return log statement before making any db call
	 */
	public static String logBeforeHittingDB(String db) {
		
		return OfferConstants.CALL_TO.get() + db + OfferConstants.DB_AT.get() + new Date();
	}
	
	/**
	 * 
	 * @param db
	 * @return  log statement after making any db call
	 */
	public static String logAfterHittingDB(String db) {
		
		return OfferConstants.RESPONSE_FROM.get() + db + OfferConstants.DB_AT.get() + new Date();
	}
	
	
	/**
	 * 
	 * @param db
	 * @return log statement before making any db call to get record count
	 */
	public static String logBeforeGettingRecordCount(String db) {
		
		return OfferConstants.CALL_FOR_RECORD_COUNT_TO.get() + db + OfferConstants.DB_AT.get() + new Date();
	}
	
	/**
	 * 
	 * @param db
	 * @return  log statement after making any db call to get record count
	 */
	public static String logAfterGettingRecordCount(String db) {
		
		return OfferConstants.RESPONSE_FOR_RECORD_COUNT_FROM.get() + db + OfferConstants.DB_AT.get() + new Date();
	}
	
	/**
	 * 
	 * @param service
	 * @return log statement before making rest call
	 */
	public static String logBeforeHittingService(String service) {
		
		return OfferConstants.CALL_TO.get() + service + OfferConstants.SERVICE_AT.get() + new Date();
	}
	
	/**
	 * 
	 * @param service
	 * @return log statement after making rest call
	 */
	public static String logAfterHittingService(String service) {
		
		return OfferConstants.RESPONSE_FROM.get() + service + OfferConstants.SERVICE_AT.get() + new Date();
	}

	
	/***
	 * 
	 * @param string
	 * @return log statement for printing constant statement without any variables
	 */
	public static String logForConstantStatment(String statement) {
		
		return statement;
	}

	/***
	 * 
	 * @param string
	 * @param contextUpdatedDate
	 * @param string2
	 * @param dbUpdatedDate
	 * @return log statement for printing value of 2 variables
	 */
	public static String logForTwoVariables(String variable1, Object value1, String variable2,
			Object value2) {
		
		return variable1 + OfferConstants.MESSAGE_SEPARATOR.get() + value1 
			 + OfferConstants.COMMA_OPERATOR.get() + variable2 
			 + OfferConstants.MESSAGE_SEPARATOR.get() + value2;
	}
	

	
	
}
