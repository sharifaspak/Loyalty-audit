package com.loyalty.marketplace.offers.member.management.outbound.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class OfferBenefit {

	private String offerType;
	private List<CatSubCat> catSubCat; 
}
