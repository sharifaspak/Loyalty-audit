package com.loyalty.marketplace.offers.outbound.database.entity;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class MarketplaceImageUrl {

	private String availableInChannel;
	private String imageType;
	private String imageUrl;
	private String imageUrlDr;
	private String imageUrlProd;
	
}
