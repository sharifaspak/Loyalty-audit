package com.loyalty.marketplace.offers.outbound.database.entity;

import java.util.Date;

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
@NoArgsConstructor
@ToString
public class DenominationCount {
	
	@Field("Denomination")
	private Integer denomination;
	
	@Field("DailyCount")
	private Integer dailyCount;
	
	@Field("WeeklyCount")
	private Integer weeklyCount;
	
	@Field("MonthlyCount")
	private Integer monthlyCount;
	
	@Field("AnnualCount")
	private Integer annualCount;
	
	@Field("TotalCount")
	private Integer totalCount;
	
	@Field("LastPurchased")
	private Date lastPurchased;

}
