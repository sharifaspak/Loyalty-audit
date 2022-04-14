package com.loyalty.marketplace.offers.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author jaya.shukla
 *
 */
@Setter
@Getter
@NoArgsConstructor
public class OfferTitleDescription {
	@Field("English")
	private String offerTitleDescriptionEn;
	@Field("Arabic")
	private String offerTitleDescriptionAr;

}
