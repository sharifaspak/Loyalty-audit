package com.loyalty.marketplace.offers.inbound.dto;

import javax.validation.constraints.Min;

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
public class DenominationLimitDto {
	
	private Integer denomination;
	
	@Min(value=1, message="{validation.denominationLimitDto.dailyLimit.min.msg}")
	private Integer dailyLimit;
	@Min(value=1, message="{validation.denominationLimitDto.weeklyLimit.min.msg}")
	private Integer weeklyLimit;
	@Min(value=1, message="{validation.denominationLimitDto.monthlyLimit.min.msg}")
	private Integer monthlyLimit;
	@Min(value=1, message="{validation.denominationLimitDto.annualLimit.min.msg}")
	private Integer annualLimit;
	@Min(value=1, message="{validation.denominationLimitDto.totalLimit.min.msg}")
	private Integer totalLimit;

}
