package com.loyalty.marketplace.offers.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
public class DenominationLimit {
	
	@Field("Denomination")
	private Integer denomination;
	@Field("DenominationDailyLimit")
	private Integer dailyLimit;
	@Field("DenominationWeeklyLimit")
	private Integer weeklyLimit;
	@Field("DenominationMonthlyLimit")
	private Integer monthlyLimit;
	@Field("DenominationAnnualLimit")
	private Integer annualLimit;
	@Field("DenominationTotalLimit")
	private Integer totalLimit;

}
