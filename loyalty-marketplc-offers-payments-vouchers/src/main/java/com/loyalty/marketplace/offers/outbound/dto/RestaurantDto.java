package com.loyalty.marketplace.offers.outbound.dto;

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
public class RestaurantDto {
	
	private String restaurantNameEng;
	private String restaurantNameAr;
	private String type;

}
