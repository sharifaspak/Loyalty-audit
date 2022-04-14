package com.loyalty.marketplace.offers.domain.model;

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
public class SubOfferValueDomain {
	
	private Double oldCost;
	private Integer oldPointValue;
	private Double newCost;
	private Integer newPointValue;

}
