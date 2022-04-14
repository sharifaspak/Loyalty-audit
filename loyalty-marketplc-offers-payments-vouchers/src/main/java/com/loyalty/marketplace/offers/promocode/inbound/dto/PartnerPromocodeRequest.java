package com.loyalty.marketplace.offers.promocode.inbound.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class PartnerPromocodeRequest {
	
	private String partnerCode;
	private String partnerRefNo;
	private String subscriptionCatalogId;
	private int promoCodeCount;
	
}
