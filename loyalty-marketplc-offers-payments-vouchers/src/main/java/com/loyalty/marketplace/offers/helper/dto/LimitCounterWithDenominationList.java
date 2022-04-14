package com.loyalty.marketplace.offers.helper.dto;

import java.util.List;

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
public class LimitCounterWithDenominationList {
	
	private String offerId;
	private Integer offerDaily;
	private Integer offerWeekly;
	private Integer offerMonthly;
	private Integer offerAnnual;
	private Integer offerTotal;
	private Integer accountOfferDaily;
	private Integer accountOfferWeekly;
	private Integer accountOfferMonthly;
	private Integer accountOfferAnnual;
	private Integer accountOfferTotal;
	private Integer memberOfferDaily;
	private Integer memberOfferWeekly;
	private Integer memberOfferMonthly;
	private Integer memberOfferAnnual;
	private Integer memberOfferTotal;
	private List<DenominationLimitCounter> denominationList;
}
