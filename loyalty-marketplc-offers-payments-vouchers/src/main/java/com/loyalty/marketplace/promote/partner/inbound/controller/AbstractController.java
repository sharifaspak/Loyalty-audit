package com.loyalty.marketplace.promote.partner.inbound.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.loyalty.marketplace.constants.MarketPlaceCode;
import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.offers.member.management.outbound.dto.ApiError;
import com.loyalty.marketplace.offers.member.management.outbound.dto.ApiStatus;
import com.loyalty.marketplace.offers.member.management.outbound.dto.CommonApiStatus;

public interface AbstractController {

	default ResponseEntity<CommonApiStatus> getSuccessStatus(String externalTransactionId, String message,
			Object result) {
		ApiStatus apiStatus = new ApiStatus(externalTransactionId, message, RequestMappingConstants.STATUS_SUCCESS, 0,
				null);
		return ResponseEntity.ok(new CommonApiStatus(apiStatus, result));
	}

	default ResponseEntity<CommonApiStatus> getErrorStatus(String externalTransactionId, String errorMessage,
			Integer errorCode, Object result) {
		List<ApiError> errors = new ArrayList<>();
		ApiError error = new ApiError(errorCode, errorMessage);
		errors.add(error);
		ApiStatus apiStatus = new ApiStatus(externalTransactionId, errorMessage, RequestMappingConstants.STATUS_ERROR,
				1, errors);
		return ResponseEntity.ok(new CommonApiStatus(apiStatus, result));
	}

	default ResponseEntity<CommonApiStatus> getBadRequestStatus(String externalTransactionId, String errorMessage,
			List<ApiError> errors, Object result) {
		ApiStatus apiStatus = new ApiStatus(externalTransactionId, errorMessage, RequestMappingConstants.STATUS_ERROR,
				1, errors);
		return new ResponseEntity<CommonApiStatus>(new CommonApiStatus(apiStatus, result), HttpStatus.BAD_REQUEST);
	}
	
	default List<ApiError> getBeanValidatorErrors(Errors errors) {
	    List<FieldError> fieldErrors = errors.getFieldErrors();
	   
	    List<ApiError> errorList = new ArrayList<>();
	    for (FieldError fieldError : fieldErrors) {
	        ApiError error = new ApiError();
	        error.setCode(MarketPlaceCode.INVALID_PARAMETER.getIntId());
	        if(fieldError.getDefaultMessage() != null) {
	            error.setMessage(fieldError.getDefaultMessage());
	        }    else {
	            error.setMessage(MarketPlaceCode.INVALID_PARAMETER.getMsg());
	        }
	        errorList.add(error);
	    }
	    return errorList;
	}

}
