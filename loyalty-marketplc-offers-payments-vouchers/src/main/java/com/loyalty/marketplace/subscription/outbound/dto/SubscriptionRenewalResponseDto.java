package com.loyalty.marketplace.subscription.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonInclude(Include.NON_NULL)
public class SubscriptionRenewalResponseDto {
	
	private String masterEpgTransactionId;
	private String epgTransactionId;
	private String subscriptionStatus;
	private Double amount;
	private String subscriptionId;
	private String subscriptionCatalogId;
	private String accountNumber;
	private String epgTransactionStatus;
	private String epgResponse;
	private String externalTransactionId;
}
