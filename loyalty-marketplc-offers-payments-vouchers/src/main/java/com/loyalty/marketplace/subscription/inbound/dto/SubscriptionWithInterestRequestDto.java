package com.loyalty.marketplace.subscription.inbound.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SubscriptionWithInterestRequestDto {
	
	private boolean callSubscription;
	private boolean includeAccountInterest;
	private List<String> accountNumber;

}
