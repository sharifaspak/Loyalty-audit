package com.loyalty.marketplace.subscription.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

@JsonInclude(Include.NON_NULL)
public class SubscriptionInfoResultResponse extends ResultResponse {
	
	List<SubscriptionInfoResponseDto> subscriptionInfo;

	public SubscriptionInfoResultResponse(String externalTransactionId) {
		super(externalTransactionId);
	}

	public List<SubscriptionInfoResponseDto> getSubscriptionInfo() {
		return subscriptionInfo;
	}

	public void setSubscriptionInfo(List<SubscriptionInfoResponseDto> subscriptionInfo) {
		this.subscriptionInfo = subscriptionInfo;
	}

}
