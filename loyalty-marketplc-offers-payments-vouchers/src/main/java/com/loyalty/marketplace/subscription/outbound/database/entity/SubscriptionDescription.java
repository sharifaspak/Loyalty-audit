package com.loyalty.marketplace.subscription.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SubscriptionDescription {

	@Field("English")
	private String subscriptionDescriptionEn;
	@Field("Arabic")
	private String subscriptionDescriptionAr;
}
