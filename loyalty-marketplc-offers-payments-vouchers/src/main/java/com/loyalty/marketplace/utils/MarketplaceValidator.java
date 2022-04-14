package com.loyalty.marketplace.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;


import com.loyalty.marketplace.outbound.dto.Errors;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

import com.loyalty.marketplace.constants.MarketPlaceCode;


public class MarketplaceValidator {
	
	protected MarketplaceValidator() {
		/*Prevent the instantiation of this class directly*/
	}
	
	public static boolean validPhoneNumber(String number) {
		if (((((number.startsWith("01") || number.startsWith("02") || number.startsWith("03") || number.startsWith("04")
				|| number.startsWith("06") || number.startsWith("07") || number.startsWith("09"))
				&& number.length() == 9) || (number.startsWith("800") && (number.length() >= 5 || number.length() <= 10))
				|| (number.startsWith("+971") && (number.length() == 13 || number.length() == 12)))
				&& number.indexOf(' ') == -1)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean validFaxNumber(String number) {
		if (((((number.startsWith("01") || number.startsWith("02") || number.startsWith("03") || number.startsWith("04")
				|| number.startsWith("06") || number.startsWith("07") || number.startsWith("09"))
				&& number.length() == 9) || (number.startsWith("800") && (number.length() >= 5 || number.length() <= 10))
				|| (number.startsWith("05") && number.length() != 10))
				&& number.indexOf(' ') == -1)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean validMobileNumber(String number) {
		if (((((number.startsWith("01") || number.startsWith("02") || number.startsWith("03") || number.startsWith("04")
				|| number.startsWith("06") || number.startsWith("07") || number.startsWith("09"))
				&& number.length() == 9) || (number.startsWith("800") && (number.length() >= 5 || number.length() <= 10))
				|| (number.startsWith("05") && number.length() != 10)
				|| (number.startsWith("+971") && (number.length() == 13 || number.length() == 12)))
				&& number.indexOf(' ') == -1)) {
			return true;
		} else {
			return false;
		}
	}

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

}
