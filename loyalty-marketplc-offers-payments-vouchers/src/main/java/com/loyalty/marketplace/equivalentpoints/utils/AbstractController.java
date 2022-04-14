package com.loyalty.marketplace.equivalentpoints.utils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.http.ResponseEntity;

import com.loyalty.marketplace.equivalentpoints.constant.ApplicationConstants;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.ApiError;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.ApiStatus;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.CommonApiStatus;

public interface AbstractController {

	default ResponseEntity<CommonApiStatus> getSuccessStatus(String externalTransactionId, String message,
			Object result) {
		ApiStatus apiStatus = new ApiStatus(externalTransactionId, message, ApplicationConstants.STATUS_SUCCESS, 0,
				null);
		return ResponseEntity.ok(new CommonApiStatus(apiStatus, result));
	}

	default ResponseEntity<CommonApiStatus> getErrorStatus(String externalTransactionId, String errorMessage,
			Integer errorCode, Object result) {
		List<ApiError> errors = new ArrayList<>();
		ApiError error = new ApiError(errorCode, errorMessage);
		errors.add(error);
		ApiStatus apiStatus = new ApiStatus(externalTransactionId, errorMessage, ApplicationConstants.STATUS_ERROR, 1,
				errors);
		return ResponseEntity.ok(new CommonApiStatus(apiStatus, result));
	}

	default ResponseEntity<CommonApiStatus> getBlockErrorStatus(String externalTransactionId, String errorMessage,
			Integer errorCode, Object result) {
		List<ApiError> errors = new ArrayList<>();
		ApiError error = new ApiError(errorCode, errorMessage);
		errors.add(error);
		ApiStatus apiStatus = new ApiStatus(externalTransactionId, errorMessage, ApplicationConstants.STATUS_ERROR, 1,
				errors);
		return ResponseEntity.ok(new CommonApiStatus(apiStatus, result));

	}

	default boolean validateDto(Object partnerActivityDto, Validator validator, CommonApiStatus resultResponse) {
		Set<ConstraintViolation<Object>> violations = validator.validate(partnerActivityDto);
		if (violations.isEmpty()) {
			return true;
		} else {
			List<ApiError> errorList = new ArrayList<>();

			for (ConstraintViolation<Object> violation : violations) {
				ApiError error = new ApiError();
				if (violation.getMessage() != null) {
					error.setCode(Integer
							.parseInt((violation.getMessage().split(ApplicationConstants.VALIDATOR_DELIMITOR, 2)[0])));
					error.setMessage((violation.getMessage().split(ApplicationConstants.VALIDATOR_DELIMITOR, 2)[1]));
					errorList.add(error);
				} else {
					error.setCode(ErrorCodes.INVALID_PARAMETERS.getCode());
					error.setMessage(ErrorCodes.INVALID_PARAMETERS.getHttpStatus().toString());
				}
			}
			resultResponse.setBulkErrorAPIResponse(errorList);
			return false;
		}
	}

}
