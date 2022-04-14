package com.loyalty.marketplace.subscription.inbound.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ManageSubscriptionRequestDto {

	private String subscriptionId;
	
	private String accountNumber;
	
	private String packageId;
	
	@NotNull(message = "{validation.manageSubscriptionAction.notNull.msg}")
	@NotEmpty(message = "{validation.manageSubscriptionAction.notEmpty.msg}")
	private String action;
	
	private Double lastChargedAmount;
	
	private String lastChargedDate;
	
	private String nextRenewalDate;

	private String cancellationReason;
	
	private boolean allowAction;
}

