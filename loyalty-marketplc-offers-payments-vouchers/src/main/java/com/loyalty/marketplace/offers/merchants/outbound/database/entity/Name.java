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
public class Name {

	@Field("English")
	private String english;

	@Field("Arabic")
	private String arabic;
	
}
