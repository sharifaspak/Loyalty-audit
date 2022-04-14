package com.loyalty.marketplace.offers.helper.dto;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@ToString
public class PurchaseCount {
	
	
	private Integer globalWeekly;
	private Integer accountWeekly;
	private Integer memberWeekly;
	
}
