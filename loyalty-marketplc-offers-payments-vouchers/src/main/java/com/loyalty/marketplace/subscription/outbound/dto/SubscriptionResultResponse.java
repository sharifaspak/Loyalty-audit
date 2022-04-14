package com.loyalty.marketplace.subscription.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

@JsonInclude(Include.NON_NULL)
public class SubscriptionResultResponse extends ResultResponse {
	
	Double bogoSavings;
	Double foodSavings;
	List<SubscriptionResponseDto> subscription;
	List<SubscriptionCatalogResponseDto> subscribedSubscriptionCatalog;
	List<SubscriptionCatalogResponseDto> eligibleSubscriptionCatalog;
	List<SubscriptionResponseDto> passiveSubscription;
	List<SubscriptionCatalogResponseDto> passiveSubscriptionCatalog;
		
	public SubscriptionResultResponse(String externalTransactionId) {
		super(externalTransactionId);
	}
	
	public Double getBogoSavings() {
		return bogoSavings;
	}

	public void setBogoSavings(Double bogoSavings) {
		this.bogoSavings = bogoSavings;
	}

	public Double getFoodSavings() {
		return foodSavings;
	}

	public void setFoodSavings(Double foodSavings) {
		this.foodSavings = foodSavings;
	}

	public List<SubscriptionResponseDto> getSubscription() {
		return subscription;
	}

	public void setSubscription(List<SubscriptionResponseDto> subscription) {
		this.subscription = subscription;
	}

	public List<SubscriptionCatalogResponseDto> getSubscribedSubscriptionCatalog() {
		return subscribedSubscriptionCatalog;
	}

	public void setSubscribedSubscriptionCatalog(List<SubscriptionCatalogResponseDto> subscribedSubscriptionCatalog) {
		this.subscribedSubscriptionCatalog = subscribedSubscriptionCatalog;
	}

	public List<SubscriptionCatalogResponseDto> getEligibleSubscriptionCatalog() {
		return eligibleSubscriptionCatalog;
	}

	public void setEligibleSubscriptionCatalog(List<SubscriptionCatalogResponseDto> eligibleSubscriptionCatalog) {
		this.eligibleSubscriptionCatalog = eligibleSubscriptionCatalog;
	}
	
	public List<SubscriptionResponseDto> getPassiveSubscription() {
		return passiveSubscription;
	}
	
	public void setPassiveSubscription(List<SubscriptionResponseDto> passiveSubscription) {
		this.passiveSubscription = passiveSubscription;
	}
	
	public List<SubscriptionCatalogResponseDto> getPassiveSubscriptionCatalog() {
		return passiveSubscriptionCatalog;
	}
	
	public void setPassiveSubscriptionCatalog(List<SubscriptionCatalogResponseDto> passiveSubscriptionCatalog) {
		this.passiveSubscriptionCatalog = passiveSubscriptionCatalog;
	}

	
	
}
