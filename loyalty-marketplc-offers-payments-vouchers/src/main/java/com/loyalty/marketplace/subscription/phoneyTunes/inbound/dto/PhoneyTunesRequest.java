package com.loyalty.marketplace.subscription.phoneyTunes.inbound.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PhoneyTunesRequest {

	private String accountNumber;
	private int freeDuration;
	private String action;
	private String chargeabilityType;
	private String packageId;
	private String packagePrice;
	private String externalTransactionId;
}
