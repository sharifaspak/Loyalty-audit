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
public class ValidatePromoCodeRequest {
	
	private String ruleId;
	private String offerId;
	private String offerType;
	private String subscriptionCatalogId;
	private List<String> customerType;
	private List<String> accountNumber;
	private PromoCodeDetails promoCodeDetails;
	private String partner;
	private String partnerRef;
	private int promoDenominationAmount;
}
