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
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AmountInfo {
	
	private Double offerCost;
	private Integer offerPoints;
	private Double spentAmount;
	private Integer spentPoints;
	private Double convertedSpentPointsToAmount;
	private Double vatAmount;
	private Double cost;
	private Double purchaseAmount;
	
}
