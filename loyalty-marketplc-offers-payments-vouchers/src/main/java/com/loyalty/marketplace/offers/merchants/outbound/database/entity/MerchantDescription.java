package com.loyalty.marketplace.offers.merchants.outbound.database.entity;

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
public class MerchantDescription {

	@Field("English")
	private String merchantDescEn;

	@Field("Arabic")
	private String merchantDescAr;
	
}
