package com.loyalty.marketplace.offers.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalDetailsDomain {
	
	private boolean isBirthdayOffer;
	private boolean isSubscribed;
	private boolean isPromotionApplied;
	private boolean isDod;
	private boolean isFree;
	private boolean isGift;
	private boolean isFeatured;
	private boolean isMamba;
	private boolean isPromotionalGift;
	private boolean isSubscriptionBenefit;
	private String promoGiftId;
	private boolean isPointsRedemptionGift;

}
