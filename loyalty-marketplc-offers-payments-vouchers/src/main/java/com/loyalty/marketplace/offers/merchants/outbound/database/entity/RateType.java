package com.loyalty.marketplace.offers.merchants.outbound.database.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document(collection = "DiscountBillingRate")
public class RateType {

	@Id
	private String typeId;
	
	@Indexed
	private String typeRate;
	
	private String typeRateDesc;
	private String type;
	
}
