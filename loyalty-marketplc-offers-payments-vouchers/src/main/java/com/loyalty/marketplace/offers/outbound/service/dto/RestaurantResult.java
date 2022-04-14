package com.loyalty.marketplace.offers.outbound.service.dto;

import java.util.List;

import com.loyalty.marketplace.offers.outbound.database.entity.Restaurant;

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
public class RestaurantResult {
	
	private List<Restaurant> results;
	private String error;
	private String message;

}
