package com.loyalty.marketplace.subscription.inbound.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NotificationValues {
	
	private boolean notifyOneTimeSubscription;
	private boolean notifyAutoRenewalSubscription;
	private String accountNumber;
	private String promoCode;
	private String paymentType;
	private String cardNumber;
	private Double spentPoints;
	private String chargebilityType;
	private String actionType;
	private String nextRenewalDate;
	private String uiLanguage;
}
