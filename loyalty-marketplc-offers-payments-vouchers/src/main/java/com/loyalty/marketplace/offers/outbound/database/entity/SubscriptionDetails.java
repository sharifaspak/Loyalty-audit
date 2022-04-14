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
public class SubscriptionDetails {
	@Field("SubscriptionPromo")
	private Boolean subscPromo;
	@Field("SubscriptionCatalogId")
	private String subscriptionCatalogId;
}