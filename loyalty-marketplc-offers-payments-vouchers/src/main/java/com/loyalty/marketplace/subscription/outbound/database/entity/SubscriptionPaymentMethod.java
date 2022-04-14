package com.loyalty.marketplace.subscription.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "SubscriptionCatalog")
@Setter
@Getter
@ToString
public class SubscriptionPaymentMethod {

	@Field("PaymentMethodId")
	private String paymentMethodId;
	@Field("Description")
	private String description;
}


