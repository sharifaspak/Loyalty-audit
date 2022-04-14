package com.loyalty.marketplace.offers.promocode.outbound.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PromoCodeDMRequest {
	
	private String offerId;
	private String offerType;
	private String customerType;
	private String subscriptionCatalogId;
	private String promoCode;
	private String accountNumber;
	private Integer denominationAmount;
	private PromoCodeResult promoCodeResult;

}
