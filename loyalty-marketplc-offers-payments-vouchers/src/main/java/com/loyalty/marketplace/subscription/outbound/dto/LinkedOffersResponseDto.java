package com.loyalty.marketplace.subscription.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(Include.NON_NULL)
@Setter
@Getter
@ToString
public class LinkedOffersResponseDto {

	private List<TitleResponseDto> linkedOffer;
	private List<TitleResponseDto> eligibleOfferType;
	private List<TitleResponseDto> eligibleOfferCategory;
	private List<TitleResponseDto> eligibleOfferSubCategory; 
}
