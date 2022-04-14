package com.loyalty.marketplace.subscription.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

@JsonInclude(Include.NON_NULL)
public class SubscriptionReportResultResponse extends ResultResponse {

	List<SubscriptionResponseDto> subscription;
	
	public SubscriptionReportResultResponse(String externalTransactionId) {
		super(externalTransactionId);
	}

	public List<SubscriptionResponseDto> getSubscription() {
		return subscription;
	}

	public void setSubscription(List<SubscriptionResponseDto> subscription) {
		this.subscription = subscription;
	}
	
	
}
