package com.loyalty.marketplace.gifting.helper;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.loyalty.marketplace.gifting.constants.GiftingCodes;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

public class GiftingValidator {

	private GiftingValidator() {}
	
	public static boolean validateRequestParameters(Object imageRequest, Validator validator, ResultResponse resultResponse) {
		
		Set<ConstraintViolation<Object>> violations = validator.validate(imageRequest);
		if (violations.isEmpty()) {
			return true;
		} else {
			for (ConstraintViolation<Object> violation : violations) {
				if (violation.getMessage() != null) {
					resultResponse.addErrorAPIResponse(
							Integer.parseInt(
									(violation.getMessage().split(GiftingCodes.VALIDATOR_DELIMITOR.getConstant(), 2)[0])),
							(violation.getMessage().split(GiftingCodes.VALIDATOR_DELIMITOR.getConstant(), 2)[1]));
				} else {
					resultResponse.addErrorAPIResponse(GiftingCodes.INVALID_PARAMETER.getIntId(),
							GiftingCodes.INVALID_PARAMETER.getMsg());
				}
			}

			return false;
		}
		
	}
	
}
