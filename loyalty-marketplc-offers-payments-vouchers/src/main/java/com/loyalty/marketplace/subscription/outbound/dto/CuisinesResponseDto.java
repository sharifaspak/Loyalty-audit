package com.loyalty.marketplace.subscription.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(Include.NON_NULL)
@Setter
@Getter
@ToString
public class CuisinesResponseDto {
	
	public String cuisinesId;
	public String cuisineNameEn;
	public String cuisineNameAr;
}
