package com.loyalty.marketplace.banners.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

import com.loyalty.marketplace.banners.constants.BannerCodes;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

public class BannerUtils {
	
	BannerUtils(){
		
	}
	
	/**
	 * Sets final result in response object
	 * @param resultResponse
	 * @param succesResult
	 * @param errorResult
	 */
	public static void setResult(ResultResponse resultResponse, BannerCodes succesResult, BannerCodes errorResult) {
		
		if(CollectionUtils.isEmpty(resultResponse.getApiStatus().getErrors())) {
			
			resultResponse.setResult(succesResult.getId(), succesResult.getMsg());
			
		} else {
			
			resultResponse.setResult(errorResult.getId(), errorResult.getMsg());
		}
	}

	/**
	 * 
	 * @param resultResponse
	 * @param succesResult
	 * @param errorResult
	 * @param successMessage
	 */
	public static void setResultWithMessage(ResultResponse resultResponse, BannerCodes succesResult, BannerCodes errorResult, String successMessage, String errorMessage) {
		
		if(CollectionUtils.isEmpty(resultResponse.getApiStatus().getErrors())) {
			
			resultResponse.setResult(succesResult.getId(), 
					!ObjectUtils.isEmpty(successMessage)
					? succesResult.getMsg()+successMessage
					: succesResult.getMsg());
			
		} else {
			
			resultResponse.setResult(errorResult.getId(), !ObjectUtils.isEmpty(errorMessage)
					? errorResult.getMsg()+errorMessage
					: errorResult.getMsg());
		}
	}

}
