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
public class OfferLabel {
	
	@Field("English")
	private String offerLabelEn;
	@Field("Arabic")
	private String offerLabelAr;

}
