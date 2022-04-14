package com.loyalty.marketplace.subscription.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

@JsonInclude(Include.NON_NULL)
public class SubscriptionWithInterestResultResponse extends ResultResponse {
	
	List<SubscriptionWithInterestResponseDto> subscriptionWithInterest;
			
	public SubscriptionWithInterestResultResponse(String externalTransactionId) {
		super(externalTransactionId);
	}

	public List<SubscriptionWithInterestResponseDto> getSubscriptionWithInterest() {
		return subscriptionWithInterest;
	}

	public void setSubscriptionWithInterest(List<SubscriptionWithInterestResponseDto> subscriptionWithInterest) {
		this.subscriptionWithInterest = subscriptionWithInterest;
	}

	
}
