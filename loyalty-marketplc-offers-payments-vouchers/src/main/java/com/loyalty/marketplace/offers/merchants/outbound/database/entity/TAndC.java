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
public class TAndC {

	@Field("English")
	private String tnCEn;

	@Field("Arabic")
	private String tnCAr;
	
}
