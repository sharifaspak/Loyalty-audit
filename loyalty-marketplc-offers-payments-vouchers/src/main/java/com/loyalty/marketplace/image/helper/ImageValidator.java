package com.loyalty.marketplace.image.helper;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.loyalty.marketplace.image.constants.ImageCodes;
import com.loyalty.marketplace.image.outbound.dto.GenerateMetaDataResponse;
import com.loyalty.marketplace.image.outbound.dto.ResultResponse;

public class ImageValidator {

	private ImageValidator() {}
	
	/**
	 * Method to validate input request parameters.
	 * @param imageRequest
	 * @param validator
	 * @param resultResponse
	 * @return
	 */
	public static boolean validateRequestParameters(Object imageRequest, Validator validator, ResultResponse resultResponse) {
		
		Set<ConstraintViolation<Object>> violations = validator.validate(imageRequest);
		if (violations.isEmpty()) {
			return true;
		} else {
			for (ConstraintViolation<Object> violation : violations) {
				if (violation.getMessage() != null) {
					resultResponse.addErrorAPIResponse(
							Integer.parseInt(
									(violation.getMessage().split(ImageCodes.VALIDATOR_DELIMITOR.getConstant(), 2)[0])),
							(violation.getMessage().split(ImageCodes.VALIDATOR_DELIMITOR.getConstant(), 2)[1]));
				} else {
					resultResponse.addErrorAPIResponse(ImageCodes.INVALID_PARAMETER.getIntId(),
							ImageCodes.INVALID_PARAMETER.getMsg());
				}
			}

			return false;
		}
		
	}
	
	/**
	 * Method to validate input request parameters for generate meta data API.
	 * @param imageRequest
	 * @param validator
	 * @param generateMetaDataResponse
	 * @return
	 */
	public static boolean validateMetaDataRequestParameters(Object imageRequest, Validator validator, GenerateMetaDataResponse generateMetaDataResponse) {
		
		Set<ConstraintViolation<Object>> violations = validator.validate(imageRequest);
		if (violations.isEmpty()) {
			return true;
		} else {
			for (ConstraintViolation<Object> violation : violations) {
				if (violation.getMessage() != null) {
					generateMetaDataResponse.addErrorAPIResponse(
							Integer.parseInt(
									(violation.getMessage().split(ImageCodes.VALIDATOR_DELIMITOR.getConstant(), 2)[0])),
							(violation.getMessage().split(ImageCodes.VALIDATOR_DELIMITOR.getConstant(), 2)[1]));
				} else {
					generateMetaDataResponse.addErrorAPIResponse(ImageCodes.INVALID_PARAMETER.getIntId(),
							ImageCodes.INVALID_PARAMETER.getMsg());
				}
			}

			return false;
		}
		
	}
	
}
