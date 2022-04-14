package com.loyalty.marketplace.subscription.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

@JsonInclude(Include.NON_NULL)
public class SubscriptionCatalogResultResponse extends ResultResponse {
	
	List<SubscriptionCatalogResponseDto> subscriptionCatalog;
		
	public SubscriptionCatalogResultResponse(String externalTransactionId) {
		super(externalTransactionId);
	}

	public List<SubscriptionCatalogResponseDto> getSubscriptionCatalog() {
		return subscriptionCatalog;
	}

	public void setSubscriptionCatalog(List<SubscriptionCatalogResponseDto> subscriptionCatalog) {
		this.subscriptionCatalog = subscriptionCatalog;
	}	
	
}
