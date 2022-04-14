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
public class OfferDetailMobile {
	@Field("English")
	private String offerDetailMobileEn;
	@Field("Arabic")
	private String offerDetailMobileAr;

}
