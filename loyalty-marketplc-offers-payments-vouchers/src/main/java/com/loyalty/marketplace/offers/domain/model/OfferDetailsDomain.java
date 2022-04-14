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
public class OfferDetailsDomain {
	
	private OfferLabelDomain offerLabel;
	private OfferTitleDomain offerTitle;
	private OfferTitleDescriptionDomain offerTitleDescription;
	private OfferDetailMobileDomain offerMobile;
	private OfferDetailWebDomain offerWeb;
	
	
}
