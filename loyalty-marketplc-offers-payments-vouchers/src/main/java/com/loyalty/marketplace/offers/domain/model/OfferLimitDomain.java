package com.loyalty.marketplace.offers.domain.model;

import java.util.List;

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
public class OfferLimitDomain {
	
	private String customerSegment;
	private Integer couponQuantity;
	private Integer downloadLimit;
	private Integer dailyLimit;
	private Integer weeklyLimit;
	private Integer monthlyLimit;
	private Integer annualLimit;
	private List<DenominationLimitDomain> denominationLimit;
	private Integer accountDailyLimit;
	private Integer accountWeeklyLimit;
	private Integer accountMonthlyLimit;
	private Integer accountAnnualLimit;
	private Integer accountTotalLimit;
	private List<DenominationLimitDomain> accountDenominationLimit;
	private Integer memberDailyLimit;
	private Integer memberWeeklyLimit;
	private Integer memberMonthlyLimit;
	private Integer memberAnnualLimit;
	private Integer memberTotalLimit;
	private List<DenominationLimitDomain> memberDenominationLimit;
	
}
