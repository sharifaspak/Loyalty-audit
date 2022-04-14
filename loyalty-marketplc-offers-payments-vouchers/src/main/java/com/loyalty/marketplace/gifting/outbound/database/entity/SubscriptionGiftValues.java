package com.loyalty.marketplace.gifting.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class SubscriptionGiftValues {

	@Field("SubscriptionCatalog")
	private String subscriptionCatalogId;
}
