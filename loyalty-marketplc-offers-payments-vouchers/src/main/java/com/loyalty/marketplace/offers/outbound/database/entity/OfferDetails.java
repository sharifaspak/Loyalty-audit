package com.loyalty.marketplace.offers.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

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
public class OfferDetails {
	
	@Field("Label")
	private OfferLabel offerLabel;
	@Field("Title")
	private OfferTitle offerTitle;
	@Field("Description")
	private OfferTitleDescription offerTitleDescription;
	@Field("Mobile")
	private OfferDetailMobile offerMobile;
	@Field("Web")
	private OfferDetailWeb offerWeb;
	
	
}
