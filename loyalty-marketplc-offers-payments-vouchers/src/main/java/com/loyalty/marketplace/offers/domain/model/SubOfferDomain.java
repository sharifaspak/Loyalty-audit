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
public class SubOfferDomain {

	private String subOfferId;
	private SubOfferTitleDomain subOfferTitle;
	private SubOfferDescDomain subOfferDesc;
	private SubOfferValueDomain subOfferValue;

}
