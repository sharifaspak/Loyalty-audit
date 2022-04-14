package com.loyalty.marketplace.offers.inbound.dto;

import java.util.List;

import javax.validation.Valid;
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
public class LimitDto {
	
	private String customerSegment;
	
	@Min(value=1, message="{validation.offerCatalogDto.couponQuantity.min.msg}")
	private Integer couponQuantity;
	
	@Min(value=1, message="{validation.offerCatalogDto.downloadLimit.min.msg}")
	private Integer downloadLimit;
	@Min(value=1, message="{validation.offerCatalogDto.dailyLimit.min.msg}")
	private Integer dailyLimit;
	@Min(value=1, message="{validation.offerCatalogDto.weeklyLimit.min.msg}")
	private Integer weeklyLimit;
	@Min(value=1, message="{validation.offerCatalogDto.monthlyLimit.min.msg}")
	private Integer monthlyLimit;
	@Min(value=1, message="{validation.offerCatalogDto.annualLimit.min.msg}")
	private Integer annualLimit;
	
	@Valid
	private List<DenominationLimitDto> denominationLimit;
	
	@Min(value=1, message="{validation.offerCatalogDto.accountDailyLimit.min.msg}")
	private Integer accountDailyLimit;
	
	@Min(value=1, message="{validation.offerCatalogDto.accountWeeklyLimit.min.msg}")
	private Integer accountWeeklyLimit;
	
	@Min(value=1, message="{validation.offerCatalogDto.accountMonthlyLimit.min.msg}")
	private Integer accountMonthlyLimit;
	
	@Min(value=1, message="{validation.offerCatalogDto.accountAnnualLimit.min.msg}")
	private Integer accountAnnualLimit;
	
	@Min(value=1, message="{validation.offerCatalogDto.accountTotalLimit.min.msg}")
	private Integer accountTotalLimit;
	
	@Valid
	private List<DenominationLimitDto> accountDenominationLimit;
	
	@Min(value=1, message="{validation.offerCatalogDto.memberDailyLimit.min.msg}")
	private Integer memberDailyLimit;
	
	@Min(value=1, message="{validation.offerCatalogDto.memberWeeklyLimit.min.msg}")
	private Integer memberWeeklyLimit;
	
	@Min(value=1, message="{validation.offerCatalogDto.memberMonthlyLimit.min.msg}")
	private Integer memberMonthlyLimit;
	
	@Min(value=1, message="{validation.offerCatalogDto.memberAnnualLimit.min.msg}")
	private Integer memberAnnualLimit;
	
	@Min(value=1, message="{validation.offerCatalogDto.memberTotalLimit.min.msg}")
	private Integer memberTotalLimit;
	
	@Valid
	private List<DenominationLimitDto> memberDenominationLimit;

}
