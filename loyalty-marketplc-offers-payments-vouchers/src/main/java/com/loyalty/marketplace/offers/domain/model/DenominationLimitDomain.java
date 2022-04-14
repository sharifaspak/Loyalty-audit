package com.loyalty.marketplace.offers.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DenominationLimitDomain {

	private Integer denomination;
	private Integer dailyLimit;
	private Integer weeklyLimit;
	private Integer monthlyLimit;
	private Integer annualLimit;
	private Integer totalLimit;
}
