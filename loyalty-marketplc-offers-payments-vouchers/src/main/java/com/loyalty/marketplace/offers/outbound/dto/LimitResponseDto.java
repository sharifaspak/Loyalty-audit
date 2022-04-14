package com.loyalty.marketplace.offers.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class LimitResponseDto {
	
	private String customerSegment;
	private Integer couponQuantity;
	private Integer downloadLimit;
	private Integer dailyLimit;
	private Integer weeklyLimit;
	private Integer monthlyLimit;
	private Integer annualLimit;
	private List<DenominationLimitDto> denominationLimit;
	private Integer accountDailyLimit;
	private Integer accountWeeklyLimit;
	private Integer accountMonthlyLimit;
	private Integer accountAnnualLimit;
	private Integer accountTotalLimit;
	private List<DenominationLimitDto> accountDenominationLimit;
	private Integer memberDailyLimit;
	private Integer memberWeeklyLimit;
	private Integer memberMonthlyLimit;
	private Integer memberAnnualLimit;
	private Integer memberTotalLimit;
	private List<DenominationLimitDto> memberDenominationLimit;

}
