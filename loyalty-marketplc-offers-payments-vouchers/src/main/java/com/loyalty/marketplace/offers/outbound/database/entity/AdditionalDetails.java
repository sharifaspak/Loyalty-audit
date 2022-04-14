package com.loyalty.marketplace.offers.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalDetails {
	
	@Field("IsBirthdayOffer")
	private boolean isBirthdayOffer;
	@Field("IsSubscribed")
	private boolean isSubscribed;
	@Field("IsPromotionApplied")
	private boolean isPromotionApplied;
	@Field("IsDod")
	private boolean isDod;
	@Field("IsFree")
	private boolean isFree;
	@Field("IsGift")
	private boolean isGift;
	@Field("IsFeatured")
	private boolean isFeatured;
	@Field("IsMamba")
	private boolean isMamba;
	@Field("IsPromotionalGift")
    private boolean isPromotionalGift;
	@Field("IsSubscriptionBenefit")
    private boolean isSubscriptionBenefit;
	@Field("PromotionalGiftId")
	private String promoGiftId;
	@Field("IsPointsRedemptionGift")
    private boolean isPointsRedemptionGift;
	
}
