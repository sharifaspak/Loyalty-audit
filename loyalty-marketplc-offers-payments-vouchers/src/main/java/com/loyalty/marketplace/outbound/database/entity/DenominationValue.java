package com.loyalty.marketplace.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DenominationValue {
	
	@Field("Points")
	private Integer pointValue;
	@Field("Cost")
	private Integer dirhamValue;

}
