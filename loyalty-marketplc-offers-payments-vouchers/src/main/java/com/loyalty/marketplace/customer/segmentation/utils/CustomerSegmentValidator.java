//package com.loyalty.marketplace.customer.segmentation.utils;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
//import javax.validation.ConstraintViolation;
//import javax.validation.Validator;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.util.CollectionUtils;
//import org.springframework.util.ObjectUtils;
//
//import com.loyalty.marketplace.constants.MarketPlaceCode;
//import com.loyalty.marketplace.customer.segmentation.constants.CustomerSegmentConstants;
//import com.loyalty.marketplace.customer.segmentation.constants.CustomerSegmentErrorCodes;
//import com.loyalty.marketplace.customer.segmentation.input.dto.CustomerSegmentRequestDto;
//import com.loyalty.marketplace.customer.segmentation.input.dto.DateValuesDto;
//import com.loyalty.marketplace.customer.segmentation.input.dto.DoubleValuesDto;
//import com.loyalty.marketplace.customer.segmentation.input.dto.IntegerValuesDto;
//import com.loyalty.marketplace.customer.segmentation.input.dto.ListValuesDto;
//import com.loyalty.marketplace.customer.segmentation.input.dto.PurchaseValuesDto;
//import com.loyalty.marketplace.outbound.dto.Errors;
//import com.loyalty.marketplace.outbound.dto.ResultResponse;
//import com.loyalty.marketplace.utils.MarketplaceValidator;
//
//public class CustomerSegmentValidator extends MarketplaceValidator {
//	
//	/**
//	 * 
//	 * @param marketPlaceDto
//	 * @param validator
//	 * @param resultResponse
//	 * @return value to indicate validation of do as per annotations
//	 */
//	public static boolean validateDto(Object marketPlaceDto, Validator validator,
//			ResultResponse resultResponse) {
//		Set<ConstraintViolation<Object>> violations = validator.validate(marketPlaceDto);
//		if (violations.isEmpty()) {
//			return true;
//		} else {
//			List<Errors> errorList=new ArrayList<>();
//			
//			for (ConstraintViolation<Object> violation : violations) {
//				 Errors error=new Errors();
//				 if (violation.getMessage()!=null) {
//					error.setCode(Integer.parseInt(
//							(violation.getMessage().split(MarketPlaceCode.VALIDATOR_DELIMITOR.getConstant(), 2)[0])));
//					error.setMessage((violation.getMessage().split(MarketPlaceCode.VALIDATOR_DELIMITOR.getConstant(),2)[1]));
//		         errorList.add(error);	
//				}else {
//					 error.setCode(MarketPlaceCode.INVALID_PARAMETER.getIntId());
//			         error.setMessage(MarketPlaceCode.INVALID_PARAMETER.getMsg());
//				}
//			}
//		resultResponse.setBulkErrorAPIResponse(errorList);
//		return false;
//		}
//	}
//	
//	/**
//	 * 
//	 * @param customerSegmentRequest
//	 * @param resultResponse
//	 * @return
//	 */
//	public static boolean validateCustomerSegment(CustomerSegmentRequestDto customerSegmentRequest,
//			ResultResponse resultResponse) {
//		
//		return validateAllListValues(customerSegmentRequest, resultResponse)
//			&& validateAllIntegerValues(customerSegmentRequest, resultResponse)
//			&& validateAllDateValues(customerSegmentRequest, resultResponse)
//			&& validateAllPurchaseValues(customerSegmentRequest, resultResponse);
//	}
//
//	/**
//	 * 
//	 * @param customerSegmentRequest
//	 * @param resultResponse
//	 * @return
//	 */
//	private static boolean validateAllPurchaseValues(CustomerSegmentRequestDto customerSegmentRequest,
//			ResultResponse resultResponse) {
//		
//		boolean status = true;
//		
//		if(!ObjectUtils.isEmpty(customerSegmentRequest)
//		&& !CollectionUtils.isEmpty(customerSegmentRequest.getPurchaseItems())) {
//			
//			for(PurchaseValuesDto purchaseValue : customerSegmentRequest.getPurchaseItems()) {
//				
//				if(!status) {
//					
//					break;
//					
//				} else {
//					
//					status = validateString(purchaseValue.getPaymentMethod(), "payment method", resultResponse)
//						  && validateString(purchaseValue.getPurchaseItem(), "purchase item", resultResponse)	
//						  && validateDoubleValue(purchaseValue.getPurchaseAmount(), "purchase amount", resultResponse)
//						  && validateDoubleValue(purchaseValue.getSpentAmount(), "spent amount", resultResponse)
//						  && validateIntegerValue(purchaseValue.getSpentPoints(), "spent amount", resultResponse)
//						  && validateDateValue(purchaseValue.getPurchaseDate(), "purchase date", resultResponse);
//				}
//				
//			}
//			
//		}
//		
//		return status;
//	}
//	
//	/**
//	 * 
//	 * @param totalAccountPoints
//	 * @param string
//	 * @param resultResponse
//	 * @return
//	 */
//	private static boolean validateDoubleValue(DoubleValuesDto doubleValue, String variable,
//			ResultResponse resultResponse) {
//		
//		return CustomerSegmentResponses.setResponseWithMessageAfterConditionCheck(CustomerSegmentChecks.checkValidOperation(doubleValue.getOperation()), CustomerSegmentErrorCodes.INVALID_OPERATION, variable + CustomerSegmentConstants.MESSAGE_SEPARATOR.get() + doubleValue.getOperation(), resultResponse)
//				&& validateDoubleRangeValues(doubleValue, variable, resultResponse)
//				&& validateDoubleExactValues(doubleValue, variable, resultResponse);
//			
//	}
//	
//	/**
//	 * 
//	 * @param dateValue
//	 * @param variable
//	 * @param resultResponse
//	 * @return
//	 */
//	private static boolean validateDoubleRangeValues(DoubleValuesDto doubleValue, String variable,
//			ResultResponse resultResponse) {
//		
//		return !CustomerSegmentChecks.checkRangeOperation(doubleValue.getOperation())
//			|| (CustomerSegmentResponses.setResponseWithMessageAfterConditionCheck(!ObjectUtils.isEmpty(doubleValue.getHighValue()), CustomerSegmentErrorCodes.HIGH_VALUE_NOT_PRESENT, variable , resultResponse)
//			&&	CustomerSegmentResponses.setResponseWithMessageAfterConditionCheck(!ObjectUtils.isEmpty(doubleValue.getLowValue()), CustomerSegmentErrorCodes.LOW_VALUE_NOT_PRESENT, variable , resultResponse));
//	}
//	
//	/**
//	 * 
//	 * @param dateValue
//	 * @param variable
//	 * @param resultResponse
//	 * @return
//	 */
//	private static boolean validateDoubleExactValues(DoubleValuesDto doubleValue, String variable,
//			ResultResponse resultResponse) {
//		
//		return !CustomerSegmentChecks.checkRangeOperation(doubleValue.getOperation())
//			|| CustomerSegmentResponses.setResponseWithMessageAfterConditionCheck(!ObjectUtils.isEmpty(doubleValue.getExactValue()), CustomerSegmentErrorCodes.EXACT_VALUE_NOT_PRESENT, variable , resultResponse);
//	}
//
//	/**
//	 * 
//	 * @param customerSegmentRequest
//	 * @param resultResponse
//	 * @return
//	 */
//	private static boolean validateAllDateValues(CustomerSegmentRequestDto customerSegmentRequest,
//			ResultResponse resultResponse) {
//		
//		return validateDateValue(customerSegmentRequest.getAccountStartDate(), "account start date", resultResponse)
//			&& validateDateValue(customerSegmentRequest.getDob(), "date of birth", resultResponse);
//	}
//
//	/**
//	 * 
//	 * @param accountStartDate
//	 * @param resultResponse
//	 * @return
//	 */
//	private static boolean validateDateValue(DateValuesDto dateValue, String variable, ResultResponse resultResponse) {
//		
//		return CustomerSegmentResponses.setResponseWithMessageAfterConditionCheck(CustomerSegmentChecks.checkValidOperation(dateValue.getOperation()), CustomerSegmentErrorCodes.INVALID_OPERATION, variable + CustomerSegmentConstants.MESSAGE_SEPARATOR.get() + dateValue.getOperation(), resultResponse)
//			&& validateDateRangeValues(dateValue, variable, resultResponse)
//			&& validateDateExactValues(dateValue, variable, resultResponse)
//			&& validateDateDurationValues(dateValue, variable, resultResponse);
//			
//	}
//
//	/**
//	 * 
//	 * @param dateValue
//	 * @param variable
//	 * @param resultResponse
//	 * @return
//	 */
//	private static boolean validateDateRangeValues(DateValuesDto dateValue, String variable,
//			ResultResponse resultResponse) {
//		
//		return !CustomerSegmentChecks.checkRangeOperation(dateValue.getOperation())
//			|| (CustomerSegmentResponses.setResponseWithMessageAfterConditionCheck(!ObjectUtils.isEmpty(dateValue.getHighValue()), CustomerSegmentErrorCodes.HIGH_VALUE_NOT_PRESENT, variable , resultResponse)
//			&&	CustomerSegmentResponses.setResponseWithMessageAfterConditionCheck(!ObjectUtils.isEmpty(dateValue.getLowValue()), CustomerSegmentErrorCodes.LOW_VALUE_NOT_PRESENT, variable , resultResponse));
//	}
//	
//	/**
//	 * 
//	 * @param dateValue
//	 * @param variable
//	 * @param resultResponse
//	 * @return
//	 */
//	private static boolean validateDateExactValues(DateValuesDto dateValue, String variable,
//			ResultResponse resultResponse) {
//		
//		return !CustomerSegmentChecks.checkRangeOperation(dateValue.getOperation())
//			|| CustomerSegmentResponses.setResponseWithMessageAfterConditionCheck(!ObjectUtils.isEmpty(dateValue.getExactValue()), CustomerSegmentErrorCodes.EXACT_VALUE_NOT_PRESENT, variable , resultResponse);
//	}
//	
//	/**
//	 * 
//	 * @param dateValue
//	 * @param variable
//	 * @param resultResponse
//	 * @return
//	 */
//	private static boolean validateDateDurationValues(DateValuesDto dateValue, String variable,
//			ResultResponse resultResponse) {
//		
//		return !dateValue.isDuration()
//			|| CustomerSegmentResponses.setResponseWithMessageAfterConditionCheck(CustomerSegmentChecks.checkValidUnit(dateValue.getUnit()), CustomerSegmentErrorCodes.INVALID_UNIT, variable , resultResponse);
//	}
//
//	/**
//	 * 
//	 * @param customerSegmentRequest
//	 * @param resultResponse
//	 * @return
//	 */
//	private static boolean validateAllIntegerValues(CustomerSegmentRequestDto customerSegmentRequest,
//			ResultResponse resultResponse) {
//		
//		return validateIntegerValue(customerSegmentRequest.getTotalAccountPoints(), "total account points", resultResponse)
//		    && validateIntegerValue(customerSegmentRequest.getTotalTierPoints(), "total tier points", resultResponse);
//	}
//
//	/**
//	 * 
//	 * @param totalAccountPoints
//	 * @param string
//	 * @param resultResponse
//	 * @return
//	 */
//	private static boolean validateIntegerValue(IntegerValuesDto integerValue, String variable,
//			ResultResponse resultResponse) {
//		
//		return CustomerSegmentResponses.setResponseWithMessageAfterConditionCheck(CustomerSegmentChecks.checkValidOperation(integerValue.getOperation()), CustomerSegmentErrorCodes.INVALID_OPERATION, variable + CustomerSegmentConstants.MESSAGE_SEPARATOR.get() + integerValue.getOperation(), resultResponse)
//				&& validateIntegerRangeValues(integerValue, variable, resultResponse)
//				&& validateIntegerExactValues(integerValue, variable, resultResponse);
//			
//	}
//	
//	/**
//	 * 
//	 * @param dateValue
//	 * @param variable
//	 * @param resultResponse
//	 * @return
//	 */
//	private static boolean validateIntegerRangeValues(IntegerValuesDto integerValue, String variable,
//			ResultResponse resultResponse) {
//		
//		return !CustomerSegmentChecks.checkRangeOperation(integerValue.getOperation())
//			|| (CustomerSegmentResponses.setResponseWithMessageAfterConditionCheck(!ObjectUtils.isEmpty(integerValue.getHighValue()), CustomerSegmentErrorCodes.HIGH_VALUE_NOT_PRESENT, variable , resultResponse)
//			&&	CustomerSegmentResponses.setResponseWithMessageAfterConditionCheck(!ObjectUtils.isEmpty(integerValue.getLowValue()), CustomerSegmentErrorCodes.LOW_VALUE_NOT_PRESENT, variable , resultResponse));
//	}
//	
//	/**
//	 * 
//	 * @param dateValue
//	 * @param variable
//	 * @param resultResponse
//	 * @return
//	 */
//	private static boolean validateIntegerExactValues(IntegerValuesDto dateValue, String variable,
//			ResultResponse resultResponse) {
//		
//		return !CustomerSegmentChecks.checkRangeOperation(dateValue.getOperation())
//			|| CustomerSegmentResponses.setResponseWithMessageAfterConditionCheck(!ObjectUtils.isEmpty(dateValue.getExactValue()), CustomerSegmentErrorCodes.EXACT_VALUE_NOT_PRESENT, variable , resultResponse);
//	}
//
//
//	/**
//	 * 
//	 * @param customerSegmentRequest
//	 * @param resultResponse
//	 * @return
//	 */
//	private static boolean validateAllListValues(CustomerSegmentRequestDto customerSegmentRequest,
//			ResultResponse resultResponse) {
//		
//		return validateListValues(customerSegmentRequest.getChannelId(), "channel id", resultResponse)
//			&& validateListValues(customerSegmentRequest.getTierLevel(), "tier level", resultResponse)
//			&& validateListValues(customerSegmentRequest.getGender(), "gender", resultResponse)
//			&& validateListValues(customerSegmentRequest.getNationality(), "nationality", resultResponse)
//			&& validateListValues(customerSegmentRequest.getNumberType(), "number type", resultResponse)
//			&& validateListValues(customerSegmentRequest.getEmailVerificationStatus(), "email verification status", resultResponse)
//			&& validateListValues(customerSegmentRequest.getAccountStatus(), "account status", resultResponse)
//			&& validateListValues(customerSegmentRequest.getDays(), "days", resultResponse)
//			&& validateListValues(customerSegmentRequest.getCustomerType(), "customer type", resultResponse)
//			&& validateListValues(customerSegmentRequest.getCobrandedCards(), "cobranded cards", resultResponse);
//	}
//	
//	/**
//	 * 
//	 * @param accountStatus
//	 * @param string
//	 * @param resultResponse
//	 * @return
//	 */
//	private static boolean validateListValues(ListValuesDto listValue, String variable,
//			ResultResponse resultResponse) {
//		
//		return ObjectUtils.isEmpty(listValue)
//			|| (validateListValue(listValue.getEligibleTypes(), variable, resultResponse)
//			 && validateListValue(listValue.getExclusionTypes(), variable, resultResponse));
//	}
//
//	/**
//	 * 
//	 * @param paymentMethod
//	 * @param string
//	 * @param resultResponse
//	 * @return
//	 */
//	private static boolean validateListValue(List<String> list, String variable, ResultResponse resultResponse) {
//		
//		boolean status = true;
//		
//		if(!CollectionUtils.isEmpty(list)) {
//			
//			for(String value : list) {
//				
//				status = validateString(value, variable, resultResponse);
//				
//				if(!status) {
//					
//					break;
//				}
//				
//			}
//			
//		}
//		
//		return status;
//	}
//
//	/**
//	 * 
//	 * @param value
//	 * @param variable
//	 * @param resultResponse
//	 * @return
//	 */
//	private static boolean validateString(String value, String variable, ResultResponse resultResponse) {
//		
//		return ObjectUtils.isEmpty(value)
//			|| CustomerSegmentResponses.setResponseWithMessageAfterConditionCheck(StringUtils.isBlank(value), CustomerSegmentErrorCodes.STRING_VALUE_BLANK, variable, resultResponse);
//	}
//
//    
//    
//	
//}
