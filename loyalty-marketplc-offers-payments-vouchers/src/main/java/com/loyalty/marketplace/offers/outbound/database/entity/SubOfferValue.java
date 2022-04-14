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
public class SubOfferValue {
	
	@Field("OldDirham")
	private Double oldCost;
	@Field("OldPoint")
	private Integer oldPointValue;
	@Field("NewDirham")
	private Double newCost;
	@Field("NewPoint")
	private Integer newPointValue;
	
}
