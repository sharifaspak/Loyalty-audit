package com.loyalty.marketplace.subscription.helper.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class RenewalValues {
	
	private Date nextRenewalDate;
	private Date lastChargedDate;
	private Double lastChargedAmount;
	
}
