package com.loyalty.marketplace.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class DenominationDescription {
	
	@Field("English")
	private String denominationDescriptionEn;
	@Field("Arabic")
	private String denominationDescriptionAr;
	
}
