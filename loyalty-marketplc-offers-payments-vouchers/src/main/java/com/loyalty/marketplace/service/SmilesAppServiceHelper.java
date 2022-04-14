package com.loyalty.marketplace.service;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.loyalty.marketplace.service.dto.PaymentReversalResponseDto;

public class SmilesAppServiceHelper {

	SmilesAppServiceHelper() {

	}
	private static final String SUCCESS_RESPONSE_CODE = "0";
	private static final String SUCCESS = "SUCCESS";
	private static final String FAILURE = "FAILURE";
	private static final String NULL_RESPONSE_MSG = "Response message is null";
	
	public static String getRefundStatus(PaymentReversalResponseDto response) {
		return !ObjectUtils.isEmpty(response) && !CollectionUtils.isEmpty(response.getResult()) 
				&& response.getResult().get(0).getResponseCode().equalsIgnoreCase(SUCCESS_RESPONSE_CODE) ? SUCCESS : FAILURE;
	}
	
	public static String getResponseMessage(PaymentReversalResponseDto response) {
		return !ObjectUtils.isEmpty(response) && !CollectionUtils.isEmpty(response.getResult()) 
				&& !ObjectUtils.isEmpty(response.getResult().get(0).getResponseMsg())
				?  response.getResult().get(0).getResponseMsg()
				: NULL_RESPONSE_MSG;
	}
	
	public static String getResponseCode(PaymentReversalResponseDto response) {
		return !ObjectUtils.isEmpty(response) && !CollectionUtils.isEmpty(response.getResult()) 
				&& !ObjectUtils.isEmpty(response.getResult().get(0).getResponseCode())
				?  response.getResult().get(0).getResponseCode()
				: null;
	}
	
	public static String getEpgReversalDesc(PaymentReversalResponseDto response) {
		return !ObjectUtils.isEmpty(response) && !CollectionUtils.isEmpty(response.getResult()) 
				&& !ObjectUtils.isEmpty(response.getResult().get(0).getEpgRerversalDesc())
				?  response.getResult().get(0).getEpgRerversalDesc()
				: null;
	}
	
	public static String getEpgReversalCode(PaymentReversalResponseDto response) {
		return !ObjectUtils.isEmpty(response) && !CollectionUtils.isEmpty(response.getResult()) 
				&& !ObjectUtils.isEmpty(response.getResult().get(0).getEpgRerversalCode())
				?  response.getResult().get(0).getEpgRerversalCode()
				: null;
	}
	
	public static String getEpgRefundCode(PaymentReversalResponseDto response) {
		return !ObjectUtils.isEmpty(response) && !CollectionUtils.isEmpty(response.getResult())
				&& !ObjectUtils.isEmpty(response.getResult().get(0).getEpgRefundCode())
				?  response.getResult().get(0).getEpgRefundCode()
				: null;
	}
	public static String getEpgRefundMsg(PaymentReversalResponseDto response) {
		return !ObjectUtils.isEmpty(response) && !CollectionUtils.isEmpty(response.getResult()) 
				&& !ObjectUtils.isEmpty(response.getResult().get(0).getEpgRefundMsg())
				?  response.getResult().get(0).getEpgRefundMsg()
				: null;
	}
	public static String getErrorMessage(PaymentReversalResponseDto response) {
		return !ObjectUtils.isEmpty(response) && !CollectionUtils.isEmpty(response.getResult()) 
				&& !ObjectUtils.isEmpty(response.getResult().get(0).getErrorMessage())
				?  response.getResult().get(0).getErrorMessage()
				: "NA";
	}
	
	public static Boolean isNotificationTrigger(PaymentReversalResponseDto response) {
		return !ObjectUtils.isEmpty(response) && !CollectionUtils.isEmpty(response.getResult()) 
				?  response.getResult().get(0).getSmsNotificationTrigger()
				: null;
	}
}