package com.loyalty.marketplace.offers.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class FreeOffers {
	@Field("IsMamba")
	private boolean isMamba;
	@Field("IsPromotionalGift")
	private boolean isPromotionalGift;
	@Field("IsPointsRedemptionGift")
	private boolean isPointsRedemptionGift;
}
