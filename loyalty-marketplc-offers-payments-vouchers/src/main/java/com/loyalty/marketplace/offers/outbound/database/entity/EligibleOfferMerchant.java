package com.loyalty.marketplace.offers.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.offers.merchants.outbound.database.entity.MerchantName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class EligibleOfferMerchant {

	@Field("Id")
	private String id;

	@Field("MerchantCode")
	private String merchantCode;

	@Field("Name")
	private MerchantName merchantName;
	
	@Field("Status")
	private String status;
	
}