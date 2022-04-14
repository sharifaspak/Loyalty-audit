package com.loyalty.marketplace.offers.helper.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LifetimeSavingsHelperDto {
	
	private Double savings;
	private boolean subscriptionStatus;
	private String merchantName;
	private double subscriptionWaiveOff;
	private String pointsTransactionId;

}
