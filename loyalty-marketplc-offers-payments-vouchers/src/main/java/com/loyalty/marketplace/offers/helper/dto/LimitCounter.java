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
@Setter
@Getter
@NoArgsConstructor
@ToString
public class LimitCounter {
	
	private String offerId;
	private Integer couponQuantity;
	private Integer offerDaily;
	private Integer offerWeekly;
	private Integer offerMonthly;
	private Integer offerAnnual;
	private Integer offerTotal;
	private Integer offerDenominationDaily;
	private Integer offerDenominationWeekly;
	private Integer offerDenominationMonthly;
	private Integer offerDenominationAnnual;
	private Integer offerDenominationTotal;
	private Integer accountOfferDaily;
	private Integer accountOfferWeekly;
	private Integer accountOfferMonthly;
	private Integer accountOfferAnnual;
	private Integer accountOfferTotal;
	private Integer accountDenominationDaily;
	private Integer accountDenominationWeekly;
	private Integer accountDenominationMonthly;
	private Integer accountDenominationAnnual;
	private Integer accountDenominationTotal;
	private Integer memberOfferDaily;
	private Integer memberOfferWeekly;
	private Integer memberOfferMonthly;
	private Integer memberOfferAnnual;
	private Integer memberOfferTotal;
	private Integer memberDenominationDaily;
	private Integer memberDenominationWeekly;
	private Integer memberDenominationMonthly;
	private Integer memberDenominationAnnual;
	private Integer memberDenominationTotal;
	
}
