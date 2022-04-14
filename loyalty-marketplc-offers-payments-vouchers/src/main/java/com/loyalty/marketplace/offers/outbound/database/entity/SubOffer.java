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
public class SubOffer {

	@Field("SubOfferId")
	private String subOfferId;
	@Field("SubOfferTitle")
	private SubOfferTitle subOfferTitle;
	@Field("SubOfferDesc")
	private SubOfferDesc subOfferDesc;
	@Field("SubOfferValue")
	private SubOfferValue subOfferValue;
	
}
