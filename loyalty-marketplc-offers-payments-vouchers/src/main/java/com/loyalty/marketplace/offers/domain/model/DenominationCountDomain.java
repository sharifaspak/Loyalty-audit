package com.loyalty.marketplace.offers.domain.model;

import java.util.Date;

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
public class DenominationCountDomain {
	
	private Integer denomination;
	private Integer dailyCount;
	private Integer weeklyCount;
	private Integer monthlyCount;
	private Integer annualCount;
	private Integer totalCount;
	private Date lastPurchased;
	
}
