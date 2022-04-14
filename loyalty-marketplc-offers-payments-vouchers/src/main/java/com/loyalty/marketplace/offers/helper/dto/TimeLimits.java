package com.loyalty.marketplace.offers.helper.dto;

import java.util.Date;

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
public class TimeLimits {
	 private Integer dailyCount;
	 private Integer weeklyCount;
	 private Integer monthlyCount;
	 private Integer annualCount;
	 private Date lastPurchased;
}
     
