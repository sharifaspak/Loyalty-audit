package com.loyalty.marketplace.offers.points.bank.outbound.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OverallSavings {

	private double lifetimeSavings;
	private double bogoSavings;
	private double foodSavings;
	private double subscriptionWaiver;
}
