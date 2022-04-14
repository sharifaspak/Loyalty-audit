package com.loyalty.marketplace.offers.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FreeOfferDomain {
	
	private boolean isMamba;
	private boolean isPromotionalGift;
	private boolean isPointsRedemptionGift;

}
