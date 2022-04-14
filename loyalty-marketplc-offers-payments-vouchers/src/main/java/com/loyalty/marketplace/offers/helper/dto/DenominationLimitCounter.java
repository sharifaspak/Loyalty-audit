package com.loyalty.marketplace.offers.helper.dto;

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
public class DenominationLimitCounter {
	
	private Integer denomination;
	private Integer offerDaily;
	private Integer offerWeekly;
	private Integer offerMonthly;
	private Integer offerAnnual;
	private Integer offerTotal;
	private Integer accountDaily;
	private Integer accountWeekly;
	private Integer accountMonthly;
	private Integer accountAnnual;
	private Integer accountTotal;
	private Integer memberDaily;
	private Integer memberWeekly;
	private Integer memberMonthly;
	private Integer memberAnnual;
	private Integer memberTotal;

}
