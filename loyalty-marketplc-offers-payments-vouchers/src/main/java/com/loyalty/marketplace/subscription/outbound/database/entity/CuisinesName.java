package com.loyalty.marketplace.subscription.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CuisinesName {
	
	@Field("English")
	private String cuisineNameEn;
	@Field("Arabic")
	private String cuisineNameAr;

}
