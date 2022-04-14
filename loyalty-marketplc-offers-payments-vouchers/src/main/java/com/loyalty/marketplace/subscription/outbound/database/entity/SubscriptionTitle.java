package com.loyalty.marketplace.subscription.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SubscriptionTitle {

	@Field("English")
	private String subscriptionTitleEn;
	@Field("Arabic")
	private String subscriptionTitleAr;
}