package com.loyalty.marketplace.utils;

import java.util.List;

import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;

import com.loyalty.marketplace.inbound.dto.ContactPersonDto;

import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.stores.constants.StoreCodes;
import com.loyalty.marketplace.stores.inbound.dto.StoreDto;


public class StoreValidator extends MarketplaceValidator{
	
	public static boolean validateStoreCode(String storeCode, ResultResponse result) {
		if ((storeCode.length() <= 5 && storeCode.indexOf(' ') == -1)) {
			return true;
		} else {
			result.addErrorAPIResponse(StoreCodes.INVALID_STORE_CODE.getIntId(), StoreCodes.INVALID_STORE_CODE.getMsg());
			return false;
		}
	}

public static boolean validateCoordinates(String latitude, String longitude, ResultResponse resultResponse) {
	if(Double.valueOf(latitude) <-90 ||Double.valueOf(latitude) >90) {
		resultResponse.addErrorAPIResponse(null, longitude);
	}
	if(Double.valueOf(longitude) < -180 || Double.valueOf(longitude)  > 180) {
		resultResponse.addErrorAPIResponse(null, longitude);
	}
	if (resultResponse.getApiStatus().getErrors() != null && 
			resultResponse.getApiStatus().getErrors().isEmpty()) {
		return true;
	}
	return false;
}

public static boolean validateRequest(StoreDto storeDto, Validator validator, ResultResponse resultResponse) {
	validateStoreCode(storeDto.getStoreCode(), resultResponse);
	validateContactPersons(storeDto.getContactPersons(), resultResponse);
	if (resultResponse.getApiStatus().getErrors() != null && 
			resultResponse.getApiStatus().getErrors().isEmpty()) {
		return true;
	}
	return false;
}
	
}
