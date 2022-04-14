package com.loyalty.marketplace.offers.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class Restaurant {
	
	@Field("RestaurantNameEn")
	private String restaurantNameEn;
	@Field("RestaurantNameAr")
	private String restaurantNameAr;
	@Field("Type")
	private String type;

}
