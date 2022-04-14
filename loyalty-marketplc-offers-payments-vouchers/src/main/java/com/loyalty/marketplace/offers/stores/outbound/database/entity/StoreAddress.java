package com.loyalty.marketplace.offers.stores.outbound.database.entity;

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
public class StoreAddress {

	@Field("English")
	private String addressEn;

	@Field("Arabic")
	private String addressAr;

}
