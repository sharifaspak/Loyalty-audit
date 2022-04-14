package com.loyalty.marketplace.subscription.inbound.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SubscriptionInfoRequestDto {

	private String membershipCode;
	private String primaryAccount;
	private List<String> accountNumber;
	private boolean includeSubscribed;
	private boolean includeBenefits;
	private boolean includeAccountInterest;
}
