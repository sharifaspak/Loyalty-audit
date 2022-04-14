package com.loyalty.marketplace.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.loyalty.marketplace.merchants.constants.MerchantCodes;
import com.loyalty.marketplace.merchants.inbound.dto.MerchantBillingRateDto;
import com.loyalty.marketplace.merchants.inbound.dto.MerchantDto;
import com.loyalty.marketplace.merchants.inbound.dto.MerchantImageRequestDto;
import com.loyalty.marketplace.merchants.outbound.database.repository.RateTypeRepository;
import com.loyalty.marketplace.outbound.dto.Errors;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

public class MerchantValidator extends MarketplaceValidator {
	
	@Autowired
	RateTypeRepository rateTypeRepository;

	public static boolean validateMerchantCode(String merchantCode, ResultResponse result) {
		if ((merchantCode.length() <= 5 && merchantCode.indexOf(' ') == -1)) {
			return true;
		} else {
			result.addErrorAPIResponse(MerchantCodes.INVALID_MERCHANT_CODE.getIntId(),
					merchantCode + ": " + MerchantCodes.INVALID_MERCHANT_CODE.getMsg());
			return false;
		}
	}
	
	public static boolean validateBillingRateDate(List<MerchantBillingRateDto> merchantBillingRateDto, ResultResponse result) {
		if(null==merchantBillingRateDto) {
			return true;
		}
		DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false);
        try {
        	for(MerchantBillingRateDto billingRateDto : merchantBillingRateDto) {
        		if(null != billingRateDto.getRateType() && !StringUtils.isEmpty(billingRateDto.getStartDate())) {
        		sdf.parse(billingRateDto.getStartDate());
        		}
        		if(null != billingRateDto.getRateType() && !StringUtils.isEmpty(billingRateDto.getEndDate())) {
        			sdf.parse(billingRateDto.getEndDate());
        		}
        	}
        } catch (ParseException e) {
        	result.addErrorAPIResponse(MerchantCodes.INVALID_DATE.getIntId(), MerchantCodes.INVALID_DATE.getMsg());
			return false;
        } 
        return true;
	}
	
	public static boolean validateBillingRate(List<MerchantBillingRateDto> discountBillingRates,
			ResultResponse resultResponse) {
		for (MerchantBillingRateDto discountBillingRate : discountBillingRates) {
			if (null != discountBillingRate.getRateType() && null == discountBillingRate.getRate()) {
				resultResponse.addErrorAPIResponse(MerchantCodes.RATE_MANDATORY_WHEN_RATE_TYPE_PASSED.getIntId(),
						MerchantCodes.RATE_MANDATORY_WHEN_RATE_TYPE_PASSED.getMsg());
			}
			if (null != discountBillingRate.getRateType() && null != discountBillingRate.getRate() && discountBillingRate.getRate() <= 0) {
				resultResponse.addErrorAPIResponse(MerchantCodes.INVALID_PARAMETERS_RATE_ZERO_OR_LESS.getIntId(),
						discountBillingRate.getRate() + ":"
								+ MerchantCodes.INVALID_PARAMETERS_RATE_ZERO_OR_LESS.getMsg());
			}
		}
		if (resultResponse.getApiStatus().getErrors() == null || 
				resultResponse.getApiStatus().getErrors().isEmpty()) {
			return true;
		}
		return false;
	}
	
	public static boolean validateMerchantImage(MerchantImageRequestDto merchantImageRequestDto, Validator validator,
			ResultResponse resultResponse) {
		Set<ConstraintViolation<MerchantImageRequestDto>> violations = validator.validate(merchantImageRequestDto);
		if (violations.isEmpty()) {
			return true;
		} else {
			List<Errors> errorList = new ArrayList<>();

			for (ConstraintViolation<MerchantImageRequestDto> violation : violations) {
				Errors error = new Errors();
				error.setCode(MerchantCodes.INVALID_PARAMETERS.getIntId());
				error.setMessage(violation.getMessage());
				errorList.add(error);
			}
			resultResponse.setBulkErrorAPIResponse(errorList);
			return false;
		}
	}
	
	public static boolean validateRequest(MerchantDto merchantDto, Validator validator, ResultResponse resultResponse) {
		validateMerchantCode(merchantDto.getMerchantCode(), resultResponse);
		validateContactPersons(merchantDto.getContactPersons(), resultResponse);
		
		if(null != merchantDto.getDiscountBillingRates()) {
			validateBillingRateDate(merchantDto.getDiscountBillingRates(), resultResponse);
			validateBillingRate(merchantDto.getDiscountBillingRates(), resultResponse);
		}
		
		return resultResponse.getApiStatus().getErrors().isEmpty() ? true : false;
	}
}
