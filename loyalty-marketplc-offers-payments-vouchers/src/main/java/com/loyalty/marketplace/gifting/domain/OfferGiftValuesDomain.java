package com.loyalty.marketplace.gifting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OfferGiftValuesDomain {
	
	private String offerId;
	private String offerType;
	private String subOfferId;
	private Integer denomination;
	private Integer couponQuantity;

}
