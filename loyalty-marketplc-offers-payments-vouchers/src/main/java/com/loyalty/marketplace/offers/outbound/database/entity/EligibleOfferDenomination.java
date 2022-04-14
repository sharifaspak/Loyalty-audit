package com.loyalty.marketplace.offers.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.outbound.database.entity.DenominationDescription;
import com.loyalty.marketplace.outbound.database.entity.DenominationValue;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EligibleOfferDenomination {
	
	@Field("Id")
	private String denominationId;
	
	@Field("Description")
	private DenominationDescription denominationDescription;
	
	@Field("Value")
	private DenominationValue denominationValue;

}
