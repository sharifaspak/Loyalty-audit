package com.loyalty.marketplace.subscription.inbound.dto;

import com.loyalty.marketplace.subscription.outbound.database.entity.Subscription;
import com.loyalty.marketplace.subscription.outbound.database.entity.SubscriptionCatalog;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class CachedValues {
	
	private boolean isPaymentRequired;
	private boolean isLifeTimeFree;
	private boolean nonExistingMemberFree;
	private boolean extendValidityPeriod;
	private boolean isfreeOffer;
	private boolean isPromoGift;
	private boolean isChannelEligible;
	private String subscriptionAction;
	private SubscriptionCatalog subscribedCatalog;
	private Subscription subscribedSubscription;
	private int leftOverDays;
	private boolean coolingPeriod;
	
	
	public CachedValues(boolean isPaymentRequired, boolean isLifeTimeFree, boolean nonExistingMemberFree, boolean isChannelEligible) {
		super();
		this.isPaymentRequired = isPaymentRequired;
		this.isLifeTimeFree = isLifeTimeFree;
		this.nonExistingMemberFree = nonExistingMemberFree;
		this.isChannelEligible = isChannelEligible;
	}
	
	
}
